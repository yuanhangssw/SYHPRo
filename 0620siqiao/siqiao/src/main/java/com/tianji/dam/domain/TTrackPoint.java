package com.tianji.dam.domain;

import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Slf4j
public class TTrackPoint implements Serializable {
    private Double CoordX;//平面坐标X
    private Double CoordY;//平面坐标Y
    private Double Latitude;//纬度
    private Double Longitude;//经度
    private Float Elevation;//高程
    private Long Timestamp;//上传时间
    private String VehicleID;//车辆ID:
    private Float Speed;//速度
    private Integer LayerID;//碾压层级
    private int IsForward;//前后向
    private int IsVibrate;//动静压
    private Double VibrateValue;//震动检测值
    private Double Frequency;//频率检测值
    private Double Acceleration;//加速度检测值
    private Double Amplitude;//振幅
    private int Satellites;//卫星数量
    private int Qualitylnd;//卫星质量
    private Double ZhuangX;//桩号X
    private Double ZhuangY;//桩号Y
    private int ishistory;//是否为历史 0:实时 1:历史
    private String rangeStr;
    private int materialname;
    private int OrderNum;//顺序号
    private float Angle;//角度
    private double CoordLX;//轮左X
    private double CoordLY;//轮左Y
    private double CoordRX;//轮右X
    private double CoordRY;//轮右Y
    private Double ZhuangLX;//桩号LX
    private Double ZhuangLY;//桩号LY
    private Double ZhuangRX;//桩号RX
    private Double ZhuangRY;//桩号RY
    public TTrackPoint() {
    }

    public Double getZhuangX() {
        return ZhuangX;
    }

    public void setZhuangX(Double zhuangX) {
        ZhuangX = zhuangX;
    }

    public Double getZhuangY() {
        return ZhuangY;
    }

    public void setZhuangY(Double zhuangY) {
        ZhuangY = zhuangY;
    }

    public Double getCoordX() {
        return CoordX;
    }

    public void setCoordX(Double coordX) {
        CoordX = coordX;
    }

    public Double getCoordY() {
        return CoordY;
    }

    public void setCoordY(Double coordY) {
        CoordY = coordY;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public Float getElevation() {
        return Elevation;
    }

    public void setElevation(Float elevation) {
        Elevation = elevation;
    }

    public Long getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(Long timestamp) {
        Timestamp = timestamp;
    }

    public String getVehicleID() {
        return VehicleID;
    }

    public void setVehicleID(String vehicleID) {
        VehicleID = vehicleID;
    }

    public Float getSpeed() {
        return Speed;
    }

    public void setSpeed(Float speed) {
        Speed = speed;
    }

    public Integer getLayerID() {
        return LayerID;
    }

    public void setLayerID(Integer layerID) {
        LayerID = layerID;
    }

    public int getIsForward() {
        return IsForward;
    }

    public void setIsForward(int isForward) {
        IsForward = isForward;
    }

    public int getIsVibrate() {
        return IsVibrate;
    }

    public void setIsVibrate(int isVibrate) {
        IsVibrate = isVibrate;
    }

    public Double getVibrateValue() {
        return VibrateValue;
    }

    public void setVibrateValue(Double vibrateValue) {
        VibrateValue = vibrateValue;
    }

    public Double getFrequency() {
        return Frequency;
    }

    public void setFrequency(Double frequency) {
        Frequency = frequency;
    }

    public Double getAcceleration() {
        return Acceleration;
    }

    public void setAcceleration(Double acceleration) {
        Acceleration = acceleration;
    }

    public Double getAmplitude() {
        return Amplitude;
    }

    public void setAmplitude(Double amplitude) {
        Amplitude = amplitude;
    }

    public int getSatellites() {
        return Satellites;
    }

    public void setSatellites(int satellites) {
        Satellites = satellites;
    }

    public int getQualitylnd() {
        return Qualitylnd;
    }

    public void setQualitylnd(int qualitylnd) {
        Qualitylnd = qualitylnd;
    }

    public String getRangeStr() {
        return rangeStr;
    }

    public void setRangeStr(String rangeStr) {
        this.rangeStr = rangeStr;
    }

    public int getMaterialname() {
        return materialname;
    }

    public void setMaterialname(int materialname) {
        this.materialname = materialname;
    }

    public int getIshistory() {
        return ishistory;
    }

    public void setIshistory(int ishistory) {
        this.ishistory = ishistory;
    }

    public int getOrderNum() {
        return OrderNum;
    }

    public void setOrderNum(int orderNum) {
        OrderNum = orderNum;
    }

    public float getAngle() {
        return Angle;
    }

    public void setAngle(float angle) {
        Angle = angle;
    }

    public double getCoordLX() {
        return CoordLX;
    }

    public void setCoordLX(double coordLX) {
        CoordLX = coordLX;
    }

    public double getCoordLY() {
        return CoordLY;
    }

    public void setCoordLY(double coordLY) {
        CoordLY = coordLY;
    }

    public double getCoordRX() {
        return CoordRX;
    }

    public void setCoordRX(double coordRX) {
        CoordRX = coordRX;
    }

    public double getCoordRY() {
        return CoordRY;
    }

    public void setCoordRY(double coordRY) {
        CoordRY = coordRY;
    }

    public Double getZhuangLX() {
        return ZhuangLX;
    }

    public void setZhuangLX(Double zhuangLX) {
        ZhuangLX = zhuangLX;
    }

    public Double getZhuangLY() {
        return ZhuangLY;
    }

    public void setZhuangLY(Double zhuangLY) {
        ZhuangLY = zhuangLY;
    }

    public Double getZhuangRX() {
        return ZhuangRX;
    }

    public void setZhuangRX(Double zhuangRX) {
        ZhuangRX = zhuangRX;
    }

    public Double getZhuangRY() {
        return ZhuangRY;
    }

    public void setZhuangRY(Double zhuangRY) {
        ZhuangRY = zhuangRY;
    }

    @Override
    public String toString() {
        return "RollingData{" +
                "CoordX=" + CoordX +
                ", CoordY=" + CoordY +
                ", Latitude=" + Latitude +
                ", Longitude=" + Longitude +
                ", Elevation=" + Elevation +
                ", Timestamp=" + Timestamp +
                ", VehicleID='" + VehicleID + '\'' +
                ", Speed=" + Speed +
                ", LayerID=" + LayerID +
                ", IsForward=" + IsForward +
                ", IsVibrate=" + IsVibrate +
                ", VibrateValue=" + VibrateValue +
                ", Frequency=" + Frequency +
                ", Acceleration=" + Acceleration +
                ", Amplitude=" + Amplitude +
                ", Satellites=" + Satellites +
                ", Qualitylnd=" + Qualitylnd +
                ", ZhuangX=" + ZhuangX +
                ", ZhuangY=" + ZhuangY +
                ", OrderNum=" + OrderNum +
                ", Angle=" + Angle +
                ", CoordLX=" + CoordLX +
                ", CoordLY=" + CoordLY +
                ", CoordRX=" + CoordRX +
                ", CoordRY=" + CoordRY +
                ", ZhuangLX=" + ZhuangLX +
                ", ZhuangLY=" + ZhuangLY +
                ", ZhuangRX=" + ZhuangRX +
                ", ZhuangRY=" + ZhuangRY +
                '}';
    }
}

