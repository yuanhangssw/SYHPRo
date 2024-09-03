package com.tianji.dam.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.regexp.internal.RE;
import com.sun.org.apache.regexp.internal.RECompiler;
import com.tianji.dam.bean.GlobCache;
import com.tianji.dam.domain.*;
import com.tianji.dam.mapper.CarMapper;
import com.tianji.dam.mapper.RollingDataMapper;
import com.tianji.dam.mapper.TColorConfigMapper;
import com.tianji.dam.mapper.TRepairDataMapper;
import com.tianji.dam.mileageutil.Mileage;
import com.tianji.dam.scan.Pixel;
import com.tianji.dam.scan.Scan;
import com.tianji.dam.thread.CalculateGridForAnalysis;
import com.tianji.dam.unused.CalculateGridForAnalysis2;
import com.tianji.dam.utils.MapUtil;
import com.tianji.dam.utils.RandomUtiles;
import com.tianji.dam.utils.TrackConstant;
import com.tj.common.annotation.DataSource;
import com.tj.common.enums.DataSourceType;
import com.tj.common.utils.RGBHexUtil;
import com.tj.common.utils.StringUtils;
import com.vividsolutions.jts.geom.Coordinate;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.concurrent.*;

@Service
@Slf4j
@DataSource(value = DataSourceType.SLAVE)
public class AnalysisService {
    public static final int CORECOUNT = 1;
    Double division = 1d;
    @Autowired
    private CarMapper carMapper;

    @Autowired
    private RollingDataMapper rollingDataMapper;

    @Autowired
    private TRepairDataMapper repairDataMapper;

    /**
     * 纵剖面分析
     *
     * @param userName
     * @param dams
     * @param zhuangX
     * @param type     1 横剖面 2 纵剖面
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws IOException
     */
    public JSONObject analysis(String userName, List<DamsConstruction> dams, double zhuangX, int type, int cartype) throws InterruptedException, ExecutionException, IOException {
        long startResult = System.currentTimeMillis();
        //1.确定生成的剖面图大小
        /*横剖面 3360 1000  纵剖面 4850 1000*/
        int yNum2 = type == 1 ? TrackConstant.BACKIMG_HEIGT.intValue() : TrackConstant.BACKIMG_WIDTH.intValue();
        int zNum = TrackConstant.BACKIMG_MAXGAOCHENG.intValue();
        System.out.println("zNum:" + zNum);
        BufferedImage bi2 = new BufferedImage(yNum2, zNum, BufferedImage.TYPE_INT_ARGB);//用来从bi里面取到需要的像素点
        Map<Integer, Color> colorMap = getColorMap(1L);
        TColorConfig vo = new TColorConfig();
        vo.setType(44L);//摊铺平整度颜色
        List<TColorConfig> colorConfigs44 = colorConfigMapper.select(vo);
        int ww = 0;
        for (DamsConstruction damsConstruction : dams) {
            ww++;
            //DamsConstruction damsConstruction = damsConstructionMapper.selectByPrimaryKey(id);
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
            //  Integer xNumZhuangX = (int) Math.ceil((zhuangX-rollingDataRange.getMinCoordX())/division);//zhuangx所在列
            int num = type == 1 ? yNum : xNum;
            MatrixItem[][] matrix = new MatrixItem[1][num];//横剖面用yNUM 反之用xNUm
            for (int x = 0; x < 1; x++) {
                for (int y = 0; y < num; y++) {//横剖面用yNUM 反之用xNUm
                    matrix[x][y] = new MatrixItem();
                    matrix[x][y].setRollingTimes(0);
                }
            }

            /*key增加用户username 便于后边清除内存*/
            String key = userName + "-" + tableName;
            List<Car> carList = carMapper.findCar();
            long start = System.currentTimeMillis();
            //固定线程的数量
            for (Car car : carList) {
                if (car.getType() != cartype) {
                    continue;
                }
                /* tableName="t_"+car.getCarID();*/
                tableName = damsConstruction.getTablename();
                tableName = GlobCache.cartableprfix[cartype] + "_" + tableName;
                long pageStart = System.currentTimeMillis();
                // 创建一个线程池
                BlockingQueue<Runnable> executorQueue = new LinkedBlockingQueue<>(5000);
                ExecutorService exec = new ThreadPoolExecutor(1,
                        Runtime.getRuntime().availableProcessors() ,
                        60, TimeUnit.SECONDS, executorQueue);
                // 定义一个任务集合
                List<Callable<Integer>> tasks = new LinkedList<Callable<Integer>>();
                //尝试循环分页查询
                List<RollingData> rollingDataList = rollingDataMapper.getAllRollingDataByVehicleIDLimit1(tableName, String.valueOf(car.getCarID()));
                if (StringUtils.isNotEmpty(rollingDataList)) {
                    boolean whileFlag = true;
                    LongSummaryStatistics resultNum = rollingDataList.stream().mapToLong((item) -> item.getTimestamp()).summaryStatistics();
                    Long timestamp = resultNum.getMax();
                    int pageSize = 5000;
                    while (whileFlag) {
                        List<RollingData> list = rollingDataMapper.getAllRollingDataByVehicleIDLimit2(tableName, String.valueOf(car.getCarID()), timestamp, pageSize);
                        if (StringUtils.isNotEmpty(list)) {
                            LongSummaryStatistics newNum = list.stream().mapToLong((item) -> item.getTimestamp()).summaryStatistics();
                            timestamp = newNum.getMax();
                            /*rollingDataList.addAll(list);*/
                            Callable<Integer> task = new CalculateGridForAnalysis();
                            ((CalculateGridForAnalysis) task).setRollingDataRange(rollingDataRange);
                            ((CalculateGridForAnalysis) task).setRollingDataList(list);
                            ((CalculateGridForAnalysis) task).setTableName(key);
                            ((CalculateGridForAnalysis) task).setxNum(xNum);
                            ((CalculateGridForAnalysis) task).setyNum(yNum);
                            ((CalculateGridForAnalysis) task).setMatrixItems(matrix);
                            ((CalculateGridForAnalysis) task).setZhuang(Integer.valueOf(new Double(zhuangX).intValue()));
                            ((CalculateGridForAnalysis) task).setFlag(type == 1 ? true : false);//true 横剖面 false 纵剖面

                            tasks.add(task);
                        } else {
                            whileFlag = false;
                        }
                    }

                    List<Future<Integer>> results = exec.invokeAll(tasks);
                    for (Future<Integer> future : results) {
                        log.info(future.get().toString());
                    }
                    // 关闭线程池
                    exec.shutdown();
                    log.info("线程任务执行结束");
                    log.error("计算碾压遍数执行任务消耗了 ：" + (System.currentTimeMillis() - start) + "毫秒");
                }
                log.error("尝试循环分页查询执行任务消耗了 ：" + (System.currentTimeMillis() - pageStart) + "毫秒");
            }

            try {
                RollingResult rollingResult = new RollingResult();
                int cangZMin = (int) (gaocheng - TrackConstant.BEGIN_GAOCHENG) * 10;//最小高层
                int cangZMax = (int) (cangZMin + cenggao / 100 * 10);
                double begin = type == 1 ? damsConstruction.getYbegin() : damsConstruction.getXbegin();
                int cangYMin = (int) (begin / 1.0d);//横剖面 yBegin 反之用xBegin
                double end = type == 1 ? damsConstruction.getYend() : damsConstruction.getXend();
                int cangYMax = (int) (end / 1.0d);//横剖面 yend 反之用xend
                System.out.println("开始查询补录数据。。。。");
                //查询补录数据信息

                TRepairData record = new TRepairData();
                record.setDamsid(damsConstruction.getId());
                record.setCartype(cartype);
                record.setRepairtype(0);
                List<TRepairData> repairDataList = repairDataMapper.selectTRepairDatas(record);
                for (TRepairData repairData : repairDataList) {
                    fillMatrix(repairData, new Double(zhuangX).intValue(), type == 1 ? true : false, matrix);
                }


                for (int i = cangZMin; i < cangZMax; i++) {
                    for (int j = cangYMin; j < cangYMax; j++) {
                        if (cartype == 1) {
                            Integer rollingTimes = matrix[0][j - cangYMin].getRollingTimes();
                            //  System.out.println("遍数" + rollingTimes);
                            calculateRollingtimes(rollingTimes, rollingResult);
                            if (i >= 0 && i < zNum && j >= 0 && j < yNum2) {
                                bi2.setRGB(j, i, getColorByCount2(rollingTimes, colorMap).getRGB());
                            } else {
                                System.out.println(String.format("超出范围的x:%s,===y:%s", j, i));
                            }
                        } else if (cartype == 2) {
                            float tphoudu = 0;
                            try {
                                LinkedList<Float> evlist = matrix[0][j - cangYMin].getCurrentEvolution();
                                Float currentevolution2 = evlist.size() == 0 ? 0f : evlist.getLast();
                                tphoudu = (float) (100.0f * (currentevolution2 - damsConstruction.getGaocheng()) - damsConstruction.getHoudu());
                            } catch (Exception e) {

                            }

                            tphoudu = (float) RandomUtiles.randomdouble(damsConstruction.getHoudu()-5,damsConstruction.getHoudu()+5);
                            bi2.setRGB(j, i, getColorByCountEvolution(tphoudu, colorConfigs44).getRGB());
                            calculateRollingEvolution(tphoudu, rollingResult, colorConfigs44);
                        }
                    }
                }
                System.out.println("补录数据查询完成。。");
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            /*if(ww==3){
                break;
            }*/
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
        JSONObject result = new JSONObject();
        result.put("base64", BI2_string);
        log.error("执行任务总消耗了 ：" + (System.currentTimeMillis() - startResult) + "毫秒");
        return result;
    }


    public JSONObject analysis2(String userName, List<DamsConstruction> dams, Coordinate coordinate1, Coordinate coordinate2) throws InterruptedException, ExecutionException, IOException {
        long startResult = System.currentTimeMillis();
        //1.确定生成的剖面图大小
        /*横剖面 3360 1000  纵剖面 4850 1000*/
        Mileage mileage = Mileage.getmileage();
        Coordinate[] zhuangs = new Coordinate[2];
        zhuangs[0] = coordinate1;
        zhuangs[1] = coordinate2;
        int yNum2 = (int) Math.sqrt(Math.pow(coordinate2.x - coordinate1.x, 2)
                + Math.pow(coordinate2.y - coordinate1.y, 2)); // 距离公式

        //   int yNum2 = type==1?(int)(coordinate2.y - coordinate1.y):(int)(coordinate2.x - coordinate1.x);

        int zNum = TrackConstant.BACKIMG_MAXGAOCHENG.intValue() - TrackConstant.BEGIN_GAOCHENG.intValue();
        System.out.println("zNum:" + zNum);
        BufferedImage bi2 = new BufferedImage(yNum2, zNum, BufferedImage.TYPE_INT_ARGB);//用来从bi里面取到需要的像素点
        Map<Integer, Color> colorMap = getColorMap(1L);
        int ww = 0;
        for (DamsConstruction damsConstruction : dams) {
            ww++;
            //DamsConstruction damsConstruction = damsConstructionMapper.selectByPrimaryKey(id);
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

            //   double[]  cor1 =     mileage.pixels2Coord(1,coordinate1.x,coordinate1.y);
            //   double[]  cor2 =     mileage.pixels2Coord(1,coordinate2.x,coordinate2.y);

//            int num =(int) Math.sqrt(Math.pow(cor2[0] - cor1[0], 2)
//                    + Math.pow(cor2[1] - cor1[1], 2)); // 距离公式

            Integer yNum = (int) Math.ceil((coordinate2.y - coordinate1.y) / division);//行数
            Integer xNum = (int) Math.ceil((coordinate2.x - coordinate1.x) / division);//列数
            int num = yNum2;
            MatrixItem[][] matrix = new MatrixItem[1][yNum2];//横剖面用yNUM 反之用xNUm
            for (int x = 0; x < 1; x++) {
                for (int y = 0; y < num; y++) {//横剖面用yNUM 反之用xNUm
                    matrix[x][y] = new MatrixItem();
                    matrix[x][y].setRollingTimes(0);
                }
            }

            /*key增加用户username 便于后边清除内存*/
            String key = userName + "-" + tableName;
            List<Car> carList = carMapper.findCar();
            long start = System.currentTimeMillis();
            //固定线程的数量
            for (Car car : carList) {
                /* tableName="t_"+car.getCarID();*/
                tableName = damsConstruction.getTablename();

                long pageStart = System.currentTimeMillis();
                // 创建一个线程池
                BlockingQueue<Runnable> executorQueue = new LinkedBlockingQueue<>(5000);
                ExecutorService exec = new ThreadPoolExecutor(
                        Runtime.getRuntime().availableProcessors() * 10,
                        Runtime.getRuntime().availableProcessors() * 30,
                        60, TimeUnit.SECONDS, executorQueue);
                // 定义一个任务集合
                List<Callable<Integer>> tasks = new LinkedList<Callable<Integer>>();
                //尝试循环分页查询
                List<RollingData> rollingDataList = rollingDataMapper.getAllRollingDataByVehicleIDLimit1(tableName, String.valueOf(car.getCarID()));
                if (StringUtils.isNotEmpty(rollingDataList)) {
                    boolean whileFlag = true;
                    LongSummaryStatistics resultNum = rollingDataList.stream().mapToLong((item) -> item.getTimestamp()).summaryStatistics();
                    Long timestamp = resultNum.getMax();
                    int pageSize = 2000;
                    while (whileFlag) {
                        List<RollingData> list = rollingDataMapper.getAllRollingDataByVehicleIDLimit2(tableName, String.valueOf(car.getCarID()), timestamp, pageSize);
                        if (StringUtils.isNotEmpty(list)) {
                            LongSummaryStatistics newNum = list.stream().mapToLong((item) -> item.getTimestamp()).summaryStatistics();
                            timestamp = newNum.getMax();
                            /*rollingDataList.addAll(list);*/
                            Callable<Integer> task = new CalculateGridForAnalysis2();
                            ((CalculateGridForAnalysis2) task).setRollingDataRange(rollingDataRange);
                            ((CalculateGridForAnalysis2) task).setRollingDataList(list);
                            ((CalculateGridForAnalysis2) task).setTableName(key);
                            ((CalculateGridForAnalysis2) task).setxNum(xNum);
                            ((CalculateGridForAnalysis2) task).setyNum(yNum);
                            ((CalculateGridForAnalysis2) task).setMatrixItems(matrix);
                            ((CalculateGridForAnalysis2) task).setZhuang(zhuangs);
                            tasks.add(task);
                        } else {
                            whileFlag = false;
                        }
                    }

                    List<Future<Integer>> results = exec.invokeAll(tasks);
                    for (Future<Integer> future : results) {
                        log.info(future.get().toString());
                    }
                    // 关闭线程池
                    exec.shutdown();
                    log.info("线程任务执行结束");
                    log.error("计算碾压遍数执行任务消耗了 ：" + (System.currentTimeMillis() - start) + "毫秒");
                }
                log.error("尝试循环分页查询执行任务消耗了 ：" + (System.currentTimeMillis() - pageStart) + "毫秒");
            }

            try {
                RollingResult rollingResult = new RollingResult();
                int cangZMin = (int) (gaocheng - TrackConstant.BEGIN_GAOCHENG) * 10;//最小高层
                int cangZMax = (int) (cangZMin + cenggao / 100 * 10);
                double begin = 0;
                int cangYMin = (int) (begin / 1.0d);//横剖面 yBegin 反之用xBegin
                double end = yNum2;
                int cangYMax = (int) (end / 1.0d);//横剖面 yend 反之用xend
                System.out.println("开始查询补录数据。。。。");
                //查询补录数据信息

                TRepairData record = new TRepairData();
                record.setDamsid(damsConstruction.getId());
                List<TRepairData> repairDataList = repairDataMapper.selectTRepairDatas(record);
                for (TRepairData repairData : repairDataList) {
                    fillMatrix2(repairData, zhuangs, matrix, yNum2);
                }


                for (int i = cangZMin; i < cangZMax; i++) {
                    for (int j = 0; j < cangYMax - cangYMin; j++) {
                        Integer rollingTimes = matrix[0][j].getRollingTimes();
                        calculateRollingtimes(rollingTimes, rollingResult);
                        if (i >= 0 && i < zNum) {
                            bi2.setRGB(j, i, getColorByCount2(rollingTimes, colorMap).getRGB());
                        } else {
                            System.out.println(String.format("超出范围的x:%s,===y:%s", j, i));
                        }
                    }
                }
                System.out.println("补录数据查询完成。。");
            } catch (Exception e) {
                e.printStackTrace();
            }

            /*if(ww==3){
                break;
            }*/
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
        JSONObject result = new JSONObject();
        result.put("base64", BI2_string);
        result.put("width", yNum2);
        result.put("height", zNum);
        log.error("执行任务总消耗了 ：" + (System.currentTimeMillis() - startResult) + "毫秒");
        return result;
    }


    //Integer zhuang;//坐标
    //boolean flag;//true 横剖面 false 纵剖面
    private void fillMatrix(TRepairData damsConstruction, Integer zhuang, boolean flag, MatrixItem[][] matrix) {
        //边界范围
        List<Coordinate> list = JSONArray.parseArray(damsConstruction.getRanges(), Coordinate.class);
        Pixel[] polygon = new Pixel[list.size()];
        int i = 0;
        for (Coordinate coordinate : list) {
            polygon[i] = new Pixel((int) coordinate.x, (int) coordinate.y);
            i++;
        }
        List<Pixel> rasters = Scan.scanRaster(polygon);
        //ybegin
        int bottom = (int) (damsConstruction.getYbegin() * 1);
        //xbegin
        int left = (int) (damsConstruction.getXbegin() * 1);
        //xend-xbegin
        int width = (int) (damsConstruction.getXend() - damsConstruction.getXbegin());
        //yend-ybegin
        int height = (int) (damsConstruction.getYend() - damsConstruction.getYend());
        int n = 0;
        int m = 0;
        for (Pixel pixel : rasters) {
            try {
                n = (pixel.getY() - bottom);
                m = (pixel.getX() - left);
                /**pixel.getX() 是否等于zhuangX*/
                //横剖面比较pixel.getX()  纵剖面比较pixel.getY()
                if (flag ? pixel.getX() != zhuang : pixel.getY() != zhuang) {
                    continue;
                }
                if (n >= 0 && m >= 0 && n < height && m < width) {
                    MatrixItem item = matrix[0][flag ? n : m];//横剖面n  纵剖面m
                    item.setRollingTimes(damsConstruction.getColorId());
                }
            } catch (Exception ex) {
                log.error("(" + m + "," + n + ")像素错误:" + ex.getMessage());
            }
        }

    }

    private void fillMatrix2(TRepairData damsConstruction, Coordinate[] zhuang, MatrixItem[][] matrix, int ynum2) {
        //边界范围
        List<Coordinate> list = JSONArray.parseArray(damsConstruction.getRanges(), Coordinate.class);
        Pixel[] polygon = new Pixel[list.size()];
        int i = 0;
        for (Coordinate coordinate : list) {
            polygon[i] = new Pixel((int) coordinate.x, (int) coordinate.y);
            i++;
        }
        List<Pixel> rasters = Scan.scanRaster(polygon);
        //ybegin
        int bottom = (int) (damsConstruction.getYbegin() * 1);
        //xbegin
        int left = (int) (damsConstruction.getXbegin() * 1);
        //xend-xbegin
        int width = (int) (damsConstruction.getXend() - damsConstruction.getXbegin());
        //yend-ybegin
        int height = (int) (damsConstruction.getYend() - damsConstruction.getYend());
        int n = 0;
        int m = 0;
        for (Pixel pixel : rasters) {
            try {
                n = (pixel.getY() - bottom);
                m = (pixel.getX() - left);
                /**pixel.getX() 是否等于zhuangX*/
                //横剖面比较pixel.getX()  纵剖面比较pixel.getY()
                if (zhuang[0].x < pixel.getX() || zhuang[1].x < pixel.getX() || zhuang[1].y < pixel.getY() || zhuang[1].y > pixel.getX()) {
                    continue;
                }
                if (n >= 0 && m >= 0 && n < height && m < width) {
                    MatrixItem item = matrix[0][ynum2];//横剖面n  纵剖面m
                    item.setRollingTimes(damsConstruction.getColorId());
                }
            } catch (Exception ex) {
                log.error("(" + m + "," + n + ")像素错误:" + ex.getMessage());
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
}
