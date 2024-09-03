package com.tianji.dam.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel(description = "轨迹确认")
@Data
public class Sysconfig {
    @ApiModelProperty(name = "id", value = "标识", required = true, example = "1")
    private Integer id;
    @ApiModelProperty(name = "sysconfigName", value = "设置名称", required = true, example = "主框架页-默认皮肤样式名称")
    private String sysconfigName;
    @ApiModelProperty(name = "sysKey", value = "设置的键", required = true, example = "sys.index.skinName")
    private String sysKey;

    @ApiModelProperty(name = "sysKeyvalue", value = "设置的键值", required = true, example = "skin-blue")
    private String sysKeyvalue;

    @ApiModelProperty(name = "createdby", value = "创建用户", required = true, example = "test")
    private String createdby;

    @ApiModelProperty(name = "createtime", value = "创建时间", required = true, example = "2018-03-16 11:33:00")
    private Date createtime;

    @ApiModelProperty(name = "updateby", value = "修改用户", required = true, example = "admin")
    private String updateby;

    @ApiModelProperty(name = "updatetime", value = "修改时间", required = true, example = "2018-03-16 11:33:00")
    private Date updatetime;

    @ApiModelProperty(name = "remark", value = "备注", required = true, example = "蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow")
    private String remark;

    @ApiModelProperty(name = "defaultvalue", value = "默认值", required = true, example = "skin-blue")
    private String defaultvalue;


}