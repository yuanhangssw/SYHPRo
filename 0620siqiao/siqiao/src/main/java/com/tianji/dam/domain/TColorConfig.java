package com.tianji.dam.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(description = "配色设置")
@Data
public class TColorConfig implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "id", value = "配色设置id",example = "1")
    private Long id;
    @ApiModelProperty(name = "level", value = "级别",example = "0")
    private Long level;
    @ApiModelProperty(name = "num", value = "数量",example = "0")
    private Long num;
    @ApiModelProperty(name = "c", value = "从",example = "0")
    private Long c;
    @ApiModelProperty(name = "d", value = "到",example = "1")
    private Long d;
    @ApiModelProperty(name = "type", value = "类型(1碾压边次 2超限次数 3机车速度 4机车频率 5沉降量 6动静碾压)",example = "1")
    private Long type;
    @ApiModelProperty(name = "color", value = "颜色",example = "#e6edf1")
    private String color;
    @ApiModelProperty(name = "explain", value = "描述",example = "合格")
    private String plain;


}
