package com.tianji.dam.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Rid计算工具类
 */
public class RidUtil {
    /**
     * 矩阵块大小-默认矩阵为正方形
     */
    public static final int R_LEN = 50;

    /**
     * 矩形块
     * 1m等分多少份 用于扫描线算法步长代表多少
     */
    public static final int STEP_X = 10;

    public static final int STEP_Y = 10;

    /**
     * 矩阵行列求当前矩阵块Rid
     *
     * @param col:X/L
     * @param row:Y/L
     */
    public static long int2Long(int col, int row) {
        return ((long) row & 0xFFFFFFFFL) | (((long) col << 32) & 0xFFFFFFFF00000000L);
    }

    /**
     * 任一点求所在矩阵块RID
     */
    public static long double2Long(double x, double y) {
        int col = col(x);
        int row = row(y);
        return int2Long(col, row);
    }

    /**
     * 求当前点所属列
     */
    public static int col(double x) {
       // return BigDecimal.valueOf(x / R_LEN).setScale(2, RoundingMode.HALF_DOWN).intValue();
           return (int) (x / R_LEN) ;
    }

    /**
     * 求当前点所属行
     */
    public static int row(double y) {
       // return BigDecimal.valueOf(y / R_LEN).setScale(2, RoundingMode.HALF_DOWN).intValue();
         return (int) (y / R_LEN);
    }

    /**
     * 矩阵块RID求左下角横坐标
     */
    public static int long2Left(long rid) {
        return (int) (rid >>> 32) * R_LEN;
    }

    /**
     * 矩阵块RID求右下角横坐标
     */
    public static int long2Right(long rid) {
        return (int) (rid >>> 32) * R_LEN + R_LEN;
    }

    /**
     * 矩阵块RID求左上角纵坐标
     */
    public static int long2Top(long rid) {
        return (int) (rid & 0xFFFFFFFFL) * R_LEN + R_LEN;
    }

    /**
     * 矩阵块RID求左下角纵坐标
     */
    public static int long2Bottom(long rid) {
        return (int) (rid & 0xFFFFFFFFL) * R_LEN;
    }

    /**
     * 矩阵块RID求所属列
     */
    public static int long2Col(long rid) {
        return (int) (rid >>> 32);
    }

    /**
     * 矩阵块RID求所属行
     */
    public static int long2Row(long rid) {
        return (int) (rid & 0xFFFFFFFFL);
    }

    /**
     * 矩阵块RID求左下角栅格列号
     * 用于扫描线算法求栅格
     */
    public static int long2Left4Scale(long rid) {
        return (int) (rid >>> 32) * R_LEN * STEP_X;
    }

    /**
     * 矩阵块RID求左上角栅格行号
     * 用于扫描线算法求栅格
     */
    public static int long2Top4Scale(long rid) {
        return ((int) (rid & 0xFFFFFFFFL) * R_LEN + R_LEN) * STEP_Y;
    }

    /**
     * 栅格行列求矩阵块RID
     * 用于扫描线算法求栅格
     */
    public static long double2Long4Scale(int rasterCol, int rasterRow) {
        int col = col(1.0 * rasterCol / STEP_X);
        int row = row(1.0 * rasterRow / STEP_Y);
        return int2Long(col, row);
    }

    /**
     * 矩阵块宽度像素值
     */
    public static int width() {
        return STEP_X * R_LEN;
    }

    /**
     * 矩阵块高度像素值
     */
    public static int height() {
        return STEP_Y * R_LEN;
    }

    /**
     * 网格宽度大小
     * 用于计算绘图X方向像素比
     */
    public static float gridXSize() {
        return 1.0f / STEP_X;
    }

    /**
     * 网格高度大小
     * 用于计算绘图Y方向像素比
     */
    public static float gridYSize() {
        return 1.0f / STEP_Y;
    }

    /**
     * 获取当前点位周围的九宫格
     * _____________
     * |_6_|_4_|_8_|
     * |_1_|_0_|_2_|
     * |_5_|_3_|_7_|
     */
    public static long[] nineGrid(double x, double y) {
        int index = 0;
        int col = RidUtil.col(x);
        int row = RidUtil.row(y);
        int R_LINECOUNT = 3;
        long[] listrid = new long[R_LINECOUNT * R_LINECOUNT];
        for (int i = 0; i <= R_LINECOUNT / 2; i++) {
            for (int j = 0; j <= R_LINECOUNT / 2; j++) {
                if (i == 0 && j == i) {
                    listrid[index++] = RidUtil.int2Long(col - i, row - j);
                    continue;
                }
                if (i == 0 && j != i) {
                    listrid[index++] = RidUtil.int2Long(col, row - j);
                    listrid[index++] = RidUtil.int2Long(col, row + j);
                    continue;
                }
                if (j == 0 && j != i) {
                    listrid[index++] = RidUtil.int2Long(col - i, row);
                    listrid[index++] = RidUtil.int2Long(col + i, row);
                    continue;
                }
                listrid[index++] = RidUtil.int2Long(col - i, row - j);
                listrid[index++] = RidUtil.int2Long(col + i, row - j);
                listrid[index++] = RidUtil.int2Long(col - i, row + j);
                listrid[index++] = RidUtil.int2Long(col + i, row + j);
            }
        }
        return listrid;
    }

}
