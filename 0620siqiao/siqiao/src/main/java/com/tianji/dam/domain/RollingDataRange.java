package com.tianji.dam.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "碾压数据范围")
@Data
public class RollingDataRange {

    @ApiModelProperty(name = "MaxCoordX", value = "坐标X最大值", required = true, example = "2871933.79")
    private Double MaxCoordX;
    @ApiModelProperty(name = "MinCoordX", value = "坐标X最小值", required = true, example = "2871933.79")
    private Double MinCoordX;
    @ApiModelProperty(name = "MaxCoordY", value = "坐标Y最大值", required = true, example = "106.83")
    private Double MaxCoordY;
    @ApiModelProperty(name = "MinCoordY", value = "坐标Y最小值", required = true, example = "106.83")
    private Double MinCoordY;

}
