package com.tianji.dam.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.tj.common.annotation.Excel;
import com.tj.common.core.domain.BaseEntity;

/**
 * 层位分析对象 t_history_pic_ceng
 * 
 * @author liyan
 * @date 2024-04-01
 */
public class HistoryPicCeng extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 区域 */
    @Excel(name = "区域")
    private Long pid;

    /** 层 */
    @Excel(name = "层")
    private Long ceng;

    /** 类型 */
    @Excel(name = "类型")
    private Long htype;

    /** 生成数据 */
    @Excel(name = "生成数据")
    private String content;

    /** 备用 */
    @Excel(name = "备用")
    private String freedom1;

    /** 备用 */
    @Excel(name = "备用")
    private String freedom2;

    /** 备用 */
    @Excel(name = "备用")
    private String freedom3;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setPid(Long pid) 
    {
        this.pid = pid;
    }

    public Long getPid() 
    {
        return pid;
    }
    public void setCeng(Long ceng) 
    {
        this.ceng = ceng;
    }

    public Long getCeng() 
    {
        return ceng;
    }
    public void setHtype(Long htype) 
    {
        this.htype = htype;
    }

    public Long getHtype() 
    {
        return htype;
    }
    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
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
            .append("id", getId())
            .append("pid", getPid())
            .append("ceng", getCeng())
            .append("htype", getHtype())
            .append("content", getContent())
            .append("createTime", getCreateTime())
            .append("freedom1", getFreedom1())
            .append("freedom2", getFreedom2())
            .append("freedom3", getFreedom3())
            .toString();
    }
}
