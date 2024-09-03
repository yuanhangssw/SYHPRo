package com.tianji.dam.utils;

import org.springframework.beans.factory.annotation.Autowired;

import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.domain.TAnalysisConfig;
import com.tianji.dam.mapper.TAnalysisConfigMapper;

/**
 * 坐标法
 * 桩号偏距与平面坐标互换
 */
public class Mileage {
    //起点 桩号0+000的坐标
    private static final double NS = 3711082.5840d;
    private static final double ES = 560949.2200d;
    //终点 桩号x+xxx的坐标
    private static final double NE = 3711179.7760d;
    private static final double EE = 561235.5150d;
    //起点桩号
    private static final double DKS =-302.343;
    //终点桩号
    private static final double DKE =0;
    
    //底图左上点x坐标
    public static double E0 = 560865.81;
    //底图左上角点y坐标
    public static double N0 =3711348.99d;
    //逆时针为正 角度[0,360]
    public static double ALPHA = 0d;
    //缩放比
    public static double SCALE = 0.1002;
    
    
    
    
    @Autowired
    private BeanContext  beancontext;
    

//    public static Mileage getInstance(){
//        return LazyHolder.INSTANCE;
//    }

//    static class LazyHolder{
//        public static Mileage INSTANCE = new Mileage();
//    }

    public Mileage(){
        initParams();
    }

    /**
     * 初始化坐标转换四参数
     */
    public void initParams(){
        TAnalysisConfigMapper tAnalysisConfigMapper = beancontext.getApplicationContext().getBean(TAnalysisConfigMapper.class);
        TAnalysisConfig tAnalysisConfig = tAnalysisConfigMapper.selectMaxIdOne();
        if (tAnalysisConfig != null) {
            E0 = tAnalysisConfig.getFourParamY();
            N0 = tAnalysisConfig.getFourParamX();
            ALPHA = tAnalysisConfig.getFourParamAngle();
            SCALE = tAnalysisConfig.getFourParamFactor();
            
        }
    }


    /**
     * 平面坐标转图标像素坐标
     * @param CoordX: 平面坐标东
     * @param CoordY: 平面坐标北
     * @return 桩号
     */

    public double[] coord2pixel(double CoordX,double CoordY) {
        double zhuangY = -1 * (((CoordX - E0) / SCALE) * Math.cos(ALPHA) + ((CoordY - N0) / SCALE) * Math.sin(ALPHA));
        double zhuangX = ((CoordX - E0) / SCALE) * Math.sin(ALPHA) + ((CoordY - N0) / SCALE) * Math.cos(ALPHA);
        return new double[]{zhuangX, zhuangY};
    }


    /**
     * 里程偏距转平面坐标
     * @param mileage:桩号
     * @param offset:里程
     * @return 平面坐标 dst[0]东方向坐标 dst[1]北方向坐标
     */
    public double[] mileage2Coord(double mileage,double offset){
    	
    	mileage =mileage-DKS;
        double x1 = EE - ES;
        double y1 = NE - NS;
        double x2 = 1;
        double y2 = 0;
        //tanO =(a叉乘b) / (a点乘b) = sin0/cos0
        double a = x1 * y2 - x2 * y1;//a点乘b
        double b = x1 * x2 + y1 * y2;//a叉乘b
        double angle = Math.atan2(a, b);
        if(a > 0){
            angle = 2*Math.PI - angle;
        }  
        //s=(XcosA - YsinA)  t = (YcosA + XsinA)
        double s = mileage * Math.cos(angle) - offset * Math.sin(angle);
        //偏距沿小里程到大里程方向 左负右正
        double t = -1 * (offset * Math.cos(angle) + mileage * Math.sin(angle));
        double[] dst = new double[]{s,t};
        dst[0] += ES;
        dst[1] += NS;
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

        double x1 = 1;
        double y1 = 0;
        double x2 = EE - ES;
        double y2 = NE - NS;

        //tanO =(a叉乘b) / (a点乘b) = sin0/cos0
        double a = x1 * y2 - x2 * y1;//a点乘b
        double b = x1 * x2 + y1 * y2;//a叉乘b
        double angle = Math.atan2(a, b);
        if(a > 0){
            angle = 2*Math.PI - angle;
        }
        //(XcosA - YsinA)  t = (YcosA + XsinA)
        
        double mileage = DKS+x * Math.cos(angle) - y * Math.sin(angle);
        //偏距沿小里程到大里程方向 左负右正
        double offset = -1 * (y * Math.cos(angle) + x * Math.sin(angle));
        double[] dst = new double[]{mileage,offset};
        return dst;
    }

    /**
     * 桩号偏距转像素坐标
     * @param type:底图坐标原点位置 0:左下 1:左上 2:右上 3:右下
     * @param mileage:桩号
     * @param offset:偏距
     * @return pixels:像素坐标 像素坐标x pixels[0] 像素坐标y pixels[1]
     */
    public double[] mileage2Pixel(int type,double mileage,double offset){
        double[] pixels = new double[]{-1,-1};
        //1.桩号偏距转平面坐标
        double[] coord = mileage2Coord(mileage,offset);
        double north = coord[1];
        double east = coord[0];
        double x = east - E0;
        double y = north - N0;
        double radians = Math.toRadians(ALPHA);
        //2.根据平面坐标与底图的四参数求得像素坐标
        //s = (XcosA - YsinA)  t = (YcosA + XsinA)
        switch (type){
            case 0:{
                break;
            }
            case 1:{
                //前端绘图 使用底图的Height像素 - pixels[1]来表示该点在底图上的实际像素位置
                pixels[0] = ((x/ SCALE)) * Math.cos(radians) - (y / SCALE) * Math.sin(radians);
                pixels[1] = -1 * ((y / SCALE) * Math.cos(radians) + (x / SCALE) * Math.sin(radians));
                break;
            }
            case 2:{
                break;
            }
            case 3:{
                break;
            }
            default:{
                break;
            }
        }
        return pixels;
    }

    /**
     * 像素坐标转平面坐标
     * @param type:底图坐标原点
     * @param pixelx:像素坐标x
     * @param pixely:像素坐标y
     * @return 平面坐标 coord[0]东方向坐标 coord[1] 北方向坐标
     */
    public double[] pixels2Coord(int type,double pixelx,double pixely){
        double[] coord = new double[]{E0,N0};
        pixely *= -1;
        switch (type){
            case 0:{
                break;
            }
            case 1:{
                //s=(XcosA - YsinA)  t = (YcosA + XsinA)
                //源 像素坐标 目标 平面坐标
                double angle = Math.toRadians(360 - ALPHA);
                double x = (pixelx * SCALE) * Math.cos(angle) - (pixely * SCALE) * Math.sin(angle);
                double y = (pixely * SCALE) * Math.cos(angle) + (pixelx * SCALE) * Math.sin(angle);
                coord[0] += x;
                coord[1] += y;
                break;
            }
            case 2:{
                break;
            }
            case 3:{
                break;
            }
            default:{
                break;
            }
        }
        return coord;
    }

    /**
     * 像素坐标转桩号偏距
     * @param pixelx:像素坐标x
     * @param pixely:像素坐标y
     * @return mileages[] mileages[0]桩号 mileages[1]偏距
     */
    public double[] pixels2Mileage(double pixelx,double pixely){
        double[] coord = pixels2Coord(1,pixelx,pixely);
        double[] mileages = coord2Mileage(coord[1],coord[0]);
        return mileages;
    }



//    @Test
    public void test(){
        double mileage = 187.84;
        double offset = 63.84;
        System.out.println("桩号:" + mileage + ",偏距:" + offset);
        double[] coord1 = mileage2Coord(mileage,offset);
        //5103703.5202 400706.9844
        System.out.println("坐标N:" + coord1[1] + ",坐标E:" + coord1[0]);

        double[] mileages2 = coord2Mileage(coord1[1],coord1[0]);
        System.out.println("桩号:" + mileages2[0] + ",偏距:" + mileages2[1]);

        double[] pixels3 = mileage2Pixel(1,mileages2[0],mileages2[1]);
        System.out.println("像素X:" + pixels3[0] + ",像素Y:" + pixels3[1]);

        double[] mileages3 = pixels2Mileage(pixels3[0],pixels3[1]);
        System.out.println("桩号:" + mileages3[0] + ",偏距:" + mileages3[1]);
    }
}
