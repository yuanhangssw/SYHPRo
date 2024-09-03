package com.tianji.dam.utils.productareapoints.clearing;

public class Mileage {
    //起点 任务起点A坐标
    private double NS;
    private double ES;

    //终点 任务终点B坐标
    private double NE;
    private double EE;


	public Mileage(double ns, double es, double ne,double ee){
		this.NS = ns;
		this.ES = es;
		this.NE = ne;
		this.EE = ee;
	}


    /**
     * 里程偏距转平面坐标
     * @param mileage:桩号
     * @param offset:里程
     * @return 平面坐标 dst[0]北方向坐标 dst[1]东方向坐标
     */
    public double[] mileage2Coord(double mileage,double offset){
        double x2 = 0;
        double y2 = 1;
        double x1 = EE - ES;
        double y1 = NE - NS;
        //tanO =(a叉乘b) / (a点乘b) = sin0/cos0
        double a = x1 * y2 - x2 * y1;//a点乘b
        double b = x1 * x2 + y1 * y2;//a叉乘b

        double angle = Math.atan2(a, b);

        //x' = (XcosA + YsinA)  y' = (YcosA - XsinA)
        double s = mileage * Math.cos(angle) - offset * Math.sin(angle);
        //偏距沿小里程到大里程方向 左负右正
        double t =  offset * Math.cos(angle) + mileage * Math.sin(angle);
        double[] dst = new double[]{s,t};
        dst[0] += NS;
        dst[1] += ES;
        return dst;
    }
    /**
     * 平面坐标转里程偏距
     * @param n:北方向坐标
     * @param e:东方向坐标
     * @return dst dst[0]桩号 dst[1]偏距
     */
    public double[] coord2Mileage(double n,double e){
        double x = e - ES;
        double y = n - NS;

        double x2 = 0;
        double y2 = 1;
        double x1 = EE - ES;
        double y1 = NE - NS;

        //tanO =(a叉乘b) / (a点乘b) = sin0/cos0
        double a = x1 * y2 - x2 * y1;//a点乘b
        double b = x1 * x2 + y1 * y2;//a叉乘b
        double angle = -1 * Math.atan2(a, b);
        //x' = (XcosA + YsinA)  y' = (YcosA - XsinA)
        double mileage = y * Math.cos(angle) - x * Math.sin(angle);
        //偏距沿小里程到大里程方向 左负右正
        double offset =  x * Math.cos(angle) + y * Math.sin(angle);
        double[] dst = new double[2];
        dst[0] = mileage;
        dst[1] = offset;
        return dst;
    }
}
