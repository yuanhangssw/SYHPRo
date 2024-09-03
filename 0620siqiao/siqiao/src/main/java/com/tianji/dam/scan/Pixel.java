package com.tianji.dam.scan;

import lombok.Data;

@Data
public class Pixel {
    private int x;
    private int y;

    public Pixel(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Pixel(){}

    @Override
    public String toString(){
        return "(" + x + "," + y + ")";
    }
}
