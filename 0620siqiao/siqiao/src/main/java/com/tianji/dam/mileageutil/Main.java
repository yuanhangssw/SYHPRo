package com.tianji.dam.mileageutil;


public class Main {

    public static void main(String[] args) {
        try {

            Mileage mileage = Mileage.getmileage();
            double pix_x = 1767;
            double pix_y = 6757;
            double[] p2c = mileage.pixels2Coord2(1, pix_x, pix_y);

            System.out.println("平面坐标:" + p2c[0] + "," + "》》》:" + p2c[1]);


            //2.平面坐标转偏距里程
            double[] value = mileage.coord2Mileage2(4358145.1247, 510639.0548, "0");
            System.out.println("里程:" + value[0] + "," + "偏距:" + value[1]);
            //3.偏距里程转平面坐标

            double[] coord = mileage.mileage2Coord(value[0], value[1], "10");
            System.out.println("N:" + coord[0] + "," + "E:" + coord[1]);
        } catch (Exception ex) {
            System.out.println("错误:" + ex.getMessage());
        }


    }
}
