package com.tianji.dam.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel(description = "碾压数据-车辆基础信息")
@Data  
public class TrackCar {
    private Integer id;
    @ApiModelProperty(name = "CoordX", value = "平面坐标X", required = true, example = "2871933.79")
    private Double coordx;
    @ApiModelProperty(name = "CoordY", value = "平面坐标Y", required = true, example = "383458.0")
    private Double coordy;
    @ApiModelProperty(name = "Latitude", value = "纬度", required = true, example = "106.83")
    private Double latitude;
    @ApiModelProperty(name = "Longitude", value = "经度", required = true, example = "25.95")
    private Double longitude;
    @ApiModelProperty(name = "Elevation", value = "高程", required = true, example = "990.87")
    private Float elevation;
    @ApiModelProperty(name = "Timestamp", value = "上传时间", required = true, example = "1585438927000")
    private Long timestamp;
    @ApiModelProperty(name = "VehicleID", value = "车辆ID", required = true, example = "1")
    private String vehicleid;
    @ApiModelProperty(name = "Speed", value = "速度", required = true, example = "0.566944")
    private Float speed;
    @ApiModelProperty(name = "LayerID", value = "碾压层级", required = true, example = "130")
    private Integer layerid;
    @ApiModelProperty(name = "IsForward", value = "前后向", required = true, example = "0")
    private Integer isforward; 
    @ApiModelProperty(name = "IsVibrate", value = "动静压", required = true, example = "0")
    private Integer isvibrate;
    @ApiModelProperty(name = "VibrateValue", value = "震动检测值", required = true, example = "0.0")
    private Double vibratevalue;
    @ApiModelProperty(name = "Frequency", value = "频率检测值", required = true, example = "0.0")
    private Double frequency;
    @ApiModelProperty(name = "Acceleration", value = "加速度检测值", required = true, example = "0.0")
    private Double acceleration;
    @ApiModelProperty(name = "Amplitude", value = "振幅", required = true, example = "0.0")
    private Double amplitude;
    @ApiModelProperty(name = "Satellites", value = "卫星数量", required = true, example = "30")
    private Integer satellites;
    @ApiModelProperty(name = "ZhuangX", value = "桩号X", required = true, example = "16910.27")
    private Double zhuangx;
    @ApiModelProperty(name = "ZhuangY", value = "桩号Y", required = true, example = "2232038.98")
    private Double zhuangy;
    @ApiModelProperty(name = "ishistory", value = "是否为历史", required = true, example = "1")
    private Integer ishistory;
    @ApiModelProperty(name = "materialname", value = "材料ID", required = true, example = "1")
    private Integer materialname;
    @ApiModelProperty(name = "OrderNum", value = "顺序号", required = true, example = "1")
    private Integer ordernum;
    @ApiModelProperty(name = "Angle", value = "角度", required = true, example = "77.0404")
    private Float angle;
    @ApiModelProperty(name = "CoordLX", value = "轮左X", required = true, example = "2871934.77")
    private Double coordlx;
    @ApiModelProperty(name = "CoordLY", value = "轮左Y", required = true, example = "383457.84")
    private Double coordly;
    @ApiModelProperty(name = "CoordRX", value = "轮右X", required = true, example = "2871932.81")
    private Double coordrx;
    @ApiModelProperty(name = "CoordRY", value = "轮右Y", required = true, example = "383458.23")
    private Double coordry;

    @ApiModelProperty(name = "carID", value = "车辆ID", required = true, example = "1")
    private Integer carID;

    @ApiModelProperty(name = "carType", value = "车辆类型", required = true, example = "碾压车1")
    private String carType;

    @ApiModelProperty(name = "addTime", value = "添加时间", required = true, example = "2019-08-27 13:22:33")
    private String addTime;

    @ApiModelProperty(name = "driverID", value = "司机ID", required = true, example = "1")
    private Integer driverID;


    @ApiModelProperty(name = "updateTime", value = "更新时间", required = true, example = "2019-08-27 13:22:33")
    private String updateTime;

    @ApiModelProperty(name = "remark", value = "备注", required = true, example = "备注")
    private String remark;

}
