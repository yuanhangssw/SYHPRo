package com.tianji.dam.thread;

import com.alibaba.fastjson.JSONObject;
import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.bean.GlobCache;
import com.tianji.dam.domain.*;
import com.tianji.dam.mapper.RollingDataMapper;
import com.tianji.dam.mapper.TColorConfigMapper;
import com.tianji.dam.mapper.TDamsconstructionMapper;
import com.tianji.dam.utils.MapUtil;
import com.tianji.dam.utils.TrackConstant;
import com.tianji.dam.websocket.WebSocketServerForHistory;
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
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class HistoryDataTaskBlockingList0412 implements Runnable {
    private Double VibrateValue0 = 40d;

    @Autowired
    private WebSocketServerForHistory webSocketServerForHistory;
    private String key;



    private String storehouseId;
    private String carId;
    private int cartype;
    private String rollingResultKey;
    private Long beginTimestamp;
    private Long endTimestamp;
    private String websoketid;


    public Integer getCartype() {
        return cartype;
    }

    public void setCartype(Integer cartype) {
        this.cartype = cartype;
    }

    public String getWebsoketid() {
        return websoketid;
    }

    public void setWebsoketid(String websoketid) {
        this.websoketid = websoketid;
    }

    public Long getBeginTimestamp() {
        return beginTimestamp;
    }

    public Long getEndTimestamp() {
        return endTimestamp;
    }

    public void setBeginTimestamp(Long beginTimestamp) {
        this.beginTimestamp = beginTimestamp;
    }

    public void setEndTimestamp(Long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

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

    Map<Integer, Color> colorMap = new HashMap<>();

    private volatile boolean isStop;//使用volatile关键字修饰，可以在多线程之间共享，成员变量来控制线程的停止.

    /**
     * 设置线程标识
     *
     * @param isStop true：停止 false：不停止
     */
    public void stopThread(boolean isStop) {
        this.isStop = isStop;
    }

    @Autowired
    private static StoreHouseMap storeHouseMap;
    private Map<String, StorehouseRange> shorehouseRange = StoreHouseMap.getHistoryRange();

    private Map<String, RollingResult> rollingResultMap = StoreHouseMap.getHistoryResultMap();

    //历史播放速度
    private static Map<String, Long> historySpeed = StoreHouseMap.getHistoryDataSpeed();


    Double division = 1d;
    Double lengthOfVehicle = (TrackConstant.WHEEL_LEFT + TrackConstant.WHEEL_RIGHT);
    List<TColorConfig> colorConfig_evolution = new ArrayList<>();

    @Override
    public void run() {

        Map<String, Session> users = webSocketServerForHistory.getUsersMap();

        Map<String, MatrixItem[][]> storeHouseMaps2RollingData = StoreHouseMap.getHistory2RollingData();
        TColorConfigMapper colorConfigMapper = BeanContext.getApplicationContext().getBean(TColorConfigMapper.class);
        getColorMap(colorConfigMapper, cartype);
        TDamsconstructionMapper damsConstructionMapper = BeanContext.getApplicationContext().getBean(TDamsconstructionMapper.class);

        DamsConstruction damsConstruction = damsConstructionMapper.selectByPrimaryKey(Integer.valueOf(key));

        String id = String.valueOf(damsConstruction.getTablename());
        RollingResult rollingResult = rollingResultMap.get(rollingResultKey);
        log.info("HistoryDataTaskBlocking" + Thread.currentThread().getName() + "执行");

        RollingDataMapper rollingDataMapper = BeanContext.getApplicationContext().getBean(RollingDataMapper.class);
        TColorConfig vo = new TColorConfig();
        vo.setType(44l);//摊铺平整度颜色
        colorConfig_evolution = colorConfigMapper.select(vo);
        List<RollingData> rollingDatas = new LinkedList<>();
        if (!isStop) {
            String tableprefix = GlobCache.cartableprfix[cartype] + "_";
            rollingDatas.addAll(rollingDataMapper.getAllRollingDataByVehicleIDAndDateRange(tableprefix + damsConstruction.getTablename(), String.valueOf(carId), beginTimestamp, endTimestamp));
            // 总数据条数
            int dataSize = rollingDatas.size();
            if (dataSize >= 2) {
                //按时间排序(Long类型)
                List<RollingData> rollingDataList = rollingDatas.stream().sorted(Comparator.comparing(RollingData::getTimestamp)).collect(Collectors.toList());


                HashMap<String, RollingData> rollingData02Map = new HashMap();
                HashMap<String, Coordinate> coordinate1Map = new HashMap();
                HashMap<String, Coordinate> coordinate2Map = new HashMap();

                RollingData rollingData01 = rollingDataList.get(0);
                rollingDataList.remove(0);
                RollingData rollingData02 = rollingDataList.get(0);
                carId =rollingData02.getVehicleID();
                if(!rollingData02Map.containsKey(carId)){
                    rollingData02Map.put(carId, new RollingData());
                }

                if(!coordinate1Map.containsKey(carId)){
                    coordinate1Map.put(carId, new Coordinate());
                }
                if(!coordinate2Map.containsKey(carId)){
                    coordinate2Map.put(carId, new Coordinate());
                }

                    rollingDataList.remove(0);
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
                        coordinate1Map.put(carId, pointList0[0]);
                        coordinate2Map.put(carId, pointList0[1]);
                    }


                 Iterator<RollingData> it = rollingDataList.iterator();

                //处理剩余数据
                    while (it.hasNext()) {
                        if (isStop) {
                            log.info("仓位回访.......线程关闭《《《《《《《..........");
                            break;
                        }else{
                          //  log.info("单元工程历史回放中..........");
                        }
                        RollingData rollingData2 = it.next();
                            it.remove();
                            carId = rollingData2.getVehicleID();

                        if (Thread.currentThread().isInterrupted()) {
                            log.info("仓位回访.......处理中断逻辑《《《《《《《..........");
                            break;
                        }
                        int isDraw = 0;
                        RollingData rollingData1 = rollingData02Map.get(carId);
                                if(null==rollingData1){
                                    rollingData02Map.put(carId,rollingData2);
                                    continue;
                                }
                        //                log.info("从右侧弹出");
                        //                log.info("数据为：" + rollingData2.toString());
                        double distance = Math.sqrt(Math.pow(rollingData1.getZhuangX() - rollingData2.getZhuangX(), 2) + Math.pow(rollingData1.getZhuangY() - rollingData2.getZhuangY(), 2));


                        //前后大于5个像素再绘制，像素太小绘图没有意义，图上完全看不到，增加消耗
                        if (distance <= TrackConstant.MIN_DIS) {
                            // log.error("历时通道内-==================前后距离过小:" + distance);
                            rollingData02Map.put(carId, rollingData2);
                        } else if (distance > TrackConstant.MAX_DIS) {
                            //log.error("历时通道内-==================前后距离过大:" + distance + " " + rollingData1.getOrderNum() + "-" + rollingData2.getOrderNum());
                            rollingData01 = rollingData02;
                            rollingData02Map.put(carId, rollingData2);
                        } else {
                            storeHouseMaps2RollingData = StoreHouseMap.getHistory2RollingData();
//                    if(distance > 5){
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

                            if(null==coordinate2Map.get(carId)&&null==coordinate1Map.get(carId)){
                                coordinate1Map.put(carId, pointList[0]);
                                coordinate2Map.put(carId, pointList[1]);
                                rollingData02Map.put(carId, rollingData2);
                                continue;
                            }

                            try {
                                pointList[2] = coordinate2Map.get(carId);
                                pointList[3] = coordinate1Map.get(carId);
                            } catch (Exception e) {
                                System.out.println(coordinates4[0].toString());
                                System.out.println(coordinates4[1].toString());
                                System.out.println(lengthOfVehicle + "<>" + pointList.length);
                                e.printStackTrace();

                                continue;
                            }


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
                            int rollingtimes = 0;
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
                                        MatrixItem[][] items = storeHouseMaps2RollingData.get(storehouseId);
                                        if (m > 0 && n > 0 && m < items.length && n < items[0].length) {
                                            isDraw = 1;
                                            MatrixItem item = items[m][n];
                                            if (null==item) {
                                                item =new MatrixItem();
                                                item.setRollingTimes(0);
                                                storeHouseMaps2RollingData.get(storehouseId)[m][n]=item;
                                            }
                                            if (cartype == 2) {
                                                item.getCurrentEvolution().add(rollingData02.getCurrentEvolution());
                                            } else {
                                                item.setRollingTimes(item.getRollingTimes() + 1);
                                            }
                                            storeHouseMaps2RollingData.put(storehouseId, items);
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

                                synchronized (storeHouseMaps2RollingData) {
                                    int XMAX = storeHouseMaps2RollingData.get(storehouseId).length;
                                    int YMAX = storeHouseMaps2RollingData.get(storehouseId)[0].length;
                                    for (int i = xMin; i < xMax + 1; i++) {
                                        for (int j = yMin; j < yMax + 1; j++) {
                                            if (i > 0 && j > 0 && i < XMAX && j < YMAX) {
                                                MatrixItem itemt = storeHouseMaps2RollingData.get(storehouseId)[i][j];

                                                if (null==itemt) {
                                                    itemt =new MatrixItem();
                                                    itemt.setRollingTimes(0);
                                                    storeHouseMaps2RollingData.get(storehouseId)[i][j]=itemt;
                                                }

                                                if (cartype == 2) {
                                                    if (itemt.getCurrentEvolution().size() > 0) {

                                                        float lastevolution = 0;
                                                        try {
                                                            lastevolution = itemt.getCurrentEvolution().getLast();
                                                        } catch (Exception e) {
                                                         e.printStackTrace();
                                                        }
                                                        // System.out.println("当前位置高程：" + lastevolution);
                                                        float currenthoudu = (lastevolution - damsConstruction.getGaocheng().floatValue()) * 100.0f;
                                                        float laststatus = (float) (currenthoudu - damsConstruction.getHoudu());
                                                        g2Last.setColor(getColorByCountEvolution(laststatus, colorConfig_evolution));
                                                        g2Last.fillRect(i - xMin, j - yMin, 1, 1);
                                                    }

                                                } else {
                                                    if (Integer.valueOf(itemt.getRollingTimes()) > 0) {
                                                        int mmm = 0;
                                                        Integer times = itemt.getRollingTimes();

                                                        g2Last.setColor(getColorByCount2(times));
                                                        g2Last.fillRect(i - xMin, j - yMin, 1, 1);

                                                    }
                                                }

                                            }
                                        }
                                    }
                                }

                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                //ByteArrayOutputStream baosForVibrate = new ByteArrayOutputStream();
                                try {
                                    ImageIO.write(biLast, "PNG", baos);
                                    //ImageIO.write(biLastForVibrate,"PNG",baosForVibrate);
                                } catch (IOException e) {

                                    e.printStackTrace();
                                }
                                byte[] bytes = baos.toByteArray();//转换成字节
                                //byte[] bytesForVibrate = baosForVibrate.toByteArray();//转换成字节
                                String bsae64_string = "data:image/png;base64," + Base64.encodeBase64String(bytes);
                                //String bsae64_string_ForVibrate="data:image/png;base64,"+ Base64.encodeBase64String(bytesForVibrate);
                                ProjCoordinate projCoordinate1 = new ProjCoordinate(shorehouseRange.get(key).getMinOfyList() + yMin * division, shorehouseRange.get(key).getMinOfxList() + xMin * division, 10);
                                ProjCoordinate projCoordinate2 = new ProjCoordinate(shorehouseRange.get(key).getMinOfyList() + yMax * division, shorehouseRange.get(key).getMinOfxList() + xMax * division, 10);
                                ProjCoordinate markPos = new ProjCoordinate(rollingData2.getZhuangY(), rollingData2.getZhuangX(), 10);
                                JSONObject result = new JSONObject();
                                  Double VibrateValue = rollingData2.getVibrateValue();
                                int iszhen = 0;
                                if (VibrateValue > VibrateValue0) {
                                    iszhen = 1;
                                }

                                result.put("lastrollingtimes", rollingtimes);

                                result.put("tablename", storehouseId);//图片占的网格编号
                                result.put("index", item);//图片占的网格编号
                                result.put("iszhen", iszhen);

                                result.put("speed", rollingData2.getSpeed());//车辆速度
                                result.put("elevation", rollingData2.getElevation());//车辆高程
                                result.put("currentelevation", rollingData2.getCurrentEvolution());//车辆高程-新版
                                result.put("base64", bsae64_string);
                                // result.put("base64ForVibrate",bsae64_string_ForVibrate);
                                result.put("rollingDataRange", rollingDataRange);
                                result.put("pointLeftBottom", projCoordinate1);
                                result.put("pointRightTop", projCoordinate2);
                                result.put("height", this.height);
                                result.put("markPos", markPos);
                                result.put("carId", rollingData2.getVehicleID());//carId
                                result.put("title", damsConstruction.getTitle());
                                result.put("angle", rollingData2.getAngle());
                                result.put("zhuanghao", rollingData2.getZhuanghao());
                                result.put("Latitude", rollingData02.getLatitude());
                                result.put("Longitude", rollingData02.getLongitude());
                                result.put("pianju", rollingData2.getPianju());

                                result.put("CoordRX",rollingData1.getCoordRX());
                                result.put("CoordRY",rollingData1.getCoordRY());
                                result.put("CoordLX",rollingData1.getCoordLX());
                                result.put("CoordLY",rollingData1.getCoordLX());

                                result.put("BeforecoordRX",rollingData1.getBeforeCoordRX());
                                result.put("BeforecoordRY",rollingData1.getBeforeCoordRY());
                                result.put("BeforecoordLX",rollingData1.getBeforeCoordLX());
                                result.put("BeforecoordLY",rollingData1.getBeforeCoordLY());


                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date t = new Date();
                                t.setTime(rollingData2.getTimestamp());
                                result.put("timestamp", sdf.format(t));
                                result.put("timestamp2", rollingData2.getTimestamp());
                                result.put("vibrateValue", rollingData2.getVibrateValue());

                                if (cartype == 2) {
                                    result.put("tphoudu", (rollingData2.getCurrentEvolution() - damsConstruction.getGaocheng()) * 100.0);
                                }


                                if (!users.isEmpty() && isDraw == 1) {

                                            //   System.out.println(carId+"车的线程推送中。。。");
                                            for (String keyOfMap : users.keySet()) {
                                                //获得websocket的storehouseID
                                                String storehouseNameOfWebsocket = keyOfMap.split(":")[0];

                                                String websocketid = keyOfMap.split(":")[1];

                                                //如果storehouseID一致
                                                if (storehouseNameOfWebsocket.equalsIgnoreCase(key) && websoketid.equals(websocketid)) {
                                                    //如果storehouseID一致
                                                    Session session = users.get(keyOfMap);
                                                    synchronized (session) {
                                                        try {
                                                            session.getBasicRemote().sendText(JSONObject.toJSONString(result));
                                                            //历史播放速度
                                                            historySpeed = StoreHouseMap.getHistoryDataSpeed();
                                                            /*控制推送速度*/
                                                            Long millisecond = historySpeed.get(keyOfMap);
                                                            if (null != millisecond && millisecond.longValue() != 0) {
                                                                Thread.currentThread().sleep(millisecond);
                                                            } else {
                                                                Thread.sleep(TrackConstant.INTERVAL);
                                                            }
                                                        } catch (Exception e) {
                                                            try {
                                                                Thread.sleep(1000);
                                                                if (session.isOpen()) {
                                                                    session.getBasicRemote().sendText(JSONObject.toJSONString(result));
                                                                } else {
                                                                    clear();
                                                                }
                                                            } catch (InterruptedException | IOException e1) {
                                                                e1.printStackTrace();
                                                            }
                                                        }
                                                    }
                                                }
                                            }




                                }else{
                                    Thread.currentThread().interrupt();
                                }
                            }
                        }
                    }



            }
        } else {
            log.info("仓位回访.......线程关闭《《《《《《《..........");
        }
        //结束清除
        clear();
        rollingDatas.clear();
        rollingDatas = null;

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

    private void clear() {
        Map<String, Session> users = webSocketServerForHistory.getUsersMap();
        Iterator<Map.Entry<String, Session>> entries = users.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, Session> entry = entries.next();
            //获得websocket的storehouseID
            String storehouseNameOfWebsocket = entry.getKey().split(":")[0];
            String carIdOfWebsocket = entry.getKey().split(":")[1];
            //如果storehouseID一致
            if (storehouseNameOfWebsocket.equalsIgnoreCase(key) && carIdOfWebsocket.equalsIgnoreCase(websoketid)) {
                Session session = users.get(entry.getKey());
                synchronized (session) {
                    try {
                        session.getBasicRemote().sendText("close");
                    } catch (Exception e) {
                        try {
                            Thread.sleep(100);
                            session.getBasicRemote().sendText("close");
                        } catch (InterruptedException | IOException e1) {
                            e1.printStackTrace();
                        }
                        e.printStackTrace();

                    }
                }
            }
        }


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

    public void getColorMap(TColorConfigMapper colorConfigMapper, int cartype) {
        TColorConfig vo = new TColorConfig();
        vo.setType(GlobCache.carcoloconfigtype[cartype].longValue());//碾压遍次
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
