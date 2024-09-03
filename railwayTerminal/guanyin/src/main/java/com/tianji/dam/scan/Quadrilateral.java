package com.tianji.dam.scan;

/**
 * 前后滚轮所形成的多边形
 */
public class Quadrilateral {
    public Point2D pt1 = new Point2D();

    public Point2D pt2= new Point2D();

    public Point2D pt3= new Point2D();

    public Point2D pt4= new Point2D();

    public Point2D getCenterPoint(){
        Point2D pt = new Point2D();
        pt.setX((pt1.getX()+pt3.getX())/2);
        pt.setY((pt1.getY()+pt3.getY())/2);
        return pt;
    }
}
