package com.tianji.dam.thread;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.bean.GlobCache;
import com.tianji.dam.bean.JtsRTree;
import com.tianji.dam.domain.*;
import com.tianji.dam.domain.vo.DamsJtsTreeVo;
import com.tianji.dam.mapper.TColorConfigMapper;
import com.tianji.dam.mapper.TDamsconstructionMapper;
import com.tianji.dam.scan.*;
import com.tianji.dam.utils.*;
import com.tianji.dam.websocket.WebSocketServerBeforeRealTime;
import com.tianji.dam.websocket.WebSocketServerforCar;
import com.tianji.dam.websocket.WebsocketServerForPosition;
import com.tj.common.utils.RGBHexUtil;
import com.tj.common.utils.StringUtils;
import com.vividsolutions.jts.algorithm.PointLocator;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.osgeo.proj4j.ProjCoordinate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import javax.websocket.Session;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Queue;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/*取数线程 不用轮训 采用阻塞的方式
 * 多线程：从redis List中弹出数据
 * 处理数据 将处理完的数据以 rid为key存入redis
 * 有车辆数据来了以后将获取车辆的最新位置，然后从redis中获取出数据然后直接推送
 * */
@Slf4j
//public class RedisRightPopTaskBlocking extends Observable implements Runnable {
public class RedisRightPopTaskBlockingforRealDone implements Runnable {
    //更改动态参数
    static Map<Integer, Color> colorMap = new HashMap<>(); //导致重启后配置的参数才会生效
    /*----------------------合并实时通道开始-----------------------*/
    @Autowired
    private static StoreHouseMap storeHouseMap;
    final String NORMAL = "normaldata_";

    /*判断数据是否有*/
    Queue<Integer> queue = new LinkedList<Integer>();
    @Autowired
    private WebsocketServerForPosition websocketServerForPosition;

    /* 记录仓下的工作车辆 */


    private String key;
    private String sessionkey;
    private String keytype;

    public String getKeytype() {
        return keytype;
    }

    public void setKeytype(String keytype) {
        this.keytype = keytype;
    }

    private volatile boolean isStop;//使用volatile关键字修饰，可以在多线程之间共享，成员变量来控制线程的停止.


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    /**
     * 设置线程标识
     *
     * @param isStop true：停止 false：不停止
     */
    public void stopThread(boolean isStop) {
        this.isStop = isStop;
    }

    @Override
    public void run() {
        log.info(key + "的数据处理启动。。。");
        if (null == storeHouseMap) {
            storeHouseMap = BeanContext.getApplicationContext().getBean(StoreHouseMap.class);
        }
        RedisUtil redis = BeanContext.getApplicationContext().getBean(RedisUtil.class);
        //更改动态参数
        TColorConfigMapper colorConfigMapper = BeanContext.getApplicationContext().getBean(TColorConfigMapper.class);
        TDamsconstructionMapper damsConstructionMapper = BeanContext.getApplicationContext().getBean(TDamsconstructionMapper.class);
        WebSocketServerforCar wsserver = BeanContext.getApplicationContext().getBean(WebSocketServerforCar.class);
        WebSocketServerBeforeRealTime webreal = BeanContext.getApplicationContext().getBean(WebSocketServerBeforeRealTime.class);
        getColorMap(colorConfigMapper, keytype);
        //开辟内存空间做缓存

        //每天的所有数据放到一个redis数据中
        RollingData rollingData01 = new RollingData();
        RollingData rollingData02 = new RollingData();
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
        while (true) {
            long startTime = System.currentTimeMillis();
            try {
                boolean speedover = false;
                RollingData rollingData = (RollingData) redis.rightPop(NORMAL + key + NORMAL);

                if (StringUtils.isNotNull(rollingData)) {
                    //  System.out.println("从redis获取数据" + rollingData.getVehicleID() + "车" + DateUtils.getTime());
                    nodatatotal = 0;
                    int orderNum = rollingData.getOrderNum();//轨迹点序列号 用于防止数据丢失

                    String ranges = "";

                    if (!queue.contains(orderNum)) {
                        //通过R树查找单元
                        int j = -1;
                        List<DamsJtsTreeVo> treeVos = JtsRTree.query(rollingData.getZhuangX(), rollingData.getZhuangY());
                        List<DamsConstruction> newCang = new LinkedList<>();
                        if (!treeVos.isEmpty()) {
                            for (int i = 0; i < treeVos.size(); i++) {
                                DamsJtsTreeVo vo = treeVos.get(i);
                                //0.获得工作仓的范围
                                DamsConstruction damsConstruction = damsConstructionMapper.selectByPrimaryKey(vo.getId());

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
                                    // 判断数据的timestamp是不是大于等于这些仓位的 actualstarttime实际开始施工时间，把还没有到开仓时间的仓位踢出掉
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
                                            //  log.info("仓位"+newCang.get(i).getTitle()+"闭仓时间" + newCang.get(i).getActualendtime() +"大于当前轨迹时间" + rollingData.getTimestamp());
                                            Float currentspeed = rollingData.getSpeed();
                                            Double normalspeed = newCang.get(i).getSpeed();
                                            //  System.out.println("速度限制："+normalspeed+";实际速度："+currentspeed);
                                            if (currentspeed > normalspeed) {
                                                speedover = true;
                                            }
                                            ranges = newCang.get(i).getRanges();
                                            break;
                                        }
                                    }
                                }
                            } else {
//                                log.info("========未匹配到仓位====");
                            }
                        }
                        int cangId = 0;
                        String cangName = null;
                        Integer showtype = 1;
                        ConcurrentHashMap<String, Session> sessionmap = wsserver.getUsersMap();
                        Map<Long, Integer> usershowtype = webreal.getShowtype();
                        for (String s : sessionmap.keySet()) {
                            String keys[] = s.split(":");
                            if (keys.length == 3) {
                                if (keys[1].equals(key)) {
                                    showtype = usershowtype.get(Long.valueOf(keys[2]));
                                }
                            }
                        }
                        if (j >= 0) {
//                            if (2 == showtype) {
//                                result0 = draw(cacheData_today, cols, rows, rollingData02);
//                                result0.put("lastrollingtime", currentPassCount);
//
//                            } else {
//                                result0 = draw(cacheData_cang, cols, rows, rollingData02);
//                                result0.put("lastrollingtime", currentPassCount_cang);
//                                if (cacheData_cang.size() == 0) {
//                                    result0.put("base64", "");
//                                }
                            }


                        if (j >= 0) {
                            DamsConstruction vo = newCang.get(j);
                            cangId = vo.getId();
                            cangName = vo.getTitle();
                        }
                        //处理数据
                        try {
                            if (!StringUtils.isNotNull(rollingData01.getVehicleID())) {
                                rollingData01 = rollingData;
                            } else {
                                rollingData02 = rollingData;
                                double dis = Math.sqrt(Math.pow(rollingData01.getZhuangX() - rollingData02.getZhuangX(), 2) +
                                        Math.pow(rollingData01.getZhuangY() - rollingData02.getZhuangY(), 2));
                                if (dis < TrackConstant.MIN_DIS) {

                                } else if (dis > TrackConstant.MAX_DIS) {
                                    rollingData01 = rollingData02;
                                    //将前一个滚轮置空,否则将会把中间连接在一起
                                    mLastPt = null;
                                } else {
                                    WheelMarkData data = WheelMarkData.calPosition(rollingData01, rollingData02, TrackConstant.WHEEL_LEFT, TrackConstant.WHEEL_RIGHT, 0);
                                    if (data != null) {
                                        rollingData02.setCoordLX(data.LPt.getX());
                                        rollingData02.setCoordLY(data.LPt.getY());
                                        rollingData02.setCoordRX(data.RPt.getX());
                                        rollingData02.setCoordRY(data.RPt.getY());
                                        if (mLastPt == null) {
                                            mLastPt = data;
                                        } else {
                                            if (ps == null) {
                                                ps = new PointCpb[]{ps1, ps2, ps3, ps4};
                                            }
                                            ps1.x = mLastPt.LPt.getX();
                                            ps1.y = mLastPt.LPt.getY();
                                            ps2.x = mLastPt.RPt.getX();
                                            ps2.y = mLastPt.RPt.getY();
                                            ps3.x = data.LPt.getX();
                                            ps3.y = data.LPt.getY();
                                            ps4.x = data.RPt.getX();
                                            ps4.y = data.RPt.getY();
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
                                                int n = 0;
                                                int m = 0;
                                                //用于当前碾压绘图需要的缓存集合
                                                ConcurrentHashMap<Long, MatrixItem[][]> cacheData = new ConcurrentHashMap<>();
                                                List<Integer> cols = new LinkedList<>();//列
                                                List<Integer> rows = new LinkedList<>();//行
                                                int allPassCount = 0;
                                                //rasters 数据像素
                                                for (Pixel pixel : rasters) {
                                                    try {

                                                        long rid = RidUtil.double2Long(pixel.getX(), pixel.getY());
                                                        //从当天Rid缓存中获取当该Rid的二维数组
                                                        String key = GenerateRedisKey.realTimeRidKey(rid, keytype);
                                                        MatrixItem[][] ridMatirxItems = cacheData.get(rid);
                                                        if (ridMatirxItems == null) {
                                                            ridMatirxItems = (MatrixItem[][]) redis.get(key);
                                                            if (ridMatirxItems == null) {
                                                                ridMatirxItems = new MatrixItem[RidUtil.R_LEN][RidUtil.R_LEN];
                                                            }
                                                            cacheData.put(rid, ridMatirxItems);
                                                        }
                                                        cols.add(RidUtil.long2Col(rid));
                                                        rows.add(RidUtil.long2Row(rid));
                                                        //计算栅格点位在当前Rid中的行列 并获取到之前该位置的栅格数据进行更新
                                                        int bottomRid = RidUtil.long2Bottom(rid);
                                                        int leftRid = RidUtil.long2Left(rid);
                                                        int nRid = (pixel.getY() - bottomRid);
                                                        int mRid = (pixel.getX() - leftRid);
                                                        if (nRid >= 0 && mRid >= 0 && nRid < RidUtil.R_LEN && mRid < RidUtil.R_LEN) {
                                                            MatrixItem realMatrixItem = ridMatirxItems[mRid][nRid];
                                                            if (realMatrixItem == null) {
                                                                realMatrixItem = new MatrixItem();
                                                            }

                                                            realMatrixItem.setIsVibrate(realMatrixItem.getIsVibrate() + rollingData02.getIsVibrate());
                                                            realMatrixItem.setIsNotVibrate(realMatrixItem.getIsNotVibrate() + (1 - rollingData02.getIsVibrate()));
                                                            realMatrixItem.setRollingTimes(realMatrixItem.getRollingTimes() + 1);
                                                            realMatrixItem.getElevationList().add(rollingData02.getElevation().floatValue());
                                                            realMatrixItem.getAccelerationList().add(rollingData02.getAcceleration());
                                                            realMatrixItem.getFrequencyList().add(rollingData02.getFrequency());
                                                            realMatrixItem.getSpeedList().add(rollingData02.getSpeed().floatValue());
                                                            realMatrixItem.getTimestampList().add(rollingData02.getTimestamp());
                                                            realMatrixItem.getVehicleIDList().add(rollingData02.getVehicleID());

                                                            ridMatirxItems[mRid][nRid] = realMatrixItem;
                                                            allPassCount += realMatrixItem.getRollingTimes();
                                                            cacheData.put(rid, ridMatirxItems);
                                                            StoreHouseMap.getCarrollingtimes().put(rollingData02.getVehicleID(), realMatrixItem.getRollingTimes());

                                                        }
                                                    } catch (Exception ex) {
                                                        ex.printStackTrace();
                                                    }
                                                }
                                                for (Map.Entry<Long, MatrixItem[][]> entry : cacheData.entrySet()) {
                                                    long key = entry.getKey();
                                                    MatrixItem[][] value = entry.getValue();
                                                    String redisKey = GenerateRedisKey.realTimeRidKey(key, keytype);
                                                    redis.set(redisKey, value);

                                                }
                                                int currentPassCount = allPassCount / rasters.size();

                                                rasters.clear();
                                                rollingData01 = rollingData02;
                                                mLastPt = data;

                                                JSONObject result0 = draw(cacheData, cols, rows, rollingData02);


                                                result0.put("speedover", speedover);
                                                result0.put("lastrollingtime", currentPassCount);
                                                result0.put("ranges", ranges);
                                                result0.put("zhuanghao", rollingData02.getZhuanghao());
                                                result0.put("pianju", rollingData02.getPianju());
//                                                double houdu = 0.0;
//                                                if (currentPassCount == 1 || currentPassCount == 0) {
//                                                    houdu = RandomUtiles.randomdouble(38.0, 39.0);
//                                                    ;
//                                                } else if (currentPassCount == 2) {
//                                                    houdu = RandomUtiles.randomdouble(35.0, 36.0);
//                                                    ;
//                                                } else if (currentPassCount == 3) {
//                                                    houdu = RandomUtiles.randomdouble(33.0, 34.0);
//                                                    ;
//                                                } else if (currentPassCount == 4) {
//                                                    houdu = RandomUtiles.randomdouble(30.0, 31.0);
//                                                    ;
//                                                } else if (currentPassCount == 5) {
//                                                    houdu = RandomUtiles.randomdouble(29.0, 30.0);
//                                                    ;
//                                                } else if (currentPassCount >= 6) {
//                                                    houdu = RandomUtiles.randomdouble(28.0, 29.0);
//                                                    ;
//                                                }
//                                                result0.put("houdu", houdu);
                                                try {
                                                    result0.put("cangId", cangId);
                                                    result0.put("title", cangName == null ? " " : cangName + " ");
                                                    result0.put("isLoad", 0);//仓未加载过

                                                 
                                                    result0.put("lat", rollingData02.getLatitude());
                                                    result0.put("lng", rollingData02.getLongitude());
                                                    // Thread.sleep(20);
                                                    //  System.out.println("数据处理完成。。");
                                                    //  Integer showtype = 1;
                                                    //     ConcurrentHashMap<String, Session> sessionmap = wsserver.getUsersMap();
                                                    for (String s : sessionmap.keySet()) {
                                                        String keys[] = s.split(":");
                                                        if (keys.length == 3) {


                                                            if (keys[1].equals(key)) {
                                                                Session session = sessionmap.get(s);
                                                                if (null != session && session.isOpen()) {
                                                                    session.getBasicRemote().sendText(JSONObject.toJSONString(result0));

                                                                    //   System.out.println("数据推送完成。。");
                                                                } else {
                                                                    //  this.stopThread(true);
                                                                }
                                                            }
                                                        }
                                                        Thread.sleep(20);
                                                    }
                                                    cacheData.clear();
                                                    cacheData = null;
                                                    cols = null;
                                                    rows = null;

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
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
                //连续50次无数据时 将睡眠10秒
                if (nodatatotal > 100) {
                    if (!isStop) {
                        Thread.sleep(10 * 1000);
                        nodatatotal = 0;
                    } else {
                        break;
                    }
                } else {
                    Thread.sleep(50);
                }
            } catch (Exception e) {
                log.info("出现异常");
                e.printStackTrace();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                e.printStackTrace(new PrintStream(baos));
                String exception = baos.toString();
                log.error(exception);
            }
        }
        System.out.println(key + "车辆断开，数据处理中止");

    }

    public JSONObject draw(ConcurrentHashMap<Long, MatrixItem[][]> cache, List<Integer> long2Cols,
                           List<Integer> long2Rows, RollingData rollingData02) {

        //筛选出最大最小列
        int[] col = new int[long2Cols.size()];//列
        for (int coli = 0; coli < long2Cols.size(); coli++) {
            /*col[i] = long2Cols.get(coli);*/
            if (StringUtils.isNotNull(long2Cols.get(coli))) {
                col[coli] = long2Cols.get(coli);
            } else {
                log.error("下标【" + coli + "】列为null");
            }
        }
        int[] row = new int[long2Rows.size()];//行
        for (int rowi = 0; rowi < long2Rows.size(); rowi++) {
            /*row[i] = long2Rows.get(rowi);*/
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
        g2.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        String bsae64_string = "";
        MatrixItem[][] matrixItems = null;
        MatrixItem item = null;
        for (Map.Entry<Long, MatrixItem[][]> entry : cache.entrySet()) {

            // 10*10 小方格
            long rid = entry.getKey();
            matrixItems = entry.getValue();
            int dltaX = RidUtil.long2Col(rid) * RidUtil.R_LEN - baseX;
            int dltaY = RidUtil.long2Row(rid) * RidUtil.R_LEN - baseY;
            for (int ii = 0; ii < RidUtil.R_LEN - 1; ii++) {
                for (int j = 0; j < RidUtil.R_LEN - 1; j++) {
                    item = matrixItems[ii][j];
                    if (item != null) {
                        int rollingTimes = item.getRollingTimes();
                        g2.setColor(getColorByCount2(rollingTimes));
                        g2.fillRect(ii + dltaX, j + dltaY, 2, 2);
                    }
                }
            }
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

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

        Double VibrateValue = 0D;//rollingData2.getVibrateValue();
        int iszhen = 0;
        if (VibrateValue > 40D) {
            iszhen = 1;
        }
        result.put("iszhen", iszhen);

        result.put("zhuangX", rollingData02.getZhuangX());
        result.put("zhuangY", rollingData02.getZhuangY());
        result.put("speed", rollingData02.getSpeed());//车辆速度
        result.put("elevation", rollingData02.getElevation());//车辆高程
        result.put("base64", bsae64_string);
        result.put("zhuanghao", rollingData02.getZhuanghao());
        result.put("pianju", rollingData02.getPianju());

        //通过轨迹多边形获得当前碾压区域的左上角点和右下角点
        RollingDataRange rollingDataRange = new RollingDataRange();
        rollingDataRange.setMaxCoordX(Double.valueOf(maxCol));
        rollingDataRange.setMinCoordX(Double.valueOf(minCol));
        rollingDataRange.setMaxCoordY(Double.valueOf(maxRow));
        rollingDataRange.setMinCoordY(Double.valueOf(minRow));
        result.put("rollingDataRange", rollingDataRange);

        ProjCoordinate projLeftBottom = new ProjCoordinate(minRow * RidUtil.R_LEN, minCol * RidUtil.R_LEN, 10);
        ProjCoordinate projRightTop = new ProjCoordinate(maxRow * RidUtil.R_LEN + RidUtil.R_LEN, maxCol * RidUtil.R_LEN + RidUtil.R_LEN, 10);
        result.put("pointLeftBottom", projLeftBottom);
        result.put("pointRightTop", projRightTop);

        result.put("height", 0);

        ProjCoordinate markPos = new ProjCoordinate(rollingData02.getZhuangY(), rollingData02.getZhuangX(), 10);
        result.put("markPos", markPos);

        result.put("carId", rollingData02.getVehicleID());//carId
        result.put("title", "1车正在施工......");
        result.put("angle", rollingData02.getAngle());
        double vcv = rollingData02.getVibrateValue();
//        if(vcv != 0){
//            vcv = 370 + 10 * Math.random();
//        }
        result.put("amplitude", vcv);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date t = new Date();
        t.setTime(rollingData02.getTimestamp());
        result.put("timestamp", sdf.format(t));
        return result;
    }

    public Color getColorByCount2(Integer count) {

        Color color = colorMap.get(count);
        if (StringUtils.isNotNull(color)) {
            return color;
        } else {
            if (count.intValue() > 0) {
                Integer maxKey = (Integer) MapUtil.getMaxKey(colorMap);//取最大key
                return colorMap.get(maxKey);
            } else if (count.intValue() == 0) {
                return new Color(255, 255, 255, 0);
            }
        }
        return null;
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

