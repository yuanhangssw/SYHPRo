package com.tianji.dam.utils;

/**
 * 轨迹绘图常量
 */
public class TrackConstant {
    /**
     * 轨迹播放间隔 毫秒
     */

    public static final Double x0 = 3216950.6158D;//北方向
    public static final Double y0 = 379703.6414D;//东方下
    public static Double alpha = 0d;//顺时针旋转角度，弧度制d
    public static Double kk = 0.100;//缩放比

    public static final int INTERVAL = 1000;
    /**
     * 前后轨迹点自大距离
     */
    public static final int MAX_DIS = 100;
    /**
     * 前后轨迹点最小距离
     */
    public static final int MIN_DIS = 0;

    public static final double WHEEL_LEFT = 12d;

    public static final double WHEEL_RIGHT = 12d;

    //剖面分析使用下方四个参数
    public static final Double BACKIMG_HEIGT = 5402D;

    public static final Double BACKIMG_WIDTH = 7853D;
    //最小高层
    public static final Double BEGIN_GAOCHENG = 195D;
    //最大高层
    public static final Double BACKIMG_MAXGAOCHENG = 255D;
}
