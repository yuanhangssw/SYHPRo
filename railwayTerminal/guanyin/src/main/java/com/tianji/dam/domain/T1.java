package com.tianji.dam.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(description = "原始轨迹数据信息")
@Data
public class T1 implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "id", value = "车辆轨迹数据id", example = "1")
    private Integer id;
    @ApiModelProperty(name = "coordX", value = "平面坐标X", example = "5103852.203966958")
    private Double coordX;
    @ApiModelProperty(name = "coordY", value = "平面坐标Y", example = "400727.0807658714")
    private Double coordY;
    @ApiModelProperty(name = "latitude", value = "纬度", example = "82.7170161905")
    private Double latitude;
    @ApiModelProperty(name = "longitude", value = "经度", example = "46.062666157500004")
    private Double longitude;
    @ApiModelProperty(name = "elevation", value = "高程", example = "970.376")
    private Double elevation;
    @ApiModelProperty(name = "timestamp", value = "上传时间", example = "1623835369000")
    private Long timestamp;
    @ApiModelProperty(name = "lengthVehicleID", value = "车辆id长度", example = "0")
    private Long lengthVehicleID;
    @ApiModelProperty(name = "vehicleID", value = "车辆id，关联t_car的CarID", example = "1")
    private String vehicleID;
    @ApiModelProperty(name = "speed", value = "速度", example = "0.494372")
    private Double speed;
    @ApiModelProperty(name = "layerID", value = "碾压层级", example = "1")
    private Long layerID;
    @ApiModelProperty(name = "isForward", value = "前后向(0前进 1后退)", example = "0")
    private Long isForward;
    @ApiModelProperty(name = "isVibrate", value = "动静压", example = "0")
    private Long isVibrate;
    @ApiModelProperty(name = "vibrateValue", value = "震动检测值", example = "0")
    private Double vibrateValue;
    @ApiModelProperty(name = "frequency", value = "频率检测值", example = "0")
    private Double frequency;
    @ApiModelProperty(name = "acceleration", value = "加速度检测值", example = "0")
    private Double acceleration;
    @ApiModelProperty(name = "amplitude", value = "振幅", example = "0")
    private Double amplitude;
    @ApiModelProperty(name = "satellites", value = "卫星数量", example = "0")
    private Long satellites;
    @ApiModelProperty(name = "zhuangX", value = "桩坐标X", example = "631.64153174276")
    private Double zhuangX;
    @ApiModelProperty(name = "zhuangY", value = "桩坐标Y", example = "243.29206608235836")
    private Double zhuangY;
    @ApiModelProperty(name = "ishistory", value = "是否历史数据(0实时，1历史)", example = "1")
    private Long ishistory;
    @ApiModelProperty(name = "materialname", value = "材料名", example = "7")
    private Long materialname;
    @ApiModelProperty(name = "orderNum", value = "轨迹点的顺序号，关联sysrate表的OrderNum字段", example = "39980")
    private Long orderNum;
    @ApiModelProperty(name = "angle", value = "角度", example = "313.967")
    private Double angle;
    @ApiModelProperty(name = "coordLX", value = "轮左X", example = "5103851.432622996")
    private Double coordLX;
    @ApiModelProperty(name = "coordLY", value = "轮左Y", example = "400726.34642697533")
    private Double coordLY;
    @ApiModelProperty(name = "coordRX", value = "轮右X", example = "5103852.975310921")
    private Double coordRX;
    @ApiModelProperty(name = "coordRY", value = "轮右Y", example = "400727.81510476745")
    private Double coordRY;
    @ApiModelProperty(name = "rangeStr", value = "记录仓号，关联3-2表的同名字段", example = "1")
    private String rangeStr;

    private Float CurrentEvolution;//当前点高程
    private double BeforeCoordLX;//前一个点轮左X
    private double BeforeCoordLY;//前一个点轮左Y
    private double BeforeCoordRX;//前一个点轮右X
    private double BeforeCoordRY;//前一个点轮右Y
    private Float BeforeElevation;//前点高程

    private double qhangle; //前后角度
    private double zyangle;//左右角度

    private String zhuanghao;
    private String pianju;

    private double houdu;

    private String beginzhuanghao;
    private String endzhuanghao;




}
