package com.tianji.dam.scan;

/**
 * 三维点坐标
 */
public class Point3D {
    private double x;
    private double y;
    private float h;

    public Point3D() {
    }

    public Point3D(double x, double y) {
        this(x,y,0);
    }

    public Point3D(double x, double y, float h) {
        this.x = x;
        this.y = y;
        this.h = h;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + "," + h + ")";
    }
}
