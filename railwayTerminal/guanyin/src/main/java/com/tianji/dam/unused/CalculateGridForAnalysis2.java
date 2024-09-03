package com.tianji.dam.unused;

import com.tianji.dam.analysis.StoreHouseMapAnalysis;
import com.tianji.dam.domain.MatrixItem;
import com.tianji.dam.domain.RollingData;
import com.tianji.dam.domain.RollingDataRange;
import com.tianji.dam.scan.Pixel;
import com.tianji.dam.scan.PointCpb;
import com.tianji.dam.scan.Scan;
import com.tianji.dam.utils.TrackConstant;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.Callable;

import static java.util.Arrays.sort;

/**
 * 多线程计算网格碾压遍数
 * 剖面分析
 */
@Slf4j
public class CalculateGridForAnalysis2 implements Callable<Integer> {
    private static final String TAG = "CalculateGridForAnalysis";
    private static Object obj1 = new Object();//锁的对象，可以是任意的对象
    Integer yNum;
    Integer xNum;
    String tableName;
    Coordinate[] zhuang;//坐标

    MatrixItem[][] matrixItems;

    public MatrixItem[][] getMatrixItems() {
        return matrixItems;
    }

    public void setMatrixItems(MatrixItem[][] matrixItems) {
        this.matrixItems = matrixItems;
    }

    public void setZhuang(Coordinate[] zhuang) {
        this.zhuang = zhuang;
    }


    public Coordinate[] getZhuang() {
        return zhuang;
    }



    public CalculateGridForAnalysis2(){}


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setyNum(Integer yNum) {
        this.yNum = yNum;
    }

    public void setxNum(Integer xNum) {
        this.xNum = xNum;
    }

    private RollingDataRange rollingDataRange;

    public void setRollingDataRange(RollingDataRange rollingDataRange) {
        this.rollingDataRange = rollingDataRange;
    }

    @Autowired
    private static StoreHouseMapAnalysis storeHouseMap;

    private List<RollingData> rollingDataList;
    public List<RollingData> getRollingDataList() {
        return rollingDataList;
    }
    public void setRollingDataList(List<RollingData> rollingDataList) {
        this.rollingDataList = rollingDataList;
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

        public Point2D pt2= new Point2D();

        public Point2D pt3= new Point2D();

        public Point2D pt4= new Point2D();

        public Point2D getCenterPoint(){
            Point2D pt = new Point2D();
            pt.setX((pt1.getX()+pt3.getX())/2);
            pt.setY((pt1.getY()+pt3.getY())/2);
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

    private WheelMarkData calPosition(RollingData lastp,RollingData currentp,
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
            log.error(TAG, "calPosition Exception:" + ex.getMessage());
        }
        return null;
    }

    private int getPointsConvexClosure(PointCpb[] ps, int fromIndex, int toIndex, PointCpb[] convex){
        sort(ps,fromIndex,toIndex);
        int len =toIndex -fromIndex;
        PointCpb[] tmp =new PointCpb[2*len];
        int up =len, down =len;
        for(int index =fromIndex;index<toIndex;index++){
            tmp[up] =tmp[down] =ps[index];
            while(len-up>=2&&multiply(tmp[up+2],tmp[up+1],tmp[up])>=0){
                tmp[up+1] =tmp[up];
                up++;
            }
            while(down-len>=2&&multiply(tmp[down-2],tmp[down-1],tmp[down])<=0){
                tmp[down-1] =tmp[down];
                down--;
            }
            up --;
            down ++;
        }
        System.arraycopy(tmp,up+1,convex,0,down-up-2);
        return down-up-2;
    }
    /**
     * 计算向量ab与ac的叉积
     */
    private double multiply(PointCpb a,PointCpb b,PointCpb c){
        return multiply(a,b,a,c);
    }
    /**
     * 计算向量ab与cd的叉积
     */
    private double multiply(PointCpb a,PointCpb b,PointCpb c,PointCpb d){
        return (b.x-a.x)*(d.y-c.y)-(d.x-c.x)*(b.y-a.y);
    }

    @Override
    public Integer call() throws Exception {
        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate[] c = new Coordinate[]{zhuang[0], zhuang[1]};
        LineString line = geometryFactory.createLineString(c);//确定线段
        if(rollingDataList.size() > 2){
            try{
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
              
                for(int i = 0;i < rollingDataList.size();i++) {
                	
                	
                    rollingData02 = rollingDataList.get(i);
                    double dis = Math.sqrt(Math.pow(rollingData01.getZhuangX() - rollingData02.getZhuangX(), 2) +
                            Math.pow(rollingData01.getZhuangY() - rollingData02.getZhuangY(), 2));
                    if (dis < TrackConstant.MIN_DIS) {
                        if (i + 1 >= rollingDataList.size()) {
                            break;
                        }
//                        log.error("前后距离过小:" + dis);
                    } else if(dis > TrackConstant.MAX_DIS){
//                        log.error("前后距离过大:" + dis + " " + rollingData01.getOrderNum() + "-" + rollingData02.getOrderNum());
                        rollingData01 = rollingData02;
                        //将前一个滚轮置空,否则将会把中间连接在一起
                        mLastPt = null;
                    } else{
                        WheelMarkData data = calPosition(rollingData01, rollingData02, TrackConstant.WHEEL_LEFT, TrackConstant.WHEEL_RIGHT, 0);
                        if (data != null) {
                            rollingData02.setCoordLX(data.LPt.getX());
                            rollingData02.setCoordLY(data.LPt.getY());
                            rollingData02.setCoordRX(data.RPt.getX());
                            rollingData02.setCoordRY(data.RPt.getY());
                            if (mLastPt == null) {
                                mLastPt = data;
                            } else {
                                if(ps == null){
                                    ps = new PointCpb[]{ps1,ps2,ps3,ps4};
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
                                    if(polygon == null){
                                        polygon = new Pixel[]{polygon1,polygon2,polygon3,polygon4};
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
                                    int bottom = (int) (rollingDataRange.getMinCoordY() * 1);
                                    int left = (int) (rollingDataRange.getMinCoordX() * 1);
                                    int width = (int) (rollingDataRange.getMaxCoordX() - rollingDataRange.getMinCoordX());
                                    int height = (int) (rollingDataRange.getMaxCoordY() - rollingDataRange.getMinCoordY());
                                    int n = 0;
                                    int m = 0;
                                    /*Map<String, MatrixItem[][]> storeHouses2RollingData = storeHouseMap.getStoreHouses2RollingData();
                                    if(storeHouses2RollingData == null){
                                        log.error("缓存中无当前仓位");
                                        return -1;
                                    }
                                    MatrixItem[][] matrixItems = storeHouses2RollingData.get(tableName);
                                    if (matrixItems == null) {
                                        log.error("缓存中当前仓位未分配空间");
                                        return -1;
                                    }*/

                                    for (Pixel pixel : rasters) {
                                        try {

                                            /* todo
                                            pixel.getX() 是否等于zhuangX*/
                                            //横剖面比较pixel.getX()  纵剖面比较pixel.getY()
//                                            if(zhuang[0].x>pixel.getX()||zhuang[1].x<pixel.getX()||zhuang[1].y>pixel.getY()||zhuang[1].y<pixel.getX()){
//                                                continue;
//                                            }
                                            Coordinate coordinate1 =new Coordinate(pixel.getX(),pixel.getY());
                                            Point point =      geometryFactory.createPoint(coordinate1);
                                        if(line.contains(point)){
                                            int yindex =(int) Math.sqrt(Math.pow(pixel.getX()- zhuang[0].x, 2) +
                                                    Math.pow(pixel.getY()- zhuang[0].y, 2));
                                            System.out.println("数组下标；"+yindex);
                                                            MatrixItem item = matrixItems[0][yindex];//横剖面n  纵剖面m
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


                                                    }


//
//                                            if(flag?pixel.getX() != zhuang:pixel.getY() != zhuang){
//                                                continue;
//                                            }
//                                            if (n >= 0 && m >= 0 && n < height && m < width) {
//                                                MatrixItem item = matrixItems[0][flag?n:m];//横剖面n  纵剖面m
//                                                item.setRollingTimes(item.getRollingTimes() + 1);
//                                                item.getElevationList().add(rollingData02.getElevation());
//                                                item.getAccelerationList().add(rollingData02.getAcceleration());
//                                                item.getFrequencyList().add(rollingData02.getFrequency());
//                                                item.getSpeedList().add(rollingData02.getSpeed());
//                                                item.getTimestampList().add(rollingData02.getTimestamp());
//                                                item.getVehicleIDList().add(rollingData02.getVehicleID());
//                                                item.getVibrateValueList().add(rollingData02.getVibrateValue());
//                                                if (rollingData02.getIsVibrate() == 1) {
//                                                    //动碾遍数+1
//                                                    item.setIsVibrate(item.getIsVibrate() + 1);
//                                                } else if (rollingData02.getIsVibrate() == 0) {
//                                                    //静碾遍数+1
//                                                    item.setIsNotVibrate(item.getIsNotVibrate() + 1);
//                                                }
//                                            }
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                            log.error("(" + m + "," + n + ")像素错误:" + ex.getMessage());
                                        }
                                    }
                                    rasters.clear();
                                    rasters = null;
                                    rollingData01 = rollingData02;
                                    mLastPt = data;
                                }else{
                                   // log.error("无法构建轨迹多边形"+count);
                                }
                            }
                        }
                    }
                    
                   
                }
            }catch (Exception ex){
                log.error("计算错误:" + ex.getMessage());
                return -1;
            }
        }
        rollingDataList.clear();
        rollingDataList = null;
        return 1;
    }
}
