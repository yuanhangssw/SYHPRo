package com.tianji.dam.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "车辆碾压的起始与结束时间")
public class CarTime {
    @ApiModelProperty(name = "VehicleID", value = "车辆ID", required = true, example = "1")
    private String  VehicleID;
    @ApiModelProperty(name = "startTime", value = "车辆开始碾压时间", required = true, example = "2021-06-06 05:40:10")
    private String startTime;
    @ApiModelProperty(name = "endTime", value = "车辆结束碾压时间", required = true, example = "2021-06-06 09:56:52")
    private String endTime;

}
