package com.tianji.dam.thread;

import com.alibaba.fastjson.JSONObject;
import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.bean.GlobCache;
import com.tianji.dam.domain.*;
import com.tianji.dam.mapper.RollingDataMapper;
import com.tianji.dam.mapper.TColorConfigMapper;
import com.tianji.dam.mapper.TDamsconstructionMapper;
import com.tianji.dam.scan.*;
import com.tianji.dam.utils.*;
import com.tianji.dam.websocket.WebSocketServerForHistory;
import com.tj.common.utils.RGBHexUtil;
import com.tj.common.utils.StringUtils;
import com.vividsolutions.jts.geom.Coordinate;
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
import java.util.List;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
public class HistoryDataTaskBlockingListBack implements Runnable {
    private Double VibrateValue0 = 40d;

    @Autowired
    private WebSocketServerForHistory webSocketServerForHistory;
    private String key;

    Map<String, Session> users = webSocketServerForHistory.getUsersMap();

    private String storehouseId;
    private String carId;
    private int cartype;
    private String rollingResultKey;
    private Long beginTimestamp;
    private Long endTimestamp;
    private String websoketid;

    Double baseevolution = 0.0d;
    Double basehoudu = 0.0d;

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
    private Map<String, MatrixItem[][]> storeHouseMaps2RollingData = StoreHouseMap.getHistory2RollingData();
    private Map<String, RollingResult> rollingResultMap = StoreHouseMap.getHistoryResultMap();

    //历史播放速度
    private static Map<String, Long> historySpeed = StoreHouseMap.getHistoryDataSpeed();


    Double division = 1d;
    Double lengthOfVehicle = (TrackConstant.WHEEL_LEFT + TrackConstant.WHEEL_RIGHT);
    String keytype = "";
    List<TColorConfig> colorConfig_evolution = new ArrayList<>();

    @Override
    public void run() {
        keytype = GlobCache.cartableprfix[cartype];
        TColorConfigMapper colorConfigMapper = BeanContext.getApplicationContext().getBean(TColorConfigMapper.class);

        TDamsconstructionMapper damsConstructionMapper = BeanContext.getApplicationContext().getBean(TDamsconstructionMapper.class);

        DamsConstruction damsConstruction = damsConstructionMapper.selectByPrimaryKey(Integer.valueOf(key));

        String id = String.valueOf(damsConstruction.getTablename());
        baseevolution = damsConstruction.getGaocheng();
        basehoudu = damsConstruction.getHoudu();
        RollingResult rollingResult = rollingResultMap.get(rollingResultKey);
        log.info("HistoryDataTaskBlocking" + Thread.currentThread().getName() + "执行");

        RollingDataMapper rollingDataMapper = BeanContext.getApplicationContext().getBean(RollingDataMapper.class);

        List<RollingData> rollingDatas = new LinkedList<>();

        TColorConfig vo = new TColorConfig();
        vo.setType(44l);//摊铺平整度颜色
        colorConfig_evolution = colorConfigMapper.select(vo);
        getColorMap(colorConfigMapper, cartype);

        if (!isStop) {
            String tableprefix = GlobCache.cartableprfix[cartype] + "_";
            rollingDatas.addAll(rollingDataMapper.getAllRollingDataByVehicleIDAndDateRange(tableprefix + damsConstruction.getTablename(), String.valueOf(carId), beginTimestamp, endTimestamp));
            // 总数据条数
            int dataSize = rollingDatas.size();
            if (dataSize >= 2) {
                //按时间排序(Long类型)
                List<RollingData> rollingDataList = rollingDatas.stream().sorted(Comparator.comparing(RollingData::getTimestamp)).collect(Collectors.toList());

                ///////////////////////////////////
                //        获取仓位的历史数据        //
                ///////////////////////////////////

                //处理i=0的情况
                HashMap<String, RollingData> rollingData02Map = new HashMap();
                HashMap<String, Coordinate> coordinate1Map = new HashMap();
                HashMap<String, Coordinate> coordinate2Map = new HashMap();
            /*HashMap<String,Integer> xSizeMap = new HashMap();
            HashMap<String,Integer> ySizeMap = new HashMap();*/
                rollingData02Map.put(carId, new RollingData());
                coordinate1Map.put(carId, new Coordinate());
                coordinate2Map.put(carId, new Coordinate());
            /*xSizeMap.put(carId,new Integer(0));
            ySizeMap.put(carId,new Integer(0));*/
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


                Iterator<RollingData> it = rollingDataList.iterator();

                while (it.hasNext()) {
                    if (isStop) {
                        log.info("历史数据通道.......线程关闭《《《《《《《..........");
                        break;
                    }
                    RollingData rollingData = it.next();

                    it.remove();


                    if (Thread.currentThread().isInterrupted()) {
                        log.info("历史数据通道.......处理中断逻辑《《《《《《《..........");
                        break;
                    }
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
                                            ConcurrentHashMap<Long, MatrixItem[][]> cacheData_today = new ConcurrentHashMap<>();

                                            List<Integer> cols = new LinkedList<>();//列
                                            List<Integer> rows = new LinkedList<>();//行
                                            int allPassCount = 0;
                                            int allPassCount_cang = 0;
                                            //rasters 数据像素
                                            for (Pixel pixel : rasters) {
                                                try {
                                                    //todo:此处的Rid是通过平面坐标进行计算得到的
                                                    long rid = RidUtil.double2Long(pixel.getX(), pixel.getY());

                                                    //从当天Rid缓存中获取当该Rid的二维数组
                                                    //当天数据
                                                    MatrixItem[][] ridMatirxItems = cacheData_today.get(rid);
                                                    if (ridMatirxItems == null) {
                                                        String key = GenerateRedisKey.realTimeRidKey(rid, keytype);
                                                        ridMatirxItems = (MatrixItem[][]) storeHouseMaps2RollingData.get(storehouseId);
                                                        if (ridMatirxItems == null) {
                                                            ridMatirxItems = new MatrixItem[RidUtil.R_LEN][RidUtil.R_LEN];
                                                        }
                                                        cacheData_today.put(rid, ridMatirxItems);
                                                    }
                                                    cols.add(RidUtil.long2Col(rid));
                                                    rows.add(RidUtil.long2Row(rid));
                                                    int bottomRid = RidUtil.long2Bottom(rid);
                                                    int leftRid = RidUtil.long2Left(rid);
                                                    int nRid = (pixel.getY() - bottomRid);
                                                    int mRid = (pixel.getX() - leftRid);
                                                    if (nRid >= 0 && mRid >= 0 && nRid < RidUtil.R_LEN && mRid < RidUtil.R_LEN) {
                                                        MatrixItem realMatrixItem = ridMatirxItems[mRid][nRid];
                                                        if (realMatrixItem == null) {
                                                            realMatrixItem = new MatrixItem();
                                                        }
                                                        //  System.out.println("遍数。" + realMatrixItem.getRollingTimes());
                                                        realMatrixItem.setRollingTimes(realMatrixItem.getRollingTimes() + 1);
                                                        realMatrixItem.getElevationList().add(rollingData02.getElevation().floatValue());
                                                        realMatrixItem.getAccelerationList().add(rollingData02.getAcceleration());
                                                        realMatrixItem.getFrequencyList().add(rollingData02.getFrequency());
                                                        realMatrixItem.getSpeedList().add(rollingData02.getSpeed().floatValue());
                                                        realMatrixItem.getTimestampList().add(rollingData02.getTimestamp());
                                                        realMatrixItem.getVehicleIDList().add(rollingData02.getVehicleID());

                                                        realMatrixItem.getCurrentEvolution().add(rollingData02.getCurrentEvolution());
                                                        realMatrixItem.getBeforeElevation().add(rollingData02.getBeforeElevation());
                                                        realMatrixItem.getZyangle().add(rollingData02.getZyangle());
                                                        realMatrixItem.getQhangle().add(rollingData02.getQhangle());

                                                        if (rollingData02.getIsVibrate() == 1) {
                                                            realMatrixItem.setIsVibrate(realMatrixItem.getIsVibrate() + 1);
                                                        } else {
                                                            realMatrixItem.setIsNotVibrate(realMatrixItem.getIsNotVibrate() + 1);
                                                        }
                                                        ridMatirxItems[mRid][nRid] = realMatrixItem;
                                                        allPassCount += realMatrixItem.getRollingTimes();
                                                        cacheData_today.put(rid, ridMatirxItems);
                                                        storeHouseMaps2RollingData.get(storehouseId)[mRid][nRid] = realMatrixItem;
                                                        realMatrixItem = null;
                                                        //操作仓位数据

                                                        //  System.out.println("当前遍数"+realMatrixItem.getRollingTimes()+"》动碾"+realMatrixItem.getIsVibrate()+"》静碾"+realMatrixItem.getIsNotVibrate());
                                                    }
                                                } catch (Exception ex) {
                                                    ex.printStackTrace();
                                                }
                                            }

                                            int currentPassCount = allPassCount / rasters.size();
                                            int currentPassCount_cang = allPassCount_cang / rasters.size();
                                            rasters.clear();
                                            rollingData01 = rollingData02;
                                            mLastPt = data;
                                            Integer showtype = 1;
                                            JSONObject result0 = new JSONObject();

                                            for (String keyOfMap : users.keySet()) {
                                                //获得websocket的storehouseID
                                                String storehouseNameOfWebsocket = keyOfMap.split(":")[0];
                                                String carIdOfWebsocket = keyOfMap.split(":")[1];
                                                String websocketid = keyOfMap.split(":")[2];

                                                //如果storehouseID一致
                                                if (storehouseNameOfWebsocket.equalsIgnoreCase(key) && carIdOfWebsocket.equalsIgnoreCase(carId) && websoketid.equals(websocketid)) {

                                                    result0 = draw(cacheData_today, cols, rows, rollingData02);
                                                    result0.put("lastrollingtime", currentPassCount);

                                                    try {
                                                        result0.put("Latitude", rollingData.getLatitude());
                                                        result0.put("Longitude", rollingData.getLongitude());
                                                        result0.put("qhangle", rollingData02.getQhangle());
                                                        result0.put("zyangle", rollingData02.getZyangle());
                                                        result0.put("currentevolution", rollingData02.getCurrentEvolution());
                                                        if (baseevolution != 0 && null != rollingData02.getCurrentEvolution()) {
                                                            System.out.println("数据高程===数据高程：" + rollingData02.getCurrentEvolution() + "====" + baseevolution);

                                                            result0.put("tphoudu", (rollingData02.getCurrentEvolution() - baseevolution.floatValue()));
                                                        } else {
                                                            result0.put("tphoudu", "");
                                                        }

                                                        result0.put("isLoad", 0);//仓未加载过
                                                        //  Thread.sleep(10);
                                                        //  System.out.println("数据处理完成。。");

                                                        Session session = users.get(keyOfMap);
                                                        synchronized (session) {
                                                            try {
                                                                session.getBasicRemote().sendText(JSONObject.toJSONString(result0));
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
                                                                    Thread.sleep(100);
                                                                    if (session.isOpen()) {

                                                                        session.getBasicRemote().sendText(JSONObject.toJSONString(result0));
                                                                    } else {
                                                                        clear();
                                                                    }
                                                                } catch (InterruptedException | IOException e1) {
                                                                    e1.printStackTrace();
                                                                }
                                                            }
                                                        }
                                                        result0.clear();
                                                        result0 = null;
                                                    } catch (Exception e) {
                                                     e.printStackTrace();
                                                    }


                                                }
                                            }
                                            cacheData_today.clear();

                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }


                }


            }
        } else {
            log.info("历史数据通道.......线程关闭《《《《《《《..........");
        }
        //结束清除
        clear();
        rollingDatas.clear();
        rollingDatas = null;

    }

    public JSONObject draw(ConcurrentHashMap<Long, MatrixItem[][]> cache, List<Integer> long2Cols,
                           List<Integer> long2Rows, RollingData rollingData02) {
        MatrixItem[][] matrixItems = null;
        MatrixItem item = null;

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


        // for(Map.Entry<Long, MatrixItem[][]> entry : cache.entrySet()){
        for (Object key : cache.keySet()) {
            // 10*10 小方格
            long rid = Long.valueOf(key.toString());
            matrixItems = cache.get(rid);
            int dltaX = RidUtil.long2Col(rid) * RidUtil.R_LEN - baseX;
            int dltaY = RidUtil.long2Row(rid) * RidUtil.R_LEN - baseY;
            for (int i = 0; i < RidUtil.R_LEN - 1; i++) {
                for (int j = 0; j < RidUtil.R_LEN - 1; j++) {
                    item = matrixItems[i][j];
                    if (item != null) {
                        if (keytype.equals("tpj")) {

                            if (item.getCurrentEvolution().size() > 0) {
                                float lastevolution = item.getCurrentEvolution() == null ? 0.0f : item.getCurrentEvolution().getLast();
                                // System.out.println("当前位置高程：" + lastevolution);
                                float currenthoudu = (lastevolution - baseevolution.floatValue()) * 100.0f;
                                float laststatus = currenthoudu - basehoudu.floatValue();
                                g2.setColor(getColorByCountEvolution(laststatus, colorConfig_evolution));
                                g2.fillRect(i + dltaX, j + dltaY, 2, 2);

                            }

                        } else if (keytype.equals("ylj")) {
                            int rollingTimes = item.getRollingTimes();
                            if (rollingTimes > 0) {

                                g2.setColor(getColorByCount2(rollingTimes));
                                g2.fillRect(i + dltaX, j + dltaY, 2, 2);
                            }
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
            log.error(e.getMessage());
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
        result.put("zhuanghao", rollingData02.getZhuanghao());
        result.put("pianju", rollingData02.getPianju());
        result.put("speed", rollingData02.getSpeed());//车辆速度
        result.put("elevation", rollingData02.getElevation());//车辆高程
        result.put("base64", bsae64_string);

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

        result.put("angle", rollingData02.getAngle());
        double vcv = rollingData02.getVibrateValue();
//        if(vcv != 0){
//            vcv = 370 + 10 * Math.random();
//        }
        result.put("amplitude", vcv);

        matrixItems = null;
        return result;
    }


    private void clear() {
        Iterator<Map.Entry<String, Session>> entries = users.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, Session> entry = entries.next();
            //获得websocket的storehouseID
            String storehouseNameOfWebsocket = entry.getKey().split(":")[0];
            String carIdOfWebsocket = entry.getKey().split(":")[1];
            //如果storehouseID一致
            if (storehouseNameOfWebsocket.equalsIgnoreCase(key) && carIdOfWebsocket.equalsIgnoreCase(carId)) {
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
