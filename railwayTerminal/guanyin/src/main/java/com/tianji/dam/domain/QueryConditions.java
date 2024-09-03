package com.tianji.dam.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "统计查询条件")
@Data
public class QueryConditions {
    @ApiModelProperty(name = "pageNum", value = "页数", example = "1")
    private int pageNum;
    @ApiModelProperty(name = "pageSize", value = "每页数据量", example = "50")
    private int pageSize;
    @ApiModelProperty(name = "carId", value = "车辆Id", example = "1")
    private int carId;
    @ApiModelProperty(name = "startTime", value = "开始时间戳，目前用于统计表格内容查询", example = "1585453620000")
    private Long startTime;
    @ApiModelProperty(name = "endTime", value = "结束时间戳，目前用于统计表格内容查询", example = "1585453620000")
    private Long endTime;
    @ApiModelProperty(name = "startElevation", value = "开始高程", example = "1002.86")
    private Float startElevation;
    @ApiModelProperty(name = "endElevation", value = "结束高程", example = "1002.86")
    private Float endElevation;

    @ApiModelProperty(name = "sys_key", value = "系统设置键名", example = "sys.index.skinName")
    private String sys_key;

    @ApiModelProperty(name = "startTimeStr", value = "distance表的开始时间，目前用于t_distance表的查询", example = "20200114")
    private String startTimeStr;
    @ApiModelProperty(name = "endTimeStr", value = "distance表的结束时间，目前用于t_distance表的查询", example = "20200114")
    private String endTimeStr;
    @ApiModelProperty(name = "date", value = "日期，目前用于日工作量统计", example = "2020-01-14")
    private String date;
    private int cartype;
    private String tablename;
}
