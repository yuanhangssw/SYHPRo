package com.tianji.dam.scan;

/**
 * 形成多边形矩形
 */
public class PointCpb  implements Comparable<PointCpb> {
    public double x,y;
    public static double distanceSq(PointCpb p1,PointCpb p2){
        return (p2.x-p1.x)*(p2.x-p1.x)+(p2.y-p1.y)*(p2.y-p1.y);
    }
    public PointCpb(){
        this(0.0,0.0);
    }
    public PointCpb(double x,double y){
        this.x =x;
        this.y =y;
    }
    /**
     * 实现Comparable的compareTo方法,将Point按从左到右,从下到上排序
     */
    public int compareTo(PointCpb p){
        if(x == p.x) {
            return y == p.y ? 0 : (y > p.y ? 1 : -1);
        }else{
            return x > p.x ? 1 : -1;
        }
    }
    /**
     * 重载equals方法,只是为了完备起见
     */
    public boolean equals(Object o){
        if(o instanceof PointCpb){
            PointCpb p =(PointCpb)o;
            return x ==p.x&&y ==p.y;
        }
        return false;
    }
    /**
     * 重载hashCode方法,只是为了完备起见
     */
    public int hashCode(){
        return (int)(x+23*y);
    }
}