package com.tianji.dam.utils.productareapoints.scan;

/**
 * 边表
 * ET的意义在于为扫描线提供待加入的新边信息。
 *
 * 对于左顶点和右顶点的情况，如果不做特殊处理会导致奇偶奇数错误，
 * 常采用的修正方法是修改以顶点为终点的那条边的区间，将顶点排除在区间之外，
 * 也就是删除这条边的终点，这样在计算交点时，就可以少计算一个交点，
 * 平衡和交点奇偶个数。结合前文定义的“边”数据结构：EDGE，
 * 只要将该边的ymax修改为ymax – 1就可以了。
 * 等扫描线到达ymax的时候，因为改变的yamx为ymax -1，所以会在AET中去掉这条边
 */

public class ET implements Comparable<ET>{
    //边的下端点x坐标，在活化链表（AET）中，表示扫描线与边的交点x坐标
    public double xi;
    //相邻的扫描线与同一直线段的交点存在步进关系，这个关系与直线段所在直线的斜率有关
    public double dx;//是个常量（直线斜率的倒数）（x + dx, y + 1）
    //一条边不会一直待在“活动边表”中，当扫描线与之没有交点时，要将其从“活动边表”中删除，判断是否有交点的依据就是看扫描线y是否大于这条边两个端点的y坐标值，为此，需要记录边的y坐标的最大值
    public int ymax;
    //重载 < 号 判断 a < b
    public static boolean less(ET a, ET b){
        return (Math.abs(a.xi - b.xi) < 1 ? a.dx < b.dx : a.xi < b.xi);
    }
    //重载 > 号 判断 a > b
    public static boolean greater(ET a, ET b){
        return (Math.abs(a.xi - b.xi) < 1 ? a.dx > b.dx : a.xi > b.xi);
    }
    //重载 == 号 判断 a == b
    public static boolean equal(ET a, ET b){
        return (Math.abs(a.xi - b.xi) < 1  && a.dx == b.dx && a.ymax == b.ymax);
    }
    //重载 != 号 判断 a != b
    public static boolean notEqual(ET a, ET b){
        return (Math.abs(a.xi - b.xi)>1 || a.dx != b.dx || a.ymax != b.ymax);
    }

    /**
     * 确保AET为有序表 根据ET的xi从小到大进行升序排序
     * 正整数	当前对象的值 > 比较对象的值 ， 位置排在后
     * 零	    当前对象的值 = 比较对象的值 ， 位置不变
     * 负整数	当前对象的值 < 比较对象的值 ， 位置排在前
     * @param o
     * @return
     */
    @Override
    public int compareTo(ET o) {
        //根据 xi 升序
        if(this.xi - o.xi < 0){
            return -1;
        }else if(this.xi - o.xi > 0){
            return 1;
        }else{
            return 0;
        }
    }
}
