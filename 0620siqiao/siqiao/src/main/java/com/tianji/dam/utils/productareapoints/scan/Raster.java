package com.tianji.dam.utils.productareapoints.scan;

public class Raster {
    public int row;
    public int col;
    public int dir;//0正向 1反向 正向时变道

    public Raster( int col, int row,int dir) {
        this.row = row;
        this.col = col;
        this.dir = dir;
    }
}
