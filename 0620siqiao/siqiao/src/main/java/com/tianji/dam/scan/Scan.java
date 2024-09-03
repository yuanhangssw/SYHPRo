package com.tianji.dam.scan;

import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 扫描线算法核心
 */
@Slf4j
public class Scan {
    //扫描栅格
    public static List<Pixel> scanRaster(Pixel[] polygon) {
        //初始化最大最小 界定扫描线的范围
        int ymax = polygon[0].getY();
        int ymin = polygon[0].getY();
        for (Pixel raster : polygon) {
            int y = raster.getY();
            if (y > ymax) {
                ymax = y;
            }
            if (y < ymin) {
                ymin = y;
            }
        }
        //多边形栅格集
        List<Pixel> rasters = new LinkedList<>();
        NET net = new NET(ymax - ymin + 1);
        //初始化新边表 Net中的第一个元素对应的是ymin所在的扫描线
        int edges = polygon.length;
        for (int i = 0; i < edges; i++) {
            Pixel ps = polygon[i];
            Pixel pe = polygon[(i + 1) % edges];
            Pixel pss = polygon[((i - 1) + edges) % edges];
            Pixel pee = polygon[(i + 2) % edges];
            //不处理水平线
            if (pe.getY() != ps.getY()) {
                ET e = new ET();
                e.dx = ((pe.getX() - ps.getX()) * 1.0f) / (pe.getY() - ps.getY());//扫描线步长 0.1m扫描一次
                if (pe.getY() > ps.getY()) {
                    e.xi = ps.getX();
                    if (pee.getY() >= pe.getY()) {
                        e.ymax = (pe.getY() - ymin) - 1;
                    } else {
                        e.ymax = (pe.getY() - ymin);
                    }
                    net.get((ps.getY() - ymin)).add(e);
                } else {
                    e.xi = pe.getX();
                    if (pss.getY() >= ps.getY()) {
                        e.ymax = (ps.getY() - ymin) - 1;
                    } else {
                        e.ymax = (ps.getY() - ymin);
                    }
                    net.get((pe.getY() - ymin)).add(e);
                }
            }
        }

        //扫描线填充
        AET aet = new AET();
        int end = (ymax - ymin);
        for (int y = 0; y <= end; y++) {
            try {
                //将扫描线对应的所有新边插入到aet中
                aet.add(net.get(y));
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
                        for (int x = from; x < to; x++) {
                            //将多边形所在栅格添加到区域栅格集中
                            rasters.add(new Pixel(x, y + ymin));
                        }
                    }
                }
                //删除非活动边 当前活动边的ymax == y，下一条扫描线 一定不会与该活性边相交
                Iterator<ET> iterator = aet.getAll().iterator();
                while (iterator.hasNext()) {
                    ET et = iterator.next();
                    if (et.ymax == y) {
                        iterator.remove();
                    }
                }
                int updateLength = aet.getAll().size();
                for (int k = 0; k < updateLength; k++) {
                    aet.get(k).xi += (aet.get(k).dx);
                }
            } catch (Exception ex) {
                log.error("y = " + y + " Exception:" + ex.getMessage());
            }
        }
        net = null;
        aet = null;
        return rasters;
    }
}
