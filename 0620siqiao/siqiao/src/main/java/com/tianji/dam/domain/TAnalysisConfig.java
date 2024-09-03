package com.tianji.dam.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(description = "分析设置")
@Data
public class TAnalysisConfig  implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "id", value = "分析设置id",example = "2")
    private Long id;
    @ApiModelProperty(name = "num", value = "碾压次数限制",example = "10")
    private Long num;
    @ApiModelProperty(name = "speed", value = "机车速度限制",example = "4")
    private Long speed;
    @ApiModelProperty(name = "rate", value = "机车频率限制",example = "5")
    private Long rate;
    @ApiModelProperty(name = "path", value = "底图地址",example = "底图.png")
    private String path;
    @ApiModelProperty(name = "x", value = "X方向的网格宽度(cm)",example = "5")
    private Double x;
    @ApiModelProperty(name = "y", value = "Y方向的网格宽度(cm)",example = "5")
    private Double y;
    @ApiModelProperty(name = "z", value = "Z方向的网格宽度(cm)",example = "5")
    private Double z;
    @ApiModelProperty(name = "fourParamX", value = "四参数X方向偏移",example = "37538695.25")
    private Double fourParamX;
    @ApiModelProperty(name = "fourParamY", value = "四参数Y方向偏移",example = "2424199.06")
    private Double fourParamY;
    @ApiModelProperty(name = "fourParamZ", value = "四参数Z方向偏移",example = "0")
    private Double fourParamZ;
    @ApiModelProperty(name = "fourParamAngle", value = "四参数旋转角度",example = "0.0590808937176625")
    private Double fourParamAngle;
    @ApiModelProperty(name = "fourParamFactor", value = "四参数放大因子",example = "1")
    private Double fourParamFactor;
    
    private Double img_width;
    private Double img_height;

    public TAnalysisConfig() {
    }

    /*重置分析*/
    public TAnalysisConfig(TAnalysisConfig config) {
        this.id = 2L;
        this.num = config.getNum();
        this.speed = config.getSpeed();
        this.rate = config.getRate();
    }
    /*重置四参数*/
    public TAnalysisConfig(TAnalysisConfig config,boolean flag) {
        this.id = 2L;
        this.path = config.getPath();
        this.x = config.getX();
        this.y = config.getY();
        this.z = config.getZ();
        this.fourParamX = config.getFourParamX();
        this.fourParamY = config.getFourParamY();
        this.fourParamZ = config.getFourParamZ();
        this.fourParamAngle = config.getFourParamAngle();
        this.fourParamFactor = config.getFourParamFactor();
    }
}
