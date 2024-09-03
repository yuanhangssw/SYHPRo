package com.tianji.dam.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/*材料实体类*/
@ApiModel(description = "材料信息")
@Data
public class Material {
    @ApiModelProperty(name = "id", value = "标识", required = true, example = "1")
    private int id;//主键
    @ApiModelProperty(name = "mid", value = "材料编号", required = true, example = "1")
    private int mid;//材料编号
    @ApiModelProperty(name = "materialname", value = "材料名称", required = true, example = "主堆石料 ")
    private String materialname;//材料名称


}
