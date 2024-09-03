package com.tianji.dam.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "设计VO辅助类")
@Data
public class TDesignVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "pageNum", value = "当前页", required = true,  example = "1")
    private Integer pageNum;
    @ApiModelProperty(name = "pageSize", value = "每页数", required = true,  example = "10")
    private Integer pageSize;
}
