package com.tianji.dam.thread;

import com.alibaba.fastjson.JSONObject;
import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.domain.*;
import com.tianji.dam.mapper.TColorConfigMapper;
import com.tianji.dam.mapper.TDamsconstructionMapper;
import com.tianji.dam.utils.MapUtil;
import com.tianji.dam.utils.TrackConstant;
import com.tianji.dam.websocket.WebSocketServer;
import com.tj.common.utils.RGBHexUtil;
import com.tj.common.utils.StringUtils;
import com.vividsolutions.jts.algorithm.PointLocator;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.operation.buffer.BufferOp;
import com.vividsolutions.jts.triangulate.ConformingDelaunayTriangulationBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.tomcat.util.codec.binary.Base64;
import org.osgeo.proj4j.ProjCoordinate;
import org.springframework.beans.factory.annotation.Autowired;

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

@Slf4j
public class RealTimeRedisRightPopTaskBlockingZhuang implements Runnable {
    private Double VibrateValue0 = 40d;

    @Autowired
    private WebSocketServer webSocketServer;
    private String key;


    Map<String, Session> users = webSocketServer.getUsersMap();

    private String storehouseId;
    private String carId;
    private String rollingResultKey;

    public String getRollingResultKey() {
        return rollingResultKey;
    }

    public void setRollingResultKey(String rollingResultKey) {
        this.rollingResultKey = rollingResultKey;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getStorehouseName() {
        return storehouseName;
    }

    public void setStorehouseName(String storehouseName) {
        this.storehouseName = storehouseName;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    private double height;
    private String storehouseName;

    public String getStorehouseId() {
        return storehouseId;
    }

    public void setStorehouseId(String storehouseId) {
        this.storehouseId = storehouseId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    //更改动态参数
    Map<Integer, Color> colorMap = new HashMap<>();

    @Autowired
    private static StoreHouseMap storeHouseMap;
    private Map<String, StorehouseRange> shorehouseRange = storeHouseMap.getShorehouseRange();
    private Map<String, MatrixItem[][]> storeHouseMaps2RollingData = storeHouseMap.getStoreHouses2RollingData();
    private Map<String, RollingResult> rollingResultMap = storeHouseMap.getRollingResultMap();
    private Map<String, List<RealTimeRedisRightPopTaskBlockingZhuang>> threadMap = storeHouseMap.getRealTimeRedisRightPopTaskBlockingZhuangList();

    //实时数据
//    Map<String, List<RollingData>> realTimeDataMap  = storeHouseMap.getRealTimeDataList();
    Map<String, Queue<RollingData>> realTimeDataMap = storeHouseMap.getRealTimeDataList();

    Double division = 1d;
    Double lengthOfVehicle = TrackConstant.WHEEL_LEFT + TrackConstant.WHEEL_RIGHT;

    private volatile boolean isStop;//使用volatile关键字修饰，可以在多线程之间共享，成员变量来控制线程的停止.

    /*@Override
    public void run() {
        try{
        //更改动态参数
        TColorConfigMapper colorConfigMapper = BeanContext.getApplicationContext().getBean(TColorConfigMapper.class);
        getColorMap(colorConfigMapper);

        DamsConstructionMapper damsConstructionMapper = BeanContext.getApplicationContext().getBean(DamsConstructionMapper.class);
        DamsConstruction damsConstruction =damsConstructionMapper.selectByPrimaryKey(Integer.valueOf(key));
        String id = String.valueOf(damsConstruction.getTablename());
        RollingResult rollingResult = rollingResultMap.get(rollingResultKey);
        log.info("RealTimeRedisRightPopTaskZhuang"+Thread.currentThread().getName()+"执行");
        RedisUtil redisUtil = BeanContext.getApplicationContext().getBean(RedisUtil.class);
        //处理i=0的情况
        HashMap<String, RollingData> rollingData02Map = new HashMap();
        HashMap<String,Coordinate> coordinate1Map = new HashMap();
        HashMap<String,Coordinate> coordinate2Map = new HashMap();
        HashMap<String,Integer> xSizeMap = new HashMap();
        HashMap<String,Integer> ySizeMap = new HashMap();
        rollingData02Map.put(carId,new RollingData());
        coordinate1Map.put(carId,new Coordinate());
        coordinate2Map.put(carId,new Coordinate());
        xSizeMap.put(carId,new Integer(0));
        ySizeMap.put(carId,new Integer(0));


        while (!isStop) {
            String redisKey = key + "-" + carId;
            // boolean contains = rollingData02Map.containsValue(carId); //判断集合中是否包含指定的value值
            boolean contains = StringUtils.isNotEmpty(rollingData02Map.get(carId).getVehicleID());
//            log.info(String.format("实时通道 %s车数据===%s", carId, (contains ? "已找到啦啦啦啦啦啦........." : "找不到找不到找不到找不到找不到找不到........")));
            if (!contains) { //如果条件不成立
                boolean bl = false;
                try {
                    bl = redisUtil.hasKey(redisKey);
                } catch (Exception ex) {
                    System.out.print("捕捉到了");
                    Iterator<Map.Entry<String, Session>> entries = users.entrySet().iterator();
                    while (entries.hasNext()) {
                        Map.Entry<String, Session> entry = entries.next();
                        String tablename = entry.getKey().split(":")[1];
                        if (tablename.equalsIgnoreCase(key)) {
                            users.remove(entry.getKey());
                            if (storeHouseMaps2RollingData.containsKey(id)) {
                                storeHouseMaps2RollingData.remove(id);
                                for (int j = 1; j < 4; j++) {
                                    String rediskey = id + "-" + j;
                                    try {
                                        if (redisUtil.hasKey(rediskey)) {
                                            redisUtil.del(rediskey);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                }
                if (bl) {
                    RollingData rollingData01 = (RollingData) redisUtil.rightPop(redisKey);
                    if (StringUtils.isNotNull(rollingData01)) {
                        // todo: 为0时推送 ishistory;//是否为历史 0:实时 1:历史
                        boolean flag = rollingData01.getIshistory() == 0;
                        log.info(String.format("实时通道 推送数据为===%s", (flag ? "实时数据" : "历史数据")));
                        if (flag) {
                            RollingData rollingData02 = (RollingData) redisUtil.rightPop(redisKey);
                            log.info("实时通道 推送数据=======" + ReflectionToStringBuilder.toString(rollingData02, ToStringStyle.MULTI_LINE_STYLE));
                            rollingData02Map.put(carId, rollingData02);
                            double dis = Math.sqrt(Math.pow(rollingData01.getZhuangX() - rollingData02.getZhuangX(), 2) + Math.pow(rollingData01.getZhuangY() - rollingData02.getZhuangY(), 2));
                            if (dis > 0d) {
                                Coordinate[] coordinates04 = new Coordinate[]{
                                        new Coordinate(rollingData01.getZhuangX(), rollingData01.getZhuangY()),
                                        new Coordinate(rollingData02.getZhuangX(), rollingData02.getZhuangY()),
                                };
                                GeometryFactory gf0 = new GeometryFactory();
                                Geometry gfLineString0 = gf0.createLineString(coordinates04);
                                BufferOp bufOp0 = new BufferOp(gfLineString0);
                                bufOp0.setEndCapStyle(BufferOp.CAP_BUTT);
                                //获得线段对应的轨迹多边形
                                Geometry bg0 = bufOp0.getResultGeometry(lengthOfVehicle / 2);
                                Coordinate[] pointList0 = bg0.getCoordinates();
                                //顺逆时针排序
                                GeometryFactory gf2 = new GeometryFactory();
                                MultiPoint mp = gf2.createMultiPoint(pointList0);
                                ConformingDelaunayTriangulationBuilder builder = new ConformingDelaunayTriangulationBuilder();
                                builder.setSites(mp);
                                //实际为GeometryCollection（组成的geometry紧密相连）
                                Geometry ts = builder.getTriangles(gf2);
                                //以0的距离进行缓冲（因为各多边形两两共边），生成一个多边形
                                //此时则将点云构造成了多边形
                                Geometry bgAfter = ts.buffer(0);
                                //计算轨迹多边形外接的正多边形
                                List<Double> xList0 = new LinkedList<Double>();
                                List<Double> yList0 = new LinkedList<Double>();
                                for (int j = 0; j < pointList0.length; j++) {
                                    xList0.add(pointList0[j].x);
                                    yList0.add(pointList0[j].y);
                                }
                                int xListMax0 = (int) Math.ceil((Collections.max(xList0) - shorehouseRange.get(key).getMinOfxList()) / division);
                                int xListMin0 = (int) Math.floor((Collections.min(xList0) - shorehouseRange.get(key).getMinOfxList()) / division);
                                int yListMax0 = (int) Math.ceil((Collections.max(yList0) - shorehouseRange.get(key).getMinOfyList()) / division);
                                int yListMin0 = (int) Math.floor((Collections.min(yList0) - shorehouseRange.get(key).getMinOfyList()) / division);
                                RollingDataRange rollingDataRange0 = new RollingDataRange();
                                rollingDataRange0.setMaxCoordX(Collections.max(xList0));
                                rollingDataRange0.setMinCoordX(Collections.min(xList0));
                                rollingDataRange0.setMaxCoordY(Collections.max(yList0));
                                rollingDataRange0.setMinCoordY(Collections.min(yList0));
                                if (xListMax0 < storeHouseMaps2RollingData.get(storehouseId).length && yListMax0 < storeHouseMaps2RollingData.get(storehouseId)[0].length) {
                                    for (int m = xListMin0; m < xListMax0; m++) {
                                        for (int n = yListMin0; n < yListMax0; n++) {
                                            //生成m,n对应的点坐标
                                            Double xTmp = shorehouseRange.get(key).getMinOfyList() + m * division;
                                            Double yTmp = shorehouseRange.get(key).getMinOfxList() + n * division;
                                            //判断点是否在多边形内
                                            Coordinate point = new Coordinate(xTmp, yTmp);
                                            PointLocator a = new PointLocator();
                                            boolean p1 = a.intersects(point, bgAfter);
                                            if (p1) {
                                                //点在内，就把对应的网格遍数+1
                                                if (m > 0 && n > 0 && m < storeHouseMaps2RollingData.get(storehouseId).length && n < storeHouseMaps2RollingData.get(storehouseId)[0].length) {
                                                    Integer rollingTimes = storeHouseMaps2RollingData.get(storehouseId)[m][n].getRollingTimes();
                                                    calculateRollingtimes(rollingTimes, rollingResult);
                                                    storeHouseMaps2RollingData.get(storehouseId)[m][n].setRollingTimes(storeHouseMaps2RollingData.get(storehouseId)[m][n].getRollingTimes() + 1);
                                                    storeHouseMaps2RollingData.get(storehouseId)[m][n].getElevationList().add(rollingData02.getElevation());
                                                    storeHouseMaps2RollingData.get(storehouseId)[m][n].getAccelerationList().add(rollingData02.getAcceleration());
                                                    storeHouseMaps2RollingData.get(storehouseId)[m][n].getFrequencyList().add(rollingData02.getFrequency());
                                                    storeHouseMaps2RollingData.get(storehouseId)[m][n].getSpeedList().add(rollingData02.getSpeed());
                                                    storeHouseMaps2RollingData.get(storehouseId)[m][n].getTimestampList().add(rollingData02.getTimestamp());
                                                    storeHouseMaps2RollingData.get(storehouseId)[m][n].getVehicleIDList().add(rollingData02.getVehicleID());
                                                    storeHouseMaps2RollingData.get(storehouseId)[m][n].getVibrateValueList().add(rollingData02.getVibrateValue());

                                                    if (rollingData02.getIsVibrate() == 1) {
                                                        //动碾遍数+1
                                                        storeHouseMaps2RollingData.get(storehouseId)[m][n].setIsVibrate(storeHouseMaps2RollingData.get(storehouseId)[m][n].getIsVibrate() + 1);
                                                    } else if (rollingData02.getIsVibrate() == 0) {
                                                        //静碾遍数+1
                                                        storeHouseMaps2RollingData.get(storehouseId)[m][n].setIsNotVibrate(storeHouseMaps2RollingData.get(storehouseId)[m][n].getIsNotVibrate() + 1);
                                                    }

                                                }
                                            }
                                        }
                                    }
                                }
                                int xSize = xListMax0 - xListMin0;
                                int ySize = yListMax0 - yListMin0;
                                BufferedImage bi = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);//INT精确度达到一定,RGB三原色，高度70,宽度150
                                //绘制振动的
                                BufferedImage biForVibrate = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);//INT精确度达到一定,RGB三原色，高度70,宽度150
                                //得到它的绘制环境(这张图片的笔)
                                Graphics2D g2 = (Graphics2D) bi.getGraphics();
                                Graphics2D g2ForVibrate = (Graphics2D) biForVibrate.getGraphics();
                                for (int i = 0; i < xSize; i++) {
                                    for (int j = 0; j < ySize; j++) {
                                        if (i + xListMin0 > 0 && j + yListMin0 > 0 && i + xListMin0 < storeHouseMaps2RollingData.get(storehouseId).length && j + yListMin0 < storeHouseMaps2RollingData.get(storehouseId)[0].length) {
                                            g2.setColor(getColorByCount2(storeHouseMaps2RollingData.get(storehouseId)[i + xListMin0][j + yListMin0].getRollingTimes()));
                                            g2ForVibrate.setColor(getColorByCount2(storeHouseMaps2RollingData.get(storehouseId)[i + xListMin0][j + yListMin0].getIsVibrate()));
                                            //g2.setColor(new Color(225,148, 207));
                                            g2.fillRect(i, j, 1, 1);
                                            g2ForVibrate.fillRect(i, j, 1, 1);
                                        }
                                    }
                                }
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                ByteArrayOutputStream baosForVibrate = new ByteArrayOutputStream();
                                try {
                                    ImageIO.write(bi, "PNG", baos);
                                    ImageIO.write(biForVibrate, "PNG", baosForVibrate);
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                byte[] bytes = baos.toByteArray();//转换成字节
                                byte[] bytesForVibrate = baosForVibrate.toByteArray();//转换成字节
                                String bsae64_string = "data:image/png;base64," + Base64.encodeBase64String(bytes);
                                String bsae64_string_ForVibrate = "data:image/png;base64," + Base64.encodeBase64String(bytesForVibrate);
                                ProjCoordinate projCoordinate10 = new ProjCoordinate(rollingDataRange0.getMinCoordY(), rollingDataRange0.getMinCoordX(), 10);
                                ProjCoordinate projCoordinate20 = new ProjCoordinate(rollingDataRange0.getMaxCoordY(), rollingDataRange0.getMaxCoordX(), 10);
                                ProjCoordinate markPos0 = new ProjCoordinate(rollingData02.getZhuangY(), rollingData02.getZhuangX(), 10);
                                JSONObject result0 = new JSONObject();
                                //计算实时合格率
                                Double rate = Double.valueOf(rollingResult.getTime8() + rollingResult.getTime9() + rollingResult.getTime10() + rollingResult.getTime11() + rollingResult.getTime11Up()) / Double.valueOf(rollingResult.getTime0() + rollingResult.getTime1() + rollingResult.getTime2() + rollingResult.getTime3() + rollingResult.getTime4() + rollingResult.getTime5() + rollingResult.getTime6() + rollingResult.getTime7() + rollingResult.getTime8() + rollingResult.getTime9() + rollingResult.getTime10() + rollingResult.getTime11() + rollingResult.getTime11Up());
                                Double coverRate = Double.valueOf(rollingResult.getTime1() + rollingResult.getTime2() + rollingResult.getTime3() + rollingResult.getTime4() + rollingResult.getTime5() + rollingResult.getTime6() + rollingResult.getTime7() + rollingResult.getTime8() + rollingResult.getTime9() + rollingResult.getTime10() + rollingResult.getTime11() + rollingResult.getTime11Up()) / Double.valueOf(rollingResult.getTime0() + rollingResult.getTime1() + rollingResult.getTime2() + rollingResult.getTime3() + rollingResult.getTime4() + rollingResult.getTime5() + rollingResult.getTime6() + rollingResult.getTime7() + rollingResult.getTime8() + rollingResult.getTime9() + rollingResult.getTime10() + rollingResult.getTime11() + rollingResult.getTime11Up());
                                result0.put("tablename", storehouseId);//图片占的网格编号
                                result0.put("speed", rollingData02.getSpeed());//车辆速度
                                result0.put("elevation", rollingData02.getElevation());//车辆高程
                                result0.put("timestamp", rollingData02.getTimestamp());
                                result0.put("base64", bsae64_string);
                                result0.put("base64ForVibrate", bsae64_string_ForVibrate);
                                result0.put("rollingDataRange", rollingDataRange0);
                                result0.put("pointLeftBottom", projCoordinate10);
                                result0.put("pointRightTop", projCoordinate20);
                                result0.put("height", this.height);
                                result0.put("markPos", markPos0);
                                result0.put("carId", rollingData02.getVehicleID());
                                result0.put("rollingResult", rollingResult);
                                result0.put("rate", rate);//合格率
                                result0.put("coverRate", coverRate);//碾压占比率
                                result0.put("title", damsConstruction.getTitle());//碾压占比率
                                result0.put("ishistory", rollingData02.getIshistory());
                                result0.put("uuid", "唯一标识.....实时1....");
                                if (!users.isEmpty()) {
                                    //遍历websocket连接池
                                    for (String keyOfMap : users.keySet()) {
                                        //获得websocket的storehouseID
                                        String storehouseNameOfWebsocket = keyOfMap.split(":")[0];
                                        String carIdOfWebsocket = keyOfMap.split(":")[1];
                                        //如果storehouseID一致
                                        if (storehouseNameOfWebsocket.equalsIgnoreCase(key) && carIdOfWebsocket.equalsIgnoreCase(rollingData02.getVehicleID())) {
                                            log.info("==========================" + rollingData02.getVehicleID() + "车数据===========================================");
                                            Session session = users.get(keyOfMap);
                                            synchronized (session) {
                                                try {
                                                    session.getBasicRemote().sendText(JSONObject.toJSONString(result0));
                                                } catch (Exception e) {
                                                    try {
                                                        Thread.sleep(100);
                                                        session.getBasicRemote().sendText(JSONObject.toJSONString(result0));
                                                    } catch (InterruptedException | IOException e1) {
                                                        e1.printStackTrace();
                                                    }
                                                    e.printStackTrace();

                                                }
                                            }
                                        }
                                    }
                                }
                                coordinate1Map.put(carId, pointList0[0]);
                                coordinate2Map.put(carId, pointList0[1]);
                            }
                        }
                    }
                }
            } else {
                if (Thread.currentThread().isInterrupted()) {
                    //处理中断逻辑
                    log.info("实时通道 处理中断逻辑===");
                    break;
                }
                redisKey = key + "-" + carId;
                boolean bl2 = false;
                try {
                    bl2 = redisUtil.hasKey(redisKey);
                } catch (Exception ex) {
                    System.out.print("捕捉到了");
                    Iterator<Map.Entry<String, Session>> entries = users.entrySet().iterator();
                    while (entries.hasNext()) {
                        Map.Entry<String, Session> entry = entries.next();
                        String tablename = entry.getKey().split(":")[1];
                        if (tablename.equalsIgnoreCase(key)) {
                            users.remove(entry.getKey());
                            if (storeHouseMaps2RollingData.containsKey(id)) {
                                storeHouseMaps2RollingData.remove(id);
                                for (int j = 1; j < 4; j++) {
                                    String rediskey = id + "-" + j;
                                    try {
                                        if (redisUtil.hasKey(rediskey)) {
                                            redisUtil.del(rediskey);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        redisUtil.del(rediskey);
                                    }
                                }
                            }
                        }
                    }
                }
                if (bl2) {
                    int isDraw = 0;
                    RollingData rollingData1 = rollingData02Map.get(carId);
                    // todo: 为0时推送 ishistory;//是否为历史 0:实时 1:历史
                    boolean flag = rollingData1.getIshistory() == 0;
                    log.info(String.format("实时通道 推送数据为===%s", (flag ? "实时数据" : "历史数据")));
                    if (flag) {
                        //                    log.info("从右侧弹出");
                        RollingData rollingData2 = (RollingData) redisUtil.rightPop(redisKey);
                        if (StringUtils.isNotNull(rollingData2)) {

                            //                    log.info("数据为：" + rollingData2.toString());
                            double distance = Math.sqrt(Math.pow(rollingData1.getZhuangX() - rollingData2.getZhuangX(), 2) + Math.pow(rollingData1.getZhuangY() - rollingData2.getZhuangY(), 2));
                            //todo:添加R树判断 rollingData1和RollingData2不属于当前单元工程的R树,不进行下边绘图处理
                            //前后大于5个像素再绘制，像素太小绘图没有意义，图上完全看不到，增加消耗
                            if (distance < TrackConstant.MIN_DIS) {
                                log.error("实时通道内-==================前后距离过小:" + distance);
                            } else if(distance > TrackConstant.MAX_DIS){
                                log.error("实时通道内-==================前后距离过大:" + distance + " " + rollingData1.getOrderNum() + "-" + rollingData2.getOrderNum());
                                rollingData1 = rollingData2;
                            } else{
//                            if (distance > 5) {
                                Coordinate[] coordinates4 = new Coordinate[]{
                                        new Coordinate(rollingData1.getZhuangX(), rollingData1.getZhuangY()),
                                        new Coordinate(rollingData2.getZhuangX(), rollingData2.getZhuangY()),
                                };
                                GeometryFactory gf = new GeometryFactory();
                                Geometry gfLineString = gf.createLineString(coordinates4);
                                //缓冲区建立
                                BufferOp bufOp = new BufferOp(gfLineString);
                                bufOp.setEndCapStyle(BufferOp.CAP_BUTT);
                                //获得线段对应的轨迹多边形
                                Geometry bg = bufOp.getResultGeometry(lengthOfVehicle / 2);
                                Coordinate[] pointList = bg.getCoordinates();
                                pointList[2] = coordinate2Map.get(carId);
                                pointList[3] = coordinate1Map.get(carId);

                                //顺逆时针排序
                                GeometryFactory gf3 = new GeometryFactory();
                                MultiPoint mp1 = gf3.createMultiPoint(pointList);
                                ConformingDelaunayTriangulationBuilder builder2 = new ConformingDelaunayTriangulationBuilder();

                                builder2.setSites(mp1);
                                //实际为GeometryCollection（组成的geometry紧密相连）
                                Geometry ts1 = builder2.getTriangles(gf3);

                                //以0的距离进行缓冲（因为各多边形两两共边），生成一个多边形
                                //此时则将点云构造成了多边形
                                Geometry bgAfter1 = ts1.buffer(0);
                                //计算轨迹多边形外接的正多边形
                                List<Double> xList1 = new LinkedList<Double>();
                                List<Double> yList1 = new LinkedList<Double>();
                                for (int j = 0; j < pointList.length; j++) {
                                    xList1.add(pointList[j].x);
                                    yList1.add(pointList[j].y);
                                }
                                int xListMax = (int) Math.ceil((Collections.max(xList1) - shorehouseRange.get(key).getMinOfxList()) / division);
                                int xListMin = (int) Math.floor((Collections.min(xList1) - shorehouseRange.get(key).getMinOfxList()) / division);
                                int yListMax = (int) Math.ceil((Collections.max(yList1) - shorehouseRange.get(key).getMinOfyList()) / division);
                                int yListMin = (int) Math.floor((Collections.min(yList1) - shorehouseRange.get(key).getMinOfyList()) / division);
                                //rollingData1和rollingData2构建的轨迹多边形的外接矩形四个角点坐标
                                List<Integer> index1 = new LinkedList<>();
                                index1.add((int) Math.ceil(xListMin / 100));
                                index1.add((int) Math.ceil(yListMin / 100));

                                List<Integer> index2 = new LinkedList<>();
                                index2.add((int) Math.ceil(xListMax / 100));
                                index2.add((int) Math.ceil(yListMin / 100));

                                List<Integer> index3 = new LinkedList<>();
                                index3.add((int) Math.ceil(xListMin / 100));
                                index3.add((int) Math.ceil(yListMax / 100));

                                List<Integer> index4 = new LinkedList<>();
                                index4.add((int) Math.ceil(xListMax / 100));
                                index4.add((int) Math.ceil(yListMax / 100));

                                Set<List<Integer>> index = new LinkedHashSet<>();
                                index.add(index1);
                                index.add(index2);
                                index.add(index3);
                                index.add(index4);
                                //通过轨迹多边形获得当前碾压区域的左上角点和右下角点
                                RollingDataRange rollingDataRange = new RollingDataRange();
                                rollingDataRange.setMaxCoordX(Collections.max(xList1));
                                rollingDataRange.setMinCoordX(Collections.min(xList1));
                                rollingDataRange.setMaxCoordY(Collections.max(yList1));
                                rollingDataRange.setMinCoordY(Collections.min(yList1));
                                //遍历轨迹多边形 判断碾压遍数和颜色
                                for (int m = xListMin; m < xListMax; m++) {
                                    for (int n = yListMin; n < yListMax; n++) {
                                        //生成m,n对应的点坐标
                                        Double xTmp = shorehouseRange.get(key).getMinOfxList().doubleValue() + m * division;
                                        Double yTmp = shorehouseRange.get(key).getMinOfyList().doubleValue() + n * division;
                                        //判断点是否在多边形内
                                        Coordinate point1 = new Coordinate(xTmp, yTmp);
                                        PointLocator a1 = new PointLocator();
                                        boolean p1 = a1.intersects(point1, bgAfter1);
                                        if (p1) {
                                            //只画在边界内的
                                            //点在内，就把对应的网格遍数+1
                                            if (m > 0 && n > 0 && m < storeHouseMaps2RollingData.get(storehouseId).length && n < storeHouseMaps2RollingData.get(storehouseId)[0].length) {
                                                isDraw = 1;
                                                Integer rollingTimes = storeHouseMaps2RollingData.get(storehouseId)[m][n].getRollingTimes();
                                                calculateRollingtimes(rollingTimes, rollingResult);
                                                storeHouseMaps2RollingData.get(storehouseId)[m][n].setRollingTimes(storeHouseMaps2RollingData.get(storehouseId)[m][n].getRollingTimes() + 1);
                                                storeHouseMaps2RollingData.get(storehouseId)[m][n].getElevationList().add(rollingData2.getElevation());
                                                storeHouseMaps2RollingData.get(storehouseId)[m][n].getAccelerationList().add(rollingData2.getAcceleration());
                                                storeHouseMaps2RollingData.get(storehouseId)[m][n].getFrequencyList().add(rollingData2.getFrequency());
                                                storeHouseMaps2RollingData.get(storehouseId)[m][n].getSpeedList().add(rollingData2.getSpeed());
                                                storeHouseMaps2RollingData.get(storehouseId)[m][n].getTimestampList().add(rollingData2.getTimestamp());
                                                storeHouseMaps2RollingData.get(storehouseId)[m][n].getVehicleIDList().add(rollingData2.getVehicleID());
                                                storeHouseMaps2RollingData.get(storehouseId)[m][n].getVibrateValueList().add(rollingData2.getVibrateValue());

                                                if (rollingData2.getIsVibrate() == 1) {
                                                    //动碾遍数+1
                                                    storeHouseMaps2RollingData.get(storehouseId)[m][n].setIsVibrate(storeHouseMaps2RollingData.get(storehouseId)[m][n].getIsVibrate() + 1);
                                                } else if (rollingData2.getIsVibrate() == 0) {
                                                    //静碾遍数+1
                                                    storeHouseMaps2RollingData.get(storehouseId)[m][n].setIsNotVibrate(storeHouseMaps2RollingData.get(storehouseId)[m][n].getIsNotVibrate() + 1);
                                                }
                                            }
                                        }
                                    }
                                }
                                coordinate1Map.put(carId, pointList[0]);
                                coordinate2Map.put(carId, pointList[1]);
                                rollingData02Map.put(carId, rollingData2);
                                //根据index中的范围进行输出
                                for (List<Integer> item : index) {
                                    //先把范围确定了
                                    int xMin = item.get(0) * 100 - 5;
                                    int xMax = (item.get(0) + 1) * 100 - 1 + 5;
                                    int yMin = item.get(1) * 100 - 5;
                                    int yMax = (item.get(1) + 1) * 100 - 1 + 5;

                                    BufferedImage biLast = new BufferedImage(xMax - xMin + 1, yMax - yMin + 1, BufferedImage.TYPE_INT_ARGB);
                                    Graphics2D g2Last = (Graphics2D) biLast.getGraphics();
                                    //增添振动统计的图像
                                    BufferedImage biLastForVibrate = new BufferedImage(xMax - xMin + 1, yMax - yMin + 1, BufferedImage.TYPE_INT_ARGB);
                                    Graphics2D g2LastForVibrate = (Graphics2D) biLastForVibrate.getGraphics();
                                    synchronized (storeHouseMaps2RollingData) {
                                        int XMAX = storeHouseMaps2RollingData.get(storehouseId).length;
                                        int YMAX = storeHouseMaps2RollingData.get(storehouseId)[0].length;
                                        for (int i = xMin; i < xMax + 1; i++) {
                                            for (int j = yMin; j < yMax + 1; j++) {
                                                if (i > 0 && j > 0 && i < XMAX && j < YMAX) {
                                                    if (Integer.valueOf(storeHouseMaps2RollingData.get(storehouseId)[i][j].getRollingTimes()) > 0) {
                                                        int mmm = 0;
                                                        Integer times = storeHouseMaps2RollingData.get(storehouseId)[i][j].getRollingTimes();
                                                        Integer vibrateTimes = storeHouseMaps2RollingData.get(storehouseId)[i][j].getIsVibrate();
                                                        g2Last.setColor(getColorByCount2(times));
                                                        g2Last.fillRect(i - xMin, j - yMin, 1, 1);
                                                        g2LastForVibrate.setColor(getColorByCount2(vibrateTimes));
                                                        g2LastForVibrate.fillRect(i - xMin, j - yMin, 1, 1);
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    ByteArrayOutputStream baosForVibrate = new ByteArrayOutputStream();
                                    try {
                                        ImageIO.write(biLast, "PNG", baos);
                                        ImageIO.write(biLastForVibrate, "PNG", baosForVibrate);
                                    } catch (IOException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    byte[] bytes = baos.toByteArray();//转换成字节
                                    byte[] bytesForVibrate = baosForVibrate.toByteArray();//转换成字节
                                    String bsae64_string = "data:image/png;base64," + Base64.encodeBase64String(bytes);
                                    String bsae64_string_ForVibrate = "data:image/png;base64," + Base64.encodeBase64String(bytesForVibrate);
                                    ProjCoordinate projCoordinate1 = new ProjCoordinate(shorehouseRange.get(key).getMinOfyList() + yMin * division, shorehouseRange.get(key).getMinOfxList() + xMin * division, 10);
                                    ProjCoordinate projCoordinate2 = new ProjCoordinate(shorehouseRange.get(key).getMinOfyList() + yMax * division, shorehouseRange.get(key).getMinOfxList() + xMax * division, 10);
                                    ProjCoordinate markPos = new ProjCoordinate(rollingData2.getZhuangY(), rollingData2.getZhuangX(), 10);
                                    JSONObject result = new JSONObject();
                                    Double rate = Double.valueOf(rollingResult.getTime8() + rollingResult.getTime9() + rollingResult.getTime10() + rollingResult.getTime11() + rollingResult.getTime11Up()) / Double.valueOf(rollingResult.getTime0() + rollingResult.getTime1() + rollingResult.getTime2() + rollingResult.getTime3() + rollingResult.getTime4() + rollingResult.getTime5() + rollingResult.getTime6() + rollingResult.getTime7() + rollingResult.getTime8() + rollingResult.getTime9() + rollingResult.getTime10() + rollingResult.getTime11() + rollingResult.getTime11Up());
                                    Double coverRate = Double.valueOf(rollingResult.getTime1() + rollingResult.getTime2() + rollingResult.getTime3() + rollingResult.getTime4() + rollingResult.getTime5() + rollingResult.getTime6() + rollingResult.getTime7() + rollingResult.getTime8() + rollingResult.getTime9() + rollingResult.getTime10() + rollingResult.getTime11() + rollingResult.getTime11Up()) / Double.valueOf(rollingResult.getTime0() + rollingResult.getTime1() + rollingResult.getTime2() + rollingResult.getTime3() + rollingResult.getTime4() + rollingResult.getTime5() + rollingResult.getTime6() + rollingResult.getTime7() + rollingResult.getTime8() + rollingResult.getTime9() + rollingResult.getTime10() + rollingResult.getTime11() + rollingResult.getTime11Up());
                                    Double VibrateValue = rollingData2.getVibrateValue();
                                    int iszhen = 0;
                                    if (VibrateValue > VibrateValue0) {
                                        iszhen = 1;
                                    }

                                    result.put("tablename", storehouseId);//图片占的网格编号
                                    result.put("index", item);//图片占的网格编号
                                    result.put("iszhen", iszhen);
                                    result.put("rate", rate);//合格率
                                    result.put("speed", rollingData2.getSpeed());//车辆速度
                                    result.put("elevation", rollingData2.getElevation());//车辆高程
                                    result.put("timestamp", rollingData2.getTimestamp());
                                    result.put("base64", bsae64_string);
                                    result.put("base64ForVibrate", bsae64_string_ForVibrate);
                                    result.put("rollingDataRange", rollingDataRange);
                                    result.put("pointLeftBottom", projCoordinate1);
                                    result.put("pointRightTop", projCoordinate2);
                                    result.put("height", this.height);
                                    result.put("markPos", markPos);
                                    result.put("carId", rollingData2.getVehicleID());
                                    result.put("rollingResult", rollingResult);
                                    result.put("coverRate", coverRate);
                                    result.put("title", damsConstruction.getTitle());
                                    result.put("angle", rollingData2.getAngle());
                                    result.put("ishistory", rollingData2.getIshistory());
                                    result.put("uuid", "唯一标识.....实时2....");

                                    if (!users.isEmpty() && isDraw == 1) {
                                        //遍历websocket连接池
                                        for (String keyOfMap : users.keySet()) {
                                            //获得websocket的storehouseID
                                            String storehouseNameOfWebsocket = keyOfMap.split(":")[0];
                                            String carIdOfWebsocket = keyOfMap.split(":")[1];
                                            //如果storehouseID一致
                                            if (storehouseNameOfWebsocket.equalsIgnoreCase(key) && carIdOfWebsocket.equalsIgnoreCase(rollingData2.getVehicleID())) {
                                                log.info("==========================" + rollingData2.getVehicleID() + "车数据===========================================");
                                                //如果storehouseID一致
                                                Session session = users.get(keyOfMap);
                                                synchronized (session) {
                                                    try {
                                                        session.getBasicRemote().sendText(JSONObject.toJSONString(result));
                                                    } catch (Exception e) {
                                                        try {
                                                            Thread.sleep(100);
                                                            session.getBasicRemote().sendText(JSONObject.toJSONString(result));
                                                        } catch (InterruptedException | IOException e1) {
                                                            e1.printStackTrace();
                                                        }
                                                        e.printStackTrace();

                                                    }
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                        }

                    }
                }
            }
        }

        }catch (NullPointerException pointerException){
            log.error(String.format("实时通道 %s车", carId)+"异常信息的...."+pointerException.getMessage());
        }catch (Exception e){
            log.error(String.format("实时通道 %s车", carId)+"异常信息的...."+e.getMessage());
        }

        }*/

    public void run() {
        int keep = 0;
        //更改动态参数
        TColorConfigMapper colorConfigMapper = BeanContext.getApplicationContext().getBean(TColorConfigMapper.class);
        getColorMap(colorConfigMapper);
        TDamsconstructionMapper damsConstructionMapper = BeanContext.getApplicationContext().getBean(TDamsconstructionMapper.class);
        DamsConstruction damsConstruction = damsConstructionMapper.selectByPrimaryKey(Integer.valueOf(key));
        RollingResult rollingResult = rollingResultMap.get(rollingResultKey);
        //处理i=0的情况
        HashMap<String, RollingData> rollingData02Map = new HashMap();
        HashMap<String, Coordinate> coordinate1Map = new HashMap();
        HashMap<String, Coordinate> coordinate2Map = new HashMap();
        rollingData02Map.put(carId, new RollingData());
        coordinate1Map.put(carId, new Coordinate());
        coordinate2Map.put(carId, new Coordinate());

        while (!isStop) {

            long begin = System.currentTimeMillis();

            try {

                if (Integer.valueOf(carId) < 2) {
//                    log.info(String.format("实时通道 %s车", carId));
                }
                String dataKey = key + "-" + carId;
                //存在实时数据
                if (realTimeDataMap.containsKey(dataKey)) {
//                    List<RollingData> dataList = realTimeDataMap.get(dataKey);
                    Queue<RollingData> dataList = realTimeDataMap.get(dataKey);
                    boolean flag = false;
                    // 总数据条数
                    int dataSize = dataList.size();

                    boolean contains = StringUtils.isNotEmpty(rollingData02Map.get(carId).getVehicleID());
                    if (!contains) { //如果条件不成立
                        if (dataSize >= 2) {
                        /*RollingData rollingData01 = dataList.get(0);
                        dataList.remove(0);*/
                            RollingData rollingData01 = dataList.poll();//返回第一个元素，并在队列中删除
                        /*RollingData rollingData02 = dataList.get(0);
                        dataList.remove(0);*/
                            RollingData rollingData02 = dataList.poll();//返回第一个元素，并在队列中删除
                            flag = one(rollingData01, rollingData02, rollingData02Map, coordinate1Map, coordinate2Map, damsConstruction, rollingResult);
                        }
                    } else {
                        if (dataSize > 0) {
                            flag = true;
                        }
                    }

                    //  循环二次数据轨迹
                    if (flag) {
                        if (StringUtils.isNotEmpty(dataList)) {
                            /*Iterator<RollingData> it = dataList.iterator();
                            while(it.hasNext()){
                                RollingData rollingData2 = it.next();
                                it.remove();
                                if(Thread.currentThread().isInterrupted()){
                                    log.info("历史数据通道.......处理中断逻辑《《《《《《《..........");
                                    break;
                                }
                                two(rollingData02Map.get(carId),rollingData2,rollingData02Map,coordinate1Map,coordinate2Map,damsConstruction,rollingResult);
                            }*/
                            /*RollingData rollingData1 = rollingData02Map.get(carId);
                            for (RollingData rollingData2:dataList){
                                if(Thread.currentThread().isInterrupted()){
                                    log.info("历史数据通道.......处理中断逻辑《《《《《《《..........");
                                    break;
                                }
                                two(rollingData1,rollingData2,rollingData02Map,coordinate1Map,coordinate2Map,damsConstruction,rollingResult);
                            }*/
                            if (Thread.currentThread().isInterrupted()) {
                                log.info("历史数据通道.......处理中断逻辑《《《《《《《..........");
                                break;
                            }
                            RollingData rollingData1 = rollingData02Map.get(carId);
                            RollingData rollingData2 = dataList.poll();//返回第一个元素，并在队列中删除
                            two(rollingData1, rollingData2, rollingData02Map, coordinate1Map, coordinate2Map, damsConstruction, rollingResult);
                        } else {
                            keep++;
                        }
                    } else {
                        keep++;
                    }

                } else {
                    keep++;
                }
                //连续50次无数据时 将睡眠5秒
                if (keep > 50) {
                    log.info(String.format("==============实时通道 %s车,,,,睡眠5秒钟...........", carId));
                    Thread.sleep(5000l);
                    keep = 0;
                    //int i = 0/0;
                }
            } catch (NullPointerException pointerException) {
                log.error(String.format("实时通道 %s车", carId) + "异常信息的...." + pointerException.getMessage());
            } catch (Exception e) {
                log.error(String.format("实时通道 %s车", carId) + "异常信息的...." + e.getMessage());
            }

            long end = System.currentTimeMillis();
            System.out.println("==============================绘制轨迹点线程执行耗时:" + (end - begin) + "毫秒");
        }
    }


    private boolean one(RollingData rollingData01, RollingData rollingData02, HashMap<String, RollingData> rollingData02Map,
                        HashMap<String, Coordinate> coordinate1Map, HashMap<String, Coordinate> coordinate2Map,
                        DamsConstruction damsConstruction, RollingResult rollingResult) {
        if (StringUtils.isNotNull(rollingData01)) {
            // todo: 为0时推送 ishistory;//是否为历史 0:实时 1:历史
            boolean flag = rollingData01.getIshistory() == 0;
            log.info(String.format("实时通道 推送数据为===%s", (flag ? "实时数据" : "历史数据")));
            if (flag) {
                log.info("实时通道 推送数据=======" + ReflectionToStringBuilder.toString(rollingData02, ToStringStyle.MULTI_LINE_STYLE));
                rollingData02Map.put(carId, rollingData02);
                double dis = Math.sqrt(Math.pow(rollingData01.getZhuangX() - rollingData02.getZhuangX(), 2) + Math.pow(rollingData01.getZhuangY() - rollingData02.getZhuangY(), 2));
                if (dis > 0d) {
                    Coordinate[] coordinates04 = new Coordinate[]{
                            new Coordinate(rollingData01.getZhuangX(), rollingData01.getZhuangY()),
                            new Coordinate(rollingData02.getZhuangX(), rollingData02.getZhuangY()),
                    };
                    GeometryFactory gf0 = new GeometryFactory();
                    Geometry gfLineString0 = gf0.createLineString(coordinates04);
                    BufferOp bufOp0 = new BufferOp(gfLineString0);
                    bufOp0.setEndCapStyle(BufferOp.CAP_BUTT);
                    //获得线段对应的轨迹多边形
                    Geometry bg0 = bufOp0.getResultGeometry(lengthOfVehicle / 2);
                    Coordinate[] pointList0 = bg0.getCoordinates();
                    //顺逆时针排序
                    GeometryFactory gf2 = new GeometryFactory();
                    MultiPoint mp = gf2.createMultiPoint(pointList0);
                    ConformingDelaunayTriangulationBuilder builder = new ConformingDelaunayTriangulationBuilder();
                    builder.setSites(mp);
                    //实际为GeometryCollection（组成的geometry紧密相连）
                    Geometry ts = builder.getTriangles(gf2);
                    //以0的距离进行缓冲（因为各多边形两两共边），生成一个多边形
                    //此时则将点云构造成了多边形
                    Geometry bgAfter = ts.buffer(0);
                    //计算轨迹多边形外接的正多边形
                    List<Double> xList0 = new LinkedList<Double>();
                    List<Double> yList0 = new LinkedList<Double>();
                    for (int j = 0; j < pointList0.length; j++) {
                        xList0.add(pointList0[j].x);
                        yList0.add(pointList0[j].y);
                    }
                    int xListMax0 = (int) Math.ceil((Collections.max(xList0) - shorehouseRange.get(key).getMinOfxList()) / division);
                    int xListMin0 = (int) Math.floor((Collections.min(xList0) - shorehouseRange.get(key).getMinOfxList()) / division);
                    int yListMax0 = (int) Math.ceil((Collections.max(yList0) - shorehouseRange.get(key).getMinOfyList()) / division);
                    int yListMin0 = (int) Math.floor((Collections.min(yList0) - shorehouseRange.get(key).getMinOfyList()) / division);
                    RollingDataRange rollingDataRange0 = new RollingDataRange();
                    rollingDataRange0.setMaxCoordX(Collections.max(xList0));
                    rollingDataRange0.setMinCoordX(Collections.min(xList0));
                    rollingDataRange0.setMaxCoordY(Collections.max(yList0));
                    rollingDataRange0.setMinCoordY(Collections.min(yList0));
                    if (xListMax0 < storeHouseMaps2RollingData.get(storehouseId).length && yListMax0 < storeHouseMaps2RollingData.get(storehouseId)[0].length) {
                        for (int m = xListMin0; m < xListMax0; m++) {
                            for (int n = yListMin0; n < yListMax0; n++) {
                                //生成m,n对应的点坐标
                                Double xTmp = shorehouseRange.get(key).getMinOfyList() + m * division;
                                Double yTmp = shorehouseRange.get(key).getMinOfxList() + n * division;
                                //判断点是否在多边形内
                                Coordinate point = new Coordinate(xTmp, yTmp);
                                PointLocator a = new PointLocator();
                                boolean p1 = a.intersects(point, bgAfter);
                                if (p1) {
                                    //点在内，就把对应的网格遍数+1
                                    if (m > 0 && n > 0 && m < storeHouseMaps2RollingData.get(storehouseId).length && n < storeHouseMaps2RollingData.get(storehouseId)[0].length) {
                                        Integer rollingTimes = storeHouseMaps2RollingData.get(storehouseId)[m][n].getRollingTimes();
                                        calculateRollingtimes(rollingTimes, rollingResult);
                                        storeHouseMaps2RollingData.get(storehouseId)[m][n].setRollingTimes(storeHouseMaps2RollingData.get(storehouseId)[m][n].getRollingTimes() + 1);
                                        storeHouseMaps2RollingData.get(storehouseId)[m][n].getElevationList().add(rollingData02.getElevation());
                                        storeHouseMaps2RollingData.get(storehouseId)[m][n].getAccelerationList().add(rollingData02.getAcceleration());
                                        storeHouseMaps2RollingData.get(storehouseId)[m][n].getFrequencyList().add(rollingData02.getFrequency());
                                        storeHouseMaps2RollingData.get(storehouseId)[m][n].getSpeedList().add(rollingData02.getSpeed());
                                        storeHouseMaps2RollingData.get(storehouseId)[m][n].getTimestampList().add(rollingData02.getTimestamp());
                                        storeHouseMaps2RollingData.get(storehouseId)[m][n].getVehicleIDList().add(rollingData02.getVehicleID());
                                        storeHouseMaps2RollingData.get(storehouseId)[m][n].getVibrateValueList().add(rollingData02.getVibrateValue());

                                        if (rollingData02.getIsVibrate() == 1) {
                                            //动碾遍数+1
                                            storeHouseMaps2RollingData.get(storehouseId)[m][n].setIsVibrate(storeHouseMaps2RollingData.get(storehouseId)[m][n].getIsVibrate() + 1);
                                        } else if (rollingData02.getIsVibrate() == 0) {
                                            //静碾遍数+1
                                            storeHouseMaps2RollingData.get(storehouseId)[m][n].setIsNotVibrate(storeHouseMaps2RollingData.get(storehouseId)[m][n].getIsNotVibrate() + 1);
                                        }

                                    }
                                }
                            }
                        }
                    }
                    int xSize = xListMax0 - xListMin0;
                    int ySize = yListMax0 - yListMin0;
                    BufferedImage bi = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);//INT精确度达到一定,RGB三原色，高度70,宽度150
                    //绘制振动的
                    //BufferedImage biForVibrate = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);//INT精确度达到一定,RGB三原色，高度70,宽度150
                    //得到它的绘制环境(这张图片的笔)
                    Graphics2D g2 = (Graphics2D) bi.getGraphics();
                    //Graphics2D g2ForVibrate = (Graphics2D) biForVibrate.getGraphics();
                    for (int i = 0; i < xSize; i++) {
                        for (int j = 0; j < ySize; j++) {
                            if (i + xListMin0 > 0 && j + yListMin0 > 0 && i + xListMin0 < storeHouseMaps2RollingData.get(storehouseId).length && j + yListMin0 < storeHouseMaps2RollingData.get(storehouseId)[0].length) {
                                g2.setColor(getColorByCount2(storeHouseMaps2RollingData.get(storehouseId)[i + xListMin0][j + yListMin0].getRollingTimes()));
                                //g2ForVibrate.setColor(getColorByCount2(storeHouseMaps2RollingData.get(storehouseId)[i + xListMin0][j + yListMin0].getIsVibrate()));
                                //g2.setColor(new Color(225,148, 207));
                                g2.fillRect(i, j, 1, 1);
                                //g2ForVibrate.fillRect(i, j, 1, 1);
                            }
                        }
                    }
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    //ByteArrayOutputStream baosForVibrate = new ByteArrayOutputStream();
                    try {
                        ImageIO.write(bi, "PNG", baos);
                        //ImageIO.write(biForVibrate, "PNG", baosForVibrate);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    byte[] bytes = baos.toByteArray();//转换成字节
                    //byte[] bytesForVibrate = baosForVibrate.toByteArray();//转换成字节
                    String bsae64_string = "data:image/png;base64," + Base64.encodeBase64String(bytes);
                    //String bsae64_string_ForVibrate = "data:image/png;base64," + Base64.encodeBase64String(bytesForVibrate);
                    ProjCoordinate projCoordinate10 = new ProjCoordinate(rollingDataRange0.getMinCoordY(), rollingDataRange0.getMinCoordX(), 10);
                    ProjCoordinate projCoordinate20 = new ProjCoordinate(rollingDataRange0.getMaxCoordY(), rollingDataRange0.getMaxCoordX(), 10);
                    ProjCoordinate markPos0 = new ProjCoordinate(rollingData02.getZhuangY(), rollingData02.getZhuangX(), 10);
                    JSONObject result0 = new JSONObject();
                    //计算实时合格率
                    Double rate = Double.valueOf(rollingResult.getTime8() + rollingResult.getTime9() + rollingResult.getTime10() + rollingResult.getTime11() + rollingResult.getTime11Up()) / Double.valueOf(rollingResult.getTime0() + rollingResult.getTime1() + rollingResult.getTime2() + rollingResult.getTime3() + rollingResult.getTime4() + rollingResult.getTime5() + rollingResult.getTime6() + rollingResult.getTime7() + rollingResult.getTime8() + rollingResult.getTime9() + rollingResult.getTime10() + rollingResult.getTime11() + rollingResult.getTime11Up());
                    Double coverRate = Double.valueOf(rollingResult.getTime1() + rollingResult.getTime2() + rollingResult.getTime3() + rollingResult.getTime4() + rollingResult.getTime5() + rollingResult.getTime6() + rollingResult.getTime7() + rollingResult.getTime8() + rollingResult.getTime9() + rollingResult.getTime10() + rollingResult.getTime11() + rollingResult.getTime11Up()) / Double.valueOf(rollingResult.getTime0() + rollingResult.getTime1() + rollingResult.getTime2() + rollingResult.getTime3() + rollingResult.getTime4() + rollingResult.getTime5() + rollingResult.getTime6() + rollingResult.getTime7() + rollingResult.getTime8() + rollingResult.getTime9() + rollingResult.getTime10() + rollingResult.getTime11() + rollingResult.getTime11Up());
                    result0.put("tablename", storehouseId);//图片占的网格编号
                    result0.put("speed", rollingData02.getSpeed());//车辆速度
                    result0.put("elevation", rollingData02.getElevation());//车辆高程
                    result0.put("timestamp", rollingData02.getTimestamp());
                    result0.put("base64", bsae64_string);
                    //result0.put("base64ForVibrate", bsae64_string_ForVibrate);
                    result0.put("rollingDataRange", rollingDataRange0);
                    result0.put("pointLeftBottom", projCoordinate10);
                    result0.put("pointRightTop", projCoordinate20);
                    result0.put("height", this.height);
                    result0.put("markPos", markPos0);
                    result0.put("carId", rollingData02.getVehicleID());
                    result0.put("rollingResult", rollingResult);
                    result0.put("rate", rate);//合格率
                    result0.put("coverRate", coverRate);//碾压占比率
                    result0.put("title", damsConstruction.getTitle());//碾压占比率
                    result0.put("ishistory", rollingData02.getIshistory());
                    result0.put("uuid", "唯一标识.....实时1....");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date t = new Date();
                    t.setTime(rollingData02.getTimestamp());
                    result0.put("timestamp", sdf.format(t));
                    result0.put("amplitude", rollingData02.getVibrateValue());


                    if (!users.isEmpty()) {
                        //遍历websocket连接池
                        for (String keyOfMap : users.keySet()) {
                            //获得websocket的storehouseID
                            String storehouseNameOfWebsocket = keyOfMap.split(":")[0];
                            String carIdOfWebsocket = keyOfMap.split(":")[1];
                            //如果storehouseID一致
                            if (storehouseNameOfWebsocket.equalsIgnoreCase(key) && carIdOfWebsocket.equalsIgnoreCase(rollingData02.getVehicleID())) {
                                //    log.info("==========================" + rollingData02.getVehicleID() + "车数据===========================================");
                                Session session = users.get(keyOfMap);
                                synchronized (session) {
                                    try {
                                        session.getBasicRemote().sendText(JSONObject.toJSONString(result0));
                                        return true;
                                    } catch (Exception e) {
                                        try {
                                            Thread.sleep(100);
                                            session.getBasicRemote().sendText(JSONObject.toJSONString(result0));
                                            return true;
                                        } catch (InterruptedException | IOException e1) {
                                            e1.printStackTrace();
                                        }
                                        e.printStackTrace();

                                    }
                                }
                            }
                        }
                    }
                    coordinate1Map.put(carId, pointList0[0]);
                    coordinate2Map.put(carId, pointList0[1]);
                }
            }
        }
        return false;
    }

    private void two(RollingData rollingData1, RollingData rollingData2, HashMap<String, RollingData> rollingData02Map,
                     HashMap<String, Coordinate> coordinate1Map, HashMap<String, Coordinate> coordinate2Map,
                     DamsConstruction damsConstruction, RollingResult rollingResult) {
        int isDraw = 0;
        // todo: 为0时推送 ishistory;//是否为历史 0:实时 1:历史
        boolean flag = rollingData1.getIshistory() == 0;
        log.info(String.format("实时通道 推送数据为===%s", (flag ? "实时数据" : "历史数据")));
        if (flag) {
            //                    log.info("从右侧弹出");
//                            RollingData rollingData2 = (RollingData) redisUtil.rightPop(redisKey);
            if (StringUtils.isNotNull(rollingData2)) {
                //                    log.info("数据为：" + rollingData2.toString());
                double distance = Math.sqrt(Math.pow(rollingData1.getZhuangX() - rollingData2.getZhuangX(), 2) + Math.pow(rollingData1.getZhuangY() - rollingData2.getZhuangY(), 2));
                //todo:添加R树判断 rollingData1和RollingData2不属于当前单元工程的R树,不进行下边绘图处理
                //前后大于5个像素再绘制，像素太小绘图没有意义，图上完全看不到，增加消耗
                if (distance < TrackConstant.MIN_DIS) {
                    //   log.error("实时通道内-==================前后距离过小:" + distance);
                } else if (distance > TrackConstant.MAX_DIS) {
                    //   log.error("实时通道内-==================前后距离过大:" + distance + " " + rollingData1.getOrderNum() + "-" + rollingData2.getOrderNum());
                    rollingData1 = rollingData2;
                    rollingData02Map.put(carId, rollingData2);

                } else {
//                            if (distance > 5) {
                    Coordinate[] coordinates4 = new Coordinate[]{
                            new Coordinate(rollingData1.getZhuangX(), rollingData1.getZhuangY()),
                            new Coordinate(rollingData2.getZhuangX(), rollingData2.getZhuangY()),
                    };
                    GeometryFactory gf = new GeometryFactory();
                    Geometry gfLineString = gf.createLineString(coordinates4);
                    //缓冲区建立
                    BufferOp bufOp = new BufferOp(gfLineString);
                    bufOp.setEndCapStyle(BufferOp.CAP_BUTT);
                    //获得线段对应的轨迹多边形
                    Geometry bg = bufOp.getResultGeometry(lengthOfVehicle / 2);
                    Coordinate[] pointList = bg.getCoordinates();
                    pointList[2] = coordinate2Map.get(carId);
                    pointList[3] = coordinate1Map.get(carId);

                    //顺逆时针排序
                    GeometryFactory gf3 = new GeometryFactory();
                    MultiPoint mp1 = gf3.createMultiPoint(pointList);
                    ConformingDelaunayTriangulationBuilder builder2 = new ConformingDelaunayTriangulationBuilder();

                    builder2.setSites(mp1);
                    //实际为GeometryCollection（组成的geometry紧密相连）
                    Geometry ts1 = builder2.getTriangles(gf3);

                    //以0的距离进行缓冲（因为各多边形两两共边），生成一个多边形
                    //此时则将点云构造成了多边形
                    Geometry bgAfter1 = ts1.buffer(0);
                    //计算轨迹多边形外接的正多边形
                    List<Double> xList1 = new LinkedList<Double>();
                    List<Double> yList1 = new LinkedList<Double>();
                    for (int j = 0; j < pointList.length; j++) {
                        xList1.add(pointList[j].x);
                        yList1.add(pointList[j].y);
                    }
                    int xListMax = (int) Math.ceil((Collections.max(xList1) - shorehouseRange.get(key).getMinOfxList()) / division);
                    int xListMin = (int) Math.floor((Collections.min(xList1) - shorehouseRange.get(key).getMinOfxList()) / division);
                    int yListMax = (int) Math.ceil((Collections.max(yList1) - shorehouseRange.get(key).getMinOfyList()) / division);
                    int yListMin = (int) Math.floor((Collections.min(yList1) - shorehouseRange.get(key).getMinOfyList()) / division);
                    //rollingData1和rollingData2构建的轨迹多边形的外接矩形四个角点坐标
                    List<Integer> index1 = new LinkedList<>();
                    index1.add((int) Math.ceil(xListMin / 100));
                    index1.add((int) Math.ceil(yListMin / 100));

                    List<Integer> index2 = new LinkedList<>();
                    index2.add((int) Math.ceil(xListMax / 100));
                    index2.add((int) Math.ceil(yListMin / 100));

                    List<Integer> index3 = new LinkedList<>();
                    index3.add((int) Math.ceil(xListMin / 100));
                    index3.add((int) Math.ceil(yListMax / 100));

                    List<Integer> index4 = new LinkedList<>();
                    index4.add((int) Math.ceil(xListMax / 100));
                    index4.add((int) Math.ceil(yListMax / 100));

                    Set<List<Integer>> index = new LinkedHashSet<>();
                    index.add(index1);
                    index.add(index2);
                    index.add(index3);
                    index.add(index4);
                    //通过轨迹多边形获得当前碾压区域的左上角点和右下角点
                    RollingDataRange rollingDataRange = new RollingDataRange();
                    rollingDataRange.setMaxCoordX(Collections.max(xList1));
                    rollingDataRange.setMinCoordX(Collections.min(xList1));
                    rollingDataRange.setMaxCoordY(Collections.max(yList1));
                    rollingDataRange.setMinCoordY(Collections.min(yList1));
                    //遍历轨迹多边形 判断碾压遍数和颜色
                    for (int m = xListMin; m < xListMax; m++) {
                        for (int n = yListMin; n < yListMax; n++) {
                            //生成m,n对应的点坐标
                            Double xTmp = shorehouseRange.get(key).getMinOfxList().doubleValue() + m * division;
                            Double yTmp = shorehouseRange.get(key).getMinOfyList().doubleValue() + n * division;
                            //判断点是否在多边形内
                            Coordinate point1 = new Coordinate(xTmp, yTmp);
                            PointLocator a1 = new PointLocator();
                            boolean p1 = a1.intersects(point1, bgAfter1);
                            if (p1) {
                                //只画在边界内的
                                //点在内，就把对应的网格遍数+1
                                if (m > 0 && n > 0 && m < storeHouseMaps2RollingData.get(storehouseId).length && n < storeHouseMaps2RollingData.get(storehouseId)[0].length) {
                                    isDraw = 1;
                                    Integer rollingTimes = storeHouseMaps2RollingData.get(storehouseId)[m][n].getRollingTimes();
                                    calculateRollingtimes(rollingTimes, rollingResult);
                                    storeHouseMaps2RollingData.get(storehouseId)[m][n].setRollingTimes(storeHouseMaps2RollingData.get(storehouseId)[m][n].getRollingTimes() + 1);
                                    storeHouseMaps2RollingData.get(storehouseId)[m][n].getElevationList().add(rollingData2.getElevation());
                                    storeHouseMaps2RollingData.get(storehouseId)[m][n].getAccelerationList().add(rollingData2.getAcceleration());
                                    storeHouseMaps2RollingData.get(storehouseId)[m][n].getFrequencyList().add(rollingData2.getFrequency());
                                    storeHouseMaps2RollingData.get(storehouseId)[m][n].getSpeedList().add(rollingData2.getSpeed());
                                    storeHouseMaps2RollingData.get(storehouseId)[m][n].getTimestampList().add(rollingData2.getTimestamp());
                                    storeHouseMaps2RollingData.get(storehouseId)[m][n].getVehicleIDList().add(rollingData2.getVehicleID());
                                    storeHouseMaps2RollingData.get(storehouseId)[m][n].getVibrateValueList().add(rollingData2.getVibrateValue());

                                    if (rollingData2.getIsVibrate() == 1) {
                                        //动碾遍数+1
                                        storeHouseMaps2RollingData.get(storehouseId)[m][n].setIsVibrate(storeHouseMaps2RollingData.get(storehouseId)[m][n].getIsVibrate() + 1);
                                    } else if (rollingData2.getIsVibrate() == 0) {
                                        //静碾遍数+1
                                        storeHouseMaps2RollingData.get(storehouseId)[m][n].setIsNotVibrate(storeHouseMaps2RollingData.get(storehouseId)[m][n].getIsNotVibrate() + 1);
                                    }
                                }
                            }
                        }
                    }
                    coordinate1Map.put(carId, pointList[0]);
                    coordinate2Map.put(carId, pointList[1]);
                    rollingData02Map.put(carId, rollingData2);
                    //根据index中的范围进行输出
                    for (List<Integer> item : index) {
                        //先把范围确定了
                        int xMin = item.get(0) * 100 - 5;
                        int xMax = (item.get(0) + 1) * 100 - 1 + 5;
                        int yMin = item.get(1) * 100 - 5;
                        int yMax = (item.get(1) + 1) * 100 - 1 + 5;

                        BufferedImage biLast = new BufferedImage(xMax - xMin + 1, yMax - yMin + 1, BufferedImage.TYPE_INT_ARGB);
                        Graphics2D g2Last = (Graphics2D) biLast.getGraphics();
                        //增添振动统计的图像
                        //BufferedImage biLastForVibrate = new BufferedImage(xMax - xMin + 1, yMax - yMin + 1, BufferedImage.TYPE_INT_ARGB);
                        //Graphics2D g2LastForVibrate = (Graphics2D) biLastForVibrate.getGraphics();
                        synchronized (storeHouseMaps2RollingData) {
                            int XMAX = storeHouseMaps2RollingData.get(storehouseId).length;
                            int YMAX = storeHouseMaps2RollingData.get(storehouseId)[0].length;
                            for (int i = xMin; i < xMax + 1; i++) {
                                for (int j = yMin; j < yMax + 1; j++) {
                                    if (i > 0 && j > 0 && i < XMAX && j < YMAX) {
                                        if (Integer.valueOf(storeHouseMaps2RollingData.get(storehouseId)[i][j].getRollingTimes()) > 0) {
                                            int mmm = 0;
                                            Integer times = storeHouseMaps2RollingData.get(storehouseId)[i][j].getRollingTimes();
                                            Integer vibrateTimes = storeHouseMaps2RollingData.get(storehouseId)[i][j].getIsVibrate();
                                            g2Last.setColor(getColorByCount2(times));
                                            g2Last.fillRect(i - xMin, j - yMin, 1, 1);
                                            //g2LastForVibrate.setColor(getColorByCount2(vibrateTimes));
                                            //g2LastForVibrate.fillRect(i - xMin, j - yMin, 1, 1);
                                        }
                                    }
                                }
                            }
                        }

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        //ByteArrayOutputStream baosForVibrate = new ByteArrayOutputStream();
                        try {
                            ImageIO.write(biLast, "PNG", baos);
                            //ImageIO.write(biLastForVibrate, "PNG", baosForVibrate);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        byte[] bytes = baos.toByteArray();//转换成字节
                        //byte[] bytesForVibrate = baosForVibrate.toByteArray();//转换成字节
                        String bsae64_string = "data:image/png;base64," + Base64.encodeBase64String(bytes);
                        //String bsae64_string_ForVibrate = "data:image/png;base64," + Base64.encodeBase64String(bytesForVibrate);
                        ProjCoordinate projCoordinate1 = new ProjCoordinate(shorehouseRange.get(key).getMinOfyList() + yMin * division, shorehouseRange.get(key).getMinOfxList() + xMin * division, 10);
                        ProjCoordinate projCoordinate2 = new ProjCoordinate(shorehouseRange.get(key).getMinOfyList() + yMax * division, shorehouseRange.get(key).getMinOfxList() + xMax * division, 10);
                        ProjCoordinate markPos = new ProjCoordinate(rollingData2.getZhuangY(), rollingData2.getZhuangX(), 10);
                        JSONObject result = new JSONObject();
                        Double rate = Double.valueOf(rollingResult.getTime8() + rollingResult.getTime9() + rollingResult.getTime10() + rollingResult.getTime11() + rollingResult.getTime11Up()) / Double.valueOf(rollingResult.getTime0() + rollingResult.getTime1() + rollingResult.getTime2() + rollingResult.getTime3() + rollingResult.getTime4() + rollingResult.getTime5() + rollingResult.getTime6() + rollingResult.getTime7() + rollingResult.getTime8() + rollingResult.getTime9() + rollingResult.getTime10() + rollingResult.getTime11() + rollingResult.getTime11Up());
                        Double coverRate = Double.valueOf(rollingResult.getTime1() + rollingResult.getTime2() + rollingResult.getTime3() + rollingResult.getTime4() + rollingResult.getTime5() + rollingResult.getTime6() + rollingResult.getTime7() + rollingResult.getTime8() + rollingResult.getTime9() + rollingResult.getTime10() + rollingResult.getTime11() + rollingResult.getTime11Up()) / Double.valueOf(rollingResult.getTime0() + rollingResult.getTime1() + rollingResult.getTime2() + rollingResult.getTime3() + rollingResult.getTime4() + rollingResult.getTime5() + rollingResult.getTime6() + rollingResult.getTime7() + rollingResult.getTime8() + rollingResult.getTime9() + rollingResult.getTime10() + rollingResult.getTime11() + rollingResult.getTime11Up());
                        Double VibrateValue = rollingData2.getVibrateValue();
                        int iszhen = 0;
                        if (VibrateValue > VibrateValue0) {
                            iszhen = 1;
                        }

                        result.put("tablename", storehouseId);//图片占的网格编号
                        result.put("index", item);//图片占的网格编号
                        result.put("iszhen", iszhen);
                        result.put("rate", rate);//合格率
                        result.put("speed", rollingData2.getSpeed());//车辆速度
                        result.put("elevation", rollingData2.getElevation());//车辆高程
                        result.put("timestamp", rollingData2.getTimestamp());
                        result.put("base64", bsae64_string);
                        //result.put("base64ForVibrate", bsae64_string_ForVibrate);
                        result.put("rollingDataRange", rollingDataRange);
                        result.put("pointLeftBottom", projCoordinate1);
                        result.put("pointRightTop", projCoordinate2);
                        result.put("height", this.height);
                        result.put("markPos", markPos);
                        result.put("carId", rollingData2.getVehicleID());
                        result.put("rollingResult", rollingResult);
                        result.put("coverRate", coverRate);
                        result.put("title", damsConstruction.getTitle());
                        result.put("angle", rollingData2.getAngle());
                        result.put("ishistory", rollingData2.getIshistory());
                        result.put("uuid", "唯一标识.....实时2....");

                        result.put("zhuanghao", rollingData2.getZhuanghao());
                        result.put("pianju", rollingData2.getPianju());

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date t = new Date();
                        t.setTime(rollingData2.getTimestamp());
                        result.put("timestamp", sdf.format(t));
                        result.put("amplitude", rollingData2.getVibrateValue());


                        if (!users.isEmpty() && isDraw == 1) {
                            //遍历websocket连接池
                            for (String keyOfMap : users.keySet()) {
                                //获得websocket的storehouseID
                                String storehouseNameOfWebsocket = keyOfMap.split(":")[0];
                                String carIdOfWebsocket = keyOfMap.split(":")[1];
                                //如果storehouseID一致
                                if (storehouseNameOfWebsocket.equalsIgnoreCase(key) && carIdOfWebsocket.equalsIgnoreCase(rollingData2.getVehicleID())) {
                                    //   log.info("==========================" + rollingData2.getVehicleID() + "车数据===========================================");
                                    //如果storehouseID一致
                                    Session session = users.get(keyOfMap);
                                    synchronized (session) {
                                        try {
                                            session.getBasicRemote().sendText(JSONObject.toJSONString(result));
                                        } catch (Exception e) {
                                            try {
                                                Thread.sleep(100);
                                                session.getBasicRemote().sendText(JSONObject.toJSONString(result));
                                            } catch (InterruptedException | IOException e1) {
                                                e1.printStackTrace();
                                            }
                                            e.printStackTrace();

                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }

        }
    }

    /**
     * 设置线程标识
     *
     * @param isStop true：停止 false：不停止
     */
    public void stopThread(boolean isStop) {
        this.isStop = isStop;
    }


    private void calculateRollingtimes(Integer rollingTimes, RollingResult rollingResult) {
        if (rollingTimes.equals(0)) {
            rollingResult.setTime0(rollingResult.getTime0() - 1);
            rollingResult.setTime1(rollingResult.getTime1() + 1);
        }
        if (rollingTimes.equals(1)) {
            rollingResult.setTime1(rollingResult.getTime1() - 1);
            rollingResult.setTime2(rollingResult.getTime2() + 1);
        }
        if (rollingTimes.equals(2)) {
            rollingResult.setTime2(rollingResult.getTime2() - 1);
            rollingResult.setTime3(rollingResult.getTime3() + 1);
        }
        if (rollingTimes.equals(3)) {
            rollingResult.setTime3(rollingResult.getTime3() - 1);
            rollingResult.setTime4(rollingResult.getTime4() + 1);
        }
        if (rollingTimes.equals(4)) {
            rollingResult.setTime4(rollingResult.getTime4() - 1);
            rollingResult.setTime5(rollingResult.getTime5() + 1);
        }
        if (rollingTimes.equals(5)) {
            rollingResult.setTime5(rollingResult.getTime5() - 1);
            rollingResult.setTime6(rollingResult.getTime6() + 1);
        }
        if (rollingTimes.equals(6)) {
            rollingResult.setTime6(rollingResult.getTime6() - 1);
            rollingResult.setTime7(rollingResult.getTime7() + 1);
        }
        if (rollingTimes.equals(7)) {
            rollingResult.setTime7(rollingResult.getTime7() - 1);
            rollingResult.setTime8(rollingResult.getTime8() + 1);
        }
        if (rollingTimes.equals(8)) {
            rollingResult.setTime8(rollingResult.getTime8() - 1);
            rollingResult.setTime9(rollingResult.getTime9() + 1);
        }
        if (rollingTimes.equals(9)) {
            rollingResult.setTime9(rollingResult.getTime9() - 1);
            rollingResult.setTime10(rollingResult.getTime10() + 1);
        }
        if (rollingTimes.equals(10)) {
            rollingResult.setTime10(rollingResult.getTime10() - 1);
            rollingResult.setTime11(rollingResult.getTime11() + 1);
        }
        if (rollingTimes.equals(11)) {
            rollingResult.setTime11(rollingResult.getTime11() - 1);
            rollingResult.setTime11Up(rollingResult.getTime11Up() + 1);
        }
        if (rollingTimes > 11) {
            rollingResult.setTime11Up(rollingResult.getTime11Up() + 0);
        }
    }
    /*public static Color getColorByCount2(Integer count) {
        if(count.equals(0)) {
            return new Color(255,255,255,0);
        }
        if(count.equals(1)) {
            return new Color(225,148, 207);
        }
        if(count.equals(2)) {
            return new Color(245,102, 102);
        }
        if(count.equals(3)) {
            return new Color(255,0,0);
        }
        if(count.equals(4)) {
            return new Color(185,40,71);
        }
        if(count.equals(5)) {
            return new Color(255,0,243);
        }
        if(count.equals(6)) {
            return new Color(72,238,217);
        }
        if(count.equals(7)) {
            return new Color(71,195,238);
        }
        if(count.equals(8)) {
            return new Color(133,244,133);
        }
        if(count.equals(9)) {
            return new Color(133,244,133);
        }
        if(count.equals(10)) {
            return new Color(133,244,133);
        }
        if(count.equals(11)) {
            return new Color(133,244,133);
        }
        if(count>11) {
            return new Color(133,244,133);
        }
        return null;
    }*/

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

    public void getColorMap(TColorConfigMapper colorConfigMapper) {
        TColorConfig vo = new TColorConfig();
        vo.setType(1L);//碾压遍次
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
