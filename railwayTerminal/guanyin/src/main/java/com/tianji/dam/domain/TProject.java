package com.tianji.dam.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@ApiModel(description = "工程信息")
@Data
public class TProject implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "id", value = "工程id",example = "1")
    private Long id;
    @ApiModelProperty(name = "name", value = "名称",example = "平寨水库")
    private String name;
    @ApiModelProperty(name = "logo", value = "LOGO",example = "LOGO图片地址")
    private String logo;
    @ApiModelProperty(name = "unitName", value = "工程名称",example = "")
    private String unitName;
    @ApiModelProperty(name = "address", value = "地点",example = "")
    private String address;
    @ApiModelProperty(name = "description", value = "基础信息描述",example = "")
    private String description;
    @ApiModelProperty(name = "code", value = "工程编码",example = "0000")
    private String code;
    @ApiModelProperty(name = "zhuangX", value = "起始桩号",example = "0")
    private BigDecimal zhuangX;
    @ApiModelProperty(name = "zhuangY", value = "结束桩号",example = "246")
    private BigDecimal zhuangY;
    @ApiModelProperty(name = "ownerUnit", value = "业主单位",example = "司")
    private String ownerUnit;
    @ApiModelProperty(name = "sgUnit", value = "施工单位",example = "xxxxxxx")
    private String sgUnit;
    @ApiModelProperty(name = "jlUnit", value = "监理单位",example = "xxxxxxx")
    private String jlUnit;
    @ApiModelProperty(name = "designUnit", value = "设计单位",example = "")
    private String designUnit;
    @ApiModelProperty(name = "planStartDate", value = "计划开工日期",example = "2021-07-15")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date planStartDate;
    @ApiModelProperty(name = "planEndDate", value = "计划竣工日期",example = "2021-07-15")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date planEndDate;
    @ApiModelProperty(name = "realStartDate", value = "实际开工日期",example = "2021-07-15")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date realStartDate;
    @ApiModelProperty(name = "realEndDate", value = "实际竣工日期",example = "2021-07-15")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date realEndDate;
    @ApiModelProperty(name = "remarks", value = "单元工程备注",example = "")
    private String remarks;
    @ApiModelProperty(name = "protype", value = "项目类型",example = "1：铁路项目，2：大坝项目、河道项目")
    private Integer  protype;
    private Double  begin_evolution;
    private Double end_evolution;

}
