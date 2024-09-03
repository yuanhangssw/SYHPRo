package com.tianji.dam.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel(description = "日期-工作量")
@Data
public class DistanceTime {
    @ApiModelProperty(name = "date", value = "日期", required = true, example = "20200114")
    String date;
    @ApiModelProperty(name = "distances", value = "工作量", required = true, example = "1000.5")
    BigDecimal distance;
    @ApiModelProperty(name = "carId", value = "车辆ID", required = true, example = "1")
    int carId;

}
