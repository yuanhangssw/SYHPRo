package com.tianji.dam.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.tj.common.annotation.Excel;
import com.tj.common.core.domain.BaseEntity;

/**
 * shuju对象 cang_allitems
 * 
 * @author ruoyi
 * @date 2023-12-05
 */
public class CangAllitems extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 平面坐标x */
    @Excel(name = "平面坐标x")
    private Long px;

    /** 平面坐标y */
    @Excel(name = "平面坐标y")
    private Long py;

    /** 高程坐标z */
    @Excel(name = "高程坐标z")
    private Long pz;

    /** 车辆id */
    @Excel(name = "车辆id")
    private Long carid;

    /** 速度（扩大100倍至整数） */
    @Excel(name = "速度", readConverterExp = "扩=大100倍至整数")
    private Long speed;

    /** vcv（扩大100倍至整数） */
    @Excel(name = "vcv", readConverterExp = "扩=大100倍至整数")
    private Long vcv;

    /** 时间戳 */
    @Excel(name = "时间戳")
    private Long times;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setPx(Long px) 
    {
        this.px = px;
    }

    public Long getPx() 
    {
        return px;
    }
    public void setPy(Long py) 
    {
        this.py = py;
    }

    public Long getPy() 
    {
        return py;
    }
    public void setPz(Long pz) 
    {
        this.pz = pz;
    }

    public Long getPz() 
    {
        return pz;
    }
    public void setCarid(Long carid) 
    {
        this.carid = carid;
    }

    public Long getCarid() 
    {
        return carid;
    }
    public void setSpeed(Long speed) 
    {
        this.speed = speed;
    }

    public Long getSpeed() 
    {
        return speed;
    }
    public void setVcv(Long vcv) 
    {
        this.vcv = vcv;
    }

    public Long getVcv() 
    {
        return vcv;
    }
    public void setTimes(Long times) 
    {
        this.times = times;
    }

    public Long getTimes() 
    {
        return times;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("px", getPx())
            .append("py", getPy())
            .append("pz", getPz())
            .append("carid", getCarid())
            .append("speed", getSpeed())
            .append("vcv", getVcv())
            .append("times", getTimes())
            .toString();
    }
}
