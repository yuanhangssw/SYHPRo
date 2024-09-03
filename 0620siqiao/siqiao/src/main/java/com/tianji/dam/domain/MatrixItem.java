package com.tianji.dam.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.LinkedList;

@Component
@ApiModel(description = "矩阵项")
public class MatrixItem implements Serializable {
    @ApiModelProperty(name = "serialVersionUID", value = "用户ID", required = true, example = "828776519500192025L")
    private static final long serialVersionUID = 828776519500192025L;

    @ApiModelProperty(name = "RollingTimes", value = "碾压遍数", required = true, example = "0")
    private LinkedList<Integer> RollingTimeList = new LinkedList<>();
    private Integer RollingTimes = 0;//碾压遍数
    @ApiModelProperty(name = "IsVibrate", value = "动碾遍数", required = true, example = "0")
    private Integer IsVibrate = 0;//动碾遍数 IsVibrate为1
    @ApiModelProperty(name = "IsNotVibrate", value = "静碾遍数", required = true, example = "0")
    private Integer IsNotVibrate = 0;//静碾遍数 IsVibrate为0
    @ApiModelProperty(name = "TimestampList", value = "时间列表", required = true)
    private LinkedList<Long> TimestampList = new LinkedList<>();//时间
    @ApiModelProperty(name = "ElevationList", value = "高程列表", required = true)
    private LinkedList<Float> ElevationList = new LinkedList<>();//高程
    @ApiModelProperty(name = "SpeedList", value = "速度列表", required = true)
    private LinkedList<Float> SpeedList = new LinkedList<>();//速度
    @ApiModelProperty(name = "VehicleIDList", value = "车辆ID列表", required = true)
    private LinkedList<String> VehicleIDList = new LinkedList<>();//车辆ID:
    @ApiModelProperty(name = "VibrateValueList", value = "震动检测值列表", required = true)
    private LinkedList<Double> VibrateValueList = new LinkedList<>();//震动检测值
    @ApiModelProperty(name = "FrequencyList", value = "频率检测值列表", required = true)
    private LinkedList<Double> FrequencyList = new LinkedList<>();//频率检测值
    @ApiModelProperty(name = "AccelerationList", value = "加速度检测值列表", required = true)
    private LinkedList<Double> AccelerationList = new LinkedList<>();//加速度检测值

    private LinkedList<Float> CurrentEvolution = new LinkedList<>();//当前点高程

    private LinkedList<Float> BeforeElevation = new LinkedList<>();//前点高程

    private LinkedList<Double> qhangle = new LinkedList<>(); //前后角度
    private LinkedList<Double> zyangle = new LinkedList<>();//左右角度


    public LinkedList<Float> getCurrentEvolution() {
        return CurrentEvolution;
    }

    public void setCurrentEvolution(LinkedList<Float> currentEvolution) {
        CurrentEvolution = currentEvolution;
    }

    public LinkedList<Float> getBeforeElevation() {
        return BeforeElevation;
    }

    public void setBeforeElevation(LinkedList<Float> beforeElevation) {
        BeforeElevation = beforeElevation;
    }

    public LinkedList<Double> getQhangle() {
        return qhangle;
    }

    public void setQhangle(LinkedList<Double> qhangle) {
        this.qhangle = qhangle;
    }

    public LinkedList<Double> getZyangle() {
        return zyangle;
    }

    public void setZyangle(LinkedList<Double> zyangle) {
        this.zyangle = zyangle;
    }

    public String toString() {
        return this.RollingTimes + " " + this.IsVibrate + " " + this.IsNotVibrate;
    }

    public LinkedList<Integer> getRollingTimeList() {
        return RollingTimeList;
    }

    public void setRollingTimeList(LinkedList<Integer> rollingTimeList) {
        RollingTimeList = rollingTimeList;
    }

    public Integer getRollingTimes() {
        return RollingTimes;
    }

    public void setRollingTimes(Integer rollingTimes) {
        RollingTimes = rollingTimes;
    }

    public Integer getIsVibrate() {
        return IsVibrate;
    }

    public void setIsVibrate(Integer isVibrate) {
        IsVibrate = isVibrate;
    }

    public Integer getIsNotVibrate() {
        return IsNotVibrate;
    }

    public void setIsNotVibrate(Integer isNotVibrate) {
        IsNotVibrate = isNotVibrate;
    }

    public LinkedList<Long> getTimestampList() {
        return TimestampList;
    }

    public void setTimestampList(LinkedList<Long> timestampList) {
        TimestampList = timestampList;
    }

    public LinkedList<Float> getElevationList() {
        return ElevationList;
    }

    public void setElevationList(LinkedList<Float> elevationList) {
        ElevationList = elevationList;
    }

    public LinkedList<Float> getSpeedList() {
        return SpeedList;
    }

    public void setSpeedList(LinkedList<Float> speedList) {
        SpeedList = speedList;
    }

    public LinkedList<String> getVehicleIDList() {
        return VehicleIDList;
    }

    public void setVehicleIDList(LinkedList<String> vehicleIDList) {
        VehicleIDList = vehicleIDList;
    }

    public LinkedList<Double> getVibrateValueList() {
        return VibrateValueList;
    }

    public void setVibrateValueList(LinkedList<Double> vibrateValueList) {
        VibrateValueList = vibrateValueList;
    }

    public LinkedList<Double> getFrequencyList() {
        return FrequencyList;
    }

    public void setFrequencyList(LinkedList<Double> frequencyList) {
        FrequencyList = frequencyList;
    }

    public LinkedList<Double> getAccelerationList() {
        return AccelerationList;
    }

    public void setAccelerationList(LinkedList<Double> accelerationList) {
        AccelerationList = accelerationList;
    }
}
