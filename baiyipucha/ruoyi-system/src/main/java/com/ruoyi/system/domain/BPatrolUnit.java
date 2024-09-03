package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 巡查单元对象 b_patrol_unit
 * 
 * @author
 * @date 2024-03-05
 */
public class BPatrolUnit extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 项目id */
    @Excel(name = "项目id")
    private Long projectId;

    /** 单元名称 */
    @Excel(name = "单元名称")
    private String unitName;

    /** 项目类型1水库2堤防 */
    @Excel(name = "项目类型1水库2堤防")
    private Long projectType;

    /** 坝型 */
    @Excel(name = "坝型")
    private Long damType;

    /** 坝长 */
    @Excel(name = "坝长")
    private Long damLength;

    /** 坝高 */
    @Excel(name = "坝高")
    private Long damHigth;

    /** 护坡方式 */
    @Excel(name = "护坡方式")
    private Long slopeProtection;

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

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setProjectId(Long projectId)
    {
        this.projectId = projectId;
    }

    public Long getProjectId()
    {
        return projectId;
    }
    public void setUnitName(String unitName)
    {
        this.unitName = unitName;
    }

    public String getUnitName()
    {
        return unitName;
    }
    public void setProjectType(Long projectType)
    {
        this.projectType = projectType;
    }

    public Long getProjectType()
    {
        return projectType;
    }
    public void setDamType(Long damType)
    {
        this.damType = damType;
    }

    public Long getDamType()
    {
        return damType;
    }
    public void setDamLength(Long damLength)
    {
        this.damLength = damLength;
    }

    public Long getDamLength()
    {
        return damLength;
    }
    public void setDamHigth(Long damHigth)
    {
        this.damHigth = damHigth;
    }

    public Long getDamHigth()
    {
        return damHigth;
    }
    public void setSlopeProtection(Long slopeProtection)
    {
        this.slopeProtection = slopeProtection;
    }

    public Long getSlopeProtection()
    {
        return slopeProtection;
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
                .append("projectId", getProjectId())
                .append("unitName", getUnitName())
                .append("projectType", getProjectType())
                .append("damType", getDamType())
                .append("damLength", getDamLength())
                .append("damHigth", getDamHigth())
                .append("slopeProtection", getSlopeProtection())
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
