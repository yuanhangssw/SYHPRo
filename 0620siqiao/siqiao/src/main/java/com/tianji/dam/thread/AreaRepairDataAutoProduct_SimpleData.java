package com.tianji.dam.thread;

import com.alibaba.fastjson.JSONArray;
import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.bean.GlobCache;
import com.tianji.dam.domain.Car;
import com.tianji.dam.domain.DamsConstruction;
import com.tianji.dam.domain.RollingData;
import com.tianji.dam.domain.TRepairData;
import com.tianji.dam.domain.vo.RollingDataListVo;
import com.tianji.dam.mapper.CarMapper;
import com.tianji.dam.mapper.T1Mapper;
import com.tianji.dam.mapper.TDamsconstructionMapper;
import com.tianji.dam.mileageutil.Mileage;
import com.tianji.dam.utils.RandomUtiles;
import com.tianji.dam.utils.TrackConstant;
import com.tianji.dam.utils.productareapoints.clearing.*;
import com.vividsolutions.jts.geom.Coordinate;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据补录-模拟数据生成类
 */
public class AreaRepairDataAutoProduct_SimpleData implements Runnable {


    private TRepairData repairData;
    private List<Coordinate> allcor;

    public List<Coordinate> getAllcor() {
        return allcor;
    }

    public void setAllcor(List<Coordinate> allcor) {
        this.allcor = allcor;
    }

    public TRepairData getRepairData() {
        return repairData;
    }

    public void setRepairData(TRepairData repairData) {
        this.repairData = repairData;
    }


    public double getdistance(Point2D begin, Point2D end) {
        //获取两个坐标之间的距离
        //

        return Math.sqrt(Math.pow(begin.getE() - end.getE(), 2) + Math.pow(begin.getN() - end.getN(), 2));
    }

    @Override
    public void run() {

        String ranges = repairData.getRanges();
        Integer carid = 1;

        List<Point2Dpix> allpoint = JSONArray.parseArray(ranges, Point2Dpix.class);
        Mileage mileage = Mileage.getmileage();
        Storehouse storehouse = new Storehouse();
        //录入的是像素坐标 需要手动转换为平面坐标

        Point2D[] maxlengt = new Point2D[2];


        List<Point2Dpix> sortlist = allpoint.stream().sorted(Comparator.comparing(Point2Dpix::getX).reversed().thenComparing(Point2Dpix::getY)).collect(Collectors.toList());


        for (Point2Dpix point2Dpix : allpoint) {
            {
                Point2Dpix point2D = point2Dpix;
               // double[] res = mileage.pixels2Coord2(1, point2D.getX(), point2D.getY());
                Point2D temp = new Point2D();
                temp.setE(point2D.getX());
                temp.setN(point2D.getY());
                storehouse.add(temp);

                if (sortlist.get(0).getX() == point2D.getX() && sortlist.get(0).getY() == point2D.getY()) {
                    maxlengt[0] = temp;
                } else if (sortlist.get(1).getX() == point2D.getX() && sortlist.get(1).getY() == point2D.getY()) {
                    maxlengt[1] = temp;
                }


            }
        }

        Params params = new Params();

        params.setWidth(2.2f);
        params.setLap(0.3f);
        params.setStartTime(Long.parseLong(repairData.getStartTime()));
        params.setMinSpeed(repairData.getSpeed().floatValue() - 0.5f);
        params.setMaxSpeed(repairData.getSpeed().floatValue() + 0.5f);
        params.setTarget(repairData.getColorId());

        //3.指定仓位起始边AB 找一个最长边作为AB
        Point2D a = maxlengt[0];
        Point2D b = maxlengt[1];

        com.tianji.dam.utils.productareapoints.clearing.Mileage mileage2 = new com.tianji.dam.utils.productareapoints.clearing.Mileage(a.getN(), a.getE(), b.getN(), b.getE());

        //4.补录数据
        Clearing clearing = new Clearing();
        clearing.setParams(params);
        clearing.setStorehouse(storehouse);
        clearing.setMileage(mileage2);
        List<Point2DWrapper> pts = clearing.clearing(0.5f);

        Point2D lastPt = null;
        long startTime = params.getStartTime();
        int jj = 0;

        RollingDataListVo listVo = new RollingDataListVo();
        List<RollingData> vodata = new LinkedList<>();
        String tablepre ="ylj_t_1";
        listVo.setTableName(tablepre);

        for (Point2DWrapper ptw : pts) {
            Point2D pt = ptw.getPoint2D();
            if (lastPt == null) {
                lastPt = pt;
            } else {
                Double speed = (Math.random() * (params.getMaxSpeed() - params.getMinSpeed()) + params.getMinSpeed());
                double dltaN = pt.getN() - lastPt.getN();
                double dltaE = pt.getE() - lastPt.getE();
                float angle = (float) Math.toDegrees(Math.atan2(dltaE, dltaN));
                double dis = Math.sqrt(Math.pow(dltaN, 2) + Math.pow(dltaE, 2));
                long time = (long) (dis / speed * 1000);
                // 包装 t1 数据
                startTime += time;
                RollingData tempdata = new RollingData();
                //计算车辆航向角

                if (ptw.getDirect() == 1) {
                    angle = angle - 180;
                }
                tempdata.setAngle(angle);
                //生成的平面坐标。
                tempdata.setCoordX(pt.getN());
                tempdata.setCoordY(pt.getE());

                tempdata.setVehicleID(carid.toString());
                tempdata.setOrderNum(jj);
                tempdata.setSpeed(speed.floatValue());


                //根据类型和需要设置需要的字段
                tempdata.setIsForward(1);
                tempdata.setTimestamp(startTime);
                tempdata.setTablename(tablepre);
                tempdata.setLayerID(1);


                jj++;
                lastPt = pt;
                vodata.add(tempdata);

                if (vodata.size() == 100) {
                    System.out.println("插入100条。。");
                    listVo.setDataList(vodata);
                 //   t1Mapper.insertrollingdatabatch(listVo);
                    vodata.clear();
                }
            }
        }


        if (vodata.size() > 0) {
            System.out.println("插入" + vodata.size() + "条。。");
            listVo.setDataList(vodata);
         //   t1Mapper.insertrollingdatabatch(listVo);
            vodata.clear();
        }


    }
}
