package com.tianji.dam.domain;

import com.tj.common.annotation.Excel;
import com.tj.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 站点预警用户对象 t_site_warning_user
 * 
 * @author tj
 * @date 2022-01-05
 */
public class TSiteWarningUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String userGid;


    private long referenceid;

    /** 姓名 */
    @Excel(name = "姓名")
    private String name;

    /** 电话 */
    @Excel(name = "电话")
    private String tel;

    /** 状态 */
    @Excel(name = "状态")
    private Long status;

    /** $column.columnComment */
    @Excel(name = "备注1")
    private String freedom1;

    /** $column.columnComment */
    @Excel(name = "备注2")
    private String freedom2;

    /** $column.columnComment */
    @Excel(name = "备注3")
    private String freedom3;

    /** $column.columnComment */
    @Excel(name = "备注4")
    private String freedom4;

    /** $column.columnComment */
    @Excel(name = "备注5")
    private String freedom5;



    public void setUserGid(String userGid) 
    {
        this.userGid = userGid;
    }

    public String getUserGid() 
    {
        return userGid;
    }
    public void setReferenceid(long referenceid)
    {
        this.referenceid = referenceid;
    }

    public long getReferenceid()
    {
        return referenceid;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    @NotBlank(message = "姓名不能为空")
    @Size(min = 0, max = 30, message = "姓名长度不能超过30个字符")
    public String getName() 
    {
        return name;
    }

    public void setTel(String tel) 
    {
        this.tel = tel;
    }

    @NotBlank(message = "电话不能为空")
    @Size(min = 0, max = 11, message = "请输入有效的电话号码")
    public String getTel() 
    {
        return tel;
    }
    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
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
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("userGid", getUserGid())
                .append("referenceid", getReferenceid())
                .append("name", getName())
                .append("tel", getTel())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("freedom1", getFreedom1())
                .append("freedom2", getFreedom2())
                .append("freedom3", getFreedom3())
                .append("freedom4", getFreedom4())
                .append("freedom5", getFreedom5())
                .toString();
    }
}
