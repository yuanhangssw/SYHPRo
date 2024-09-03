package com.tianji.dam.utils.productareapoints.scan;

public class PointStep {
    public double x;
    public double y;

    public PointStep(double x, double y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
