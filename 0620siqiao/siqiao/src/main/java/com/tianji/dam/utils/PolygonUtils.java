package com.tianji.dam.utils;


import static java.util.Arrays.sort;

import com.tianji.dam.scan.PointCpb;

/**
 * 用于计算多边形边界点是否符合要求
 */
public class PolygonUtils {

    /**
     * 判断多边形边界点
     * @param ps
     * @param fromIndex
     * @param toIndex
     * @param convex
     * @return
     */
    public static int getPointsConvexClosure(PointCpb[] ps, int fromIndex, int toIndex, PointCpb[] convex){
        sort(ps,fromIndex,toIndex);
        int len =toIndex -fromIndex;
        PointCpb[] tmp =new PointCpb[2*len];
        int up =len, down =len;
        for(int index =fromIndex;index<toIndex;index++){
            tmp[up] =tmp[down] =ps[index];
            while(len-up>=2&&multiply(tmp[up+2],tmp[up+1],tmp[up])>=0){
                tmp[up+1] =tmp[up];
                up++;
            }
            while(down-len>=2&&multiply(tmp[down-2],tmp[down-1],tmp[down])<=0){
                tmp[down-1] =tmp[down];
                down--;
            }
            up --;
            down ++;
        }
        System.arraycopy(tmp,up+1,convex,0,down-up-2);
        return down-up-2;
    }
    /**
     * 计算向量ab与ac的叉积
     */
    public static double multiply(PointCpb a,PointCpb b,PointCpb c){
        return multiply(a,b,a,c);
    }
    /**
     * 计算向量ab与cd的叉积
     */
    public static double multiply(PointCpb a,PointCpb b,PointCpb c,PointCpb d){
        return (b.x-a.x)*(d.y-c.y)-(d.x-c.x)*(b.y-a.y);
    }
}
