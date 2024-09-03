package com.tianji.dam.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class RandomUtiles {

    public static double randomdouble(double begin, double end) {
        Random r = new Random();

        double d3 = r.nextDouble() * (end - begin) + begin;

        Double rs = new BigDecimal(d3).setScale(2, RoundingMode.HALF_UP).doubleValue();
        //System.out.println(rs);
        return rs;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            System.out.println(randomdouble(2.5, 2.8));
            ;
        }

    }

}
