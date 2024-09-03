package com.tianji.dam.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "单元工程范围VO辅助类")
@Data
public class RangesVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "id", value = "单位工程id", required = true,  example = "1L")
    private Long id;

    @ApiModelProperty(name = "ranges", value = "范围", required = true,  example = "[{\"x\":283.05,\"y\":29.678},{\"x\":502.421,\"y\":30.421},{\"x\":503.165,\"y\":130.811},{\"x\":284.537,\"y\":127.837}]")
    private String ranges;

}
