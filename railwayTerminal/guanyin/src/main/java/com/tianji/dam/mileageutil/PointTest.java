package com.tianji.dam.mileageutil;

public class PointTest {
    private String dk;
    private double n;
    private double e;

    public PointTest() {
    }

    public PointTest(double dk, double n, double e) {
        this.dk = Tools.mileage2Dk(dk,"L");
        this.n = n;
        this.e = e;
    }

    public String getDk() {
        return dk;
    }

    public void setDk(String dk) {
        this.dk = dk;
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
        String info = getDk() + " (" + n + "," + e + ")";
        return info;
    }
}
