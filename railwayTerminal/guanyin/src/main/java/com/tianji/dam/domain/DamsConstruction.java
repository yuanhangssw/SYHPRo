package com.tianji.dam.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@ApiModel(description = "组织结构")
@Data
public class DamsConstruction {
    @ApiModelProperty(name = "id", value = "工程ID", required = true, example = "1")
    private Integer id;

    @ApiModelProperty(name = "damsid", value = "坝ID", required = true, example = "1")
    private Integer damsid;

    @ApiModelProperty(name = "pid", value = "上一级工程ID", required = true, example = "1")
    private Integer pid;

    @ApiModelProperty(name = "title", value = "标题", required = true, example = "上游1")
    private String title;

    @ApiModelProperty(name = "engcode", value = "编码", required = true, example = "43_3_4")
    private String engcode;

    @ApiModelProperty(name = "status", value = "状态码", required = true, example = "8")
    private Integer status;

    @ApiModelProperty(name = "gaocheng", value = "层初始层高", required = true, example = "970.416")
    private Double gaocheng;

    @ApiModelProperty(name = "gaochengact", value = "层初始层高", required = true, example = "970.416")
    private Double gaochengact;
    @ApiModelProperty(name = "cenggao", value = "层高", required = true, example = "1.0")
    private Double cenggao;

    @ApiModelProperty(name = "planstarttime", value = "计划开工日期", required = true, example = "2019-08-14")
    private String planstarttime;

    @ApiModelProperty(name = "planendtime", value = "计划竣工日期", required = true, example = "2020-06-30")
    private String planendtime;

    @ApiModelProperty(name = "actualstarttime", value = "实际开工日期", required = true, example = "2019-08-14")
    private String actualstarttime;

    @ApiModelProperty(name = "actualendtime", value = "实际竣工日期", required = true, example = "2020-06-30")
    private String actualendtime;

    @ApiModelProperty(name = "remarks", value = "备注", required = true, example = "备注")
    private String remarks;

    @ApiModelProperty(name = "materialname", value = "材料ID", required = true, example = "1")
    private String materialname;

    @ApiModelProperty(name = "addtime", value = "添加时间时间戳", required = true, example = "1565787296000")
    private String addtime;

    @ApiModelProperty(name = "updatetime", value = "更新时间时间戳", required = true, example = "1565787296000")
    private String updatetime;

    @ApiModelProperty(name = "edge", value = "边界点", required = true)
    private String edge;

    @ApiModelProperty(name = "rect", value = "外接矩形", required = true)
    private String rect;

    @ApiModelProperty(name = "xbegin", value = "X开始值", required = true, example = "100")
    private Double xbegin;

    @ApiModelProperty(name = "xend", value = "X结束值", required = true, example = "200")
    private Double xend;

    @ApiModelProperty(name = "ybegin", value = "Y开始值", required = true, example = "100")
    private Double ybegin;

    @ApiModelProperty(name = "yend", value = "Y结束值", required = true, example = "200")
    private Double yend;

    @ApiModelProperty(name = "iscang", value = "是否仓", required = true, example = "1")
    private Integer iscang;

    @ApiModelProperty(name = "hasdata", value = "是否有数据", required = true, example = "1")
    private Integer hasdata;

    @ApiModelProperty(name = "tablename", value = "表名", required = true, example = "1-1-4")
    private String tablename;

    @ApiModelProperty(name = "heightIndex", value = "层ID", required = true, example = "1")
    private Integer heightIndex;
    @ApiModelProperty(name = "spread", value = "是否展开", required = true, example = "true")
    private Boolean spread = true;
    @ApiModelProperty(name = "children", value = "子工程", required = false)
    private List<DamsConstruction> children;
    @ApiModelProperty(name = "href", value = "链接父工程", required = false, example = "treeLevel1?id=1")
    private String href;
    @ApiModelProperty(name = "speed", value = "速度限制", required = true, example = "10")
   private Double speed ;
    @ApiModelProperty(name = "frequency", value = "碾压遍数限制", required = true, example = "8")
   private Integer frequency;
   
    @ApiModelProperty(name = "ranges", value = "范围", required = true, example = "8")
    private String ranges;

    private String freedom1;

    private String freedom2;

    private String freedom3;

    private String freedom4;

    private String freedom5;
    private double houdu;
    private int types;
    private String timepass;
    private String houdus;

    private Map<String,String> pzmap;


}