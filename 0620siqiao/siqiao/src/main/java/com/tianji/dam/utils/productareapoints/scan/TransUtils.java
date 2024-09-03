package com.tianji.dam.utils.productareapoints.scan;

public class TransUtils {
    private static double rotation=0.0;
    private static double scale =0;
    private static double tx =0;
    private static double ty=0;

    /**
     *
     * @param old1 原始坐标系A点
     * @param old2  原始坐标系B点
     * @param newPointStep1  原始中A点在新坐标系中的坐标
     * @param newPointStep2  原始中B点在新坐标系中的坐标
     */
    public void init(PointStep old1,PointStep old2,PointStep newPoint1,PointStep newPoint2){
         rotation = Math.toRadians(Math.abs(getAngle(old1 , old2 ) - getAngle(newPoint1 , newPoint2 )));
         scale = getScale(newPoint1 , old1 , newPoint2 , old2 );
         tx = getXTranslation(newPoint1 , old1 , rotation, scale);
         ty = getYTranslation(newPoint1 , old1 , rotation, scale);
////需要转换的坐标 x,y,z


    }

    /**
     * 获取两点连线与y轴夹角
     *
     * @param p1 点1
     * @param p2 点2
     * @return 与y轴夹角(角度)
     */
    public  double getAngle(PointStep p1, PointStep p2) {
        double angle = Math.atan2(p2.getX() - p1.getX(), p2.getY() - p1.getY());
        return angle * (180 / Math.PI);
    }

    /**
     * 获取缩放比例
     *
     * @param p1 源点1
     * @param b1 目标点1
     * @param p2 源点2
     * @param b2 目标点2
     * @return 缩放比例
     */
    public double getScale(PointStep p1, PointStep b1, PointStep p2, PointStep b2) {
        return getLength(b1, b2) / getLength(p1, p2);
    }

    /**
     * 获取两点之间连线的长度
     *
     * @param p1 点1
     * @param p2 点2
     * @return 长度
     */
    public static double getLength(PointStep p1, PointStep p2) {
        return Math.sqrt(Math.pow(p2.getX() - p1.getX(), 2) + Math.pow(p2.getY() - p1.getY(), 2));
    }

    /**
     * X方向偏移距离参数
     *
     * @param p1       源点1
     * @param b1       目标点1
     * @param rotation 旋转角度
     * @param scale    缩放比例
     * @return X方向偏移
     */
    public double getXTranslation(PointStep p1, PointStep b1, double rotation, double scale) {
        return (b1.getX() - scale * (p1.getX() * Math.cos(rotation) - p1.getY() * Math.sin(rotation)));
    }

    /**
     * Y方向偏移距离参数
     *
     * @param p1       源点1
     * @param b1       目标点1
     * @param rotation 旋转角度
     * @param scale    缩放比例
     * @return Y方向偏移
     */
    public double getYTranslation(PointStep p1, PointStep b1, double rotation, double scale) {
        return (b1.getY() - scale * (p1.getX() * Math.sin(rotation) + p1.getY() * Math.cos(rotation)));
    }

    /**
     * 坐标转换
     * @param gp
     * @return
     */
    public PointStep transformBoePoint(PointStep gp) {
        double A = scale * Math.cos(rotation);
        double B = scale * Math.sin(rotation);
        return new PointStep(retain6(A * gp.getX() - B * gp.getY() + tx),  retain6(B * gp.getX() + A * gp.getY() + ty));
    }



    /**
     * 保留小数点后六位
     *
     * @param num
     * @return
     */
    public static double retain6(double num) {
        String result = String.format("%.6f", num);
        return Double.valueOf(result);
    }



}