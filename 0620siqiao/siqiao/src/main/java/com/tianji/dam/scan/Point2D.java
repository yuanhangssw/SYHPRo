package com.tianji.dam.scan;

import lombok.Data;

/**
 * 二维平面点
 */
@Data
public class Point2D {
    private double x;
    private double y;

    public Point2D() {
    }

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
