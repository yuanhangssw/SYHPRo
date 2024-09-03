package com.tianji.dam.scan;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import com.tianji.dam.domain.TTrackPoint;
import com.tianji.dam.utils.TrackConstant;

/**
 * 轨迹处理类
 * TODO:等同CalculateGridForZhuang
 */
@Slf4j
public class TrackProcess implements Callable<Integer> {
    /**
     * X方向步长
     */
    private static final float STEPX = 0.1f;

    /**
     * Y方向步长
     */
    private static final float STEPY = 0.1f;

    /**
     * 单元工程内的轨迹点
     */
    private List<TTrackPoint> trackPoints;

    public void setTrackPoints(List<TTrackPoint> trackPoints) {
        this.trackPoints = trackPoints;
    }

    /**
     * 单元工程的碾压格网
     */
    private RasterData rasterData;

    public void setRasterData(RasterData rasterData) {
        this.rasterData = rasterData;
    }

    @Override
    public Integer call() throws Exception {
        if(trackPoints.size() < 2){
            return -1;
        }
        Iterator<TTrackPoint> iterable = trackPoints.iterator();
        TTrackPoint first = null;
        TTrackPoint second = null;
        while (iterable.hasNext()){
            if(first == null){
                first = iterable.next();
                continue;
            }
            second = iterable.next();
            //根据前后俩个点生成轨迹多边形
            Quadrilateral re = cpbDraw(first,second);
            if(re == null){
                first = second;
                continue;
            }
            Pixel[] polygon = new Pixel[4];
            polygon[0] = new Pixel((int)(re.pt1.getX() * STEPX),(int)(re.pt1.getY() * STEPY));
            polygon[1] = new Pixel((int)(re.pt2.getX() * STEPX),(int)(re.pt2.getY() * STEPY));
            polygon[2] = new Pixel((int)(re.pt3.getX() * STEPX),(int)(re.pt3.getY() * STEPY));
            polygon[3] = new Pixel((int)(re.pt4.getX() * STEPX),(int)(re.pt4.getY() * STEPY));
            List<Pixel> pixels = Scan.scanRaster(polygon);
            //todo:从rasterData中加载缓存，然后循环pixels，更新缓存内容
            for(Pixel pixel : pixels){
                if(rasterData == null){
                    break;
                }
                int left = (int) (rasterData.startX * STEPX);
                int top = (int) (rasterData.endY * STEPY);
                //根据行列号获得当前区域内对应栅格点 并对栅格点进行更新操作
                int row =(top - pixel.getY());
                int col = (pixel.getX() - left);
                Raster raster = rasterData.getHashRaster(row,col);
                if(raster == null){
                    raster = new Raster(row,col);
                    int passCount = raster.getValue() + 1;
                    raster.setValue(passCount);
                    ConcurrentHashMap<Integer,Raster> rowHash = rasterData.rasters.get(row);
                    if(rowHash == null){
                        rasterData.rasters.put(row,new ConcurrentHashMap<Integer, Raster>());
                    }
                    rasterData.rasters.get(row).put(col,raster);
                }else{
                    int passCount = raster.getValue() + 1;
                    raster.setValue(passCount);
                }
            }
            pixels.clear();
            pixels = null;

            first = second;
        }
        return -1;
    }

    private Quadrilateral cpbDraw(TTrackPoint first, TTrackPoint second){
        double lx = first.getZhuangLX();
        double ly = first.getZhuangLY();
        double cx = second.getZhuangRX();
        double cy = second.getZhuangRY();
        double dis = Math.sqrt(Math.pow(lx-cx,2) + Math.pow(ly-cy,2));
        //前后距离判断 如果前后距离过远 直接丢弃
        if(dis < TrackConstant.MIN_DIS){
            log.warn("前后俩点距离过小");
        }else if(dis < TrackConstant.MAX_DIS){
            PointCpb[] ps = new PointCpb[]{
                    new PointCpb(first.getZhuangLX(), first.getZhuangLY()),
                    new PointCpb(first.getZhuangRX(), first.getZhuangRY()),
                    new PointCpb(second.getZhuangRX(),second.getZhuangRY()),
                    new PointCpb(second.getZhuangLX(),second.getZhuangLY())
            };
            PointCpb[] convex = new PointCpb[4];
            int count = Algorithm.getPointsConvexClosure(ps, 0, 4, convex);
            if (count == 4) {
                Quadrilateral ql = new Quadrilateral();
                ql.pt1 = new Point2D(convex[0].x, convex[0].y);
                ql.pt2 = new Point2D(convex[1].x, convex[1].y);
                ql.pt3 = new Point2D(convex[2].x, convex[2].y);
                ql.pt4 = new Point2D(convex[3].x, convex[3].y);
                return ql;
            }else{
                log.warn("无法形成四边形:" + second.getOrderNum());
            }
        }else{
            log.warn("前后俩点距离过远:" + second.getOrderNum());
        }
        return null;
    }
}
