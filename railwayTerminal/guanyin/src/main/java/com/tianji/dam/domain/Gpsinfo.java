package com.tianji.dam.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.tj.common.annotation.Excel;
import com.tj.common.core.domain.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 人员定位信息对象 gpsinfo
 * 
 * @author ruoyi
 * @date 2023-10-19
 */
public class Gpsinfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 头 */
    @Excel(name = "头")
    private String head;

    /** 消息ID */
    @Excel(name = "消息ID")
    private String messageid;

    /** 属性 */
    @Excel(name = "属性")
    private String attribute;

    /** ID号 */
    @Excel(name = "ID号")
    private String idcard;

    /** 流水号 */
    @Excel(name = "流水号")
    private String waternumber;

    /** 报警标志 */
    @Excel(name = "报警标志")
    private String warning;

    /** 状态位 */
    @Excel(name = "状态位")
    private String status;

    /** 纬度 */
    @Excel(name = "纬度")
    private String latitude;

    /** 经度 */
    @Excel(name = "经度")
    private String longitude;

    /** 高程 */
    @Excel(name = "高程")
    private String gaocheng;

    /** 速度 */
    @Excel(name = "速度")
    private String speed;

    /** 方向角 */
    @Excel(name = "方向角")
    private String directionangle;

    /** 时间 */
    @Excel(name = "时间")
    @DateTimeFormat
    private String time;

    /** 电量 */
    @Excel(name = "电量")
    private String electricity;

    /** 长度 */
    @Excel(name = "长度")
    private String length;

    /** 校验位 */
    @Excel(name = "校验位")
    private String check;

    /** 包尾 */
    @Excel(name = "包尾")
    private String end;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setHead(String head) 
    {
        this.head = head;
    }

    public String getHead() 
    {
        return head;
    }
    public void setMessageid(String messageid) 
    {
        this.messageid = messageid;
    }

    public String getMessageid() 
    {
        return messageid;
    }
    public void setAttribute(String attribute) 
    {
        this.attribute = attribute;
    }

    public String getAttribute() 
    {
        return attribute;
    }
    public void setIdcard(String idcard) 
    {
        this.idcard = idcard;
    }

    public String getIdcard() 
    {
        return idcard;
    }
    public void setWaternumber(String waternumber) 
    {
        this.waternumber = waternumber;
    }

    public String getWaternumber() 
    {
        return waternumber;
    }
    public void setWarning(String warning) 
    {
        this.warning = warning;
    }

    public String getWarning() 
    {
        return warning;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setLatitude(String latitude) 
    {
        this.latitude = latitude;
    }

    public String getLatitude() 
    {
        return latitude;
    }
    public void setLongitude(String longitude) 
    {
        this.longitude = longitude;
    }

    public String getLongitude() 
    {
        return longitude;
    }
    public void setGaocheng(String gaocheng) 
    {
        this.gaocheng = gaocheng;
    }

    public String getGaocheng() 
    {
        return gaocheng;
    }
    public void setSpeed(String speed) 
    {
        this.speed = speed;
    }

    public String getSpeed() 
    {
        return speed;
    }
    public void setDirectionangle(String directionangle) 
    {
        this.directionangle = directionangle;
    }

    public String getDirectionangle() 
    {
        return directionangle;
    }
    public void setTime(String time) 
    {
        this.time = time;
    }

    public String getTime() 
    {
        return time;
    }
    public void setElectricity(String electricity) 
    {
        this.electricity = electricity;
    }

    public String getElectricity() 
    {
        return electricity;
    }
    public void setLength(String length) 
    {
        this.length = length;
    }

    public String getLength() 
    {
        return length;
    }
    public void setCheck(String check) 
    {
        this.check = check;
    }

    public String getCheck() 
    {
        return check;
    }
    public void setEnd(String end) 
    {
        this.end = end;
    }

    public String getEnd() 
    {
        return end;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("idcard", getIdcard())
            .append("status", getStatus())
            .append("latitude", getLatitude())
            .append("longitude", getLongitude())
            .append("gaocheng", getGaocheng())
            .append("speed", getSpeed())
            .append("directionangle", getDirectionangle())
            .append("time", getTime())
            .append("electricity", getElectricity())
            .toString();
    }
}
