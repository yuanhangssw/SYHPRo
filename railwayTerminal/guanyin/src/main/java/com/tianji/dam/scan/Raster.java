package com.tianji.dam.scan;

import lombok.Data;

@Data
public class Raster {
    private int row;
    private int col;
    private int value;

    public Raster(int row, int col) {
        this(row,col,0);
    }

    public Raster(int row, int col, int value) {
        this.row = row;
        this.col = col;
        this.value = value;
    }

    @Override
    public String toString(){
        return "(" + col + "," + row + ")" + value;
    }
}
