package com.tianji.dam.domain;

import com.tj.common.annotation.Excel;
import com.tj.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 层高程对象 t_encode_evolution
 * 
 * @author ruoyi
 * @date 2023-11-01
 */
public class TEncodeEvolution extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long gid;

    /** 分部工程 */
    @Excel(name = "分部工程")
    private Long areagid;

    /** 仓位 */
    @Excel(name = "仓位")
    private Long damgid;

    /** 层 */
    @Excel(name = "层")
    private Long encode;

    /** 平均高程 */
    @Excel(name = "平均高程")
    private BigDecimal evolution;

    /** 设计高程 */
    @Excel(name = "设计高程")
    private BigDecimal normalEvolution;

    /** 最小高程 */
    @Excel(name = "最小高程")
    private BigDecimal minEvolution;

    /** 最大高程 */
    @Excel(name = "最大高程")
    private BigDecimal maxEvolution;

    /** Y删除N未删除 */
    @Excel(name = "Y删除N未删除")
    private String delflag;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String freedom1;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String freedom2;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String freedom3;

    public void setGid(Long gid) 
    {
        this.gid = gid;
    }

    public Long getGid() 
    {
        return gid;
    }
    public void setAreagid(Long areagid) 
    {
        this.areagid = areagid;
    }

    public Long getAreagid() 
    {
        return areagid;
    }
    public void setDamgid(Long damgid) 
    {
        this.damgid = damgid;
    }

    public Long getDamgid() 
    {
        return damgid;
    }
    public void setEncode(Long encode) 
    {
        this.encode = encode;
    }

    public Long getEncode() 
    {
        return encode;
    }
    public void setEvolution(BigDecimal evolution) 
    {
        this.evolution = evolution;
    }

    public BigDecimal getEvolution() 
    {
        return evolution;
    }
    public void setNormalEvolution(BigDecimal normalEvolution) 
    {
        this.normalEvolution = normalEvolution;
    }

    public BigDecimal getNormalEvolution() 
    {
        return normalEvolution;
    }
    public void setMinEvolution(BigDecimal minEvolution) 
    {
        this.minEvolution = minEvolution;
    }

    public BigDecimal getMinEvolution() 
    {
        return minEvolution;
    }
    public void setMaxEvolution(BigDecimal maxEvolution) 
    {
        this.maxEvolution = maxEvolution;
    }

    public BigDecimal getMaxEvolution() 
    {
        return maxEvolution;
    }
    public void setDelflag(String delflag) 
    {
        this.delflag = delflag;
    }

    public String getDelflag() 
    {
        return delflag;
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
            .append("areagid", getAreagid())
            .append("damgid", getDamgid())
            .append("encode", getEncode())
            .append("evolution", getEvolution())
            .append("normalEvolution", getNormalEvolution())
            .append("minEvolution", getMinEvolution())
            .append("maxEvolution", getMaxEvolution())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("delflag", getDelflag())
            .append("freedom1", getFreedom1())
            .append("freedom2", getFreedom2())
            .append("freedom3", getFreedom3())
            .toString();
    }
}
