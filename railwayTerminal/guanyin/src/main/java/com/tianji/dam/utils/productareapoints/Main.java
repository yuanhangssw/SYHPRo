package com.tianji.dam.utils.productareapoints;


import com.tianji.dam.utils.productareapoints.clearing.*;

import java.util.List;

/**
 * 压实补录
 */
public class Main {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        //1.录入仓位边界 边界点为平面坐标
        Storehouse storehouse = new Storehouse();
        storehouse.add(new Point2D(0, 0));
        storehouse.add(new Point2D(100, 0));
        storehouse.add(new Point2D(100, 100));
        storehouse.add(new Point2D(0, 100));
        //2.录入压力机轮宽 录入搭接 录入开始时间 录入形式速度 录入合格遍数
        Params params = new Params();
        params.setWidth(2f);
        params.setLap(0.2f);
        params.setStartTime(System.currentTimeMillis());
        params.setMinSpeed(1);
        params.setMaxSpeed(3);
        params.setTarget(4);
        //3.指定仓位起始边AB 找一个最长边作为AB
        Point2D a = new Point2D(0, 0);
        Point2D b = new Point2D(100, 0);
        Mileage mileage = new Mileage(a.getN(), a.getE(), b.getN(), b.getE());
        //4.补录数据
        Clearing clearing = new Clearing();
        clearing.setParams(params);
        clearing.setStorehouse(storehouse);
        clearing.setMileage(mileage);
        List<Point2DWrapper> pts = clearing.clearing(1f);
        //5.包装轨迹点
        long stop = System.currentTimeMillis();
        System.out.println("生成时长:" + (stop - start) + "ms" + " 生成数据:" + pts.size());
        Point2D lastPt = null;
        long startTime = params.getStartTime();
        for (int i = 0; i < pts.size(); i++) {
            Point2D pt = pts.get(i).getPoint2D();
            if (lastPt == null) {
                lastPt = pt;
            } else {
                double speed = (Math.random() * (params.getMaxSpeed() - params.getMinSpeed()) + params.getMinSpeed()) / 3.6;
                double dltaN = pt.getN() - lastPt.getN();
                double dltaE = pt.getE() - lastPt.getE();
                double dis = Math.sqrt(Math.pow(dltaN, 2) + Math.pow(dltaE, 2));
                long time = (long) (dis / speed * 1000);
                Track track = new Track();
                track.e = pt.getE();
                track.n = pt.getN();
                track.speed = speed * 3.6;
                track.time = (startTime + time);
                System.out.println(track.toString());
            }
        }
    }
}
