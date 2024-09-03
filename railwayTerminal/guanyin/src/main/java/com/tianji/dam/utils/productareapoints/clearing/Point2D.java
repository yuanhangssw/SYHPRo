package com.tianji.dam.utils.productareapoints.clearing;

public class Point2D {
    private double n;
    private double e;

    public Point2D() {
    }

    public Point2D(double n, double e) {
        this.n = n;
        this.e = e;
    }

    public double getN() {
        return n;
    }

    public void setN(double n) {
        this.n = n;
    }

    public double getE() {
        return e;
    }

    public void setE(double e) {
        this.e = e;
    }

    @Override
    public String toString() {
        return "(" + e + "," + n + ")";
    }
}
