package com.tianji.dam.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "配色设置VO辅助类")
@Data
public class TColorConfigVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "pageNum", value = "当前页", required = true,  example = "1")
    private Integer pageNum;
    @ApiModelProperty(name = "pageSize", value = "每页数", required = true,  example = "10")
    private Integer pageSize;
    @ApiModelProperty(name = "type", value = "类型(1碾压边次 2超限次数 3机车速度 4机车频率 5沉降量)",example = "1")
    private Long type;
}
