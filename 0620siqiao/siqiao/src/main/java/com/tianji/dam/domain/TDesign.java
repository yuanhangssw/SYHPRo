package com.tianji.dam.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(description = "设计")
@Data
public class TDesign implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "id", value = "设计id", example = "1")
    private Long id;
    @ApiModelProperty(name = "mileage", value = "点的里程值", example = "0")
    private String mileage;
    @ApiModelProperty(name = "distance", value = "点的偏距值", example = "0")
    private Double distance;
    @ApiModelProperty(name = "x", value = "坐标X", example = "0")
    private Double x;
    @ApiModelProperty(name = "y", value = "坐标Y", example = "0")
    private Double y;

    @ApiModelProperty(name = "pointName", value = "点名", example = "大坝起点")
    private String pointName;
    @ApiModelProperty(name = "pointType", value = "类型1起点，2终点", example = "1")
    private int pointType;
    @ApiModelProperty(name = "n", value = "N", example = "0")
    private Double n;
    @ApiModelProperty(name = "e", value = "E", example = "0")
    private Double e;
    @ApiModelProperty(name = "remarks", value = "备注", example = "这是大坝起点")
    private String remarks;
    /**
     * 多线组合时，区分 所属线段组合
     */
    private Integer type;
    
    private String lat;//纬度
    private String lng;//经度

}
