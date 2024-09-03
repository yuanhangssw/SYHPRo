package com.tianji.dam.mileageutil;


import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.domain.TAnalysisConfig;
import com.tianji.dam.domain.TDesign;
import com.tianji.dam.mapper.CommonMapper;
import com.tianji.dam.utils.TrackConstant;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Mileage {

    //底图左上点x坐标
    private static double E0 = TrackConstant.x0;
    //底图左上角点y坐标
    public static double N0 = TrackConstant.y0;
    //逆时针为正 角度[0,360]
    public static double ALPHA = TrackConstant.alpha;
    //缩放比
    public static double SCALE = TrackConstant.kk;
    private static Mileage mileage;

    /**
     * 坐标法-线段集
     */

    private Map<String, List<Line>> alllines = new HashMap<String, List<Line>>();

    public static Mileage getmileage() {
        if (null == mileage) {
            mileage = new Mileage();
        }
        return mileage;
    }

    private Mileage() {

        double n0 = 0;
        double e0 = 0;
        CommonMapper mapper = BeanContext.getApplicationContext().getBean(CommonMapper.class);

        //查询初始参数
        TAnalysisConfig config = mapper.getconfig();
        E0 = config.getFourParamX();
        N0 = config.getFourParamY();
        ALPHA = config.getFourParamAngle();
        SCALE = config.getFourParamFactor();
        //获取所有类型
        List<String> alltype = mapper.getalltype();
        for (String string : alltype) {
            List<Line> lines = new LinkedList<Line>();
            List<PointTest> pts = new LinkedList<>();
            List<TDesign> allpoint = mapper.getallpointbytype(string);

            for (int i = 1; i < allpoint.size(); i++) {
                TDesign current = allpoint.get(i);
                TDesign last = allpoint.get(i - 1);
                double dks = Tools.dk2Mileage(last.getMileage());
                double dke = Tools.dk2Mileage(current.getMileage());
                Line line = new Line(last.getY() - n0, last.getX() - e0, current.getY() - n0, current.getX() - e0, dks, dke);
                lines.add(line);
            }
            alllines.put(string, lines);
        }

    }

    /**
     * 平面坐标转图标像素坐标
     *
     * @param CoordX: 平面坐标东
     * @param CoordY: 平面坐标北
     * @return 桩号
     */

    public double[] coord2pixel(double CoordX, double CoordY) {
        double zhuangY = -1 * (((CoordX - E0) / SCALE) * Math.cos(ALPHA) + ((CoordY - N0) / SCALE) * Math.sin(ALPHA));
        double zhuangX = ((CoordX - E0) / SCALE) * Math.sin(ALPHA) + ((CoordY - N0) / SCALE) * Math.cos(ALPHA);
        return new double[]{zhuangX, zhuangY};
    }


    /**
     * 里程偏距转平面坐标
     *
     * @param mileage:桩号
     * @param offset:里程
     * @return 平面坐标 dst[0]东方向坐标 dst[1]北方向坐标
     */
    public double[] mileage2Coord(double mileage, double offset, String type) {
        Line line = getLine4Mileage(mileage, offset, type);
        System.out.println("所在线段。。。");
        System.out.println(line.toString());
        double DKS = line.getDKS();
        double EE = line.getEE();
        double ES = line.getES();
        double NE = line.getNE();
        double NS = line.getNS();
        mileage = mileage - DKS;
        double x2 = 0;
        double y2 = 1;
        double x1 = EE - ES;
        double y1 = NE - NS;
        //tanO =(a叉乘b) / (a点乘b) = sin0/cos0
        double a = x1 * y2 - x2 * y1;//a点乘b
        double b = x1 * x2 + y1 * y2;//a叉乘b

        double angle = Math.atan2(a, b);
        double aaa = Math.toDegrees(angle);
        //x' = (XcosA + YsinA)  y' = (YcosA - XsinA)
        double s = mileage * Math.cos(angle) - offset * Math.sin(angle);
        //偏距沿小里程到大里程方向 左负右正
        double t = offset * Math.cos(angle) + mileage * Math.sin(angle);
        double[] dst = new double[]{s, t};
        dst[0] += NS;
        dst[1] += ES;
        return dst;
    }

    /**
     * 平面坐标转里程偏距
     *
     * @param n:北方向坐标
     * @param e:东方向坐标
     * @return dst dst[0]桩号 dst[1]偏距
     */
    public double[] coord2Mileage(double n, double e, String type) {
        Line line = getLine4Coord(n, e, type);
        double DKS = line.getDKS();
        double EE = line.getEE();
        double ES = line.getES();
        double NE = line.getNE();
        double NS = line.getNS();
        double x = e - ES;
        double y = n - NS;

        double x2 = 0;
        double y2 = 1;
        double x1 = EE - ES;
        double y1 = NE - NS;

        //tanO =(a叉乘b) / (a点乘b) = sin0/cos0
        double a = x1 * y2 - x2 * y1;//a点乘b
        double b = x1 * x2 + y1 * y2;//a叉乘b
        double angle = -1 * Math.atan2(a, b);
        //x' = (XcosA + YsinA)  y' = (YcosA - XsinA)
        double mileage = DKS + y * Math.cos(angle) - x * Math.sin(angle);
        //偏距沿小里程到大里程方向 左负右正
        double offset = x * Math.cos(angle) + y * Math.sin(angle);
        double[] dst = new double[]{mileage, offset};
        return dst;
    }

    public double[] coord2Mileage2(double n, double e, String type) {

        List<Line> nowline = alllines.get(type);

        if (nowline.size() == 0) {
            throw new IllegalArgumentException("坐标法-线段集合为空 无法计算");
        }
        double[] dst = new double[]{0, 0};
        for (Line line : nowline) {
            double DKS = line.getDKS();
            double DKE = line.getDKE();
            double EE = line.getEE();
            double ES = line.getES();
            double NE = line.getNE();
            double NS = line.getNS();
            double x = e - ES;
            double y = n - NS;

            double x2 = 0;
            double y2 = 1;
            double x1 = EE - ES;
            double y1 = NE - NS;

            //tanO =(a叉乘b) / (a点乘b) = sin0/cos0
            double a = x1 * y2 - x2 * y1;//a点乘b
            double b = x1 * x2 + y1 * y2;//a叉乘b
            double angle = -1 * Math.atan2(a, b);
            //x' = (XcosA + YsinA)  y' = (YcosA - XsinA)
            double mileage = DKS + y * Math.cos(angle) - x * Math.sin(angle);
            //偏距沿小里程到大里程方向 左负右正
            double offset = x * Math.cos(angle) + y * Math.sin(angle);
            if (mileage <= DKE && mileage >= DKS) {
                //在当前线段里程范围内
                dst[0] = mileage;
                dst[1] = offset;
                break;
            }
        }
        return dst;
    }

    /**
     * 桩号偏距转像素坐标
     *
     * @param type:底图坐标原点位置 0:左下 1:左上 2:右上 3:右下
     * @param mileage:桩号
     * @param offset:偏距
     * @return pixels:像素坐标 像素坐标x pixels[0] 像素坐标y pixels[1]
     */
    public double[] mileage2Pixel(int type, double mileage, double offset, String linetype) {
        double[] pixels = new double[]{-1, -1};
        //1.桩号偏距转平面坐标
        double[] coord = mileage2Coord(mileage, offset, linetype);

        System.out.println(coord[0] + ">>" + coord[1]);

        double north = coord[0];
        double east = coord[1];
        double x = east - E0;
        double y = north - N0;
        double radians = Math.toRadians(ALPHA);
        //2.根据平面坐标与底图的四参数求得像素坐标
        //s = (XcosA - YsinA)  t = (YcosA + XsinA)
        switch (type) {
            case 0: {
                break;
            }
            case 1: {
                //前端绘图 使用底图的Height像素 - pixels[1]来表示该点在底图上的实际像素位置
                pixels[1] = -1 * ((x / SCALE)) * Math.cos(radians) - (y / SCALE) * Math.sin(radians);
                pixels[0] = 1 * ((y / SCALE) * Math.cos(radians) + (x / SCALE) * Math.sin(radians));
                break;
            }
            case 2: {
                break;
            }
            case 3: {
                break;
            }
            default: {
                break;
            }
        }
        return pixels;
    }

    /**
     * 像素坐标转平面坐标
     *
     * @param type:底图坐标原点
     * @param pixelx:像素坐标x
     * @param pixely:像素坐标y
     * @return 平面坐标 coord[0]东方向坐标 coord[1] 北方向坐标
     */
    public double[] pixels2Coord(int type, double pixelx, double pixely) {
        double[] coord = new double[]{E0, N0};
        pixelx *= -1;
        switch (type) {
            case 1: {
                //s=(XcosA - YsinA)  t = (YcosA + XsinA)
                //源 像素坐标 目标 平面坐标
                double angle = Math.toRadians(360 - ALPHA);
                double x = (pixelx * SCALE) * Math.cos(angle) - (pixely * SCALE) * Math.sin(angle);
                double y = (pixely * SCALE) * Math.cos(angle) + (pixelx * SCALE) * Math.sin(angle);
                coord[0] += x;
                coord[1] += y;
                break;
            }
            case 2:
            case 3:
            case 0:
            default: {
                break;
            }
        }
        return coord;
    }

    public double[] pixels2Coord2(int type, double pixelx, double pixely) {
        double[] coord = new double[]{N0, E0};
        pixely *= -1;
        switch (type) {
            case 0: {
                break;
            }
            case 1: {
                //s=(XcosA - YsinA)  t = (YcosA + XsinA)
                //源 像素坐标 目标 平面坐标
                double angle = Math.toRadians(360 - ALPHA);
                double x = (pixelx * SCALE) * Math.cos(angle) - (pixely * SCALE) * Math.sin(angle);
                double y = (pixely * SCALE) * Math.cos(angle) + (pixelx * SCALE) * Math.sin(angle);
                coord[0] += x;
                coord[1] += y;
                break;
            }
            case 2: {
                break;
            }
            case 3: {
                break;
            }
            default: {
                break;
            }
        }
        return coord;
    }

    /**
     * 像素坐标转桩号偏距
     *
     * @param pixelx:像素坐标x
     * @param pixely:像素坐标y
     * @return mileages[] mileages[0]桩号 mileages[1]偏距
     */
    public double[] pixels2Mileage(double pixelx, double pixely, String type) {
        double[] coord = pixels2Coord(1, pixelx, pixely);
        for (int i = 0; i < coord.length; i++) {
            double d = coord[i];
            System.out.println(d);
        }
        double[] mileages = coord2Mileage2(coord[0], coord[1], type);
        return mileages;
    }

    public double[] pixels2Mileage2(double pixelx, double pixely, String type) {
        double[] coord = pixels2Coord(1, pixelx, pixely);
        for (int i = 0; i < coord.length; i++) {
            double d = coord[i];
            System.out.println(d);
        }
        double[] mileages = coord2Mileage2(coord[1], coord[0], type);
        return mileages;
    }

    /**
     * 获取平面坐标所在线段
     */
    private Line getLine4Coord(double n, double e, String type) {

        List<Line> templines = alllines.get(type);

        if (templines.size() == 0) {
            throw new IllegalArgumentException("坐标法-线段集合为空 无法计算");
        }
        Line minLine = templines.get(0);
        double minDis = dis(minLine, n, e);
        for (int i = 1; i < templines.size(); i++) {
            Line line = templines.get(i);
            double dis = dis(line, n, e);
            if (dis < minDis) {
                //todo:单使用距离作为判断 在超出里程范围 或 俩线段转角处会判断错误 该bug后期解决
                minLine = line;
            }
        }
        System.out.println("最近线段:" + minLine.toString());
        return minLine;
    }

    /**
     * 求点(e,n)到直线的最短距离
     */
    private double dis(Line line, double n, double e) {
        double A = line.getA();
        double B = line.getB();
        double C = line.getC();
        double dis = Math.abs((A * e + B * n + C) / Math.sqrt(Math.pow(A, 2) + Math.pow(B, 2)));
        return dis;
    }

    /**
     * 获取桩号偏距所在线段
     */
    private Line getLine4Mileage(double mileage, double offset, String type) {
        Line line = null;

        List<Line> templines = alllines.get(type);
        for (Line l : templines) {
            double dks = l.getDKS();
            double dke = l.getDKE();
            if (mileage >= dks && mileage <= dke) {
                line = l;
                break;
            }
        }
        //  System.out.println("所在线段:" + line.toString());
        return line;
    }

    public static void main(String[] args) {
        Mileage m = getmileage();
        double[] x = m.coord2pixel(3249790.661d, 634555.4709d);


    }


}
