package com.tianji.dam.thread;

import com.alibaba.fastjson.JSONObject;
import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.bean.GlobCache;
import com.tianji.dam.domain.MatrixItem;
import com.tianji.dam.domain.StoreHouseMap;
import com.tianji.dam.domain.T1;
import com.tianji.dam.domain.TColorConfig;
import com.tianji.dam.domain.vo.T1VO;
import com.tianji.dam.mapper.T1Mapper;
import com.tianji.dam.mapper.TColorConfigMapper;
import com.tianji.dam.mileageutil.Mileage;
import com.tianji.dam.scan.*;
import com.tianji.dam.utils.MapUtil;
import com.tianji.dam.utils.PolygonUtils;
import com.tianji.dam.utils.RidUtil;
import com.tianji.dam.utils.TrackConstant;
import com.tianji.dam.websocket.WebSocketServerForHistoryCar;
import com.tj.common.utils.DateUtils;
import com.tj.common.utils.RGBHexUtil;
import com.tj.common.utils.StringUtils;
import com.vividsolutions.jts.geom.Coordinate;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.osgeo.proj4j.ProjCoordinate;

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

/**
 * 自定义车辆
 */
@Slf4j
public class HistoryCarDataTaskBlockingList implements Runnable {


    @Setter
    @Getter
    private String key;
    @Setter
    @Getter
    private String GcarId;
    @Setter
    @Getter
    private Integer cartype;

    private boolean isbreak = false;

    public void breakthread() {
        isbreak = true;

    }

    WebSocketServerForHistoryCar webSocketServerForHistoryCar ;

    Map<Integer, Color> colorMap = new HashMap<>();

    @Setter
    @Getter
    private T1VO vo;

    @Setter
    public List<T1> alldata;


    @Override
    public void run() {
        webSocketServerForHistoryCar = BeanContext.getApplicationContext().getBean(WebSocketServerForHistoryCar.class);
        TColorConfigMapper colorConfigMapper = BeanContext.getApplicationContext().getBean(TColorConfigMapper.class);
        T1Mapper t1Mapper = BeanContext.getApplicationContext().getBean(T1Mapper.class);
        //筛选出车辆数据
        List<T1> t1s = alldata;
        TColorConfig colorvo = new TColorConfig();
        colorvo.setType(GlobCache.cartypecolorindex.get(GlobCache.cartableprfix[cartype]).longValue());//摊铺平整度颜色
        List<TColorConfig> colorConfigs44 = colorConfigMapper.select(colorvo);
        getColorMap(colorConfigMapper, cartype);
        if (StringUtils.isNotEmpty(t1s)) {
            boolean whileFlag = true;
            LongSummaryStatistics resultNum = t1s.stream().mapToLong(T1::getTimestamp).summaryStatistics();
            long timestamp = resultNum.getMax();
            int pageSize = 2000;
            while (whileFlag) {
                vo.setTimestamp(timestamp);
                vo.setPageSize(pageSize);
                vo.setTablename(GlobCache.cartableprfix[cartype] + "_t_1");
                List<T1> list = t1Mapper.selectPage(vo);
                if (StringUtils.isNotEmpty(list)) {
                    LongSummaryStatistics newNum = list.stream().mapToLong((item) -> item.getTimestamp()).summaryStatistics();
                    timestamp = newNum.getMax();
                    t1s.addAll(list);
                } else {
                    whileFlag = false;
                }
            }
        }
        //排个序
        t1s.stream().sorted(Comparator.comparing(T1::getTimestamp));
        double avgevolution = t1s.stream().filter(t1 -> t1.getCurrentEvolution() != null).collect(Collectors.averagingDouble(T1::getCurrentEvolution)).doubleValue();
        Mileage mileage =Mileage.getmileage();

        // 总数据条数
        int dataSize = t1s.size();
        if (dataSize > 2) {
            //开辟内存空间做缓存

            HashMap<String, T1> rollingData02Map = new HashMap();
            HashMap<String, Coordinate> coordinate1Map = new HashMap();
            HashMap<String, Coordinate> coordinate2Map = new HashMap();

            /*==================================*/
            try {

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
                    if (isbreak) {
                        break;
                    }

                    // System.out.println("计算中。。。");
                    // Thread.sleep(TrackConstant.INTERVAL);
                    // String datenow = DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss",new Date(rollingData.getTimestamp()));
                    //  System.out.println("车辆"+rollingData.getVehicleID()+"时间："+datenow);

                    T1 rollingData02 = t1s.get(i);
                    String  carId = rollingData02.getVehicleID();

                    double[] rd1=mileage.coord2pixel(rollingData02.getCoordRX(),rollingData02.getCoordRY());
                    double[] ld1= mileage.coord2pixel(rollingData02.getCoordLX(),rollingData02.getCoordLY());
                    double[] rd2= mileage.coord2pixel(rollingData02.getBeforeCoordLX(),rollingData02.getBeforeCoordLY());
                    double[] ld2= mileage.coord2pixel(rollingData02.getBeforeCoordRX(),rollingData02.getBeforeCoordRY());

                    if (ps == null) {
                        ps = new PointCpb[]{ps1, ps2, ps3, ps4};
                    }
                    ps1.x =rd1[0];
                    ps1.y =rd1[1];
                    ps2.x = ld1[0];
                    ps2.y =ld1[1];
                    ps3.x = rd2[0];
                    ps3.y = rd2[1];
                    ps4.x = ld2[0];
                    ps4.y =  ld2[1];
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
                        //   ConcurrentHashMap<Long, MatrixItem[][]> cacheData = new ConcurrentHashMap<>();
                        List<Integer> long2Cols = new LinkedList<>();//列
                        List<Integer> long2Rows = new LinkedList<>();//行
                        int lastrollingtimes = 0;

                        for (Pixel pixel : rasters) {
                            try {
                                ConcurrentHashMap<Long, MatrixItem[][]> cache = GlobCache.historycars.computeIfAbsent(key, k -> new ConcurrentHashMap<>());
                                long rid = RidUtil.double2Long(pixel.getX(), pixel.getY());
                                //  System.out.print(rid);
                                //  System.out.println(pixel);
                                int bottom = RidUtil.long2Bottom(rid);
                                int left = RidUtil.long2Left(rid);
                                n = (pixel.getY() - bottom);
                                m = (pixel.getX() - left);
                                MatrixItem[][] matrixItems = cache.get(rid);
                                if (!StringUtils.isNotEmpty(matrixItems)) {
                                    matrixItems = cache.get(rid);
                                    if (!StringUtils.isNotEmpty(matrixItems)) {
                                        matrixItems = new MatrixItem[RidUtil.R_LEN][RidUtil.R_LEN];
                                        //TODO:10*10 PIXEL为一个矩形块
                                        cache.put(rid, matrixItems);
                                        //  cacheData.put(rid, matrixItems);
                                    }
                                }
                                //根据Rid  获取 long2Col 列  long2Row 行
                                long2Cols.add(RidUtil.long2Col(rid));
                                long2Rows.add(RidUtil.long2Row(rid));
                                int bottomRid = RidUtil.long2Bottom(rid);
                                int leftRid = RidUtil.long2Left(rid);
                                int nRid = (pixel.getY() - bottomRid);
                                int mRid = (pixel.getX() - leftRid);
                                if (nRid >= 0 && mRid >= 0 && nRid < RidUtil.R_LEN && mRid < RidUtil.R_LEN) {

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
                                    // item.getVehicleIDList().add(rollingData02.getVehicleID());
                                    item.getCurrentEvolution().add(rollingData02.getCurrentEvolution());
                                    //  item.getBeforeElevation().add(rollingData02.getBeforeElevation());
                                    //  item.getZyangle().add(rollingData02.getZyangle());
                                    //  item.getQhangle().add(rollingData02.getQhangle());

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
                                    if (item.getRollingTimes() >= lastrollingtimes) {

                                        lastrollingtimes = item.getRollingTimes();
                                    }
                                    //  cacheData.put(rid, matrixItems);
                                    GlobCache.historycars.get(key).put(rid,matrixItems);
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                log.error("(" + m + "," + n + ")像素错误:" + ex.getMessage());
                            }
                        }

                        rasters.clear();

                        /*-------绘图*/
                        if (long2Cols.isEmpty() || long2Rows.isEmpty()) {
                            continue;
                        }
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
                        JSONObject result = new JSONObject();
                        List<Float> avgcurrentevolution = new ArrayList<>();
                        ConcurrentHashMap<Long, MatrixItem[][]> cache = GlobCache.historycars.get(key);
                        for (Map.Entry<Long, MatrixItem[][]> entry : cache.entrySet()) {

                            // 10*10 小方格
                            long rid = entry.getKey();
                            matrixItems = entry.getValue();
                            int dltaX = RidUtil.long2Col(rid) * RidUtil.R_LEN - baseX;
                            int dltaY = RidUtil.long2Row(rid) * RidUtil.R_LEN - baseY;

                            for (int ii = 0; ii <= RidUtil.R_LEN - 1; ii++) {
                                for (int j = 0; j <= RidUtil.R_LEN - 1; j++) {
                                    item = matrixItems[ii][j];
                                    if (item != null) {
                                        try {
                                            if (cartype == 1) {
                                                int rollingTimes = item.getRollingTimes();
                                                if (0 == rollingTimes) {
                                                    //   System.out.println(item);
                                                }
                                                g2.setColor(getColorByCount2(rollingTimes));
                                                int x = ii + dltaX;
                                                int y = j + dltaY;
                                                g2.fillRect(x, y, 2, 2);

                                            } else if (cartype == 2) {
                                                Float currente = (item.getCurrentEvolution().getLast() - (float) avgevolution) * 100;
                                                avgcurrentevolution.add(currente);
                                                g2.setColor(getColorByCountEvolution(currente, colorConfigs44));
                                                g2.fillRect(ii + dltaX, j + dltaY, 2, 2);

                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                        //  GlobCache.historycars.clear();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        try {
                            ImageIO.write(bi, "PNG", baos);
                            byte[] bytes = baos.toByteArray();//转换成字节
                            bsae64_string = "data:image/png;base64," + Base64.encodeBase64String(bytes);
                            baos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ProjCoordinate projLeftBottom = new ProjCoordinate(minRow * RidUtil.R_LEN, minCol * RidUtil.R_LEN, 10);
                        ProjCoordinate projRightTop = new ProjCoordinate(maxRow * RidUtil.R_LEN + RidUtil.R_LEN, maxCol * RidUtil.R_LEN + RidUtil.R_LEN, 10);


                        double cuavge = avgcurrentevolution.stream().filter(nf -> nf != 0).collect(Collectors.averagingDouble(Float::floatValue)).floatValue();

                        /*ProjCoordinate projRightTop = new ProjCoordinate(maxRow * RidUtil.R_LEN,maxCol * RidUtil.R_LEN,10);*/
                        result.put("pointLeftBottom", projLeftBottom);
                        result.put("vibrateValue",rollingData02.getVibrateValue());
                        result.put("pointRightTop", projRightTop);
                        result.put("avgpzd", cuavge);
                        result.put("base64", bsae64_string);
                        result.put("Latitude", rollingData02.getLatitude());
                        result.put("Longitude", rollingData02.getLongitude());

                        result.put("beforeCoordLX", rollingData02.getBeforeCoordLX());
                        result.put("beforeCoordLY", rollingData02.getBeforeCoordLY());
                        result.put("beforeCoordRX", rollingData02.getBeforeCoordRX());
                        result.put("beforeCoordRY", rollingData02.getBeforeCoordRY());

                        result.put("coordLX", rollingData02.getCoordLX());
                        result.put("coordLY", rollingData02.getCoordLY());
                        result.put("coordRX", rollingData02.getCoordRX());
                        result.put("coordRY", rollingData02.getCoordRY());

                        // result.put("index", index);//图片占的网格编号
                        result.put("iszhen", rollingData02.getIsForward());
                        int rate = 100;
                        result.put("rate", rate);//合格率
                        result.put("speed", rollingData02.getSpeed());//车辆速度
                        result.put("elevation", rollingData02.getElevation());//车辆高程
                        result.put("currentelevation", rollingData02.getCurrentEvolution());//车辆高程-新版
                        //时间格式化
                        String time = DateUtils.parseDateToStr("MM-dd HH:mm:ss", new Date(rollingData02.getTimestamp()));
                        result.put("timestamp", time);
                        result.put("zhuanghao", rollingData02.getZhuanghao());
                        result.put("pianju", rollingData02.getPianju());
                        result.put("lastrollingtime", lastrollingtimes - 1);
                        result.put("amplitude", rollingData02.getAmplitude());

                        result.put("base64", bsae64_string);

                        //result.put("base64ForVibrate",bsae64_string_ForVibrate);//增加震动影像
                        ProjCoordinate markPos = new ProjCoordinate(rollingData02.getZhuangY(), rollingData02.getZhuangX(), 10);
                        result.put("markPos", markPos);
                        result.put("carId", rollingData02.getVehicleID());
                        //result.put("coverRate",coverRate);//未知范围
                        result.put("title", "");
                        result.put("angle", rollingData02.getAngle());
                        result.put("coordx", rollingData02.getCoordX());
                        result.put("coordy", rollingData02.getCoordY());
                        Map<String, Session> users = webSocketServerForHistoryCar.getUsersMap();

                        //   System.out.println("当前会话："+users.keySet().size());
                        if (!users.isEmpty()) {
                            //遍历websocket连接池
                            for (String keyOfMap : users.keySet()) {
                                //获得websocket的storehouseID
                                String storehouseNameOfWebsocket = keyOfMap.split(":")[1];
                                String carIdOfWebsocket = keyOfMap.split(":")[0];
                                //如果storehouseID一致
                                if (storehouseNameOfWebsocket.equalsIgnoreCase(key) && carIdOfWebsocket.equalsIgnoreCase(GcarId)) {
                                    //如果storehouseID一致
                                    Session session = users.get(keyOfMap);
                                    synchronized (session) {
                                        try {
                                            session.getBasicRemote().sendText(JSONObject.toJSONString(result));

                                            //历史播放速度
                                            //历史播放速度
                                            Map<String, Long> historycarSpeed = StoreHouseMap.getHistorycarDataSpeed();
                                            /*控制推送速度*/
                                            Long millisecond = historycarSpeed.get(keyOfMap);
                                            //   System.out.println(millisecond);
                                            if (null != millisecond && millisecond.longValue() != 0) {
                                                Thread.currentThread().sleep(millisecond / 2);
                                            } else {
                                                Thread.currentThread().sleep(TrackConstant.INTERVAL / 2);
                                            }


                                        } catch (Exception e) {
                                            try {
                                                Thread.sleep(TrackConstant.INTERVAL / 2);
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

                    } else {
                        log.error("无法构建轨迹多边形");
                    }


                }

            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("计算错误:" + ex.getMessage());
            }


        }
        Map<String, Session> users = webSocketServerForHistoryCar.getUsersMap();
        //结束清除
        Iterator<Map.Entry<String, Session>> entries = users.entrySet().iterator();
        GlobCache.historycars.get(key).clear();
        while (entries.hasNext()) {
            Map.Entry<String, Session> entry = entries.next();
            String tablename = entry.getKey().split(":")[0];
            if (tablename.equalsIgnoreCase(key)) {
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

        t1s.clear();
        t1s = null;
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
