package com.tianji.dam.mileageutil;

/**
 * 坐标法
 * 线段实体
 */
public class Line {
    //起点 桩号0+000的坐标
    private double NS = 5103773.9170;
    private double ES = 400521.5020;
    //终点 桩号x+xxx的坐标
    private double NE = 5103762.2340;
    private double EE = 400853.9960;
    //起点桩号
    private double DKS = 0;
    //终点桩号
    private double DKE = 332.699;
    //直线方程系数 AX+BY+C=0
    private double A;
    private double B;
    private double C;

    public Line(double NS, double ES, double NE, double EE, double DKS, double DKE) {
        this.NS = NS;
        this.ES = ES;
        this.NE = NE;
        this.EE = EE;
        A = NS - NE;
        B = EE - ES;
        C = -EE * NS + ES * NE;
        this.DKS = DKS;
        this.DKE = DKE;
    }

    public double getNS() {
        return NS;
    }

    public void setNS(double NS) {
        this.NS = NS;
    }

    public double getES() {
        return ES;
    }

    public void setES(double ES) {
        this.ES = ES;
    }

    public double getNE() {
        return NE;
    }

    public void setNE(double NE) {
        this.NE = NE;
    }

    public double getEE() {
        return EE;
    }

    public void setEE(double EE) {
        this.EE = EE;
    }

    public double getDKS() {
        return DKS;
    }

    public void setDKS(double DKS) {
        this.DKS = DKS;
    }

    public double getDKE() {
        return DKE;
    }

    public void setDKE(double DKE) {
        this.DKE = DKE;
    }

    public double getA() {
        return A;
    }

    public void setA(double a) {
        A = a;
    }

    public double getB() {
        return B;
    }

    public void setB(double b) {
        B = b;
    }

    public double getC() {
        return C;
    }

    public void setC(double c) {
        C = c;
    }

    @Override
    public String toString() {
        return "Line{" +
                "NS=" + NS +
                ", ES=" + ES +
                ", NE=" + NE +
                ", EE=" + EE +
                ", DKS=" + DKS +
                ", DKE=" + DKE +
                ", A=" + A +
                ", B=" + B +
                ", C=" + C +
                '}';
    }
}
