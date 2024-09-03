package com.tianji.dam.Test;


import com.tianji.dam.scan.Point2D;

public class TestR {


    public static void main(String[] args) {
    while (true) {
        Point2D a = new Point2D();
        a.setX(806);
        a.setY(342);

        Point2D b = new Point2D();
        b.setX(4538);
        b.setY(2646);
        double distance = Math.sqrt(Math.pow(b.getX() - a.getX(), 2)
                + Math.pow(b.getY() - a.getY(), 2)); // 距离公式

        System.out.println(distance);

    }
    }

}
