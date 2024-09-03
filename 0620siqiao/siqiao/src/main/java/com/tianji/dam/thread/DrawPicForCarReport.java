package com.tianji.dam.thread;

import com.alibaba.fastjson.JSONObject;
import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.bean.GlobCache;
import com.tianji.dam.domain.MatrixItem;
import com.tianji.dam.domain.RollingResult;
import com.tianji.dam.domain.TColorConfig;
import com.tianji.dam.mapper.TColorConfigMapper;
import com.tianji.dam.utils.MapUtil;
import com.tianji.dam.utils.RidUtil;
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
import java.io.PrintStream;
import java.util.List;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 平面分析、按车辆查询
 */
@Slf4j
public class DrawPicForCarReport implements Callable<Integer> {

    ConcurrentHashMap<Long, MatrixItem[][]> cache;
    List<Integer> long2Cols;
    List<Integer> long2Rows;
    TColorConfigMapper colorConfigMapper;
    Session session;
    private String stock = "1123";
    private int cartype;
    private Float avgevolution;
    Map<Integer, Color> colorMap = new HashMap<>();
    List<TColorConfig> colorConfigs;
    List<TColorConfig> colorConfigs6;
    List<TColorConfig> colorConfigs44;

    public DrawPicForCarReport(ConcurrentHashMap<Long, MatrixItem[][]> cache, List<Integer> long2Cols,
                               List<Integer> long2Rows, Session session, int cartype, Float evolution, Map<Integer, Color> colorMap,
     List<TColorConfig> colorConfigs, List<TColorConfig> colorConfigs6, List<TColorConfig> colorConfigs44) {
        this.cache = cache;
        this.long2Cols = long2Cols;
        this.long2Rows = long2Rows;
        this.session = session;
        this.cartype = cartype;
        this.avgevolution = evolution;
         this.colorMap =colorMap;
         this.colorConfigs=colorConfigs;
        this.colorConfigs6=colorConfigs6;
        this.colorConfigs44=colorConfigs44;
    }

    @Override
    public Integer call() throws Exception {

        MatrixItem[][] matrixItems = null;
        MatrixItem item = null;
        JSONObject result = new JSONObject();
        try {
            if (long2Cols.size() != 0 && long2Rows.size() != 0) {
                //筛选出最大最小列
                int[] col = new int[long2Cols.size()];//列
                for (int coli = 0; coli < long2Cols.size(); coli++) {
                    if (StringUtils.isNotNull(long2Cols.get(coli))) {
                        col[coli] = long2Cols.get(coli);
                    }
                }
                int[] row = new int[long2Rows.size()];//行
                for (int rowi = 0; rowi < long2Rows.size(); rowi++) {
                    if (StringUtils.isNotNull(long2Rows.get(rowi))) {
                        row[rowi] = long2Rows.get(rowi);
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
                //超限
                BufferedImage biSpeed = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2Speed = (Graphics2D) biSpeed.getGraphics();
                //动静碾压
                BufferedImage biVibration = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);//INT精确度达到一定,RGB三原色，高度70,宽度150
                BufferedImage biEvolution = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);//INT精确度达到一定,RGB三原色，高度70,宽度150
                //动静碾压
                Graphics2D g2Vibration = (Graphics2D) biVibration.getGraphics();
                Graphics2D g2Evolution = (Graphics2D) biEvolution.getGraphics();

                int count0 = 0;
                int count0Speed = 0;
                int count0Vibration = 0;
                int count0Evolution = 0;
                String bsae64_string = "";
                String bsae64_string_speed = "";
                String bsae64_string_vibration = "";
                String bsae64_string_evolution = "";
                RollingResult rollingResult = new RollingResult();
                //超限
                RollingResult rollingResultSpeed = new RollingResult();
                //动静碾压
                RollingResult rollingResultVibration = new RollingResult();
                RollingResult rollingResultEvolution = new RollingResult();
                for (Object key : cache.keySet()) {
                    // 10*10 小方格
                    long rid = Long.valueOf(key.toString());
                    matrixItems = cache.get(rid);
                    int dltaX = RidUtil.long2Col(rid) * RidUtil.R_LEN - baseX;
                    int dltaY = RidUtil.long2Row(rid) * RidUtil.R_LEN - baseY;

                    if (cartype == 2) {
                        for (int i = 0; i < RidUtil.R_LEN - 1; i++) {
                            for (int j = 0; j < RidUtil.R_LEN - 1; j++) {
                                item = matrixItems[i][j];
                                if (item != null) {
                                    try {
                                    count0Evolution++;
                                    Float currentevolution = item.getCurrentEvolution() == null ? 0.0f : item.getCurrentEvolution().getLast();

                                        //evolution 平整度
                                        if (StringUtils.isNotNull(currentevolution)) {
                                            calculateRollingEvolution(currentevolution, rollingResultEvolution, colorConfigs44);
                                        }

                                        g2Evolution.setColor(getColorByCountEvolution(currentevolution, colorConfigs44));
                                        g2Evolution.fillRect(i, j, 1, 1);
                                        currentevolution = (currentevolution - avgevolution) * 100;
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

                        int time0Evolution = count0Evolution - rollingResultEvolution.getTime1();
                        if (time0Evolution <= 0) {
                            time0Evolution = 0;
                        }
                        rollingResultEvolution.setTime0(time0Evolution);
                    } else if (cartype == 1) {

                        for (int i = 0; i < RidUtil.R_LEN - 1; i++) {
                            for (int j = 0; j < RidUtil.R_LEN - 1; j++) {
                                item = matrixItems[i][j];
                                if (item != null) {
                                    count0++;
                                    count0Speed++;
                                    count0Vibration++;
                                    int rollingTimes = item.getRollingTimes();
                                    g2.setColor(getColorByCount2(rollingTimes));
                                    g2.fillRect(i + dltaX, j + dltaY, 2, 2);

                                    // TODO 超限次数
                                    //获得速度集合
                                    LinkedList<Float> speeds = item.getSpeedList();
                                    //根据速度集合匹配图例配置表？？？？？？
                                    g2Speed.setColor(getColorByCountSpeed(StringUtils.isNotEmpty(speeds) ? speeds.get(speeds.size() - 1) : new Float(-1), colorConfigs));
                                    calculateRollingSpeed(StringUtils.isNotEmpty(speeds) ? speeds.get(speeds.size() - 1) : new Float(-1), rollingResultSpeed, colorConfigs);
                                    g2Speed.fillRect(i + dltaX, j + dltaY, 2, 2);

                                    //Vibration 动静碾压
                                    LinkedList<Double> vibrations = item.getVibrateValueList() != null ? item.getVibrateValueList() : new LinkedList<>();
                                    //剔除null值 动静碾压
                                    LinkedList<Double> vibs = new LinkedList<>();
                                    for (Double vcv : vibrations) {
                                        //todo 动静碾压
                                        if (StringUtils.isNotNull(vcv)) {
                                            calculateRollingVibrate(vcv, rollingResultVibration, colorConfigs6);

                                            vibs.add(vcv);
                                        }
                                    }
                                    Double vibration = new Double(-1);

                                    if (StringUtils.isNotEmpty(vibs)) {
                                        if (StringUtils.isNotNull(vibs.get(0))) {
                                            try {
                                                vibration = Collections.max(vibs);
                                            } catch (NullPointerException e) {
                                               e.printStackTrace();
                                            }
                                        }
                                    }
                                    g2Vibration.setColor(getColorByCountVibrate(vibration, colorConfigs6));
//                        calculateRollingVibrate(vibration,rollingResultVibration,colorConfigs6);
                                    g2Vibration.fillRect(i + dltaX, j + dltaY, 2, 2);

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

                        //todo 超限
                        int time0Speed = count0Speed - rollingResultSpeed.getTime1();
                        if (time0Speed <= 0) {
                            time0Speed = 0;
                        }
                        rollingResultSpeed.setTime0(time0Speed);
                        //todo 动静碾压
                        int time0Vibration = count0Vibration - rollingResultVibration.getTime1();
                        if (time0Vibration <= 0) {
                            time0Vibration = 0;
                        }
                        rollingResultVibration.setTime0(time0Vibration);


                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        //超限
                        ByteArrayOutputStream baosSpeed = new ByteArrayOutputStream();
                        //动静碾压
                        ByteArrayOutputStream baosVibration = new ByteArrayOutputStream();
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
                            log.error(e.getMessage());
                        }
                    }

                }
                ProjCoordinate projLeftBottom = new ProjCoordinate(minRow * RidUtil.R_LEN, minCol * RidUtil.R_LEN, 10);
                result.put("pointLeftBottom", projLeftBottom);
                ProjCoordinate projRightTop = new ProjCoordinate(maxRow * RidUtil.R_LEN + RidUtil.R_LEN, maxCol * RidUtil.R_LEN + RidUtil.R_LEN, 10);
                result.put("pointRightTop", projRightTop);
                result.put("base64", bsae64_string);
                result.put("base64Speed", bsae64_string_speed);
                result.put("base64Vibration", bsae64_string_vibration);
                result.put("base64Evolution", bsae64_string_evolution);
                result.put("rollingResult", rollingResult);
                result.put("rollingResultSpeed", rollingResultSpeed);
                result.put("rollingResultVibration", rollingResultVibration);
                result.put("rollingResultEvolution", rollingResultEvolution);
                synchronized (stock) {
                    if (session.isOpen()) {
                        session.getBasicRemote().sendText(JSONObject.toJSONString(result));
                        for (Map.Entry<Long, MatrixItem[][]> entry : cache.entrySet()) {
                            long key = entry.getKey();
                            MatrixItem[][] value = entry.getValue();
                            value = null;
                            entry = null;
                        }
                    } else {
                    }
                }
                result = null;
            }

            item = null;
            matrixItems = null;

        } catch (IOException e) {
            log.error("出现错误");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            e.printStackTrace(new PrintStream(baos));
            String exception = baos.toString();
            log.error(exception);

        }
        return 1;

    }

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


    public Color getColorByCount2(Integer count) {

        return getColorByCount2(count, colorMap);
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

    public void getColorMap(Long type) {

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

    }

}
