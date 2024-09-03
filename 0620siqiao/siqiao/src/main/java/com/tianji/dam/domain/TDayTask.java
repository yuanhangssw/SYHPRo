package com.tianji.dam.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tj.common.annotation.Excel;
import com.tj.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 作业任务对象 t_day_task
 *
 * @author liyan
 * @date 2022-10-31
 */
public class TDayTask extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 任务名称
     */
    @Excel(name = "任务名称")
    private String title;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    private Double baseEvolution;

    /**
     * 摊铺状态
     * 当有车辆加入时，更新为摊铺中、
     */
    @Excel(name = "摊铺状态")
    private String tpStatus;

    /**
     * 碾压状态
     * 碾压默认为任务进行中，
     * 只有验收人员可以结束任务，所有未结束的任务，摊铺机碾压机 都可以再次加入。
     * Y进行中 N结束，默认Y
     */
    private String nyStatus;

    /**
     * 碾压范围
     */
    @Excel(name = "碾压范围")
    private String ranges;

    /**
     * x坐标起始
     */
    private String xbegin;

    /**
     * x坐标结束
     */
    private String xend;

    /**
     * Y坐标起始
     */
    private String ybegin;

    /**
     * Y坐标结束
     */
    private String yend;

    /**
     * 验收人
     */
    @Excel(name = "验收人")
    private String checkBy;

    /**
     * 备用
     */
    private String freedom1;

    /**
     * 备用
     */
    private String freedom2;

    /**
     * 备用
     */
    private String freedom3;

    /**
     * 备用
     */
    private String freedom4;

    /**
     * 备用
     */
    private String freedom5;

    public Double getBaseEvolution() {
        return baseEvolution;
    }

    public void setBaseEvolution(Double baseEvolution) {
        this.baseEvolution = baseEvolution;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setTpStatus(String tpStatus) {
        this.tpStatus = tpStatus;
    }

    public String getTpStatus() {
        return tpStatus;
    }

    public void setNyStatus(String nyStatus) {
        this.nyStatus = nyStatus;
    }

    public String getNyStatus() {
        return nyStatus;
    }

    public void setRanges(String ranges) {
        this.ranges = ranges;
    }

    public String getRanges() {
        return ranges;
    }

    public void setXbegin(String xbegin) {
        this.xbegin = xbegin;
    }

    public String getXbegin() {
        return xbegin;
    }

    public void setXend(String xend) {
        this.xend = xend;
    }

    public String getXend() {
        return xend;
    }

    public void setYbegin(String ybegin) {
        this.ybegin = ybegin;
    }

    public String getYbegin() {
        return ybegin;
    }

    public void setYend(String yend) {
        this.yend = yend;
    }

    public String getYend() {
        return yend;
    }

    public void setCheckBy(String checkBy) {
        this.checkBy = checkBy;
    }

    public String getCheckBy() {
        return checkBy;
    }

    public void setFreedom1(String freedom1) {
        this.freedom1 = freedom1;
    }

    public String getFreedom1() {
        return freedom1;
    }

    public void setFreedom2(String freedom2) {
        this.freedom2 = freedom2;
    }

    public String getFreedom2() {
        return freedom2;
    }

    public void setFreedom3(String freedom3) {
        this.freedom3 = freedom3;
    }

    public String getFreedom3() {
        return freedom3;
    }

    public void setFreedom4(String freedom4) {
        this.freedom4 = freedom4;
    }

    public String getFreedom4() {
        return freedom4;
    }

    public void setFreedom5(String freedom5) {
        this.freedom5 = freedom5;
    }

    public String getFreedom5() {
        return freedom5;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("title", getTitle())
                .append("beginTime", getBeginTime())
                .append("endTime", getEndTime())
                .append("createTime", getCreateTime())
                .append("createBy", getCreateBy())
                .append("tpStatus", getTpStatus())
                .append("nyStatus", getNyStatus())
                .append("ranges", getRanges())
                .append("xbegin", getXbegin())
                .append("xend", getXend())
                .append("ybegin", getYbegin())
                .append("yend", getYend())
                .append("checkBy", getCheckBy())
                .append("freedom1", getFreedom1())
                .append("freedom2", getFreedom2())
                .append("freedom3", getFreedom3())
                .append("freedom4", getFreedom4())
                .append("freedom5", getFreedom5())
                .toString();
    }
}
