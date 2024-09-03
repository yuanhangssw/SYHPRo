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

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据补录-模拟数据生成类
 */
public class AreaRepairDataAutoProduct implements Runnable {

    private static Double x0 = TrackConstant.x0;//北方向
    private static Double y0 = TrackConstant.y0;//东方下

    private static Double alpha = TrackConstant.alpha;//顺时针旋转角度，弧度制d
    private static Double kk = TrackConstant.kk;//缩放比


    private TRepairData repairData;


    public TRepairData getRepairData() {
        return repairData;
    }

    public void setRepairData(TRepairData repairData) {
        this.repairData = repairData;
    }


    public double getdistance(Point2D begin, Point2D end) {
        //获取两个坐标之间的距离
        //
//       GlobalCoordinates source = new GlobalCoordinates(begin.getE(), begin.getN());
//       GlobalCoordinates target = new GlobalCoordinates(end.getE(), end.getN());
//
//       //创建GeodeticCalculator，调用计算方法，传入坐标系、经纬度用于计算距离
//       GeodeticCurve geoCurve = new GeodeticCalculator().calculateGeodeticCurve(Ellipsoid.Sphere, source, target);
//       Double distance = geoCurve.getEllipsoidalDistance();
//
////       //格式化-保留两位小数
////       DecimalFormat df = new DecimalFormat("#.00");
////
////       String distanceStr = df.format(distance/1000);


        return Math.sqrt(Math.pow(begin.getE() - end.getE(), 2) + Math.pow(begin.getN() - end.getN(), 2));
    }

    @Override
    public void run() {

        String ranges = repairData.getRanges();
        Integer carid = repairData.getCarid();
        CarMapper carMapper = BeanContext.getApplicationContext().getBean(CarMapper.class);
        TDamsconstructionMapper construction = BeanContext.getApplicationContext().getBean(TDamsconstructionMapper.class);

        T1Mapper t1Mapper = BeanContext.getApplicationContext().getBean(T1Mapper.class);
        DamsConstruction damsConstruction = construction.selectByPrimaryKey(repairData.getDamsid());
        Car autocar = carMapper.selectByPrimaryKey(carid);

        Double lunkuan = autocar.getLunkuan();


        List<Point2Dpix> allpoint = JSONArray.parseArray(ranges, Point2Dpix.class);
        Mileage mileage = Mileage.getmileage();
        Storehouse storehouse = new Storehouse();
        //录入的是像素坐标 需要手动转换为平面坐标

        Point2D[] maxlengt = new Point2D[2];


        List<Point2Dpix> sortlist = allpoint.stream().sorted(Comparator.comparing(Point2Dpix::getX).reversed().thenComparing(Point2Dpix::getY)).collect(Collectors.toList());


        for (Point2Dpix point2Dpix : allpoint) {
            {
                Point2Dpix point2D = point2Dpix;
                double[] res = mileage.pixels2Coord2(1, point2D.getX(), point2D.getY());
                Point2D temp = new Point2D();
                temp.setE(res[0]);
                temp.setN(res[1]);
                storehouse.add(temp);

                if (sortlist.get(0).getX() == point2D.getX() && sortlist.get(0).getY() == point2D.getY()) {
                    maxlengt[0] = temp;
                } else if (sortlist.get(1).getX() == point2D.getX() && sortlist.get(1).getY() == point2D.getY()) {
                    maxlengt[1] = temp;
                }


            }
        }


        Params params = new Params();

        params.setWidth(lunkuan.floatValue() == 0 ? 2.4f : lunkuan.floatValue());
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
        String tablepre = GlobCache.cartableprfix[repairData.getCartype()];
        listVo.setTableName(tablepre + "_" + damsConstruction.getTablename());

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
                tempdata.setCoordX(pt.getN());
                tempdata.setCoordY(pt.getE());
                tempdata.setIsVibrate(1);
                Double vib = repairData.getVibration();
                Double vib_r = RandomUtiles.randomdouble(vib - 10, vib + 10);

                tempdata.setVibrateValue(vib_r);
                tempdata.setIshistory(1);

                Double gaocheng = damsConstruction.getGaocheng();
                Float gaocheng_r = (float) RandomUtiles.randomdouble(gaocheng - 0.05, gaocheng + 0.05);

                tempdata.setElevation(gaocheng_r);
                tempdata.setVehicleID(carid.toString());
                tempdata.setMaterialname(Integer.parseInt(repairData.getMaterialname()));
                tempdata.setOrderNum(jj);
                tempdata.setSpeed(speed.floatValue());
                tempdata.setIsForward(1);

                tempdata.setTimestamp(startTime);
                tempdata.setTablename(damsConstruction.getTablename());

                tempdata.setLayerID(1);

                //计算里程和偏距
                try {
                    Mileage m = Mileage.getmileage();
                    double[] value = m.coord2Mileage2(tempdata.getCoordY(), tempdata.getCoordX(), "0");


                    tempdata.setZhuanghao(value[0]);
                    tempdata.setPianju(value[1]);

                    Double zhuangY = -1 * (((tempdata.getCoordX() - x0) / kk) * Math.cos(alpha) + ((tempdata.getCoordY() - y0) / kk) * Math.sin(alpha));
                    Double zhuangX = ((tempdata.getCoordX() - x0) / kk) * Math.sin(alpha) + ((tempdata.getCoordY() - y0) / kk) * Math.cos(alpha);
                    tempdata.setZhuangX(zhuangX);
                    tempdata.setZhuangY(zhuangY);
                } catch (Exception e) {
                    System.out.println("里程偏距计算错误。");
                }

                jj++;
                lastPt = pt;
                vodata.add(tempdata);

                if (vodata.size() == 100) {
                    System.out.println("插入100条。。");
                    listVo.setDataList(vodata);
                    t1Mapper.insertrollingdatabatch(listVo);
                    vodata.clear();
                }
            }
        }


        if (vodata.size() > 0) {
            System.out.println("插入" + vodata.size() + "条。。");
            listVo.setDataList(vodata);
            t1Mapper.insertrollingdatabatch(listVo);
            vodata.clear();
        }


    }
}
