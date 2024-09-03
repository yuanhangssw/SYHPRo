package com.tianji.dam.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(description = "原始轨迹数据VO辅助类")
@Data
public class T1VO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "beginTimestamp", value = "开始时间", example = "2021-07-20 15:30:30")
    private String beginTimestamp;
    private String Lbegintimestamp;
    @ApiModelProperty(name = "endTimestamp", value = "结束时间", example = "2021-07-22 15:30:30")
    private String endTimestamp;
    private String Lendtimestamp;
    @ApiModelProperty(name = "vehicleID", value = "车辆id，关联t_car的CarID", example = "1")
    private String vehicleID;
    @ApiModelProperty(name = "pageNum", value = "当前页", required = true, example = "1")
    private Integer pageNum;
    @ApiModelProperty(name = "pageSize", value = "每页数", required = true, example = "10")
    private Integer pageSize;
    @ApiModelProperty(name = "elevation", value = "高程", example = "970.376")
    private Double elevation;
    private double currentEvolution;
    private String startAltitude;
    private String endAltitude;
    //以下为筛选条件
    private Integer materialname;
    private Double gaocheng;
    private Double cenggao;
    private List<Integer> list;
    private String tablename;
    private String formname;

    private Double xbegin;
    private Double xend;
    private Double ybegin;
    private Double yend;

    private Integer sort;

    private Long timestamp;

    private Long beginTime;
    private Long endTime;

    private Double beginElevation;
    private Double endElevation;

    private Double beginzhuanghao;
    private Double endzhuanghao;

    private Double speed1;
    private Double speed2;

    private Integer layerid;

    public T1VO(Double xbegin, Double xend, Double ybegin, Double yend) {
        this.xbegin = xbegin;
        this.xend = xend;
        this.ybegin = ybegin;
        this.yend = yend;
    }

    //数据归档
    public T1VO(List<Integer> list, String tablename) {
        this.list = list;
        this.tablename = tablename;
    }

    //数据归档2.0
    public T1VO(List<Integer> list, String tablename, String formname) {
        this.list = list;
        this.tablename = tablename;
        this.formname = formname;
    }

    //数据归档查询
    public T1VO(Integer materialname, Double gaocheng, Double cenggao) {
        this.materialname = materialname;
        this.gaocheng = gaocheng;
        this.cenggao = cenggao;
    }

    //数据归档查询2.0
    public T1VO(Integer materialname, Double gaocheng, Double cenggao, Double xbegin, Double xend, Double ybegin, Double yend) {
        this.materialname = materialname;
        this.gaocheng = gaocheng;
        this.cenggao = cenggao;
        this.xbegin = xbegin;
        this.xend = xend;
        this.ybegin = ybegin;
        this.yend = yend;
    }

    //数据归档查询3.0
    public T1VO(String beginTimestamp, String endTimestamp, Double xbegin, Double xend, Double ybegin, Double yend) {
        this.beginTimestamp = beginTimestamp;
        this.endTimestamp = endTimestamp;
        this.xbegin = xbegin;
        this.xend = xend;
        this.ybegin = ybegin;
        this.yend = yend;
    }

    //数据归档查询4.0
    public T1VO(String beginTimestamp, String endTimestamp, Double xbegin, Double xend, Double ybegin, Double yend, String tablename) {
        this.beginTimestamp = beginTimestamp;
        this.endTimestamp = endTimestamp;
        this.xbegin = xbegin;
        this.xend = xend;
        this.ybegin = ybegin;
        this.yend = yend;
        this.tablename = tablename;
    }

    public T1VO() {
    }
}
