package com.tianji.dam.scan;


import java.util.concurrent.ConcurrentHashMap;

/**
 * 栅格区域实体
 * 代表该区域内所有栅格集
 */
public class RasterData{
    public String rid;
    public float startX;
    public float startY;
    public float endX;
    public float endY;
    //栅格集 Hash<Row,Hash<Col,Raster>>
    public ConcurrentHashMap<Integer,ConcurrentHashMap<Integer,Raster>> rasters = new ConcurrentHashMap<>();
    public Raster getHashRaster(int row,int col){
        ConcurrentHashMap<Integer,Raster> rowRasters = rasters.get(row);
        if(rowRasters != null){
            Raster raster = rowRasters.get(col);
            if(raster != null){
                return raster;
            }
        }
        return null;
    }

    public void offerRaster(int row,int col,Raster raster){
        if(null == rasters.get(row)){
            rasters.put(row,new ConcurrentHashMap<Integer, Raster>());
        }
        rasters.get(row).put(col,raster);
    }

    public RasterData(){}

    public RasterData(String rid){
        this.rid = rid;
    }
}
