package com.tj.web.controller.dam;

import com.alibaba.fastjson.JSONObject;
import com.tianji.dam.domain.DamsConstruction;
import com.tianji.dam.domain.RollingResult;
import com.tianji.dam.domain.TColorConfig;
import com.tianji.dam.domain.TRepairData;
import com.tianji.dam.mapper.TColorConfigMapper;
import com.tianji.dam.mapper.TDamsconstructionMapper;
import com.tianji.dam.mapper.TRepairDataMapper;
import com.tianji.dam.service.DownloadService;
import com.tianji.dam.service.RollingDataService;
import com.tianji.dam.utils.RepairDataUtil;
import com.tj.common.utils.RGBHexUtil;
import com.tj.common.utils.SecurityUtils;
import com.tj.common.utils.StringUtils;
import com.tj.common.utils.uuid.IdUtils;
import io.swagger.annotations.*;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@CrossOrigin
@RequestMapping("/bidr/export")
@RestController
@Api(value = "报告预览与导出", tags = {"报告预览与导出"})
@Controller
public class DownloadController {
    @Autowired
    private DownloadService downloadService;
    @Autowired
    private RollingDataService rollingDataService;

    //pdf预览
    @GetMapping(value = "/pdf/{tableName}")
    @ApiOperation(value = "报告预览", httpMethod = "GET")
    @ApiImplicitParam(name = "tableName", value = "t_damsconstruction表的主键id", required = true, paramType = "path", dataType = "String")
    @ApiResponses({@ApiResponse(code = 200, message = "OK", response = ModelAndView.class)
    })
    @ResponseBody
    public ModelAndView previewPdf(HttpServletRequest request, HttpServletResponse response, @PathVariable("tableName") String tableName) throws InterruptedException, ExecutionException, IOException {
        String pdfUrl = IdUtils.fastUUID() + ".pdf";
        String userName = SecurityUtils.getUsername();
        JSONObject jsonObject2 = rollingDataService.getHistoryPicMultiThreadingByZhuangByTod(tableName, 1);   //绘制历史碾压轨迹图
        String base64 = ((String) jsonObject2.get("base64")).substring(22);                                            //轨迹图的base64
        RollingResult rollingresult = (RollingResult) jsonObject2.get("rollingResult");                             //碾压遍数结果
        ModelAndView modelAndView = downloadService.findRoingDate(tableName, base64, rollingresult);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline;filename" + "test.pdf");
        request.getSession().putValue("pdf", modelAndView);
        return modelAndView;
    }


    //pdf下载
    @GetMapping(value = "/pdf/export")
    @ApiOperation(value = "报告导出", httpMethod = "GET")
    @ApiResponses({@ApiResponse(code = 200, message = "OK", response = ModelAndView.class)
    })
    @ResponseBody
    public ModelAndView exportPdf(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView session = (ModelAndView) request.getSession().getAttribute("pdf");
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment;filename" + "test.pdf");
        return session;
    }

    @Value("${serverUrl}")
    private String serverUrl;

    @Autowired
    private TRepairDataMapper repairDataMapper;
    @Autowired
    private TDamsconstructionMapper damsConstructionMapper;
    @Autowired
    private TColorConfigMapper colorConfigMapper;

    //pdf预览
    @GetMapping(value = "/preview/{tableName}")
    @ApiOperation(value = "生成报表", httpMethod = "GET")
    @ApiImplicitParam(name = "tableName", value = "t_damsconstruction表的主键id", required = true, paramType = "path", dataType = "String")
    @ApiResponses({@ApiResponse(code = 200, message = "OK", response = ModelAndView.class)
    })
    @ResponseBody
    public ModelAndView previewPdf1(HttpServletRequest request, HttpServletResponse response, @PathVariable("tableName") String tableName) throws InterruptedException, ExecutionException, IOException {

        String pdfUrl = IdUtils.fastUUID() + ".pdf";
        String userName = SecurityUtils.getUsername();
        JSONObject jsonObject2 = rollingDataService.getHistoryPicMultiThreadingByZhuangByTod(tableName, 1);   //绘制历史碾压轨迹图
        String base64 = ((String) jsonObject2.get("base64")).substring(22);                                            //轨迹图的base64
        /**
         * 查询补录数据
         */
        List<JSONObject> images = new LinkedList<>();
        TRepairData record = new TRepairData();
        record.setDamsid(Integer.valueOf(tableName));
        List<TRepairData> repairDataList = repairDataMapper.selectTRepairDatas(record);
        List<Integer> xbs = new LinkedList<>();
        List<Integer> xes = new LinkedList<>();
        List<Integer> ybs = new LinkedList<>();
        List<Integer> yes = new LinkedList<>();
        DamsConstruction damsConstruction = damsConstructionMapper.selectByPrimaryKey(Integer.valueOf(tableName));
        xbs.add(damsConstruction.getXbegin().intValue());
        xes.add(damsConstruction.getXend().intValue());
        ybs.add(damsConstruction.getYbegin().intValue());
        yes.add(damsConstruction.getYend().intValue());
        if (StringUtils.isNotEmpty(repairDataList)) {
            for (TRepairData data : repairDataList) {
                xbs.add(data.getXbegin().intValue());
                xes.add(data.getXend().intValue());
                ybs.add(data.getYbegin().intValue());
                yes.add(data.getYend().intValue());
                TColorConfig color = colorConfigMapper.getById(Long.valueOf(data.getColorId()));
                int[] rgb = RGBHexUtil.hex2RGB(color.getColor());
                images.add(RepairDataUtil.drawBufferedImage(data, new Color(rgb[0], rgb[1], rgb[2])));
            }
        }
        int minX = xbs.stream().mapToInt(Integer -> Integer.intValue()).min().getAsInt();//xbg 最小列
        int maxX = xes.stream().mapToInt(Integer -> Integer.intValue()).max().getAsInt();//xend 最大列
        int minY = ybs.stream().mapToInt(Integer -> Integer.intValue()).min().getAsInt();//ybg 最小行
        int maxY = yes.stream().mapToInt(Integer -> Integer.intValue()).max().getAsInt();//yed 最大行
        int width = maxX - minX;//width = 最大的xe-最小的xb  2505 - 1414
        int height = maxY - minY;//height = 最大的ye - 最小的yb 1385-552
        int baseX = minX;//最小的xb
        int baseY = minY;//最小的yb

        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);//INT精确度达到一定,RGB三原色，高度70,宽度150
        //得到它的绘制环境(这张图片的笔)
        Graphics2D g2 = (Graphics2D) bi.getGraphics();

        BufferedImage image1 = (BufferedImage) jsonObject2.get("bi");
        g2.drawImage(image1, damsConstruction.getXbegin().intValue() - baseX, damsConstruction.getYbegin().intValue() - baseY, image1.getWidth(), image1.getHeight(), null);
        for (JSONObject obj : images) {
            //x = xb- baseX    y = yb - baseY
            BufferedImage image = (BufferedImage) obj.get("bi");
            int xb = ((Double) obj.get("xb")).intValue();
            int yb = ((Double) obj.get("yb")).intValue();
            g2.drawImage(image, xb - baseX, yb - baseY, image.getWidth(), image.getHeight(), null);
        }
        String bsae64_string = "";
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


        RollingResult rollingresult = (RollingResult) jsonObject2.get("rollingResult");

        //碾压遍数结果
        ModelAndView modelAndView = downloadService.getRoingDate(tableName, base64, rollingresult, pdfUrl);
        downloadService.getRoingDate(tableName, base64, rollingresult, pdfUrl);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline;filename" + "test.pdf");
        request.getSession().putValue("pdf", modelAndView);
        String url = serverUrl + pdfUrl;
        response.setHeader("pdfurl", url);
        return modelAndView;
    }



//    @GetMapping(value = "/division/{tableName}")
//    @ApiOperation(value = "按层预览", httpMethod = "GET")
//    @ApiImplicitParam(name = "tableName", value = "层数", required = true, paramType = "path", dataType = "String")
//    @ResponseBody
//    public ModelAndView Pdf(HttpServletRequest request, HttpServletResponse response, @PathVariable("tableName") String tableName) throws InterruptedException, ExecutionException, IOException {
//        String pdfUrl = IdUtils.fastUUID() + ".pdf";
//        String userName = SecurityUtils.getUsername();
//        List listJSONObject = rollingDataService.getDivisionalPicForThread(userName, tableName);
//        ModelAndView modelAndView = downloadService.getRoingDate0(tableName, listJSONObject, pdfUrl);
//        downloadService.getRoingDate0(tableName, listJSONObject, pdfUrl);
//        response.setContentType("application/pdf");
//        response.setHeader("Content-Disposition", "inline;filename" + "test.pdf");
//        request.getSession().putValue("pdf", modelAndView);
//        String url = serverUrl + pdfUrl;
//        response.setHeader("pdfurl", url);
//        return modelAndView;
//    }
    //pdf预览
    @SuppressWarnings("deprecation")
    @RequestMapping(value = "/preview_html2pdf")
    @ApiOperation(value = "html生成报表", httpMethod = "GET")
    @ApiResponses({@ApiResponse(code = 200, message = "OK", response = ModelAndView.class)
    })
    @ResponseBody
    public String htmltopdf(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> params, int cartype) {

        String pdfUrl = IdUtils.fastUUID() + ".pdf";
        downloadService.gethtml2pdf(pdfUrl, params, cartype);
        return pdfUrl;
    }


}
