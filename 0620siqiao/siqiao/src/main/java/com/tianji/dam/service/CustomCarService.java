package com.tianji.dam.service;

import com.alibaba.fastjson.JSONObject;
import com.tianji.dam.domain.*;
import com.tianji.dam.mapper.T1Mapper;
import com.tianji.dam.mapper.TColorConfigMapper;
import com.tianji.dam.scan.*;
import com.tianji.dam.utils.MapUtil;
import com.tianji.dam.utils.PolygonUtils;
import com.tianji.dam.utils.RidUtil;
import com.tianji.dam.utils.TrackConstant;
import com.tj.common.annotation.DataSource;
import com.tj.common.enums.DataSourceType;
import com.tj.common.utils.RGBHexUtil;
import com.tj.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.osgeo.proj4j.ProjCoordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义车辆查询信息 服务层
 */
@Slf4j
@Service
@DataSource(value = DataSourceType.SLAVE)
public class CustomCarService {

    @Autowired
    private T1Mapper t1Mapper;


    private JSONObject draw(ConcurrentHashMap<Long, MatrixItem[][]> cache, List<Integer> long2Cols, List<Integer> long2Rows, T1 rollingData02, Map<Long, Long> rids) {
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
        //图例
        Map<Integer, Color> colorMap = getColorMap(1L);
        for (Map.Entry<Long, MatrixItem[][]> entry : cache.entrySet()) {
            // 10*10 小方格
            long rid = entry.getKey();
            matrixItems = entry.getValue();
            int dltaX = RidUtil.long2Col(rid) * RidUtil.R_LEN - baseX;
            int dltaY = RidUtil.long2Row(rid) * RidUtil.R_LEN - baseY;
            for (int i = 0; i < RidUtil.R_LEN - 1; i++) {
                for (int j = 0; j < RidUtil.R_LEN - 1; j++) {
                    item = matrixItems[i][j];
                    if (item != null) {
                        int rollingTimes = item.getRollingTimes();
                        g2.setColor(getColorByCount2(rollingTimes, colorMap));
                        g2.fillRect(i + dltaX, j + dltaY, 2, 2);
                    }
                }
            }

            //获取小格子的坐标x,y
//            int dltaX = RidUtil.long2Col(rid) * RidUtil.R_LEN
//            int dltaY = RidUtil.long2Row(rid) * RidUtil.R_LEN
        }

        /*for(Long rid :rids.keySet()){
            //get方式
            matrixItems = cache.get(rid);
            int dltaX = RidUtil.long2Col(rid) * RidUtil.R_LEN - baseX;
            int dltaY = RidUtil.long2Row(rid) * RidUtil.R_LEN - baseY;
            for (int i = 0; i < RidUtil.R_LEN - 1; i++) {
                for (int j = 0; j < RidUtil.R_LEN - 1; j++) {
                    item = matrixItems[i][j];
                    if(item != null) {
                        int rollingTimes = item.getRollingTimes();
                        g2.setColor(getColorByCount2(rollingTimes,colorMap));
                        g2.fillRect(i+dltaX, j+dltaY, 2, 2);
                    }
                }
            }
        }*/

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

        Double VibrateValue = 0D;//rollingData2.getVibrateValue();
        int iszhen = 0;
        if (VibrateValue > 40D) {
            iszhen = 1;
        }
        result.put("iszhen", iszhen);

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

        result.put("carId", "rollingData2.getVehicleID()");//carId
//        result.put("rollingResult",rollingResult); //检查前端是否使用了此参数
//        result.put("coverRate",coverRate); //检查前端是否使用了此参数
        result.put("title", "1车正在施工.");
        result.put("angle", rollingData02.getAngle());
//        System.out.println(result.toJSONString());
        System.out.println(bsae64_string);

        return result;
    }

    public JSONObject call(List<T1> t1s, ConcurrentHashMap<Long, MatrixItem[][]> cache, List<Integer> long2Cols, List<Integer> long2Rows) {
        if (t1s.size() > 2) {
            try {
                T1 rollingData01 = t1s.get(0);
                T1 rollingData02 = t1s.get(0);
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
                for (int i = 0; i < t1s.size(); i++) {
                    System.out.println("循环次数=========" + i);
                    rollingData02 = t1s.get(i);
                    double dis = Math.sqrt(Math.pow(rollingData01.getZhuangX() - rollingData02.getZhuangX(), 2) +
                            Math.pow(rollingData01.getZhuangY() - rollingData02.getZhuangY(), 2));
                    if (dis < TrackConstant.MIN_DIS) {
                        if (i + 1 >= t1s.size()) {
                            break;
                        }
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

                                    ConcurrentHashMap<Long, MatrixItem[][]> cacheData = new ConcurrentHashMap<>();
                                    Map<Long, Long> rids = new HashMap<>();
                                    List<Integer> cols = new LinkedList<>();//列
                                    List<Integer> rows = new LinkedList<>();//行
                                    //rasters 数据像素
                                    for (Pixel pixel : rasters) {
                                        try {
                                            //根据像素获得rid 也就是10*10小格子
                                            long rid = RidUtil.double2Long(pixel.getX(), pixel.getY());

                                            rids.put(rid, rid);
                                            cols.add(RidUtil.long2Col(rid));
                                            rows.add(RidUtil.long2Row(rid));

                                            int bottom = RidUtil.long2Bottom(rid);
                                            int left = RidUtil.long2Left(rid);
                                            n = (pixel.getY() - bottom);
                                            m = (pixel.getX() - left);
                                            MatrixItem[][] matrixItems = cache.get(rid);
                                            if (!StringUtils.isNotEmpty(matrixItems)) {
                                                //TODO:10*10 PIXEL为一个矩形块
                                                //根据Rid  获取 long2Col 列  long2Row 行
                                                /*long2Cols.add(RidUtil.long2Col(rid));
                                                long2Rows.add(RidUtil.long2Row(rid));*/
                                                cache.put(rid, new MatrixItem[RidUtil.R_LEN][RidUtil.R_LEN]);


                                                cacheData.put(rid, new MatrixItem[RidUtil.R_LEN][RidUtil.R_LEN]);
                                            }
                                            if (n >= 0 && m >= 0 && n < RidUtil.R_LEN && m < RidUtil.R_LEN) {
                                                MatrixItem item = matrixItems[m][n];
                                                if (item == null) {
                                                    item = new MatrixItem();
                                                }
                                                item.setRollingTimes(item.getRollingTimes() + 1);
                                                item.getElevationList().add(rollingData02.getElevation().floatValue());
                                                item.getAccelerationList().add(rollingData02.getAcceleration());
                                                item.getFrequencyList().add(rollingData02.getFrequency());
                                                item.getSpeedList().add(rollingData02.getSpeed().floatValue());
                                                item.getTimestampList().add(rollingData02.getTimestamp());
                                                item.getVehicleIDList().add(rollingData02.getVehicleID());
                                                Double vibrateValue = new Double(0);
                                                if (StringUtils.isNotNull(rollingData02.getVibrateValue())) {
                                                    vibrateValue = rollingData02.getVibrateValue();
                                                }
                                                item.getVibrateValueList().add(vibrateValue);
                                                if (rollingData02.getIsVibrate() == 1) {
                                                    //动碾遍数+1
                                                    item.setIsVibrate(item.getIsVibrate() + 1);
                                                } else if (rollingData02.getIsVibrate() == 0) {
                                                    //静碾遍数+1
                                                    item.setIsNotVibrate(item.getIsNotVibrate() + 1);
                                                }
                                                matrixItems[m][n] = item;

                                                cacheData.put(rid, matrixItems);
                                            }
                                        } catch (Exception ex) {
                                        }
                                    }
                                    rasters.clear();
                                    rollingData01 = rollingData02;
                                    mLastPt = data;

                                    /**
                                     * 绘制轨迹
                                     */
                                    /*draw( cache, long2Cols, long2Rows, rollingData02);*/
                                    return draw(cacheData, cols, rows, rollingData02, rids);

                                } else {
                                }
                            }
                        }
                    }

                }
            } catch (Exception ex) {
            }
        }
        return null;
    }

    //日期转换为时间戳 (精确到毫秒)
    public static long timeToStamp(String timers) {
        long timeStemp = 0;
        try {
            Date d = new Date();
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            d = sf.parse(timers);// 日期转换为时间戳
            timeStemp = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStemp;
    }

    public void cacheData(List<T1> t1s, ConcurrentHashMap<Long, MatrixItem[][]> cache, List<Integer> long2Cols, List<Integer> long2Rows) {
        if (t1s.size() > 2) {
            try {
                T1 rollingData01 = t1s.get(0);
                T1 rollingData02 = t1s.get(0);
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
                for (int i = 0; i < t1s.size(); i++) {
                    rollingData02 = t1s.get(i);
                    double dis = Math.sqrt(Math.pow(rollingData01.getZhuangX() - rollingData02.getZhuangX(), 2) +
                            Math.pow(rollingData01.getZhuangY() - rollingData02.getZhuangY(), 2));
                    if (dis < TrackConstant.MIN_DIS) {
                        if (i + 1 >= t1s.size()) {
                            break;
                        }
                        log.error("前后距离过小:" + dis);
                    } else if (dis > TrackConstant.MAX_DIS) {
                        log.error("前后距离过大:" + dis + " " + rollingData01.getOrderNum() + "-" + rollingData02.getOrderNum());
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
                                    for (Pixel pixel : rasters) {
                                        try {
                                            long rid = RidUtil.double2Long(pixel.getX(), pixel.getY());

                                            int bottom = RidUtil.long2Bottom(rid);
                                            int left = RidUtil.long2Left(rid);
                                            n = (pixel.getY() - bottom);
                                            m = (pixel.getX() - left);
                                            MatrixItem[][] matrixItems = cache.get(rid);
                                            if (!StringUtils.isNotEmpty(matrixItems)) {
                                                //TODO:10*10 PIXEL为一个矩形块
                                                //根据Rid  获取 long2Col 列  long2Row 行
                                                long2Cols.add(RidUtil.long2Col(rid));
                                                long2Rows.add(RidUtil.long2Row(rid));
                                                cache.put(rid, new MatrixItem[RidUtil.R_LEN][RidUtil.R_LEN]);
                                            }
                                            if (n >= 0 && m >= 0 && n < RidUtil.R_LEN && m < RidUtil.R_LEN) {
                                                MatrixItem item = matrixItems[m][n];
                                                if (item == null) {
                                                    item = new MatrixItem();
                                                }
                                                item.setRollingTimes(item.getRollingTimes() + 1);
                                                item.getElevationList().add(rollingData02.getElevation().floatValue());
                                                item.getAccelerationList().add(rollingData02.getAcceleration());
                                                item.getFrequencyList().add(rollingData02.getFrequency());
                                                item.getSpeedList().add(rollingData02.getSpeed().floatValue());
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
            }
        }
    }

    @Autowired
    private TColorConfigMapper colorConfigMapper;


    public Color getColorByCountSpeed(Float count, List<TColorConfig> colorConfigs) {
        for (TColorConfig color : colorConfigs) {
            //判断count 在哪个从和到之间
            if (count >= color.getC().floatValue() && count <= color.getD().floatValue()) {
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
            if (speed >= color.getC().floatValue() && speed <= color.getD().floatValue()) {
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

    public static Color getColorByCount2(Integer count, Map<Integer, Color> colorMap) {
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

}
