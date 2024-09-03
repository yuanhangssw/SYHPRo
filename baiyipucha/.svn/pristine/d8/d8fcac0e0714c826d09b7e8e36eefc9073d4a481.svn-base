package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 巡查类型对象 b_patrol_type
 * 
 * @author ruoyi
 * @date 2024-03-05
 */
public class BPatrolType extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 巡查类型1白蚁2动物 */
    @Excel(name = "巡查类型1白蚁2动物")
    private Long patrolType;

    /** 巡查类型名称 */
    @Excel(name = "巡查类型名称")
    private String typeName;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String freedom1;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String freedom2;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String freedom3;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String freedom4;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String freedom5;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setPatrolType(Long patrolType) 
    {
        this.patrolType = patrolType;
    }

    public Long getPatrolType() 
    {
        return patrolType;
    }
    public void setTypeName(String typeName) 
    {
        this.typeName = typeName;
    }

    public String getTypeName() 
    {
        return typeName;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
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
    public void setFreedom4(String freedom4) 
    {
        this.freedom4 = freedom4;
    }

    public String getFreedom4() 
    {
        return freedom4;
    }
    public void setFreedom5(String freedom5) 
    {
        this.freedom5 = freedom5;
    }

    public String getFreedom5() 
    {
        return freedom5;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("patrolType", getPatrolType())
            .append("typeName", getTypeName())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("freedom1", getFreedom1())
            .append("freedom2", getFreedom2())
            .append("freedom3", getFreedom3())
            .append("freedom4", getFreedom4())
            .append("freedom5", getFreedom5())
            .toString();
    }
}
