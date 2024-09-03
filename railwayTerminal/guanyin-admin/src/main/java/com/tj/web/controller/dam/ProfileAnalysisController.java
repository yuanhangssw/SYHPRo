package com.tj.web.controller.dam;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tianji.dam.domain.AnalysisVO;
import com.tianji.dam.domain.DamsConstruction;
import com.tianji.dam.mapper.TAnalysisConfigMapper;
import com.tianji.dam.mapper.TableMapper;
import com.tianji.dam.mileageutil.Mileage;
import com.tianji.dam.service.AnalysisService;
import com.tianji.dam.service.RollingDataService;
import com.tianji.dam.utils.TrackConstant;
import com.tj.common.core.domain.AjaxResult;
import com.tj.common.utils.SecurityUtils;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 剖面分析controller
 */
@RestController("ProfileAnalysisController")
@RequestMapping("/bidr/profileAnalysis")
@Slf4j
@Api(tags = "剖面分析接口")
public class ProfileAnalysisController {
    @Autowired
    private TableMapper tableMapper;
    @Autowired
    RollingDataService rollingDataService;

    @Autowired
    AnalysisService analysisService;

    @Autowired
    TAnalysisConfigMapper configmapper;


    /**
     * 剖面分析
     * 1.遍历所有的仓，取出ranges，生成多边形。
     * zhuangx，ymin；zhuangx，ymax生成一个线段
     * jts判断相交，取出相交的仓
     * 2.用原来画图的方法，取出matrix中x为zhuangx的像素点
     * 3.base64 put到result
     * 4.按照高程区间画剖面图
     *
     * @param zhuangX
     * @return
     */
    @PostMapping("/analysis/{cartype}")
    @ApiOperation(value = "剖面分析图", httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK")
    })
    public AjaxResult analysis(double zhuangX, int type, @PathVariable("cartype") int cartype) throws IOException, ExecutionException, InterruptedException {
        //1
        Mileage mileage = Mileage.getmileage();
//        mileage.initParams();
//        if (type == 1) {
//            double[] zhuang = mileage.mileage2Pixel(1, zhuangX, 0, "0");
//            zhuangX = zhuang[0];
//        } else {
//            double[] zhuang = mileage.mileage2Pixel(1, 0, zhuangX, "0");
//            zhuangX = zhuang[1];
//        }


        double yMax = type == 1 ? TrackConstant.BACKIMG_HEIGT : TrackConstant.BACKIMG_WIDTH;//this.tableMapper.findZhuangYMaxT_1();//查询zhaungymax
        double yMin = 0;//this.tableMapper.findZhuangYMinT_1();//查询zhuangymin
        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coordinate1 = type == 1 ? new Coordinate(zhuangX, yMin) : new Coordinate(yMax, zhuangX);
        Coordinate coordinate2 = type == 1 ? new Coordinate(zhuangX, yMax) : new Coordinate(yMin, zhuangX);
        Coordinate[] c = new Coordinate[]{coordinate1, coordinate2};
        LineString line = geometryFactory.createLineString(c);//确定线段

        List<DamsConstruction> cangIds = new LinkedList<>();//新建相交的仓的id list
        List<DamsConstruction> cangs = this.tableMapper.findIdRangesFromDam();//全部的仓
        for (DamsConstruction dam : cangs) {
            if (dam.getRanges() != null && !dam.getRanges().equals("")) {
                //首先构建多边形
                List<Coordinate> coordList = new LinkedList<>();
                JSONArray array = JSON.parseArray(dam.getRanges());//字符串转JSONArray
                if (array != null && array.size() > 0) {
                    for (int i = 0; i < array.size(); i++) {
                        JSONObject o = (JSONObject) array.get(i);//取到每个横纵坐标
                        Coordinate coord = new Coordinate(Double.parseDouble(o.get("x").toString()), Double.parseDouble(o.get("y").toString()));
                        coordList.add(coord);
                    }
                    JSONObject o = (JSONObject) array.get(0);

                    Coordinate coord = new Coordinate(Double.parseDouble(o.get("x").toString()), Double.parseDouble(o.get("y").toString()));//构建多边形坐标点要形成闭环
                    coordList.add(coord);//构建多边形坐标点要形成闭环，所以把第一个点加到list末尾
                    Coordinate[] coordArray = new Coordinate[coordList.size() + 1];
                    coordList.toArray(coordArray);
                    coordArray[coordList.size()] = coordArray[0];
                    Polygon polygon = geometryFactory.createPolygon(coordArray);//构建多边形

                    //确定多边形与线段是否相交，相交则将仓id加入cangIds
                    if (line.intersects(polygon)) {
                        cangIds.add(dam);
                    }
                }
            }
        }
        JSONObject jsonObject = this.analysisService.analysis(SecurityUtils.getUsername(), cangIds, zhuangX, type, cartype);
        return AjaxResult.success(jsonObject.get("base64"));
    }


    @PostMapping("/analysis2")
    @ApiOperation(value = "剖面分析图", httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK")
    })
    public AjaxResult analysis2(@RequestBody AnalysisVO analysisVO) throws IOException, ExecutionException, InterruptedException {
        GeometryFactory geometryFactory = new GeometryFactory();


        //  return AjaxResult.success("");
        Coordinate coordinate1 = new Coordinate(analysisVO.getBeginx(), analysisVO.getBeginy());
        Coordinate coordinate2 = new Coordinate(analysisVO.getEndx(), analysisVO.getEndy());


        Coordinate[] c = new Coordinate[]{coordinate1, coordinate2};
        LineString line = geometryFactory.createLineString(c);//确定线段

        List<DamsConstruction> cangIds = new LinkedList<>();//新建相交的仓的id list
        List<DamsConstruction> cangs = this.tableMapper.findIdRangesFromDam();//全部的仓
        for (DamsConstruction dam : cangs) {
            if (dam.getRanges() != null && !dam.getRanges().equals("")) {
                //首先构建多边形
                List<Coordinate> coordList = new LinkedList<>();
                JSONArray array = JSON.parseArray(dam.getRanges());//字符串转JSONArray
                if (array != null && array.size() > 0) {
                    for (int i = 0; i < array.size(); i++) {
                        JSONObject o = (JSONObject) array.get(i);//取到每个横纵坐标
                        Coordinate coord = new Coordinate(Double.parseDouble(o.get("x").toString()), Double.parseDouble(o.get("y").toString()));
                        coordList.add(coord);
                    }
                    JSONObject o = (JSONObject) array.get(0);

                    Coordinate coord = new Coordinate(Double.parseDouble(o.get("x").toString()), Double.parseDouble(o.get("y").toString()));//构建多边形坐标点要形成闭环
                    coordList.add(coord);//构建多边形坐标点要形成闭环，所以把第一个点加到list末尾
                    Coordinate[] coordArray = new Coordinate[coordList.size() + 1];
                    coordList.toArray(coordArray);
                    coordArray[coordList.size()] = coordArray[0];
                    Polygon polygon = geometryFactory.createPolygon(coordArray);//构建多边形

                    //确定多边形与线段是否相交，相交则将仓id加入cangIds
                    if (line.intersects(polygon)) {
                        cangIds.add(dam);
                    }
                }
            }
        }
        JSONObject jsonObject = this.analysisService.analysis2(SecurityUtils.getUsername(), cangIds, coordinate1, coordinate2);
        return AjaxResult.success(jsonObject);
    }

}
