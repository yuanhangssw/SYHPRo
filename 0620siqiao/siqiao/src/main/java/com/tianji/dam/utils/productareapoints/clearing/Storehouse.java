package com.tianji.dam.utils.productareapoints.clearing;

import java.util.LinkedList;import java.util.ArrayList;
import java.util.List;

/**
 * 仓位实体
 * 包含仓位的边界点信息
 */
public class Storehouse {
    private List<Point2D> pts = new LinkedList<>();

    public Storehouse() {
    }

    public List<Point2D> getPts() {
        return pts;
    }

    public void setPts(List<Point2D> pts) {
        this.pts = pts;
    }

    public void add(Point2D pt){
        pts.add(pt);
    }
}