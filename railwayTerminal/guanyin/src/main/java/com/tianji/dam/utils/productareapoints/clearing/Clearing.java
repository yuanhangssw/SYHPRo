package com.tianji.dam.utils.productareapoints.clearing;


import com.tianji.dam.utils.productareapoints.scan.Point;
import com.tianji.dam.utils.productareapoints.scan.Raster;
import com.tianji.dam.utils.productareapoints.scan.ScanManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 补录实体
 */
public class Clearing {
    private Storehouse storehouse;
    private Params params;
    private Mileage mileage;

    public Storehouse getStorehouse() {
        return storehouse;
    }

    public void setStorehouse(Storehouse storehouse) {
        this.storehouse = storehouse;
    }

    public Params getParams() {
        return params;
    }

    public void setParams(Params params) {
        this.params = params;
    }

    public Mileage getMileage() {
        return mileage;
    }

    public void setMileage(Mileage mileage) {
        this.mileage = mileage;
    }

    /**
     * @param resolution 分辨率 多远距离采集一个点 单位m
     */
    public List<Point2DWrapper> clearing(float resolution) {
        //4.根据ab边 生成AB坐标系 并将仓位转换为AB坐标系 AB坐标系X轴向上平移半个轮迹
        float trans = params.getWidth() / 2;
        int size = storehouse.getPts().size();
        Point[] polygon = new Point[size];
        //基础放大倍数
        int scale = 10;
        //stepX表示为前后轨迹点距离
        int stepX = (int) (resolution * scale);
        //stepY表示为轮宽-搭接
        int stepY = (int) ((params.getWidth() - params.getLap()) * scale);
        for (int i = 0; i < size; i++) {
            Point2D pt = storehouse.getPts().get(i);
            double[] ptAB = {pt.getN(),pt.getE()};
            int x = (int) ((ptAB[0]) * scale);
            int y = (int) ((ptAB[1]) * scale);
            polygon[i] = (new Point(x, y));
        }
        //5.扫描获得轨迹点
//        Arrays.stream(polygon).forEach(System.out::println);
        List<Raster> rasters = ScanManager.scanRaster(polygon, stepX, stepY);
        polygon = null;
        //6.轨迹点转为平面坐标
        List<Point2DWrapper> tracks = new LinkedList<>();

        for (Raster raster : rasters) {
            double x = raster.col * 1.0 / scale;
            double y = (raster.row * 1.0 / scale);
            Point2D pt = new Point2D(x, y);
            Point2DWrapper wrapper = new Point2DWrapper();
            wrapper.setPoint2D(pt);
            wrapper.setDirect(raster.dir);
            tracks.add(wrapper);
        }
        //7.根据合格遍数对轨迹点排序
        int trackSize = rasters.size();
        rasters.clear();
        rasters = null;
        //倍率-目标遍数-params.getTarget();
        int power = 1;
        List<Point2DWrapper> allTracks = new ArrayList<>(trackSize * power);
        for (int i = 0; i < power; i++) {
            //偶数时直接将轨迹点添加到大集合中 奇数时将轨迹点倒叙添加到大集合中
            boolean isOdd = i % 2 == 0;
            if (isOdd) {
                for (int index = 0; index < trackSize; index++) {
                    Point2DWrapper pt = tracks.get(index);
                    allTracks.add(pt);
                }
            } else {
                for (int index = trackSize - 1; index >= 0; index--) {
                    Point2DWrapper pt = tracks.get(index);
                    allTracks.add(pt);
                }
            }
        }
        tracks.clear();
        return allTracks;
    }
}
