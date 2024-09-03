package com.tianji.dam.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "时间-超限速度")
@Data
public class DateSpeed {
    @ApiModelProperty(name = "dateTime", value = "时间", required = true, example = "2021-01-01 13:23:16")
    private String dateTime;
    @ApiModelProperty(name = "overSpeed", value = "超限速度值", required = true, example = "302.39")
    private Float overSpeed;
    @ApiModelProperty(name = "VehicleID", value = "车辆ID", required = true, example = "1")
    private int VehicleID;

}
