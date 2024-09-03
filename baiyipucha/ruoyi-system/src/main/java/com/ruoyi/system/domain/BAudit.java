package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 审核记录对象 b_audit
 * 
 * @author ruoyi
 * @date 2024-03-05
 */
public class BAudit extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 巡查id */
    @Excel(name = "巡查id")
    private Long patrolId;

    /** 审核人 */
    @Excel(name = "审核人")
    private Long auditUser;

    /** 审核意见 */
    @Excel(name = "审核意见")
    private String auditOpinion;

    /** 审核状态（通过、未通过） */
    @Excel(name = "审核状态", readConverterExp = "通=过、未通过")
    private Long auditStatus;

    /** 当前审核部门 */
    @Excel(name = "当前审核部门")
    private Long currentDept;

    /** 下级审核部门 */
    @Excel(name = "下级审核部门")
    private Long subordinateDept;

    /** 数据状态(1进行中2流程中止3完成) */
    @Excel(name = "数据状态(1进行中2流程中止3完成)")
    private Long dataState;

    private String project_name;
    private String unit_name;
    private String uptime;
    private String inspector_name;
    private String phone;
    private  String patrol_type;

    public String getPatrol_type() {
        return patrol_type;
    }

    public void setPatrol_type(String patrol_type) {
        this.patrol_type = patrol_type;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public String getInspector_name() {
        return inspector_name;
    }

    public void setInspector_name(String inspector_name) {
        this.inspector_name = inspector_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

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
    public void setPatrolId(Long patrolId) 
    {
        this.patrolId = patrolId;
    }

    public Long getPatrolId() 
    {
        return patrolId;
    }
    public void setAuditUser(Long auditUser) 
    {
        this.auditUser = auditUser;
    }

    public Long getAuditUser() 
    {
        return auditUser;
    }
    public void setAuditOpinion(String auditOpinion) 
    {
        this.auditOpinion = auditOpinion;
    }

    public String getAuditOpinion() 
    {
        return auditOpinion;
    }
    public void setAuditStatus(Long auditStatus) 
    {
        this.auditStatus = auditStatus;
    }

    public Long getAuditStatus() 
    {
        return auditStatus;
    }
    public void setCurrentDept(Long currentDept) 
    {
        this.currentDept = currentDept;
    }

    public Long getCurrentDept() 
    {
        return currentDept;
    }
    public void setSubordinateDept(Long subordinateDept) 
    {
        this.subordinateDept = subordinateDept;
    }

    public Long getSubordinateDept() 
    {
        return subordinateDept;
    }
    public void setDataState(Long dataState) 
    {
        this.dataState = dataState;
    }

    public Long getDataState() 
    {
        return dataState;
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
            .append("patrolId", getPatrolId())
            .append("auditUser", getAuditUser())
            .append("auditOpinion", getAuditOpinion())
            .append("auditStatus", getAuditStatus())
            .append("currentDept", getCurrentDept())
            .append("subordinateDept", getSubordinateDept())
            .append("dataState", getDataState())
            .append("freedom1", getFreedom1())
            .append("freedom2", getFreedom2())
            .append("freedom3", getFreedom3())
            .append("freedom4", getFreedom4())
            .append("freedom5", getFreedom5())
            .toString();
    }
}
