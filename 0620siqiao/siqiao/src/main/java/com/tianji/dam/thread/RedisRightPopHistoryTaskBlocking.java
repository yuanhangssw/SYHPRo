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

/* 就是单纯的对历史数据进行匹配入库 【不处理其他业务】
 * 单线程：从redis List中弹出历史数据*/
@Slf4j
public class RedisRightPopHistoryTaskBlocking extends Observable implements Runnable {

    /*判断数据是否有*/
    Queue<Integer> queue = new LinkedList<Integer>();

    final String HISTORY = "_history";

    private TSpeedWarmingMapper speedmapper;
    private TWarningUserMapper warningUserMapper;
    private CarMapper carMapper;
    private T1Mapper t1Mapper;
    private RedisUtil redisUtil;
    private List<Car> carList;
    Map<String, List<RollingData>> t1listmap = new HashMap<>();
    Map<String, Integer> keepmap = new HashMap<>();
    Integer nodatacount = 0;
    TDamsconstructionMapper damsConstructionMapper;
    TableMapper tableMapper;

    @Override
    public void run() {
        carMapper = BeanContext.getApplicationContext().getBean(CarMapper.class);
        warningUserMapper = BeanContext.getApplicationContext().getBean(TWarningUserMapper.class);
        speedmapper = BeanContext.getApplicationContext().getBean(TSpeedWarmingMapper.class);
        redisUtil = BeanContext.getApplicationContext().getBean(RedisUtil.class);
        carMapper = BeanContext.getApplicationContext().getBean(CarMapper.class);
        t1Mapper = BeanContext.getApplicationContext().getBean(T1Mapper.class);
        damsConstructionMapper = BeanContext.getApplicationContext().getBean(TDamsconstructionMapper.class);
        tableMapper = BeanContext.getApplicationContext().getBean(TableMapper.class);



        /*初始化*/
        for (int i = 0; i < 20; i++) {
            queue.offer(0);
        }
        try {
            log.info("历史数据进行匹配入库线程启动................................................................");

            carList = carMapper.findCar();
            Map<String, List<RollingData>> tablesavemaps = new HashMap<>();
            while (true) {
                for (Car car : carList) {
                    int type = car.getType();
                    String cartableprfix = GlobCache.cartableprfix[type];
                    if (!keepmap.containsKey(cartableprfix)) {
                        keepmap.put(cartableprfix, 0);
                    }

                    String key = HISTORY + car.getCarID().toString() + HISTORY;
                    RollingData rollingData = (RollingData) redisUtil.rightPop(key);  //key：车id
                    if (null == rollingData) {
                        nodatacount++;
                        continue;
                    }

                    datasave(rollingData, cartableprfix);


                }
                //连续50次无数据时 将睡眠5秒
                if (nodatacount > 100) {
                    log.info("历史数据通道暂无数据进入,,,,睡眠10秒钟...........a");

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
                        } catch (Exception e) {

                        }
                        value.clear();

                    }
                    Thread.sleep(5 * 1000L);
                    nodatacount = 0;


                }

                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("出现异常");
            log.info(e.toString());
            setChanged();
            notifyObservers();
        }
    }

    /**
     * 超速处理、记录+短信
     *
     * @param rollingData
     * @param currentspeed
     * @param normalspeed
     * @param cangid
     * @param cangname
     */
    public void speedwarning(RollingData rollingData, double currentspeed, double normalspeed, Integer cangid, String cangname) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TSpeedWarming temp = new TSpeedWarming();

        Optional<Car> firstcar = carList.stream().filter(car -> car.getCarID().toString().equals(rollingData.getVehicleID())).findFirst();

        Date d = new Date();
        d.setTime(rollingData.getTimestamp());
        temp.setCarid(Long.valueOf(rollingData.getVehicleID()));
        temp.setDatatime(sdf.format(d));
        temp.setCurrentvalue(new BigDecimal(currentspeed).setScale(2, RoundingMode.HALF_UP));
        temp.setNormalvalue(new BigDecimal(normalspeed).setScale(2, RoundingMode.HALF_UP));
        temp.setDamgid(Long.valueOf(cangid));
        temp.setDamtitle(cangname);
        temp.setGid(UUID.randomUUID().toString().replace("-", ""));
        temp.setCreattime(DateUtils.getTime());
        temp.setFreedom1("1");
        temp.setFreedom3("1");
        speedmapper.insertTSpeedWarming(temp);
        List<TWarningUser> users = warningUserMapper.selectTWarningUserList(null);
        for (TWarningUser user : users) {
            if (null != user.getTel() && !"".equals(user.getTel())) {
                SMSBean sm = new SMSBean();
                sm.setUsertel(user.getTel());
                sm.setUsername(user.getName());
                sm.setTime(DateUtils.getTime());
                sm.setDatagid(temp.getGid());
                sm.setWarning("车辆" + firstcar.get().getRemark() + "，有超速行驶。");
                //  SMSutils.send(sm);
            }

        }
    }


    public int datasave(RollingData rollingData, String cartableprfix) {

        int keep = keepmap.get(cartableprfix);
        //todo:先将redis里的

        if (StringUtils.isNotNull(rollingData)) {
            //有数据-k归零
            keep = 0;
            nodatacount = 0;
            int orderNum = rollingData.getOrderNum();//轨迹点序列号 用于防止数据丢失
            if (!queue.contains(orderNum)) {
                String newTableName = "";
                Integer subId = 0;
                // todo: 为0时推送 ishistory;//是否为实时 0:实时 1:实时

                //通过R树查找单元
                int j = -1;
                List<DamsJtsTreeVo> treeVos = JtsRTree.query(rollingData.getZhuangX(), rollingData.getZhuangY());
                List<DamsConstruction> newCang = new LinkedList<>();
                if (!treeVos.isEmpty()) {
                    for (int i = 0; i < treeVos.size(); i++) {
                        DamsJtsTreeVo vo = treeVos.get(i);
                        DamsConstruction damsConstruction;
                        //0.获得工作仓的范围
                        if (!GlobCache.daminfomap.containsKey(vo.getId())) {

                            damsConstruction = damsConstructionMapper.selectByPrimaryKey(vo.getId());
                        } else {
                            damsConstruction = GlobCache.daminfomap.get(vo.getId());
                        }
                        PointLocator a = new PointLocator();
                        Coordinate point = new Coordinate();
                        point.x = rollingData.getZhuangX();//桩x
                        point.y = rollingData.getZhuangY();//桩y
                        point.z = rollingData.getElevation();//高程
                        GeometryFactory geometryFactory = new GeometryFactory();
                        List<Coordinate> list = JSONArray.parseArray(damsConstruction.getRanges(), Coordinate.class);
                        Coordinate[] array = new Coordinate[list.size() + 1];  //最重要，不能遗漏，预先申请好数组空间
                        list.toArray(array);
                        array[list.size()] = array[0];
                        Geometry bg = geometryFactory.createPolygon(array);
                        boolean p1 = a.intersects(point, bg);
                        if (p1) {//判断是否在边界内

                            if (damsConstruction.getStatus().intValue() == 8) {//对已开仓的仓位进行匹配
                                Long starttime = (new SimpleDateFormat("yyyy-MM-dd")).parse(damsConstruction.getActualstarttime(), new ParsePosition(0)).getTime();
                                if (rollingData.getTimestamp().longValue() >= starttime.longValue()) {//数据的timestamp 大于等于仓位的 actualstarttime实际开始施工时间
                                    newCang.add(damsConstruction);//把已匹配的仓位加入新的集合中
                                }
                            }
                        }
                    }
                    if (StringUtils.isNotEmpty(newCang)) {//如果匹配到仓位
                        if (newCang.size() >= 1) {//并且多个仓位时
                            for (int i = 0; i < newCang.size(); i++) {
                                Long storehouseEndTime = (new SimpleDateFormat("yyyy-MM-dd")).parse(newCang.get(i).getActualendtime(), new ParsePosition(0)).getTime();
                                Long rollingTime = rollingData.getTimestamp();
                                if (rollingTime.longValue() <= storehouseEndTime.longValue()) {//当前时间非大于实际闭仓时间
                                    j = i;

                                    break;
                                }
                            }
                        }
                    } else {

                    }
                } else {

                }
                if (j >= 0) {
                    DamsConstruction vo = newCang.get(j);
                    newTableName = vo.getTablename();
                    newTableName = cartableprfix + "_" + newTableName;
                    try {
                        tableMapper.insertRollingData(vo.getTablename(), rollingData);//写入数据
                    } catch (Exception ignore) {

                    }
                }
                if (!t1listmap.containsKey(cartableprfix)) {
                    t1listmap.put(cartableprfix, new ArrayList<>());
                }
                t1listmap.get(cartableprfix).add(rollingData);

                if (t1listmap.get(cartableprfix).size() > 200) {
                    try {
                        RollingDataListVo savevo = new RollingDataListVo();
                        System.out.println("历史数据向t1表插入200条");
                        savevo.setTableName(cartableprfix + "_t_1");
                        savevo.setDataList(t1listmap.get(cartableprfix));
                        t1Mapper.insertrollingdatabatch(savevo);
                    } catch (Exception e) {
                        System.out.println("历史数据插入出错。。");
                    }
                    t1listmap.get(cartableprfix).clear();
                }

            } else {
                /*如果包含*/
                /*删除第一个*/
                queue.poll();
                /*在最后增加一个*/
                queue.offer(orderNum);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            keep++;
            nodatacount++;
            if (keep > 100) {
                for (String s : t1listmap.keySet()) {

                    List<RollingData> t1list = t1listmap.get(s);
                    if (t1list.size() > 0) {
                        try {
                            RollingDataListVo savevo = new RollingDataListVo();
                            System.out.println("历史数据向t1表插入" + t1list.size() + "条");
                            savevo.setTableName(cartableprfix + "_t_1");
                            savevo.setDataList(t1list);
                            t1Mapper.insertrollingdatabatch(savevo);
                        } catch (Exception e) {

                        }
                        t1listmap.get(s).clear();
                    }

                }
                keep = 0;
            }
            keepmap.put(cartableprfix, keep);

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return keep;
    }


}
