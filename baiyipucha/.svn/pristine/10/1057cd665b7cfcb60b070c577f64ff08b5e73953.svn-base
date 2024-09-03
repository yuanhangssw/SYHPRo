package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 巡查用户和巡查单元关联对象 b_inspector_unit
 * 
 * @author ruoyi
 * @date 2024-03-05
 */
public class BInspectorUnit extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 巡查用户 */
    @Excel(name = "巡查用户")
    private Long inspector;

    /** 项目id */
    @Excel(name = "项目id")
    private Long projectId;

    /** 巡查单元 */
    @Excel(name = "巡查单元")
    private Long patrolUnitId;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setInspector(Long inspector) 
    {
        this.inspector = inspector;
    }

    public Long getInspector() 
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
    public void setPatrolUnitId(Long patrolUnitId) 
    {
        this.patrolUnitId = patrolUnitId;
    }

    public Long getPatrolUnitId() 
    {
        return patrolUnitId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("inspector", getInspector())
            .append("projectId", getProjectId())
            .append("patrolUnitId", getPatrolUnitId())
            .toString();
    }
}
