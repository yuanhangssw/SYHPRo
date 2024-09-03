package com.tianji.dam.thread;

import com.alibaba.fastjson.JSONArray;
import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.bean.GlobCache;
import com.tianji.dam.bean.JtsRTree;
import com.tianji.dam.bean.SMSBean;
import com.tianji.dam.domain.*;
import com.tianji.dam.domain.vo.DamsJtsTreeVo;
import com.tianji.dam.domain.vo.RollingDataListVo;
import com.tianji.dam.mapper.*;
import com.tianji.dam.utils.MLTSMSutils;
import com.tianji.dam.utils.RedisUtil;
import com.tj.common.utils.DateUtils;
import com.tj.common.utils.StringUtils;
import com.tj.common.utils.uuid.UUID;
import com.vividsolutions.jts.algorithm.PointLocator;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/* 就是单纯的对实时数据进行匹配入库 【不处理其他业务】
 * 单线程：从redis List中弹出实时数据*/
@Slf4j
public class RedisRightPopRealTaskBlocking extends Observable implements Runnable {

    /*判断数据是否有*/
    Queue<Integer> queue = new LinkedList<>();

    final String REAL = "_real";
    private TSpeedWarmingMapper speedmapper;
    private TWarningUserMapper warningUserMapper;
    private T1Mapper t1Mapper;
    private List<Car> carList;
    private  TaskCarRecordMapper recordMapper;
    private  TDamsconstructionMapper damsconstructionMapper;
    private SmsSendRecordMapper smsrecordMapper;

     private int cartype;

    public int getCartype() {
        return cartype;
    }

    public void setCartype(int cartype) {
        this.cartype = cartype;
    }

    Map<String, List<RollingData>> t1listmap = new ConcurrentHashMap<>();
    Map<String, Integer> keepmap = new HashMap<>();
    Integer nodatacount = 0;

    @Override
    public void run() {
        keepmap.put(GlobCache.cartableprfix[1], 0);


        speedmapper = BeanContext.getApplicationContext().getBean(TSpeedWarmingMapper.class);
        warningUserMapper = BeanContext.getApplicationContext().getBean(TWarningUserMapper.class);
        RedisUtil redisUtil = BeanContext.getApplicationContext().getBean(RedisUtil.class);
        CarMapper carMapper = BeanContext.getApplicationContext().getBean(CarMapper.class);
        t1Mapper = BeanContext.getApplicationContext().getBean(T1Mapper.class);
        recordMapper= BeanContext.getApplicationContext().getBean(TaskCarRecordMapper.class);
        damsconstructionMapper = BeanContext.getApplicationContext().getBean(TDamsconstructionMapper.class);
        smsrecordMapper = BeanContext.getApplicationContext().getBean(SmsSendRecordMapper.class);
        carList = carMapper.findCar();
        /*初始化*/
        for (int i = 0; i < 20; i++) {
            queue.offer(0);
        }
            log.info(cartype+"实时数据进行匹配入库线程启动......");
            while (true) {
                try {
                for (Car car : carList) {
                    if(car.getType()!=cartype){
                        continue;
                    }
                    int type = car.getType();
                    String cartableprfix = GlobCache.cartableprfix[type];
                    String key = REAL + car.getCarID().toString() + REAL;
                    RollingData rollingData = (RollingData) redisUtil.rightPop(key);  //key：车id
                    if(null==rollingData){
                        nodatacount++;
                        continue;
                    }
                         datasave(rollingData, cartableprfix,  car);

                }
                //连续50次无数据时 将睡眠5秒
                if (nodatacount > 100) {
                    log.info("类型"+cartype+"实时数据入库线程暂无数据进入,,,,睡眠10秒钟...........a");

                    for (String s : t1listmap.keySet()) {
                        List<RollingData> value = t1listmap.get(s);
                            if (null == value || value.size() == 0) {
                                continue;
                            }
                            try {
                                RollingDataListVo savevo = new RollingDataListVo();
                                System.out.println("实时数据向" + s + "t1表插入" + value.size() + "条");
                                savevo.setTableName(s + "_t_1");
                                savevo.setDataList(value);
                                t1Mapper.insertrollingdatabatch(savevo);
                            } catch (Exception ignored) {
                            }
                            value.clear();
                    }
                    Thread.sleep(5 * 1000L);
                    nodatacount = 0;
                }
                     Thread.sleep(50);
                } catch (Exception e) {

                    log.info("出现异常");
                    e.printStackTrace();
                    setChanged();
                    notifyObservers();
                    break;
                }
            }

    }

    /*

     */
    public void speedwarning(RollingData rollingData, double currentspeed, double normalspeed, Integer cangid, String cangname,Car car) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TSpeedWarming temp = new TSpeedWarming();

        Date d = new Date();
        d.setTime(rollingData.getTimestamp());
        temp.setCarid(Long.valueOf(rollingData.getVehicleID()));
        temp.setCarname(car.getRemark());
        temp.setDatatime(sdf.format(d));
        temp.setCurrentvalue(new BigDecimal(currentspeed).setScale(2, RoundingMode.HALF_UP));
        temp.setNormalvalue(new BigDecimal(normalspeed).setScale(2, RoundingMode.HALF_UP));
        temp.setDamgid(Long.valueOf(cangid));
        temp.setDamtitle(cangname);
        temp.setGid(UUID.randomUUID().toString().replace("-", ""));
        temp.setCreattime(DateUtils.getTime());
        temp.setFreedom1("1");
        speedmapper.insertTSpeedWarming(temp);
        List<TWarningUser> users = warningUserMapper.selectuserlist();
        if(GlobCache.carspeedwarning.containsKey(car.getRemark())){

            if(rollingData.getTimestamp() -GlobCache.carspeedtime>10000){
                GlobCache.carspeedwarning.put(car.getRemark(),0);
                GlobCache.carspeedtime = rollingData.getTimestamp();
            }
            Integer  speedcount = GlobCache.carspeedwarning.get(car.getRemark());
          //  System.out.println(sdf.format(d)+">>"+firstcar.get().getRemark()+"第"+speedcount+"次超速");
            if(speedcount.intValue()>20){
                for (TWarningUser user : users) {
                    if (null != user.getTel() && !"".equals(user.getTel())) {

                        savesendrecord(currentspeed, normalspeed, cangid, car, user);

                        SMSBean sm = new SMSBean();
                        sm.setUsertel(user.getTel());
                        sm.setUsername(user.getName());
                        sm.setTime(DateUtils.getTime());
                        sm.setSitename("观音水库工程");
                        sm.setDevicename(car.getRemark());
                        MLTSMSutils.send_speed(sm);

                    }
                }
                GlobCache.carspeedwarning.remove(car.getRemark());
            }
            else{
                GlobCache.carspeedwarning.put(car.getRemark(),1+speedcount.intValue());
            }
        }else{
            GlobCache.carspeedwarning.put(car.getRemark(),1);
        }
    }

    /**
     *
     * @param currentspeed  当前速度
     * @param normalspeed   正常速度
     * @param cangid
     * @param car   车辆
     * @param user 用户
     */
    private void savesendrecord(double currentspeed, double normalspeed, Integer cangid, Car car, TWarningUser user) {
        SmsSendRecord record =new SmsSendRecord();
        record.setCarName(car.getRemark());
        record.setCurrentValue(new BigDecimal(currentspeed).setScale(2, RoundingMode.HALF_UP));
        record.setNormalValue(new BigDecimal(normalspeed).setScale(2, RoundingMode.HALF_UP));
        record.setDamGid(cangid.longValue());
        record.setSmsTel(user.getTel());
        record.setTelUser(user.getName());
        smsrecordMapper.insertSmsSendRecord(record);
    }

    public synchronized void datasave(RollingData rollingData, String cartableprfix,Car car) throws InterruptedException {


        if (!keepmap.containsKey(cartableprfix)) {
            keepmap.put(cartableprfix, 1);
        }

        int keep = keepmap.get(cartableprfix);
        //todo:先将redis里的

        if (StringUtils.isNotNull(rollingData)) {

            //有数据-k归零
            keep = 0;
            nodatacount = 0;
            int orderNum = rollingData.getOrderNum();//轨迹点序列号 用于防止数据丢失
            if (!queue.contains(orderNum)) {
                String newTableName;

                // todo: 为0时推送 ishistory;//是否为实时 0:实时 1:实时
                //0.拼接数据库名称
                TableMapper tableMapper = BeanContext.getApplicationContext().getBean(TableMapper.class);
                //通过R树查找单元
                int j = -1;
                //任务数据插入任务表
                List<DamsConstruction> newCang = new LinkedList<>();
                TaskCarRecord recordquery = new TaskCarRecord();
                recordquery.setStatus(0L);
                recordquery.setCarId(Long.parseLong(rollingData.getVehicleID()));
                List<TaskCarRecord> allrecrord = recordMapper.selectTaskCarRecordList(recordquery);
                DamsConstruction damsConstruction = null;


                if (allrecrord.size() > 0) {
                    TaskCarRecord temp = allrecrord.get(0);
                    Integer cangid = temp.getTaskId().intValue();
                    damsConstruction = damsconstructionMapper.selectByPrimaryKey(cangid);
                    Float currentspeed = rollingData.getSpeed();
                    Double normalspeed =damsConstruction.getSpeed();
                    if (currentspeed > normalspeed) {
                        try {
                            speedwarning(rollingData, currentspeed, normalspeed, damsConstruction.getId(),damsConstruction.getTitle(),car);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else{
                        GlobCache.carspeedwarning.put(car.getRemark()+"",0);
                    }

                }
                DamsConstruction vo = damsConstruction;
                newTableName = vo.getTablename();
                newTableName = cartableprfix + "_" + newTableName;
                try {
                   // System.out.println("向" + newTableName + "插入数据：");
                    tableMapper.insertRollingData(newTableName, rollingData);//写入数据
                } catch (Exception e) {

                }
                if (!t1listmap.containsKey(cartableprfix)) {
                    t1listmap.put(cartableprfix, new ArrayList<>());
                }
             //   System.out.println("车辆："+rollingData.getVehicleID()+"添加数据："+rollingData.getCoordX()+">"+rollingData.getCoordY());
                t1listmap.get(cartableprfix).add(rollingData);

                if (t1listmap.get(cartableprfix).size() > 20) {
                    try {
                        RollingDataListVo savevo = new RollingDataListVo();
                        System.out.println("实时数据向" + cartableprfix + "t1表插入20条");
                        savevo.setTableName(cartableprfix + "_t_1");
                        savevo.setDataList(t1listmap.get(cartableprfix));
                        t1Mapper.insertrollingdatabatch(savevo);
                    } catch (Exception e) {
                        System.out.println("实时批量插入数据出错。。");
                    }

                    t1listmap.get(cartableprfix).clear();
                }

               // 删除对应车辆数据
                  tableMapper.deleteNewRollingData(rollingData.getVehicleID());
              //  4.插入实时数据
                rollingData.setTablename(newTableName);
                // rollingData.setSubid(subId);
                 tableMapper.insertNewRollingData("t_new_data", rollingData);
            } else {
                /*如果包含*/
                /*删除第一个*/
                queue.poll();
                /*在最后增加一个*/
                queue.offer(orderNum);
            }
            Thread.sleep(10);
        } else {
            keep++;
            nodatacount++;
            if (keep > 10) {
                for (String s : t1listmap.keySet()) {


                    try {
                        List<RollingData> t1list = t1listmap.get(s);
                        if (t1list.size() > 0) {
                            RollingDataListVo savevo = new RollingDataListVo();
                            System.out.println("实时数据向" + cartableprfix + "_t_1表插入" + keep + "条");
                            savevo.setTableName(cartableprfix + "_t_1");
                            savevo.setDataList(t1listmap.get(cartableprfix));
                            t1Mapper.insertrollingdatabatch(savevo);

                        }
                    } catch (Exception e) {
                        System.out.println("实时插入数据出错。");
                    }
                    t1listmap.get(cartableprfix).clear();
                }
                keep = 0;
            }

            Thread.sleep(10);
        }
        keepmap.put(cartableprfix, keep);


    }

}
