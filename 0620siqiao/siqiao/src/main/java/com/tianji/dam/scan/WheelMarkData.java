package com.tianji.dam.scan;

import com.tianji.dam.domain.RollingData;
import com.tianji.dam.domain.T1;

/**
 * 碾压轮实体
 */
public class WheelMarkData {
    public Point2D LPt;
    public Point2D RPt;
    public Point2D CPt;

    @Override
    public String toString() {
        return "L:" + LPt.toString() +
                "  C:" + CPt.toString() +
                "   R:" + RPt.toString();
    }

    public static WheelMarkData calPosition(RollingData lastp, RollingData currentp,
                                            double mLeftLen, double mRightLen, double mDistance) {
        try {
            WheelMarkData wheelMarkData;
            Point2D tmPoint2d;
            Point2D startpt = new Point2D(lastp.getZhuangX(), lastp.getZhuangY());
            Point2D endpt = new Point2D(currentp.getZhuangX(), currentp.getZhuangY());

            double _x = endpt.getX() - startpt.getX();
            double _y = endpt.getY() - startpt.getY();
            double len = Math.sqrt(_x * _x + _y * _y);
            if (len == 0) {
                return null;
            }
            double x = mDistance * (endpt.getX() - startpt.getX()) / len + endpt.getX();
            double y = mDistance * (endpt.getY() - startpt.getY()) / len + endpt.getY();
            tmPoint2d = new Point2D(x, y);
            //滚轮左右位置点
            Point2D[] lrPointPair = null;
            double distance = Math.sqrt(Math.pow(tmPoint2d.getX() - startpt.getX(), 2)
                    + Math.pow(tmPoint2d.getY() - startpt.getY(), 2)); // 距离公式
            if (distance != 0) {
                lrPointPair = new Point2D[2];
                double deltaX = mLeftLen * (tmPoint2d.getY() - startpt.getY()) / distance;
                double deltaY = mLeftLen * (tmPoint2d.getX() - startpt.getX()) / distance;
                Point2D LPoint = new Point2D(tmPoint2d.getX() - deltaX, tmPoint2d.getY() + deltaY);
                deltaX = mRightLen * (tmPoint2d.getY() - startpt.getY()) / distance;
                deltaY = mRightLen * (tmPoint2d.getX() - startpt.getX()) / distance;
                Point2D RPoint = new Point2D(tmPoint2d.getX() + deltaX, tmPoint2d.getY() - deltaY);
                lrPointPair[0] = LPoint;
                lrPointPair[1] = RPoint;
                wheelMarkData = new WheelMarkData();
                wheelMarkData.CPt = tmPoint2d;
                wheelMarkData.LPt = lrPointPair[0];
                wheelMarkData.RPt = lrPointPair[1];
                return wheelMarkData;
            }
        } catch (Exception ex) {
            System.out.println("calPosition Exception:" + ex.getMessage());
        }
        return null;
    }

    public static WheelMarkData calPosition(T1 lastp, T1 currentp,
                                            double mLeftLen, double mRightLen, double mDistance) {
        try {
            WheelMarkData wheelMarkData;
            Point2D tmPoint2d;
            Point2D startpt = new Point2D(lastp.getZhuangX(), lastp.getZhuangY());
            Point2D endpt = new Point2D(currentp.getZhuangX(), currentp.getZhuangY());

            double _x = endpt.getX() - startpt.getX();
            double _y = endpt.getY() - startpt.getY();
            double len = Math.sqrt(_x * _x + _y * _y);
            if (len == 0) {
                return null;
            }
            double x = mDistance * (endpt.getX() - startpt.getX()) / len + endpt.getX();
            double y = mDistance * (endpt.getY() - startpt.getY()) / len + endpt.getY();
            tmPoint2d = new Point2D(x, y);
            //滚轮左右位置点
            Point2D[] lrPointPair = null;
            double distance = Math.sqrt(Math.pow(tmPoint2d.getX() - startpt.getX(), 2)
                    + Math.pow(tmPoint2d.getY() - startpt.getY(), 2)); // 距离公式
            if (distance != 0) {
                lrPointPair = new Point2D[2];
                double deltaX = mLeftLen * (tmPoint2d.getY() - startpt.getY()) / distance;
                double deltaY = mLeftLen * (tmPoint2d.getX() - startpt.getX()) / distance;
                Point2D LPoint = new Point2D(tmPoint2d.getX() - deltaX, tmPoint2d.getY() + deltaY);
                deltaX = mRightLen * (tmPoint2d.getY() - startpt.getY()) / distance;
                deltaY = mRightLen * (tmPoint2d.getX() - startpt.getX()) / distance;
                Point2D RPoint = new Point2D(tmPoint2d.getX() + deltaX, tmPoint2d.getY() - deltaY);
                lrPointPair[0] = LPoint;
                lrPointPair[1] = RPoint;
                wheelMarkData = new WheelMarkData();
                wheelMarkData.CPt = tmPoint2d;
                wheelMarkData.LPt = lrPointPair[0];
                wheelMarkData.RPt = lrPointPair[1];
                return wheelMarkData;
            }
        } catch (Exception ex) {
            System.out.println("calPosition Exception:" + ex.getMessage());
        }
        return null;
    }
}
