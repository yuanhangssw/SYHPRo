package com.tianji.dam.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "车辆信息")
@Data
public class Car {
    @ApiModelProperty(name = "carID", value = "车辆ID", required = true, example = "1")
    private Integer carID;

    @ApiModelProperty(name = "carType", value = "车辆类型", required = true, example = "碾压车1")
    private String carType;

    @ApiModelProperty(name = "addTime", value = "添加时间", required = true, example = "2019-08-27 13:22:33")
    private String addTime;

    @ApiModelProperty(name = "driverID", value = "司机ID", required = true, example = "1")
    private Integer driverID;

    @ApiModelProperty(name = "driverName", value = "司机姓名", required = true, example = "司机1")
    private String driverName;

    @ApiModelProperty(name = "updateTime", value = "更新时间", required = true, example = "2019-08-27 13:22:33")
    private String updateTime;

    @ApiModelProperty(name = "remark", value = "备注", required = true, example = "备注")
    private String remark;
    //车辆状态 
    private String status;
    //吨位
    private Double tonnage;
    //轮宽
    private Double lunkuan;
    //操作手
    private String driver;
    //车辆类型
    private Integer type;


}
