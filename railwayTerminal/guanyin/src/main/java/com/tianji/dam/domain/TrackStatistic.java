package com.tianji.dam.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
 
@ApiModel(description = "工作量统计表格")
@Data
public class TrackStatistic {

    @ApiModelProperty(name = "VehicleID", value = "车辆ID", required = true, example = "1")
    private int VehicleID;
    @ApiModelProperty(name = "date", value = "日期", required = true, example = "2020-01-14")
    private String date;
    @ApiModelProperty(name = "startTime", value = "开始时间", required = true, example = "12:02:32")
    private String startTime;
    @ApiModelProperty(name = "endTime", value = "结束时间", required = true, example = "12:02:32")
    private String endTime;
    @ApiModelProperty(name = "minElevation", value = "最小高程", required = true, example = "990.87")
    private Float minElevation;
    @ApiModelProperty(name = "maxElevation", value = "最大高程", required = true, example = "990.87")
    private Float maxElevation;
    @ApiModelProperty(name = "count", value = "上传数据量", required = true, example = "2109")
    private int count;
    private String Remark;

}
  