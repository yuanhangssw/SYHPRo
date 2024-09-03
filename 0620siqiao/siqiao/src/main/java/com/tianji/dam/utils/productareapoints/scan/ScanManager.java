package com.tianji.dam.utils.productareapoints.scan;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 扫描线算法核心
 */
public class ScanManager {
    //扫描栅格
    public static List<Raster> scanRaster(Point[] polygon, int stepX, int stepY) {
        //初始化最大最小 界定扫描线的范围
        int ymax = polygon[0].y;
        int ymin = polygon[0].y;
        for (Point point : polygon) {
            int y = point.y;
            if (y > ymax) {
                ymax = y;
            }
            if (y < ymin) {
                ymin = y;
            }
        }
        //多边形栅格集
        List<Raster> rasters = new LinkedList<>();
        NET net = new NET(ymax - ymin + 1);
        //初始化新边表 Net中的第一个元素对应的是ymin所在的扫描线
        int edges = polygon.length;
        for (int i = 0; i < edges; i++) {
            Point ps = polygon[i];
            Point pe = polygon[(i + 1) % edges];
            Point pss = polygon[((i - 1) + edges) % edges];
            Point pee = polygon[(i + 2) % edges];
            //不处理水平线
            if (pe.y != ps.y) {
                ET e = new ET();
                e.dx = ((pe.x - ps.x) * 1f) / (pe.y - ps.y);//扫描线步长 0.1m扫描一次
                if (pe.y > ps.y) {
                    e.xi = ps.x;
                    if (pee.y >= pe.y) {
                        e.ymax = (pe.y - ymin) - 1;
                    } else {
                        e.ymax = (pe.y - ymin);
                    }
                    net.get((ps.y - ymin)).add(e);
                } else {
                    e.xi = pe.x;
                    if (pss.y >= ps.y) {
                        e.ymax = (ps.y - ymin) - 1;
                    } else {
                        e.ymax = (ps.y - ymin);
                    }
                    net.get((pe.y - ymin)).add(e);
                }
            }
        }
        //扫描线填充
        AET aet = new AET();
        for (int y = ymin; y <= ymax; y++) {
            try {

                //将扫描线对应的所有新边插入到aet中
                aet.add(net.get(y - ymin));
                //插入操作到保证aet还是有序表 根据ET的xi从小到大排序
                Collections.sort(aet.getAll());
                //执行具体填充动作，将aet中的边交点对取出组成填充区间 根据左闭右开原则对每个区间进行填充
                if (aet.getAll().size() < 2) {
                    //aet中只有一个活性表不需要填充
                    continue;
                } else {
                    //边交点成对填充 12 34 56
                    int fillingLength = aet.getAll().size();
                    for (int j = 0; j < fillingLength; j += 2) {
                        int from = (int) aet.get(j).xi;
                        int to = (int) aet.get(j + 1).xi;
                        if (y % stepY == 0) {
                            for (int x = from; x <= to; x++) {

                                //将多边形所在栅格添加到区域栅格集中
                                if (x % stepX == 0) {
                                    rasters.add(new Raster(x, y, 0));
                                }
                            }
                            for (int x = to; x >= from; x--) {
                                //将多边形所在栅格添加到区域栅格集中
                                if (x % stepX == 0) {
                                    rasters.add(new Raster(x, y, 1));
                                }
                            }
                            for (int x = from; x <= to; x++) {

                                //将多边形所在栅格添加到区域栅格集中
                                if (x % stepX == 0) {
                                    rasters.add(new Raster(x, y, 0));
                                }
                            }
                            for (int x = to; x >= from; x--) {
                                //将多边形所在栅格添加到区域栅格集中
                                if (x % stepX == 0) {
                                    rasters.add(new Raster(x, y, 1));
                                }
                            }
                            for (int x = from; x <= to; x++) {

                                //将多边形所在栅格添加到区域栅格集中
                                if (x % stepX == 0) {
                                    rasters.add(new Raster(x, y, 0));
                                }
                            }
                            for (int x = to; x >= from; x--) {
                                //将多边形所在栅格添加到区域栅格集中
                                if (x % stepX == 0) {
                                    rasters.add(new Raster(x, y, 1));
                                }
                            }
                            for (int x = from; x <= to; x++) {

                                //将多边形所在栅格添加到区域栅格集中
                                if (x % stepX == 0) {
                                    rasters.add(new Raster(x, y, 0));
                                }
                            }
                            for (int x = to; x >= from; x--) {
                                //将多边形所在栅格添加到区域栅格集中
                                if (x % stepX == 0) {
                                    rasters.add(new Raster(x, y, 1));
                                }
                            }
                        }

                    }
                }
                //删除非活动边 当前活动边的ymax == y，下一条扫描线 一定不会与该活性边相交
                Iterator<ET> iterator = aet.getAll().iterator();
                while (iterator.hasNext()) {
                    ET et = iterator.next();
                    if (et.ymax == (y - ymin)) {
                        iterator.remove();
                    }
                }
                int updateLength = aet.getAll().size();
                for (int k = 0; k < updateLength; k++) {
                    aet.get(k).xi += (aet.get(k).dx);
                }
            } catch (Exception ex) {
                System.out.println("y = " + (y - ymin) + " Exception:" + ex.getMessage());
            }
        }

        return rasters;
    }

}
