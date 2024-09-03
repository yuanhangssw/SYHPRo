package com.tianji.dam.scan;

import static java.util.Arrays.sort;

public class Algorithm {
    private Algorithm(){}

    /**
     * 构造Point数组ps中从fromIndex到toIndex的Point的凸包,时间复杂度O(N*logN)
     * 注意: 该方法可能改变ps数组中从fromIndex到toIndex元素的顺序
     * 前置条件: 一个任意的二维点集,不同点的个数>=2
     *@param ps 保存二维点集的Point数组
     *@param fromIndex 点集在数组中的起始位置
     *@param toIndex   点集在数组中的结束位置
     *@param convex    用来保存凸多边形的顶点的Point数组,其顶点将按逆时针排列
     *@return length   凸多边形的顶点数目
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
     * 构造一个简单多边形的凸包
     * 前置条件: 一列顺序排列表示一个简单多边形的顶点,顶点个数>=3
     */
    public static int getPolygonConvexClosure(PointCpb[] pg,int fromIndex,int toIndex,PointCpb[] convex,int offset){
        int len =toIndex -fromIndex;
        PointCpb[] tmp =new PointCpb[2*len];
        tmp[len] =pg[fromIndex];
        tmp[len+1] =pg[fromIndex+1];
        tmp[len+2] =pg[fromIndex+2];
        tmp[len-1] =tmp[len+2];
        if(multiply(tmp[len],tmp[len+1],tmp[len+2])<0){
            tmp[len+1] =pg[fromIndex];
            tmp[len  ] =pg[fromIndex+1];
        }
        int tail =len-2, head =len+3;
        for(int index=fromIndex+3;index<toIndex;index++){
            tmp[tail] =tmp[head] =pg[index];
            if(multiply(tmp[head-2],tmp[head-1],tmp[head])>=0&&
                    multiply(tmp[tail+2],tmp[tail+1],tmp[tail])<=0) continue;
            while(multiply(tmp[head-2],tmp[head-1],tmp[head])<0){
                tmp[head-1] =tmp[head];
                head --;
            }
            while(multiply(tmp[tail+2],tmp[tail+1],tmp[tail])>0){
                tmp[tail+1] =tmp[tail];
                tail++;
            }
            tail --;
            head ++;
        }
        System.arraycopy(tmp,tail+1,convex,offset,head-tail-2);
        return head -tail -2;
    }
    /**
     * 求凸多边形convex的直径,时间复杂度O(n)
     * 前置条件: convex中保存按逆时针顺序排列的凸多边形顶点,顶点个数>=2,且要求convex前后都至少有一个预留位置
     *@return 该凸多边形的直径
     */
    public static double getDiameter(PointCpb[] convex,int fromIndex,int toIndex){
        convex[toIndex] =convex[fromIndex];
        convex[fromIndex-1] =convex[toIndex-1];
        int i =fromIndex-1, j=fromIndex;
        while(multiply(convex[i],convex[i+1],convex[j],convex[j+1])>0) j++;
        double diam =0.0;
        while(i<toIndex){
            double m =multiply(convex[i],convex[i+1],convex[j],convex[j+1]);
            if(m<=0) i++;
            else{
                j ++;
                if(j==toIndex) j =fromIndex;
            }
            double distSq =PointCpb.distanceSq(convex[i],convex[j]);
            if(distSq>diam) diam =distSq;
        }
        return Math.sqrt(diam);
    }
    /**
     * 计算向量ab与ac的叉积
     */
    private static double multiply(PointCpb a,PointCpb b,PointCpb c){
        return multiply(a,b,a,c);
    }
    /**
     * 计算向量ab与cd的叉积
     */
    private static double multiply(PointCpb a,PointCpb b,PointCpb c,PointCpb d){
        return (b.x-a.x)*(d.y-c.y)-(d.x-c.x)*(b.y-a.y);
    }

    /**
     * 二维向量夹角计算
     * 叉乘为俩向量构成平行四边形面积
     * a点乘b = |a|*|b|*cosO = x1x2 + y1y2
     * a叉乘b = |a|*|b|*sinO = x1y2 - x2y1
     * tanO =(a叉乘b) / (a点乘b) = sin0/cos0
     * atan2 返回的是弧度值 [0,π] [0,-π] ,即大于180的为负数
     * @param vector1：p1->p2
     * @param vector2: 原点->p1
     * @return
     */
    private static double AngleBetween(PointCpb vector1, PointCpb vector2) {
        double sin = vector1.x * vector2.y - vector2.x * vector1.y;
        double cos = vector1.x * vector2.x + vector1.y * vector2.y;
        return Math.atan2(sin, cos) * (180 / Math.PI);
    }
}
