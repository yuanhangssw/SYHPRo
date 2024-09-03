package com.tianji.dam.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.tj.common.annotation.Excel;
import com.tj.common.core.domain.BaseEntity;

/**
 * 项目试验文件对象 t_project_upfile
 * 
 * @author liyan
 * @date 2022-06-14
 */
public class TProjectUpfile extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long gid;

    /** 所属项目 */
    @Excel(name = "所属项目")
    private Long projectid;

    /** 文件名称 */
    @Excel(name = "文件名称")
    private String filename;

    /** 选择文件 */
    @Excel(name = "选择文件")
    private String filepath;

    /** 标记 */
    @Excel(name = "标记")
    private String delflag;

    /**  */
    private String freedom1;

    /** 备注 */
    private String freedom2;

    /** 备注 */
    private String freedom3;

    /** 备注 */
    private String freedom4;

    /** 备注 */
    private String freedom5;

    public void setGid(Long gid) 
    {
        this.gid = gid;
    }

    public Long getGid() 
    {
        return gid;
    }
    public void setProjectid(Long projectid) 
    {
        this.projectid = projectid;
    }

    public Long getProjectid() 
    {
        return projectid;
    }
    public void setFilename(String filename) 
    {
        this.filename = filename;
    }

    public String getFilename() 
    {
        return filename;
    }
    public void setFilepath(String filepath) 
    {
        this.filepath = filepath;
    }

    public String getFilepath() 
    {
        return filepath;
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
            .append("gid", getGid())
            .append("projectid", getProjectid())
            .append("filename", getFilename())
            .append("filepath", getFilepath())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("delflag", getDelflag())
            .append("freedom1", getFreedom1())
            .append("freedom2", getFreedom2())
            .append("freedom3", getFreedom3())
            .append("freedom4", getFreedom4())
            .append("freedom5", getFreedom5())
            .toString();
    }
}
