package com.tianji.dam.thread;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.websocket.Session;

import org.apache.tomcat.util.codec.binary.Base64;
import org.osgeo.proj4j.ProjCoordinate;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.domain.MatrixItem;
import com.tianji.dam.domain.RollingData;
import com.tianji.dam.domain.RollingDataRange;
import com.tianji.dam.domain.RollingResult;
import com.tianji.dam.domain.StoreHouseMap;
import com.tianji.dam.domain.StorehouseRange;
import com.tianji.dam.utils.RedisUtil;
import com.tianji.dam.utils.TrackConstant;
import com.tianji.dam.websocket.WebSocketServer;
import com.vividsolutions.jts.algorithm.PointLocator;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.operation.buffer.BufferOp;
import com.vividsolutions.jts.triangulate.ConformingDelaunayTriangulationBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RealTimeRedisRightPopTaskBlockingZhuangForHand implements Runnable{
    private Double VibrateValue0=40d;

    @Autowired
    private WebSocketServer webSocketServer;
    @Autowired
    private BeanContext  beancontext;
    private String key;
    Map<String, Session> users =webSocketServer.getUsersMap();
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

    @Autowired
    private static StoreHouseMap storeHouseMap;
    private  Map<String, StorehouseRange> shorehouseRange=storeHouseMap.getShorehouseRange();
    private  Map<String, MatrixItem[][]> storeHouseMaps2RollingData = storeHouseMap.getStoreHouses2RollingData();
    private Map<String, RollingResult> rollingResultMap = storeHouseMap.getRollingResultMap();
    Double division = 0.1;
    Double lengthOfVehicle = TrackConstant.WHEEL_LEFT+TrackConstant.WHEEL_RIGHT;
    @Override
    public void run() {
        String id =key;
        RollingResult rollingResult = rollingResultMap.get(rollingResultKey);
        log.info("RealTimeRedisRightPopTaskZhuangForHand"+Thread.currentThread().getName()+"执行");
        RedisUtil redisUtil = beancontext.getApplicationContext().getBean(RedisUtil.class);
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

        String redisKey=key+"-"+carId;
        boolean bl=false;
        try{
            bl =redisUtil.hasKey(redisKey);
        }catch(Exception ex){
            System.out.print("捕捉到了");
            Iterator<Map.Entry<String, Session>> entries = users.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, Session> entry = entries.next();
                String tablename= entry.getKey().split(":")[1];
                if(tablename.equalsIgnoreCase(key)){
                    users.remove(entry.getKey());
                    if(storeHouseMaps2RollingData.containsKey(id)){
                        storeHouseMaps2RollingData.remove(id);
                        for(int j=1;j<4;j++){
                            String rediskey =id+"-"+j;
                            try {
                                if(redisUtil.hasKey(rediskey)){
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


        if(bl) {
            RollingData rollingData01 = (RollingData) redisUtil.rightPop(redisKey);
            RollingData rollingData02 = (RollingData) redisUtil.rightPop(redisKey);
            rollingData02Map.put(carId,rollingData02);
            double dis = Math.sqrt(Math.pow(rollingData01.getZhuangX() - rollingData02.getZhuangX(), 2) + Math.pow(rollingData01.getZhuangY() - rollingData02.getZhuangY(), 2));
            if(dis>0d){
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
                                        calculateRollingtimes(rollingTimes,rollingResult);
                                        storeHouseMaps2RollingData.get(storehouseId)[m][n].setRollingTimes(storeHouseMaps2RollingData.get(storehouseId)[m][n].getRollingTimes() + 1);
                                        storeHouseMaps2RollingData.get(storehouseId)[m][n].getElevationList().add(rollingData02.getElevation());
                                        storeHouseMaps2RollingData.get(storehouseId)[m][n].getAccelerationList().add(rollingData02.getAcceleration());
                                        storeHouseMaps2RollingData.get(storehouseId)[m][n].getFrequencyList().add(rollingData02.getFrequency());
                                        storeHouseMaps2RollingData.get(storehouseId)[m][n].getSpeedList().add(rollingData02.getSpeed());
                                        storeHouseMaps2RollingData.get(storehouseId)[m][n].getTimestampList().add(rollingData02.getTimestamp());
                                        storeHouseMaps2RollingData.get(storehouseId)[m][n].getVehicleIDList().add(rollingData02.getVehicleID());
                                        storeHouseMaps2RollingData.get(storehouseId)[m][n].getVibrateValueList().add(rollingData02.getVibrateValue());

                                        if(rollingData02.getIsVibrate()==1){
                                            //动碾遍数+1
                                            storeHouseMaps2RollingData.get(storehouseId)[m][n].setIsVibrate(storeHouseMaps2RollingData.get(storehouseId)[m][n].getIsVibrate() + 1);
                                        }else if(rollingData02.getIsVibrate()==0){
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
//                bi = RotateImage.Rotate(bi, 270);
                /*klst翻转*/
//                bi = (BufferedImage) ImgRotate.imageMisro(bi,0);
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
                    ProjCoordinate projCoordinate10 = new ProjCoordinate(rollingDataRange0.getMinCoordY() , rollingDataRange0.getMinCoordX(), 10);
                    ProjCoordinate projCoordinate20 = new ProjCoordinate(rollingDataRange0.getMaxCoordY() , rollingDataRange0.getMaxCoordX(), 10);
                    ProjCoordinate markPos0 = new ProjCoordinate(rollingData02.getZhuangY() , rollingData02.getZhuangX(), 10);
                    JSONObject result0 = new JSONObject();
                    //计算实时合格率
                    Double rate = Double.valueOf(rollingResult.getTime8()+rollingResult.getTime9()+rollingResult.getTime10()+rollingResult.getTime11()+rollingResult.getTime11Up())/ Double.valueOf(rollingResult.getTime0()+rollingResult.getTime1()+rollingResult.getTime2()+rollingResult.getTime3()+rollingResult.getTime4()+rollingResult.getTime5()+rollingResult.getTime6()+rollingResult.getTime7()+rollingResult.getTime8()+rollingResult.getTime9()+rollingResult.getTime10()+rollingResult.getTime11()+rollingResult.getTime11Up());
                    Double coverRate = Double.valueOf(rollingResult.getTime1()+rollingResult.getTime2()+rollingResult.getTime3()+rollingResult.getTime4()+rollingResult.getTime5()+rollingResult.getTime6()+rollingResult.getTime7()+rollingResult.getTime8()+rollingResult.getTime9()+rollingResult.getTime10()+rollingResult.getTime11()+rollingResult.getTime11Up())/ Double.valueOf(rollingResult.getTime0()+rollingResult.getTime1()+rollingResult.getTime2()+rollingResult.getTime3()+rollingResult.getTime4()+rollingResult.getTime5()+rollingResult.getTime6()+rollingResult.getTime7()+rollingResult.getTime8()+rollingResult.getTime9()+rollingResult.getTime10()+rollingResult.getTime11()+rollingResult.getTime11Up());
                    result0.put("tablename",storehouseId);//图片占的网格编号
                    result0.put("speed",rollingData02.getSpeed());//车辆速度
                    result0.put("elevation",rollingData02.getElevation());//车辆高程
                    result0.put("base64", bsae64_string);
                    result0.put("base64ForVibrate", bsae64_string_ForVibrate);
                    result0.put("rollingDataRange", rollingDataRange0);
                    result0.put("pointLeftBottom", projCoordinate10);
                    result0.put("pointRightTop", projCoordinate20);
                    result0.put("height", this.height);
                    result0.put("markPos", markPos0);
                    result0.put("carId", carId);
                    result0.put("rollingResult",rollingResult);
                    result0.put("rate",rate);//合格率
                    result0.put("coverRate",coverRate);//碾压占比率
                    result0.put("vibrateValue",rollingData01.getVibrateValue());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date t = new Date();
                    t.setTime(rollingData01.getTimestamp());
                    result0.put("timestamp",sdf.format(t));
                    result0.put("amplitude",rollingData01.getVibrateValue());
	                    
                    
                    if (!users.isEmpty()) {
                        //遍历websocket连接池
                        for (String keyOfMap : users.keySet()) {
                            //获得websocket的storehouseID
                            String storehouseNameOfWebsocket = keyOfMap.split(":")[1];
                            String carIdOfWebsocket = keyOfMap.split(":")[2];
                            //如果storehouseID一致
                            if (storehouseNameOfWebsocket.equalsIgnoreCase(key)||carIdOfWebsocket.equalsIgnoreCase(carId)) {
                                log.info(storehouseNameOfWebsocket);
                                Session session = users.get(keyOfMap);
                                synchronized(session){
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
        while (true) {
            if(Thread.currentThread().isInterrupted()){
                //处理中断逻辑
                break;
            }

                redisKey=key+"-"+carId;
                boolean bl2=false;
                try{
                    bl2 =redisUtil.hasKey(redisKey);
                }catch(Exception ex){
                    System.out.print("捕捉到了");
                    Iterator<Map.Entry<String, Session>> entries = users.entrySet().iterator();
                    while (entries.hasNext()) {
                        Map.Entry<String, Session> entry = entries.next();
                        String tablename= entry.getKey().split(":")[1];
                        if(tablename.equalsIgnoreCase(key)){
                            users.remove(entry.getKey());
                            if(storeHouseMaps2RollingData.containsKey(id)){
                                storeHouseMaps2RollingData.remove(id);
                                for(int j=1;j<4;j++){
                                    String rediskey =id+"-"+j;
                                    try {
                                        if(redisUtil.hasKey(rediskey)){
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

                if(bl2) {
                    int isDraw = 0;
                    RollingData rollingData1 = rollingData02Map.get(carId);
                   // log.info("从右侧弹出");
                    RollingData rollingData2 = (RollingData) redisUtil.rightPop(redisKey);
                   // log.info("数据为：" + rollingData2.toString());
                    double distance = Math.sqrt(Math.pow(rollingData1.getZhuangX() - rollingData2.getZhuangX(), 2) + Math.pow(rollingData1.getZhuangY() - rollingData2.getZhuangY(), 2));
                    if(distance>0.5d){
                            Coordinate[] coordinates4 = new Coordinate[]{
                                    new Coordinate(rollingData1.getZhuangX(), rollingData1.getZhuangY()),
                                    new Coordinate(rollingData2.getZhuangX(), rollingData2.getZhuangY()),
                            };
                            GeometryFactory gf = new GeometryFactory();
                            Geometry gfLineString = gf.createLineString(coordinates4);
                            //double degree = 10 / (2*Math.PI*6371004)*360;
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
                            //Geometry bgAfter = gf.createPolygon(pointList);
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
                            //导出图片的索引 index
                            List<Integer> index1= new LinkedList<>();
                            index1.add((int)Math.ceil(xListMin/100));
                            index1.add((int)Math.ceil(yListMin/100));

                            List<Integer> index2= new LinkedList<>();
                            index2.add((int)Math.ceil(xListMax/100));
                            index2.add((int)Math.ceil(yListMin/100));

                            List<Integer> index3= new LinkedList<>();
                            index3.add((int)Math.ceil(xListMin/100));
                            index3.add((int)Math.ceil(yListMax/100));

                            List<Integer> index4= new LinkedList<>();
                            index4.add((int)Math.ceil(xListMax/100));
                            index4.add((int)Math.ceil(yListMax/100));

                            Set<List<Integer>> index = new LinkedHashSet<>();
                            index.add(index1);
                            index.add(index2);
                            index.add(index3);
                            index.add(index4);

                            RollingDataRange rollingDataRange = new RollingDataRange();
                            rollingDataRange.setMaxCoordX(Collections.max(xList1));
                            rollingDataRange.setMinCoordX(Collections.min(xList1));
                            rollingDataRange.setMaxCoordY(Collections.max(yList1));
                            rollingDataRange.setMinCoordY(Collections.min(yList1));
                            if (distance < 5d) {
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
                                            if(m>0&&n>0&&m<storeHouseMaps2RollingData.get(storehouseId).length&&n<storeHouseMaps2RollingData.get(storehouseId)[0].length) {
                                                isDraw=1;
                                                Integer rollingTimes = storeHouseMaps2RollingData.get(storehouseId)[m][n].getRollingTimes();
                                                calculateRollingtimes(rollingTimes,rollingResult);
                                                storeHouseMaps2RollingData.get(storehouseId)[m][n].setRollingTimes(storeHouseMaps2RollingData.get(storehouseId)[m][n].getRollingTimes() + 1);
                                                storeHouseMaps2RollingData.get(storehouseId)[m][n].getElevationList().add(rollingData2.getElevation());
                                                storeHouseMaps2RollingData.get(storehouseId)[m][n].getAccelerationList().add(rollingData2.getAcceleration());
                                                storeHouseMaps2RollingData.get(storehouseId)[m][n].getFrequencyList().add(rollingData2.getFrequency());
                                                storeHouseMaps2RollingData.get(storehouseId)[m][n].getSpeedList().add(rollingData2.getSpeed());
                                                storeHouseMaps2RollingData.get(storehouseId)[m][n].getTimestampList().add(rollingData2.getTimestamp());
                                                storeHouseMaps2RollingData.get(storehouseId)[m][n].getVehicleIDList().add(rollingData2.getVehicleID());
                                                storeHouseMaps2RollingData.get(storehouseId)[m][n].getVibrateValueList().add(rollingData2.getVibrateValue());

                                                if(rollingData2.getIsVibrate()==1){
                                                    //动碾遍数+1
                                                    storeHouseMaps2RollingData.get(storehouseId)[m][n].setIsVibrate(storeHouseMaps2RollingData.get(storehouseId)[m][n].getIsVibrate() + 1);
                                                }else if(rollingData2.getIsVibrate()==0){
                                                    //静碾遍数+1
                                                    storeHouseMaps2RollingData.get(storehouseId)[m][n].setIsNotVibrate(storeHouseMaps2RollingData.get(storehouseId)[m][n].getIsNotVibrate() + 1);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            coordinate1Map.put(carId, pointList[0]);
                            coordinate2Map.put(carId, pointList[1]);
                            rollingData02Map.put(carId, rollingData2) ;
                            //根据index中的范围进行输出
                            for(List<Integer> item:index){
                                //先把范围确定了
                                int xMin = item.get(0)*100-5;
                                int xMax = (item.get(0)+1)*100-1+5;
                                int yMin = item.get(1)*100-5;
                                int yMax = (item.get(1)+1)*100-1+5;

                                BufferedImage biLast = new BufferedImage(xMax-xMin+1,yMax-yMin+1,BufferedImage.TYPE_INT_ARGB);
                                Graphics2D g2Last = (Graphics2D) biLast.getGraphics();
                                /*增添振动统计的图像*/
                                BufferedImage biLastForVibrate = new BufferedImage(xMax-xMin+1,yMax-yMin+1,BufferedImage.TYPE_INT_ARGB);
                                Graphics2D g2LastForVibrate = (Graphics2D) biLastForVibrate.getGraphics();
                                synchronized (storeHouseMaps2RollingData){
                                    int XMAX =storeHouseMaps2RollingData.get(storehouseId).length ;
                                    int YMAX =storeHouseMaps2RollingData.get(storehouseId)[0].length;
                                    for(int i=xMin;i<xMax+1;i++) {
                                        for(int j=yMin;j<yMax+1;j++) {
                                            if(i>0&&j>0&&i<XMAX&&j<YMAX) {
                                                if(Integer.valueOf(storeHouseMaps2RollingData.get(storehouseId)[i][j].getRollingTimes())>0){
                                                    int mmm=0;
                                                    Integer times = storeHouseMaps2RollingData.get(storehouseId)[i][j].getRollingTimes();
                                                    Integer vibrateTimes = storeHouseMaps2RollingData.get(storehouseId)[i][j].getIsVibrate();
                                                    g2Last.setColor(getColorByCount2(times));
                                                    //g2.setColor(new Color(225,148, 207));
                                                    g2Last.fillRect(i-xMin,j-yMin,1,1);
                                                    g2LastForVibrate.setColor(getColorByCount2(vibrateTimes));
                                                    //g2.setColor(new Color(225,148, 207));
                                                    g2LastForVibrate.fillRect(i-xMin,j-yMin,1,1);
                                                }
                                            }
                                        }
                                    }
                                }

    //                    biLast = RotateImage.Rotate(biLast, 270);
                                /*翻转*/
//                                biLast = (BufferedImage) ImgRotate.imageMisro(biLast,0);
                               /* biLast = (BufferedImage) ImgRotate.imageMisro(biLast,0);
                                biLastForVibrate = (BufferedImage) ImgRotate.imageMisro(biLastForVibrate,0);*/
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                ByteArrayOutputStream baosForVibrate = new ByteArrayOutputStream();
                                try {
                                    ImageIO.write(biLast,"PNG",baos);
                                    ImageIO.write(biLastForVibrate,"PNG",baosForVibrate);
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                byte[] bytes = baos.toByteArray();//转换成字节
                                byte[] bytesForVibrate = baosForVibrate.toByteArray();//转换成字节
                                String bsae64_string="data:image/png;base64,"+ Base64.encodeBase64String(bytes);
                                String bsae64_string_ForVibrate="data:image/png;base64,"+ Base64.encodeBase64String(bytesForVibrate);
                                ProjCoordinate projCoordinate1 = new ProjCoordinate(shorehouseRange.get(key).getMinOfyList()+yMin*division,shorehouseRange.get(key).getMinOfxList()+xMin*division,10);
                                ProjCoordinate projCoordinate2 = new ProjCoordinate(shorehouseRange.get(key).getMinOfyList()+yMax*division,shorehouseRange.get(key).getMinOfxList()+xMax*division,10);
                                ProjCoordinate markPos = new ProjCoordinate(rollingData2.getZhuangY(),rollingData2.getZhuangX(),10);
                                JSONObject result = new JSONObject();
                                Double rate = Double.valueOf(rollingResult.getTime8()+rollingResult.getTime9()+rollingResult.getTime10()+rollingResult.getTime11()+rollingResult.getTime11Up())/ Double.valueOf(rollingResult.getTime0()+rollingResult.getTime1()+rollingResult.getTime2()+rollingResult.getTime3()+rollingResult.getTime4()+rollingResult.getTime5()+rollingResult.getTime6()+rollingResult.getTime7()+rollingResult.getTime8()+rollingResult.getTime9()+rollingResult.getTime10()+rollingResult.getTime11()+rollingResult.getTime11Up());
                                Double coverRate = Double.valueOf(rollingResult.getTime1()+rollingResult.getTime2()+rollingResult.getTime3()+rollingResult.getTime4()+rollingResult.getTime5()+rollingResult.getTime6()+rollingResult.getTime7()+rollingResult.getTime8()+rollingResult.getTime9()+rollingResult.getTime10()+rollingResult.getTime11()+rollingResult.getTime11Up())/ Double.valueOf(rollingResult.getTime0()+rollingResult.getTime1()+rollingResult.getTime2()+rollingResult.getTime3()+rollingResult.getTime4()+rollingResult.getTime5()+rollingResult.getTime6()+rollingResult.getTime7()+rollingResult.getTime8()+rollingResult.getTime9()+rollingResult.getTime10()+rollingResult.getTime11()+rollingResult.getTime11Up());
                                Double VibrateValue = rollingData2.getVibrateValue();
                                int iszhen=0;
                                if(VibrateValue>VibrateValue0){
                                    iszhen=1;
                                }

                                result.put("tablename",storehouseId);//图片占的网格编号
                                result.put("index",item);//图片占的网格编号
                                result.put("iszhen",iszhen);
                                result.put("rate",rate);//合格率
                                result.put("speed",rollingData2.getSpeed());//车辆速度
                                result.put("elevation",rollingData2.getElevation());//车辆高程
                                result.put("base64",bsae64_string);
                                result.put("base64ForVibrate",bsae64_string_ForVibrate);
                                result.put("rollingDataRange",rollingDataRange);
                                result.put("pointLeftBottom",projCoordinate1);
                                result.put("pointRightTop",projCoordinate2);
                                result.put("height",this.height);
                                result.put("markPos",markPos);
                                result.put("carId",carId);
                                result.put("rollingResult",rollingResult);
                                result.put("coverRate",coverRate);
                                result.put("vibrateValue",rollingData2.getVibrateValue());
                                
                                result.put("zhuanghao", rollingData2.getZhuanghao());
                                result.put("pianju", rollingData2.getPianju());
                                
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date t = new Date();
                                t.setTime(rollingData2.getTimestamp());
                                result.put("timestamp",sdf.format(t));
                                result.put("amplitude",rollingData2.getVibrateValue());
                                
                                if(!users.isEmpty()&&isDraw==1) {
                                    //遍历websocket连接池
                                    for(String keyOfMap:users.keySet()) {
                                        //获得websocket的storehouseID
                                        String storehouseNameOfWebsocket = keyOfMap.split(":")[1];
                                        String carIdOfWebsocket = keyOfMap.split(":")[2];
                                        //如果storehouseID一致
                                        if (storehouseNameOfWebsocket.equalsIgnoreCase(key)||carIdOfWebsocket.equalsIgnoreCase(carId)) {
                                        //如果storehouseID一致
                                            Session session = users.get(keyOfMap);
                                            synchronized(session){
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
                try {
					Thread.sleep(20);
				} catch (Exception e) {
					e.printStackTrace();
				}
        }
    }


    private void calculateRollingtimes(Integer rollingTimes, RollingResult rollingResult) {
        if(rollingTimes.equals(0)){
            rollingResult.setTime0(rollingResult.getTime0()-1);
            rollingResult.setTime1(rollingResult.getTime1()+1);
        }
        if(rollingTimes.equals(1)){
            rollingResult.setTime1(rollingResult.getTime1()-1);
            rollingResult.setTime2(rollingResult.getTime2()+1);
        }
        if(rollingTimes.equals(2)){
            rollingResult.setTime2(rollingResult.getTime2()-1);
            rollingResult.setTime3(rollingResult.getTime3()+1);
        }
        if(rollingTimes.equals(3)){
            rollingResult.setTime3(rollingResult.getTime3()-1);
            rollingResult.setTime4(rollingResult.getTime4()+1);
        }
        if(rollingTimes.equals(4)){
            rollingResult.setTime4(rollingResult.getTime4()-1);
            rollingResult.setTime5(rollingResult.getTime5()+1);
        }
        if(rollingTimes.equals(5)){
            rollingResult.setTime5(rollingResult.getTime5()-1);
            rollingResult.setTime6(rollingResult.getTime6()+1);
        }
        if(rollingTimes.equals(6)){
            rollingResult.setTime6(rollingResult.getTime6()-1);
            rollingResult.setTime7(rollingResult.getTime7()+1);
        }
        if(rollingTimes.equals(7)){
            rollingResult.setTime7(rollingResult.getTime7()-1);
            rollingResult.setTime8(rollingResult.getTime8()+1);
        }
        if(rollingTimes.equals(8)){
            rollingResult.setTime8(rollingResult.getTime8()-1);
            rollingResult.setTime9(rollingResult.getTime9()+1);
        }
        if(rollingTimes.equals(9)){
            rollingResult.setTime9(rollingResult.getTime9()-1);
            rollingResult.setTime10(rollingResult.getTime10()+1);
        }
        if(rollingTimes.equals(10)){
            rollingResult.setTime10(rollingResult.getTime10()-1);
            rollingResult.setTime11(rollingResult.getTime11()+1);
        }
        if(rollingTimes.equals(11)){
            rollingResult.setTime11(rollingResult.getTime11()-1);
            rollingResult.setTime11Up(rollingResult.getTime11Up()+1);
        }
        if(rollingTimes>11){
            rollingResult.setTime11Up(rollingResult.getTime11Up()+0);
        }
    }
    public static Color getColorByCount2(Integer count) {
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

    }
}
