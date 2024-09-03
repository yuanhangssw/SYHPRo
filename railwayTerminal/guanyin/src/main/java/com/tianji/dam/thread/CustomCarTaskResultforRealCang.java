package com.tianji.dam.thread;

import com.tianji.dam.bean.GlobCache;
import com.tianji.dam.domain.MatrixItem;
import com.tianji.dam.domain.MatrixItemRedisReal;
import com.tianji.dam.domain.T1;
import com.tianji.dam.mileageutil.Mileage;
import com.tianji.dam.scan.*;
import com.tianji.dam.utils.PolygonUtils;
import com.tianji.dam.utils.RidUtil;
import com.tianji.dam.utils.TrackConstant;
import com.tj.common.utils.StringUtils;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;


public class CustomCarTaskResultforRealCang implements Callable<String> {

    List<T1> t1s;
    private String cartype;

    List<Integer> long2Cols;
    List<Integer> long2Rows;

    public CustomCarTaskResultforRealCang(String cartype, List<T1> t1s, List<Integer> long2Cols, List<Integer> long2Rows) {
        this.t1s = t1s;

        this.long2Cols = long2Cols;
        this.long2Rows = long2Rows;
        this.cartype = cartype;
    }

    long bigen = 0;

    public String call() throws Exception {
        //System.out.println("call()方法被自动调用,干活！！！             " + Thread.currentThread().getName());
        bigen = System.currentTimeMillis();
        Mileage mileage =Mileage.getmileage();
        if (t1s.size() > 2) {
            try {
                T1 rollingData01 = t1s.get(0);
                T1 rollingData02 = t1s.get(0);
                WheelMarkData mLastPt = null;
                Quadrilateral ql = new Quadrilateral();
                PointCpb[] convex = new PointCpb[4];

                PointCpb[] ps = null;
                PointCpb ps1 = new PointCpb();
                PointCpb ps2 = new PointCpb();
                PointCpb ps3 = new PointCpb();
                PointCpb ps4 = new PointCpb();
                Pixel[] polygon = null;
                Pixel polygon1 = new Pixel();
                Pixel polygon2 = new Pixel();
                Pixel polygon3 = new Pixel();
                Pixel polygon4 = new Pixel();
                int nf = 0;
                for (int i = 0; i < t1s.size(); i++) {
                    rollingData02 = t1s.get(i);
                    double dis = Math.sqrt(Math.pow(rollingData01.getZhuangX() - rollingData02.getZhuangX(), 2) +
                            Math.pow(rollingData01.getZhuangY() - rollingData02.getZhuangY(), 2));
                    if (dis < TrackConstant.MIN_DIS) {
                        if (i + 1 >= t1s.size()) {
                            break;
                        }
                    } else if (dis > TrackConstant.MAX_DIS) {
                        rollingData01 = rollingData02;
                        //将前一个滚轮置空,否则将会把中间连接在一起
                        mLastPt = null;
                    } else {
                      //  WheelMarkData data = WheelMarkData.calPosition(rollingData01, rollingData02, TrackConstant.WHEEL_LEFT, TrackConstant.WHEEL_RIGHT, 0);
                      //  if (data != null) {
                      //      rollingData02.setCoordLX(data.LPt.getX());
                     //       rollingData02.setCoordLY(data.LPt.getY());
                      //      rollingData02.setCoordRX(data.RPt.getX());
                      //      rollingData02.setCoordRY(data.RPt.getY());
//                            if (mLastPt == null) {
//                                mLastPt = data;
//                            } else {
                                if (ps == null) {
                                    ps = new PointCpb[]{ps1, ps2, ps3, ps4};
                                }

                                double[] rd1=mileage.coord2pixel(rollingData02.getCoordRX(),rollingData02.getCoordRY());
                                double[] ld1= mileage.coord2pixel(rollingData02.getCoordLX(),rollingData02.getCoordLY());
                                double[] rd2= mileage.coord2pixel(rollingData02.getBeforeCoordLX(),rollingData02.getBeforeCoordLY());
                                double[] ld2= mileage.coord2pixel(rollingData02.getBeforeCoordRX(),rollingData02.getBeforeCoordRY());

                                ps1.x =rd1[0];
                                ps1.y =rd1[1];
                                ps2.x = ld1[0];
                                ps2.y =ld1[1];
                                ps3.x = rd2[0];
                                ps3.y = rd2[1];
                                ps4.x = ld2[0];
                                ps4.y =  ld2[1];
//
//                                ps1.x = mLastPt.LPt.getX();
//                                ps1.y = mLastPt.LPt.getY();
//                                ps2.x = mLastPt.RPt.getX();
//                                ps2.y = mLastPt.RPt.getY();
//                                ps3.x = data.LPt.getX();
//                                ps3.y = data.LPt.getY();
//                                ps4.x = data.RPt.getX();
//                                ps4.y = data.RPt.getY();
                                int count = PolygonUtils.getPointsConvexClosure(ps, 0, 4, convex);
                                if (count == 4) {
                                    ql.pt1.setX(convex[0].x);
                                    ql.pt1.setY(convex[0].y);
                                    ql.pt2.setX(convex[1].x);
                                    ql.pt2.setY(convex[1].y);
                                    ql.pt3.setX(convex[2].x);
                                    ql.pt3.setY(convex[2].y);
                                    ql.pt4.setX(convex[3].x);
                                    ql.pt4.setY(convex[3].y);
                                    if (polygon == null) {
                                        polygon = new Pixel[]{polygon1, polygon2, polygon3, polygon4};
                                    }
                                    polygon1.setX((int) (ql.pt1.getX()));
                                    polygon1.setY((int) (ql.pt1.getY()));
                                    polygon2.setX((int) (ql.pt2.getX()));
                                    polygon2.setY((int) (ql.pt2.getY()));
                                    polygon3.setX((int) (ql.pt3.getX()));
                                    polygon3.setY((int) (ql.pt3.getY()));
                                    polygon4.setX((int) (ql.pt4.getX()));
                                    polygon4.setY((int) (ql.pt4.getY()));
                                    List<Pixel> rasters = Scan.scanRaster(polygon);

                                ConcurrentHashMap<Long, MatrixItemRedisReal[][]>  allriddata =      GlobCache.t1T0redishistorycarCachDataCang.get(cartype);
                                if(null== allriddata) {
                                    GlobCache.t1T0redishistorycarCachDataCang.put(cartype, new ConcurrentHashMap<>());
                                      allriddata =new ConcurrentHashMap<>();
                                }
                                    for (Pixel pixel : rasters) {
                                        try {
                                            long rid = RidUtil.double2Long(pixel.getX(), pixel.getY());
                                            int bottom = RidUtil.long2Bottom(rid);
                                            int left = RidUtil.long2Left(rid);
                                            int      n = (pixel.getY() - bottom);
                                            int    m = (pixel.getX() - left);

                                            if (null == GlobCache.t1T0redishistorycarCachDataCang.get(cartype)) {
                                                GlobCache.t1T0redishistorycarCachDataCang.put(cartype, new ConcurrentHashMap<>());
                                            }
                                            MatrixItemRedisReal[][] matrixItems = allriddata.get(rid);
                                            if (!StringUtils.isNotEmpty(matrixItems)) {
                                                //TODO:10*10 PIXEL为一个矩形块
                                                //根据Rid  获取 long2Col 列  long2Row 行
                                                long2Cols.add(RidUtil.long2Col(rid));
                                                long2Rows.add(RidUtil.long2Row(rid));
                                                GlobCache.t1T0redishistorycarCachDataCang.get(cartype).put(rid, new MatrixItemRedisReal[RidUtil.R_LEN][RidUtil.R_LEN]);
                                            }
                                            if (n >= 0 && m >= 0 && n < RidUtil.R_LEN && m < RidUtil.R_LEN) {

                                                MatrixItemRedisReal item = matrixItems[m][n];
                                                if (item == null) {
                                                    item = new MatrixItemRedisReal();
                                                }
                                                if(cartype.equals("tpj")){
                                                    item.getCurrentEvolution().add(rollingData02.getCurrentEvolution());
                                                }else{
                                                    item.setRollingTimes(item.getRollingTimes() + 1);

                                                }

                                                matrixItems[m][n] = item;
                                                allriddata.put(rid, matrixItems);
                                            }

                                        } catch (Exception ex) {
                                        }
                                    }
                                    GlobCache.t1T0redishistorycarCachDataCang.put(cartype,allriddata);
                                    rasters.clear();
                                    rollingData01 = rollingData02;
                                 //   mLastPt = data;
                                }
                         //   }
                    //    }
                    }
                }
                System.out.println("线程执行时间："+(System.currentTimeMillis()-bigen));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return "call()方法被自动调用 " + Thread.currentThread().getName();
    }
}
