package com.tianji.dam.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tj.common.annotation.Excel;
import com.tj.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 日施工数据对象 t_day_construction_info
 * 
 * @author liyan
 * @date 2023-11-30
 */
public class DayConstructionInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long gid;

    /** 分区 */
    @Excel(name = "分区")
    private Long areaid;

    /** 日期 */
    @Excel(name = "日期")
    private String day;

    /** 开始面积 */
    @Excel(name = "开始面积")
    private BigDecimal basearea;

    /** 开始方量 */
    @Excel(name = "开始方量")
    private BigDecimal basequantity;

    /** 开始高程 */
    @Excel(name = "开始高程")
    private BigDecimal baseevolution;

    /** 当日面积 */
    @Excel(name = "当日面积")
    private BigDecimal todayarea;

    /** 当日方量 */
    @Excel(name = "当日方量")
    private BigDecimal todayquantity;

    /** 当日最大高程 */
    @Excel(name = "当日最大高程")
    private BigDecimal todayevolution;

    /** 备用 */
    @Excel(name = "备用")
    private String freedom1;

    /** 备用 */
    @Excel(name = "备用")
    private String freedom2;

    /** 备用 */
    @Excel(name = "备用")
    private String freedom3;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date    createtime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date    updatetime;

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public void setGid(Long gid)
    {
        this.gid = gid;
    }

    public Long getGid() 
    {
        return gid;
    }
    public void setAreaid(Long areaid) 
    {
        this.areaid = areaid;
    }

    public Long getAreaid() 
    {
        return areaid;
    }
    public void setDay(String day) 
    {
        this.day = day;
    }

    public String getDay() 
    {
        return day;
    }
    public void setBasearea(BigDecimal basearea) 
    {
        this.basearea = basearea;
    }

    public BigDecimal getBasearea() 
    {
        return basearea;
    }
    public void setBasequantity(BigDecimal basequantity) 
    {
        this.basequantity = basequantity;
    }

    public BigDecimal getBasequantity() 
    {
        return basequantity;
    }
    public void setBaseevolution(BigDecimal baseevolution) 
    {
        this.baseevolution = baseevolution;
    }

    public BigDecimal getBaseevolution() 
    {
        return baseevolution;
    }
    public void setTodayarea(BigDecimal todayarea) 
    {
        this.todayarea = todayarea;
    }

    public BigDecimal getTodayarea() 
    {
        return todayarea;
    }
    public void setTodayquantity(BigDecimal todayquantity) 
    {
        this.todayquantity = todayquantity;
    }

    public BigDecimal getTodayquantity() 
    {
        return todayquantity;
    }
    public void setTodayevolution(BigDecimal todayevolution) 
    {
        this.todayevolution = todayevolution;
    }

    public BigDecimal getTodayevolution() 
    {
        return todayevolution;
    }
    public void setFreedom1(String freedom1) 
    {
        this.freedom1 = freedom1;
    }

    public String getFreedom1() 
    {
        return freedom1;
    }
    public void setFreedom2(String freedom2) 
    {
        this.freedom2 = freedom2;
    }

    public String getFreedom2() 
    {
        return freedom2;
    }
    public void setFreedom3(String freedom3) 
    {
        this.freedom3 = freedom3;
    }

    public String getFreedom3() 
    {
        return freedom3;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("gid", getGid())
            .append("areaid", getAreaid())
            .append("day", getDay())
            .append("basearea", getBasearea())
            .append("basequantity", getBasequantity())
            .append("baseevolution", getBaseevolution())
            .append("todayarea", getTodayarea())
            .append("todayquantity", getTodayquantity())
            .append("todayevolution", getTodayevolution())
            .append("createtime", getCreatetime())
            .append("updatetime", getUpdatetime())
            .append("freedom1", getFreedom1())
            .append("freedom2", getFreedom2())
            .append("freedom3", getFreedom3())
            .toString();
    }
}
