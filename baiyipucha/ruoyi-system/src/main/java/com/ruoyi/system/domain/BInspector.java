package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 巡查员用户对象 b_inspector
 * 
 * @author ruoyi
 * @date 2024-03-05
 */
public class BInspector extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 巡查用户姓名 */
    @Excel(name = "巡查用户姓名")
    private String inspectorName;

    /** 手机号 */
    @Excel(name = "手机号")
    private String phone;

    /** 密码 */
    @Excel(name = "密码")
    private String password;

    /** 部门用户创建者 */
    @Excel(name = "部门用户创建者")
    private Long userId;

    /** 部门创建的用户 */
    @Excel(name = "部门创建的用户")
    private Long deptId;

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

    private String deptName;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setInspectorName(String inspectorName) 
    {
        this.inspectorName = inspectorName;
    }

    public String getInspectorName() 
    {
        return inspectorName;
    }
    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }
    public void setPassword(String password) 
    {
        this.password = password;
    }

    public String getPassword() 
    {
        return password;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setDeptId(Long deptId) 
    {
        this.deptId = deptId;
    }

    public Long getDeptId() 
    {
        return deptId;
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

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("inspectorName", getInspectorName())
            .append("phone", getPhone())
            .append("password", getPassword())
            .append("userId", getUserId())
            .append("deptId", getDeptId())
            .append("delFlag", getDelFlag())
            .append("freedom1", getFreedom1())
            .append("freedom2", getFreedom2())
            .append("freedom3", getFreedom3())
            .append("freedom4", getFreedom4())
            .append("freedom5", getFreedom5())
            .toString();
    }
}
