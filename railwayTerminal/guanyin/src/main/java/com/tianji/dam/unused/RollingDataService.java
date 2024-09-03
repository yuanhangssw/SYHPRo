package com.tianji.dam.unused;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tianji.dam.bean.GlobCache;
import com.tianji.dam.domain.*;
import com.tianji.dam.mapper.*;
import com.tianji.dam.mileageutil.Mileage;
import com.tianji.dam.scan.Pixel;
import com.tianji.dam.scan.PointCpb;
import com.tianji.dam.scan.Scan;
import com.tianji.dam.service.ITDamsconstructionReportService;
import com.tianji.dam.thread.*;
import com.tianji.dam.utils.*;
import com.tianji.dam.utils.productareapoints.scan.PointStep;
import com.tianji.dam.utils.productareapoints.scan.TransUtils;
import com.tianji.dam.websocket.WebSocketServer;
import com.tj.common.annotation.DataSource;
import com.tj.common.enums.DataSourceType;
import com.tj.common.utils.DateUtils;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.websocket.Session;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

import static java.util.Arrays.sort;

@Service
@Slf4j
@DataSource(value = DataSourceType.SLAVE)
public class RollingDataService {
    public static final int CORECOUNT = 10;
    @Value("${imgPath}")
    private String imgPath;
    private static final Object obj1 = new Object();//锁
    private Map<String, Set<String>> globalThreadMap = new HashMap<>();
    private Map<String, Set<String>> storehouseThreadList = new HashMap<>();
    private Map<String, Set<String>> threadTimestamp = new HashMap<>();
    private Map<String, String> threadStorehouse = new HashMap<>();
    Double division = 1d;
    @Autowired
    private CarMapper carMapper;
    @Autowired
    private TableMapper tableMapper;
    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private WebSocketServer webSocketServer;
    Map<String, Session> users = webSocketServer.getUsersMap();
    @Autowired
    private ITDamsconstructionReportService tDamsconstructionReportService;

    @Autowired
    private static StoreHouseMap storeHouseMap;
    private static Map<String, List<RealTimeRedisRightPopTaskBlockingZhuang>> threadMap = storeHouseMap.getRealTimeRedisRightPopTaskBlockingZhuangList();
    private static Map<String, StorehouseRange> shorehouseRange = storeHouseMap.getShorehouseRange();
    private static Map<String, RollingResult> rollingResultMap = storeHouseMap.getRollingResultMap();
    @Autowired
    private RollingDataMapper rollingDataMapper;
    @Autowired
    private TDamsconstructionMapper damsConstructionMapper;

    public List<RollingData> getAllRollingData(String tableName) {
        return rollingDataMapper.getAllRollingData(tableName);
    }

    /*计算高程*/
    public JSONObject calElevation(String userName, String tableName) throws InterruptedException, ExecutionException, IOException {

        JSONObject result = new JSONObject();
        return result;
    }

    /*多线程绘制历史数据
     * 将图片按照桩号放入无经纬度概念的map中*/
    public JSONObject getHistoryPicMultiThreadingByZhuang(String userName, String tableName) throws InterruptedException, ExecutionException, IOException {
        String id = tableName;
        DamsConstruction damsConstruction = damsConstructionMapper.selectByPrimaryKey(Integer.valueOf(id));
        tableName = damsConstruction.getTablename();
        String mat = damsConstruction.getMaterialname();
        /*String range = tableName.split("_")[0]+"_"+tableName.split("_")[1];*/
        RollingDataRange rollingDataRange = new RollingDataRange();
        //从数据库中获得范围
        rollingDataRange.setMaxCoordX(damsConstruction.getXend());
        rollingDataRange.setMinCoordX(damsConstruction.getXbegin());
        rollingDataRange.setMaxCoordY(damsConstruction.getYend());
        rollingDataRange.setMinCoordY(damsConstruction.getYbegin());
        //记录外接矩形的范围
        StorehouseRange storehouseRange = new StorehouseRange();
        storehouseRange.setMinOfxList(rollingDataRange.getMinCoordY());
        storehouseRange.setMaxOfxList(rollingDataRange.getMaxCoordY());
        storehouseRange.setMinOfyList(rollingDataRange.getMinCoordX());
        storehouseRange.setMaxOfyList(rollingDataRange.getMaxCoordX());
        Integer yNum = (int) Math.ceil((rollingDataRange.getMaxCoordY() - rollingDataRange.getMinCoordY()) / division);//行数
        Integer xNum = (int) Math.ceil((rollingDataRange.getMaxCoordX() - rollingDataRange.getMinCoordX()) / division);//列数
        MatrixItem[][] matrix = new MatrixItem[xNum][yNum];
        for (int x = 0; x < xNum; x++) {
            for (int y = 0; y < yNum; y++) {
                matrix[x][y] = new MatrixItem();
                matrix[x][y].setRollingTimes(0);
            }
        }
        /*key增加用户username 便于后边清除内存*/
        String key = userName + "-" + tableName;
        shorehouseRange.put(key, storehouseRange);
        StoreHouseMap.getStoreHouses2RollingData().put(key, matrix);
        List<Car> carList = carMapper.findCar();
        long start = System.currentTimeMillis();
        //固定线程的数量
        for (Car car : carList) {
            tableName = "t_" + car.getCarID();
            /* List<RollingData> rollingDataList = rollingDataMapper.getAllRollingDataByVehicleID(tableName,car.getCarID().toString());*/
            List<RollingData> rollingDataList = rollingDataMapper.getAllRollingDataByCube(tableName, car.getCarID().toString(), damsConstruction.getXbegin(), damsConstruction.getXend(), damsConstruction.getYbegin(), damsConstruction.getYend(), damsConstruction.getGaocheng(), damsConstruction.getGaocheng() + damsConstruction.getCenggao() * 0.01, mat);
            // 总数据条数
            int dataSize = rollingDataList.size();
            if (dataSize < 10) {
                continue;
            }
            if (dataSize == 0) {
                continue;
            }
            // 线程数 2个核心
            int threadNum = CORECOUNT;
            // 余数
            int special = dataSize % threadNum;
            //每个线程处理的数据量
            int dataSizeEveryThread = dataSize / threadNum;
            // 创建一个线程池
            ExecutorService exec = Executors.newFixedThreadPool(threadNum);
            // 定义一个任务集合
            List<Callable<Integer>> tasks = new LinkedList<Callable<Integer>>();
            Callable<Integer> task = null;
            List<RollingData> cutList = null;
            // 确定每条线程的数据
            for (int i = 0; i < threadNum; i++) {
                if (i == threadNum - 1) {
                    int index = dataSizeEveryThread * i - 1 < 0 ? 0 : dataSizeEveryThread * i - 1;
                    cutList = rollingDataList.subList(index, dataSize);
                } else {
                    if (i == 0) {
                        cutList = rollingDataList.subList(dataSizeEveryThread * i, dataSizeEveryThread * (i + 1));
                    } else {
                        cutList = rollingDataList.subList(dataSizeEveryThread * i - 1, dataSizeEveryThread * (i + 1));
                    }

                }
                final List<RollingData> listRollingData = cutList;
                task = new CalculateGridForZhuang();
                ((CalculateGridForZhuang) task).setRollingDataRange(rollingDataRange);
                ((CalculateGridForZhuang) task).setRollingDataList(listRollingData);
                ((CalculateGridForZhuang) task).setTableName(key);
                ((CalculateGridForZhuang) task).setxNum(xNum);
                ((CalculateGridForZhuang) task).setyNum(yNum);
                // 这里提交的任务容器列表和返回的Future列表存在顺序对应的关系
                tasks.add(task);
            }
            List<Future<Integer>> results = exec.invokeAll(tasks);
            for (Future<Integer> future : results) {
                log.info(future.get().toString());
            }
            // 关闭线程池
            exec.shutdown();
            log.info("线程任务执行结束");
            rollingDataList.clear();
            rollingDataList = null;
        }
        log.error("计算碾压遍数执行任务消耗了 ：" + (System.currentTimeMillis() - start) + "毫秒");

        //绘制图片
        //得到图片缓冲区
        /*碾压轨迹遍数的绘制*/
        BufferedImage bi = new BufferedImage(xNum, yNum, BufferedImage.TYPE_INT_ARGB);//TYPE_INT_ARGB 为透明的 INT精确度达到一定,RGB三原色，高度70,宽度150
        //得到它的绘制环境(这张图片的笔)
        Graphics2D g2 = (Graphics2D) bi.getGraphics();
        RollingResult rollingResult = new RollingResult();
        List<Coordinate> list = JSONArray.parseArray(damsConstruction.getRanges(), Coordinate.class);
        Coordinate[] pointList = new Coordinate[list.size() + 1];  //最重要，不能遗漏，预先申请好数组空间
        list.toArray(pointList);
        pointList[list.size()] = pointList[0];
        GeometryFactory gf = new GeometryFactory();
        Geometry edgePoly = gf.createPolygon(pointList);
        long drawstart = System.currentTimeMillis();
        int count0 = 0;
        for (int i = 0; i < xNum - 1; i++) {
            for (int j = 0; j < yNum - 1; j++) {
                //计算边界的网格数量
                Double xTmp = rollingDataRange.getMinCoordX() + i * division;
                Double yTmp = rollingDataRange.getMinCoordY() + j * division;
                //判断点是否在多边形内
                Coordinate point = new Coordinate(xTmp, yTmp);
                PointLocator a = new PointLocator();
                boolean p1 = a.intersects(point, edgePoly);
                if (p1) {
                    count0++;
                    Integer rollingTimes = StoreHouseMap.getStoreHouses2RollingData().get(key)[i][j].getRollingTimes();
                    calculateRollingtimes(rollingTimes, rollingResult);
                    g2.setColor(getColorByCount2(rollingTimes));
                    g2.fillRect(i, j, 1, 1);
                }
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bi, "PNG", baos);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] bytes = baos.toByteArray();//转换成字节
        String bsae64_string = "data:image/png;base64," + Base64.encodeBase64String(bytes);
        log.error("绘图执行任务消耗了 ：" + (System.currentTimeMillis() - drawstart) + "毫秒");

        if (damsConstruction.getGaocheng() == null) {
            damsConstruction.setGaocheng(0d);
        }
        //无经纬度概念 无需坐标转换
        ProjCoordinate projCoordinate1 = new ProjCoordinate(rollingDataRange.getMinCoordY(), rollingDataRange.getMinCoordX(), damsConstruction.getGaocheng());
        ProjCoordinate projCoordinate2 = new ProjCoordinate(rollingDataRange.getMaxCoordY(), rollingDataRange.getMaxCoordX(), damsConstruction.getGaocheng());
        JSONObject result = new JSONObject();
        rollingResult.setTime0(count0 - rollingResult.getTime1() - rollingResult.getTime2() - rollingResult.getTime3() - rollingResult.getTime4() - rollingResult.getTime5() - rollingResult.getTime6() - rollingResult.getTime7() - rollingResult.getTime8()
                - rollingResult.getTime9() - rollingResult.getTime10() - rollingResult.getTime11() - rollingResult.getTime11Up());
        result.put("rollingResult", rollingResult);
        result.put("base64", bsae64_string);
        result.put("rollingDataRange", rollingDataRange);
        result.put("pointLeftBottom", projCoordinate1);
        result.put("pointRightTop", projCoordinate2);
        result.put("height", damsConstruction.getGaocheng());
        result.put("cenggao", damsConstruction.getCenggao());
        /*  result.put("range",range);*/
        return result;
    }

    public JSONObject getHistoryPicMultiThreadingByZhuangByTodforReal(String userName, String tableName) throws InterruptedException, ExecutionException, IOException {
        String id = tableName;
        DamsConstruction damsConstruction = damsConstructionMapper.selectByPrimaryKey(Integer.valueOf(id));
        int dmstatus = damsConstruction.getStatus();
        //工作仓 数据
        tableName = damsConstruction.getTablename();
        String range = tableName.split("_")[0] + "_" + tableName.split("_")[1];
        //1.根据工作仓初设化网格
        //直接根据桩号获得范围
        BigDecimal minOfxList = BigDecimal.valueOf(damsConstruction.getXbegin());
        BigDecimal maxOfxList = BigDecimal.valueOf(damsConstruction.getXend());
        BigDecimal minOfyList = BigDecimal.valueOf(damsConstruction.getYbegin());
        BigDecimal maxOfyList = BigDecimal.valueOf(damsConstruction.getYend());
        StorehouseRange storehouseRange = new StorehouseRange();
        storehouseRange.setMinOfxList(minOfxList.doubleValue());
        storehouseRange.setMaxOfxList(maxOfxList.doubleValue());
        storehouseRange.setMinOfyList(minOfyList.doubleValue());
        storehouseRange.setMaxOfyList(maxOfyList.doubleValue());
        RollingDataRange rollingDataRange = new RollingDataRange();
        rollingDataRange.setMaxCoordY(maxOfyList.doubleValue());
        rollingDataRange.setMinCoordY(minOfyList.doubleValue());
        rollingDataRange.setMaxCoordX(maxOfxList.doubleValue());
        rollingDataRange.setMinCoordX(minOfxList.doubleValue());
        int xSize = (int) Math.abs(damsConstruction.getXend() - damsConstruction.getXbegin());
        int ySize = (int) Math.abs(damsConstruction.getYend() - damsConstruction.getYbegin());
        //所有车
        List<Car> carList = carMapper.findCar();
        //判断工作仓数据是否存在
        boolean isHave = StoreHouseMap.getStoreHouses2RollingData().containsKey(tableName);
        long start = System.currentTimeMillis();
        if (true) {//不存在则进行
            MatrixItem[][] matrix = new MatrixItem[xSize][ySize];
            for (int i = 0; i < xSize; i++) {
                for (int j = 0; j < ySize; j++) {
                    matrix[i][j] = new MatrixItem();
                    matrix[i][j].setRollingTimes(0);
                }
            }

            // if(2 == dmstatus) {
            shorehouseRange.put(id, storehouseRange);
            StoreHouseMap.getStoreHouses2RollingData().put(tableName, matrix);
            // }
            /**
             * 查询出当前仓位的补录区域
             */
            TRepairData record = new TRepairData();
            record.setDamsid(damsConstruction.getId());
            List<TRepairData> repairDataList = repairDataMapper.selectTRepairDatas(record);

            //2 获得数据库中的数据
            for (Car car : carList) {
                List<RollingData> rollingDataList = rollingDataMapper.getAllRollingDataByVehicleID(tableName, car.getCarID().toString());
                // 总数据条数
                int dataSize = rollingDataList.size();
                if (dataSize < 10) {
                    continue;
                }
                if (dataSize == 0) {
                    continue;
                }
                // 线程数 四个核心
                int threadNum = CORECOUNT;
                // 余数
                int special = dataSize % threadNum;
                //每个线程处理的数据量
                int dataSizeEveryThread = dataSize / threadNum;
                // 创建一个线程池
                ExecutorService exec = Executors.newFixedThreadPool(threadNum);
                // 定义一个任务集合
                List<Callable<Integer>> tasks = new LinkedList<Callable<Integer>>();
                Callable<Integer> task = null;
                List<RollingData> cutList = null;

                // 确定每条线程的数据
                for (int i = 0; i < threadNum; i++) {
                    if (i == threadNum - 1) {
                        int index = dataSizeEveryThread * i - 1 < 0 ? 0 : dataSizeEveryThread * i - 1;
                        cutList = rollingDataList.subList(index, dataSize);
                    } else {
                        if (i == 0) {
                            cutList = rollingDataList.subList(dataSizeEveryThread * i, dataSizeEveryThread * (i + 1));
                        } else {
                            cutList = rollingDataList.subList(dataSizeEveryThread * i - 1, dataSizeEveryThread * (i + 1));
                        }
                    }
                    final List<RollingData> listRollingData = cutList;

                    task = new CalculateGridForZhuangHistoryforRealTime();
                    ((CalculateGridForZhuangHistoryforRealTime) task).setRollingDataRange(rollingDataRange);
                    ((CalculateGridForZhuangHistoryforRealTime) task).setRollingDataList(listRollingData);
                    ((CalculateGridForZhuangHistoryforRealTime) task).setTableName(tableName);
                    ((CalculateGridForZhuangHistoryforRealTime) task).setxNum(xSize);
                    ((CalculateGridForZhuangHistoryforRealTime) task).setyNum(ySize);
                    ((CalculateGridForZhuangHistoryforRealTime) task).setCangname(damsConstruction.getTitle());
                    // 这里提交的任务容器列表和返回的Future列表存在顺序对应的关系
                    tasks.add(task);
                }
                //执行加载之前先清空掉旧的缓存避免数据叠加
                Map<Long, MatrixItem[][]> realTime2RollingData = storeHouseMap.getRealTimeStorehouseDataItem(damsConstruction.getTitle());
                for (Long aLong : realTime2RollingData.keySet()) {

                    MatrixItem[][] m = realTime2RollingData.get(aLong);
                    for (MatrixItem[] matrixItems : m) {

                        for (MatrixItem matrixItem : matrixItems) {

                            matrixItem = null;

                        }

                    }

                }


                List<Future<Integer>> results = exec.invokeAll(tasks);
                for (Future<Integer> future : results) {
                    log.info(future.get().toString());
                }
                // 关闭线程池
                exec.shutdown();
                log.info("线程任务执行结束");
                rollingDataList.clear();
                rollingDataList = null;
            }

            //根据补录内容 将仓对应区域的碾压遍数、速度、压实度更新
//            if(repairDataList != null) {
//                Map<String, MatrixItem[][]> storeHouses2RollingData = storeHouseMap.getStoreHouses2RollingData();
//                MatrixItem[][] matrixItems = storeHouses2RollingData.get(tableName);
//                for (TRepairData repairData : repairDataList) {
//                    String ranges = repairData.getRanges();
//                    int passCount = repairData.getColorId();
//                    float speed = repairData.getSpeed().floatValue();
//                    double vibration = repairData.getVibration();
//
//                    List<Coordinate> repairRange = JSONArray.parseArray(ranges, Coordinate.class);
//                    Pixel[] repairPolygon = new Pixel[repairRange.size()];
//                    for(int i = 0;i < repairRange.size();i++) {
//                        Coordinate coordinate = repairRange.get(i);
//                        Pixel pixel = new Pixel();
//                        pixel.setX((int)coordinate.x);
//                        pixel.setY((int)coordinate.y);
//                        repairPolygon[i] = pixel;
//                    }
//                    List<Pixel> rasters = Scan.scanRaster(repairPolygon);
//                    int bottom = (int) (rollingDataRange.getMinCoordY() * 1);
//                    int left = (int) (rollingDataRange.getMinCoordX() * 1);
//                    int width = (int) (rollingDataRange.getMaxCoordX() - rollingDataRange.getMinCoordX());
//                    int height = (int) (rollingDataRange.getMaxCoordY() - rollingDataRange.getMinCoordY());
//                    int n = 0;
//                    int m = 0;
//                    for (Pixel pixel : rasters) {
//                        try {
//                            n = (pixel.getY() - bottom);
//                            m = (pixel.getX() - left);
//                            if (n >= 0 && m >= 0 && n < height && m < width) {
//                                MatrixItem item = matrixItems[m][n];
//                                if(item == null){
//                                    item = new MatrixItem();
//                                    matrixItems[m][n] = item;
//                                }
//                                item.setRollingTimes(passCount);
//                                item.getSpeedList().add(speed);
//                                item.getVibrateValueList().add(vibration);
//                            }
//                        } catch (Exception ex) {
//                            log.error("(" + m + "," + n + ")像素错误:" + ex.getMessage());
//                        }
//                    }
//                    rasters.clear();
//                    rasters = null;
//                }
//            }
            log.error("生成网格遍数执行任务消耗了 ：" + (System.currentTimeMillis() - start) + "毫秒");
        }
        RollingResult rollingResult = new RollingResult();
        //超限
        RollingResult rollingResultSpeed = new RollingResult();
        //动静碾压
        RollingResult rollingResultVibration = new RollingResult();
        int count0 = 0;
        int count0Speed = 0;
        int count0Vibration = 0;
        String bsae64_string = "";
        String bsae64_string_speed = "";
        String bsae64_string_vibration = "";

        TColorConfig vo = new TColorConfig();
        vo.setType(3L);//超限
        List<TColorConfig> colorConfigs = colorConfigMapper.select(vo);
        vo.setType(6L);//动静碾压
        List<TColorConfig> colorConfigs6 = colorConfigMapper.select(vo);
        synchronized (obj1) {// 同步代码块
            //绘制图片
            //得到图片缓冲区
            BufferedImage bi = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);//INT精确度达到一定,RGB三原色，高度70,宽度150
            //超限次数
            BufferedImage biSpeed = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);//INT精确度达到一定,RGB三原色，高度70,宽度150
            //动静碾压
            BufferedImage biVibration = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);//INT精确度达到一定,RGB三原色，高度70,宽度150
            //得到它的绘制环境(这张图片的笔)
            Graphics2D g2 = (Graphics2D) bi.getGraphics();
            //超限次数
            Graphics2D g2Speed = (Graphics2D) biSpeed.getGraphics();
            //动静碾压
            Graphics2D g2Vibration = (Graphics2D) biVibration.getGraphics();
            List<Coordinate> list = JSONArray.parseArray(damsConstruction.getRanges(), Coordinate.class);
            Coordinate[] pointList = new Coordinate[list.size() + 1];  //最重要，不能遗漏，预先申请好数组空间
            list.toArray(pointList);
            pointList[list.size()] = pointList[0];
            //记录遍数
            GeometryFactory gf = new GeometryFactory();
            Geometry edgePoly = gf.createPolygon(pointList);
            long startTime = System.currentTimeMillis();
            Map<Integer, Color> colorMap = getColorMap(1L);

            for (int i = 0; i < xSize - 1; i++) {
                for (int j = 0; j < ySize - 1; j++) {
                    //计算边界的网格数量
                    Double xTmp = rollingDataRange.getMinCoordX() + i * division;
                    Double yTmp = rollingDataRange.getMinCoordY() + j * division;
                    //判断点是否在多边形内
                    Coordinate point = new Coordinate(xTmp, yTmp);
                    PointLocator a = new PointLocator();
                    boolean p1 = a.intersects(point, edgePoly);
                    if (p1) {
                        count0++;
                        count0Speed++;
                        count0Vibration++;
                        Integer rollingTimes = StoreHouseMap.getStoreHouses2RollingData().get(tableName)[i][j].getRollingTimes();

                        g2.setColor(getColorByCount2(rollingTimes, colorMap));
                        calculateRollingtimes(rollingTimes, rollingResult);
                        g2.fillRect(i, j, 1, 1);

                        // TODO: 2021/10/21  超速次数
                        //获得速度集合
                        LinkedList<Float> speeds = StoreHouseMap.getStoreHouses2RollingData().get(tableName)[i][j].getSpeedList();
                        if (speeds != null) {
                            g2Speed.setColor(getColorByCountSpeed(StringUtils.isNotEmpty(speeds) ? speeds.get(speeds.size() - 1) : new Float(-1), colorConfigs));
                            calculateRollingSpeed(StringUtils.isNotEmpty(speeds) ? speeds.get(speeds.size() - 1) : new Float(-1), rollingResultSpeed, colorConfigs);
                            g2Speed.fillRect(i, j, 1, 1);
                        }

                        //Vibration 动静碾压
                        LinkedList<Double> vibrations = StoreHouseMap.getStoreHouses2RollingData().get(tableName)[i][j].getVibrateValueList();
                        if (vibrations != null) {
                            for (Double vcv : vibrations) {
                                //todo 动静碾压
                                if (StringUtils.isNotNull(vcv)) {
                                    calculateRollingVibrate(vcv, rollingResultVibration, colorConfigs6);
                                }
                            }
                            g2Vibration.setColor(getColorByCountVibrate(StringUtils.isNotEmpty(vibrations) ? Collections.max(vibrations) : new Double(-1), colorConfigs6));
                            g2Vibration.fillRect(i, j, 1, 1);
                        }
                    }
                }
            }
            //count0表示所有属于当前单元工程的轨迹点 time0表示在计算完遍数后，该单元工程内没有碾压的数据
            int time0 = count0 - rollingResult.getTime1() - rollingResult.getTime2() - rollingResult.getTime3() - rollingResult.getTime4() - rollingResult.getTime5() - rollingResult.getTime6() - rollingResult.getTime7() - rollingResult.getTime8()
                    - rollingResult.getTime9() - rollingResult.getTime10() - rollingResult.getTime11() - rollingResult.getTime11Up();
            //因为轨迹点使用缓冲区，所以会出现time0比0小的情况，这里要加一下判断如果小于0证明单元工程全部碾压到了
            if (time0 <= 0) {
                time0 = 0;
            }
            rollingResult.setTime0(time0);

            //速度超限
            int time0Speed = count0Speed - rollingResultSpeed.getTime1() - rollingResultSpeed.getTime2();
            //因为轨迹点使用缓冲区，所以会出现time0比0小的情况，这里要加一下判断如果小于0证明单元工程全部碾压到了
            if (time0Speed <= 0) {
                time0Speed = 0;
            }
            rollingResultSpeed.setTime0(time0Speed);

            long endTime = System.currentTimeMillis();    //获取结束时间
            log.info("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //超限
            ByteArrayOutputStream baosSpeed = new ByteArrayOutputStream();
            //动静碾压
            ByteArrayOutputStream baosVibration = new ByteArrayOutputStream();
            //        bi = (BufferedImage) ImgRotate.imageMisro(bi,0);
            try {
                ImageIO.write(bi, "PNG", baos);
                byte[] bytes = baos.toByteArray();//转换成字节
                bsae64_string = "data:image/png;base64," + Base64.encodeBase64String(bytes);
                baos.close();
                //超限
                ImageIO.write(biSpeed, "PNG", baosSpeed);
                byte[] bytesSpeed = baosSpeed.toByteArray();//转换成字节
                bsae64_string_speed = "data:image/png;base64," + Base64.encodeBase64String(bytesSpeed);
                baosSpeed.close();
                //动静碾压
                ImageIO.write(biVibration, "PNG", baosVibration);
                byte[] bytesVibration = baosVibration.toByteArray();//转换成字节
                bsae64_string_vibration = "data:image/png;base64," + Base64.encodeBase64String(bytesVibration);
                baosVibration.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        ProjCoordinate projCoordinate1 = new ProjCoordinate(rollingDataRange.getMinCoordY(), rollingDataRange.getMinCoordX(), 10);
        ProjCoordinate projCoordinate2 = new ProjCoordinate(rollingDataRange.getMaxCoordY(), rollingDataRange.getMaxCoordX(), 10);
        JSONObject result = new JSONObject();
        result.put("rollingResult", rollingResult);
        result.put("rollingResultSpeed", rollingResultSpeed);
        result.put("rollingResultVibration", rollingResultVibration);
        result.put("base64", bsae64_string);
        result.put("base64Speed", bsae64_string_speed);
        result.put("base64Vibration", bsae64_string_vibration);
        result.put("rollingDataRange", rollingDataRange);
        result.put("pointLeftBottom", projCoordinate1);
        result.put("pointRightTop", projCoordinate2);
        result.put("height", damsConstruction.getGaocheng());
        result.put("cenggao", damsConstruction.getCenggao());
        result.put("range", range);
        //边界
        result.put("ranges", damsConstruction.getRanges());
        result.put("id", damsConstruction.getId());
        List<JSONObject> images = new LinkedList<>();
        result.put("images", images);
        return result;
    }


    /**
     * 平面分析-单元工程
     */
    public JSONObject getHistoryPicMultiThreadingByZhuangByTod(String tableName, Integer cartype) {
        JSONObject result = new JSONObject();
        String id = tableName;
        DamsConstruction damsConstruction = damsConstructionMapper.selectByPrimaryKey(Integer.valueOf(id));
        //工作仓 数据
        tableName = GlobCache.cartableprfix[cartype] + "_" + damsConstruction.getTablename();
        int dataSize;
        String range = tableName.split("_")[0] + "_" + tableName.split("_")[1];
        //1.根据工作仓初设化网格
        //直接根据桩号获得范围
        BigDecimal minOfxList = BigDecimal.valueOf(damsConstruction.getXbegin());
        BigDecimal maxOfxList = BigDecimal.valueOf(damsConstruction.getXend());
        BigDecimal minOfyList = BigDecimal.valueOf(damsConstruction.getYbegin());
        BigDecimal maxOfyList = BigDecimal.valueOf(damsConstruction.getYend());
        StorehouseRange storehouseRange = new StorehouseRange();
        storehouseRange.setMinOfxList(minOfxList.doubleValue());
        storehouseRange.setMaxOfxList(maxOfxList.doubleValue());
        storehouseRange.setMinOfyList(minOfyList.doubleValue());
        storehouseRange.setMaxOfyList(maxOfyList.doubleValue());
        RollingDataRange rollingDataRange = new RollingDataRange();
        rollingDataRange.setMaxCoordY(maxOfyList.doubleValue());
        rollingDataRange.setMinCoordY(minOfyList.doubleValue());
        rollingDataRange.setMaxCoordX(maxOfxList.doubleValue());
        rollingDataRange.setMinCoordX(minOfxList.doubleValue());
        int xSize = (int) Math.abs(damsConstruction.getXend() - damsConstruction.getXbegin());
        int ySize = (int) Math.abs(damsConstruction.getYend() - damsConstruction.getYbegin());
        //所有车
        List<Car> carList = carMapper.findCar();
        List<TRepairData> repairDataList;
        //判断工作仓数据是否存在
        // boolean isHave=StoreHouseMap.getStoreHouses2RollingData().containsKey(tableName);
        long start = System.currentTimeMillis();
        //不存在则进行
        MatrixItem[][] matrix = new MatrixItem[xSize][ySize];
        for (int i = 0; i < xSize; i++) {
            for (int j = 0; j < ySize; j++) {
                matrix[i][j] = new MatrixItem();
                matrix[i][j].setRollingTimes(0);
            }
        }

        // if(2 == dmstatus) {
        shorehouseRange.put(id, storehouseRange);
        StoreHouseMap.getStoreHouses2RollingData().put(tableName, matrix);
        // }
        List<RollingData> rollingDataList_all = new LinkedList<>();

        //2 获得数据库中的数据
        for (Car car : carList) {
            List<RollingData> rollingDataList = rollingDataMapper.getAllRollingDataByVehicleID(tableName, car.getCarID().toString());
            rollingDataList_all.addAll(rollingDataList);
        }
        // 总数据条数
        dataSize = rollingDataList_all.size();
        System.out.println("即将处理》》" + dataSize + "条数据。。");

        //如果是摊铺 需要计算出所有数据的

        // 线程数 四个核心
        int threadNum = dataSize == 0 ? 1 : CORECOUNT;

        //每个线程处理的数据量
        int dataSizeEveryThread = dataSize / (threadNum );
        // 创建一个线程池
        ExecutorService exec = Executors.newFixedThreadPool(threadNum);

        // 定义一个任务集合
        List<Callable<Integer>> tasks = new LinkedList<>();
        CalculateGridForZhuangHistory task;
        List<RollingData> cutList;

        // 确定每条线程的数据
        for (int i = 0; i < threadNum; i++) {

            try {
                if (i == threadNum  - 1) {
                    int index = Math.max(dataSizeEveryThread * i - 1, 0);
                    cutList = rollingDataList_all.subList(index-1, dataSize);
                } else {
                    if (i == 0) {
                        cutList = rollingDataList_all.subList(0, dataSizeEveryThread * (i + 1));
                    } else {
                        cutList = rollingDataList_all.subList(dataSizeEveryThread * i - 2, dataSizeEveryThread * (i + 1));
                    }
                }
                final List<RollingData> listRollingData = cutList;
                task = new CalculateGridForZhuangHistory();
                task.setRollingDataRange(rollingDataRange);
                task.setRollingDataList(listRollingData);
                task.setTableName(tableName);
                task.setxNum(xSize);
                task.setyNum(ySize);
                task.setCartype(cartype);
                // 这里提交的任务容器列表和返回的Future列表存在顺序对应的关系
                tasks.add(task);
            } catch (Exception e) {
                System.out.println("仓位无数据");
            }
        }

        try {
            List<Future<Integer>> results = exec.invokeAll(tasks);
            for (Future<Integer> future : results) {
                future.get();
            }
            // 关闭线程池
            exec.shutdown();
            log.info("线程任务执行结束");
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
          查询出当前仓位的补录区域
         */
        TRepairData record = new TRepairData();
        record.setDamsid(damsConstruction.getId());
        record.setCartype(cartype);
        repairDataList = repairDataMapper.selectTRepairDatas(record);
        //根据补录内容 将仓对应区域的碾压遍数、速度、压实度更新
        if (repairDataList != null && repairDataList.size() > 0) {
            Map<String, MatrixItem[][]> storeHouses2RollingData = StoreHouseMap.getStoreHouses2RollingData();
            MatrixItem[][] matrixItems = storeHouses2RollingData.get(tableName);
            for (TRepairData repairData : repairDataList) {
                String ranges = repairData.getRanges();
                int passCount = repairData.getColorId();
                float speed = repairData.getSpeed().floatValue();
                double vibration = repairData.getVibration();

                List<Coordinate> repairRange = JSONArray.parseArray(ranges, Coordinate.class);
                Pixel[] repairPolygon = new Pixel[repairRange.size()];
                for (int i = 0; i < repairRange.size(); i++) {
                    Coordinate coordinate = repairRange.get(i);
                    Pixel pixel = new Pixel();
                    pixel.setX((int) coordinate.x);
                    pixel.setY((int) coordinate.y);
                    repairPolygon[i] = pixel;
                }
                List<Pixel> rasters = Scan.scanRaster(repairPolygon);
                int bottom = (int) (rollingDataRange.getMinCoordY() * 1);
                int left = (int) (rollingDataRange.getMinCoordX() * 1);
                int width = (int) (rollingDataRange.getMaxCoordX() - rollingDataRange.getMinCoordX());
                int height = (int) (rollingDataRange.getMaxCoordY() - rollingDataRange.getMinCoordY());
                int n = 0;
                int m = 0;
                int  time10 =0;
                for (Pixel pixel : rasters) {
                    try {
                        n = (pixel.getY() - bottom);
                        m = (pixel.getX() - left);
                        if (n >= 0 && m >= 0 && n < height && m < width) {
                            if (matrixItems[m][n] == null) {
                                matrixItems[m][n] = new MatrixItem();

                            }
                            time10++;
                            matrixItems[m][n].setRollingTimes(damsConstruction.getFrequency()+1);
                            matrixItems[m][n].getSpeedList().add(speed);
                            matrixItems[m][n].getVibrateValueList().add(vibration);
                        }
                    } catch (Exception ex) {
                        log.error("(" + m + "," + n + ")像素错误:" + ex.getMessage());
                    }
                }
                System.out.println("扫描点一共"+rasters.size()+"个,十遍点"+time10+"个，X>Y="+xSize+":"+ySize);
                StoreHouseMap.getStoreHouses2RollingData().put(tableName,matrixItems);
                rasters.clear();
            }
        }
        log.error("生成网格遍数执行任务消耗了 ：" + (System.currentTimeMillis() - start) + "毫秒");
        RollingResult rollingResult = new RollingResult();
        //超限
        RollingResult rollingResultSpeed = new RollingResult();
        //动静碾压
        RollingResult rollingResultVibration = new RollingResult();
        RollingResult rollingResultEvolution = new RollingResult();
        int count0 = 0;
        int count0Speed = 0;

        String bsae64_string = "";
        String bsae64_string_speed = "";
        String bsae64_string_vibration = "";
        String bsae64_string_evolution = "";
        Map<Integer, Color> colorMap = getColorMap(GlobCache.carcoloconfigtype[cartype].longValue());

        TColorConfig vo = new TColorConfig();
        vo.setType(3L);//超限
        List<TColorConfig> colorConfigs = colorConfigMapper.select(vo);
        vo.setType(6L);//动静碾压
        List<TColorConfig> colorConfigs6 = colorConfigMapper.select(vo);
        vo.setType(44L);//摊铺平整度颜色
        List<TColorConfig> colorConfigs44 = colorConfigMapper.select(vo);
        Map<Long, MatrixItem> reportdetailsmap = new HashMap<>();
        List<TDamsconstrctionReportDetail> allreportpoint = new LinkedList<>();
        synchronized (obj1) {// 同步代码块
            //绘制图片
            //得到图片缓冲区
            BufferedImage bi = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);//INT精确度达到一定,RGB三原色，高度70,宽度150
            //超限次数
            BufferedImage biSpeed = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);//INT精确度达到一定,RGB三原色，高度70,宽度150
            //动静碾压
            BufferedImage biVibration = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);//INT精确度达到一定,RGB三原色，高度70,宽度150
            BufferedImage biEvolution = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);//INT精确度达到一定,RGB三原色，高度70,宽度150
            //得到它的绘制环境(这张图片的笔)
            Graphics2D g2 = (Graphics2D) bi.getGraphics();
            //超限次数
            Graphics2D g2Speed = (Graphics2D) biSpeed.getGraphics();
            //动静碾压
            Graphics2D g2Vibration = (Graphics2D) biVibration.getGraphics();
            //平整度
            Graphics2D g2Evolution = (Graphics2D) biEvolution.getGraphics();
            List<Coordinate> list = JSONArray.parseArray(damsConstruction.getRanges(), Coordinate.class);
            Coordinate[] pointList = new Coordinate[list.size() + 1];  //最重要，不能遗漏，预先申请好数组空间
            list.toArray(pointList);
            pointList[list.size()] = pointList[0];
            //记录遍数
            GeometryFactory gf = new GeometryFactory();
            Geometry edgePoly = gf.createPolygon(pointList);
            long startTime = System.currentTimeMillis();

            //查询该区域是否存在试验点数据。
            TDamsconstructionReport treport = new TDamsconstructionReport();
            treport.setDamgid(Long.valueOf(damsConstruction.getId()));
            List<TDamsconstructionReport> allreport = tDamsconstructionReportService.selectTDamsconstructionReportList(treport);

            if (allreport.size() > 0) {
                for (TDamsconstructionReport tDamsconstructionReport : allreport) {
                    tDamsconstructionReport = tDamsconstructionReportService.selectTDamsconstructionReportByGid(tDamsconstructionReport.getGid());

                    List<TDamsconstrctionReportDetail> details = tDamsconstructionReport.gettDamsconstrctionReportDetailList();
                    allreportpoint.addAll(details);
                }


            }

            for (TDamsconstrctionReportDetail next : allreportpoint) {
                List<Coordinate> rlist = JSONArray.parseArray(next.getRanges(), Coordinate.class);
                if (rlist.size() == 1) {
                    Coordinate tempc = rlist.get(0);
                    double xxd = tempc.getOrdinate(0) - rollingDataRange.getMinCoordX();
                    int portx = (int) xxd;
                    double yyd = tempc.getOrdinate(1) - rollingDataRange.getMinCoordY();
                    int porty = (int) yyd;

                    if (null != StoreHouseMap.getStoreHouses2RollingData().get(tableName)[portx][porty]) {
                        reportdetailsmap.put(next.getGid(), StoreHouseMap.getStoreHouses2RollingData().get(tableName)[portx][porty]);
                    }
                }

            }
            //如果是摊铺推平 需要先计算出所有碾压区域的最后一个后点高程的平均值，然后通过与平均值的差值展示显示平整度
            MatrixItem[][] items = StoreHouseMap.getStoreHouses2RollingData().get(tableName);
            if (cartype == 2) {//推平结果处理

                Double begin_evolution = damsConstruction.getGaocheng();
                Double target_houdu = damsConstruction.getCenggao();

                for (int i = 0; i < xSize - 1; i++) {
                    for (int j = 0; j < ySize - 1; j++) {
                        //计算边界的网格数量
                        double xTmp = rollingDataRange.getMinCoordX() + i * division;
                        double yTmp = rollingDataRange.getMinCoordY() + j * division;
                        //判断点是否在多边形内
                        Coordinate point = new Coordinate(xTmp, yTmp);
                        PointLocator a = new PointLocator();
                        boolean p1 = a.intersects(point, edgePoly);
                        if (p1) {
                            if (dataSize == 0) {
                                assert repairDataList != null;
                                if (repairDataList.size() == 0) {
                                    break;
                                }
                            }

                            try {
                                MatrixItem m = StoreHouseMap.getStoreHouses2RollingData().get(tableName)[i][j];
                                if (null != m && m.getCurrentEvolution().size() > 0) {
                                    LinkedList<Float> evlist = m.getCurrentEvolution();
                                    Float currentevolution2 = evlist.size() == 0 ? 0f : evlist.getLast();
                                    float tphoudu = 100.0f * (currentevolution2 - begin_evolution.floatValue()) - target_houdu.floatValue();
                                    g2Evolution.setColor(getColorByCountEvolution(tphoudu, colorConfigs44));
                                    calculateRollingEvolution(tphoudu, rollingResult, colorConfigs44);
                                    g2Evolution.fillRect(i, j, 1, 1);
                                }


                            } catch (Exception e) {
                                log.error("出现错误。");
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                e.printStackTrace(new PrintStream(baos));
                                String exception = baos.toString();
                                log.error(exception);
                            }
                        }
                    }
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
                    ImageIO.write(biEvolution, "PNG", baos);
                    byte[] bytes = baos.toByteArray();//转换成字节
                    bsae64_string_evolution = "data:image/png;base64," + Base64.encodeBase64String(bytes);
                    baos.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            } else if (cartype == 1) {//碾压结果处理
                for (int i = 0; i < xSize - 1; i++) {
                    for (int j = 0; j < ySize - 1; j++) {
                        //计算边界的网格数量
                        double xTmp = rollingDataRange.getMinCoordX() + i * division;
                        double yTmp = rollingDataRange.getMinCoordY() + j * division;
                        //判断点是否在多边形内
                        Coordinate point = new Coordinate(xTmp, yTmp);
                        PointLocator a = new PointLocator();
                        boolean p1 = a.intersects(point, edgePoly);
                        if (p1) {
                            if (dataSize == 0) {
                                assert repairDataList != null;
                                if (repairDataList.size() == 0) {
                                    break;
                                }
                            }
                            count0++;
                            count0Speed++;



                    MatrixItem  item = items[i][j];
                            Integer rollingTimes = item.getRollingTimes();
                            try {
                                if (items[i][j + 1].getRollingTimes() > rollingTimes && items[i][j - 1].getRollingTimes() > rollingTimes) {
                                    rollingTimes++;

                                }
                                if (items[i + 1][j].getRollingTimes() > rollingTimes && items[i - 1][j].getRollingTimes() > rollingTimes) {
                                    rollingTimes++;
                                }

                            } catch (Exception ignored) {

                            }


                            g2.setColor(getColorByCount2(rollingTimes, colorMap));
                            calculateRollingtimes(rollingTimes, rollingResult);
                            g2.fillRect(i, j, 1, 1);

                            // TODO: 2021/10/21  超速次数
                            //获得速度集合
                            LinkedList<Float> speeds = items[i][j].getSpeedList();
                            if (speeds != null) {
                                g2Speed.setColor(getColorByCountSpeed(StringUtils.isNotEmpty(speeds) ? speeds.get(speeds.size() - 1) : new Float(-1), colorConfigs));
                                calculateRollingSpeed(StringUtils.isNotEmpty(speeds) ? speeds.get(speeds.size() - 1) : new Float(-1), rollingResultSpeed, colorConfigs);
                                g2Speed.fillRect(i, j, 2, 2);
                            }

                            try {
                                //Vibration 动静碾压
                                MatrixItem items2 = items[i][j];
                                if (null != items2) {
                                    LinkedList<Double> vibrations = items2.getVibrateValueList();
                                    if (null != vibrations && vibrations.size() > 0) {
                                        try {
                                            for (Double vcv : vibrations) {
                                                //todo 动静碾压
                                                if (StringUtils.isNotNull(vcv)) {
                                                    calculateRollingVibrate(vcv, rollingResultVibration, colorConfigs6);
                                                }
                                            }
                                            g2Vibration.setColor(getColorByCountVibrate(StringUtils.isNotEmpty(vibrations) ? Collections.max(vibrations) : new Double(-1), colorConfigs6));
                                            g2Vibration.fillRect(i, j, 2, 2);
                                        } catch (Exception ignored) {

                                        }
                                    }
                                }
                            } catch (Exception e) {
                                log.error("出现错误。");
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                e.printStackTrace(new PrintStream(baos));
                                String exception = baos.toString();
                                log.error(exception);
                            }
                        }
                    }
                }
                System.out.println("矩形区域内一共有点"+count0+"个");
                //count0表示所有属于当前单元工程的轨迹点 time0表示在计算完遍数后，该单元工程内没有碾压的数据
                int time0 = count0 - rollingResult.getTime1() - rollingResult.getTime2() - rollingResult.getTime3() - rollingResult.getTime4() - rollingResult.getTime5() - rollingResult.getTime6() - rollingResult.getTime7() - rollingResult.getTime8()
                        - rollingResult.getTime9() - rollingResult.getTime10() - rollingResult.getTime11() - rollingResult.getTime11Up();
                //因为轨迹点使用缓冲区，所以会出现time0比0小的情况，这里要加一下判断如果小于0证明单元工程全部碾压到了
                if (time0 <= 0) {
                    time0 = 0;
                }
                rollingResult.setTime0(time0);

                //速度超限
                int time0Speed = count0Speed - rollingResultSpeed.getTime1() - rollingResultSpeed.getTime2();
                //因为轨迹点使用缓冲区，所以会出现time0比0小的情况，这里要加一下判断如果小于0证明单元工程全部碾压到了
                if (time0Speed <= 0) {
                    time0Speed = 0;
                }
                rollingResultSpeed.setTime0(time0Speed);

                long endTime = System.currentTimeMillis();    //获取结束时间
                log.info("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                //超限
                ByteArrayOutputStream baosSpeed = new ByteArrayOutputStream();
                //动静碾压
                ByteArrayOutputStream baosVibration = new ByteArrayOutputStream();
                //        bi = (BufferedImage) ImgRotate.imageMisro(bi,0);
                try {
                    ImageIO.write(bi, "PNG", baos);
                    byte[] bytes = baos.toByteArray();//转换成字节
                    bsae64_string = "data:image/png;base64," + Base64.encodeBase64String(bytes);
                    baos.close();
                    //超限
                    ImageIO.write(biSpeed, "PNG", baosSpeed);
                    byte[] bytesSpeed = baosSpeed.toByteArray();//转换成字节
                    bsae64_string_speed = "data:image/png;base64," + Base64.encodeBase64String(bytesSpeed);
                    baosSpeed.close();
                    //动静碾压
                    ImageIO.write(biVibration, "PNG", baosVibration);
                    byte[] bytesVibration = baosVibration.toByteArray();//转换成字节
                    bsae64_string_vibration = "data:image/png;base64," + Base64.encodeBase64String(bytesVibration);
                    baosVibration.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        StoreHouseMap.getStoreHouses2RollingData().remove(tableName);

        ProjCoordinate projCoordinate1 = new ProjCoordinate(rollingDataRange.getMinCoordY(), rollingDataRange.getMinCoordX(), 10);
        ProjCoordinate projCoordinate2 = new ProjCoordinate(rollingDataRange.getMaxCoordY(), rollingDataRange.getMaxCoordX(), 10);

        result.put("rollingResult", rollingResult);
        result.put("rollingResultSpeed", rollingResultSpeed);
        result.put("rollingResultVibration", rollingResultVibration);
        result.put("rollingResultEvolution", rollingResultEvolution);
        result.put("base64", bsae64_string);
        result.put("base64Speed", bsae64_string_speed);
        result.put("base64Vibration", bsae64_string_vibration);
        result.put("base64Evolution", bsae64_string_evolution);
        result.put("rollingDataRange", rollingDataRange);
        result.put("pointLeftBottom", projCoordinate1);
        result.put("pointRightTop", projCoordinate2);
        result.put("height", damsConstruction.getGaocheng());
        result.put("cenggao", damsConstruction.getCenggao());
        result.put("range", range);
        result.put("reportdetailsmap", reportdetailsmap);
        result.put("allreportpoint", allreportpoint);
        //边界
        result.put("ranges", damsConstruction.getRanges());
        result.put("id", damsConstruction.getId());
        List<JSONObject> images = new LinkedList<>();
        result.put("images", images);
        return result;
    }


    public JSONObject getdam_rotate(String userName, String tableName, Integer cartype) {
        JSONObject result = new JSONObject();
        String id = tableName;
        DamsConstruction damsConstruction = damsConstructionMapper.selectByPrimaryKey(Integer.valueOf(id));
        int dmstatus = damsConstruction.getStatus();
        //工作仓 数据
        tableName = GlobCache.cartableprfix[cartype] + "_" + damsConstruction.getTablename();
        int dataSize = 0;
        String range = tableName.split("_")[0] + "_" + tableName.split("_")[1];
        //1.根据工作仓初设化网格
        //直接根据桩号获得范围
        BigDecimal minOfxList = BigDecimal.valueOf(damsConstruction.getXbegin());
        BigDecimal maxOfxList = BigDecimal.valueOf(damsConstruction.getXend());
        BigDecimal minOfyList = BigDecimal.valueOf(damsConstruction.getYbegin());
        BigDecimal maxOfyList = BigDecimal.valueOf(damsConstruction.getYend());
        StorehouseRange storehouseRange = new StorehouseRange();
        storehouseRange.setMinOfxList(minOfxList.doubleValue());
        storehouseRange.setMaxOfxList(maxOfxList.doubleValue());
        storehouseRange.setMinOfyList(minOfyList.doubleValue());
        storehouseRange.setMaxOfyList(maxOfyList.doubleValue());
        RollingDataRange rollingDataRange = new RollingDataRange();
        rollingDataRange.setMaxCoordY(maxOfyList.doubleValue());
        rollingDataRange.setMinCoordY(minOfyList.doubleValue());
        rollingDataRange.setMaxCoordX(maxOfxList.doubleValue());
        rollingDataRange.setMinCoordX(minOfxList.doubleValue());
        int xSize = (int) Math.abs(damsConstruction.getXend() - damsConstruction.getXbegin());
        int ySize = (int) Math.abs(damsConstruction.getYend() - damsConstruction.getYbegin());
        //所有车
        List<Car> carList = carMapper.findCar();
        List<TRepairData> repairDataList;
        //判断工作仓数据是否存在
        // boolean isHave=StoreHouseMap.getStoreHouses2RollingData().containsKey(tableName);
        long start = System.currentTimeMillis();
        if (true) {//不存在则进行
            MatrixItem[][] matrix = new MatrixItem[xSize][ySize];
            for (int i = 0; i < xSize; i++) {
                for (int j = 0; j < ySize; j++) {
                    matrix[i][j] = new MatrixItem();
                    matrix[i][j].setRollingTimes(0);
                }
            }

            // if(2 == dmstatus) {
            shorehouseRange.put(id, storehouseRange);
            StoreHouseMap.getStoreHouses2RollingData().put(tableName, matrix);
            // }
            List<RollingData> rollingDataList_all = new LinkedList<>();

            //2 获得数据库中的数据
            for (Car car : carList) {
                List<RollingData> rollingDataList = rollingDataMapper.getAllRollingDataByVehicleID(tableName, car.getCarID().toString());
                rollingDataList_all.addAll(rollingDataList);
            }
            // 总数据条数
            dataSize = rollingDataList_all.size();
            System.out.println("即将处理》》" + dataSize + "条数据。。");

            //如果是摊铺 需要计算出所有数据的

            // 线程数 四个核心
            int threadNum = dataSize == 0 ? 1 : CORECOUNT;
            // 余数
            int special = dataSize % threadNum;
            //每个线程处理的数据量
            int dataSizeEveryThread = dataSize / threadNum;
            // 创建一个线程池
            ExecutorService exec = Executors.newFixedThreadPool(threadNum);

            // 定义一个任务集合
            List<Callable<Integer>> tasks = new LinkedList<Callable<Integer>>();
            Callable<Integer> task = null;
            List<RollingData> cutList = null;

            // 确定每条线程的数据
            for (int i = 0; i < threadNum; i++) {

                try {
                    if (i == threadNum - 1) {
                        int index = dataSizeEveryThread * i - 1 < 0 ? 0 : dataSizeEveryThread * i - 1;
                        cutList = rollingDataList_all.subList(index, dataSize);
                    } else {
                        if (i == 0) {
                            cutList = rollingDataList_all.subList(dataSizeEveryThread * i, dataSizeEveryThread * (i + 1));
                        } else {
                            cutList = rollingDataList_all.subList(dataSizeEveryThread * i - 1, dataSizeEveryThread * (i + 1));
                        }
                    }
                    final List<RollingData> listRollingData = cutList;
                    task = new CalculateGridForZhuangHistoryForCloseCang();
                    ((CalculateGridForZhuangHistoryForCloseCang) task).setRollingDataRange(rollingDataRange);
                    ((CalculateGridForZhuangHistoryForCloseCang) task).setRollingDataList(listRollingData);
                    ((CalculateGridForZhuangHistoryForCloseCang) task).setTableName(tableName);
                    ((CalculateGridForZhuangHistoryForCloseCang) task).setxNum(xSize);
                    ((CalculateGridForZhuangHistoryForCloseCang) task).setyNum(ySize);
                    // 这里提交的任务容器列表和返回的Future列表存在顺序对应的关系
                    tasks.add(task);
                } catch (Exception e) {
                    System.out.println("仓位无数据");
                }
            }

            try {
                List<Future<Integer>> results = exec.invokeAll(tasks);
                for (Future<Integer> future : results) {
                    log.info(future.get().toString());
                }
                // 关闭线程池
                exec.shutdown();
                log.info("线程任务执行结束");
            } catch (Exception e) {
                e.printStackTrace();
            }

            /**
             * 查询出当前仓位的补录区域
             */
            TRepairData record = new TRepairData();
            record.setDamsid(damsConstruction.getId());
            record.setCartype(cartype);
            repairDataList = repairDataMapper.selectTRepairDatas(record);
            //根据补录内容 将仓对应区域的碾压遍数、速度、压实度更新
            if (repairDataList != null && repairDataList.size() > 0) {
                Map<String, MatrixItem[][]> storeHouses2RollingData = storeHouseMap.getStoreHouses2RollingData();
                MatrixItem[][] matrixItems = storeHouses2RollingData.get(tableName);
                for (TRepairData repairData : repairDataList) {
                    String ranges = repairData.getRanges();
                    int passCount = repairData.getColorId();
                    float speed = repairData.getSpeed().floatValue();
                    double vibration = repairData.getVibration();

                    List<Coordinate> repairRange = JSONArray.parseArray(ranges, Coordinate.class);
                    Pixel[] repairPolygon = new Pixel[repairRange.size()];
                    for (int i = 0; i < repairRange.size(); i++) {
                        Coordinate coordinate = repairRange.get(i);
                        Pixel pixel = new Pixel();
                        pixel.setX((int) coordinate.x);
                        pixel.setY((int) coordinate.y);
                        repairPolygon[i] = pixel;
                    }
                    List<Pixel> rasters = Scan.scanRaster(repairPolygon);
                    int bottom = (int) (rollingDataRange.getMinCoordY() * 1);
                    int left = (int) (rollingDataRange.getMinCoordX() * 1);
                    int width = (int) (rollingDataRange.getMaxCoordX() - rollingDataRange.getMinCoordX());
                    int height = (int) (rollingDataRange.getMaxCoordY() - rollingDataRange.getMinCoordY());
                    int n = 0;
                    int m = 0;
                    for (Pixel pixel : rasters) {
                        try {
                            n = (pixel.getY() - bottom);
                            m = (pixel.getX() - left);
                            if (n >= 0 && m >= 0 && n < height && m < width) {
                                MatrixItem item = matrixItems[m][n];
                                if (item == null) {
                                    item = new MatrixItem();
                                    matrixItems[m][n] = item;
                                }
                                item.setRollingTimes(passCount);
                                item.getSpeedList().add(speed);
                                item.getVibrateValueList().add(vibration);
                            }
                        } catch (Exception ex) {
                            log.error("(" + m + "," + n + ")像素错误:" + ex.getMessage());
                        }
                    }
                    rasters.clear();
                    rasters = null;
                }
            }
            log.error("生成网格遍数执行任务消耗了 ：" + (System.currentTimeMillis() - start) + "毫秒");
        }
        RollingResult rollingResult = new RollingResult();
        //超限
        RollingResult rollingResultSpeed = new RollingResult();
        //动静碾压
        RollingResult rollingResultVibration = new RollingResult();
        RollingResult rollingResultEvolution = new RollingResult();
        int count0 = 0;
        int count0Speed = 0;
        int count0Vibration = 0;
        int count0Evolution = 0;
        String bsae64_string = "";
        String bsae64_string_speed = "";
        String bsae64_string_vibration = "";
        String bsae64_string_evolution = "";
        Map<Integer, Color> colorMap = getColorMap(GlobCache.carcoloconfigtype[cartype].longValue());

        TColorConfig vo = new TColorConfig();
        vo.setType(3L);//超限
        List<TColorConfig> colorConfigs = colorConfigMapper.select(vo);
        vo.setType(6L);//动静碾压
        List<TColorConfig> colorConfigs6 = colorConfigMapper.select(vo);
        vo.setType(44l);//摊铺平整度颜色
        List<TColorConfig> colorConfigs44 = colorConfigMapper.select(vo);
        Map<Long, MatrixItem> reportdetailsmap = new HashMap<>();
        List<TDamsconstrctionReportDetail> allreportpoint = new LinkedList<>();
        synchronized (obj1) {// 同步代码块

            List<Coordinate> list = JSONArray.parseArray(damsConstruction.getRanges(), Coordinate.class);
            Coordinate[] pointList = new Coordinate[list.size() + 1];  //最重要，不能遗漏，预先申请好数组空间
            list.toArray(pointList);
            pointList[list.size()] = pointList[0];
            //记录遍数
            GeometryFactory gf = new GeometryFactory();
            Geometry edgePoly = gf.createPolygon(pointList);
            long startTime = System.currentTimeMillis();

            //查询该区域是否存在试验点数据。
            TDamsconstructionReport treport = new TDamsconstructionReport();
            treport.setDamgid(Long.valueOf(damsConstruction.getId()));
            List<TDamsconstructionReport> allreport = tDamsconstructionReportService.selectTDamsconstructionReportList(treport);

            if (allreport.size() > 0) {
                for (TDamsconstructionReport tDamsconstructionReport : allreport) {
                    tDamsconstructionReport = tDamsconstructionReportService.selectTDamsconstructionReportByGid(tDamsconstructionReport.getGid());

                    List<TDamsconstrctionReportDetail> details = tDamsconstructionReport.gettDamsconstrctionReportDetailList();
                    allreportpoint.addAll(details);
                }
            }
            for (Iterator<TDamsconstrctionReportDetail> iterator = allreportpoint.iterator(); iterator.hasNext(); ) {
                TDamsconstrctionReportDetail next = iterator.next();
                List<Coordinate> rlist = JSONArray.parseArray(next.getRanges(), Coordinate.class);
                if (rlist.size() == 1) {
                    Coordinate tempc = rlist.get(0);
                    Double xxd = tempc.getOrdinate(0) - rollingDataRange.getMinCoordX().doubleValue();
                    int portx = xxd.intValue();
                    Double yyd = tempc.getOrdinate(1) - rollingDataRange.getMinCoordY().doubleValue();
                    int porty = yyd.intValue();

                    if (null != StoreHouseMap.getStoreHouses2RollingData().get(tableName)[portx][porty]) {
                        reportdetailsmap.put(next.getGid(), StoreHouseMap.getStoreHouses2RollingData().get(tableName)[portx][porty]);
                    }
                }

            }


            if (cartype == 1) {//碾压结果处理
                for (int i = 0; i < xSize - 1; i++) {
                    for (int j = 0; j < ySize - 1; j++) {
                        //计算边界的网格数量
                        Double xTmp = rollingDataRange.getMinCoordX() + i * division;
                        Double yTmp = rollingDataRange.getMinCoordY() + j * division;
                        //判断点是否在多边形内
                        Coordinate point = new Coordinate(xTmp, yTmp);
                        PointLocator a = new PointLocator();
                        boolean p1 = a.intersects(point, edgePoly);
                        if (p1) {
                            if (dataSize == 0 && repairDataList.size() == 0) {
                                break;
                            }
                            count0++;
                            count0Speed++;
                            count0Vibration++;
                            Integer rollingTimes = StoreHouseMap.getStoreHouses2RollingData().get(tableName)[i][j].getRollingTimes();
                            calculateRollingtimes(rollingTimes, rollingResult);

                        }
                    }
                }

                //count0表示所有属于当前单元工程的轨迹点 time0表示在计算完遍数后，该单元工程内没有碾压的数据
                int time0 = count0 - rollingResult.getTime1() - rollingResult.getTime2() - rollingResult.getTime3() - rollingResult.getTime4() - rollingResult.getTime5() - rollingResult.getTime6() - rollingResult.getTime7() - rollingResult.getTime8()
                        - rollingResult.getTime9() - rollingResult.getTime10() - rollingResult.getTime11() - rollingResult.getTime11Up();
                //因为轨迹点使用缓冲区，所以会出现time0比0小的情况，这里要加一下判断如果小于0证明单元工程全部碾压到了
                if (time0 <= 0) {
                    time0 = 0;
                }
                rollingResult.setTime0(time0);

                //速度超限
                int time0Speed = count0Speed - rollingResultSpeed.getTime1() - rollingResultSpeed.getTime2();
                //因为轨迹点使用缓冲区，所以会出现time0比0小的情况，这里要加一下判断如果小于0证明单元工程全部碾压到了
                if (time0Speed <= 0) {
                    time0Speed = 0;
                }
                rollingResultSpeed.setTime0(time0Speed);

            }
        }

        StoreHouseMap.getStoreHouses2RollingData().remove(tableName);


        result.put("rollingResult", rollingResult);

        return result;
    }


    public JSONObject getHistoryPicByElvacation(String bottom, String top) throws InterruptedException, ExecutionException {
        String tableName = String.valueOf(((new Date()).getTime()));
        Double Elabottom = Double.valueOf(bottom);
        Double Elatop = Double.valueOf(top);
        RangeZhuang rangeZhuang = tableMapper.getRangeZhuangDataByEla(Elabottom, Elatop);
        RollingDataRange rollingDataRange = new RollingDataRange();
        //从数据库中获得范围
        rollingDataRange.setMaxCoordX(rangeZhuang.getXend());
        rollingDataRange.setMinCoordX(rangeZhuang.getXbegin());
        rollingDataRange.setMaxCoordY(rangeZhuang.getYend());
        rollingDataRange.setMinCoordY(rangeZhuang.getYbegin());
        //记录外接矩形的范围
        StorehouseRange storehouseRange = new StorehouseRange();
        storehouseRange.setMinOfxList(rollingDataRange.getMinCoordY());
        storehouseRange.setMaxOfxList(rollingDataRange.getMaxCoordY());
        storehouseRange.setMinOfyList(rollingDataRange.getMinCoordX());
        storehouseRange.setMaxOfyList(rollingDataRange.getMaxCoordX());
        Integer yNum = (int) Math.ceil((rollingDataRange.getMaxCoordY() - rollingDataRange.getMinCoordY()) / division);//行数
        Integer xNum = (int) Math.ceil((rollingDataRange.getMaxCoordX() - rollingDataRange.getMinCoordX()) / division);//列数
        MatrixItem[][] matrix = new MatrixItem[xNum][yNum];
        for (int x = 0; x < xNum; x++) {
            for (int y = 0; y < yNum; y++) {
                matrix[x][y] = new MatrixItem();
                matrix[x][y].setRollingTimes(0);
            }
        }
        shorehouseRange.put(tableName, storehouseRange);
        StoreHouseMap.getStoreHouses2RollingData().put(tableName, matrix);
        List<Car> carList = carMapper.findCar();
        long start = System.currentTimeMillis();
        //固定线程的数量
        for (Car car : carList) {
            List<RollingData> rollingDataList = rollingDataMapper.getAllRollingDataByVehicleIDByEla(car.getCarID().toString(), Elabottom, Elatop);
            // 总数据条数
            int dataSize = rollingDataList.size();
            if (dataSize < 10) {
                continue;
            }
            if (dataSize == 0) {
                continue;
            }
            // 线程数 四个核心
            int threadNum = CORECOUNT;
            // 余数
            int special = dataSize % threadNum;
            //每个线程处理的数据量
            int dataSizeEveryThread = dataSize / threadNum;
            // 创建一个线程池
            ExecutorService exec = Executors.newFixedThreadPool(threadNum);
            // 定义一个任务集合
            List<Callable<Integer>> tasks = new LinkedList<Callable<Integer>>();
            Callable<Integer> task = null;
            List<RollingData> cutList = null;
            // 确定每条线程的数据
            for (int i = 0; i < threadNum; i++) {
                if (i == threadNum - 1) {
                    int index = dataSizeEveryThread * i - 1 < 0 ? 0 : dataSizeEveryThread * i - 1;
                    cutList = rollingDataList.subList(index, dataSize);
                } else {
                    if (i == 0) {
                        cutList = rollingDataList.subList(dataSizeEveryThread * i, dataSizeEveryThread * (i + 1));
                    } else {
                        cutList = rollingDataList.subList(dataSizeEveryThread * i - 1, dataSizeEveryThread * (i + 1));
                    }
                }
                final List<RollingData> listRollingData = cutList;
                task = new CalculateGridForZhuang();
                ((CalculateGridForZhuang) task).setRollingDataRange(rollingDataRange);
                ((CalculateGridForZhuang) task).setRollingDataList(listRollingData);
                ((CalculateGridForZhuang) task).setTableName(tableName);
                ((CalculateGridForZhuang) task).setxNum(xNum);
                ((CalculateGridForZhuang) task).setyNum(yNum);
                // 这里提交的任务容器列表和返回的Future列表存在顺序对应的关系
                tasks.add(task);
            }
            List<Future<Integer>> results = exec.invokeAll(tasks);
            for (Future<Integer> future : results) {
                future.get().toString();
            }
            // 关闭线程池
            exec.shutdown();
            log.info("线程任务执行结束");
        }
        log.error("生成网格遍数执行任务消耗了 ：" + (System.currentTimeMillis() - start) + "毫秒");
        //绘制图片
        //得到图片缓冲区
        int dd = 5;//图片精度
        /*碾压轨迹遍数的绘制*/
        BufferedImage bi = new BufferedImage(xNum, yNum, BufferedImage.TYPE_INT_ARGB);//TYPE_INT_ARGB 为透明的 INT精确度达到一定,RGB三原色，高度70,宽度150
        //得到它的绘制环境(这张图片的笔)
        Graphics2D g2 = (Graphics2D) bi.getGraphics();
        RollingResult rollingResult = new RollingResult();
        Coordinate[] pointList = new Coordinate[5];
        for (int i = 0; i < 5; i++) {
            Coordinate aPoint = new Coordinate(0, 0, 0);
            pointList[i] = aPoint;
        }
        pointList[0].x = rollingDataRange.getMinCoordX();
        pointList[0].y = rollingDataRange.getMinCoordY();
        pointList[1].x = rollingDataRange.getMaxCoordX();
        pointList[1].y = rollingDataRange.getMinCoordY();
        pointList[2].x = rollingDataRange.getMaxCoordX();
        pointList[2].y = rollingDataRange.getMaxCoordY();
        pointList[3].x = rollingDataRange.getMinCoordX();
        pointList[3].y = rollingDataRange.getMaxCoordY();
        pointList[4] = pointList[0];
        GeometryFactory gf = new GeometryFactory();
        Geometry edgePoly = gf.createPolygon(pointList);
        long drawstart = System.currentTimeMillis();
        int count0 = 0;
        for (int i = 0; i < xNum - 1; i++) {
            for (int j = 0; j < yNum - 1; j++) {
                //计算边界的网格数量
                Double xTmp = rollingDataRange.getMinCoordX() + i * division;
                Double yTmp = rollingDataRange.getMinCoordY() + j * division;
                //判断点是否在多边形内
                Coordinate point = new Coordinate(xTmp, yTmp);
                PointLocator a = new PointLocator();
                boolean p1 = a.intersects(point, edgePoly);
                if (p1) {
                    count0++;
                    Integer rollingTimes = StoreHouseMap.getStoreHouses2RollingData().get(tableName)[i][j].getRollingTimes();
                    calculateRollingtimes(rollingTimes, rollingResult);
                    g2.setColor(getColorByCount2(rollingTimes));
                    g2.fillRect(i, j, 1, 1);
                }
            }
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bi, "PNG", baos);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] bytes = baos.toByteArray();//转换成字节
        String bsae64_string = "data:image/png;base64," + Base64.encodeBase64String(bytes);
        log.error("绘图执行任务消耗了 ：" + (System.currentTimeMillis() - drawstart) + "毫秒");
        //无经纬度概念 无需坐标转换
        ProjCoordinate projCoordinate1 = new ProjCoordinate(rollingDataRange.getMinCoordY(), rollingDataRange.getMinCoordX(), Elabottom);
        ProjCoordinate projCoordinate2 = new ProjCoordinate(rollingDataRange.getMaxCoordY(), rollingDataRange.getMaxCoordX(), Elabottom);
        JSONObject result = new JSONObject();
        rollingResult.setTime0(count0 - rollingResult.getTime1() - rollingResult.getTime2() - rollingResult.getTime3() - rollingResult.getTime4() - rollingResult.getTime5() - rollingResult.getTime6() - rollingResult.getTime7() - rollingResult.getTime8()
                - rollingResult.getTime9() - rollingResult.getTime10() - rollingResult.getTime11() - rollingResult.getTime11Up());
        result.put("rollingResult", rollingResult);
        result.put("base64", bsae64_string);
        result.put("rollingDataRange", rollingDataRange);
        result.put("pointLeftBottom", projCoordinate1);
        result.put("pointRightTop", projCoordinate2);
        return result;
    }

    private void calculateRollingtimes(Integer rollingTimes, RollingResult rollingResult) {
        if (rollingTimes.equals(0)) {
            rollingResult.setTime0(rollingResult.getTime0() + 1);
        }
        if (rollingTimes.equals(1)) {
            rollingResult.setTime1(rollingResult.getTime1() + 1);
        }
        if (rollingTimes.equals(2)) {
            rollingResult.setTime2(rollingResult.getTime2() + 1);
        }
        if (rollingTimes.equals(3)) {
            rollingResult.setTime3(rollingResult.getTime3() + 1);
        }
        if (rollingTimes.equals(4)) {
            rollingResult.setTime4(rollingResult.getTime4() + 1);
        }
        if (rollingTimes.equals(5)) {
            rollingResult.setTime5(rollingResult.getTime5() + 1);
        }
        if (rollingTimes.equals(6)) {
            rollingResult.setTime6(rollingResult.getTime6() + 1);
        }
        if (rollingTimes.equals(7)) {
            rollingResult.setTime7(rollingResult.getTime7() + 1);
        }
        if (rollingTimes.equals(8)) {
            rollingResult.setTime8(rollingResult.getTime8() + 1);
        }
        if (rollingTimes.equals(9)) {
            rollingResult.setTime9(rollingResult.getTime9() + 1);
        }
        if (rollingTimes.equals(10)) {
            rollingResult.setTime10(rollingResult.getTime10() + 1);
        }
        if (rollingTimes.equals(11)) {
            rollingResult.setTime11(rollingResult.getTime11() + 1);
        }
        if (rollingTimes > 11) {
            rollingResult.setTime11Up(rollingResult.getTime11Up() + 1);
        }
    }

    /**
     * 无车辆id
     *
     * @param id
     * @param userName
     * @param tablename
     * @param timestamp
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public JSONObject realTimeZhuang(String id, String userName, String tablename, String timestamp) throws InterruptedException, ExecutionException {
        //获得时间戳
        String rollingResultKey = timestamp;
        //0.获得工作仓的范围
        DamsConstruction damsConstruction = damsConstructionMapper.selectByPrimaryKey(Integer.valueOf(id));
        //工作仓 数据
        String tableName = damsConstruction.getTablename();
        String range = tableName.split("_")[0] + "_" + tableName.split("_")[1];
        //1.根据工作仓初设化网格
        //直接根据桩号获得范围
        BigDecimal minOfxList = BigDecimal.valueOf(damsConstruction.getXbegin());
        BigDecimal maxOfxList = BigDecimal.valueOf(damsConstruction.getXend());
        BigDecimal minOfyList = BigDecimal.valueOf(damsConstruction.getYbegin());
        BigDecimal maxOfyList = BigDecimal.valueOf(damsConstruction.getYend());
        StorehouseRange storehouseRange = new StorehouseRange();
        storehouseRange.setMinOfxList(minOfxList.doubleValue());
        storehouseRange.setMaxOfxList(maxOfxList.doubleValue());
        storehouseRange.setMinOfyList(minOfyList.doubleValue());
        storehouseRange.setMaxOfyList(maxOfyList.doubleValue());
        RollingDataRange rollingDataRange = new RollingDataRange();
        rollingDataRange.setMaxCoordY(maxOfyList.doubleValue());
        rollingDataRange.setMinCoordY(minOfyList.doubleValue());
        rollingDataRange.setMaxCoordX(maxOfxList.doubleValue());
        rollingDataRange.setMinCoordX(minOfxList.doubleValue());
        int xSize = (int) Math.abs(damsConstruction.getXend() - damsConstruction.getXbegin());
        int ySize = (int) Math.abs(damsConstruction.getYend() - damsConstruction.getYbegin());
        //多用户访问 内存是否含有本工作仓的信息
        //所有车
        List<Car> carList = carMapper.findCar();
        //判断工作仓数据是否存在
        boolean isHave = StoreHouseMap.getStoreHouses2RollingData().containsKey(tableName);
        if (!isHave) {//不存在则进行
            shorehouseRange.put(id, storehouseRange);
            //storehouse.setState(1);
            MatrixItem[][] matrix = new MatrixItem[xSize][ySize];
            for (int i = 0; i < xSize; i++) {
                for (int j = 0; j < ySize; j++) {
                    matrix[i][j] = new MatrixItem();
                    matrix[i][j].setRollingTimes(0);
                }
            }
            StoreHouseMap.getStoreHouses2RollingData().put(tableName, matrix);

            //2 获得数据库中的数据
            long start = System.currentTimeMillis();
            for (Car car : carList) {
                List<RollingData> rollingDataList = rollingDataMapper.getAllRollingDataByVehicleID(tableName, car.getCarID().toString());
                // 总数据条数
                int dataSize = rollingDataList.size();
                if (dataSize < 10) {
                    continue;
                }
                if (dataSize == 0) {
                    continue;
                }
                // 线程数 四个核心
                int threadNum = CORECOUNT;
                // 余数
                //   int special = dataSize % threadNum;
                //每个线程处理的数据量
                int dataSizeEveryThread = dataSize / threadNum;
                // 创建一个线程池
                ExecutorService exec = Executors.newFixedThreadPool(threadNum);
                // 定义一个任务集合
                List<Callable<Integer>> tasks = new LinkedList<Callable<Integer>>();
                Callable<Integer> task = null;
                List<RollingData> cutList = null;
                // 确定每条线程的数据
                for (int i = 0; i < threadNum; i++) {
                    if (i == threadNum - 1) {
                        int index = dataSizeEveryThread * i - 1 < 0 ? 0 : dataSizeEveryThread * i - 1;
                        cutList = rollingDataList.subList(index, dataSize);
                    } else {
                        if (i == 0) {
                            cutList = rollingDataList.subList(dataSizeEveryThread * i, dataSizeEveryThread * (i + 1));
                        } else {
                            cutList = rollingDataList.subList(dataSizeEveryThread * i - 1, dataSizeEveryThread * (i + 1));
                        }
                    }
                    final List<RollingData> listRollingData = cutList;
                    task = new CalculateGridForZhuang();
                    ((CalculateGridForZhuang) task).setRollingDataRange(rollingDataRange);
                    ((CalculateGridForZhuang) task).setRollingDataList(listRollingData);
                    ((CalculateGridForZhuang) task).setTableName(tableName);
                    ((CalculateGridForZhuang) task).setxNum(xSize);
                    ((CalculateGridForZhuang) task).setyNum(ySize);
                    // 这里提交的任务容器列表和返回的Future列表存在顺序对应的关系
                    tasks.add(task);
                }
                List<Future<Integer>> results = exec.invokeAll(tasks);
                for (Future<Integer> future : results) {
                    future.get().toString();
                }
                // 关闭线程池
                exec.shutdown();
                log.info("线程任务执行结束");
            }
            log.error("生成网格遍数执行任务消耗了 ：" + (System.currentTimeMillis() - start) + "毫秒");
        }
        RollingResult rollingResult = new RollingResult();
        int count0 = 0;
        String bsae64_string = "";
        synchronized (obj1) {// 同步代码块
            //绘制图片
            //得到图片缓冲区
            BufferedImage bi = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);//INT精确度达到一定,RGB三原色，高度70,宽度150
            //得到它的绘制环境(这张图片的笔)
            Graphics2D g2 = (Graphics2D) bi.getGraphics();
            //记录遍数
            List<Coordinate> list = JSONArray.parseArray(damsConstruction.getRanges(), Coordinate.class);
            Coordinate[] pointList = new Coordinate[list.size() + 1];  //最重要，不能遗漏，预先申请好数组空间
            list.toArray(pointList);
            pointList[list.size()] = pointList[0];
            GeometryFactory gf = new GeometryFactory();
            Geometry edgePoly = gf.createPolygon(pointList);
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < xSize - 1; i++) {
                for (int j = 0; j < ySize - 1; j++) {
                    //计算边界的网格数量
                    Double xTmp = rollingDataRange.getMinCoordX() + i * division;
                    Double yTmp = rollingDataRange.getMinCoordY() + j * division;
                    //判断点是否在多边形内
                    Coordinate point = new Coordinate(xTmp, yTmp);
                    PointLocator a = new PointLocator();
                    boolean p1 = a.intersects(point, edgePoly);
                    if (p1) {
                        count0++;
                        Integer rollingTimes = StoreHouseMap.getStoreHouses2RollingData().get(tableName)[i][j].getRollingTimes();
                        g2.setColor(getColorByCount2(rollingTimes));
                        calculateRollingtimes(rollingTimes, rollingResult);
                        g2.fillRect(i, j, 1, 1);
                    }
                }
            }
            rollingResult.setTime0(count0 - rollingResult.getTime1() - rollingResult.getTime2() - rollingResult.getTime3() - rollingResult.getTime4() - rollingResult.getTime5() - rollingResult.getTime6() - rollingResult.getTime7() - rollingResult.getTime8()
                    - rollingResult.getTime9() - rollingResult.getTime10() - rollingResult.getTime11() - rollingResult.getTime11Up());
            long endTime = System.currentTimeMillis();    //获取结束时间
            log.info("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ImageIO.write(bi, "PNG", baos);
                byte[] bytes = baos.toByteArray();//转换成字节
                bsae64_string = "data:image/png;base64," + Base64.encodeBase64String(bytes);
                baos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        ProjCoordinate projCoordinate1 = new ProjCoordinate(rollingDataRange.getMinCoordY(), rollingDataRange.getMinCoordX(), 10);
        ProjCoordinate projCoordinate2 = new ProjCoordinate(rollingDataRange.getMaxCoordY(), rollingDataRange.getMaxCoordX(), 10);
        JSONObject result = new JSONObject();
        Set<String> threadList = new HashSet<>();
        //将rollingResult放入到全局变量中 key为时间戳
        rollingResultMap.put(rollingResultKey, rollingResult);
        if (!isHave) {
            threadMap.put(id, new ArrayList());
            for (int i = 0; i < carList.size(); i++) {
                RealTimeRedisRightPopTaskBlockingZhuang realTimeRedisRightPopTaskBlocking = new RealTimeRedisRightPopTaskBlockingZhuang();
                realTimeRedisRightPopTaskBlocking.setRollingResultKey(rollingResultKey);
                realTimeRedisRightPopTaskBlocking.setKey(id);
                realTimeRedisRightPopTaskBlocking.setCarId(carList.get(i).getCarID().toString());
                Double gaocheng = 0d;
                /*if(damsConstruction.getGaocheng()==null){
                    gaocheng = Double.valueOf(damsConstruction.getHeightIndex());
                }else{
                    gaocheng = damsConstruction.getGaocheng();
                 }*/
                realTimeRedisRightPopTaskBlocking.setHeight(gaocheng);
                realTimeRedisRightPopTaskBlocking.setStorehouseId(tablename);
                Thread thread1 = new Thread(realTimeRedisRightPopTaskBlocking);
                threadList.add(thread1.getName());
                threadStorehouse.put(thread1.getName(), id);
                thread1.start();
                Set set = new HashSet();
                set.add(timestamp);
                threadTimestamp.put(thread1.getName(), set);
                threadMap.get(id).add(realTimeRedisRightPopTaskBlocking);
            }
            storehouseThreadList.put(id, threadList);
            globalThreadMap.put(timestamp, threadList);
        }
        if (isHave) {
            Set<String> set = storehouseThreadList.get(id);
            for (String threadName : set) {
                threadTimestamp.get(threadName).add(timestamp);
            }
            threadList = set;
        }
        result.put("tablename", id);
        result.put("base64", bsae64_string);
        result.put("rollingDataRange", rollingDataRange);
        result.put("pointLeftBottom", projCoordinate1);
        result.put("pointRightTop", projCoordinate2);
        result.put("height", damsConstruction.getGaocheng());
        result.put("threadList", threadList);
        result.put("carList", carList);
        result.put("range", range);
        //边界
        result.put("ranges", damsConstruction.getRanges());
        /*if(StringUtils.isNotNull(damsConstruction.getRanges())) {
            List<Coordinate> list = JSONArray.parseArray(damsConstruction.getRanges(), Coordinate.class);
        }*/
        return result;
    }

    /**
     * 车辆id
     *
     * @param id
     * @param userName
     * @param tablename
     * @param timestamp
     * @param carId
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public JSONObject realTimeZhuang(String id, String userName, String tablename, String timestamp, String carId) throws InterruptedException, ExecutionException {
        //获得时间戳
        //   String rollingResultKey = timestamp;
        //0.获得工作仓的范围
        DamsConstruction damsConstruction = damsConstructionMapper.selectByPrimaryKey(Integer.valueOf(id));
        //工作仓 数据
        String tableName = damsConstruction.getTablename();
        String range = tableName.split("_")[0] + "_" + tableName.split("_")[1];
        //1.根据工作仓初设化网格
        //直接根据桩号获得范围
        BigDecimal minOfxList = BigDecimal.valueOf(damsConstruction.getXbegin());
        BigDecimal maxOfxList = BigDecimal.valueOf(damsConstruction.getXend());
        BigDecimal minOfyList = BigDecimal.valueOf(damsConstruction.getYbegin());
        BigDecimal maxOfyList = BigDecimal.valueOf(damsConstruction.getYend());
        StorehouseRange storehouseRange = new StorehouseRange();
        storehouseRange.setMinOfxList(minOfxList.doubleValue());
        storehouseRange.setMaxOfxList(maxOfxList.doubleValue());
        storehouseRange.setMinOfyList(minOfyList.doubleValue());
        storehouseRange.setMaxOfyList(maxOfyList.doubleValue());
        RollingDataRange rollingDataRange = new RollingDataRange();
        rollingDataRange.setMaxCoordY(maxOfyList.doubleValue());
        rollingDataRange.setMinCoordY(minOfyList.doubleValue());
        rollingDataRange.setMaxCoordX(maxOfxList.doubleValue());
        rollingDataRange.setMinCoordX(minOfxList.doubleValue());
        //多用户访问 内存是否含有本工作仓的信息
        //所有车
        List<Car> carList = carMapper.findCar();

        boolean isHave = storeHouseMap.getRealTimeStorehouseDataMap().containsKey(tableName);
        if (!isHave) {//不存在则进行

            ConcurrentHashMap<Long, MatrixItem[][]> cache = new ConcurrentHashMap<Long, MatrixItem[][]>();
            storeHouseMap.getRealTimeStorehouseDataMap().put(tableName, cache);

            //ConcurrentHashMap<Long, MatrixItem[][]> cache = storeHouseMap.getRealTimeStorehouseDataItem(tableName);

            //2 获得数据库中的数据
            long start = System.currentTimeMillis();
            for (Car car : carList) {
                List<RollingData> rollingDataList = rollingDataMapper.getAllRollingDataByVehicleID(tableName, car.getCarID().toString());

                // 总数据条数
                int dataSize = rollingDataList.size();
                if (dataSize < 10) {
                    continue;
                }
                if (dataSize == 0) {
                    continue;
                }
                // 线程数 四个核心
                int threadNum = CORECOUNT;
                // 余数
                int special = dataSize % threadNum;
                //每个线程处理的数据量
                int dataSizeEveryThread = dataSize / threadNum;
                // 创建一个线程池
                ExecutorService exec = Executors.newFixedThreadPool(threadNum);
                // 定义一个任务集合
                List<Callable<Integer>> tasks = new LinkedList<Callable<Integer>>();
                Callable<Integer> task = null;
                List<RollingData> cutList = null;
                // 确定每条线程的数据
                for (int i = 0; i < threadNum; i++) {
                    if (i == threadNum - 1) {
                        int index = dataSizeEveryThread * i - 1 < 0 ? 0 : dataSizeEveryThread * i - 1;
                        cutList = rollingDataList.subList(index, dataSize);
                    } else {
                        if (i == 0) {
                            cutList = rollingDataList.subList(dataSizeEveryThread * i, dataSizeEveryThread * (i + 1));
                        } else {
                            cutList = rollingDataList.subList(dataSizeEveryThread * i - 1, dataSizeEveryThread * (i + 1));
                        }
                    }
                    final List<RollingData> listRollingData = cutList;
                    task = new RealTimeTaskResult(listRollingData, cache);
                    // 这里提交的任务容器列表和返回的Future列表存在顺序对应的关系
                    tasks.add(task);
                }
                List<Future<Integer>> results = exec.invokeAll(tasks);
                try {
                    for (Future<Integer> future : results) {
                        future.get().toString();
                    }
                    // 关闭线程池
                } catch (Exception e) {
                    e.printStackTrace();
                }
                exec.shutdown();
                log.info("线程任务执行结束");
            }
            log.error("生成网格遍数执行任务消耗了 ：" + (System.currentTimeMillis() - start) + "毫秒");

        }
        RollingResult rollingResult = new RollingResult();
        int count0 = 0;
        String bsae64_string = "";
        synchronized (obj1) {// 同步代码块
            //绘制图片
            //记录遍数
            List<Coordinate> list = JSONArray.parseArray(damsConstruction.getRanges(), Coordinate.class);
            Coordinate[] pointList = new Coordinate[list.size() + 1];  //最重要，不能遗漏，预先申请好数组空间
            list.toArray(pointList);
            pointList[list.size()] = pointList[0];
            // GeometryFactory gf = new GeometryFactory();
            //  Geometry edgePoly = gf.createPolygon(pointList);
            long startTime = System.currentTimeMillis();
            ConcurrentHashMap<Long, MatrixItem[][]> cache = storeHouseMap.getRealTimeStorehouseDataMap().get(tableName);

            List<Integer> long2Cols = new LinkedList<>();//列
            List<Integer> long2Rows = new LinkedList<>();//行

            for (Map.Entry<Long, MatrixItem[][]> entry : cache.entrySet()) {
                long rid = entry.getKey();
                long2Cols.add(RidUtil.long2Col(rid));
                long2Rows.add(RidUtil.long2Row(rid));
            }
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
            for (Map.Entry<Long, MatrixItem[][]> entry : cache.entrySet()) {
                // 10*10 小方格
                long rid = entry.getKey();
                MatrixItem[][] matrixItems = entry.getValue();
                int dltaX = RidUtil.long2Col(rid) * RidUtil.R_LEN - baseX;
                int dltaY = RidUtil.long2Row(rid) * RidUtil.R_LEN - baseY;
                for (int i = 0; i < RidUtil.R_LEN - 1; i++) {
                    for (int j = 0; j < RidUtil.R_LEN - 1; j++) {
                        MatrixItem item = matrixItems[i][j];
                        if (item != null) {
                            int rollingTimes = item.getRollingTimes();
                            //  g2.setColor(getColorByCount2(rollingTimes));
                            // g2.fillRect(i+dltaX, j+dltaY, 2, 2);
                        }
                    }
                }
            }
            rollingResult.setTime0(count0 - rollingResult.getTime1() - rollingResult.getTime2() - rollingResult.getTime3() - rollingResult.getTime4() - rollingResult.getTime5() - rollingResult.getTime6() - rollingResult.getTime7() - rollingResult.getTime8()
                    - rollingResult.getTime9() - rollingResult.getTime10() - rollingResult.getTime11() - rollingResult.getTime11Up());
            long endTime = System.currentTimeMillis();    //获取结束时间
            log.info("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ImageIO.write(bi, "PNG", baos);
                byte[] bytes = baos.toByteArray();//转换成字节
                bsae64_string = "data:image/png;base64," + Base64.encodeBase64String(bytes);
                baos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        ProjCoordinate projCoordinate1 = new ProjCoordinate(rollingDataRange.getMinCoordY(), rollingDataRange.getMinCoordX(), 10);
        ProjCoordinate projCoordinate2 = new ProjCoordinate(rollingDataRange.getMaxCoordY(), rollingDataRange.getMaxCoordX(), 10);
        JSONObject result = new JSONObject();

        result.put("tablename", id);
        result.put("base64", bsae64_string);
        result.put("rollingDataRange", rollingDataRange);
        result.put("pointLeftBottom", projCoordinate1);
        result.put("pointRightTop", projCoordinate2);
        result.put("height", damsConstruction.getGaocheng());
//        result.put("threadList",threadList);
        result.put("carList", carList);
        result.put("range", range);
        //边界
        result.put("ranges", damsConstruction.getRanges());
        /*if(StringUtils.isNotNull(damsConstruction.getRanges())) {
            List<Coordinate> list = JSONArray.parseArray(damsConstruction.getRanges(), Coordinate.class);
        }*/
        return result;
    }

    public JSONObject realTimeThread(String id, String userName, String tablename, String timestamp, String carId) {
        Set<String> storehouseThread = storehouseThreadList.get(id);
        Set<String> globalThread = globalThreadMap.get(timestamp);

        RealTimeRedisRightPopTaskBlockingZhuang realTimeRedisRightPopTaskBlocking = new RealTimeRedisRightPopTaskBlockingZhuang();
        realTimeRedisRightPopTaskBlocking.setRollingResultKey(timestamp);
        realTimeRedisRightPopTaskBlocking.setKey(id);
        realTimeRedisRightPopTaskBlocking.setCarId(carId);
        Double gaocheng = 0d;
        realTimeRedisRightPopTaskBlocking.setHeight(gaocheng);
        realTimeRedisRightPopTaskBlocking.setStorehouseId(tablename);
        Thread thread1 = new Thread(realTimeRedisRightPopTaskBlocking);
        storehouseThread.add(thread1.getName());
        globalThread.add(thread1.getName());
        threadStorehouse.put(thread1.getName(), id);
        thread1.start();
        Set set = new HashSet();
        set.add(timestamp);
        threadTimestamp.put(thread1.getName(), set);
        threadMap.get(id).add(realTimeRedisRightPopTaskBlocking);
        storehouseThreadList.put(id, storehouseThread);
        globalThreadMap.put(timestamp, globalThread);

        JSONObject result = new JSONObject();
        result.put("tablename", id);
        return result;
    }


    /*根据横纵坐标判断点所属的仓位 返回对应的仓位名称*/
    public String getBelongStorehouseRange(double longitude, double latitude) {
        for (String key : shorehouseRange.keySet()) {
            if (latitude > shorehouseRange.get(key).getMinOfxList() && latitude < shorehouseRange.get(key).getMaxOfxList()
                    && longitude > shorehouseRange.get(key).getMinOfyList() && longitude < shorehouseRange.get(key).getMaxOfyList()) {
                return key;
            }
        }
        return null;
    }

    public MatrixItem getMatrixItemBylonLat(double longitude, double latitude) {
        /*
         *遍历shorehouseRange 根据longitude，latitude判断所属的区域 把这个区域对应的matrix取出来
         * */
        String storehouesId = getBelongStorehouseRange(longitude, latitude);
        if (storehouesId == null) {
            return new MatrixItem();
        }
        MatrixItem[][] matrix = StoreHouseMap.getStoreHouses2RollingData().get(storehouesId);
        StorehouseRange range = shorehouseRange.get(storehouesId);
        int yIndex = (int) Math.ceil((latitude - range.getMinOfxList()) / division);
        int xIndex = (int) Math.ceil((longitude - range.getMinOfyList()) / division);

        MatrixItem matrixItem = matrix[xIndex][yIndex];
        return matrixItem;
    }

    public String drawPic(String storehouesId) {
        MatrixItem[][] matrix = StoreHouseMap.getStoreHouses2RollingData().get(storehouesId);
        //width713 height984
        BufferedImage bi = new BufferedImage(matrix.length, matrix[0].length, BufferedImage.TYPE_INT_ARGB);//INT精确度达到一定,RGB三原色，高度70,宽度150
        //得到它的绘制环境(这张图片的笔)
        Graphics2D g2 = (Graphics2D) bi.getGraphics();
        long startTime = System.currentTimeMillis();    //获取开始时间
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                g2.setColor(getColorByCount2(matrix[i][j].getRollingTimes()));
                g2.fillRect(i, j, 1, 1);
            }
        }
        long endTime = System.currentTimeMillis();    //获取结束时间
        log.info("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bi, "PNG", baos);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] bytes = baos.toByteArray();//转换成字节
        String base64_string = "data:image/png;base64," + Base64.encodeBase64String(bytes);
        return base64_string;
    }

    public String deleteStorehouseMatrix(String storehouseStr) {
        StoreHouseMap.getStoreHouses2RollingData().remove(storehouseStr);
        return "ok";
    }

    public String killThread(String threadName, String tableName) {
        //在这里清楚map
        //应该判断有几个连接 如果连接>1 就不应该清除
        Set<String> userKyes = users.keySet();
        Set<String> timeSet = new HashSet<>();
        /* 遍历set 去session 获得时间戳*/
        for (String str : userKyes) {
            String time = str.split(":")[3];
            timeSet.add(time);
        }
        if (timeSet.size() == 1) {
            Iterator<String> iter = StoreHouseMap.getStoreHouses2RollingData().keySet().iterator();
            while (iter.hasNext()) {
                String str = iter.next();
                DamsConstruction damsConstruction = damsConstructionMapper.selectByTablename(str);
                String id = String.valueOf(damsConstruction.getId());
                if (id.equals(tableName)) {
                    iter.remove();
                }
            }
            boolean b = killThreadByName(threadName);
            if (b) {
                return "ok";
            }
            return "false";
        }
        return "还有其他的用户在连接，不清楚线程";
    }

    public String killThreadByThreadName(String timestamp, String threadName) {
        String id = threadStorehouse.get(threadName);
        Set<String> set = threadTimestamp.get(threadName);
        if (set.size() == 1) {
            for (String str : set) {
                if (str.equalsIgnoreCase(timestamp)) {
                    boolean b = killThreadByName(threadName);

                    if (b) {
                        DamsConstruction damsConstruction = damsConstructionMapper.selectByPrimaryKey(Integer.valueOf(id));
                        String tablename = String.valueOf(damsConstruction.getTablename());
                        if (StoreHouseMap.getStoreHouses2RollingData().containsKey(tablename)) {
                            StoreHouseMap.getStoreHouses2RollingData().remove(tablename);
                        }
                        threadStorehouse.remove(threadName);
                        threadTimestamp.remove(threadName);
                        return "没有其他用户，删除了";
                    }
                }
            }
        } else {
            set.remove(timestamp);
            threadTimestamp.put(threadName, set);
        }
        return "还有其他用户，不删除";
    }

    public List getAllThread() {
        ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();
        int noThreads = currentGroup.activeCount();
        Thread[] lstThreads = new Thread[noThreads];
        currentGroup.enumerate(lstThreads);
        List<String> nameList = new LinkedList<>();
        for (int i = 0; i < noThreads; i++) {
            String nm = lstThreads[i].getName();
            nameList.add(nm);
        }
        return nameList;
    }

    public boolean killThreadByName(String name) {
        ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();
        int noThreads = currentGroup.activeCount();
        Thread[] lstThreads = new Thread[noThreads];
        currentGroup.enumerate(lstThreads);
        for (int i = 0; i < noThreads; i++) {
            String nm = lstThreads[i].getName();
            if (nm.equals(name)) {
                lstThreads[i].interrupt();
                return true;
            }
        }
        return false;
    }


    public static Color getColorByCount2(Integer count) {
        if (count.equals(0)) {
            return new Color(255, 255, 255, 0);
        }
        if (count.equals(1)) {
            return new Color(225, 148, 207);
        }
        if (count.equals(2)) {
            return new Color(245, 102, 102);
        }
        if (count.equals(3)) {
            return new Color(255, 0, 0);
        }
        if (count.equals(4)) {
            return new Color(185, 40, 71);
        }
        if (count.equals(5)) {
            return new Color(255, 0, 243);
        }
        if (count.equals(6)) {
            return new Color(72, 238, 217);
        }
        if (count.equals(7)) {
            return new Color(71, 195, 238);
        }
        if (count.equals(8)) {
            return new Color(133, 244, 133);
        }
        if (count.equals(9)) {
            return new Color(133, 244, 133);
        }
        if (count.equals(10)) {
            return new Color(133, 244, 133);
        }
        if (count.equals(11)) {
            return new Color(133, 244, 133);
        }
        if (count > 11) {
            return new Color(133, 244, 133);
        }
        return null;

    }

    public Color getColorByCount2(Integer count, Map<Integer, Color> colorMap) {
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

    public Color getColorByCountSpeed(Float count, List<TColorConfig> colorConfigs) {
        for (TColorConfig color : colorConfigs) {
            //判断count 在哪个从和到之间
            if (count >= color.getC().floatValue() && count < color.getD().floatValue()) {
                int[] rgb = RGBHexUtil.hex2RGB(color.getColor());
                return new Color(rgb[0], rgb[1], rgb[2]);
            }
        }
        //匹配不到的情况下
        return new Color(255, 255, 255, 0);
    }

    private void calculateRollingSpeed(float speed, RollingResult rollingResult, List<TColorConfig> colorConfigs) {
        for (TColorConfig color : colorConfigs) {
            //判断count 在哪个从和到之间
            if (speed >= color.getC().floatValue() && speed < color.getD().floatValue()) {
                if (color.getLevel().intValue() == 1) {
                    rollingResult.setTime0(rollingResult.getTime0() + 1);
                } else if (color.getLevel().intValue() == 2) {
                    rollingResult.setTime1(rollingResult.getTime1() + 1);
                }
            }
        }
    }

    public Color getColorByCountVibrate(Double count, List<TColorConfig> colorConfigs) {
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

    public Color getColorByCountEvolution(Float count, List<TColorConfig> colorConfigs) {
        for (TColorConfig color : colorConfigs) {
            //判断count 在哪个从和到之间
            if (count >= color.getC().doubleValue() && count <= color.getD().doubleValue()) {
                int[] rgb = RGBHexUtil.hex2RGB(color.getColor());
                return new Color(rgb[0], rgb[1], rgb[2]);
            }
        }
        //匹配不到的情况下
        int[] rgb = RGBHexUtil.hex2RGB(colorConfigs.get(0).getColor());
        return new Color(rgb[0], rgb[1], rgb[2]);
        // return new Color(255, 255, 255, 0);
    }


    private void calculateRollingVibrate(Double vibrate, RollingResult rollingResult, List<TColorConfig> colorConfigs) {
        for (TColorConfig color : colorConfigs) {
            //判断count 在哪个从和到之间
            if (vibrate >= color.getC().doubleValue() && vibrate <= color.getD().doubleValue()) {
                if (color.getLevel().intValue() == 1) {
                    rollingResult.setTime0(rollingResult.getTime0() + 1);
                } else if (color.getLevel().intValue() == 2) {
                    rollingResult.setTime1(rollingResult.getTime1() + 1);
                }
            }
        }
    }

    private void calculateRollingEvolution(float vibrate, RollingResult rollingResult, List<TColorConfig> colorConfigs) {
        for (TColorConfig color : colorConfigs) {
            //判断count 在哪个从和到之间
            if (vibrate >= color.getC().doubleValue() && vibrate <= color.getD().doubleValue()) {
                if (color.getLevel().intValue() == 1) {
                    rollingResult.setTime0(rollingResult.getTime0() + 1);
                } else if (color.getLevel().intValue() == 2) {
                    rollingResult.setTime1(rollingResult.getTime1() + 1);
                } else if (color.getLevel().intValue() == 3) {
                    rollingResult.setTime1(rollingResult.getTime1() + 1);
                }
            }
        }
    }

    @Autowired
    TColorConfigMapper colorConfigMapper;

    public Map<Integer, Color> getColorMap(Long type) {
        Map<Integer, Color> colorMap = new HashMap<>();
        TColorConfig vo = new TColorConfig();
        vo.setType(type);//碾压遍次
        List<TColorConfig> colorConfigs = colorConfigMapper.select(vo);
        for (TColorConfig color : colorConfigs) {
            if (color.getNum().intValue() == 0) {
                colorMap.put(0, new Color(255, 255, 255, 0));
            } else {
                int[] rgb = RGBHexUtil.hex2RGB(color.getColor());
                colorMap.put(Integer.valueOf(String.valueOf(color.getNum())), new Color(rgb[0], rgb[1], rgb[2]));
            }
        }
        return colorMap;
    }

    public JSONObject getHistoryPicByElvacationAndRange(String range, String bottom, String top, String mat) throws InterruptedException, ExecutionException {
        String id = range;
        DamsConstruction damsConstruction = damsConstructionMapper.selectByPrimaryKey(Integer.valueOf(id));
        String tableName = damsConstruction.getTablename();
        RollingDataRange rollingDataRange = new RollingDataRange();
        //从数据库中获得范围
        rollingDataRange.setMaxCoordX(damsConstruction.getXend());
        rollingDataRange.setMinCoordX(damsConstruction.getXbegin());
        rollingDataRange.setMaxCoordY(damsConstruction.getYend());
        rollingDataRange.setMinCoordY(damsConstruction.getYbegin());
        //记录外接矩形的范围
        StorehouseRange storehouseRange = new StorehouseRange();
        storehouseRange.setMinOfxList(rollingDataRange.getMinCoordY());
        storehouseRange.setMaxOfxList(rollingDataRange.getMaxCoordY());
        storehouseRange.setMinOfyList(rollingDataRange.getMinCoordX());
        storehouseRange.setMaxOfyList(rollingDataRange.getMaxCoordX());
        Integer yNum = (int) Math.ceil((rollingDataRange.getMaxCoordY() - rollingDataRange.getMinCoordY()) / division);//行数
        Integer xNum = (int) Math.ceil((rollingDataRange.getMaxCoordX() - rollingDataRange.getMinCoordX()) / division);//列数

        MatrixItem[][] matrix = new MatrixItem[xNum][yNum];
        for (int x = 0; x < xNum; x++) {
            for (int y = 0; y < yNum; y++) {
                matrix[x][y] = new MatrixItem();
                matrix[x][y].setRollingTimes(0);
            }
        }
        shorehouseRange.put(tableName, storehouseRange);
        StoreHouseMap.getStoreHouses2RollingData().put(tableName, matrix);
        List<Car> carList = carMapper.findCar();
        long start = System.currentTimeMillis();
        //固定线程的数量
        for (Car car : carList) {
            List<RollingData> rollingDataList = rollingDataMapper.getAllRollingDataByVehicleIDByElaAndRange(tableName, car.getCarID().toString(), Double.valueOf(bottom), Double.valueOf(top), mat);
            // 总数据条数
            int dataSize = rollingDataList.size();
            if (dataSize < 10) {
                continue;
            }
            if (dataSize == 0) {
                continue;
            }
            // 线程数 四个核心
            int threadNum = CORECOUNT;
            // 余数
            int special = dataSize % threadNum;
            //每个线程处理的数据量
            int dataSizeEveryThread = dataSize / threadNum;
            // 创建一个线程池
            ExecutorService exec = Executors.newFixedThreadPool(threadNum);
            // 定义一个任务集合
            List<Callable<Integer>> tasks = new LinkedList<Callable<Integer>>();
            Callable<Integer> task = null;
            List<RollingData> cutList = null;
            // 确定每条线程的数据
            for (int i = 0; i < threadNum; i++) {
                if (i == threadNum - 1) {
                    int index = dataSizeEveryThread * i - 1 < 0 ? 0 : dataSizeEveryThread * i - 1;
                    cutList = rollingDataList.subList(index, dataSize);
                } else {
                    if (i == 0) {
                        cutList = rollingDataList.subList(dataSizeEveryThread * i, dataSizeEveryThread * (i + 1));
                    } else {
                        cutList = rollingDataList.subList(dataSizeEveryThread * i - 1, dataSizeEveryThread * (i + 1));
                    }
                }
                final List<RollingData> listRollingData = cutList;
                task = new CalculateGridForZhuang();
                ((CalculateGridForZhuang) task).setRollingDataRange(rollingDataRange);
                ((CalculateGridForZhuang) task).setRollingDataList(listRollingData);
                ((CalculateGridForZhuang) task).setTableName(tableName);
                ((CalculateGridForZhuang) task).setxNum(xNum);
                ((CalculateGridForZhuang) task).setyNum(yNum);
                // 这里提交的任务容器列表和返回的Future列表存在顺序对应的关系
                tasks.add(task);
            }
            List<Future<Integer>> results = exec.invokeAll(tasks);
            for (Future<Integer> future : results) {
                future.get().toString();
            }
            // 关闭线程池
            exec.shutdown();
            log.info("线程任务执行结束");
        }
        log.error("生成网格遍数执行任务消耗了 ：" + (System.currentTimeMillis() - start) + "毫秒");
        //绘制图片
        //得到图片缓冲区
        /*碾压轨迹遍数的绘制*/
        BufferedImage bi = new BufferedImage(xNum, yNum, BufferedImage.TYPE_INT_ARGB);//TYPE_INT_ARGB 为透明的 INT精确度达到一定,RGB三原色，高度70,宽度150
        //得到它的绘制环境(这张图片的笔)
        Graphics2D g2 = (Graphics2D) bi.getGraphics();
        RollingResult rollingResult = new RollingResult();
        List<Coordinate> list = JSONArray.parseArray(damsConstruction.getRanges(), Coordinate.class);
        Coordinate[] pointList = new Coordinate[list.size() + 1];  //最重要，不能遗漏，预先申请好数组空间
        list.toArray(pointList);
        pointList[list.size()] = pointList[0];
        GeometryFactory gf = new GeometryFactory();
        Geometry edgePoly = gf.createPolygon(pointList);

        long drawstart = System.currentTimeMillis();
        int count0 = 0;
        for (int i = 0; i < xNum - 1; i++) {
            for (int j = 0; j < yNum - 1; j++) {
                //计算边界的网格数量
                Double xTmp = rollingDataRange.getMinCoordX() + i * division;
                Double yTmp = rollingDataRange.getMinCoordY() + j * division;
                //判断点是否在多边形内
                Coordinate point = new Coordinate(xTmp, yTmp);
                PointLocator a = new PointLocator();
                boolean p1 = a.intersects(point, edgePoly);
                if (p1) {
                    count0++;
                    Integer rollingTimes = StoreHouseMap.getStoreHouses2RollingData().get(tableName)[i][j].getRollingTimes();
                    calculateRollingtimes(rollingTimes, rollingResult);
                    g2.setColor(getColorByCount2(rollingTimes));
                    g2.fillRect(i, j, 1, 1);
                }
            }
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bi, "PNG", baos);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] bytes = baos.toByteArray();//转换成字节
        String bsae64_string = "data:image/png;base64," + Base64.encodeBase64String(bytes);
        log.error("绘图执行任务消耗了 ：" + (System.currentTimeMillis() - drawstart) + "毫秒");
        //无经纬度概念 无需坐标转换
        ProjCoordinate projCoordinate1 = new ProjCoordinate(rollingDataRange.getMinCoordY(), rollingDataRange.getMinCoordX(), Double.valueOf(bottom));
        ProjCoordinate projCoordinate2 = new ProjCoordinate(rollingDataRange.getMaxCoordY(), rollingDataRange.getMaxCoordX(), Double.valueOf(bottom));
        JSONObject result = new JSONObject();
        rollingResult.setTime0(count0 - rollingResult.getTime1() - rollingResult.getTime2() - rollingResult.getTime3() - rollingResult.getTime4() - rollingResult.getTime5() - rollingResult.getTime6() - rollingResult.getTime7() - rollingResult.getTime8()
                - rollingResult.getTime9() - rollingResult.getTime10() - rollingResult.getTime11() - rollingResult.getTime11Up());
        result.put("rollingResult", rollingResult);
        result.put("base64", bsae64_string);
        result.put("rollingDataRange", rollingDataRange);
        result.put("pointLeftBottom", projCoordinate1);
        result.put("pointRightTop", projCoordinate2);
        result.put("range", range);
        //边界
        result.put("ranges", damsConstruction.getRanges());
        /*if(StringUtils.isNotNull(damsConstruction.getRanges())) {
            List<Coordinate> list = JSONArray.parseArray(damsConstruction.getRanges(), Coordinate.class);
        }*/
        shorehouseRange.remove(tableName);
        StoreHouseMap.getStoreHouses2RollingData().remove(tableName);
        return result;
    }

    public JSONObject realTimeByZhuangByElaAndRange(String range, String bottom, String top, String timestamp, String mat) throws InterruptedException, ExecutionException {
        //获得时间戳
        String rollingResultKey = timestamp;
        DamsConstruction damsConstruction = damsConstructionMapper.selectByPrimaryKey(Integer.valueOf(range));
        String id = String.valueOf(damsConstruction.getId());
        String tableName0 = damsConstruction.getTablename();
        String tableName = id + "-" + bottom + "-" + top + "-" + mat;
        //1.根据工作仓初设化网格
        //直接根据桩号获得范围
        BigDecimal minOfxList = BigDecimal.valueOf(damsConstruction.getXbegin());
        BigDecimal maxOfxList = BigDecimal.valueOf(damsConstruction.getXend());
        BigDecimal minOfyList = BigDecimal.valueOf(damsConstruction.getYbegin());
        BigDecimal maxOfyList = BigDecimal.valueOf(damsConstruction.getYend());
        StorehouseRange storehouseRange = new StorehouseRange();
        storehouseRange.setMinOfxList(minOfxList.doubleValue());
        storehouseRange.setMaxOfxList(maxOfxList.doubleValue());
        storehouseRange.setMinOfyList(minOfyList.doubleValue());
        storehouseRange.setMaxOfyList(maxOfyList.doubleValue());
        RollingDataRange rollingDataRange = new RollingDataRange();
        rollingDataRange.setMaxCoordY(maxOfyList.doubleValue());
        rollingDataRange.setMinCoordY(minOfyList.doubleValue());
        rollingDataRange.setMaxCoordX(maxOfxList.doubleValue());
        rollingDataRange.setMinCoordX(minOfxList.doubleValue());
        int xSize = (maxOfxList.subtract(minOfxList)).intValue();
        int ySize = (maxOfyList.subtract(minOfyList)).intValue();
        //多用户访问 内存是否含有本工作仓的信息
        List<Car> carList = carMapper.findCar();
        boolean isHave = StoreHouseMap.getStoreHouses2RollingData().containsKey(tableName);
        if (!isHave) {
            shorehouseRange.put(tableName, storehouseRange);
            MatrixItem[][] matrix = new MatrixItem[xSize][ySize];
            for (int x = 0; x < xSize; x++) {
                for (int y = 0; y < ySize; y++) {
                    matrix[x][y] = new MatrixItem();
                    matrix[x][y].setRollingTimes(0);
                }
            }
            StoreHouseMap.getStoreHouses2RollingData().put(tableName, matrix);
            //2 获得数据库中的数据

            long start = System.currentTimeMillis();
            for (Car car : carList) {
                List<RollingData> rollingDataList = rollingDataMapper.getAllRollingDataByVehicleIDByElaAndRange(tableName0, car.getCarID().toString(), Double.valueOf(bottom), Double.valueOf(top), mat);
                // 总数据条数
                int dataSize = rollingDataList.size();
                if (dataSize < 10) {
                    continue;
                }
                if (dataSize == 0) {
                    continue;
                }
                // 线程数 四个核心
                int threadNum = CORECOUNT;
                // 余数
                int special = dataSize % threadNum;
                //每个线程处理的数据量
                int dataSizeEveryThread = dataSize / threadNum;
                // 创建一个线程池
                ExecutorService exec = Executors.newFixedThreadPool(threadNum);
                // 定义一个任务集合
                List<Callable<Integer>> tasks = new LinkedList<Callable<Integer>>();
                Callable<Integer> task = null;
                List<RollingData> cutList = null;
                // 确定每条线程的数据
                for (int i = 0; i < threadNum; i++) {
                    if (i == threadNum - 1) {
                        int index = dataSizeEveryThread * i - 1 < 0 ? 0 : dataSizeEveryThread * i - 1;
                        cutList = rollingDataList.subList(index, dataSize);
                    } else {
                        if (i == 0) {
                            cutList = rollingDataList.subList(dataSizeEveryThread * i, dataSizeEveryThread * (i + 1));
                        } else {
                            cutList = rollingDataList.subList(dataSizeEveryThread * i - 1, dataSizeEveryThread * (i + 1));
                        }

                    }
                    final List<RollingData> listRollingData = cutList;
                    task = new CalculateGridForZhuang();
                    ((CalculateGridForZhuang) task).setRollingDataRange(rollingDataRange);
                    ((CalculateGridForZhuang) task).setRollingDataList(listRollingData);
                    ((CalculateGridForZhuang) task).setTableName(tableName);
                    ((CalculateGridForZhuang) task).setxNum(xSize);
                    ((CalculateGridForZhuang) task).setyNum(ySize);
                    // 这里提交的任务容器列表和返回的Future列表存在顺序对应的关系
                    tasks.add(task);
                }
                List<Future<Integer>> results = exec.invokeAll(tasks);
                for (Future<Integer> future : results) {
                    future.get().toString();
                }
                // 关闭线程池
                exec.shutdown();
                log.info("线程任务执行结束");
            }
            log.error("生成网格遍数执行任务消耗了 ：" + (System.currentTimeMillis() - start) + "毫秒");
        }
        RollingResult rollingResult = new RollingResult();
        int count0 = 0;
        String bsae64_string;
        String bsae64_string_ForVibrate;
        synchronized (obj1) {// 同步代码块
            //绘制图片
            //得到图片缓冲区
            int dd = 5;//图片精度
            BufferedImage bi = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);//INT精确度达到一定,RGB三原色，高度70,宽度150
            //得到它的绘制环境(这张图片的笔)
            Graphics2D g2 = (Graphics2D) bi.getGraphics();
            //记录遍数
            List<Coordinate> list = JSONArray.parseArray(damsConstruction.getRanges(), Coordinate.class);
            Coordinate[] pointList = new Coordinate[list.size() + 1];  //最重要，不能遗漏，预先申请好数组空间
            list.toArray(pointList);
            pointList[list.size()] = pointList[0];
            GeometryFactory gf = new GeometryFactory();
            Geometry edgePoly = gf.createPolygon(pointList);
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < xSize - 1; i++) {
                for (int j = 0; j < ySize - 1; j++) {
                    //计算边界的网格数量
                    Double xTmp = rollingDataRange.getMinCoordX() + i * division;
                    Double yTmp = rollingDataRange.getMinCoordY() + j * division;
                    //判断点是否在多边形内
                    Coordinate point = new Coordinate(xTmp, yTmp);
                    PointLocator a = new PointLocator();
                    boolean p1 = a.intersects(point, edgePoly);
                    if (p1) {
                        count0++;
                        Integer rollingTimes = StoreHouseMap.getStoreHouses2RollingData().get(tableName)[i][j].getRollingTimes();
                        g2.setColor(getColorByCount2(rollingTimes));
                        calculateRollingtimes(rollingTimes, rollingResult);
                        g2.fillRect(i, j, 1, 1);
                    }
                }
            }
            rollingResult.setTime0(count0 - rollingResult.getTime1() - rollingResult.getTime2() - rollingResult.getTime3() - rollingResult.getTime4() - rollingResult.getTime5() - rollingResult.getTime6() - rollingResult.getTime7() - rollingResult.getTime8()
                    - rollingResult.getTime9() - rollingResult.getTime10() - rollingResult.getTime11() - rollingResult.getTime11Up());
            //bi = RotateImage.Rotate(bi, 270);//旋转270
            long endTime = System.currentTimeMillis();    //获取结束时间
            log.info("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //        bi = (BufferedImage) ImgRotate.imageMisro(bi,0);
            try {
                ImageIO.write(bi, "PNG", baos);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            byte[] bytes = baos.toByteArray();//转换成字节
            bsae64_string = "data:image/png;base64," + Base64.encodeBase64String(bytes);
        }

        ProjCoordinate projCoordinate1 = new ProjCoordinate(rollingDataRange.getMinCoordY(), rollingDataRange.getMinCoordX(), 10);
        ProjCoordinate projCoordinate2 = new ProjCoordinate(rollingDataRange.getMaxCoordY(), rollingDataRange.getMaxCoordX(), 10);
        JSONObject result = new JSONObject();
//        List<String> threadList = new LinkedList<>();
        //将rollingResult放入到全局变量中 key为时间戳
        Set<String> threadList = new HashSet<>();
        rollingResultMap.put(rollingResultKey, rollingResult);
        if (!isHave) {
            for (int i = 0; i < carList.size(); i++) {
                RealTimeRedisRightPopTaskBlockingZhuangForHand realTimeRedisRightPopTaskBlocking = new RealTimeRedisRightPopTaskBlockingZhuangForHand();
                realTimeRedisRightPopTaskBlocking.setRollingResultKey(rollingResultKey);
                realTimeRedisRightPopTaskBlocking.setKey(tableName);
                realTimeRedisRightPopTaskBlocking.setCarId(carList.get(i).getCarID().toString());
                realTimeRedisRightPopTaskBlocking.setHeight(Double.parseDouble(bottom));
                realTimeRedisRightPopTaskBlocking.setStorehouseId(tableName);
                Thread thread1 = new Thread(realTimeRedisRightPopTaskBlocking);
                threadList.add(thread1.getName());
                threadStorehouse.put(thread1.getName(), id);
                thread1.start();
                Set set = new HashSet();
                set.add(timestamp);
                threadTimestamp.put(thread1.getName(), set);
            }
            storehouseThreadList.put(id, threadList);
            globalThreadMap.put(timestamp, threadList);
//            globalThread.add(thread1.getName());
        }
        if (isHave) {
            Set<String> set = storehouseThreadList.get(id);
            for (String threadName : set) {
                threadTimestamp.get(threadName).add(timestamp);
            }
            threadList = set;
        }
        result.put("tablename", id);
        result.put("base64", bsae64_string);
        result.put("rollingDataRange", rollingDataRange);
        result.put("pointLeftBottom", projCoordinate1);
        result.put("pointRightTop", projCoordinate2);
        result.put("height", bottom);
        result.put("threadList", threadList);
        result.put("carList", carList);
        result.put("range", range);
        return result;
    }

    /*生成每个工作仓的图片*/
    public void getAllPic() throws ExecutionException, InterruptedException {
        List<DamsConstruction> damsConstructionList = damsConstructionMapper.getAllHasData();
        for (DamsConstruction damsConstruction : damsConstructionList) {
            JSONObject jsonObject = drawPicture(String.valueOf(damsConstruction.getId()));
            BufferedImage bi = (BufferedImage) jsonObject.get("imageBuffer");
            String fileName = damsConstruction.getTablename() + ".png";
            String filePath = "C:\\myProject\\springbootnettyserver\\src\\main\\resources\\static\\img\\" + fileName;
            try {
                ImageIO.write(bi, "PNG", new File(filePath));
                log.info(filePath + "生成");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /*生成每层的图片*/
    public void getPicByLayerMat() throws IOException {
        List<DamsConstruction> damsConstructions = damsConstructionMapper.getAllCeng();
        for (DamsConstruction dams : damsConstructions) {
            int layerId = dams.getHeightIndex();
            int mat = Integer.parseInt(dams.getMaterialname());
            String resultPath = imgPath + layerId + "_" + mat + ".png";
            /*底图*/
            Image bufImage = ImageIO.read(new File(imgPath + "\\bottomPic.png"));
            BufferedImage bi = new BufferedImage(2 * bufImage.getWidth(null), 2 * bufImage.getHeight(null), BufferedImage.TYPE_INT_RGB);//TYPE_INT_ARGB 为透明的 INT精确度达到一定,RGB三原色，高度70,宽度150
            Graphics2D g = (Graphics2D) bi.getGraphics();
            g.drawImage(bufImage, 0, 0, 2 * bufImage.getWidth(null), 2 * bufImage.getHeight(null), null);
            List<DamsConstruction> damsConstructionList = damsConstructionMapper.getAllHasDataByLayerMat(layerId, mat);
            for (DamsConstruction damsConstruction : damsConstructionList) {
                String fileName = damsConstruction.getTablename() + ".png";
                String filePath = imgPath + fileName;
                Image bi2 = ImageIO.read(new File(filePath));
                //得到它的绘制环境(这张图片的笔)
                g.drawImage(bi2, 2 * (int) ((damsConstruction.getXbegin() + 8.410439372) * bufImage.getWidth(null) / 499), 2 * (int) ((damsConstruction.getYbegin() + 20.99353026) * bufImage.getHeight(null) / 804), 2 * (int) (damsConstruction.getXend() - damsConstruction.getXbegin()) * bufImage.getWidth(null) / 499, 2 * (int) (damsConstruction.getYend() - damsConstruction.getYbegin()) * bufImage.getHeight(null) / 804, null);
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ImageIO.write(bi, "PNG", new File(resultPath));
                log.info(resultPath + "生成");
                String resultPathCrop = imgPath + layerId + "_" + mat + "_crop.png";
                ImageUtils.crop(resultPath, resultPathCrop, 700, 1770, 2150, 2450);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /*根据工作仓生成图片*/
    public JSONObject drawPicture(String tableName) throws InterruptedException, ExecutionException {
        String id = tableName;
        DamsConstruction damsConstruction = damsConstructionMapper.selectByPrimaryKey(Integer.valueOf(id));
        tableName = damsConstruction.getTablename();
        String range = tableName.split("_")[0] + "_" + tableName.split("_")[1];
        RollingDataRange rollingDataRange = new RollingDataRange();
        //从数据库中获得范围
        rollingDataRange.setMaxCoordX(damsConstruction.getXend());
        rollingDataRange.setMinCoordX(damsConstruction.getXbegin());
        rollingDataRange.setMaxCoordY(damsConstruction.getYend());
        rollingDataRange.setMinCoordY(damsConstruction.getYbegin());
        //记录外接矩形的范围
        StorehouseRange storehouseRange = new StorehouseRange();
        storehouseRange.setMinOfxList(rollingDataRange.getMinCoordY());
        storehouseRange.setMaxOfxList(rollingDataRange.getMaxCoordY());
        storehouseRange.setMinOfyList(rollingDataRange.getMinCoordX());
        storehouseRange.setMaxOfyList(rollingDataRange.getMaxCoordX());
        Integer yNum = (int) Math.ceil((rollingDataRange.getMaxCoordY() - rollingDataRange.getMinCoordY()) / division);//行数
        Integer xNum = (int) Math.ceil((rollingDataRange.getMaxCoordX() - rollingDataRange.getMinCoordX()) / division);//列数
        MatrixItem[][] matrix = new MatrixItem[xNum][yNum];
        for (int x = 0; x < xNum; x++) {
            for (int y = 0; y < yNum; y++) {
                matrix[x][y] = new MatrixItem();
                matrix[x][y].setRollingTimes(0);
            }
        }
        shorehouseRange.put(tableName, storehouseRange);
        StoreHouseMap.getStoreHouses2RollingData().put(tableName, matrix);
        List<Car> carList = carMapper.findCar();
        long start = System.currentTimeMillis();
        //固定线程的数量
        for (Car car : carList) {
            List<RollingData> rollingDataList = rollingDataMapper.getAllRollingDataByVehicleID(tableName, car.getCarID().toString());
            // 总数据条数
            int dataSize = rollingDataList.size();
            if (dataSize < 10) {
                continue;
            }
            if (dataSize == 0) {
                continue;
            }
            // 线程数 四个核心
            int threadNum = CORECOUNT;
            // 余数
            int special = dataSize % threadNum;
            //每个线程处理的数据量
            int dataSizeEveryThread = dataSize / threadNum;
            // 创建一个线程池
            ExecutorService exec = Executors.newFixedThreadPool(threadNum);
            // 定义一个任务集合
            List<Callable<Integer>> tasks = new LinkedList<Callable<Integer>>();
            Callable<Integer> task = null;
            List<RollingData> cutList = null;
            // 确定每条线程的数据
            for (int i = 0; i < threadNum; i++) {
                if (i == threadNum - 1) {
                    int index = dataSizeEveryThread * i - 1 < 0 ? 0 : dataSizeEveryThread * i - 1;
                    cutList = rollingDataList.subList(index, dataSize);
                } else {
                    if (i == 0) {
                        cutList = rollingDataList.subList(dataSizeEveryThread * i, dataSizeEveryThread * (i + 1));
                    } else {
                        cutList = rollingDataList.subList(dataSizeEveryThread * i - 1, dataSizeEveryThread * (i + 1));
                    }

                }
                final List<RollingData> listRollingData = cutList;
                task = new CalculateGridForZhuang();
                ((CalculateGridForZhuang) task).setRollingDataRange(rollingDataRange);
                ((CalculateGridForZhuang) task).setRollingDataList(listRollingData);
                ((CalculateGridForZhuang) task).setTableName(tableName);
                ((CalculateGridForZhuang) task).setxNum(xNum);
                ((CalculateGridForZhuang) task).setyNum(yNum);
                // 这里提交的任务容器列表和返回的Future列表存在顺序对应的关系
                tasks.add(task);
            }
            List<Future<Integer>> results = exec.invokeAll(tasks);
            for (Future<Integer> future : results) {
                log.info(future.get().toString());
            }
            // 关闭线程池
            exec.shutdown();
            log.info("线程任务执行结束");
        }
        log.error("生成网格遍数执行任务消耗了 ：" + (System.currentTimeMillis() - start) + "毫秒");
        //绘制图片
        //得到图片缓冲区
        int dd = 5;//图片精度
        /*碾压轨迹遍数的绘制*/
        BufferedImage bi = new BufferedImage(xNum, yNum, BufferedImage.TYPE_INT_ARGB);//TYPE_INT_ARGB 为透明的 INT精确度达到一定,RGB三原色，高度70,宽度150
        //得到它的绘制环境(这张图片的笔)
        Graphics2D g2 = (Graphics2D) bi.getGraphics();
        RollingResult rollingResult = new RollingResult();
        List<Coordinate> list = JSONArray.parseArray(damsConstruction.getRanges(), Coordinate.class);
        Coordinate[] pointList = new Coordinate[list.size() + 1];  //最重要，不能遗漏，预先申请好数组空间
        list.toArray(pointList);
        pointList[list.size()] = pointList[0];
        GeometryFactory gf = new GeometryFactory();
        Geometry edgePoly = gf.createPolygon(pointList);


        long drawstart = System.currentTimeMillis();
        int count0 = 0;
        for (int i = 0; i < xNum - 1; i++) {
            for (int j = 0; j < yNum - 1; j++) {
                //计算边界的网格数量
                Double xTmp = rollingDataRange.getMinCoordX() + i * division;
                Double yTmp = rollingDataRange.getMinCoordY() + j * division;
                //判断点是否在多边形内
                Coordinate point = new Coordinate(xTmp, yTmp);
                PointLocator a = new PointLocator();
                boolean p1 = a.intersects(point, edgePoly);
                if (p1) {
                    count0++;
                    Integer rollingTimes = StoreHouseMap.getStoreHouses2RollingData().get(tableName)[i][j].getRollingTimes();
                    calculateRollingtimes(rollingTimes, rollingResult);
                    g2.setColor(getColorByCount2(rollingTimes));
                    g2.fillRect(i, j, 1, 1);
                }
            }
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bi, "PNG", baos);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] bytes = baos.toByteArray();//转换成字节
        String bsae64_string = "data:image/png;base64," + Base64.encodeBase64String(bytes);
        log.error("绘图执行任务消耗了 ：" + (System.currentTimeMillis() - drawstart) + "毫秒");

        if (damsConstruction.getGaocheng() == null) {
            damsConstruction.setGaocheng(0d);
        }
        //无经纬度概念 无需坐标转换
        ProjCoordinate projCoordinate1 = new ProjCoordinate(rollingDataRange.getMinCoordY(), rollingDataRange.getMinCoordX(), damsConstruction.getGaocheng());
        ProjCoordinate projCoordinate2 = new ProjCoordinate(rollingDataRange.getMaxCoordY(), rollingDataRange.getMaxCoordX(), damsConstruction.getGaocheng());
        JSONObject result = new JSONObject();
        rollingResult.setTime0(count0 - rollingResult.getTime1() - rollingResult.getTime2() - rollingResult.getTime3() - rollingResult.getTime4() - rollingResult.getTime5() - rollingResult.getTime6() - rollingResult.getTime7() - rollingResult.getTime8()
                - rollingResult.getTime9() - rollingResult.getTime10() - rollingResult.getTime11() - rollingResult.getTime11Up());
        result.put("rollingResult", rollingResult);
        result.put("base64", bsae64_string);
        result.put("imageBuffer", bi);
//        result.put("bsae64ForVibrate",bsae64_string_ForVibrate);
        result.put("rollingDataRange", rollingDataRange);
        result.put("pointLeftBottom", projCoordinate1);
        result.put("pointRightTop", projCoordinate2);
        result.put("height", damsConstruction.getGaocheng());
        result.put("cenggao", damsConstruction.getCenggao());
        result.put("range", range);
        shorehouseRange.remove(tableName);
        StoreHouseMap.getStoreHouses2RollingData().remove(tableName);
        return result;
    }


    public String clearMatrix(String userName) {
        /*清除storehouseRange*/
        /*清除storehouseMaos2RollingData*/
        Iterator<String> iterator = shorehouseRange.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String[] split1 = key.split("-");
            String userNameOfMatrix = split1[0];
            if (userName.equalsIgnoreCase(userNameOfMatrix)) {
                iterator.remove();
            }
        }
        Iterator<String> iterator2 = StoreHouseMap.getStoreHouses2RollingData().keySet().iterator();
        while (iterator2.hasNext()) {
            String key = iterator2.next();
            String[] split1 = key.split("-");
            String userNameOfMatrix = split1[0];
            if (userName.equalsIgnoreCase(userNameOfMatrix)) {
                iterator2.remove();
            }
        }
        return "清除该用户对应的内存";
    }

    /**
     * 按分部工程绘图
     */
    public List getDivisionalPicForThread(String userName, String tableName) throws ExecutionException, InterruptedException {
        //将所属于分部工程的单元工程tablename放入列表
        long starttime = System.currentTimeMillis();
        List listName = rollingDataMapper.getUnittableName(tableName);
        int size = listName.size();
        //定义多线程任务列表
        List<ThreadGetPic> listThreadGetPic = new LinkedList<>();
        //创建线程
        for (int i = 0; i < size; i++) {
            listThreadGetPic.add(new ThreadGetPic(userName, String.valueOf(listName.get(i))));
        }

        ExecutorService ser = Executors.newFixedThreadPool(CORECOUNT);
        //提交执行
        List<Future> listFuture = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            listFuture.add(ser.submit(listThreadGetPic.get(i)));
        }
        //将jsonobject结果加入到List
        List<JSONObject> listJSONObject = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            listJSONObject.add((JSONObject) listFuture.get(i).get());
        }
        ser.shutdown();
        System.out.println("分部工程图用时" + (System.currentTimeMillis() - starttime) + "ms");
        return listJSONObject;
    }

    /**
     * 纵剖面分析
     *
     * @param userName
     * @param ids
     * @param zhuangX
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws IOException
     */
    public JSONObject longitudinal(String userName, List<Integer> ids, double zhuangX) throws InterruptedException, ExecutionException, IOException {
        double yEndMax = this.tableMapper.findMaxYend();
        double yBeginMin = this.tableMapper.findMinYbegin();
        double gaochengMin = this.tableMapper.findMinGaocheng();
        double gaochengMax = this.tableMapper.findMaxGaocheng();
        double lastCenggao = this.tableMapper.findLastCenggao();//查询最高层的层高，用来确定最高处
        /*Integer yNum2 = (int) Math.ceil((yEndMax-yBeginMin)/division);//横剖 =图片的高
        Integer zNum = (int) Math.ceil((gaochengMax-gaochengMin)*100+lastCenggao)+600;//有可能存在数组越界，所以手动加了像素  横剖*/
        //1.确定生成的剖面图大小
        int yNum2 = 3360;
        int zNum = 1000;
        System.out.println("zNum:" + zNum);
        BufferedImage bi2 = new BufferedImage(yNum2, zNum, BufferedImage.TYPE_INT_ARGB);//用来从bi里面取到需要的像素点
        //System.out.println("仓数："+ids.size());
        int ww = 0;
        for (int id : ids) {
            ww++;
            DamsConstruction damsConstruction = damsConstructionMapper.selectByPrimaryKey(id);
            double gaocheng = damsConstruction.getGaocheng();
            double cenggao = damsConstruction.getCenggao();
            //System.out.println("id:"+id+"高程："+gaocheng+"层高："+cenggao);
            double yBegin = damsConstruction.getYbegin();
            String tableName = damsConstruction.getTablename();
            String mat = damsConstruction.getMaterialname();
            /*String range = tableName.split("_")[0]+"_"+tableName.split("_")[1];*/
            RollingDataRange rollingDataRange = new RollingDataRange();
            //从数据库中获得范围
            rollingDataRange.setMaxCoordX(damsConstruction.getXend());
            rollingDataRange.setMinCoordX(damsConstruction.getXbegin());
            rollingDataRange.setMaxCoordY(damsConstruction.getYend());
            rollingDataRange.setMinCoordY(damsConstruction.getYbegin());
            //记录外接矩形的范围
            StorehouseRange storehouseRange = new StorehouseRange();
            storehouseRange.setMinOfxList(rollingDataRange.getMinCoordY());
            storehouseRange.setMaxOfxList(rollingDataRange.getMaxCoordY());
            storehouseRange.setMinOfyList(rollingDataRange.getMinCoordX());
            storehouseRange.setMaxOfyList(rollingDataRange.getMaxCoordX());
            Integer yNum = (int) Math.ceil((rollingDataRange.getMaxCoordY() - rollingDataRange.getMinCoordY()) / division);//行数
            Integer xNum = (int) Math.ceil((rollingDataRange.getMaxCoordX() - rollingDataRange.getMinCoordX()) / division);//列数
            Integer xNumZhuangX = (int) Math.ceil((zhuangX - rollingDataRange.getMinCoordX()) / division);//zhuangx所在列
            MatrixItem[][] matrix = new MatrixItem[xNum][yNum];
            for (int x = 0; x < xNum; x++) {
                for (int y = 0; y < yNum; y++) {
                    matrix[x][y] = new MatrixItem();
                    matrix[x][y].setRollingTimes(0);
                }
            }

            /*key增加用户username 便于后边清除内存*/
            String key = userName + "-" + tableName;
            shorehouseRange.put(key, storehouseRange);
            StoreHouseMap.getStoreHouses2RollingData().put(key, matrix);
            List<Car> carList = carMapper.findCar();
            long start = System.currentTimeMillis();
            //固定线程的数量
            for (Car car : carList) {
                tableName = "t_" + car.getCarID();
                List<RollingData> rollingDataList = rollingDataMapper.getAllRollingDataByCube2(tableName, car.getCarID().toString(), damsConstruction.getXbegin(), damsConstruction.getXend(), damsConstruction.getYbegin(), damsConstruction.getYend(), damsConstruction.getGaocheng(), damsConstruction.getGaocheng() + damsConstruction.getCenggao() * 0.01, mat);
                // 总数据条数
                int dataSize = rollingDataList.size();
                if (dataSize < 10) {
                    continue;
                }
                if (dataSize == 0) {
                    continue;
                }
                // 线程数 2个核心
                int threadNum = CORECOUNT;
                // 余数
                int special = dataSize % threadNum;
                //每个线程处理的数据量
                int dataSizeEveryThread = dataSize / threadNum;
                // 创建一个线程池
                ExecutorService exec = Executors.newFixedThreadPool(threadNum);
                // 定义一个任务集合
                List<Callable<Integer>> tasks = new LinkedList<Callable<Integer>>();
                Callable<Integer> task = null;
                List<RollingData> cutList = null;
                // 确定每条线程的数据
                for (int i = 0; i < threadNum; i++) {
                    if (i == threadNum - 1) {
                        int index = dataSizeEveryThread * i - 1 < 0 ? 0 : dataSizeEveryThread * i - 1;
                        cutList = rollingDataList.subList(index, dataSize);
                    } else {
                        if (i == 0) {
                            cutList = rollingDataList.subList(dataSizeEveryThread * i, dataSizeEveryThread * (i + 1));
                        } else {
                            cutList = rollingDataList.subList(dataSizeEveryThread * i - 1, dataSizeEveryThread * (i + 1));
                        }

                    }
                    final List<RollingData> listRollingData = cutList;
                    task = new CalculateGridForZhuang();
                    ((CalculateGridForZhuang) task).setRollingDataRange(rollingDataRange);
                    ((CalculateGridForZhuang) task).setRollingDataList(listRollingData);
                    ((CalculateGridForZhuang) task).setTableName(key);
                    ((CalculateGridForZhuang) task).setxNum(xNum);
                    ((CalculateGridForZhuang) task).setyNum(yNum);
                    // 这里提交的任务容器列表和返回的Future列表存在顺序对应的关系
                    tasks.add(task);
                }
                List<Future<Integer>> results = exec.invokeAll(tasks);
                for (Future<Integer> future : results) {
                    log.info(future.get().toString());
                }
                // 关闭线程池
                exec.shutdown();
                log.info("线程任务执行结束");
            }
            log.error("计算碾压遍数执行任务消耗了 ：" + (System.currentTimeMillis() - start) + "毫秒");

            //绘制图片
            //得到图片缓冲区
            /*碾压轨迹遍数的绘制*/
            BufferedImage bi = new BufferedImage(xNum, yNum, BufferedImage.TYPE_INT_ARGB);//TYPE_INT_ARGB 为透明的 INT精确度达到一定,RGB三原色，高度70,宽度150

            //得到它的绘制环境(这张图片的笔)
            Graphics2D g2 = (Graphics2D) bi.getGraphics();
            RollingResult rollingResult = new RollingResult();
            List<Coordinate> list = JSONArray.parseArray(damsConstruction.getRanges(), Coordinate.class);
            Coordinate[] pointList = new Coordinate[list.size() + 1];  //最重要，不能遗漏，预先申请好数组空间
            list.toArray(pointList);
            pointList[list.size()] = pointList[0];
            GeometryFactory gf = new GeometryFactory();
            Geometry edgePoly = gf.createPolygon(pointList);
            long drawstart = System.currentTimeMillis();
            int count0 = 0;
            for (int i = 0; i < xNum - 1; i++) {
                for (int j = 0; j < yNum - 1; j++) {
                    //计算边界的网格数量
                    Double xTmp = rollingDataRange.getMinCoordX() + i * division;
                    Double yTmp = rollingDataRange.getMinCoordY() + j * division;
                    //判断点是否在多边形内
                    Coordinate point = new Coordinate(xTmp, yTmp);
                    PointLocator a = new PointLocator();
                    boolean p1 = a.intersects(point, edgePoly);
                    if (p1) {
                        count0++;
                        Integer rollingTimes = StoreHouseMap.getStoreHouses2RollingData().get(key)[i][j].getRollingTimes();
                        calculateRollingtimes(rollingTimes, rollingResult);
                        g2.setColor(getColorByCount2(rollingTimes));
                        g2.fillRect(i, j, 1, 1);
                    }
                }
            }

            ByteArrayOutputStream BIbaos = new ByteArrayOutputStream();
            try {
                ImageIO.write(bi, "PNG", BIbaos);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            byte[] BIbytes = BIbaos.toByteArray();//转换成字节
            String BI_string = "data:image/png;base64," + Base64.encodeBase64String(BIbytes);

            int cangZMin = (int) (gaocheng - TrackConstant.BEGIN_GAOCHENG) * 10;//最小高层
            int cangZmax = (int) (cangZMin + cenggao / 100 * 10);
            int cangYMin = (int) (damsConstruction.getYbegin() / 1.0d);
            int cangYMax = (int) (damsConstruction.getYend() / 1.0d);
            for (int i = cangZMin; i < cangZmax; i++) {
                for (int j = cangYMin; j < cangYMax; j++) {
                    int rgb = bi.getRGB(xNumZhuangX, j - cangYMin);
                    bi2.setRGB(j, i, rgb);
                }
            }

            //外层循环是高程维度，1cm高度对应一个像素点的宽度。由于高程单位是m，所以乘100
            //(int)Math.ceil((gaocheng-gaochengMin)*100)为这个仓在高程维度的起点，共cenggao个像素高度
            /*for (int i = (int)Math.ceil((gaocheng-gaochengMin)*100); i < (int)Math.ceil((gaocheng-gaochengMin)*100+cenggao); i++) {
//                for(int j=(int)Math.ceil((yBegin-yBeginMin)/division);j<(int)Math.ceil((yBegin-yBeginMin)/division)+yNum;j++){
                //内层循环取zhuangx对应的像素点
                for(int j=0;j<yNum;j++){
                    int rgb = bi.getRGB(xNumZhuangX,j);
                    bi2.setRGB(j+(int)Math.ceil((yBegin-yBeginMin)/division),i,rgb);//起点为j+(int)Math.ceil((yBegin-yBeginMin)/division)
                    //System.out.println("i:"+i);
//                    bi2.setRGB(j,i,bi.getRGB(xNumZhuangX,j-(int)(Math.ceil((yBegin-yBeginMin)/division))));
                }
            }*/
            if (ww == 3) {
                break;
            }
        }

        ByteArrayOutputStream BI2baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bi2, "PNG", BI2baos);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] BI2bytes = BI2baos.toByteArray();//转换成字节
        String BI2_string = "data:image/png;base64," + Base64.encodeBase64String(BI2bytes);

        /*BufferedImage bi3 = new BufferedImage(yNum2,zNum,BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < zNum; i++) {
            for (int j = 0; j < yNum2; j++) {
                bi3.setRGB(j,i,bi2.getRGB(j,zNum-i-1));//由于高程纬度是由低到高存的，显示的时候也是由低到高显示，所以上下翻转一下
            }
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bi3,"PNG",baos);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] bytes = baos.toByteArray();//转换成字节
        String bsae64_string="data:image/png;base64,"+ Base64.encodeBase64String(bytes);*/
        JSONObject result = new JSONObject();
        //log.error("绘图执行任务消耗了 ：" + (System.currentTimeMillis() - drawstart) + "毫秒");

//        if(damsConstruction.getGaocheng()==null){
//            damsConstruction.setGaocheng(0d);
//        }
//        //无经纬度概念 无需坐标转换
//        ProjCoordinate projCoordinate1 = new ProjCoordinate(rollingDataRange.getMinCoordY(), rollingDataRange.getMinCoordX(),damsConstruction.getGaocheng());
//        ProjCoordinate projCoordinate2 = new ProjCoordinate(rollingDataRange.getMaxCoordY(), rollingDataRange.getMaxCoordX(),damsConstruction.getGaocheng());
//        JSONObject result = new JSONObject();
//        rollingResult.setTime0(count0-rollingResult.getTime1()-rollingResult.getTime2()-rollingResult.getTime3()-rollingResult.getTime4()-rollingResult.getTime5()-rollingResult.getTime6()-rollingResult.getTime7()-rollingResult.getTime8()
//                -rollingResult.getTime9()-rollingResult.getTime10()-rollingResult.getTime11()-rollingResult.getTime11Up());
//        result.put("rollingResult",rollingResult);
        //result.put("base64",bsae64_string);//返回base64
//        result.put("rollingDataRange",rollingDataRange);
//        result.put("pointLeftBottom",projCoordinate1);
//        result.put("pointRightTop",projCoordinate2);
//        result.put("height",damsConstruction.getGaocheng());
//        result.put("cenggao",damsConstruction.getCenggao());
        /*  result.put("range",range);*/
        return result;
    }

    /**
     * 横剖面分析
     * 原理和纵剖面相同，只是将查询参数由zhuangx变成zhuangy，以及一些变量由x到y，由y到x的区别
     *
     * @param userName
     * @param ids
     * @param zhuangY
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws IOException
     */
    public JSONObject cross(String userName, List<Integer> ids, double zhuangY) throws InterruptedException, ExecutionException, IOException {
        double xEndMax = this.tableMapper.findMaxXend();
        double xBeginMin = this.tableMapper.findMinXbegin();
        double gaochengMin = this.tableMapper.findMinGaocheng();
        double gaochengMax = this.tableMapper.findMaxGaocheng();
        double lastCenggao = this.tableMapper.findLastCenggao();
        Integer xNum2 = (int) Math.ceil((xEndMax - xBeginMin) / division) + 1000;
        Integer zNum = (int) Math.ceil((gaochengMax - gaochengMin) * 100 + lastCenggao) + 1000;
        //System.out.println("zNum:"+zNum);
        BufferedImage bi2 = new BufferedImage(xNum2, zNum, BufferedImage.TYPE_INT_ARGB);
        //System.out.println("仓数："+ids.size());
        for (int id : ids) {
            DamsConstruction damsConstruction = damsConstructionMapper.selectByPrimaryKey(id);
            double gaocheng = damsConstruction.getGaocheng();
            double cenggao = damsConstruction.getCenggao();
            //System.out.println("id:"+id+"高程："+gaocheng+"层高："+cenggao);
            double xBegin = damsConstruction.getXbegin();
            String tableName = damsConstruction.getTablename();
            String mat = damsConstruction.getMaterialname();
            /*String range = tableName.split("_")[0]+"_"+tableName.split("_")[1];*/
            RollingDataRange rollingDataRange = new RollingDataRange();
            //从数据库中获得范围
            rollingDataRange.setMaxCoordX(damsConstruction.getXend());
            rollingDataRange.setMinCoordX(damsConstruction.getXbegin());
            rollingDataRange.setMaxCoordY(damsConstruction.getYend());
            rollingDataRange.setMinCoordY(damsConstruction.getYbegin());
            //记录外接矩形的范围
            StorehouseRange storehouseRange = new StorehouseRange();
            storehouseRange.setMinOfxList(rollingDataRange.getMinCoordY());
            storehouseRange.setMaxOfxList(rollingDataRange.getMaxCoordY());
            storehouseRange.setMinOfyList(rollingDataRange.getMinCoordX());
            storehouseRange.setMaxOfyList(rollingDataRange.getMaxCoordX());
            Integer yNum = (int) Math.ceil((rollingDataRange.getMaxCoordY() - rollingDataRange.getMinCoordY()) / division);//行数
            Integer xNum = (int) Math.ceil((rollingDataRange.getMaxCoordX() - rollingDataRange.getMinCoordX()) / division);//列数
            Integer yNumZhuangY = (int) Math.ceil((zhuangY - rollingDataRange.getMinCoordY()) / division);//zhuangY所在行
            MatrixItem[][] matrix = new MatrixItem[xNum][yNum];
            for (int x = 0; x < xNum; x++) {
                for (int y = 0; y < yNum; y++) {
                    matrix[x][y] = new MatrixItem();
                    matrix[x][y].setRollingTimes(0);
                }
            }

            /*key增加用户username 便于后边清除内存*/
            String key = userName + "-" + tableName;
            shorehouseRange.put(key, storehouseRange);
            StoreHouseMap.getStoreHouses2RollingData().put(key, matrix);
            List<Car> carList = carMapper.findCar();
            long start = System.currentTimeMillis();
            //固定线程的数量
            for (Car car : carList) {
                tableName = "t_" + car.getCarID();
                List<RollingData> rollingDataList = rollingDataMapper.getAllRollingDataByCube2(tableName, car.getCarID().toString(), damsConstruction.getXbegin(), damsConstruction.getXend(), damsConstruction.getYbegin(), damsConstruction.getYend(), damsConstruction.getGaocheng(), damsConstruction.getGaocheng() + damsConstruction.getCenggao() * 0.01, mat);
                // 总数据条数
                int dataSize = rollingDataList.size();
                if (dataSize < 10) {
                    continue;
                }
                if (dataSize == 0) {
                    continue;
                }
                // 线程数 2个核心
                int threadNum = CORECOUNT;
                // 余数
                int special = dataSize % threadNum;
                //每个线程处理的数据量
                int dataSizeEveryThread = dataSize / threadNum;
                // 创建一个线程池
                ExecutorService exec = Executors.newFixedThreadPool(threadNum);
                // 定义一个任务集合
                List<Callable<Integer>> tasks = new LinkedList<Callable<Integer>>();
                Callable<Integer> task = null;
                List<RollingData> cutList = null;
                // 确定每条线程的数据
                for (int i = 0; i < threadNum; i++) {
                    if (i == threadNum - 1) {
                        int index = dataSizeEveryThread * i - 1 < 0 ? 0 : dataSizeEveryThread * i - 1;
                        cutList = rollingDataList.subList(index, dataSize);
                    } else {
                        if (i == 0) {
                            cutList = rollingDataList.subList(dataSizeEveryThread * i, dataSizeEveryThread * (i + 1));
                        } else {
                            cutList = rollingDataList.subList(dataSizeEveryThread * i - 1, dataSizeEveryThread * (i + 1));
                        }

                    }
                    final List<RollingData> listRollingData = cutList;
                    task = new CalculateGridForZhuang();
                    ((CalculateGridForZhuang) task).setRollingDataRange(rollingDataRange);
                    ((CalculateGridForZhuang) task).setRollingDataList(listRollingData);
                    ((CalculateGridForZhuang) task).setTableName(key);
                    ((CalculateGridForZhuang) task).setxNum(xNum);
                    ((CalculateGridForZhuang) task).setyNum(yNum);
                    // 这里提交的任务容器列表和返回的Future列表存在顺序对应的关系
                    tasks.add(task);
                }
                List<Future<Integer>> results = exec.invokeAll(tasks);
                for (Future<Integer> future : results) {
                    log.info(future.get().toString());
                }
                // 关闭线程池
                exec.shutdown();
                log.info("线程任务执行结束");
            }
            log.error("计算碾压遍数执行任务消耗了 ：" + (System.currentTimeMillis() - start) + "毫秒");

            //绘制图片
            //得到图片缓冲区
            /*碾压轨迹遍数的绘制*/
            BufferedImage bi = new BufferedImage(xNum, yNum, BufferedImage.TYPE_INT_ARGB);//TYPE_INT_ARGB 为透明的 INT精确度达到一定,RGB三原色，高度70,宽度150

            //得到它的绘制环境(这张图片的笔)
            Graphics2D g2 = (Graphics2D) bi.getGraphics();
            RollingResult rollingResult = new RollingResult();
            List<Coordinate> list = JSONArray.parseArray(damsConstruction.getRanges(), Coordinate.class);
            Coordinate[] pointList = new Coordinate[list.size() + 1];  //最重要，不能遗漏，预先申请好数组空间
            list.toArray(pointList);
            pointList[list.size()] = pointList[0];
            GeometryFactory gf = new GeometryFactory();
            Geometry edgePoly = gf.createPolygon(pointList);
            long drawstart = System.currentTimeMillis();
            int count0 = 0;
            for (int i = 0; i < xNum - 1; i++) {
                for (int j = 0; j < yNum - 1; j++) {
                    //计算边界的网格数量
                    Double xTmp = rollingDataRange.getMinCoordX() + i * division;
                    Double yTmp = rollingDataRange.getMinCoordY() + j * division;
                    //判断点是否在多边形内
                    Coordinate point = new Coordinate(xTmp, yTmp);
                    PointLocator a = new PointLocator();
                    boolean p1 = a.intersects(point, edgePoly);
                    if (p1) {
                        count0++;
                        Integer rollingTimes = StoreHouseMap.getStoreHouses2RollingData().get(key)[i][j].getRollingTimes();
                        calculateRollingtimes(rollingTimes, rollingResult);
                        g2.setColor(getColorByCount2(rollingTimes));
                        g2.fillRect(i, j, 1, 1);
                    }
                }
            }
            for (int i = (int) Math.ceil((gaocheng - gaochengMin) * 100); i < (int) Math.ceil((gaocheng - gaochengMin) * 100 + cenggao); i++) {
//                for(int j=(int)Math.ceil((yBegin-yBeginMin)/division);j<(int)Math.ceil((yBegin-yBeginMin)/division)+yNum;j++){
                for (int j = 0; j < xNum; j++) {
                    int rgb = bi.getRGB(j, yNumZhuangY);
                    bi2.setRGB(j + (int) Math.ceil((xBegin - xBeginMin) / division), i, rgb);
                    //System.out.println("i:"+i);
//                    bi2.setRGB(j,i,bi.getRGB(xNumZhuangX,j-(int)(Math.ceil((yBegin-yBeginMin)/division))));
                }
            }
        }
        BufferedImage bi3 = new BufferedImage(xNum2, zNum, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < zNum; i++) {
            for (int j = 0; j < xNum2; j++) {
                bi3.setRGB(j, i, bi2.getRGB(j, zNum - i - 1));
            }
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bi3, "PNG", baos);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] bytes = baos.toByteArray();//转换成字节
        String bsae64_string = "data:image/png;base64," + Base64.encodeBase64String(bytes);
        JSONObject result = new JSONObject();
        //log.error("绘图执行任务消耗了 ：" + (System.currentTimeMillis() - drawstart) + "毫秒");

//        if(damsConstruction.getGaocheng()==null){
//            damsConstruction.setGaocheng(0d);
//        }
//        //无经纬度概念 无需坐标转换
//        ProjCoordinate projCoordinate1 = new ProjCoordinate(rollingDataRange.getMinCoordY(), rollingDataRange.getMinCoordX(),damsConstruction.getGaocheng());
//        ProjCoordinate projCoordinate2 = new ProjCoordinate(rollingDataRange.getMaxCoordY(), rollingDataRange.getMaxCoordX(),damsConstruction.getGaocheng());
//        JSONObject result = new JSONObject();
//        rollingResult.setTime0(count0-rollingResult.getTime1()-rollingResult.getTime2()-rollingResult.getTime3()-rollingResult.getTime4()-rollingResult.getTime5()-rollingResult.getTime6()-rollingResult.getTime7()-rollingResult.getTime8()
//                -rollingResult.getTime9()-rollingResult.getTime10()-rollingResult.getTime11()-rollingResult.getTime11Up());
//        result.put("rollingResult",rollingResult);
        result.put("base64", bsae64_string);
//        result.put("rollingDataRange",rollingDataRange);
//        result.put("pointLeftBottom",projCoordinate1);
//        result.put("pointRightTop",projCoordinate2);
//        result.put("height",damsConstruction.getGaocheng());
//        result.put("cenggao",damsConstruction.getCenggao());
        /*  result.put("range",range);*/
        return result;
    }

    @Autowired
    TAnalysisConfigMapper tanalysisConfigMapper;

    /**
     * 碾压
     *
     * @return
     */
    public QualifiedRate getQualifiedRate(RollingResult rollingresult, int target) {

        //获取分析参数设置
        TAnalysisConfig tAnalysisConfig = tanalysisConfigMapper.selectMaxIdOne();
        //将碾压遍数放入列表
        List<Integer> rollingTimeList = new LinkedList<>();
        rollingTimeList.add(rollingresult.getTime0());
        rollingTimeList.add(rollingresult.getTime1());
        rollingTimeList.add(rollingresult.getTime2());
        rollingTimeList.add(rollingresult.getTime3());
        rollingTimeList.add(rollingresult.getTime4());
        rollingTimeList.add(rollingresult.getTime5());
        rollingTimeList.add(rollingresult.getTime6());
        rollingTimeList.add(rollingresult.getTime7());
        rollingTimeList.add(rollingresult.getTime8());
        rollingTimeList.add(rollingresult.getTime9());
        rollingTimeList.add(rollingresult.getTime10());
        rollingTimeList.add(rollingresult.getTime11());
        rollingTimeList.add(rollingresult.getTime11Up());

        //计算总的碾压
        int alltime = 0;
        int qualifiedtime = 0;
        int num = target;//碾压遍次限制默认值
//        if (StringUtils.isNotNull(tAnalysisConfig)) {
//            if (StringUtils.isNotNull(tAnalysisConfig.getNum())) {
//                num = Integer.valueOf(String.valueOf(tAnalysisConfig.getNum()));
//            }
//        }
        for (int i = 1; i < 13; i++) {
            alltime += rollingTimeList.get(i);
            if (i >= num) {
                qualifiedtime += rollingTimeList.get(i);
            }
        }

        return new QualifiedRate(alltime, qualifiedtime);
    }


    public JSONObject realTimeZhuang4Rid(String id, String userName, String tablename, String timestamp) {
        //0.获取仓位边界
        DamsConstruction damsConstruction = damsConstructionMapper.selectByPrimaryKey(Integer.valueOf(id));
        List<Coordinate> list = JSONArray.parseArray(damsConstruction.getRanges(), Coordinate.class);
        Coordinate[] pointList = new Coordinate[list.size() + 1];
        list.toArray(pointList);
        pointList[list.size()] = pointList[0];
        GeometryFactory gf = new GeometryFactory();
        Geometry edgePoly = gf.createPolygon(pointList);
        //1.计算矩形块
        double minOfxList = damsConstruction.getXbegin();
        double maxOfxList = damsConstruction.getXend();
        double minOfyList = damsConstruction.getYbegin();
        double maxOfyList = damsConstruction.getYend();
        int minCol = RidUtil.col(minOfxList);
        int maxCol = RidUtil.col(maxOfxList);
        int minRow = RidUtil.row(minOfyList);
        int maxRow = RidUtil.row(maxOfyList);
        //开辟内存空间做缓存
        ConcurrentHashMap<Long, MatrixItem[][]> cache = new ConcurrentHashMap<>();
        for (int row = minRow; row <= maxRow; row++) {
            for (int col = minCol; col <= maxCol; col++) {
                long rid = RidUtil.int2Long(col, row);
                //TODO:100*100 PIXEL为一个矩形块
                MatrixItem[][] matrixItems = new MatrixItem[RidUtil.R_LEN][RidUtil.R_LEN];
                cache.put(rid, matrixItems);
            }
        }
        //2.读取数据,前后点形成多边形,绘制图片
        List<Car> cars = carMapper.findCar();
        for (Car car : cars) {
            int carId = car.getCarID();
            String storehouseName = damsConstruction.getTablename();
            List<RollingData> rollingDataList = rollingDataMapper.getAllRollingDataByVehicleID(storehouseName, String.valueOf(carId));
            if (rollingDataList.size() > 2) {
                try {
                    RollingData rollingData01 = rollingDataList.get(0);
                    RollingData rollingData02 = rollingDataList.get(0);
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
                    for (int i = 0; i < rollingDataList.size(); i++) {
                        rollingData02 = rollingDataList.get(i);
                        double dis = Math.sqrt(Math.pow(rollingData01.getZhuangX() - rollingData02.getZhuangX(), 2) +
                                Math.pow(rollingData01.getZhuangY() - rollingData02.getZhuangY(), 2));
                        if (dis < 20) {
                            if (i + 1 >= rollingDataList.size()) {
                                break;
                            }
                            //   log.error("前后距离过小:" + dis);
                        } else if (dis > 40) {
                            //  log.error("前后距离过大:" + dis + " " + rollingData01.getOrderNum() + "-" + rollingData02.getOrderNum());
                            rollingData01 = rollingData02;
                        } else {
                            WheelMarkData data = calPosition(rollingData01, rollingData02, TrackConstant.WHEEL_LEFT, TrackConstant.WHEEL_RIGHT, 0);
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
                                    int count = getPointsConvexClosure(ps, 0, 4, convex);
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
                                        for (Pixel pixel : rasters) {
                                            try {
                                                long rid = RidUtil.double2Long(pixel.getX(), pixel.getY());
                                                int bottom = RidUtil.long2Bottom(rid);
                                                int left = RidUtil.long2Left(rid);
                                                n = (pixel.getY() - bottom);
                                                m = (pixel.getX() - left);
                                                MatrixItem[][] matrixItems = cache.get(rid);
                                                if (n >= 0 && m >= 0 && n < RidUtil.R_LEN && m < RidUtil.R_LEN) {
                                                    MatrixItem item = matrixItems[m][n];
                                                    if (item == null) {
                                                        item = new MatrixItem();
                                                    }
                                                    item.setRollingTimes(item.getRollingTimes() + 1);
                                                    item.getElevationList().add(rollingData02.getElevation());
                                                    item.getAccelerationList().add(rollingData02.getAcceleration());
                                                    item.getFrequencyList().add(rollingData02.getFrequency());
                                                    item.getSpeedList().add(rollingData02.getSpeed());
                                                    item.getTimestampList().add(rollingData02.getTimestamp());
                                                    item.getVehicleIDList().add(rollingData02.getVehicleID());
                                                    item.getVibrateValueList().add(rollingData02.getVibrateValue());
                                                    if (rollingData02.getIsVibrate() == 1) {
                                                        //动碾遍数+1
                                                        item.setIsVibrate(item.getIsVibrate() + 1);
                                                    } else if (rollingData02.getIsVibrate() == 0) {
                                                        //静碾遍数+1
                                                        item.setIsNotVibrate(item.getIsNotVibrate() + 1);
                                                    }
                                                    matrixItems[m][n] = item;
                                                }
                                            } catch (Exception ex) {
                                                log.error("(" + m + "," + n + ")像素错误:" + ex.getMessage());
                                            }
                                        }
                                        rasters.clear();
                                        rasters = null;
//                                    }
                                        rollingData01 = rollingData02;
                                        mLastPt = data;
                                    } else {
                                        log.error("无法构建轨迹多边形");
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    log.error("计算错误:" + ex.getMessage());
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ex.printStackTrace(new PrintStream(baos));
                    String exception = baos.toString();
                    log.error(exception);
                }
            }
            rollingDataList.clear();
            rollingDataList = null;
        }
        //3.拼接图片，转换base64
        String bsae64_string = "";
        int xSize = (maxCol - minCol) * RidUtil.R_LEN;
        int ySize = (maxRow - minRow) * RidUtil.R_LEN;
        int baseX = minCol * RidUtil.R_LEN;
        int baseY = minRow * RidUtil.R_LEN;
        BufferedImage bi = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) bi.getGraphics();
        MatrixItem[][] matrixItems = null;
        Coordinate point = null;
        MatrixItem item = null;
        PointLocator a = new PointLocator();
        for (Map.Entry<Long, MatrixItem[][]> entry : cache.entrySet()) {
            long rid = entry.getKey();
            matrixItems = entry.getValue();
            int width = RidUtil.R_LEN;
            int height = RidUtil.R_LEN;
            int left = RidUtil.long2Left(rid);
            int bottom = RidUtil.long2Bottom(rid);
            int dltaX = RidUtil.long2Col(rid) * RidUtil.R_LEN - baseX;
            int dltaY = RidUtil.long2Row(rid) * RidUtil.R_LEN - baseY;
            for (int i = 0; i < width - 1; i++) {
                for (int j = 0; j < height - 1; j++) {
                    point = new Coordinate(i + left, bottom + j);
                    boolean isContainer = a.intersects(point, edgePoly);
                    if (isContainer) {
                        item = matrixItems[i][j];
                        if (item != null) {
                            int rollingTimes = item.getRollingTimes();
                            g2.setColor(getColorByCount2(rollingTimes));
                            g2.fillRect(i + dltaX, j + dltaY, 1, 1);
                        }
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
        //4.开启线程给前端传递实时轨迹
        Set<String> threadList = new HashSet<>();
        //5.传到前端
        JSONObject result = new JSONObject();
        result.put("tablename", id);
        result.put("base64", bsae64_string);
        RollingDataRange rollingDataRange = new RollingDataRange();
        rollingDataRange.setMinCoordX(minOfxList);
        rollingDataRange.setMaxCoordX(maxOfxList);
        rollingDataRange.setMinCoordY(minOfyList);
        rollingDataRange.setMaxCoordY(maxOfyList);
        result.put("rollingDataRange", rollingDataRange);
        ProjCoordinate projLeftBottom = new ProjCoordinate(minOfyList, minOfxList);
        result.put("pointLeftBottom", projLeftBottom);
        ProjCoordinate projRightTop = new ProjCoordinate(maxOfyList, maxOfxList);
        result.put("pointRightTop", projRightTop);
        result.put("height", damsConstruction.getGaocheng());
        result.put("threadList", threadList);
        result.put("carList", cars);
        result.put("range", damsConstruction.getRanges());
        result.put("ranges", damsConstruction.getRanges());
        return result;
    }


    private static class WheelMarkData {
        public Point2D LPt;
        public Point2D RPt;
        public Point2D CPt;

        @Override
        public String toString() {
            return "L:" + LPt.toString() +
                    "  C:" + CPt.toString() +
                    "   R:" + RPt.toString();
        }
    }

    private static class Quadrilateral {
        public Point2D pt1 = new Point2D();

        public Point2D pt2 = new Point2D();

        public Point2D pt3 = new Point2D();

        public Point2D pt4 = new Point2D();

        public Point2D getCenterPoint() {
            Point2D pt = new Point2D();
            pt.setX((pt1.getX() + pt3.getX()) / 2);
            pt.setY((pt1.getY() + pt3.getY()) / 2);
            return pt;
        }
    }

    private static class Point2D {
        private double x;
        private double y;

        public Point2D() {
        }

        public Point2D(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }
    }

    private WheelMarkData calPosition(RollingData lastp, RollingData currentp,
                                      double mLeftLen, double mRightLen, double mDistance) {
        try {
            WheelMarkData wheelMarkData;
            Point2D tmPoint2d;
            Point2D startpt = new Point2D(lastp.getZhuangX(), lastp.getZhuangY());
            Point2D endpt = new Point2D(currentp.getZhuangX(), currentp.getZhuangY());

            double _x = endpt.getX() - startpt.getX();
            double _y = endpt.getY() - startpt.getY();
            double len = Math.sqrt(_x * _x + _y * _y);
            if (len == 0) {
                return null;
            }
            double x = mDistance * (endpt.getX() - startpt.getX()) / len + endpt.getX();
            double y = mDistance * (endpt.getY() - startpt.getY()) / len + endpt.getY();
            tmPoint2d = new Point2D(x, y);
            //滚轮左右位置点
            Point2D[] lrPointPair = null;
            double distance = Math.sqrt(Math.pow(tmPoint2d.getX() - startpt.getX(), 2)
                    + Math.pow(tmPoint2d.getY() - startpt.getY(), 2)); // 距离公式
            if (distance != 0) {
                lrPointPair = new Point2D[2];
                double deltaX = mLeftLen * (tmPoint2d.getY() - startpt.getY()) / distance;
                double deltaY = mLeftLen * (tmPoint2d.getX() - startpt.getX()) / distance;
                Point2D LPoint = new Point2D(tmPoint2d.getX() - deltaX, tmPoint2d.getY() + deltaY);
                deltaX = mRightLen * (tmPoint2d.getY() - startpt.getY()) / distance;
                deltaY = mRightLen * (tmPoint2d.getX() - startpt.getX()) / distance;
                Point2D RPoint = new Point2D(tmPoint2d.getX() + deltaX, tmPoint2d.getY() - deltaY);
                lrPointPair[0] = LPoint;
                lrPointPair[1] = RPoint;
                wheelMarkData = new WheelMarkData();
                wheelMarkData.CPt = tmPoint2d;
                wheelMarkData.LPt = lrPointPair[0];
                wheelMarkData.RPt = lrPointPair[1];
                return wheelMarkData;
            }
        } catch (Exception ex) {

            log.error("RollingDataService", "calPosition Exception:" + ex.getMessage());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ex.printStackTrace(new PrintStream(baos));
            String exception = baos.toString();
            log.error(exception);
        }
        return null;
    }

    private int getPointsConvexClosure(PointCpb[] ps, int fromIndex, int toIndex, PointCpb[] convex) {
        sort(ps, fromIndex, toIndex);
        int len = toIndex - fromIndex;
        PointCpb[] tmp = new PointCpb[2 * len];
        int up = len, down = len;
        for (int index = fromIndex; index < toIndex; index++) {
            tmp[up] = tmp[down] = ps[index];
            while (len - up >= 2 && multiply(tmp[up + 2], tmp[up + 1], tmp[up]) >= 0) {
                tmp[up + 1] = tmp[up];
                up++;
            }
            while (down - len >= 2 && multiply(tmp[down - 2], tmp[down - 1], tmp[down]) <= 0) {
                tmp[down - 1] = tmp[down];
                down--;
            }
            up--;
            down++;
        }
        System.arraycopy(tmp, up + 1, convex, 0, down - up - 2);
        return down - up - 2;
    }

    /**
     * 计算向量ab与ac的叉积
     */
    private double multiply(PointCpb a, PointCpb b, PointCpb c) {
        return multiply(a, b, a, c);
    }

    /**
     * 计算向量ab与cd的叉积
     */
    private double multiply(PointCpb a, PointCpb b, PointCpb c, PointCpb d) {
        return (b.x - a.x) * (d.y - c.y) - (d.x - c.x) * (b.y - a.y);
    }


    /**
     * 单元报表轨迹导出
     *
     * @param userName
     * @param tableName
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws IOException
     */
    @Autowired
    private TRepairDataMapper repairDataMapper;

    public JSONObject unitreport_track(String userName, String tableName) throws InterruptedException, ExecutionException, IOException {
        String id = tableName;
        DamsConstruction damsConstruction = damsConstructionMapper.selectByPrimaryKey(Integer.valueOf(id));

        int dmstatus = damsConstruction.getStatus();
        //工作仓 数据
        tableName = damsConstruction.getTablename();
        String range = tableName.split("_")[0] + "_" + tableName.split("_")[1];
        //1.根据工作仓初设化网格
        //直接根据桩号获得范围
        BigDecimal minOfxList = BigDecimal.valueOf(damsConstruction.getXbegin());
        BigDecimal maxOfxList = BigDecimal.valueOf(damsConstruction.getXend());
        BigDecimal minOfyList = BigDecimal.valueOf(damsConstruction.getYbegin());
        BigDecimal maxOfyList = BigDecimal.valueOf(damsConstruction.getYend());
        StorehouseRange storehouseRange = new StorehouseRange();
        storehouseRange.setMinOfxList(minOfxList.doubleValue());
        storehouseRange.setMaxOfxList(maxOfxList.doubleValue());
        storehouseRange.setMinOfyList(minOfyList.doubleValue());
        storehouseRange.setMaxOfyList(maxOfyList.doubleValue());
        RollingDataRange rollingDataRange = new RollingDataRange();
        rollingDataRange.setMaxCoordY(maxOfyList.doubleValue());
        rollingDataRange.setMinCoordY(minOfyList.doubleValue());
        rollingDataRange.setMaxCoordX(maxOfxList.doubleValue());
        rollingDataRange.setMinCoordX(minOfxList.doubleValue());
        int xSize = (int) Math.abs(damsConstruction.getXend() - damsConstruction.getXbegin());
        int ySize = (int) Math.abs(damsConstruction.getYend() - damsConstruction.getYbegin());
        //所有车
        List<Car> carList = carMapper.findCar();
        //判断工作仓数据是否存在
        //  boolean isHave = StoreHouseMap.getStoreHouses2RollingData().containsKey(tableName);
        MatrixItem[][] matrix = new MatrixItem[xSize][ySize];
        if (true) {//不存在则进行
            for (int i = 0; i < xSize; i++) {
                for (int j = 0; j < ySize; j++) {
                    matrix[i][j] = new MatrixItem();
                    matrix[i][j].setRollingTimes(0);
                }
            }

            //  shorehouseRange.put(id,storehouseRange);
            // StoreHouseMap.getStoreHouses2RollingData().put(tableName, matrix);


            /**
             * 查询出当前仓位的补录区域
             */
            TRepairData record = new TRepairData();
            record.setDamsid(damsConstruction.getId());
            List<TRepairData> repairDataList = repairDataMapper.selectTRepairDatas(record);

            //2 获得数据库中的数据
            long start = System.currentTimeMillis();
            for (Car car : carList) {
                List<RollingData> rollingDataList = rollingDataMapper.getAllRollingDataByVehicleID(tableName, car.getCarID().toString());
                // 总数据条数
                int dataSize = rollingDataList.size();
                if (dataSize < 10) {
                    continue;
                }
                if (dataSize == 0) {
                    continue;
                }
                // 线程数 四个核心
                int threadNum = CORECOUNT;
                // 余数
                int special = dataSize % threadNum;
                //每个线程处理的数据量
                int dataSizeEveryThread = dataSize / threadNum;
                // 创建一个线程池
                ExecutorService exec = Executors.newFixedThreadPool(threadNum);
                // 定义一个任务集合
                List<Callable<Integer>> tasks = new LinkedList<Callable<Integer>>();
                Callable<Integer> task = null;
                List<RollingData> cutList = null;

                // 确定每条线程的数据
                for (int i = 0; i < threadNum; i++) {
                    if (i == threadNum - 1) {
                        int index = dataSizeEveryThread * i - 1 < 0 ? 0 : dataSizeEveryThread * i - 1;
                        cutList = rollingDataList.subList(index, dataSize);
                    } else {
                        if (i == 0) {
                            cutList = rollingDataList.subList(dataSizeEveryThread * i, dataSizeEveryThread * (i + 1));
                        } else {
                            cutList = rollingDataList.subList(dataSizeEveryThread * i - 1, dataSizeEveryThread * (i + 1));
                        }
                    }
                    final List<RollingData> listRollingData = cutList;
                    task = new CalculateGridForZhuangForReport();
                    ((CalculateGridForZhuangForReport) task).setRollingDataRange(rollingDataRange);
                    ((CalculateGridForZhuangForReport) task).setRollingDataList(listRollingData);
                    ((CalculateGridForZhuangForReport) task).setTableName(tableName);
                    ((CalculateGridForZhuangForReport) task).setxNum(xSize);
                    ((CalculateGridForZhuangForReport) task).setyNum(ySize);
                    ((CalculateGridForZhuangForReport) task).setTempmatrix(matrix);
                    // 这里提交的任务容器列表和返回的Future列表存在顺序对应的关系
                    tasks.add(task);
                }
                List<Future<Integer>> results = exec.invokeAll(tasks);
                for (Future<Integer> future : results) {
                    log.info(future.get().toString());
                }
                // 关闭线程池
                exec.shutdown();

                log.info("线程任务执行结束");
            }
            //根据补录内容 将仓对应区域的碾压遍数、速度、压实度更新
            if (repairDataList != null) {
                //   MatrixItem[][] matrixItems = matrix;
                for (TRepairData repairData : repairDataList) {
                    String ranges = repairData.getRanges();
                    int passCount = repairData.getColorId();
                    float speed = repairData.getSpeed().floatValue();
                    double vibration = repairData.getVibration();

                    List<Coordinate> repairRange = JSONArray.parseArray(ranges, Coordinate.class);
                    Pixel[] repairPolygon = new Pixel[repairRange.size()];
                    for (int i = 0; i < repairRange.size(); i++) {
                        Coordinate coordinate = repairRange.get(i);
                        Pixel pixel = new Pixel();
                        pixel.setX((int) coordinate.x);
                        pixel.setY((int) coordinate.y);
                        repairPolygon[i] = pixel;
                    }
                    List<Pixel> rasters = Scan.scanRaster(repairPolygon);
                    int bottom = (int) (rollingDataRange.getMinCoordY() * 1);
                    int left = (int) (rollingDataRange.getMinCoordX() * 1);
                    int width = (int) (rollingDataRange.getMaxCoordX() - rollingDataRange.getMinCoordX());
                    int height = (int) (rollingDataRange.getMaxCoordY() - rollingDataRange.getMinCoordY());
                    int n = 0;
                    int m = 0;
                    for (Pixel pixel : rasters) {
                        try {
                            n = (pixel.getY() - bottom);
                            m = (pixel.getX() - left);
                            if (n >= 0 && m >= 0 && n < height && m < width) {
                                MatrixItem item = matrix[m][n];
                                if (item == null) {
                                    item = new MatrixItem();
                                    matrix[m][n] = item;
                                }
                                item.setRollingTimes(passCount);
                                item.getSpeedList().add(speed);
                                item.getVibrateValueList().add(vibration);
                            }
                        } catch (Exception ex) {
                            log.error("(" + m + "," + n + ")像素错误:" + ex.getMessage());
                        }
                    }
                    rasters.clear();
                    rasters = null;
                }
                // storeHouses2RollingData =null;
            }

            log.error("生成网格遍数执行任务消耗了 ：" + (System.currentTimeMillis() - start) + "毫秒");
            // matrix= null;
        }


        RollingResult rollingResult = new RollingResult();
        //超限
        RollingResult rollingResultSpeed = new RollingResult();
        //动静碾压
        RollingResult rollingResultVibration = new RollingResult();
        int count0 = 0;
        int count0Speed = 0;
        int count0Vibration = 0;
        String bsae64_string = "";
        String bsae64_string_speed = "";
        String bsae64_string_vibration = "";
        TColorConfig vo = new TColorConfig();
        JSONObject result = new JSONObject();
        vo.setType(3L);//超限
        List<TColorConfig> colorConfigs = colorConfigMapper.select(vo);
        vo.setType(6L);//动静碾压
        List<TColorConfig> colorConfigs6 = colorConfigMapper.select(vo);
        synchronized (obj1) {// 同步代码块
            //绘制图片
            //得到图片缓冲区
            BufferedImage bi = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);//INT精确度达到一定,RGB三原色，高度70,宽度150
            //超限次数
            BufferedImage biSpeed = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);//INT精确度达到一定,RGB三原色，高度70,宽度150
            //动静碾压
            BufferedImage biVibration = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);//INT精确度达到一定,RGB三原色，高度70,宽度150
            //得到它的绘制环境(这张图片的笔)
            Graphics2D g2 = (Graphics2D) bi.getGraphics();
            //超限次数
            Graphics2D g2Speed = (Graphics2D) biSpeed.getGraphics();
            //动静碾压
            Graphics2D g2Vibration = (Graphics2D) biVibration.getGraphics();
            List<Coordinate> list = JSONArray.parseArray(damsConstruction.getRanges(), Coordinate.class);
            Coordinate[] pointList = new Coordinate[list.size() + 1];//最重要，不能遗漏，预先申请好数组空间
            list.toArray(pointList);
            pointList[list.size()] = pointList[0];
            //记录遍数
            GeometryFactory gf = new GeometryFactory();
            Geometry edgePoly = gf.createPolygon(pointList);

            Coordinate[] allcors = edgePoly.getCoordinates();

            int[] xlist = new int[allcors.length];
            int[] ylist = new int[allcors.length];
            for (int i = 0; i < allcors.length; i++) {
                Coordinate one = allcors[i];
                Double x = one.getOrdinate(0) - rollingDataRange.getMinCoordX();
                Double y = one.getOrdinate(1) - rollingDataRange.getMinCoordY();
                xlist[i] = (int) Math.round(x);
                ylist[i] = (int) Math.round(y);

            }
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(255, 0, 0));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawPolygon(xlist, ylist, allcors.length);


            long startTime = System.currentTimeMillis();
            Map<Integer, Color> colorMap = getColorMap(1L);

            int designPassCount = damsConstruction.getFrequency();
            float designSpeed = damsConstruction.getSpeed().floatValue();
            double designVibration = 0d;
            for (int i = 0; i < xSize - 1; i++) {
                for (int j = 0; j < ySize - 1; j++) {
                    //计算边界的网格数量
                    Double xTmp = rollingDataRange.getMinCoordX() + i * division;
                    Double yTmp = rollingDataRange.getMinCoordY() + j * division;
                    //判断点是否在多边形内
                    Coordinate point = new Coordinate(xTmp, yTmp);
                    PointLocator a = new PointLocator();
                    boolean p1 = a.intersects(point, edgePoly);
                    if (p1) {
                        count0++;
                        count0Speed++;
                        count0Vibration++;
                        Integer rollingTimes = matrix[i][j].getRollingTimes();
                        g2.setColor(getColorByCount2(rollingTimes, colorMap));
                        calculateRollingtimes(rollingTimes, rollingResult);
                        g2.fillRect(i, j, 1, 1);

                    }
                }
            }
            //count0表示所有属于当前单元工程的轨迹点 time0表示在计算完遍数后，该单元工程内没有碾压的数据
            int time0 = count0 - rollingResult.getTime1() - rollingResult.getTime2() - rollingResult.getTime3() - rollingResult.getTime4() - rollingResult.getTime5() - rollingResult.getTime6() - rollingResult.getTime7() - rollingResult.getTime8()
                    - rollingResult.getTime9() - rollingResult.getTime10() - rollingResult.getTime11() - rollingResult.getTime11Up();
            //因为轨迹点使用缓冲区，所以会出现time0比0小的情况，这里要加一下判断如果小于0证明单元工程全部碾压到了
            if (time0 <= 0) {
                time0 = 0;
            }
            rollingResult.setTime0(time0);


            long endTime = System.currentTimeMillis();    //获取结束时间
            log.info("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            try {
                ImageIO.write(bi, "PNG", baos);
                byte[] bytes = baos.toByteArray();//转换成字节
                bsae64_string = "data:image/png;base64," + Base64.encodeBase64String(bytes);
                baos.close();
                result.put("bi", bi);
                result.put("width_bi", xSize);
                result.put("height_bi", ySize);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        ProjCoordinate projCoordinate1 = new ProjCoordinate(rollingDataRange.getMinCoordY(), rollingDataRange.getMinCoordX(), 10);
        ProjCoordinate projCoordinate2 = new ProjCoordinate(rollingDataRange.getMaxCoordY(), rollingDataRange.getMaxCoordX(), 10);

        result.put("rollingResult", rollingResult);
        result.put("rollingResultSpeed", rollingResultSpeed);
        result.put("rollingResultVibration", rollingResultVibration);

        result.put("base64", bsae64_string);
        result.put("base64Speed", bsae64_string_speed);
        result.put("base64Vibration", bsae64_string_vibration);
        result.put("rollingDataRange", rollingDataRange);
        result.put("pointLeftBottom", projCoordinate1);
        result.put("pointRightTop", projCoordinate2);
        result.put("height", damsConstruction.getGaocheng());
        result.put("cenggao", damsConstruction.getCenggao());
        result.put("range", range);
        //边界
        result.put("ranges", damsConstruction.getRanges());
        result.put("id", damsConstruction.getId());

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = null;
            }
        }
        matrix = null;
        return result;
    }


    public JSONObject unitreport_track_zhuanghao(String userName, String tableName, int cartype) throws InterruptedException, ExecutionException, IOException {
        String id = tableName;
        DamsConstruction damsConstruction = damsConstructionMapper.selectByPrimaryKey(Integer.valueOf(id));

        int dmstatus = damsConstruction.getStatus();
        //工作仓 数据
        tableName = damsConstruction.getTablename();

        String range = tableName.split("_")[0] + "_" + tableName.split("_")[1];
        //1.根据工作仓初设化网格
        //直接根据桩号获得范围
        BigDecimal minOfxList = BigDecimal.valueOf(damsConstruction.getXbegin());
        BigDecimal maxOfxList = BigDecimal.valueOf(damsConstruction.getXend());
        BigDecimal minOfyList = BigDecimal.valueOf(damsConstruction.getYbegin());
        BigDecimal maxOfyList = BigDecimal.valueOf(damsConstruction.getYend());
        StorehouseRange storehouseRange = new StorehouseRange();
        storehouseRange.setMinOfxList(minOfxList.doubleValue());
        storehouseRange.setMaxOfxList(maxOfxList.doubleValue());
        storehouseRange.setMinOfyList(minOfyList.doubleValue());
        storehouseRange.setMaxOfyList(maxOfyList.doubleValue());
        RollingDataRange rollingDataRange = new RollingDataRange();
        rollingDataRange.setMaxCoordY(maxOfyList.doubleValue());
        rollingDataRange.setMinCoordY(minOfyList.doubleValue());
        rollingDataRange.setMaxCoordX(maxOfxList.doubleValue());
        rollingDataRange.setMinCoordX(minOfxList.doubleValue());
        int xSize = (int) Math.abs(damsConstruction.getXend() - damsConstruction.getXbegin());
        int ySize = (int) Math.abs(damsConstruction.getYend() - damsConstruction.getYbegin());

        //获取图片旋转角度
        float angele = getrotate(damsConstruction);

        //所有车

        List<Car> carList = carMapper.findCar();
        //判断工作仓数据是否存在
        //  boolean isHave = StoreHouseMap.getStoreHouses2RollingData().containsKey(tableName);
        MatrixItem[][] matrix = new MatrixItem[xSize][ySize];
        if (true) {//不存在则进行
            for (int i = 0; i < xSize; i++) {
                for (int j = 0; j < ySize; j++) {
                    matrix[i][j] = new MatrixItem();
                    matrix[i][j].setRollingTimes(0);
                }
            }

            //  shorehouseRange.put(id,storehouseRange);
            // StoreHouseMap.getStoreHouses2RollingData().put(tableName, matrix);


            /**
             * 查询出当前仓位的补录区域
             */
            TRepairData record = new TRepairData();
            record.setDamsid(damsConstruction.getId());
            record.setCartype(cartype);
            List<TRepairData> repairDataList = repairDataMapper.selectTRepairDatas(record);

            //2 获得数据库中的数据
            long start = System.currentTimeMillis();
            for (Car car : carList) {

                if (car.getType() != cartype) {
                    continue;
                }

                String newtable = GlobCache.cartableprfix[car.getType()] + "_" + tableName;
                List<RollingData> rollingDataList = rollingDataMapper.getAllRollingDataByVehicleID(newtable, car.getCarID().toString());
                // 总数据条数
                int dataSize = rollingDataList.size();
                if (dataSize < 10) {
                    continue;
                }
                if (dataSize == 0) {
                    continue;
                }
                // 线程数 四个核心
                int threadNum = CORECOUNT;

                //每个线程处理的数据量
                int dataSizeEveryThread = dataSize / (threadNum );
                // 创建一个线程池
                ExecutorService exec = Executors.newFixedThreadPool(threadNum);
                // 定义一个任务集合
                List<Callable<Integer>> tasks = new LinkedList<Callable<Integer>>();
                Callable<Integer> task = null;
                List<RollingData> cutList = null;

                // 确定每条线程的数据
                for (int i = 0; i < threadNum ; i++) {
                    if (i == threadNum  - 1) {
                        int index = dataSizeEveryThread * i - 1 < 0 ? 0 : dataSizeEveryThread * i - 1;
                        cutList = rollingDataList.subList(index-1, dataSize);
                    } else {
                        if (i == 0) {
                            cutList = rollingDataList.subList(dataSizeEveryThread * i, dataSizeEveryThread * (i + 1));
                        } else {
                            cutList = rollingDataList.subList(dataSizeEveryThread * i - 2, dataSizeEveryThread * (i + 1));
                        }
                    }
                    final List<RollingData> listRollingData = cutList;
                    task = new CalculateGridForZhuangForReport();
                    ((CalculateGridForZhuangForReport) task).setRollingDataRange(rollingDataRange);
                    ((CalculateGridForZhuangForReport) task).setRollingDataList(listRollingData);
                    ((CalculateGridForZhuangForReport) task).setTableName(tableName);
                    ((CalculateGridForZhuangForReport) task).setxNum(xSize);
                    ((CalculateGridForZhuangForReport) task).setyNum(ySize);
                    ((CalculateGridForZhuangForReport) task).setTempmatrix(matrix);
                    ((CalculateGridForZhuangForReport) task).setCartype(cartype);
                    // 这里提交的任务容器列表和返回的Future列表存在顺序对应的关系
                    tasks.add(task);
                }
                List<Future<Integer>> results = exec.invokeAll(tasks);
                for (Future<Integer> future : results) {
                    log.info(future.get().toString());
                }
                // 关闭线程池
                exec.shutdown();

                log.info("线程任务执行结束");
            }
            //根据补录内容 将仓对应区域的碾压遍数、速度、压实度更新
            if (repairDataList != null) {
                //   MatrixItem[][] matrixItems = matrix;
                for (TRepairData repairData : repairDataList) {
                    String ranges = repairData.getRanges();
                    int passCount = repairData.getColorId();
                    float speed = repairData.getSpeed().floatValue();
                    double vibration = repairData.getVibration();

                    List<Coordinate> repairRange = JSONArray.parseArray(ranges, Coordinate.class);
                    Pixel[] repairPolygon = new Pixel[repairRange.size()];
                    for (int i = 0; i < repairRange.size(); i++) {
                        Coordinate coordinate = repairRange.get(i);
                        Pixel pixel = new Pixel();
                        pixel.setX((int) coordinate.x);
                        pixel.setY((int) coordinate.y);
                        repairPolygon[i] = pixel;
                    }
                    List<Pixel> rasters = Scan.scanRaster(repairPolygon);
                    int bottom = (int) (rollingDataRange.getMinCoordY() * 1);
                    int left = (int) (rollingDataRange.getMinCoordX() * 1);
                    int width = (int) (rollingDataRange.getMaxCoordX() - rollingDataRange.getMinCoordX());
                    int height = (int) (rollingDataRange.getMaxCoordY() - rollingDataRange.getMinCoordY());
                    int n = 0;
                    int m = 0;
                    for (Pixel pixel : rasters) {
                        try {
                            n = (pixel.getY() - bottom);
                            m = (pixel.getX() - left);
                            if (n >= 0 && m >= 0 && n < height && m < width) {
                                MatrixItem item = matrix[m][n];
                                if (item == null) {
                                    item = new MatrixItem();
                                    matrix[m][n] = item;
                                }
                                item.setRollingTimes(passCount);
                                item.getSpeedList().add(speed);
                                item.getVibrateValueList().add(vibration);
                            }
                        } catch (Exception ex) {
                            log.error("(" + m + "," + n + ")像素错误:" + ex.getMessage());
                        }
                    }
                    rasters.clear();
                    rasters = null;
                }
                // storeHouses2RollingData =null;
            }

            log.error("生成网格遍数执行任务消耗了 ：" + (System.currentTimeMillis() - start) + "毫秒");
            // matrix= null;
        }


        RollingResult rollingResult = new RollingResult();
        //超限
        RollingResult rollingResultSpeed = new RollingResult();
        //动静碾压
        RollingResult rollingResultVibration = new RollingResult();
        int count0 = 0;
        int count0Speed = 0;
        int count0Vibration = 0;
        String bsae64_string = "";
        String bsae64_string_speed = "";
        String bsae64_string_vibration = "";
        TColorConfig vo = new TColorConfig();
        JSONObject result = new JSONObject();
        vo.setType(3L);//超限
        List<TColorConfig> colorConfigs = colorConfigMapper.select(vo);

        vo.setType(44L);//动静碾压
        List<TColorConfig> colorConfigs44 = colorConfigMapper.select(vo);
        synchronized (obj1) {// 同步代码块
            //绘制图片
            //得到图片缓冲区
            BufferedImage bi = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);//INT精确度达到一定,RGB三原色，高度70,宽度150

            //动静碾压
            BufferedImage biVibration = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);//INT精确度达到一定,RGB三原色，高度70,宽度150
            //得到它的绘制环境(这张图片的笔)
            Graphics2D g2 = (Graphics2D) bi.getGraphics();
            g2.transform(new AffineTransform());

            //动静碾压
            Graphics2D g2Vibration = (Graphics2D) biVibration.getGraphics();
            List<Coordinate> list = JSONArray.parseArray(damsConstruction.getRanges(), Coordinate.class);
            Coordinate[] pointList = new Coordinate[list.size() + 1];//最重要，不能遗漏，预先申请好数组空间
            list.toArray(pointList);
            pointList[list.size()] = pointList[0];
            //记录遍数
            GeometryFactory gf = new GeometryFactory();
            Geometry edgePoly = gf.createPolygon(pointList);


            Coordinate[] allcors = edgePoly.getCoordinates();

            int[] xlist = new int[allcors.length];
            int[] ylist = new int[allcors.length];
            for (int i = 0; i < allcors.length; i++) {
                Coordinate one = allcors[i];
                Double x = one.getOrdinate(0) - rollingDataRange.getMinCoordX();
                Double y = one.getOrdinate(1) - rollingDataRange.getMinCoordY();
                xlist[i] = (int) Math.round(x);
                ylist[i] = (int) Math.round(y);

            }
            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setColor(new Color(24, 157, 37));
            g2.setStroke(new BasicStroke(1f));
            g2.drawPolygon(xlist, ylist, allcors.length);

            long startTime = System.currentTimeMillis();
            Map<Integer, Color> colorMap = getColorMap(1L);

            int designPassCount = damsConstruction.getFrequency();
            float designSpeed = damsConstruction.getSpeed().floatValue();
            Double normalhoudu = damsConstruction.getCenggao();
            Double beginevolution = damsConstruction.getGaocheng();
            double designVibration = 0d;
            List<Float> allhoudu = new ArrayList<>();
            if (cartype == 2) {

                for (int i = 0; i < xSize - 1; i++) {
                    for (int j = 0; j < ySize - 1; j++) {
                        //计算边界的网格数量
                        Double xTmp = rollingDataRange.getMinCoordX() + i * division;
                        Double yTmp = rollingDataRange.getMinCoordY() + j * division;
                        //判断点是否在多边形内
                        Coordinate point = new Coordinate(xTmp, yTmp);
                        PointLocator a = new PointLocator();
                        boolean p1 = a.intersects(point, edgePoly);
                        if (p1) {
                            count0++;
                            count0Speed++;
                            count0Vibration++;
                            LinkedList<Float> evlist = matrix[i][j].getCurrentEvolution();
                            Float currentevolution = evlist.size() == 0 ? 0f : evlist.getLast();
                            if (evlist.size() > 0) {
                                // System.out.println(i + "_" + j + "最后高程：" + currentevolution);
                                allhoudu.add(100.0f * (currentevolution == null ? 0 : currentevolution - beginevolution.floatValue()));
                            }
                            Float tphoudu = 100.0f * (currentevolution == null ? 0 : currentevolution - beginevolution.floatValue()) - normalhoudu.floatValue();
                            g2.setColor(getColorByCountEvolution(tphoudu, colorConfigs44));
                            calculateRollingEvolution(tphoudu, rollingResult, colorConfigs44);
                            g2.fillRect(i, j, 1, 1);


                        }
                    }
                }
                //计算平均摊铺厚度、最大摊铺厚度、最小摊铺厚度
                if (allhoudu.size() > 0) {
                    Float avghoudu = allhoudu.stream().collect(Collectors.averagingDouble(Float::floatValue)).floatValue();
                    Float maxhoudu = allhoudu.stream().max(Float::compareTo).get().floatValue();
                    Float minhoudu = allhoudu.stream().min(Float::compareTo).get().floatValue();
                    result.put("avghoudu", avghoudu);
                    result.put("maxhoudu", maxhoudu);
                    result.put("minhoudu", minhoudu);
                }
            } else if (cartype == 1) {
                for (int i = 0; i < xSize - 1; i++) {
                    for (int j = 0; j < ySize - 1; j++) {
                        //计算边界的网格数量
                        Double xTmp = rollingDataRange.getMinCoordX() + i * division;
                        Double yTmp = rollingDataRange.getMinCoordY() + j * division;
                        //判断点是否在多边形内
                        Coordinate point = new Coordinate(xTmp, yTmp);
                        PointLocator a = new PointLocator();
                        boolean p1 = a.intersects(point, edgePoly);
                        if (p1) {
                            count0++;
                            count0Speed++;
                            count0Vibration++;
                            Integer rollingTimes = matrix[i][j].getRollingTimes();
                            g2.setColor(getColorByCount2(rollingTimes, colorMap));
                            calculateRollingtimes(rollingTimes, rollingResult);
                            g2.fillRect(i, j, 1, 1);


                        }
                    }
                }
            }
            //count0表示所有属于当前单元工程的轨迹点 time0表示在计算完遍数后，该单元工程内没有碾压的数据
            int time0 = count0 - rollingResult.getTime1() - rollingResult.getTime2() - rollingResult.getTime3() - rollingResult.getTime4() - rollingResult.getTime5() - rollingResult.getTime6() - rollingResult.getTime7() - rollingResult.getTime8()
                    - rollingResult.getTime9() - rollingResult.getTime10() - rollingResult.getTime11() - rollingResult.getTime11Up();
            //因为轨迹点使用缓冲区，所以会出现time0比0小的情况，这里要加一下判断如果小于0证明单元工程全部碾压到了
            if (time0 <= 0) {
                time0 = 0;
            }
            rollingResult.setTime0(time0);
            //:todo  计算碾压面积 用 碾压过一边的区域乘以块面积

            long endTime = System.currentTimeMillis();    //获取结束时间
            log.info("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            try {
                ImageIO.write(bi, "PNG", baos);
                byte[] bytes = baos.toByteArray();//转换成字节
                bsae64_string = "data:image/png;base64," + Base64.encodeBase64String(bytes);
                baos.close();
                result.put("bi", bi);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        result.put("width_bi", xSize);
        result.put("height_bi", ySize);
        ProjCoordinate projCoordinate1 = new ProjCoordinate(rollingDataRange.getMinCoordY(), rollingDataRange.getMinCoordX(), 10);
        ProjCoordinate projCoordinate2 = new ProjCoordinate(rollingDataRange.getMaxCoordY(), rollingDataRange.getMaxCoordX(), 10);
        result.put("angele", angele);
        result.put("rollingResult", rollingResult);
        result.put("rollingResultSpeed", rollingResultSpeed);
        result.put("rollingResultVibration", rollingResultVibration);
        result.put("base64", bsae64_string);
        result.put("base64Speed", bsae64_string_speed);
        result.put("base64Vibration", bsae64_string_vibration);
        result.put("rollingDataRange", rollingDataRange);
        result.put("pointLeftBottom", projCoordinate1);
        result.put("pointRightTop", projCoordinate2);
        result.put("height", damsConstruction.getGaocheng());
        result.put("cenggao", damsConstruction.getCenggao());
        result.put("range", range);
        //边界
        result.put("ranges", damsConstruction.getRanges());
        result.put("id", damsConstruction.getId());

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = null;
            }
        }
        matrix = null;
        return result;
    }

    public JSONObject unitreport_track_chengdu(String userName, String tableName, int rollingtimes) {
        String id = tableName;
        DamsConstruction damsConstruction = damsConstructionMapper.selectByPrimaryKey(Integer.valueOf(id));

        int dmstatus = damsConstruction.getStatus();
        //工作仓 数据
        tableName = damsConstruction.getTablename();
        String range = tableName.split("_")[0] + "_" + tableName.split("_")[1];
        //1.根据工作仓初设化网格
        //直接根据桩号获得范围

        Mileage mileage = Mileage.getmileage();
        List<PointStep> newranges = new LinkedList<>();
        TransUtils transUtils = new TransUtils();

        List<Point2D> rangelist = JSONArray.parseArray(damsConstruction.getRanges(), Point2D.class);
        List<Point2D> zhuanghaos = new LinkedList<>();
        PointStep one = new PointStep(rangelist.get(0).getX(), rangelist.get(0).getY());
        PointStep two = new PointStep(rangelist.get(1).getX(), rangelist.get(1).getY());
        PointStep three = new PointStep(rangelist.get(2).getX(), rangelist.get(2).getY());
        PointStep four = new PointStep(rangelist.get(3).getX(), rangelist.get(3).getY());
        JSONObject ob = JSONObject.parseObject(damsConstruction.getFreedom3());
        BigDecimal beginzhuang = new BigDecimal(ob.get("begin").toString()).setScale(2, RoundingMode.HALF_DOWN);
        BigDecimal endzhuang = new BigDecimal(ob.get("end").toString()).setScale(2, RoundingMode.HALF_DOWN);
        BigDecimal pianju = new BigDecimal(ob.get("width").toString()).setScale(2, RoundingMode.HALF_DOWN);


        double[] begincoord = mileage.mileage2Pixel(1, beginzhuang.doubleValue(), pianju.doubleValue(), "0");
        double[] endcoord = mileage.mileage2Pixel(1, endzhuang.doubleValue(), pianju.doubleValue(), "0");

        double dis = Math.sqrt(Math.pow(endcoord[0] - begincoord[0], 2) +
                Math.pow(endcoord[1] - begincoord[1], 2));

        transUtils.init(new PointStep(begincoord[1], begincoord[0]), new PointStep(endcoord[1], endcoord[0]), new PointStep(0, 0), new PointStep(0, dis));

        one = transUtils.transformBoePoint(one);
        two = transUtils.transformBoePoint(two);
        three = transUtils.transformBoePoint(three);
        four = transUtils.transformBoePoint(four);

        List<Double> xlistr = new LinkedList<>();
        List<Double> ylistr = new LinkedList<>();

        xlistr.add(one.getX());
        xlistr.add(two.getX());
        xlistr.add(three.getX());
        xlistr.add(four.getX());

        ylistr.add(one.getY());
        ylistr.add(two.getY());
        ylistr.add(three.getY());
        ylistr.add(four.getY());
        xlistr.sort(Comparator.comparingDouble(Double::doubleValue));
        ylistr.sort(Comparator.comparingDouble(Double::doubleValue));


        newranges.add(one);
        newranges.add(two);
        newranges.add(three);
        newranges.add(four);
        String newrangess = JSONArray.toJSONString(newranges);
        damsConstruction.setRanges(newrangess);
        BigDecimal minOfxList = BigDecimal.valueOf(xlistr.get(0));
        BigDecimal maxOfxList = BigDecimal.valueOf(xlistr.get(3));
        BigDecimal minOfyList = BigDecimal.valueOf(ylistr.get(0));
        BigDecimal maxOfyList = BigDecimal.valueOf(ylistr.get(3));
        StorehouseRange storehouseRange = new StorehouseRange();
        storehouseRange.setMinOfxList(minOfxList.doubleValue());
        storehouseRange.setMaxOfxList(maxOfxList.doubleValue());
        storehouseRange.setMinOfyList(minOfyList.doubleValue());
        storehouseRange.setMaxOfyList(maxOfyList.doubleValue());
        RollingDataRange rollingDataRange = new RollingDataRange();
        rollingDataRange.setMaxCoordY(maxOfyList.doubleValue());
        rollingDataRange.setMinCoordY(minOfyList.doubleValue());
        rollingDataRange.setMaxCoordX(maxOfxList.doubleValue());
        rollingDataRange.setMinCoordX(minOfxList.doubleValue());
        int xSize = (int) Math.abs(xlistr.get(3) - xlistr.get(0));
        int ySize = (int) Math.abs(ylistr.get(3) - ylistr.get(0));


        //所有车
        List<Car> carList = carMapper.findCar();
        //判断工作仓数据是否存在
        //  boolean isHave = StoreHouseMap.getStoreHouses2RollingData().containsKey(tableName);

        MatrixItem[][] matrix = new MatrixItem[xSize][ySize];
        //初始化 2*0.5的新格子 用于将生成好的数据归档于这些格子当中
        int gridxstep = (int) (2 / TrackConstant.kk);
        double gridystep = 0.5 / TrackConstant.kk;
        double gridxsized = xSize / (gridxstep * 1.0);
        int gridxsize = xSize / gridxstep == gridxsized ? xSize / gridxstep : xSize / gridxstep + 1;
        double gridysized = ySize / (gridystep * 1.0);
        int gridysize = ySize * 1.0 / gridystep == gridysized ? (int) (ySize / gridystep) : (int) (ySize / gridystep + 1);
        MatrixItemGrid[][] matrixItemGrids = new MatrixItemGrid[gridxsize][gridysize];

        if (true) {//不存在则进行

            for (int i = 0; i < xSize; i++) {
                for (int j = 0; j < ySize; j++) {
                    matrix[i][j] = new MatrixItem();
                    matrix[i][j].setRollingTimes(0);
                }
            }

            for (int i = 0; i < gridxsize; i++) {
                for (int j = 0; j < gridysize; j++) {
                    matrixItemGrids[i][j] = new MatrixItemGrid();
                    matrixItemGrids[i][j].setItems(new LinkedList<>());
                    matrixItemGrids[i][j].setVcvvalue(new LinkedList<>());
                }
            }


            //  shorehouseRange.put(id,storehouseRange);
            // StoreHouseMap.getStoreHouses2RollingData().put(tableName, matrix);


            /**
             * 查询出当前仓位的补录区域
             */
            TRepairData record = new TRepairData();
            record.setDamsid(damsConstruction.getId());
            List<TRepairData> repairDataList = repairDataMapper.selectTRepairDatas(record);

            //2 获得数据库中的数据
            long start = System.currentTimeMillis();
            for (Car car : carList) {
                List<RollingData> rollingDataList = rollingDataMapper.getAllRollingDataByVehicleID(tableName, car.getCarID().toString());
                // 总数据条数
                int dataSize = rollingDataList.size();
                if (dataSize < 10) {
                    continue;
                }
                if (dataSize == 0) {
                    continue;
                }
                // 线程数 四个核心
                int threadNum = CORECOUNT;
                // 余数
                int special = dataSize % threadNum;
                //每个线程处理的数据量
                int dataSizeEveryThread = dataSize / threadNum;
                // 创建一个线程池
                ExecutorService exec = Executors.newFixedThreadPool(threadNum);
                // 定义一个任务集合
                List<Callable<Integer>> tasks = new LinkedList<Callable<Integer>>();
                Callable<Integer> task = null;
                List<RollingData> cutList = null;

                // 确定每条线程的数据
                for (int i = 0; i < threadNum; i++) {
                    if (i == threadNum - 1) {
                        int index = dataSizeEveryThread * i - 1 < 0 ? 0 : dataSizeEveryThread * i - 1;
                        cutList = rollingDataList.subList(index, dataSize);
                    } else {
                        if (i == 0) {
                            cutList = rollingDataList.subList(dataSizeEveryThread * i, dataSizeEveryThread * (i + 1));
                        } else {
                            cutList = rollingDataList.subList(dataSizeEveryThread * i - 1, dataSizeEveryThread * (i + 1));
                        }
                    }
                    final List<RollingData> listRollingData = cutList;
                    task = new CalculateGridForZhuangForReportTieLu();
                    ((CalculateGridForZhuangForReportTieLu) task).setRollingDataRange(rollingDataRange);
                    ((CalculateGridForZhuangForReportTieLu) task).setRollingDataList(listRollingData);
                    ((CalculateGridForZhuangForReportTieLu) task).setTableName(tableName);
                    ((CalculateGridForZhuangForReportTieLu) task).setxNum(xSize);
                    ((CalculateGridForZhuangForReportTieLu) task).setyNum(ySize);
                    ((CalculateGridForZhuangForReportTieLu) task).setTempmatrix(matrix);
                    ((CalculateGridForZhuangForReportTieLu) task).setTransUtils(transUtils);
                    // 这里提交的任务容器列表和返回的Future列表存在顺序对应的关系
                    tasks.add(task);
                }
                try {
                    List<Future<Integer>> results = exec.invokeAll(tasks);
                    for (Future<Integer> future : results) {
                        future.get().toString();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                // 关闭线程池
                exec.shutdown();

                log.info("线程任务执行结束");
            }
            //根据补录内容 将仓对应区域的碾压遍数、速度、压实度更新
            if (repairDataList != null) {
                setRepairData(repairDataList, matrix, rollingDataRange);
            }

            log.error("生成网格遍数执行任务消耗了 ：" + (System.currentTimeMillis() - start) + "毫秒");
            // matrix= null;
        }


        RollingResult rollingResult = new RollingResult();
        //超限
        RollingResult rollingResultSpeed = new RollingResult();
        //动静碾压
        RollingResult rollingResultVibration = new RollingResult();
        int count0 = 0;
        int count0Speed = 0;
        int count0Vibration = 0;
        String bsae64_string = "";
        String bsae64_string_speed = "";
        String bsae64_string_vibration = "";
        TColorConfig vo = new TColorConfig();
        JSONObject result = new JSONObject();
        vo.setType(3L);//超限
        List<TColorConfig> colorConfigs = colorConfigMapper.select(vo);
        vo.setType(6L);//动静碾压
        List<TColorConfig> colorConfigs6 = colorConfigMapper.select(vo);
        double[][] rollingtimes2 = new double[gridysize][gridxsize];
        synchronized (obj1) {// 同步代码块
            //绘制图片
            //得到图片缓冲区

            //超限次数
            List<Coordinate> list = JSONArray.parseArray(damsConstruction.getRanges(), Coordinate.class);
            Coordinate[] pointList = new Coordinate[list.size() + 1];//最重要，不能遗漏，预先申请好数组空间
            list.toArray(pointList);
            pointList[list.size()] = pointList[0];
            //记录遍数
            GeometryFactory gf = new GeometryFactory();
            Geometry edgePoly = gf.createPolygon(pointList);

            Coordinate[] allcors = edgePoly.getCoordinates();

            int[] xlist = new int[allcors.length];
            int[] ylist = new int[allcors.length];
            for (int i = 0; i < allcors.length; i++) {
                Coordinate one2 = allcors[i];
                Double x = one2.getOrdinate(0) - rollingDataRange.getMinCoordX();
                Double y = one2.getOrdinate(1) - rollingDataRange.getMinCoordY();
                xlist[i] = (int) Math.round(x);
                ylist[i] = (int) Math.round(y);

            }


            long startTime = System.currentTimeMillis();
            Map<Integer, Color> colorMap = getColorMap(1L);

            int designPassCount = damsConstruction.getFrequency();
            float designSpeed = damsConstruction.getSpeed().floatValue();
            double designVibration = 0d;
            List<Long> timelist = new LinkedList<>();
            List<Double> allvcv = new LinkedList<>();
            for (int i = 0; i < xSize; i++) {
                for (int j = 0; j < ySize; j++) {
                    //计算边界的网格数量
                    Double xTmp = rollingDataRange.getMinCoordX() + i * division;
                    Double yTmp = rollingDataRange.getMinCoordY() + j * division;
                    //判断点是否在多边形内
                    Coordinate point = new Coordinate(xTmp, yTmp);
                    PointLocator a = new PointLocator();
                    boolean p1 = a.intersects(point, edgePoly);
                    if (p1) {

                        int xgrid = new BigDecimal(i / gridxstep).setScale(0, RoundingMode.HALF_DOWN).intValue();
                        int ygrid = new BigDecimal(j / gridystep).setScale(0, RoundingMode.HALF_DOWN).intValue();
                        try {

                            if (xgrid > gridxsize - 1) {
                                xgrid = gridxsize - 1;
                            }
                            if (ygrid > gridysize - 1) {
                                ygrid = gridysize - 1;
                            }
                            if (null == matrix[i][j].getVibrateValueList() || matrix[i][j].getVibrateValueList().size() == 0) {
                                matrixItemGrids[xgrid][ygrid].getVcvvalue().add(0.0);
                                timelist.add(0l);
                            } else {

                                Double avgvib = matrix[i][j].getVibrateValueList().get(rollingtimes - 1);
                                matrixItemGrids[xgrid][ygrid].getVcvvalue().add(avgvib);
                                timelist.add(matrix[i][j].getTimestampList().get(rollingtimes - 1));
                            }
                        } catch (Exception e) {
                            timelist.add(0l);
                            matrixItemGrids[xgrid][ygrid].getVcvvalue().add(0.0);
                        }

                    }
                }
            }

            try {
                timelist = timelist.stream().filter((e) -> {
                    //(中间操作)所有的中间操作不会做任何的处理
                    if (null == e) {
                        return false;
                    } else
                        return e > 0;
                }).collect(Collectors.toList());
                Long endtime = timelist.stream().mapToLong(Long::longValue).max().getAsLong();
                Long begintime = timelist.stream().mapToLong(Long::longValue).min().getAsLong();

                Date begidate = new Date(begintime);
                Date enddate = new Date(endtime);
                String begidates = DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", begidate);
                String enddates = DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", enddate);
                result.put("begidate", begidates);
                result.put("enddate", enddates);
            } catch (Exception e) {
                e.printStackTrace();
            }


            int passarea = 0;
            int countarea = 0;
            String targetv = damsConstruction.getFreedom4() == null ? "70" : damsConstruction.getFreedom4();
            Double biaozhunvcv = Double.valueOf(targetv);
            for (int j = 0; j < gridysize; j++) {
                for (int i = 0; i < gridxsize; i++) {
                    //   countarea++;
                    if (matrixItemGrids[i][j].getVcvvalue().size() == 0) {
                        rollingtimes2[j][i] = 0;
                    } else {

                        double vcvvalue = 0;
                        if (matrixItemGrids[i][j].getVcvvalue().size() < rollingtimes) {
                            vcvvalue = 0;
                        } else {
                            countarea++;
                            vcvvalue = matrixItemGrids[i][j].getVcvvalue().get(rollingtimes - 1);
                        }
                        allvcv.add(vcvvalue);
                        rollingtimes2[j][i] = vcvvalue;
                        if (vcvvalue >= biaozhunvcv) {
                            passarea++;
                        }
                    }
                }
            }

            double minvcv = allvcv.stream().min(Comparator.comparingDouble(Double::doubleValue)).get();
            double maxvcv = allvcv.stream().max(Comparator.comparingDouble(Double::doubleValue)).get();
            double avgvcv = allvcv.stream().mapToDouble(Double::doubleValue).average().getAsDouble();

            double jicha = new BigDecimal(maxvcv - minvcv).setScale(2, RoundingMode.HALF_DOWN).doubleValue();
            double sumfangcha = 0;
            for (Double aDouble : allvcv) {
                sumfangcha += Math.pow((aDouble - avgvcv), 2);
            }

            double fangcha = sumfangcha / allvcv.size();
            double biaozhuncha = new BigDecimal(Math.sqrt(fangcha)).setScale(2, RoundingMode.HALF_DOWN).doubleValue();
            double bianyi = new BigDecimal(biaozhuncha / avgvcv).setScale(2, RoundingMode.HALF_DOWN).doubleValue();

            double pass = new BigDecimal((passarea * 1.0) / countarea).setScale(2, RoundingMode.HALF_DOWN).doubleValue() * 100;

            double passaread = passarea * 2 * 0.5;
            double finalarea = countarea * 2 * 0.5;
            result.put("targetvalue", biaozhunvcv);
            result.put("normalvalue", "");
            result.put("workfrequency", "");
            result.put("maxvalue", maxvcv);
            result.put("minvalu", minvcv);
            result.put("jicha", jicha);
            result.put("avg", avgvcv);
            result.put("biaozhun", biaozhuncha);
            result.put("xishu", bianyi);
            result.put("pass", pass + "%");
            result.put("passarea", passaread + "㎡");
            result.put("finalarea", finalarea + "㎡");


            //count0表示所有属于当前单元工程的轨迹点 time0表示在计算完遍数后，该单元工程内没有碾压的数据
            int time0 = count0 - rollingResult.getTime1() - rollingResult.getTime2() - rollingResult.getTime3() - rollingResult.getTime4() - rollingResult.getTime5() - rollingResult.getTime6() - rollingResult.getTime7() - rollingResult.getTime8()
                    - rollingResult.getTime9() - rollingResult.getTime10() - rollingResult.getTime11() - rollingResult.getTime11Up();
            //因为轨迹点使用缓冲区，所以会出现time0比0小的情况，这里要加一下判断如果小于0证明单元工程全部碾压到了
            if (time0 <= 0) {
                time0 = 0;
            }
            rollingResult.setTime0(time0);


            long endTime = System.currentTimeMillis();    //获取结束时间
            log.info("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间

        }

        ProjCoordinate projCoordinate1 = new ProjCoordinate(rollingDataRange.getMinCoordY(), rollingDataRange.getMinCoordX(), 10);
        ProjCoordinate projCoordinate2 = new ProjCoordinate(rollingDataRange.getMaxCoordY(), rollingDataRange.getMaxCoordX(), 10);
        result.put("width_bi", gridxsize);
        result.put("height_bi", gridysize);
        result.put("rollingResult", rollingResult);
        result.put("rollingResultSpeed", rollingResultSpeed);
        result.put("rollingResultVibration", rollingResultVibration);
        result.put("base64", bsae64_string);
        result.put("base64Speed", bsae64_string_speed);
        result.put("base64Vibration", bsae64_string_vibration);
        result.put("rollingDataRange", rollingDataRange);
        result.put("pointLeftBottom", projCoordinate1);
        result.put("pointRightTop", projCoordinate2);
        result.put("height", damsConstruction.getGaocheng());
        result.put("cenggao", damsConstruction.getCenggao());
        result.put("range", range);
        result.put("rollingtimes2", rollingtimes2);
        result.put("kuandu", pianju);
        //边界
        result.put("ranges", damsConstruction.getRanges());
        result.put("id", damsConstruction.getId());

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = null;
            }
        }
        matrix = null;
        return result;
    }


    public JSONObject unitreport_process(String tableName) throws InterruptedException, ExecutionException, IOException {
        String id = tableName;
        DamsConstruction damsConstruction = damsConstructionMapper.selectByPrimaryKey(Integer.valueOf(id));
        int rollingtimes = damsConstruction.getFrequency();
        int dmstatus = damsConstruction.getStatus();
        //工作仓 数据
        tableName = damsConstruction.getTablename();
        String range = tableName.split("_")[0] + "_" + tableName.split("_")[1];
        //1.根据工作仓初设化网格
        //直接根据桩号获得范围
        BigDecimal minOfxList = BigDecimal.valueOf(damsConstruction.getXbegin());
        BigDecimal maxOfxList = BigDecimal.valueOf(damsConstruction.getXend());
        BigDecimal minOfyList = BigDecimal.valueOf(damsConstruction.getYbegin());
        BigDecimal maxOfyList = BigDecimal.valueOf(damsConstruction.getYend());
        StorehouseRange storehouseRange = new StorehouseRange();
        storehouseRange.setMinOfxList(minOfxList.doubleValue());
        storehouseRange.setMaxOfxList(maxOfxList.doubleValue());
        storehouseRange.setMinOfyList(minOfyList.doubleValue());
        storehouseRange.setMaxOfyList(maxOfyList.doubleValue());
        RollingDataRange rollingDataRange = new RollingDataRange();
        rollingDataRange.setMaxCoordY(maxOfyList.doubleValue());
        rollingDataRange.setMinCoordY(minOfyList.doubleValue());
        rollingDataRange.setMaxCoordX(maxOfxList.doubleValue());
        rollingDataRange.setMinCoordX(minOfxList.doubleValue());
        int xSize = (int) Math.abs(damsConstruction.getXend() - damsConstruction.getXbegin());
        int ySize = (int) Math.abs(damsConstruction.getYend() - damsConstruction.getYbegin());

//        //获取图片旋转角度
//        float angele = getrotate(damsConstruction);

        //所有车

        List<Car> carList = carMapper.findCar();
        //判断工作仓数据是否存在
        boolean isHave = StoreHouseMap.getStoreHouses2RollingData().containsKey(tableName);
        MatrixItem[][] matrix = new MatrixItem[xSize][ySize];

        for (int i = 0; i < xSize; i++) {
            for (int j = 0; j < ySize; j++) {
                matrix[i][j] = new MatrixItem();
                matrix[i][j].setRollingTimes(0);
            }
        }

        shorehouseRange.put(id, storehouseRange);
        StoreHouseMap.getStoreHouses2RollingData().put(tableName, matrix);


        /**
         * 查询出当前仓位的补录区域
         */
        TRepairData record = new TRepairData();
        record.setDamsid(damsConstruction.getId());
        List<TRepairData> repairDataList = repairDataMapper.selectTRepairDatas(record);

        //2 获得数据库中的数据
        long start = System.currentTimeMillis();
        for (Car car : carList) {
            List<RollingData> rollingDataList = rollingDataMapper.getAllRollingDataByVehicleID(tableName, car.getCarID().toString());
            // 总数据条数
            int dataSize = rollingDataList.size();
            if (dataSize < 10) {
                continue;
            }
            if (dataSize == 0) {
                continue;
            }
            // 线程数 四个核心
            int threadNum = 1;
            // 余数
            int special = dataSize % threadNum;
            //每个线程处理的数据量
            int dataSizeEveryThread = dataSize / threadNum;
            // 创建一个线程池
            ExecutorService exec = Executors.newFixedThreadPool(threadNum);
            // 定义一个任务集合
            List<Callable<Integer>> tasks = new LinkedList<Callable<Integer>>();
            Callable<Integer> task = null;
            List<RollingData> cutList = null;

            // 确定每条线程的数据
            for (int i = 0; i < threadNum; i++) {
                if (i == threadNum - 1) {
                    int index = dataSizeEveryThread * i - 1 < 0 ? 0 : dataSizeEveryThread * i - 1;
                    cutList = rollingDataList.subList(index, dataSize);
                } else {
                    if (i == 0) {
                        cutList = rollingDataList.subList(dataSizeEveryThread * i, dataSizeEveryThread * (i + 1));
                    } else {
                        cutList = rollingDataList.subList(dataSizeEveryThread * i - 1, dataSizeEveryThread * (i + 1));
                    }
                }
                final List<RollingData> listRollingData = cutList;
                task = new CalculateGridForZhuangForReport();
                ((CalculateGridForZhuangForReport) task).setRollingDataRange(rollingDataRange);
                ((CalculateGridForZhuangForReport) task).setRollingDataList(listRollingData);
                ((CalculateGridForZhuangForReport) task).setTableName(tableName);
                ((CalculateGridForZhuangForReport) task).setxNum(xSize);
                ((CalculateGridForZhuangForReport) task).setyNum(ySize);
                ((CalculateGridForZhuangForReport) task).setTempmatrix(matrix);
                // 这里提交的任务容器列表和返回的Future列表存在顺序对应的关系
                tasks.add(task);
            }
            List<Future<Integer>> results = exec.invokeAll(tasks);
            for (Future<Integer> future : results) {
                log.info(future.get().toString());
            }
            // 关闭线程池
            exec.shutdown();

            log.info("线程任务执行结束");
        }
        //根据补录内容 将仓对应区域的碾压遍数、速度、压实度更新
        if (repairDataList != null) {
            setRepairData(repairDataList, matrix, rollingDataRange);
        }

        log.info("生成网格遍数执行任务消耗了 ：" + (System.currentTimeMillis() - start) + "毫秒");

        //开始组装遍数数据
        JSONObject result = new JSONObject();

        Map<String, Object> data1 = tableMapper.selecttabledata(damsConstruction.getTablename());
        Integer carid = Integer.parseInt((String) data1.get("VehicleID"));
        Car car = carMapper.selectByPrimaryKey(carid);
        Double xbegin = (Double) data1.get("xbegin");
        Double xend = (Double) data1.get("xend");
        BigDecimal xbgeinb = new BigDecimal(xbegin).setScale(2, RoundingMode.HALF_UP);
        BigDecimal xendb = new BigDecimal(xend).setScale(2, RoundingMode.HALF_UP);
        Float beging = (Float) data1.get("gaocheng_bottom");
        Float endg = (Float) data1.get("gaocheng_top");
        Float houdu = endg - beging;
        houdu = new BigDecimal(houdu * 100).setScale(2, RoundingMode.HALF_DOWN).floatValue();
        String type = damsConstruction.getTypes() == 1 ? "机械填筑" : "人工填筑";
        Material materil = materialMapper.selectByPrimaryKey(Integer.parseInt(damsConstruction.getMaterialname()));
        String materilname = materil.getMaterialname();

        Double ybegin = (Double) data1.get("ybegin");
        Double yend = (Double) data1.get("yend");
        double kuandu = new BigDecimal(yend - ybegin).setScale(2, RoundingMode.HALF_DOWN).doubleValue();
        result.put("projectname", "");
        result.put("beiginl", xbgeinb);
        result.put("cenghao", damsConstruction.getEngcode());
        result.put("endl", xendb);
        result.put("materilname", materilname);
        result.put("lunshu", "4");
        result.put("houdu", houdu);
        result.put("bianshu", rollingtimes);
        result.put("kuandu", kuandu);

        String datef = damsConstruction.getActualstarttime().substring(0, damsConstruction.getActualstarttime().indexOf(" "));
        result.put("date", datef);

        result.put("carname", car.getRemark());
        result.put("carfrq", "27/32");
        result.put("cartonnage", car.getTonnage());
        result.put("caram", "0");
        result.put("carspeed", "5KM/h");
        result.put("carvcv", "405/290");

        String targetval = damsConstruction.getFreedom4() == null ? "70" : damsConstruction.getFreedom4();
        double normalvalue = new BigDecimal(Double.valueOf(targetval) * 0.98).setScale(2, RoundingMode.HALF_DOWN).doubleValue();
        result.put("targetvalue", targetval);
        result.put("normalvalue", normalvalue);


        Map<String, Object> datamap = new HashMap<>();
        Map<String, Object> timemap = new HashMap<>();
        List<Long> r1 = new LinkedList<>();
        int itemcount = 0;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                MatrixItem item = matrix[i][j];
                List<Long> alltime = item.getTimestampList();

                try {
                    for (int ik = 0; ik < alltime.size(); ik++) {
                        itemcount++;
                        if (ik > rollingtimes - 1) {
                            break;
                        }
                        if (timemap.containsKey("time" + ik)) {
                            List<Long> oldtime = (List<Long>) timemap.get("time" + ik);
                            oldtime.add(item.getTimestampList().get(ik));
                            timemap.put("time" + ik, oldtime);

                            List<Double> oldvib = (List<Double>) datamap.get("vib" + ik);
                            try {
                                oldvib.add(item.getVibrateValueList().get(ik));
                                datamap.put("vib" + ik, oldvib);
                            } catch (Exception e) {
                                oldvib.add(0.0);
                                datamap.put("vib" + ik, oldvib);
                            }

                        } else {
                            List<Long> newtime = new LinkedList<>();
                            newtime.add(item.getTimestampList().get(ik));
                            timemap.put("time" + ik, newtime);
                            List<Double> newvib = new LinkedList<>();
                            newvib.add(item.getVibrateValueList().get(ik));
                            datamap.put("vib" + ik, newvib);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        List<Map<String, Object>> datalist = new LinkedList<>();
        List<Double> allavgvcv = new LinkedList<>();
        for (String ob : timemap.keySet()) {
            Map<String, Object> temp = new HashMap<>();
            List<Long> alltimelist = (List<Long>) timemap.get(ob);
            List<Long> sortlist = alltimelist.stream().sorted().collect(Collectors.toList());
            Long begin = sortlist.get(0);
            Date begind = new Date(begin);
            String begins = DateUtils.parseDateToStr("HH:mm:ss", begind);
            Long end = sortlist.get(sortlist.size() - 1);
            Date endd = new Date(end);
            String ends = DateUtils.parseDateToStr("HH:mm:ss", endd);
            temp.put("bianshu", Integer.parseInt(ob.substring(ob.indexOf("time") + 4)) + 1);
            temp.put("times", begins + "-" + ends);
            List<Double> allviblist = (List<Double>) datamap.get(ob.replace("time", "vib"));
            List<Double> sortlist2 = allviblist.stream().sorted().collect(Collectors.toList());
            Double min = sortlist2.get(0);
            Double max = sortlist2.get(sortlist.size() - 1);
            Double avg = allviblist.stream().collect(Collectors.averagingDouble(Double::doubleValue));
            avg = new BigDecimal(avg).setScale(2, RoundingMode.HALF_DOWN).doubleValue();
            long passcount = allviblist.stream().filter((e) -> {
                //(中间操作)所有的中间操作不会做任何的处理
                return e >= Double.valueOf(targetval);
            }).count();

            double sumfangcha = 0;
            for (double aDouble : allviblist) {
                sumfangcha += Math.pow((aDouble - avg), 2);
            }
            allavgvcv.add(avg);

            double fangcha = sumfangcha / allviblist.size();
            double biaozhuncha = new BigDecimal(Math.sqrt(fangcha)).setScale(2, RoundingMode.HALF_DOWN).doubleValue();
            double bianyi = new BigDecimal(biaozhuncha / avg).setScale(2, RoundingMode.HALF_DOWN).doubleValue();

            double pass = new BigDecimal(passcount * 100.0 / allviblist.size()).setScale(2, RoundingMode.HALF_DOWN).doubleValue();

            temp.put("max", max);
            temp.put("min", min);
            temp.put("avg", avg);
            temp.put("ratio", bianyi);
            temp.put("junyun", "1");
            temp.put("wending", "1");
            temp.put("pass", pass + "%");

            datalist.add(temp);

        }

        double carvcv = allavgvcv.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
        carvcv = new BigDecimal(carvcv).setScale(2, RoundingMode.HALF_DOWN).doubleValue();


        List<Map<String, Object>> datalistsort = datalist.stream().sorted(Comparator.comparing(map -> Integer.parseInt(map.get("bianshu").toString()))).collect(Collectors.toList());


        Double finalarea = new BigDecimal(itemcount / 100.0).setScale(4, RoundingMode.HALF_DOWN).doubleValue();

        result.put("area", finalarea);

        result.put("datalist", datalistsort);
        matrix = null;
        return result;
    }


    public JSONObject unitreport_verification(String tableName) {
        String id = tableName;
        DamsConstruction damsConstruction = damsConstructionMapper.selectByPrimaryKey(Integer.valueOf(id));

        Map<String, Object> data1 = tableMapper.selecttabledata(damsConstruction.getTablename());
        JSONObject result = new JSONObject();

        Double xbegin = (Double) data1.get("xbegin");
        Double xend = (Double) data1.get("xend");
        BigDecimal xbgeinb = new BigDecimal(xbegin).setScale(2, RoundingMode.HALF_UP);
        BigDecimal xendb = new BigDecimal(xend).setScale(2, RoundingMode.HALF_UP);
        Float beging = (Float) data1.get("gaocheng_bottom");
        Float endg = (Float) data1.get("gaocheng_top");
        Float houdu = endg - beging;

        Integer carid = Integer.parseInt((String) data1.get("VehicleID"));
        Car car = carMapper.selectByPrimaryKey(carid);

        String type = damsConstruction.getTypes() == 1 ? "机械填筑" : "人工填筑";

        Material materil = materialMapper.selectByPrimaryKey(Integer.parseInt(damsConstruction.getMaterialname()));
        String materilname = materil.getMaterialname();

        String craft = "";
        String freedom1 = "";
        //查询区域的试验报告
        List<TDamsconstrctionReportDetail> allreportpoint = new LinkedList<>();
        TDamsconstructionReport treport = new TDamsconstructionReport();
        treport.setDamgid(Long.valueOf(damsConstruction.getId()));
        List<TDamsconstructionReport> allreport = tDamsconstructionReportService.selectTDamsconstructionReportList(treport);
        Double bsumx = 0.0;
        Double bsumy = 0.0;
        if (allreport.size() > 0) {
            for (TDamsconstructionReport tDamsconstructionReport : allreport) {

                tDamsconstructionReport = tDamsconstructionReportService.selectTDamsconstructionReportByGid(tDamsconstructionReport.getGid());
                freedom1 = tDamsconstructionReport.getFreedom1();
                List<TDamsconstrctionReportDetail> details = tDamsconstructionReport.gettDamsconstrctionReportDetailList();
                allreportpoint.addAll(details);
            }
        }


        try {
            //首先计算平均数
            for (TDamsconstrctionReportDetail t : allreportpoint) {
                bsumx += Double.valueOf(t.getParam1());
                bsumy += Double.valueOf(t.getParam4());
            }
            Double avgx = bsumx / allreport.size();
            Double avgy = bsumy / allreport.size();

            //计算系数
            List<Double> listx = new LinkedList<>();
            List<Double> listy = new LinkedList<>();
            List<Double> lista_ = new LinkedList<>();
            Double sumx = 0.0;
            Double sumy = 0.0;
            Double sumx_ = 0.0;
            for (TDamsconstrctionReportDetail t : allreportpoint) {
                Double xi = Double.valueOf(t.getParam1());
                Double yi = Double.valueOf(t.getParam4());
                Double x = (xi - avgx) * (yi - avgy);
                Double y = Math.pow(xi - avgx, 2) * Math.pow(yi - avgy, 2);
                Double x_ = Math.pow(xi - avgx, 2);
                sumx += x;
                sumy += y;
                sumx_ += x_;
            }
            //最后计算系数r
            double r = new BigDecimal(sumx / Math.sqrt(sumy)).setScale(2, RoundingMode.HALF_DOWN).doubleValue();
            //计算回归方程的 a b 值
            double b = new BigDecimal(sumx / sumx_).setScale(2, RoundingMode.HALF_DOWN).doubleValue();
            double a = new BigDecimal(avgy - b * (avgx)).setScale(2, RoundingMode.HALF_DOWN).doubleValue();

            result.put("r", r);
            result.put("a", a);
            result.put("b", b);
            result.put("n", allreportpoint.size());
        } catch (NumberFormatException e) {
            log.error("计算系数出现错误。");
        }

        result.put("sno", damsConstruction.getTitle() + "SN01");
        result.put("licheng", xbgeinb + "至" + xendb);
        result.put("cartype", car.getCarType());
        result.put("projectname", "");
        result.put("houdu", houdu);
        result.put("craft", "");
        result.put("type", type);
        result.put("device", "");
        result.put("freedom1", freedom1);
        result.put("datalist", allreportpoint);


        return result;
    }


    public void setRepairData(List<TRepairData> repairDataList, MatrixItem[][] matrix, RollingDataRange rollingDataRange) {
        for (TRepairData repairData : repairDataList) {
            String ranges = repairData.getRanges();
            int passCount = repairData.getColorId();
            float speed = repairData.getSpeed().floatValue();
            double vibration = repairData.getVibration();

            List<Coordinate> repairRange = JSONArray.parseArray(ranges, Coordinate.class);
            Pixel[] repairPolygon = new Pixel[repairRange.size()];
            for (int i = 0; i < repairRange.size(); i++) {
                Coordinate coordinate = repairRange.get(i);
                Pixel pixel = new Pixel();
                pixel.setX((int) coordinate.x);
                pixel.setY((int) coordinate.y);
                repairPolygon[i] = pixel;
            }
            List<Pixel> rasters = Scan.scanRaster(repairPolygon);
            int bottom = (int) (rollingDataRange.getMinCoordY() * 1);
            int left = (int) (rollingDataRange.getMinCoordX() * 1);
            int width = (int) (rollingDataRange.getMaxCoordX() - rollingDataRange.getMinCoordX());
            int height = (int) (rollingDataRange.getMaxCoordY() - rollingDataRange.getMinCoordY());
            int n = 0;
            int m = 0;
            for (Pixel pixel : rasters) {
                try {
                    n = (pixel.getY() - bottom);
                    m = (pixel.getX() - left);
                    if (n >= 0 && m >= 0 && n < height && m < width) {
                        MatrixItem item = matrix[m][n];
                        if (item == null) {
                            item = new MatrixItem();
                            matrix[m][n] = item;
                        }
                        Double d = RandomUtiles.randomdouble(1, 10);
                        //   item.setRollingTimes(d.intValue());
                        item.setRollingTimes(passCount);
                        item.getSpeedList().add(speed);
                        item.getVibrateValueList().add(vibration);
                    }
                } catch (Exception ex) {
                    log.error("(" + m + "," + n + ")像素错误:" + ex.getMessage());
                }
            }
            rasters.clear();
            rasters = null;
        }

    }


    /**
     * 获取图形旋转为横向的旋转角度
     *
     * @param damsConstruction
     * @return
     */
    public float getrotate(DamsConstruction damsConstruction) {

        List<Coordinate> pointist = JSONArray.parseArray(damsConstruction.getRanges(), Coordinate.class);

        Comparator<? super Coordinate> comparatorxmax = Comparator.comparingDouble(new ToDoubleFunction<Coordinate>() {
            @Override
            public double applyAsDouble(Coordinate value) {
                return value.getOrdinate(0);
            }
        });

        Comparator<? super Coordinate> comparatorxmay = Comparator.comparingDouble(new ToDoubleFunction<Coordinate>() {
            @Override
            public double applyAsDouble(Coordinate value) {
                return value.getOrdinate(1);
            }
        });

        Optional<Coordinate> maxx = pointist.stream().max(comparatorxmax);
        Optional<Coordinate> maxy = pointist.stream().min(comparatorxmay);

        //找到坐标点中的 Y最大的点 和 X 最大的点作为 角度计算点。
        double dltaE = maxx.get().getOrdinate(0) - maxy.get().getOrdinate(0);
        double dltaN = maxx.get().getOrdinate(1) - maxy.get().getOrdinate(1);
        float angle = (float) Math.toDegrees(Math.atan2(dltaE, dltaN));
        float ss = 90 - angle > angle ? 90 - angle : angle;
        return 180 - ss;
    }


}
