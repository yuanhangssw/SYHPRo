package com.tianji.dam.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * t_repair_data
 *
 * @author
 */
@Data
public class TRepairData implements Serializable {
    private Integer id;

    /**
     * 仓位ID
     */
    private Integer damsid;

    /**
     * 区域边界
     */
    private String ranges;

    /**
     * 区域名称
     */
    private String title;

    /**
     * 碾压遍数，关联颜色表
     */
    private Integer colorId;

    /**
     * 碾压速度
     */
    private Double speed;

    /**
     * 碾压振动值
     */
    private Double vibration;

    /**
     * 开工时间
     */
    private String startTime;

    /**
     * 竣工时间
     */
    private String endTime;

    /**
     * 补录时间
     */
    private String repairTime;

    /**
     * 材料ID
     */
    private String materialname;

    /**
     * X起始坐标
     */
    private Double xbegin;

    /**
     * X结束坐标
     */
    private Double xend;

    /**
     * Y起始坐标
     */
    private Double ybegin;

    /**
     * Y结束坐标
     */
    private Double yend;

    private int repairtype;

    private Integer carid;
    private Integer cartype;

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "pageNum", value = "当前页", required = true, example = "1")
    private Integer pageNum;
    @ApiModelProperty(name = "pageSize", value = "每页数", required = true, example = "10")
    private Integer pageSize;
    @ApiModelProperty(name = "materialValue", value = "材料名称", example = "过渡料")

    private String materialValue;
}