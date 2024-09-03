package com.tianji.dam.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tianji.dam.utils.ExChange;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Slf4j
@JsonIgnoreProperties(ignoreUnknown = true)
public class RollingData implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String tablename;
    private Integer subid;

    public String getTablename() {
        return tablename;
    }

    public Integer getSubid() {
        return subid;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public void setSubid(Integer subid) {
        this.subid = subid;
    }

    private Double CoordX;//平面坐标X
    private Double CoordY;//平面坐标Y
    private Double Latitude;//纬度
    private Double Longitude;//经度
    private Float Elevation;//高程
    private Long Timestamp;//上传时间
    private int LengthVehicleID;//车辆ID长度
    private String VehicleID;//车辆ID:
    private Float Speed;//速度
    private Integer LayerID;//碾压层级
    private int IsForward;//前后向
    private int IsVibrate;//动静压
    private Double VibrateValue = -1d;//震动检测值
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

    private Float CurrentEvolution;//当前点高程
    private double BeforeCoordLX;//前一个点轮左X
    private double BeforeCoordLY;//前一个点轮左Y
    private double BeforeCoordRX;//前一个点轮右X
    private double BeforeCoordRY;//前一个点轮右Y
    private Float BeforeElevation;//前点高程

    private double qhangle; //前后角度
    private double zyangle;//左右角度

    private double zhuanghao;
    private double pianju;

    private double houdu;


    //动静碾
    private int iszhen;

    public int getLengthVehicleID() {
        return LengthVehicleID;
    }

    public void setLengthVehicleID(int lengthVehicleID) {
        LengthVehicleID = lengthVehicleID;
    }

    public int getIszhen() {
        return iszhen;
    }

    public void setIszhen(int iszhen) {
        this.iszhen = iszhen;
    }

    public Float getCurrentEvolution() {
        return CurrentEvolution;
    }

    public void setCurrentEvolution(Float currentEvolution) {
        CurrentEvolution = currentEvolution;
    }

    public double getBeforeCoordLX() {
        return BeforeCoordLX;
    }

    public void setBeforeCoordLX(double beforeCoordLX) {
        BeforeCoordLX = beforeCoordLX;
    }

    public double getBeforeCoordLY() {
        return BeforeCoordLY;
    }

    public void setBeforeCoordLY(double beforeCoordLY) {
        BeforeCoordLY = beforeCoordLY;
    }

    public double getBeforeCoordRX() {
        return BeforeCoordRX;
    }

    public void setBeforeCoordRX(double beforeCoordRX) {
        BeforeCoordRX = beforeCoordRX;
    }

    public double getBeforeCoordRY() {
        return BeforeCoordRY;
    }

    public void setBeforeCoordRY(double beforeCoordRY) {
        BeforeCoordRY = beforeCoordRY;
    }

    public Float getBeforeElevation() {
        return BeforeElevation;
    }

    public void setBeforeElevation(Float beforeElevation) {
        BeforeElevation = beforeElevation;
    }

    public double getQhangle() {
        return qhangle;
    }

    public void setQhangle(double qhangle) {
        this.qhangle = qhangle;
    }

    public double getZyangle() {
        return zyangle;
    }

    public void setZyangle(double zyangle) {
        this.zyangle = zyangle;
    }


    public double getHoudu() {
        return houdu;
    }

    public void setHoudu(double houdu) {
        this.houdu = houdu;
    }

    public double getZhuanghao() {
        return zhuanghao;
    }

    public void setZhuanghao(double zhuanghao) {
        this.zhuanghao = zhuanghao;
    }

    public double getPianju() {
        return pianju;
    }

    public void setPianju(double pianju) {
        this.pianju = pianju;
    }

    public RollingData() {
    }

    public RollingData(Double coordX, Double coordY, Double latitude, Double longitude,
                       Float elevation, Long timestamp, String vehicleID, Float speed,
                       Integer layerID, int isForward, int isVibrate, Double vibrateValue,
                       Double frequency, Double acceleration, Double amplitude, int satellites,
                       int qualitylnd, Double zhuangX, Double zhuangY, int orderNum, double zhuanghao, double pianju,
                       Float CurrentEvolution, Double BeforeCoordLX, Double BeforeCoordLY, Double BeforeCoordRX, Double BeforeCoordRY,
                       Float BeforeElevation, Double qhangle, Double zyangle
    ) {
        CoordX = coordX;
        CoordY = coordY;
        Latitude = latitude;
        Longitude = longitude;
        Elevation = elevation;
        Timestamp = timestamp;
        VehicleID = vehicleID;
        Speed = speed;
        LayerID = layerID;
        IsForward = isForward;
        IsVibrate = isVibrate;
        VibrateValue = vibrateValue;
        Frequency = frequency;
        Acceleration = acceleration;
        Amplitude = amplitude;
        Satellites = satellites;
        Qualitylnd = qualitylnd;
        ZhuangX = zhuangX;
        ZhuangY = zhuangY;
        OrderNum = orderNum;
        this.zhuanghao = zhuanghao;
        this.pianju = pianju;
        this.CurrentEvolution = CurrentEvolution;
        this.BeforeCoordLX = BeforeCoordLX;
        this.BeforeCoordLY = BeforeCoordLY;
        this.BeforeCoordRX = BeforeCoordRX;
        this.BeforeCoordRY = BeforeCoordRY;
        this.BeforeElevation = BeforeElevation;
        this.qhangle = qhangle;
        this.zyangle = zyangle;
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

    @Override
    public String toString() {
        return "RollingData{" +
                "CoordX=" + CoordX +
                ", CoordY=" + CoordY +
                ", Latitude=" + Latitude +
                ", Longitude=" + Longitude +
                ", Elevation=" + Elevation +
                ", Timestamp=" + Timestamp +
                ", LengthVehicleID=" + LengthVehicleID +
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
                ",zhuanghao=" + zhuanghao +
                ", pianju=" + pianju +
                '}';
    }

    /**
     * 如果对象类型是User,先比较hashcode，一致的场合再比较每个属性的值
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (obj instanceof RollingData) {
            RollingData rollingData = (RollingData) obj;
            // 比较每个属性的值 一致时才返回true
            if (rollingData.LayerID.equals(this.LayerID) && Integer.valueOf(rollingData.materialname).equals(this.materialname))
                ;
            return true;
        }
        return false;
    }

    /**
     * 重写hashcode 方法，返回的hashCode不一样才再去比较每个属性的值
     */
    @Override
    public int hashCode() {
        return LayerID.hashCode() * Integer.valueOf(materialname).hashCode();
    }


    public byte[] toBinary() {
        ExChange ex = new ExChange(false);
        try {
            ex.AddDoubleAsBytes(CoordX);
            ex.AddDoubleAsBytes(CoordY);
            ex.AddDoubleAsBytes(Longitude);
            ex.AddDoubleAsBytes(Latitude);
            ex.AddFloatAsBytes(Elevation);
            ex.AddLongAsBytes(Timestamp);
            ex.AddStringAsBytes(VehicleID);
            ex.AddFloatAsBytes(Speed);
            ex.AddIntAsBytes(LayerID);
            ex.AddIntAsBytes(IsForward);
            ex.AddIntAsBytes(IsVibrate);
            ex.AddDoubleAsBytes(VibrateValue);
            ex.AddDoubleAsBytes(Frequency);
            ex.AddDoubleAsBytes(Acceleration);
            ex.AddDoubleAsBytes(Amplitude);
            ex.AddIntAsBytes(Satellites);
            ex.AddIntAsBytes(Qualitylnd);
            ex.AddIntAsBytes(materialname);
            ex.AddIntAsBytes(OrderNum);
            ex.AddFloatAsBytes(Angle);
            ex.AddDoubleAsBytes(CoordLX);
            ex.AddDoubleAsBytes(CoordLY);
            ex.AddDoubleAsBytes(CoordRX);
            ex.AddDoubleAsBytes(CoordRY);
            ex.AddIntAsBytes(ishistory);

        } catch (Exception e) {
            /*  log.error(e.getMessage());*/
        }
        return ex.GetAllBytes();
    }

    public byte[] toBinary2() {
        ExChange ex = new ExChange(false);
        try {
            ex.AddDoubleAsBytes(CoordX);
            ex.AddDoubleAsBytes(CoordY);
            ex.AddDoubleAsBytes(Longitude);
            ex.AddDoubleAsBytes(Latitude);
            ex.AddFloatAsBytes(Elevation);
            ex.AddLongAsBytes(Timestamp);
            ex.AddStringAsBytes(VehicleID);
            ex.AddFloatAsBytes(Speed);
            ex.AddIntAsBytes(LayerID);
            ex.AddIntAsBytes(IsForward);
            ex.AddIntAsBytes(IsVibrate);
            ex.AddIntAsBytes(Satellites);
            ex.AddIntAsBytes(Qualitylnd);
            ex.AddIntAsBytes(materialname);
            ex.AddIntAsBytes(OrderNum);
            ex.AddFloatAsBytes(Angle);
            ex.AddDoubleAsBytes(CoordLX);
            ex.AddDoubleAsBytes(CoordLY);
            ex.AddDoubleAsBytes(CoordRX);
            ex.AddDoubleAsBytes(CoordRY);
            ex.AddIntAsBytes(ishistory);
            ex.AddFloatAsBytes(CurrentEvolution);
            ex.AddDoubleAsBytes(BeforeCoordLX);
            ex.AddDoubleAsBytes(BeforeCoordLY);
            ex.AddDoubleAsBytes(BeforeCoordRX);
            ex.AddDoubleAsBytes(BeforeCoordRY);
            ex.AddFloatAsBytes(BeforeElevation);
            ex.AddDoubleAsBytes(qhangle);
            ex.AddDoubleAsBytes(zyangle);
        } catch (Exception e) {
            /*  log.error(e.getMessage());*/
        }
        return ex.GetAllBytes();
    }

    public byte[] toBinary3() {
        ExChange ex = new ExChange(false);
        try {
            ex.AddDoubleAsBytes(CoordX);
            ex.AddDoubleAsBytes(CoordY);
            ex.AddDoubleAsBytes(Longitude);
            ex.AddDoubleAsBytes(Latitude);
            ex.AddFloatAsBytes(Elevation);
            ex.AddLongAsBytes(Timestamp);
            ex.AddStringAsBytes(VehicleID);
            ex.AddFloatAsBytes(Speed);
            ex.AddIntAsBytes(LayerID);
            ex.AddIntAsBytes(IsForward);
            ex.AddIntAsBytes(IsVibrate);
            ex.AddDoubleAsBytes(VibrateValue);
            ex.AddDoubleAsBytes(Frequency);
            ex.AddDoubleAsBytes(Acceleration);
            ex.AddDoubleAsBytes(Amplitude);
            ex.AddIntAsBytes(Satellites);
            ex.AddIntAsBytes(Qualitylnd);
            ex.AddIntAsBytes(materialname);
            ex.AddIntAsBytes(OrderNum);
            ex.AddFloatAsBytes(Angle);
            ex.AddDoubleAsBytes(CoordLX);
            ex.AddDoubleAsBytes(CoordLY);
            ex.AddDoubleAsBytes(CoordRX);
            ex.AddDoubleAsBytes(CoordRY);
            ex.AddIntAsBytes(ishistory);
            ex.AddFloatAsBytes(CurrentEvolution);
            ex.AddDoubleAsBytes(BeforeCoordLX);
            ex.AddDoubleAsBytes(BeforeCoordLY);
            ex.AddDoubleAsBytes(BeforeCoordRX);
            ex.AddDoubleAsBytes(BeforeCoordRY);
            ex.AddFloatAsBytes(BeforeElevation);
            ex.AddDoubleAsBytes(qhangle);
        } catch (Exception e) {
            /*  log.error(e.getMessage());*/
        }
        return ex.GetAllBytes();
    }
}
