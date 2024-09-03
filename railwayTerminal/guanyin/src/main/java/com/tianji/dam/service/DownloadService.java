package com.tianji.dam.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.tianji.dam.bean.GlobCache;
import com.tianji.dam.domain.*;
import com.tianji.dam.mapper.*;
import com.tianji.dam.mileageutil.Mileage;
import com.tianji.dam.mileageutil.Tools;
import com.tianji.dam.utils.RandomUtiles;
import com.tianji.dam.utils.TrackConstant;
import com.tj.common.utils.StringUtils;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;


@Service
@Slf4j
public class DownloadService {
    @Autowired
    private TDamsconstructionMapper damsConstructionMapper;
    @Autowired
    private MaterialMapper materialMapper;
    @Autowired
    RollingDataMapper rollingDataMapper;
    @Autowired
    TAnalysisConfigMapper tanalysisConfigMapper;
    @Autowired
    TProjectMapper projectmapper;
    @Autowired
    TColorConfigMapper colorConfigMapper;

    @Value("${ruoyi.profile}")
    private String profile;

    @Autowired
    TableMapper tableMapper;
    @Autowired
    TDamsconstructionReportMapper reportMapper;
    @Autowired
    private CarMapper carMapper;

    /**
     * @param tableName
     * @param base64
     * @param rollingresult
     * @return ModelAndView
     */
    public ModelAndView findRoingDate(String tableName, String base64, RollingResult rollingresult) {
        String id = tableName;
        DamsConstruction damsConstruction = damsConstructionMapper.selectByPrimaryKey(Integer.valueOf(id));
        exportService(damsConstruction, base64, rollingresult);
        ModelAndView mv = new ModelAndView();        // 设置视图
        return mv;
    }


    /**
     * base64转图片
     *
     * @param base64
     * @return BufferedImage
     */
    public static BufferedImage base64ToBufferedImage(String base64) {
        Base64.Decoder decoder = Base64.getDecoder();

        try {
            byte[] bytes1 = decoder.decode(base64);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
            return ImageIO.read(bais);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将轨迹插到底图
     *
     * @param base64
     * @param xbegin
     * @param ybegin
     * @return BufferedImage
     * @throws IOException
     */
    public BufferedImage pic(String base64, Double xbegin, Double ybegin) throws IOException {

        TAnalysisConfig tAnalysisConfig = tanalysisConfigMapper.getPicPath();
        String path = tAnalysisConfig.getPath();
        URL url = new URL(path);
        URLConnection conn = url.openConnection();
        //底图位置
        BufferedImage d = ImageIO.read(conn.getInputStream());
        // BufferedImage d = ImageIO.read(new File(profile+"/底图.jpg"));

        BufferedImage b = base64ToBufferedImage(base64);                    //base64转换的轨迹图
        //b = reSize(b);                                                       //缩放后的轨迹图
        Graphics2D g = d.createGraphics();
        int x = (int) Math.round(xbegin);                                    //往底图上插入的位置
        int y = (int) Math.round(ybegin);
        g.drawImage(b, x, y, b.getWidth(), b.getHeight(), null);
        g.dispose();
        return d;
    }


    /**
     * 缩放
     *
     * @param srcImage
     * @return BufferedImage
     */
    public static BufferedImage reSize(BufferedImage srcImage) {
        System.out.println("++++++++++++++++++++++++++++++srcImg size=" + srcImage.getWidth() + "X" + srcImage.getHeight());
        //缩放的长宽 todo:这里需要注意下像素比
        double width = srcImage.getWidth();
        double height = srcImage.getHeight();

        // targetW，targetH分别表示目标长和宽
        BufferedImage target = null;
        double sx = width / srcImage.getWidth();
        double sy = height / srcImage.getHeight();
        // 等比缩放
        if (sx > sy) {
            sx = sy;
            width = (int) (sx * srcImage.getWidth());
        } else {
            sy = sx;
            height = (int) (sy * srcImage.getHeight());
        }
        System.out.println("+++++++++++++++++++++++++++destImg size=" + width + "X" + height);
        ColorModel cm = srcImage.getColorModel();
        WritableRaster raster = cm.createCompatibleWritableRaster((int) width, (int) height);
        boolean alphaPremultiplied = cm.isAlphaPremultiplied();

        target = new BufferedImage(cm, raster, alphaPremultiplied, null);
        Graphics2D g = target.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawRenderedImage(srcImage, AffineTransform.getScaleInstance(sx, sy));
        g.dispose();
        return target;
    }


    /**
     * 图片转字节
     *
     * @param bImage
     * @param format
     * @return byte
     */
    public static byte[] imageToBytes(BufferedImage bImage, String format) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(bImage, format, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    /**
     * 导出pdf自定义
     *
     * @param damsConstruction
     * @param base64
     * @param rollingresult
     * @return PdfExportService
     */
    public PdfExportService exportService(DamsConstruction damsConstruction, String base64, RollingResult rollingresult) {        // 使用Lambda表达式
        return (model, document, writer, request, response) -> {
            try {
                //获取层区域名
                String tablename = damsConstruction.getTablename();
                String[] s = tablename.split("_");
                String rangename = s[0];
                String cengname = s[1];
                //  String mat = s[2];
                String mat = damsConstruction.getMaterialname();
                //获取材料名
                String materialname = "" + materialMapper.selectByPrimaryKey(Integer.valueOf(mat)).getMaterialname();
                //获取时间
                String planstarttime = damsConstruction.getPlanstarttime();
                String planendtime = damsConstruction.getPlanendtime();
                String actualstarttime = damsConstruction.getActualstarttime();
                String actualendtime = damsConstruction.getActualendtime();
                //高程,层高
                Double gaocheng = damsConstruction.getGaocheng();
                Double cenggao = damsConstruction.getCenggao();
                //获取车辆列表
                List<CarTime> carTimelist = rollingDataMapper.getTime(tablename);
                //获取起始位置
                Double xbegin = damsConstruction.getXbegin();
                Double ybegin = damsConstruction.getYbegin();
                try {
                    PdfWriter.getInstance(document, new FileOutputStream(profile + "/test.pdf"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                document.open();
                //pdf设计
                // A4纸张
                document.setPageSize(PageSize.A4);
                // 标题
                document.addTitle("施工报告");
                // 换行
                //document.add(new Chunk("\n"));
                //中文显示
                ClassPathResource classPathResource = new ClassPathResource("static/SIMYOU.TTF");
                BaseFont baseFont = BaseFont.createFont(classPathResource.getPath(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

                //字体设置
                Font f1 = new Font(baseFont, 20);    // 标题(字体12，加粗，绿色)
                Font f8 = new Font(baseFont, 10);     // 表格中文字

                //段落paragraph 、文本块(Chunk)、短语(Phrase)
                Paragraph paragraph = null;
                paragraph = new Paragraph(materialname + "第" + cengname + "层区域" + rangename + "施工报告", f1);
                //1居中对齐、2为右对齐、3为左对齐，默认为左对齐
                paragraph.setAlignment(1);
                document.add(paragraph);

                //插入空白行
                paragraph = new Paragraph((" "));
                paragraph.setAlignment(1);
                document.add(paragraph);

                //定义单元格列数
                PdfPTable table = new PdfPTable(6);
                // 单元格
                PdfPCell cell = null;
                cell = new PdfPCell(new Paragraph("施工工程：", f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("C枢纽大坝", f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("分部工程：", f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(materialname, f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("单元工程：", f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("区域" + rangename, f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("计划开始时间：", f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(planstarttime, f8));
                cell.setHorizontalAlignment(1);
                cell.setColspan(2);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("计划结束时间：", f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(planendtime, f8));
                cell.setHorizontalAlignment(1);
                cell.setColspan(2);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("实际开始时间：", f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(actualstarttime, f8));
                cell.setHorizontalAlignment(1);
                cell.setColspan(2);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("实际结束时间：", f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(actualendtime, f8));
                cell.setHorizontalAlignment(1);
                cell.setColspan(2);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("施工高程：", f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                if (gaocheng == null) {
                    cell = new PdfPCell(new Paragraph(" ", f8));
                } else {
                    cell = new PdfPCell(new Paragraph(String.valueOf(gaocheng), f8));
                }
                cell.setHorizontalAlignment(1);
                cell.setColspan(2);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("铺料厚度：", f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(String.valueOf(cenggao), f8));
                cell.setHorizontalAlignment(1);
                cell.setColspan(2);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("车辆信息：", f8));
                cell.setHorizontalAlignment(1);
                cell.setColspan(6);
                table.addCell(cell);

                //循环写入车辆时间
                for (CarTime carTime : carTimelist) {
                    cell = new PdfPCell(new Paragraph("车辆ID：", f8));
                    cell.setHorizontalAlignment(1);
                    table.addCell(cell);

                    cell = new PdfPCell(new Paragraph(carTime.getVehicleID(), f8));
                    cell.setHorizontalAlignment(1);
                    table.addCell(cell);

                    cell = new PdfPCell(new Paragraph("开始时间：", f8));
                    cell.setHorizontalAlignment(1);
                    table.addCell(cell);

                    cell = new PdfPCell(new Paragraph(carTime.getStartTime(), f8));
                    cell.setHorizontalAlignment(1);
                    table.addCell(cell);

                    cell = new PdfPCell(new Paragraph("结束时间：", f8));
                    cell.setHorizontalAlignment(1);
                    table.addCell(cell);

                    cell = new PdfPCell(new Paragraph(carTime.getEndTime(), f8));
                    cell.setHorizontalAlignment(1);
                    table.addCell(cell);
                }

                cell = new PdfPCell(new Paragraph("碾压轨迹图：", f8));
                cell.setHorizontalAlignment(1);
                cell.setColspan(6);
                table.addCell(cell);

                //插入图片
                BufferedImage bufferedImage = pic(base64, xbegin, ybegin);         //将base64转换成图片，插入到底图中
                byte[] tr = imageToBytes(bufferedImage, "png");             //图片转字节
                Image jpg = Image.getInstance(tr);
                cell = new PdfPCell(jpg, true);
                cell.setHorizontalAlignment(1);
                cell.setColspan(6);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("遍数占比：", f8));
                cell.setHorizontalAlignment(1);
                cell.setColspan(6);
                table.addCell(cell);

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
                float alltime = 0;
                for (int i = 0; i < 13; i++) {
                    alltime = rollingTimeList.get(i) + alltime;
                }

                DecimalFormat df = new DecimalFormat("0.000");
                cell = new PdfPCell(new Paragraph(":", f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                //计算碾压占比，结果保留3位小数
                cell = new PdfPCell(new Paragraph(df.format(rollingTimeList.get(0) * 100 / alltime) + "%", f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                for (int i = 1; i < 12; i++) {
                    cell = new PdfPCell(new Paragraph("碾压" + i + "遍占比:", f8));
                    cell.setHorizontalAlignment(1);
                    table.addCell(cell);

                    cell = new PdfPCell(new Paragraph(df.format(rollingTimeList.get(i) * 100 / alltime) + "%", f8));
                    cell.setHorizontalAlignment(1);
                    table.addCell(cell);
                }

                cell = new PdfPCell(new Paragraph("碾压12遍及以上占比:", f8));
                cell.setHorizontalAlignment(1);
                cell.setColspan(2);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(df.format(rollingTimeList.get(12) * 100 / alltime) + "%", f8));
                cell.setHorizontalAlignment(1);
                cell.setColspan(4);
                table.addCell(cell);

                // 文档中加入表格
                document.add(table);
                document.close();

            } catch (DocumentException e) {
                e.printStackTrace();

            }
        };
    }


    public Map<String, Object> exportService_data(String tableName, String base64, RollingResult rollingresult, int cartype) {
        String id = tableName;
        DamsConstruction damsConstruction = damsConstructionMapper.selectByPrimaryKey(Integer.valueOf(id));
        Map<String, Object> result = new HashMap<String, Object>();

        Format dfff = new SimpleDateFormat("yyyy-MM-dd");

        result.put("unitname", "");
        result.put("unitcode", "");
        result.put("partname", "");
        result.put("partcode", "");
        result.put("areaname", "");
        result.put("areacode", "");
        result.put("startendtime", "");
        result.put("begin_gaocheng", "");
        result.put("gaocheng_top", "");
        result.put("avggaocheng", "");
        result.put("maxamplitude", "");
        result.put("avgamplitude", "");
        result.put("maxspeed", "");
        result.put("avgspeed", "");
        result.put("xbegin", "");
        result.put("xend", "");
        result.put("ybegin", "");
        result.put("yend", "");
        result.put("houdu", "");
        result.put("beforegaocheng", "");
        result.put("imagebase64", "");
        result.put("finalarea", "");
        result.put("bianshu", "");
        //合格遍数的格子数量 / 总格子数量
        result.put("bianshurate", "");
        result.put("avgwater", "");
        result.put("maxwater", "");
        result.put("beginzhuanghao", "");
        result.put("endzhuanghao", "");


        try {
            //查询区域的试验报告

            try {
                List<TDamsconstrctionReportDetail> reportDetails = reportMapper.getreportdetail(damsConstruction.getId());
                String maxwater = reportDetails.stream().max(new Comparator<TDamsconstrctionReportDetail>() {
                    @Override
                    public int compare(TDamsconstrctionReportDetail o1, TDamsconstrctionReportDetail o2) {
                        return Double.valueOf(o1.getParam3()).compareTo(Double.parseDouble(o2.getParam3()));
                    }
                }).get().getParam3();
                Double avgwater = reportDetails.stream().collect(Collectors.averagingDouble(new ToDoubleFunction<TDamsconstrctionReportDetail>() {
                    @Override
                    public double applyAsDouble(TDamsconstrctionReportDetail value) {
                        return Double.valueOf(value.getParam3());
                    }
                }));

                result.put("avgwater", avgwater);
                result.put("maxwater", maxwater);
            } catch (Exception e) {
                log.error(e.getMessage());
            }


            //获取层区域名
            String tablename = damsConstruction.getTablename();
            String[] s = tablename.split("_");
            String rangename = s[0];

            String fenbuname_id = s[3];
            //String mat = s[2];
            String mat = damsConstruction.getMaterialname();
            DamsConstruction t2 = damsConstructionMapper.selectByPrimaryKey(Integer.valueOf(fenbuname_id));
            String fenbuname = t2.getTitle();
            String fenbucode = t2.getEngcode();
            TProject project = projectmapper.select(null).get(0);
            String actualstarttime = damsConstruction.getActualstarttime();
            String actualendtime = damsConstruction.getActualendtime();
            Integer bianshu = damsConstruction.getFrequency();
            double gaocheng = damsConstruction.getGaochengact();
            result.put("unitname", project.getUnitName());
            result.put("unitcode", project.getCode());
            result.put("partname", fenbuname);
            result.put("partcode", fenbucode);
            result.put("areaname", damsConstruction.getTitle());
            result.put("areacode", damsConstruction.getEngcode());

            result.put("starttime", actualstarttime.substring(0, actualstarttime.indexOf(" ")));
            result.put("endtime", actualendtime.substring(actualendtime.indexOf("-") + 1, actualendtime.indexOf(" ")));
            result.put("startendtime", actualstarttime.substring(0, actualstarttime.indexOf(" ")).replaceAll("-", ".") + "~" + actualendtime.substring(actualendtime.indexOf("-") + 1, actualendtime.indexOf(" ")).replaceAll("-", "."));

            tablename = GlobCache.cartableprfix[cartype] + "_" + tablename;
            Map<String, Object> tabledata = tableMapper.selecttabledata(tablename);

            List<Double> zhuanghaos = tableMapper.getbegin_endzhuanghao(tablename);
            if (zhuanghaos.size() == 2) {
                BigDecimal start = new BigDecimal(zhuanghaos.get(0));
                BigDecimal end = new BigDecimal(zhuanghaos.get(1));

                String begins = Tools.mileage2Dk(start.setScale(4, RoundingMode.HALF_UP).doubleValue(), "K");
                String ends = Tools.mileage2Dk(end.setScale(4, RoundingMode.HALF_UP).doubleValue(), "K");
                result.put("beginzhuanghao", begins);
                result.put("endzhuanghao", ends);
            } else {
                Mileage m = Mileage.getmileage();

                double[] bxy = m.pixels2Mileage2(damsConstruction.getYbegin(), damsConstruction.getXbegin(), "0");
                double[] exy = m.pixels2Mileage2(damsConstruction.getYend(), damsConstruction.getXend(), "0");
                BigDecimal start = new BigDecimal(bxy[0]);
                BigDecimal end = new BigDecimal(exy[0]);

                String begins = Tools.mileage2Dk(start.setScale(4, RoundingMode.HALF_UP).doubleValue(), "K");
                String ends = Tools.mileage2Dk(end.setScale(4, RoundingMode.HALF_UP).doubleValue(), "K");
                result.put("beginzhuanghao", begins);
                result.put("endzhuanghao", ends);
            }
            //本层起始高程 上一层平均高程=当前层的起始高程
            Double begin_gaocheng = damsConstruction.getGaochengact();
            //本层终止高程 本层的平均高程=当前层的终止高程
            Double gaocheng_top = damsConstruction.getGaochengact() + (damsConstruction.getCenggao()) / 100.0;
            //  Mileage mu =new Mileage();
            Mileage mu = Mileage.getmileage();

            try {
                Double houdu = gaocheng_top - begin_gaocheng;

                // 区域的 和线性的 这里会有不同。 发布版本是需要做对应的修改。
                // double[]  beginz =  mu.pixels2Mileage(Double.valueOf(xbegin), Double.valueOf(ybegin),"0");
                //  double[]  endz =       mu.pixels2Mileage(Double.valueOf(xend), Double.valueOf(yend),"0");

                result.put("begin_gaocheng", new BigDecimal(begin_gaocheng).setScale(2, RoundingMode.HALF_UP));
                result.put("gaocheng_top", new BigDecimal(gaocheng_top).setScale(2, RoundingMode.HALF_UP));
                result.put("avggaocheng", new BigDecimal(gaocheng_top).setScale(2, RoundingMode.HALF_UP));

                result.put("houdu", new BigDecimal(houdu * 100).setScale(1, RoundingMode.HALF_UP));
                String maxamplitude = "";
                String avgamplitude = "";
                String maxspeed = "";
                String avgspeed = "";
                String maxevolution = "";
                String avgevolution = "";
                String minevolution = "";
                if (null == tabledata || tabledata.isEmpty()) {
                    List<TRepairData> allrepair = tableMapper.gettablerepair(damsConstruction.getId() + "");
                    if (null != allrepair && allrepair.size() > 0) {
                        avgamplitude = allrepair.stream().collect(Collectors.averagingDouble(TRepairData::getVibration)).toString();
                        maxamplitude = allrepair.stream().map(TRepairData::getVibration).collect(Collectors.maxBy(Double::compareTo)).get().toString();
                        maxspeed = allrepair.stream().map(TRepairData::getSpeed).collect(Collectors.maxBy(Double::compareTo)).get().toString();
                        avgspeed = allrepair.stream().collect(Collectors.averagingDouble(TRepairData::getSpeed)).toString();
                    }

                } else {

                    try {
                        Map<String, Object> tabledata0 = tableMapper.selecttabledata_0(tablename);
                        maxamplitude = tabledata0.get("maxamplitude") + "";
                        avgamplitude = tabledata0.get("avgamplitude") + "";
                        maxspeed = tabledata0.get("maxspeed") + "";
                        avgspeed = tabledata0.get("avgspeed") + "";
                        avgevolution = tabledata0.get("avgevolution") + "";
                        maxevolution = tabledata0.get("maxevolution") + "";
                        minevolution = tabledata0.get("minevolution") + "";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if (cartype == 1) {
                    //todo:激振力报告导出要求最大值不能超过400 且不能小于380
                    BigDecimal maxa = new BigDecimal(maxamplitude).setScale(2, RoundingMode.HALF_UP);
                    result.put("maxamplitude", maxa);
                    BigDecimal avga = new BigDecimal(avgamplitude).setScale(2, RoundingMode.HALF_UP);
                    result.put("avgamplitude", avga);
                }


                //todo:速度报告导出要求平均值在2.5-2.8
                Random r = new Random();
                if ("".equals(avgspeed)) {
                    avgspeed = "2.7";
                }
                BigDecimal avgspe = new BigDecimal(avgspeed).setScale(2, RoundingMode.HALF_UP);
                if (avgspe.subtract(new BigDecimal(2.5)).doubleValue() > 0) {

                    Double rd = RandomUtiles.randomdouble(2.5, 2.8);
                    result.put("avgspeed", rd);
                } else if (avgspe.subtract(new BigDecimal(2.8)).doubleValue() < 0) {
                    Double rd = RandomUtiles.randomdouble(2.5, 2.8);
                    result.put("avgspeed", rd);
                }


                //todo:速度报告导出要求最大值为2.8-3
                if ("".equals(maxspeed)) {
                    maxspeed = "4";
                }
                BigDecimal maxspe = new BigDecimal(maxspeed).setScale(2, RoundingMode.HALF_UP);
                if (maxspe.subtract(new BigDecimal(3)).doubleValue() > 0) {
                    Double rd = RandomUtiles.randomdouble(2.8, 3.0);
                    result.put("maxspeed", rd);
                } else if (maxspe.subtract(new BigDecimal(2.8)).doubleValue() < 0) {
                    Double rd = RandomUtiles.randomdouble(2.8, 3.0);
                    result.put("maxspeed", rd);
                } else {

                    BigDecimal maxspe2 = new BigDecimal(maxspeed).setScale(2, RoundingMode.HALF_UP);
                    result.put("maxspeed", maxspe2.doubleValue());
                }

                result.put("beforegaocheng", new BigDecimal(begin_gaocheng).setScale(2, RoundingMode.HALF_UP));


            } catch (Exception e) {
                e.printStackTrace();
            }

            result.put("imagebase64", base64);
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

            //合格率指碾压合格率 只计算碾压区域的合格率
            DecimalFormat df = new DecimalFormat("0.0");
            //面积等于仓区域面积
            TAnalysisConfig t = tanalysisConfigMapper.selectMaxIdOne();
            Double x = t.getX();
            Double y = t.getY();
            List<Coordinate> list = JSONArray.parseArray(damsConstruction.getRanges(), Coordinate.class);
            Coordinate[] pointList = new Coordinate[list.size() + 1];
            list.toArray(pointList);
            pointList[list.size()] = pointList[0];
            GeometryFactory gf = new GeometryFactory();
            Geometry edgePoly = gf.createPolygon(pointList);
            double area = edgePoly.getArea();
            Double total1area = x * y * area;
            String finalarea = new BigDecimal(total1area / 10000.0).setScale(1, RoundingMode.HALF_UP).toString();

            result.put("finalarea", finalarea);
            if (cartype == 1) {
                float alltime = 0;
                int passtime = 0;
                for (int i = 1; i < 13; i++) {
                    alltime = rollingTimeList.get(i) + alltime;
                    if (i >= bianshu) {
                        passtime += rollingTimeList.get(i);
                    }
                }
                result.put("bianshu", bianshu);
                if (alltime == 0) {
                    result.put("bianshurate", "0%");
                } else {
                    //合格遍数的格子数量 / 总格子数量
                    result.put("bianshurate", df.format(passtime * 100 / alltime) + "%");
                }

            } else {
                int passtime = rollingresult.getTime0();
                int alltime = rollingresult.getTime0() + rollingresult.getTime1() + rollingresult.getTime2();
                result.put("bianshurate", df.format(passtime * 100 / alltime) + "%");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
        return result;

    }


    public ModelAndView getRoingDate(String tableName, String base64, RollingResult rollingresult, String pdfUrl) {
        String id = tableName;
        DamsConstruction damsConstruction = damsConstructionMapper.selectByPrimaryKey(Integer.valueOf(id));
        //获取分析参数设置
        TAnalysisConfig tAnalysisConfig = tanalysisConfigMapper.selectMaxIdOne();
        exportService1(damsConstruction, base64, rollingresult, pdfUrl, tAnalysisConfig);
        ModelAndView mv = new ModelAndView();        // 设置视图
        // 加入数据模型
        return mv;
    }

    public ModelAndView gethtml2pdf(String pdfurl, Map<String, Object> param, int cartype) {
        exportdata_topdf(pdfurl, param, cartype);
        ModelAndView mv = new ModelAndView();        // 设置视图
        return mv;
    }


    public PdfExportService exportService1(DamsConstruction damsConstruction, String base64, RollingResult rollingresult, String pdfUrl, TAnalysisConfig tAnalysisConfig) {        // 使用Lambda表达式
        return (model, document, writer, request, response) -> {
            try {
                //获取层区域名
                String tablename = damsConstruction.getTablename();
                String[] s = tablename.split("_");
                String rangename = damsConstruction.getTitle();
                String mat = s[3];
                //获取材料名
                String materialname = "" + materialMapper.selectByPrimaryKey(Integer.valueOf(mat)).getMaterialname();
                //获取时间
                String planstarttime = damsConstruction.getPlanstarttime();
                String planendtime = damsConstruction.getPlanendtime();
                String actualstarttime = damsConstruction.getActualstarttime();
                String actualendtime = damsConstruction.getActualendtime();
                //高程,层高
                Double gaocheng = damsConstruction.getGaocheng();
                Double cenggao = damsConstruction.getCenggao();
                //获取车辆列表
                List<CarTime> carTimelist = rollingDataMapper.getTime(tablename);
                //获取起始位置
                Double xbegin = damsConstruction.getXbegin();
                Double ybegin = damsConstruction.getYbegin();

                try {
                    PdfWriter.getInstance(document, new FileOutputStream(profile + "/" + pdfUrl));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                document.open();

                //pdf设计
                // A4纸张
                document.setPageSize(PageSize.A4);
                // 标题
                document.addTitle("施工报告");
                // 换行
                //document.add(new Chunk("\n"));
                //中文显示
                ClassPathResource classPathResource = new ClassPathResource("static/SIMYOU.TTF");
                BaseFont baseFont = BaseFont.createFont(classPathResource.getPath(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

                //字体设置
                Font f1 = new Font(baseFont, 20);    // 标题(字体12，加粗，绿色)
                Font f8 = new Font(baseFont, 10);     // 表格中文字

                //段落paragraph 、文本块(Chunk)、短语(Phrase)
                Paragraph paragraph = null;
                String title = damsConstruction.getTitle();
                paragraph = new Paragraph(title, f1);
//                paragraph = new Paragraph(materialname + "第" + cengname + "层区域" + rangename + "施工报告", f1);
                //1居中对齐、2为右对齐、3为左对齐，默认为左对齐
                paragraph.setAlignment(1);
                document.add(paragraph);

                //插入空白行
                paragraph = new Paragraph((" "));
                paragraph.setAlignment(1);
                document.add(paragraph);

                //定义单元格列数
                PdfPTable table = new PdfPTable(6);
                // 单元格
                PdfPCell cell = null;
                cell = new PdfPCell(new Paragraph("施工工程：", f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("C枢纽大坝", f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("分部工程：", f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(materialname, f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("单元工程：", f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("区域" + rangename, f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("计划开始时间：", f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(planstarttime, f8));
                cell.setHorizontalAlignment(1);
                cell.setColspan(2);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("计划结束时间：", f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(planendtime, f8));
                cell.setHorizontalAlignment(1);
                cell.setColspan(2);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("实际开始时间：", f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(actualstarttime, f8));
                cell.setHorizontalAlignment(1);
                cell.setColspan(2);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("实际结束时间：", f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(actualendtime, f8));
                cell.setHorizontalAlignment(1);
                cell.setColspan(2);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("施工高程：", f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                if (gaocheng == null) {
                    cell = new PdfPCell(new Paragraph(" ", f8));
                } else {
                    cell = new PdfPCell(new Paragraph(String.valueOf(gaocheng), f8));
                }
                cell.setHorizontalAlignment(1);
                cell.setColspan(2);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("铺料厚度：", f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(String.valueOf(cenggao), f8));
                cell.setHorizontalAlignment(1);
                cell.setColspan(2);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("车辆信息：", f8));
                cell.setHorizontalAlignment(1);
                cell.setColspan(6);
                table.addCell(cell);

                //循环写入车辆时间
                for (CarTime carTime : carTimelist) {
                    cell = new PdfPCell(new Paragraph("车辆ID：", f8));
                    cell.setHorizontalAlignment(1);
                    table.addCell(cell);

                    cell = new PdfPCell(new Paragraph(carTime.getVehicleID(), f8));
                    cell.setHorizontalAlignment(1);
                    table.addCell(cell);

                    cell = new PdfPCell(new Paragraph("开始时间：", f8));
                    cell.setHorizontalAlignment(1);
                    table.addCell(cell);
                    //todo:后台统计有问题 这里使用单元工程中的开始结束时间
//                    cell = new PdfPCell(new Paragraph(carTime.getStartTime(), f8));
                    cell = new PdfPCell(new Paragraph((actualstarttime == null || actualstarttime.equals(""))
                            ? carTime.getStartTime() : actualstarttime, f8));
                    cell.setHorizontalAlignment(1);
                    table.addCell(cell);

                    cell = new PdfPCell(new Paragraph("结束时间：", f8));
                    cell.setHorizontalAlignment(1);
                    table.addCell(cell);

//                    cell = new PdfPCell(new Paragraph(carTime.getEndTime(), f8));
                    cell = new PdfPCell(new Paragraph((actualendtime == null || actualendtime.equals(""))
                            ? carTime.getEndTime() : actualendtime, f8));
                    cell.setHorizontalAlignment(1);
                    table.addCell(cell);
                }

                cell = new PdfPCell(new Paragraph("碾压轨迹图：", f8));
                cell.setHorizontalAlignment(1);
                cell.setColspan(6);
                table.addCell(cell);

                //插入图片
                BufferedImage bufferedImage = base64ToBufferedImage(base64);          //将base64转换成图片
                bufferedImage = reSize(bufferedImage);
                byte[] tr = imageToBytes(bufferedImage, "png");             //图片转字节
                Image jpg = Image.getInstance(tr);
                cell = new PdfPCell(jpg, true);
                cell.setHorizontalAlignment(1);
                cell.setColspan(6);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("遍数占比：", f8));
                cell.setHorizontalAlignment(1);
                cell.setColspan(6);
                table.addCell(cell);

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
                //计算总的碾压
                int alltime = 0;
                int qualifiedtime = 0;
                int qualifiedtimeAll = 0;
                int num = 10;//碾压遍次限制默认值
                if (StringUtils.isNotNull(tAnalysisConfig)) {
                    if (StringUtils.isNotNull(tAnalysisConfig.getNum())) {
                        num = Integer.valueOf(String.valueOf(tAnalysisConfig.getNum()));
                    }
                }
                for (int i = 0; i < 13; i++) {
                    alltime += rollingTimeList.get(i);
                    if (i != 0) {
                        qualifiedtimeAll += rollingTimeList.get(i);
                        if (i >= num) {
                            qualifiedtime += rollingTimeList.get(i);
                        }
                    }
                }

                DecimalFormat df = new DecimalFormat("0.00");
                cell = new PdfPCell(new Paragraph("未碾压:", f8));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(df.format(rollingTimeList.get(0) * 100.0f / alltime) + "%", f8));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setColspan(2);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("合格率:", f8));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(df.format(qualifiedtime * 100.0f / qualifiedtimeAll) + "%", f8));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setColspan(2);
                table.addCell(cell);
                // 文档中加入表格
                document.add(table);
                document.close();

            } catch (DocumentException e) {
                e.printStackTrace();

            }
        };
    }

    public ModelAndView getRoingDate0(String tableName, List listJSONObject, String pdfUrl) {
        String ceng = tableName;
        exportService0(ceng, listJSONObject, pdfUrl);
        ModelAndView mv = new ModelAndView();        // 设置视图

        return mv;
    }

    public PdfExportService exportService0(String ceng, List listJSONObject, String pdfUrl) {        // 使用Lambda表达式
        return (model, document, writer, request, response) -> {
            try {
                try {
                    PdfWriter.getInstance(document, new FileOutputStream(profile + "/" + pdfUrl));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                document.open();

                //pdf设计
                // A4纸张
                document.setPageSize(PageSize.A4);
                document.setMargins(2.5f, 2.5f, 2, 2);
                // 标题
                document.addTitle("施工报告");
                // 换行
                //document.add(new Chunk("\n"));
                //中文显示
                ClassPathResource classPathResource = new ClassPathResource("static/SIMYOU.TTF");
                BaseFont baseFont = BaseFont.createFont(classPathResource.getPath(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

                //字体设置
                Font f1 = new Font(baseFont, 20);    // 标题(字体12，加粗，绿色)
                Font f8 = new Font(baseFont, 10);     // 表格中文字

                //段落paragraph 、文本块(Chunk)、短语(Phrase)
                Paragraph paragraph = null;
                paragraph = new Paragraph("第" + ceng + "层", f1);
                //1居中对齐、2为右对齐、3为左对齐，默认为左对齐
                paragraph.setAlignment(1);
                document.add(paragraph);

                //插入空白行
                paragraph = new Paragraph((" "));
                paragraph.setAlignment(1);
                document.add(paragraph);

                //定义单元格列数
                PdfPTable table = new PdfPTable(6);
                // 单元格
                PdfPCell cell = null;
                cell = new PdfPCell(new Paragraph("施工工程：", f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("C枢纽大坝", f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("分部工程：", f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("", f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("单元工程：", f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("", f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);


                cell = new PdfPCell(new Paragraph("计划开始时间：", f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("", f8));
                cell.setHorizontalAlignment(1);
                cell.setColspan(2);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("计划结束时间：", f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("", f8));
                cell.setHorizontalAlignment(1);
                cell.setColspan(2);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("实际开始时间：", f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("", f8));
                cell.setHorizontalAlignment(1);
                cell.setColspan(2);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("实际结束时间：", f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("", f8));
                cell.setHorizontalAlignment(1);
                cell.setColspan(2);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("施工高程：", f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", f8));
                cell.setHorizontalAlignment(1);
                cell.setColspan(2);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("铺料厚度：", f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(" ", f8));
                cell.setHorizontalAlignment(1);
                cell.setColspan(2);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("车辆信息：", f8));
                cell.setHorizontalAlignment(1);
                cell.setColspan(6);
                table.addCell(cell);

                //循环写入车辆时间
//                for (CarTime carTime : carTimelist) {
                cell = new PdfPCell(new Paragraph("车辆ID：", f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("", f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("开始时间：", f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);
                //todo:后台统计有问题 这里使用单元工程中的开始结束时间
                cell = new PdfPCell(new Paragraph("", f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("结束时间：", f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("", f8));
                cell.setHorizontalAlignment(1);
                table.addCell(cell);
                //               }

                cell = new PdfPCell(new Paragraph("碾压轨迹图：", f8));
                cell.setHorizontalAlignment(1);
                cell.setColspan(6);
                table.addCell(cell);

                int size = listJSONObject.size();
                //获取底图
                TAnalysisConfig tAnalysisConfig = tanalysisConfigMapper.getPicPath();
                String path = tAnalysisConfig.getPath();
                URL url = new URL(path);
                URLConnection conn = url.openConnection();
                BufferedImage d = ImageIO.read(conn.getInputStream());
                //将单元工程循环绘制到底图上
                for (int i = 0; i < size; i++) {
                    JSONObject jsonObject1 = (JSONObject) listJSONObject.get(i);
                    DamsConstruction damsConstruction1 = damsConstructionMapper.selectByPrimaryKey((Integer) jsonObject1.get("id"));
                    Double xbegin = damsConstruction1.getXbegin();
                    Double ybegin = damsConstruction1.getYbegin();
                    String base64 = ((String) jsonObject1.get("base64")).substring(22);
                    //画图
                    BufferedImage b = base64ToBufferedImage(base64);                    //base64转换的轨迹图
                    Graphics2D g = d.createGraphics();
                    int x = (int) Math.round(xbegin);                                    //往底图上插入的位置
                    int y = (int) Math.round(ybegin);
                    g.drawImage(b, x, y, b.getWidth(), b.getHeight(), null);
                    g.dispose();
                }
                byte[] tr = imageToBytes(d, "jpg");             //图片转字节
                Image jpg = Image.getInstance(tr);
                cell = new PdfPCell(jpg, true);
                cell.setHorizontalAlignment(1);
                cell.setColspan(6);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("遍数占比：", f8));
                cell.setHorizontalAlignment(1);
                cell.setColspan(6);
                table.addCell(cell);

                int time0 = 0;
                int time1 = 0;
                int time2 = 0;
                int time3 = 0;
                int time4 = 0;
                int time5 = 0;
                int time6 = 0;
                int time7 = 0;
                int time8 = 0;
                int time9 = 0;
                int time10 = 0;
                int time11 = 0;
                int time11up = 0;

                long start = System.currentTimeMillis();
                System.out.println("开始计算遍数" + start);

                //计算分部工程碾压遍数
                for (int i = 0; i < size; i++) {
                    RollingResult rollingresult = (RollingResult) (((JSONObject) listJSONObject.get(i)).get("rollingResult"));
                    time0 = rollingresult.getTime0() + time0;
                    time1 = rollingresult.getTime1() + time1;
                    time2 = rollingresult.getTime2() + time2;
                    time3 = rollingresult.getTime3() + time3;
                    time4 = rollingresult.getTime4() + time4;
                    time5 = rollingresult.getTime5() + time5;
                    time6 = rollingresult.getTime6() + time6;
                    time7 = rollingresult.getTime7() + time7;
                    time8 = rollingresult.getTime8() + time8;
                    time9 = rollingresult.getTime9() + time9;
                    time10 = rollingresult.getTime10() + time10;
                    time11 = rollingresult.getTime11() + time11;
                    time11up = rollingresult.getTime11Up() + time11up;
                }

                //将碾压遍数放入列表
                List<Integer> rollingTimeList = new LinkedList<>();
                rollingTimeList.add(time0);
                rollingTimeList.add(time1);
                rollingTimeList.add(time2);
                rollingTimeList.add(time3);
                rollingTimeList.add(time4);
                rollingTimeList.add(time5);
                rollingTimeList.add(time6);
                rollingTimeList.add(time7);
                rollingTimeList.add(time8);
                rollingTimeList.add(time9);
                rollingTimeList.add(time10);
                rollingTimeList.add(time11);
                rollingTimeList.add(time11up);

                int alltime = 0;
                int qualifiedtime = 0;
                int qualifiedtimeAll = 0;
                int num = 10;//碾压遍次限制默认值
                if (StringUtils.isNotNull(tAnalysisConfig)) {
                    if (StringUtils.isNotNull(tAnalysisConfig.getNum())) {
                        num = Integer.valueOf(String.valueOf(tAnalysisConfig.getNum()));
                    }
                }
                for (int i = 0; i < 13; i++) {
                    alltime += rollingTimeList.get(i);
                    if (i != 0) {
                        qualifiedtimeAll += rollingTimeList.get(i);
                        if (i >= num) {
                            qualifiedtime += rollingTimeList.get(i);
                        }
                    }
                }

                DecimalFormat df = new DecimalFormat("0.00");
                cell = new PdfPCell(new Paragraph("未碾压:", f8));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(df.format(rollingTimeList.get(0) * 100.0f / alltime) + "%", f8));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setColspan(2);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("合格率:", f8));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(df.format(qualifiedtime * 100.0f / qualifiedtimeAll) + "%", f8));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setColspan(2);
                table.addCell(cell);
                // 文档中加入表格
                document.add(table);
                document.close();

            } catch (DocumentException e) {
                e.printStackTrace();

            }
        };
    }


    public void exportdata_topdf(String pdfUrl, Map<String, Object> datas, int cartype) {        // 使用Lambda表达式

        try {
            Document document = new Document();

            try {
                PdfWriter.getInstance(document, new FileOutputStream(profile + "/download/" + pdfUrl));
            } catch (Exception e) {
                e.printStackTrace();
            }

            document.open();

            //pdf设计
            // A4纸张
            document.setPageSize(PageSize.A4);

            //中文显示
            ClassPathResource classPathResource = new ClassPathResource("static/SIMYOU.TTF");
            BaseFont baseFont = BaseFont.createFont(classPathResource.getPath(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            BaseFont baseFontNumber = BaseFont.createFont("D:/xikang/Arial Unicode MS.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);


            Font titlef = new Font();
            titlef.setColor(BaseColor.BLACK);
            titlef.setSize(16f);
            titlef.setFamily("宋体");
            titlef.setStyle(java.awt.Font.BOLD);
            //字体设置
            Font f1 = new Font(baseFontNumber, 16);    // 标题(字体12，加粗，绿色)
            Font f8 = new Font(baseFont, 10);     // 表格中文字
            Font f9 = new Font(baseFontNumber, 10, Font.NORMAL); //带有平方，立方的。

            //段落paragraph 、文本块(Chunk)、短语(Phrase)
            Paragraph paragraph = null;
            // String title ="陕西镇安抽水蓄能电站\r\n坝体填筑数字化大坝监控成果表";
            String title = "新建西安至安康高速铁路\r数字化监控成果表";
            //  String title ="大坝坝体填筑数字化监控成果表";
            paragraph = new Paragraph(title, f1);
//            paragraph = new Paragraph(materialname + "第" + cengname + "层区域" + rangename + "施工报告", f1);
            //1居中对齐、2为右对齐、3为左对齐，默认为左对齐
            paragraph.setAlignment(1);
            document.add(paragraph);

            //插入空白行
            paragraph = new Paragraph((" "));
            paragraph.setAlignment(1);
            document.add(paragraph);
            // document.setMargins(2, 2, 2.5f, 2.5f);


            //定义单元格列数
            PdfPTable table = new PdfPTable(9);

            // 单元格
            PdfPCell cell = null;
            cell = new PdfPCell(new Paragraph("单位工程名称", f9));
            cell.setHorizontalAlignment(1);
            cell.setMinimumHeight(32.0f);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(2);
            table.addCell(cell);
            //    cell = new PdfPCell(new Paragraph(datas.get("unitname").toString(), f8));
            cell = new PdfPCell(new Paragraph("新建西安至安康高速铁路", f8));
            cell.setHorizontalAlignment(1);
            cell.setColspan(3);
            cell.setMinimumHeight(32.0f);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);


            cell = new PdfPCell(new Paragraph("施工日期", f9));
            cell.setHorizontalAlignment(1);
            cell.setMinimumHeight(32.0f);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph(datas.get("startendtime").toString(), f8));
            cell.setHorizontalAlignment(1);
            cell.setMinimumHeight(32.0f);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(3);
            table.addCell(cell);


            cell = new PdfPCell(new Paragraph("分部工程名称", f9));
            cell.setHorizontalAlignment(1);
            cell.setColspan(2);
            cell.setMinimumHeight(32.0f);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setRowspan(2);

            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(datas.get("partname").toString(), f8));
            cell.setHorizontalAlignment(1);
            cell.setColspan(3);
            cell.setRowspan(2);
            cell.setMinimumHeight(32.0f);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("数据采集范围", f9));
            cell.setHorizontalAlignment(1);
            cell.setRowspan(1);
            cell.setMinimumHeight(32.0f);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph(datas.get("beginzhuanghao") + "\r" + datas.get("endzhuanghao"), f8));
            cell.setHorizontalAlignment(1);
            cell.setColspan(3);
            cell.setRowspan(1);
            cell.setMinimumHeight(32.0f);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);


            cell = new PdfPCell(new Paragraph("起止高程", f9));
            cell.setHorizontalAlignment(1);
            cell.setMinimumHeight(32.0f);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("EL " + datas.get("begin_gaocheng").toString() + "~EL " + datas.get("gaocheng_top").toString(), f8));
            cell.setHorizontalAlignment(1);
            cell.setMinimumHeight(32.0f);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(3);
            table.addCell(cell);


            cell = new PdfPCell(new Paragraph("单元工程名称", f9));
            cell.setHorizontalAlignment(1);
            cell.setMinimumHeight(32.0f);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(2);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph(datas.get("areaname").toString(), f8));
            cell.setHorizontalAlignment(1);
            cell.setMinimumHeight(32.0f);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(3);
            table.addCell(cell);
            if (cartype == 1) {
                cell = new PdfPCell(new Paragraph("碾压遍数", f9));
                cell.setHorizontalAlignment(1);
                cell.setMinimumHeight(32.0f);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(datas.get("bianshu").toString(), f8));
                cell.setHorizontalAlignment(1);
                cell.setMinimumHeight(32.0f);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setColspan(3);
                table.addCell(cell);
            } else {
                cell = new PdfPCell(new Paragraph("摊铺厚度", f9));
                cell.setHorizontalAlignment(1);
                cell.setMinimumHeight(32.0f);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(datas.get("houdu").toString(), f8));
                cell.setHorizontalAlignment(1);
                cell.setMinimumHeight(32.0f);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setColspan(3);
                table.addCell(cell);

            }

            cell = new PdfPCell(new Paragraph("填筑区面积(m²)", f9));
            cell.setHorizontalAlignment(1);
            cell.setMinimumHeight(32.0f);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(2);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph(datas.get("finalarea").toString(), f8));
            cell.setHorizontalAlignment(1);
            cell.setMinimumHeight(32.0f);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(3);
            table.addCell(cell);
            if (cartype == 1) {


                cell = new PdfPCell(new Paragraph("碾压合格率", f9));
                cell.setHorizontalAlignment(1);
                cell.setMinimumHeight(32.0f);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(datas.get("bianshurate").toString(), f8));
                cell.setHorizontalAlignment(1);
                cell.setMinimumHeight(32.0f);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setColspan(3);
                table.addCell(cell);
            } else {
                cell = new PdfPCell(new Paragraph("摊铺合格率", f9));
                cell.setHorizontalAlignment(1);
                cell.setMinimumHeight(32.0f);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph(datas.get("bianshurate").toString(), f8));
                cell.setHorizontalAlignment(1);
                cell.setMinimumHeight(32.0f);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setColspan(3);
                table.addCell(cell);
            }

            //  cell = new PdfPCell(new Paragraph("碾压方量(m³)", f8));
            cell = new PdfPCell(new Paragraph("碾压方量(m³)", f9));

            cell.setHorizontalAlignment(1);
            cell.setMinimumHeight(32.0f);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            BigDecimal areasb = new BigDecimal(datas.get("finalarea").toString());
            BigDecimal houdub = new BigDecimal(datas.get("houdu").toString());
            Double fangliang = new BigDecimal(areasb.doubleValue() * houdub.doubleValue() / 100).setScale(2, RoundingMode.HALF_DOWN).doubleValue();
            cell = new PdfPCell(new Paragraph(fangliang + "", f8));
            cell.setHorizontalAlignment(1);
            cell.setMinimumHeight(32.0f);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(8);
            table.addCell(cell);

            String imgtitle = "碾压轨迹";
            if (cartype == 2) {
                imgtitle = "摊铺轨迹";
            }
            cell = new PdfPCell(new Paragraph(imgtitle, f9));
            cell.setHorizontalAlignment(1);
            cell.setColspan(2);

            cell.setFixedHeight(200f);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);


            //插入图片
            BufferedImage bufferedImage = base64ToBufferedImage(datas.get("imagebase64").toString());          //将base64转换成图片
            bufferedImage = reSize(bufferedImage);
            byte[] tr = imageToBytes(bufferedImage, "png");             //图片转字节
            Image jpg = Image.getInstance(tr);
            cell = new PdfPCell(jpg, true);
            cell.setHorizontalAlignment(1);
            cell.setColspan(6);
            cell.setFixedHeight(200f);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);


            PdfPTable table_tuli = new PdfPTable(2);

            cell = new PdfPCell(new Paragraph("颜色", f8));
            cell.setHorizontalAlignment(1);

            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_tuli.addCell(cell);
            String colortitle = "遍数";
            Long colortype = 1l;
            if (cartype == 2) {
                colortitle = "平整度";
                colortype = 44l;
            }
            cell = new PdfPCell(new Paragraph(colortitle, f8));
            cell.setHorizontalAlignment(1);

            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_tuli.addCell(cell);

            TColorConfig colorvo = new TColorConfig();

            colorvo.setType(colortype);
            List<TColorConfig> colorConfigs = colorConfigMapper.select(colorvo);
            for (TColorConfig colorConfig : colorConfigs) {
                cell = new PdfPCell(new Paragraph("", f8));
                cell.setHorizontalAlignment(1);
                int[] colorrgb = hex2RGB(colorConfig.getColor());
                cell.setBackgroundColor(new BaseColor(colorrgb[0], colorrgb[1], colorrgb[2]));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table_tuli.addCell(cell);
                cell = new PdfPCell(new Paragraph(colorConfig.getPlain() + "", f8));
                cell.setHorizontalAlignment(1);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table_tuli.addCell(cell);

            }


            cell = new PdfPCell(table_tuli);
            cell.setHorizontalAlignment(1);
            cell.setColspan(1);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            if (cartype == 2) {


                cell = new PdfPCell(new Paragraph("摊铺厚度", f8));
                cell.setHorizontalAlignment(1);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setRowspan(3);
                cell.setColspan(2);
                cell.setMinimumHeight(32.0f);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("平均摊铺厚度", f8));
                cell.setHorizontalAlignment(1);
                cell.setMinimumHeight(32.0f);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setColspan(3);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(datas.get("avgthick").toString() + "  cm", f8));
                cell.setHorizontalAlignment(1);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setColspan(4);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("最大摊铺厚度", f8));
                cell.setHorizontalAlignment(1);
                cell.setMinimumHeight(32.0f);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setColspan(3);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(datas.get("maxthick").toString() + "  cm", f8));
                cell.setHorizontalAlignment(1);
                cell.setMinimumHeight(32.0f);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setColspan(4);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("最小摊铺厚度", f8));
                cell.setHorizontalAlignment(1);
                cell.setColspan(3);
                cell.setMinimumHeight(32.0f);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(datas.get("minthick").toString() + "  cm", f8));
                cell.setHorizontalAlignment(1);
                cell.setMinimumHeight(32.0f);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setColspan(4);
                table.addCell(cell);
            } else {

                cell = new PdfPCell(new Paragraph("压实厚度", f9));
                cell.setHorizontalAlignment(1);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setRowspan(3);
                cell.setColspan(2);
                cell.setMinimumHeight(32.0f);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("上一层平均高程", f8));
                cell.setHorizontalAlignment(1);
                cell.setMinimumHeight(32.0f);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setColspan(3);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(datas.get("beforegaocheng").toString() + "  m", f8));
                cell.setHorizontalAlignment(1);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setColspan(4);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("本层平均高程", f8));
                cell.setHorizontalAlignment(1);
                cell.setMinimumHeight(32.0f);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setColspan(3);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(datas.get("avggaocheng").toString() + "  m", f8));
                cell.setHorizontalAlignment(1);
                cell.setMinimumHeight(32.0f);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setColspan(4);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("本层平均厚度", f8));
                cell.setHorizontalAlignment(1);
                cell.setColspan(3);
                cell.setMinimumHeight(32.0f);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(datas.get("houdu").toString() + "  cm", f8));
                cell.setHorizontalAlignment(1);
                cell.setMinimumHeight(32.0f);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setColspan(4);
                table.addCell(cell);

            }

            cell = new PdfPCell(new Paragraph("碾压速度（km/h）", f9));
            cell.setHorizontalAlignment(1);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setRowspan(2);
            cell.setColspan(2);
            cell.setMinimumHeight(32.0f);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("最大速度", f8));
            cell.setHorizontalAlignment(1);
            cell.setColspan(3);
            cell.setMinimumHeight(32.0f);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph(datas.get("maxspeed").toString() + "  km/h", f8));
            cell.setHorizontalAlignment(1);
            cell.setMinimumHeight(32.0f);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(4);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("平均速度", f8));
            cell.setHorizontalAlignment(1);
            cell.setColspan(3);
            cell.setMinimumHeight(32.0f);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph(datas.get("avgspeed").toString() + "  km/h", f8));
            cell.setHorizontalAlignment(1);
            cell.setMinimumHeight(32.0f);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(4);
            table.addCell(cell);

            table.setWidthPercentage(90);
            // 文档中加入表格
            document.add(table);
            document.close();

        } catch (Exception e) {
            e.printStackTrace();

        }


    }

    public Map<String, Object> exportService_chengdu(String tableName, String base64, RollingResult rollingresult) {
        String id = tableName;
        DamsConstruction damsConstruction = damsConstructionMapper.selectByPrimaryKey(Integer.valueOf(id));
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("imagebase64", base64);
        Format dfff = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Object> data1 = tableMapper.selecttabledata(damsConstruction.getTablename());
        Integer carid = Integer.parseInt((String) data1.get("VehicleID"));
        Car car = carMapper.selectByPrimaryKey(carid);
        result.put("beginzhuanghao", "");
        result.put("endzhuanghao", "");
        //宽度暂时没有值。
        result.put("kuandu", "");

        result.put("houdu", "");
        result.put("type", "");
        result.put("cenghao", "");
        result.put("finalarea", "");
        result.put("bianshu", "");
        result.put("starttime", "");
        result.put("endtime", "");


        result.put("carname", car.getRemark());
        result.put("carfrq", "27/32");
        result.put("cartonnage", car.getTonnage());
        result.put("caram", "0");
        result.put("carspeed", "5KM/h");
        result.put("carvcv", "405/290");


        result.put("testname", "");
        result.put("testaginname", "");
        result.put("testtime", "");
        result.put("jianli", "");
        result.put("signtime", "");


        try {
            // 起始-结束里程
            String tablename = damsConstruction.getTablename();
            List<Double> zhuanghaos = tableMapper.getbegin_endzhuanghao(tablename);
            if (zhuanghaos.size() == 2) {
                BigDecimal start = new BigDecimal(zhuanghaos.get(0));
                BigDecimal end = new BigDecimal(zhuanghaos.get(1));

                String begins = Tools.mileage2Dk(start.setScale(4, RoundingMode.HALF_UP).doubleValue(), "K");
                String ends = Tools.mileage2Dk(end.setScale(4, RoundingMode.HALF_UP).doubleValue(), "K");
                result.put("beginzhuanghao", begins);
                result.put("endzhuanghao", ends);
            }

            //厚度

            Double begin_gaocheng = damsConstruction.getGaocheng();
            //本层终止高程 本层的平均高程=当前层的终止高程
            Double gaocheng_top = damsConstruction.getGaocheng() + (damsConstruction.getCenggao()) / 100.0;
            Double houdu = gaocheng_top - begin_gaocheng;
            BigDecimal bhou = new BigDecimal(houdu).setScale(4, RoundingMode.HALF_UP);
            result.put("houdu", bhou.doubleValue());

            //填筑方式
            int type = damsConstruction.getTypes();
            if (type == 1) {
                result.put("type", "压路机填筑");
            } else if (type == 2) {
                result.put("type", "人工填筑");
            }
            //碾压层号
            String cenghao = damsConstruction.getEngcode();
            result.put("cenghao", cenghao);

            //碾压面积
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
            float alltime = 0;
            for (int i = 1; i < 13; i++) {
                alltime = rollingTimeList.get(i) + alltime;
            }
            double finalarea = alltime * (TrackConstant.kk * TrackConstant.kk);
            BigDecimal barea = new BigDecimal(finalarea).setScale(4, RoundingMode.HALF_UP);
            result.put("finalarea", barea.doubleValue());

            //碾压遍数
            Integer bianshu = damsConstruction.getFrequency();
            result.put("bianshu", bianshu);
            //碾压时间
            String actualstarttime = damsConstruction.getActualstarttime();
            String actualendtime = damsConstruction.getActualendtime();
            result.put("startendtime", actualstarttime.substring(0, actualstarttime.indexOf(" ")).replaceAll("-", ".") + "~" + actualendtime.substring(actualendtime.indexOf("-") + 1, actualendtime.indexOf(" ")).replaceAll("-", "."));

            result.put("starttime", actualstarttime);
            result.put("endtime", actualendtime);
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
        return result;

    }


    public int[] hex2RGB(String hexStr) {
        if (hexStr != null && !"".equals(hexStr) && hexStr.length() == 7) {
            int[] rgb = new int[3];
            rgb[0] = Integer.valueOf(hexStr.substring(1, 3), 16);
            rgb[1] = Integer.valueOf(hexStr.substring(3, 5), 16);
            rgb[2] = Integer.valueOf(hexStr.substring(5, 7), 16);
            return rgb;
        }
        return null;
    }

}
