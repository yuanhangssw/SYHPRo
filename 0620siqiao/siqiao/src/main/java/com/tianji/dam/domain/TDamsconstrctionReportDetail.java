package com.tianji.dam.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tj.common.annotation.Excel;
import com.tj.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 试验报告详情对象 t_damsconstrction_report_detail
 *
 * @author liyan
 * @date 2022-06-10
 */
public class TDamsconstrctionReportDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private Long gid;

    /**
     * 所属报告
     */
    @Excel(name = "所属报告")
    private Long reportGid;

    /**
     * 样品编号
     */
    @Excel(name = "样品编号")
    private String sno;
    private String ranges;

    /**
     * 检测时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "检测时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date checkTime;

    /**
     * 湿密度
     */
    @Excel(name = "湿密度")
    private String param1;

    /**
     * 干密度
     */
    @Excel(name = "干密度")
    private String param2;

    /**
     * 含水率
     */
    @Excel(name = "含水率")
    private String param3;

    /**
     * 压实度
     */
    @Excel(name = "压实度")
    private String param4;

    /**
     * 属性5
     */
    @Excel(name = "属性5")
    private String param5;

    /**
     * 删除标记
     */
    @Excel(name = "删除标记")
    private String delflag;

    public String getRanges() {
        return ranges;
    }

    public void setRanges(String ranges) {
        this.ranges = ranges;
    }

    public void setGid(Long gid) {
        this.gid = gid;
    }

    public Long getGid() {
        return gid;
    }

    public void setReportGid(Long reportGid) {
        this.reportGid = reportGid;
    }

    public Long getReportGid() {
        return reportGid;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getSno() {
        return sno;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam3(String param3) {
        this.param3 = param3;
    }

    public String getParam3() {
        return param3;
    }

    public void setParam4(String param4) {
        this.param4 = param4;
    }

    public String getParam4() {
        return param4;
    }

    public void setParam5(String param5) {
        this.param5 = param5;
    }

    public String getParam5() {
        return param5;
    }

    public void setDelflag(String delflag) {
        this.delflag = delflag;
    }

    public String getDelflag() {
        return delflag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("gid", getGid())
                .append("reportGid", getReportGid())
                .append("sno", getSno())
                .append("checkTime", getCheckTime())
                .append("param1", getParam1())
                .append("param2", getParam2())
                .append("param3", getParam3())
                .append("param4", getParam4())
                .append("param5", getParam5())
                .append("delflag", getDelflag())
                .toString();
    }
}
