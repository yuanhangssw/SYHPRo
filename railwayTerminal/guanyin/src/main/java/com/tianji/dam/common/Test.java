package com.tianji.dam.common;


import com.tianji.dam.mileageutil.Mileage;

public class Test {

    public static void main(String[] args) {

        Mileage mileage = Mileage.getmileage();

        double[] coord3 = mileage.mileage2Pixel(1, 1200, 50, "0");
        System.out.println(coord3[0] + ">>" + coord3[1]);


    }

}
