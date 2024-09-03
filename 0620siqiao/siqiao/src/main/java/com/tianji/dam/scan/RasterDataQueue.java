package com.tianji.dam.scan;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 栅格缓冲区
 * TODO:等同StoreHouses2RollingData
 */
public class RasterDataQueue {
    private AtomicInteger count = new AtomicInteger(0);
    private ConcurrentHashMap<String,RasterData> dataQueue = new ConcurrentHashMap<>();
    public ConcurrentHashMap<String,RasterData> getDataQueue(){
        return dataQueue;
    }

    /**
     * 返回队列中栅格数
     * @return 队列中栅格数
     */
    public int getCount(){
        return count.get();
    }
    /**
     * 重置栅格
     */
    public void reset(){
        count.set(0);
        dataQueue.clear();
    }
    /**
     * 添加栅格数据到队列中
     * @param rid 单元工程
     * @param raster 栅格实体
     */
    public void put(String rid,Raster raster){
        if(null == dataQueue.get(rid)){
            dataQueue.put(rid,new RasterData(rid));
        }
        dataQueue.get(rid).offerRaster(raster.getRow(),raster.getCol(),raster);
        count.incrementAndGet();
    }
}
