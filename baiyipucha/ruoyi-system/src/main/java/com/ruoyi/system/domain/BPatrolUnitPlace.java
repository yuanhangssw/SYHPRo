package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 普查处管理对象 b_patrol_unit_place
 * 
 * @author ruoyi
 * @date 2024-04-09
 */
public class BPatrolUnitPlace extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 普查单元 */
    @Excel(name = "普查单元")
    private Long patrolUnit;

    /** 巡查表多个巡查逗号隔开 */
    @Excel(name = "巡查表多个巡查逗号隔开")
    private String patrolId;

    /** 危害类别 */
    @Excel(name = "危害类别")
    private Long detrimentType;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String adress;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String lat;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String lon;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String inspector;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long projectId;


    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setPatrolUnit(Long patrolUnit) 
    {
        this.patrolUnit = patrolUnit;
    }

    public Long getPatrolUnit() 
    {
        return patrolUnit;
    }
    public void setPatrolId(String patrolId) 
    {
        this.patrolId = patrolId;
    }

    public String getPatrolId() 
    {
        return patrolId;
    }
    public void setDetrimentType(Long detrimentType) 
    {
        this.detrimentType = detrimentType;
    }

    public Long getDetrimentType() 
    {
        return detrimentType;
    }
    public void setAdress(String adress) 
    {
        this.adress = adress;
    }

    public String getAdress() 
    {
        return adress;
    }
    public void setLat(String lat) 
    {
        this.lat = lat;
    }

    public String getLat() 
    {
        return lat;
    }
    public void setLon(String lon) 
    {
        this.lon = lon;
    }

    public String getLon() 
    {
        return lon;
    }
    public void setInspector(String inspector) 
    {
        this.inspector = inspector;
    }

    public String getInspector() 
    {
        return inspector;
    }
    public void setProjectId(Long projectId) 
    {
        this.projectId = projectId;
    }

    public Long getProjectId() 
    {
        return projectId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("patrolUnit", getPatrolUnit())
            .append("patrolId", getPatrolId())
            .append("detrimentType", getDetrimentType())
            .append("adress", getAdress())
            .append("lat", getLat())
            .append("lon", getLon())
            .append("inspector", getInspector())
            .append("projectId", getProjectId())
            .toString();
    }
}
