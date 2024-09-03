package com.tianji.dam.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.tomcat.util.codec.binary.Base64;
import org.osgeo.proj4j.ProjCoordinate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tianji.dam.domain.RollingDataRange;
import com.tianji.dam.domain.RollingResult;
import com.tianji.dam.domain.StorehouseRange;
import com.tianji.dam.domain.TRepairData;
import com.vividsolutions.jts.algorithm.PointLocator;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

public class RepairDataUtil {

    public static JSONObject drawImage(TRepairData damsConstruction,Color color) {
        String bsae64_string = "";
        Double division = 1d;
        RollingDataRange rollingDataRange = new RollingDataRange();
        //从数据库中获得范围
        rollingDataRange.setMaxCoordX(damsConstruction.getXend());
        rollingDataRange.setMinCoordX(damsConstruction.getXbegin());
        rollingDataRange.setMaxCoordY(damsConstruction.getYend());
        rollingDataRange.setMinCoordY(damsConstruction.getYbegin());
        //记录外接矩形的范围
        StorehouseRange storehouseRange =new StorehouseRange();
        storehouseRange.setMinOfxList(rollingDataRange.getMinCoordY());
        storehouseRange.setMaxOfxList(rollingDataRange.getMaxCoordY());
        storehouseRange.setMinOfyList(rollingDataRange.getMinCoordX());
        storehouseRange.setMaxOfyList(rollingDataRange.getMaxCoordX());
        Integer yNum =(int) Math.ceil((rollingDataRange.getMaxCoordY()-rollingDataRange.getMinCoordY())/division);//行数
        Integer xNum =(int) Math.ceil((rollingDataRange.getMaxCoordX()-rollingDataRange.getMinCoordX())/division);//列数

        int xSize= (int) Math.abs(damsConstruction.getXend() - damsConstruction.getXbegin());
        int ySize= (int) Math.abs(damsConstruction.getYend() - damsConstruction.getYbegin());
        //绘制图片
        //得到图片缓冲区
        BufferedImage bi = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);//INT精确度达到一定,RGB三原色，高度70,宽度150
        //得到它的绘制环境(这张图片的笔)
        Graphics2D g2 = (Graphics2D) bi.getGraphics();
        //记录遍数
        List<Coordinate> list = JSONArray.parseArray(damsConstruction.getRanges(), Coordinate.class);
        Coordinate[] pointList= new Coordinate[list.size()+1];  //最重要，不能遗漏，预先申请好数组空间
        list.toArray(pointList);
        pointList[list.size()]=pointList[0];
        GeometryFactory gf = new GeometryFactory();
        Geometry edgePoly = gf.createPolygon(pointList);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < xSize - 1; i++) {
            for (int j = 0; j < ySize - 1; j++) {
                //计算边界的网格数量
                Double xTmp = rollingDataRange.getMinCoordX()+i*division;
                Double yTmp = rollingDataRange.getMinCoordY()+j*division;
                //判断点是否在多边形内
                Coordinate point = new Coordinate(xTmp,yTmp);
                PointLocator a=new PointLocator();
                boolean p1=a.intersects(point, edgePoly);
                if(p1){
                    g2.setColor(color);
                    g2.fillRect(i, j, 1, 1);
                }
            }
        }

        JSONObject result = new JSONObject();
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
        RollingResult rollingResult = new RollingResult();
        rollingResult.setTime8(0);

        ProjCoordinate projCoordinate1 = new ProjCoordinate(rollingDataRange.getMinCoordY() , rollingDataRange.getMinCoordX(), 10);
        ProjCoordinate projCoordinate2 = new ProjCoordinate(rollingDataRange.getMaxCoordY() , rollingDataRange.getMaxCoordX(), 10);
        result.put("rollingResult",rollingResult);
        result.put("base64",bsae64_string);
        result.put("rollingDataRange",rollingDataRange);
        result.put("pointLeftBottom",projCoordinate1);
        result.put("pointRightTop",projCoordinate2);
        /*result.put("height",damsConstruction.getGaocheng());
        result.put("cenggao",damsConstruction.getCenggao());*/
        result.put("range",damsConstruction.getRanges());
        //边界
        result.put("ranges",damsConstruction.getRanges());
        result.put("id", damsConstruction.getId());

        result.put("xb", damsConstruction.getXbegin());
        result.put("yb", damsConstruction.getYbegin());

        return result;
    }


    public static JSONObject drawBufferedImage(TRepairData damsConstruction,Color color) {
        String bsae64_string = "";
        Double division = 1d;
        RollingDataRange rollingDataRange = new RollingDataRange();
        //从数据库中获得范围
        rollingDataRange.setMaxCoordX(damsConstruction.getXend());
        rollingDataRange.setMinCoordX(damsConstruction.getXbegin());
        rollingDataRange.setMaxCoordY(damsConstruction.getYend());
        rollingDataRange.setMinCoordY(damsConstruction.getYbegin());
        //记录外接矩形的范围
        StorehouseRange storehouseRange =new StorehouseRange();
        storehouseRange.setMinOfxList(rollingDataRange.getMinCoordY());
        storehouseRange.setMaxOfxList(rollingDataRange.getMaxCoordY());
        storehouseRange.setMinOfyList(rollingDataRange.getMinCoordX());
        storehouseRange.setMaxOfyList(rollingDataRange.getMaxCoordX());
        Integer yNum =(int) Math.ceil((rollingDataRange.getMaxCoordY()-rollingDataRange.getMinCoordY())/division);//行数
        Integer xNum =(int) Math.ceil((rollingDataRange.getMaxCoordX()-rollingDataRange.getMinCoordX())/division);//列数

        int xSize= (int) Math.abs(damsConstruction.getXend() - damsConstruction.getXbegin());
        int ySize= (int) Math.abs(damsConstruction.getYend() - damsConstruction.getYbegin());
        //绘制图片
        //得到图片缓冲区
        BufferedImage bi = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);//INT精确度达到一定,RGB三原色，高度70,宽度150
        //得到它的绘制环境(这张图片的笔)
        Graphics2D g2 = (Graphics2D) bi.getGraphics();
        //记录遍数
        List<Coordinate> list = JSONArray.parseArray(damsConstruction.getRanges(), Coordinate.class);
        Coordinate[] pointList= new Coordinate[list.size()+1];  //最重要，不能遗漏，预先申请好数组空间
        list.toArray(pointList);
        pointList[list.size()]=pointList[0];
        GeometryFactory gf = new GeometryFactory();
        Geometry edgePoly = gf.createPolygon(pointList);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < xSize - 1; i++) {
            for (int j = 0; j < ySize - 1; j++) {
                //计算边界的网格数量
                Double xTmp = rollingDataRange.getMinCoordX()+i*division;
                Double yTmp = rollingDataRange.getMinCoordY()+j*division;
                //判断点是否在多边形内
                Coordinate point = new Coordinate(xTmp,yTmp);
                PointLocator a=new PointLocator();
                boolean p1=a.intersects(point, edgePoly);
                if(p1){
                    g2.setColor(color);
                    g2.fillRect(i, j, 1, 1);
                }
            }
        }

        JSONObject result = new JSONObject();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bi, "PNG", baos);
            byte[] bytes = baos.toByteArray();//转换成字节
            result.put("bi",bi);
            bsae64_string = "data:image/png;base64," + Base64.encodeBase64String(bytes);
            baos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(bsae64_string);
        RollingResult rollingResult = new RollingResult();
        rollingResult.setTime8(0);
        result.put("rollingResult",rollingResult);
        result.put("base64",bsae64_string);
        result.put("range",damsConstruction.getRanges());
        //边界
        result.put("ranges",damsConstruction.getRanges());
        result.put("id", damsConstruction.getId());
        result.put("xb", damsConstruction.getXbegin());
        result.put("yb", damsConstruction.getYbegin());

        return result;
    }

}
