package com.tianji.dam.model;



import java.util.concurrent.ConcurrentHashMap;

import com.tianji.dam.domain.MatrixItem;
import com.tianji.dam.domain.RollingData;
import com.tianji.dam.scan.Pixel;
import com.tianji.dam.scan.PointCpb;
import com.tianji.dam.scan.Quadrilateral;
import com.tianji.dam.scan.WheelMarkData;

import lombok.Data;

@Data
public class RealTimeRedisDataModel {

    ConcurrentHashMap<Long, MatrixItem[][]> cache;
    RollingData rollingData01;
    RollingData rollingData02;
    WheelMarkData mLastPt;
    Quadrilateral ql;
    PointCpb[] convex;

    PointCpb[] ps;
    PointCpb ps1;
    PointCpb ps2;
    PointCpb ps3;
    PointCpb ps4;
    Pixel[] polygon;
    Pixel polygon1;
    Pixel polygon2;
    Pixel polygon3;
    Pixel polygon4;

    public RealTimeRedisDataModel() {
    }

    public RealTimeRedisDataModel(ConcurrentHashMap<Long, MatrixItem[][]> cache, RollingData rollingData01, RollingData rollingData02, WheelMarkData mLastPt, Quadrilateral ql, PointCpb[] convex, PointCpb[] ps, PointCpb ps1, PointCpb ps2, PointCpb ps3, PointCpb ps4, Pixel[] polygon, Pixel polygon1, Pixel polygon2, Pixel polygon3, Pixel polygon4) {
        this.cache = cache;
        this.rollingData01 = rollingData01;
        this.rollingData02 = rollingData02;
        this.mLastPt = mLastPt;
        this.ql = ql;
        this.convex = convex;
        this.ps = ps;
        this.ps1 = ps1;
        this.ps2 = ps2;
        this.ps3 = ps3;
        this.ps4 = ps4;
        this.polygon = polygon;
        this.polygon1 = polygon1;
        this.polygon2 = polygon2;
        this.polygon3 = polygon3;
        this.polygon4 = polygon4;
    }

    public RealTimeRedisDataModel(ConcurrentHashMap<Long, MatrixItem[][]> cache) {
        this.cache = cache;
    }
}
