package com.tianji.dam.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel(description = "车辆总工作量")
@Data
public class DistanceSum {
    @ApiModelProperty(name = "carId", value = "车辆ID", required = true, example = "1")
    private int carId;
    @ApiModelProperty(name = "sum", value = "单车工作量", required = true, example = "1000.5")
    private BigDecimal sum;

}
