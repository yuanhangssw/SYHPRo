package com.tianji.dam.thread;


import com.alibaba.fastjson.JSONObject;
import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.bean.GlobCache;
import com.tianji.dam.domain.*;
import com.tianji.dam.mapper.*;
import com.tianji.dam.mileageutil.Mileage;
import com.tianji.dam.scan.*;
import com.tianji.dam.utils.*;
import com.tianji.dam.websocket.WebSocketServerBeforeRealTimeByType;
import com.tianji.dam.websocket.WebSocketServerforCar;
import com.tj.common.utils.DateUtils;
import com.tj.common.utils.RGBHexUtil;
import com.tj.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.osgeo.proj4j.ProjCoordinate;

import javax.imageio.ImageIO;
import javax.websocket.Session;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Queue;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 改版为按照时间+车辆作为仓位数据。
 */
@Slf4j
//public class RedisRightPopTaskBlocking extends Observable implements Runnable {
public class RedisRightPopTaskBlockingforRealDoneCang implements Runnable {
    //更改动态参数
    static Map<Integer, Color> colorMap = new HashMap<>(); //导致重启后配置的参数才会生效
    /*----------------------合并实时通道开始-----------------------*/
    static final String NORMAL = "normaldata_";

    /*判断数据是否有*/
    Queue<Integer> queue = new LinkedList<>();


    private String key;

    private String keytype;
    private SimpleDateFormat sdf;


    List<TColorConfig> colorConfig_evolution = new ArrayList<>();

    Double baseevolution = 0.0d;
    Double basehoudu = 0.0d;


    public void setKeytype(String keytype) {
        this.keytype = keytype;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    /**
     * 设置线程标识
     */
    public void stopThread() {

    }


    @Override
    public void run() {
        log.info(key + "的数据处理启动。。。");
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        RedisUtil redis = BeanContext.getApplicationContext().getBean(RedisUtil.class);
        //更改动态参数
        TColorConfigMapper colorConfigMapper = BeanContext.getApplicationContext().getBean(TColorConfigMapper.class);

        TDayTaskMapper taskMapper = BeanContext.getApplicationContext().getBean(TDayTaskMapper.class);
        TaskCarRecordMapper carRecordMapper = BeanContext.getApplicationContext().getBean(TaskCarRecordMapper.class);
        TDamsconstructionMapper tDamsconstructionMapper = BeanContext.getApplicationContext().getBean(TDamsconstructionMapper.class);
        TableMapper tableMapper = BeanContext.getApplicationContext().getBean(TableMapper.class);
        TColorConfig vo = new TColorConfig();
        vo.setType(44L);//摊铺平整度颜色
        colorConfig_evolution = colorConfigMapper.select(vo);
        getColorMap(colorConfigMapper, keytype);
        //开辟内存空间做缓存

        //每天的所有数据放到一个redis数据中
        RollingData rollingData01 = new RollingData();
        RollingData rollingData02;
        WheelMarkData mLastPt = null;
        Quadrilateral ql = new Quadrilateral();
        PointCpb[] convex = new PointCpb[4];
        PointCpb[] ps = null;
        PointCpb ps1 = new PointCpb();
        PointCpb ps2 = new PointCpb();
        PointCpb ps3 = new PointCpb();
        PointCpb ps4 = new PointCpb();
        Pixel[] polygon = null;
        Pixel polygon1 = new Pixel();
        Pixel polygon2 = new Pixel();
        Pixel polygon3 = new Pixel();
        Pixel polygon4 = new Pixel();

        /*初始化*/
        for (int i = 0; i < 20; i++) {
            queue.offer(0);
        }
        int nodatatotal = 0;
        Long time1 = 0l;
        Long time2 = 0l;
        Long time3 = 0l;
        Long time4 = 0l;
        while (true) {

            try {
                Map<Integer, DamsConstruction> cangForRealMap = GlobCache.cartaskmapcang;
                boolean speedover = false;
                //  log.info("从redis读取。"+ DateUtils.getTime());
                RollingData rollingData = (RollingData) redis.rightPop(NORMAL + key + NORMAL);
                if (StringUtils.isNotNull(rollingData)) {
                    Mileage mileage = Mileage.getmileage();
                    nodatatotal = 0;

                    int orderNum = rollingData.getOrderNum();//轨迹点序列号 用于防止数据丢失

                    String ranges = "";
                    String rediskey = null;
                    String cangName = null;
                    String cangId = null;
                    String tablename = "";
                    Double speedNormal = 4.0;
                    //  Double baseevolution = 0.0;
                    if (!queue.contains(orderNum)) {
                        //通过R树查找单元
                        List<DamsConstruction> alltask = tDamsconstructionMapper.findByStatus(8);
                        //查询当前开仓的零时仓位
                        try {
                            time1 = System.currentTimeMillis();
                            if (cangForRealMap.containsKey(Integer.valueOf(rollingData.getVehicleID()))) {
                                DamsConstruction temp = cangForRealMap.get(Integer.valueOf(rollingData.getVehicleID()));
                                speedNormal = temp.getSpeed();  // 大于这个速度为true
                                rediskey = temp.getTablename();
                                cangName = temp.getTitle();
                                cangId = temp.getId().toString();
                                tablename = temp.getTablename();
                                baseevolution = temp.getGaocheng();
                                basehoudu = Double.valueOf(temp.getFreedom3() == null ? "80" : temp.getFreedom3());
                            } else {
                                for (DamsConstruction tDayTask : alltask) {
                                    TaskCarRecord recordquery = new TaskCarRecord();
                                    recordquery.setStatus(0L);
                                    recordquery.setTaskId(tDayTask.getId().longValue());
                                    List<TaskCarRecord> allrecrord = carRecordMapper.selectTaskCarRecordList(recordquery);
                                    for (TaskCarRecord record : allrecrord) {
                                        if (Objects.equals(record.getCarId(), Long.valueOf(rollingData.getVehicleID()))) {
                                            cangForRealMap.put(record.getCarId().intValue(), tDayTask);
                                            rediskey = tDayTask.getTablename();
                                            cangName = tDayTask.getTitle();
                                            cangId = tDayTask.getId().toString();
                                            tablename = tDayTask.getTablename();
                                            speedNormal = tDayTask.getSpeed();  // 大于这个速度为true
                                            baseevolution = tDayTask.getGaocheng();
                                            basehoudu = tDayTask.getCenggao();
                                        }
                                    }
                                }
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }

                        //处理数据
                        try {

                            if (!StringUtils.isNotNull(rollingData01.getVehicleID())) {
                                rollingData01 = rollingData;
                            } else {
                                rollingData02 = rollingData;
                                double dis = Math.sqrt(Math.pow(rollingData01.getZhuangX() - rollingData02.getZhuangX(), 2) +
                                        Math.pow(rollingData01.getZhuangY() - rollingData02.getZhuangY(), 2));
                                if (dis > TrackConstant.MAX_DIS) {
                                    rollingData01 = rollingData02;
                                    //将前一个滚轮置空,否则将会把中间连接在一起
                                    mLastPt = null;
                                } else {

                                    if (ps == null) {
                                        ps = new PointCpb[]{ps1, ps2, ps3, ps4};
                                    }

                                    double[] rd1 = mileage.coord2pixel(rollingData02.getCoordRX(), rollingData02.getCoordRY());
                                    double[] ld1 = mileage.coord2pixel(rollingData02.getCoordLX(), rollingData02.getCoordLY());
                                    double[] rd2 = mileage.coord2pixel(rollingData02.getBeforeCoordLX(), rollingData02.getBeforeCoordLY());
                                    double[] ld2 = mileage.coord2pixel(rollingData02.getBeforeCoordRX(), rollingData02.getBeforeCoordRY());

                                    ps1.x = rd1[0];
                                    ps1.y = rd1[1];
                                    ps2.x = ld1[0];
                                    ps2.y = ld1[1];
                                    ps3.x = rd2[0];
                                    ps3.y = rd2[1];
                                    ps4.x = ld2[0];
                                    ps4.y = ld2[1];

                                    int count = PolygonUtils.getPointsConvexClosure(ps, 0, 4, convex);
                                    if (count == 4) {
                                        ql.pt1.setX(convex[0].x);
                                        ql.pt1.setY(convex[0].y);
                                        ql.pt2.setX(convex[1].x);
                                        ql.pt2.setY(convex[1].y);
                                        ql.pt3.setX(convex[2].x);
                                        ql.pt3.setY(convex[2].y);
                                        ql.pt4.setX(convex[3].x);
                                        ql.pt4.setY(convex[3].y);
                                        if (polygon == null) {
                                            polygon = new Pixel[]{polygon1, polygon2, polygon3, polygon4};
                                        }
                                        polygon1.setX((int) (ql.pt1.getX()));
                                        polygon1.setY((int) (ql.pt1.getY()));
                                        polygon2.setX((int) (ql.pt2.getX()));
                                        polygon2.setY((int) (ql.pt2.getY()));
                                        polygon3.setX((int) (ql.pt3.getX()));
                                        polygon3.setY((int) (ql.pt3.getY()));
                                        polygon4.setX((int) (ql.pt4.getX()));
                                        polygon4.setY((int) (ql.pt4.getY()));
                                        List<Pixel> rasters = Scan.scanRaster(polygon);
                                        //用于当前碾压绘图需要的缓存集合
                                        ConcurrentHashMap<Long, MatrixItemRedisReal[][]> cacheData_today = new ConcurrentHashMap<>();
                                        ConcurrentHashMap<Long, MatrixItemRedisReal[][]> cacheData_cang = new ConcurrentHashMap<>();
                                        List<Integer> cols = new LinkedList<>();//列
                                        List<Integer> rows = new LinkedList<>();//行
                                        int allPassCount = 0;
                                        int allPassCount_cang = 0;
                                        //rasters 数据像素
                                        List<Long> allrid = new ArrayList<>();
                                        for (Pixel pixel : rasters) {
                                            try {
                                                //todo:此处的Rid是通过平面坐标进行计算得到的
                                                long rid = RidUtil.double2Long(pixel.getX(), pixel.getY());
                                                //从当天Rid缓存中获取当该Rid的二维数组
                                                //当天数据
//                                                        MatrixItemRedisReal[][] ridMatirxItems = cacheData_today.get(rid);
//                                                        if (ridMatirxItems == null) {
//                                                            String key = GenerateRedisKey.realTimeRidKey(rid, keytype);
//                                                            ridMatirxItems = (MatrixItemRedisReal[][]) redis.get(key);
//                                                            if (ridMatirxItems == null) {
//                                                                ridMatirxItems = new MatrixItemRedisReal[RidUtil.R_LEN][RidUtil.R_LEN];
//                                                            }
//                                                            cacheData_today.put(rid, ridMatirxItems);
//                                                        }
                                                //仓位数据。
                                                MatrixItemRedisReal[][] ridMatirxItems_cang = cacheData_cang.get(rid);
                                                if (null != rediskey) {

                                                    String key = "TempRealCang_" + keytype + "==" + rediskey + "==" + rid;

                                                    if (null == ridMatirxItems_cang) {
                                                        ridMatirxItems_cang = (MatrixItemRedisReal[][]) redis.get(key);
                                                        if (ridMatirxItems_cang == null) {
                                                            System.out.println("redis中不存在任务碾压，新增空白对象。");
                                                            ridMatirxItems_cang = new MatrixItemRedisReal[RidUtil.R_LEN][RidUtil.R_LEN];
                                                        }
                                                    }
                                                    cacheData_cang.put(rid, ridMatirxItems_cang);

                                                }
                                                cols.add(RidUtil.long2Col(rid));
                                                rows.add(RidUtil.long2Row(rid));
                                                int bottomRid = RidUtil.long2Bottom(rid);
                                                int leftRid = RidUtil.long2Left(rid);
                                                int nRid = (pixel.getY() - bottomRid);
                                                int mRid = (pixel.getX() - leftRid);


                                                if (nRid >= 0 && mRid >= 0 && nRid < RidUtil.R_LEN && mRid < RidUtil.R_LEN) {
//                                                            MatrixItemRedisReal realMatrixItem = ridMatirxItems[mRid][nRid];
//                                                            if (realMatrixItem == null) {
//                                                                realMatrixItem = new MatrixItemRedisReal();
//                                                            }
//                                                            realMatrixItem.setRollingTimes(realMatrixItem.getRollingTimes() + 1);
//
//                                                            realMatrixItem.getCurrentEvolution().add(rollingData02.getCurrentEvolution());
////
//                                                            ridMatirxItems[mRid][nRid] = realMatrixItem;
//                                                            allPassCount += realMatrixItem.getRollingTimes();
//                                                            cacheData_today.put(rid, ridMatirxItems);
                                                    //操作仓位数据
                                                    if (null != rediskey) {
                                                        MatrixItemRedisReal realMatrixItem_cang = ridMatirxItems_cang[mRid][nRid];
                                                        if (realMatrixItem_cang == null) {
                                                            realMatrixItem_cang = new MatrixItemRedisReal();
                                                        }
                                                        realMatrixItem_cang.setRollingTimes(realMatrixItem_cang.getRollingTimes() + 1);

                                                        realMatrixItem_cang.getCurrentEvolution().add(rollingData02.getCurrentEvolution());

                                                        ridMatirxItems_cang[mRid][nRid] = realMatrixItem_cang;
                                                        allPassCount_cang += realMatrixItem_cang.getRollingTimes();
                                                        cacheData_cang.put(rid, ridMatirxItems_cang);


                                                    }

                                                }
                                                //  ridMatirxItems= null;
                                                ridMatirxItems_cang = null;
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }
                                        }
                                        time2 = System.currentTimeMillis();

//                                                for (Map.Entry<Long, MatrixItemRedisReal[][]> entry : cacheData_today.entrySet()) {
//                                                    long key = entry.getKey();
//                                                    MatrixItemRedisReal[][] value = entry.getValue();
//                                                    String redisKeyreal = GenerateRedisKey.realTimeRidKey(key, keytype);
//                                                    redis.set(redisKeyreal, value);
//                                                }
                                        for (Map.Entry<Long, MatrixItemRedisReal[][]> entry : cacheData_cang.entrySet()) {
                                            long key = entry.getKey();
                                            MatrixItemRedisReal[][] value = entry.getValue();
                                            String redisKeyreal = "TempRealCang_" + keytype + "==" + rediskey + "==" + key;
                                            redis.set(redisKeyreal, value);
                                            if (!allrid.contains(key)) {
                                                allrid.add(key);
                                            }
                                        }

                                        List<Long> todayold = (List<Long>) redis.get("day_" + DateUtils.getDate());
                                        if (null == todayold) {
                                            todayold = new ArrayList<>();
                                        }
                                        List<Long> newlist = new ArrayList<>();
                                        for (Long aLong : allrid) {
                                            if (null != todayold && !todayold.contains(aLong)) {
                                                newlist.add(aLong);
                                            }
                                        }
                                        todayold.addAll(newlist);
                                        redis.set("day_" + DateUtils.getDate(), todayold);

                                        time3 = System.currentTimeMillis();

                                        int currentPassCount = allPassCount / rasters.size();
                                        int currentPassCount_cang = allPassCount_cang / rasters.size();
                                        rasters.clear();
                                        rollingData01 = rollingData02;

                                        Integer showtype;
                                        JSONObject result0 = new JSONObject();
                                        ConcurrentHashMap<String, Session> sessionmap = WebSocketServerforCar.getUsersMap();
                                        Map<Long, Integer> usershowtype = WebSocketServerBeforeRealTimeByType.getShowtype();
                                        for (String s : sessionmap.keySet()) {
                                            Session session = sessionmap.get(s);
                                            String[] keys = s.split(":");
                                            boolean ifsend = true;
                                            if (keys.length == 3) {
                                                if (keys[1].equals(key)) {
                                                    showtype = usershowtype.get(Long.valueOf(keys[2]));


//                                                            if (null == showtype || 1 == showtype) {
//                                                                result0 = draw(cacheData_today, cols, rows, rollingData02, showtype);
//                                                                result0.put("lastrollingtime", currentPassCount);
//                                                                ifsend = true;
//                                                            } else if(showtype==2){
                                                    result0 = draw(cacheData_cang, cols, rows, rollingData02, showtype);
                                                    result0.put("lastrollingtime", currentPassCount_cang);
                                                    if (cacheData_cang.size() == 0) {
                                                        System.out.println("任务碾压缓存无数据。不推送");
                                                        result0.put("base64", "");
                                                        ifsend = false;
//                                                                }
//                                                            }else{//如果展示类型为3，则直接推送原始的数据。
//                                                                ifsend = true;
                                                    }
                                                    if (ifsend) {
                                                        if (rollingData.getSpeed() > speedNormal) {  //大于这个速度改为true
                                                            speedover = true;
                                                        }
                                                        result0.put("speedover", speedover);
                                                        result0.put("ranges", ranges);
                                                        try {

                                                            result0.put("vibrateValue", rollingData02.getVibrateValue());
                                                            result0.put("cangId", cangId);
                                                            result0.put("Latitude", rollingData.getLatitude());
                                                            result0.put("Longitude", rollingData.getLongitude());
                                                            result0.put("qhangle", rollingData02.getQhangle());
                                                            result0.put("zyangle", rollingData02.getZyangle());
                                                            result0.put("currentevolution", rollingData02.getCurrentEvolution());
                                                            if (baseevolution != 0 && null != rollingData02.getCurrentEvolution()) {
                                                                result0.put("tphoudu", (rollingData02.getCurrentEvolution() - baseevolution.floatValue()));
                                                            } else {
                                                                result0.put("tphoudu", "");
                                                            }
                                                            result0.put("coordx", rollingData02.getCoordX());
                                                            result0.put("coordy", rollingData02.getCoordY());

                                                            result0.put("beforeCoordLX", rollingData02.getBeforeCoordLX());
                                                            result0.put("beforeCoordLY", rollingData02.getBeforeCoordLY());
                                                            result0.put("beforeCoordRX", rollingData02.getBeforeCoordRX());
                                                            result0.put("beforeCoordRY", rollingData02.getBeforeCoordRY());

                                                            result0.put("coordLX", rollingData02.getCoordLX());
                                                            result0.put("coordLY", rollingData02.getCoordLY());
                                                            result0.put("coordRX", rollingData02.getCoordRX());
                                                            result0.put("coordRY", rollingData02.getCoordRY());

                                                            result0.put("title", cangName == null ? " " : cangName + " ");
                                                            result0.put("isLoad", 0);//仓未加载过
                                                            //  Thread.sleep(10);
                                                            //  System.out.println("数据处理完成。。");
                                                            time4 = System.currentTimeMillis();
                                                            //    System.out.println(key+"车推送结束。时间为：处理时间："+(time2-time1)+">>存储时间："+(time3-time2)+">>>：绘图时间："+(time4-time3)+">>总耗时："+(time4-time1) );
                                                            if (session.isOpen()) {
                                                                session.getBasicRemote().sendText(JSONObject.toJSONString(result0));


                                                            }
                                                            result0.clear();
                                                            result0 = null;
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        cacheData_today.clear();
                                        cacheData_cang.clear();
                                    }
                                }
                                //    }
                                //   }
                            }
                        } catch (Exception ex) {

                            System.out.println(rollingData);
                            ex.printStackTrace();
                            System.gc();
                        }
                    } else {
                        /*如果包含*/
                        /*删除第一个*/
                        queue.poll();
                        /*在最后增加一个*/
                        queue.offer(orderNum);
                    }
                } else {
                    nodatatotal++;
                }
                //连续50次无数据时 将睡眠5秒
                if (nodatatotal > 500) {
                    log.info(key + "车辆数据处理线程休眠。10S>>>" + DateUtils.getTime());
                    System.out.println(key + "车辆数据处理线程休眠。10S");
                    // if (!isStop) {
                    Thread.sleep(10 * 1000);
                    nodatatotal = 0;
                    System.gc();
//                    }else{
//                        break;
//                    }
                }
                Thread.sleep(5);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    public synchronized JSONObject draw(ConcurrentHashMap<Long, MatrixItemRedisReal[][]> cache, List<Integer> long2Cols,
                                        List<Integer> long2Rows, RollingData rollingData02, Integer showtype) {
        MatrixItemRedisReal[][] matrixItems;
        MatrixItemRedisReal item = null;

        //筛选出最大最小列
        int[] col = new int[long2Cols.size()];//列
        for (int coli = 0; coli < long2Cols.size(); coli++) {
            if (StringUtils.isNotNull(long2Cols.get(coli))) {
                col[coli] = long2Cols.get(coli);
            } else {
                log.error("下标【" + coli + "】列为null");
            }
        }
        int[] row = new int[long2Rows.size()];//行
        for (int rowi = 0; rowi < long2Rows.size(); rowi++) {
            if (StringUtils.isNotNull(long2Rows.get(rowi))) {
                row[rowi] = long2Rows.get(rowi);
            } else {
                log.error("下标【" + rowi + "】行为null");
            }
        }

        int minCol = Arrays.stream(col).min().getAsInt();//xbg 最小列
        int maxCol = Arrays.stream(col).max().getAsInt();//xend 最大列
        int minRow = Arrays.stream(row).min().getAsInt();//ybg 最小行
        int maxRow = Arrays.stream(row).max().getAsInt();//yed 最大行

        int xSize = (maxCol - minCol) * RidUtil.R_LEN + RidUtil.R_LEN;
        int ySize = (maxRow - minRow) * RidUtil.R_LEN + RidUtil.R_LEN;
        int baseX = minCol * RidUtil.R_LEN;
        int baseY = minRow * RidUtil.R_LEN;
        BufferedImage bi = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) bi.getGraphics();


        // for(Map.Entry<Long, MatrixItemRedisReal[][]> entry : cache.entrySet()){
        for (Object key : cache.keySet()) {
            // 10*10 小方格
            long rid = Long.parseLong(key.toString());
            matrixItems = cache.get(rid);
            int dltaX = RidUtil.long2Col(rid) * RidUtil.R_LEN - baseX;
            int dltaY = RidUtil.long2Row(rid) * RidUtil.R_LEN - baseY;
            for (int i = 0; i < RidUtil.R_LEN - 1; i++) {
                for (int j = 0; j < RidUtil.R_LEN - 1; j++) {
                    item = matrixItems[i][j];
                    if (item != null) {
                        if (keytype.equals("tpj")) {
                            if (baseevolution == 0 || null == showtype || showtype == 1) {
                                g2.setColor(new Color(254, 149, 206));
                                g2.fillRect(i + dltaX, j + dltaY, 2, 2);
                            } else {
                                float lastevolution = item.getCurrentEvolution().size() == 0 ? baseevolution.floatValue() : item.getCurrentEvolution().getLast();
                                // System.out.println("当前位置高程：" + lastevolution);
                                float currenthoudu = (lastevolution - baseevolution.floatValue()) * 100.0f;
                                float laststatus = currenthoudu - basehoudu.floatValue();

                                g2.setColor(getColorByCountEvolution(laststatus, colorConfig_evolution));
                                g2.fillRect(i + dltaX, j + dltaY, 2, 2);
                            }
                            // g2.setColor(new Color(254, 149, 206));
                            // g2.fillRect(i + dltaX, j + dltaY, 2, 2);
                        } else if (keytype.equals("ylj")) {
                            int rollingTimes = item.getRollingTimes();
                            g2.setColor(getColorByCount2(rollingTimes));
                            g2.fillRect(i + dltaX, j + dltaY, 2, 2);
                        }
                    }
                }
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String bsae64_string = "";
        try {
            ImageIO.write(bi, "PNG", baos);
            byte[] bytes = baos.toByteArray();//转换成字节
            bsae64_string = "data:image/png;base64," + Base64.encodeBase64String(bytes);
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject result = new JSONObject();
        result.put("tablename", "storehouseId");//图片占的网格编号
        result.put("index", item);//图片占的网格编号

        double VibrateValue = rollingData02.getVibrateValue();
        int iszhen = 0;
        if (VibrateValue > 40D) {
            iszhen = 1;
        }
        result.put("iszhen", iszhen);

        result.put("zhuangX", rollingData02.getZhuangX());
        result.put("zhuangY", rollingData02.getZhuangY());
        result.put("zhuanghao", rollingData02.getZhuanghao());
        result.put("pianju", rollingData02.getPianju());
        result.put("speed", rollingData02.getSpeed());//车辆速度
        result.put("elevation", rollingData02.getElevation());//车辆高程
        result.put("base64", bsae64_string);

        //通过轨迹多边形获得当前碾压区域的左上角点和右下角点
        RollingDataRange rollingDataRange = new RollingDataRange();
        rollingDataRange.setMaxCoordX((double) maxCol);
        rollingDataRange.setMinCoordX((double) minCol);
        rollingDataRange.setMaxCoordY((double) maxRow);
        rollingDataRange.setMinCoordY((double) minRow);
        result.put("rollingDataRange", rollingDataRange);

        ProjCoordinate projLeftBottom = new ProjCoordinate(minRow * RidUtil.R_LEN, minCol * RidUtil.R_LEN, 10);
        ProjCoordinate projRightTop = new ProjCoordinate(maxRow * RidUtil.R_LEN + RidUtil.R_LEN, maxCol * RidUtil.R_LEN + RidUtil.R_LEN, 10);
        result.put("pointLeftBottom", projLeftBottom);
        result.put("pointRightTop", projRightTop);

        result.put("height", 0);

        ProjCoordinate markPos = new ProjCoordinate(rollingData02.getZhuangY(), rollingData02.getZhuangX(), 10);
        result.put("markPos", markPos);

        result.put("carId", rollingData02.getVehicleID());//carId
        result.put("title", "正在施工......");
        result.put("angle", rollingData02.getAngle());
        double vcv = rollingData02.getVibrateValue();
//        if(vcv != 0){
//            vcv = 370 + 10 * Math.random();
//        }
        result.put("amplitude", vcv);

        Date t = new Date();
        t.setTime(rollingData02.getTimestamp());
        result.put("timestamp", sdf.format(t));

        //   cache.clear();

        return result;
    }

    public Color getColorByCount2(Integer count) {

        Color color = colorMap.get(count);
        if (StringUtils.isNotNull(color)) {
            return color;
        } else {
            if (count > 0) {
                Integer maxKey = (Integer) MapUtil.getMaxKey(colorMap);//取最大key
                return colorMap.get(maxKey);
            } else if (count == 0) {
                return new Color(255, 255, 255, 0);
            }
        }
        return null;
    }

    public Color getColorByCountEvolution(Float count, List<TColorConfig> colorConfigs) {
        for (TColorConfig color : colorConfigs) {
            //判断count 在哪个从和到之间
            if (count >= color.getC().doubleValue() && count <= color.getD().doubleValue()) {
                int[] rgb = RGBHexUtil.hex2RGB(color.getColor());
                return new Color(rgb[0], rgb[1], rgb[2]);
            }
        }
        //匹配不到的情况下
        return new Color(255, 255, 255, 0);
    }

    public void getColorMap(TColorConfigMapper colorConfigMapper, String cartype) {
        TColorConfig vo = new TColorConfig();
        vo.setType(GlobCache.cartypecolorindex.get(cartype).longValue());//碾压遍次
        List<TColorConfig> colorConfigs = colorConfigMapper.select(vo);
        for (TColorConfig color : colorConfigs) {
            if (color.getNum().intValue() == 0) {
                colorMap.put(0, new Color(255, 255, 255, 0));
            } else {
                int[] rgb = RGBHexUtil.hex2RGB(color.getColor());
                colorMap.put(Integer.valueOf(String.valueOf(color.getNum())), new Color(rgb[0], rgb[1], rgb[2]));
            }
        }
    }
}

