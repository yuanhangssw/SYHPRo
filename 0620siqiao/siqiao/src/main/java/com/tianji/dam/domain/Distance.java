package com.tianji.dam.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "车辆里程") 
@Data
public class Distance {
    @ApiModelProperty(name = "id", value = "标识", required = true, example = "1")
    private Integer id;

    @ApiModelProperty(name = "date", value = "日期", required = true, example = "20200114")
    private String date;

    @ApiModelProperty(name = "distance", value = "里程", required = true, example = "1000.5")
    private Double distance;

    @ApiModelProperty(name = "carId", value = "车辆ID", required = true, example = "1")
    private Integer carId;

}