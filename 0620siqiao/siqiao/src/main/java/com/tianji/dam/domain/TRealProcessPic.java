package com.tianji.dam.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.tj.common.annotation.Excel;
import com.tj.common.core.domain.BaseEntity;

/**
 * 实景图管理对象 t_real_process_pic
 * 
 * @author liyan
 * @date 2023-12-28
 */
public class TRealProcessPic extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 航拍日期 */
    @Excel(name = "航拍日期")
    private String times;

    /** 图片地址 */
    @Excel(name = "图片地址")
    private String srcs;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String freedom1;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String freedom2;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setTimes(String times) 
    {
        this.times = times;
    }

    public String getTimes() 
    {
        return times;
    }
    public void setSrcs(String srcs) 
    {
        this.srcs = srcs;
    }

    public String getSrcs() 
    {
        return srcs;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("times", getTimes())
            .append("srcs", getSrcs())
            .append("createTime", getCreateTime())
            .append("freedom1", getFreedom1())
            .append("freedom2", getFreedom2())
            .toString();
    }
}
