package com.tianji.dam.thread;

import com.alibaba.fastjson.JSONObject;
import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.bean.GlobCache;
import com.tianji.dam.domain.MatrixItem;
import com.tianji.dam.domain.RollingDataRange;
import com.tianji.dam.domain.TColorConfig;
import com.tianji.dam.domain.TDayTask;
import com.tianji.dam.mapper.TColorConfigMapper;
import com.tianji.dam.mapper.TDayTaskMapper;
import com.tianji.dam.utils.MapUtil;
import com.tianji.dam.utils.RidUtil;
import com.tj.common.utils.RGBHexUtil;
import com.tj.common.utils.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.osgeo.proj4j.ProjCoordinate;

import javax.imageio.ImageIO;
import javax.websocket.Session;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DrawPicBeforeRealTimeThread implements Runnable {

    ConcurrentHashMap<Long, MatrixItem[][]> cache;
    List<Integer> long2Cols;
    List<Integer> long2Rows;
    TColorConfigMapper colorConfigMapper;
    Session session;
    private Integer cartype;
    private Integer showtype;
    private String rediskey;
    Map<Integer, Color> colorMap = new HashMap<>();

    public DrawPicBeforeRealTimeThread(ConcurrentHashMap<Long, MatrixItem[][]> cache, List<Integer> long2Cols,
                                       List<Integer> long2Rows, Session session, Integer cartype, Integer showtype, String rediskey) {
        this.cache = cache;
        this.long2Cols = long2Cols;
        this.long2Rows = long2Rows;
        this.session = session;
        this.cartype = cartype;
        this.showtype = showtype;
        this.rediskey = rediskey;
    }

    @Override
    public void run() {

        MatrixItem[][] matrixItems;
        MatrixItem item;
        String bsae64_string;
        JSONObject result;
        Double baseevolution = 0.0d;
        Double basehoudu = 0.0d;
        TDayTaskMapper dayTaskMapper = BeanContext.getApplicationContext().getBean(TDayTaskMapper.class);
//TempRealCang_tpj_6b20390f64f1412eb771fcf7208cbc91_120259084329

        if (showtype == 2) {
            String[] rediss = rediskey.split("_");
            String freedom1 = rediss[2];

            if (GlobCache.daytaskbaseevolution.containsKey(freedom1)) {
                baseevolution = Double.valueOf(GlobCache.daytaskbaseevolution.get(freedom1).split(",")[0]);
                basehoudu = Double.valueOf(GlobCache.daytaskbaseevolution.get(freedom1).split(",")[1]);
            } else {
                TDayTask query = new TDayTask();
                query.setFreedom1(freedom1);
                List<TDayTask> all = dayTaskMapper.selectTDayTaskList(query);
                if (all.size() == 1) {
                    baseevolution = all.get(0).getBaseEvolution();
                    basehoudu = Double.valueOf(all.get(0).getFreedom3() == null ? "80" : all.get(0).getFreedom3());
                    GlobCache.daytaskbaseevolution.put(freedom1, baseevolution + "," + basehoudu);
                }
            }
        }

        if (null == colorConfigMapper) {
            colorConfigMapper = BeanContext.getApplicationContext().getBean(TColorConfigMapper.class);
        }
        List<TColorConfig> colorConfigs44 =new ArrayList<>();
        if(!GlobCache.typecolors.containsKey(44L)){
            TColorConfig vo = new TColorConfig();
            vo.setType(44l);//摊铺平整度颜色
             colorConfigs44 = colorConfigMapper.select(vo);
            GlobCache.typecolors.put(44l,colorConfigs44);
        }else{
            colorConfigs44 =GlobCache.typecolors.get(44L);
        }

        if(!GlobCache.rtimecolors.containsKey("rtimecolors"+cartype)){
            getColorMap(GlobCache.carcoloconfigtype[cartype].longValue());
            GlobCache.rtimecolors.put("rtimecolors"+cartype,colorMap);
        }else{
            colorMap = GlobCache.rtimecolors.get("rtimecolors"+cartype);
        }


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

                                if (cartype == 2) {

                                    if (showtype == 2) {

                                        float lastevolution = item.getCurrentEvolution() == null ? 0.0f : item.getCurrentEvolution().getLast();
                                        float currenthoudu = (lastevolution - baseevolution.floatValue()) * 100.0f;
                                        float laststatus = currenthoudu - basehoudu.floatValue();

                                        g2.setColor(getColorByCountEvolution(laststatus, colorConfigs44));
                                        g2.fillRect(i + dltaX, j + dltaY, 2, 2);

                                    } else {
                                        g2.setColor(new Color(254, 149, 206));
                                        g2.fillRect(i + dltaX, j + dltaY, 2, 2);

                                    }


                                } else if (cartype == 1) {
                                    int rollingTimes = item.getRollingTimes();
                                    g2.setColor(getColorByCount2(rollingTimes));
                                    g2.fillRect(i + dltaX, j + dltaY, 2, 2);

                                }

                            }
                        }
                    }

                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                ImageIO.write(bi, "PNG", baos);
                byte[] bytes = baos.toByteArray();//转换成字节
                bsae64_string = "data:image/png;base64," + Base64.encodeBase64String(bytes);
                baos.close();

                RollingDataRange rollingDataRange = new RollingDataRange();
                rollingDataRange.setMaxCoordX((double) maxCol);
                rollingDataRange.setMinCoordX((double) minCol);
                rollingDataRange.setMaxCoordY((double) maxRow);
                rollingDataRange.setMinCoordY((double) minRow);

                ProjCoordinate projLeftBottom = new ProjCoordinate(minRow * RidUtil.R_LEN, minCol * RidUtil.R_LEN, 10);
                ProjCoordinate projRightTop = new ProjCoordinate(maxRow * RidUtil.R_LEN + RidUtil.R_LEN, maxCol * RidUtil.R_LEN + RidUtil.R_LEN, 10);
                result = new JSONObject();
                result.put("base64", bsae64_string);
                result.put("pointLeftBottom", projLeftBottom);
                result.put("pointRightTop", projRightTop);
                String stock = "1123";
                synchronized (stock) {
                    if (session.isOpen()) {
                        session.getBasicRemote().sendText(JSONObject.toJSONString(result));

                    }
                }

            }



            cache = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        //  return result;

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
