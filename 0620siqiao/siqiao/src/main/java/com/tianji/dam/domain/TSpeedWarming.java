package com.tianji.dam.domain;

import com.tj.common.annotation.Excel;
import com.tj.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 预警信息对象 t_speed_warming
 *
 * @author tianji_liyan
 * @date 2021-12-28
 */
@Data
public class TSpeedWarming extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String gid;

    /**
     * 仓位ID
     */
    private Long damgid;
    private String ceng;

    /**
     * 车辆ID
     */
    @Excel(name = "车辆ID")
    private Long carid;

    /**
     * 仓位名称
     */
    @Excel(name = "仓位名称")
    private String damtitle;

    /**
     * 车辆名称
     */
    @Excel(name = "车辆名称")
    private String carname;

    /**
     * 限制速度
     */
    @Excel(name = "限制速度")
    private BigDecimal normalvalue;

    /**
     * 实际速度
     */
    @Excel(name = "实际速度")
    private BigDecimal currentvalue;

    /**
     * 入库时间
     */
    private String creattime;

    /**
     * 超速时间
     */
    @Excel(name = "超速时间")
    private String datatime;
    private String begintime;
    private String endtime;


    /**
     * 备用
     */
    private String freedom1;

    /**
     * 备用
     */
    private String freedom2;

    /**
     * 备用
     */
    private String freedom3;

    /**
     * 备用
     */
    private String freedom4;

    /**
     * 备用
     */
    private String freedom5;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("gid", getGid())
                .append("damgid", getDamgid())
                .append("carid", getCarid())
                .append("damtitle", getDamtitle())
                .append("carname", getCarname())
                .append("normalvalue", getNormalvalue())
                .append("currentvalue", getCurrentvalue())
                .append("creattime", getCreattime())
                .append("datatime", getDatatime())
                .append("freedom1", getFreedom1())
                .append("freedom2", getFreedom2())
                .append("freedom3", getFreedom3())
                .append("freedom4", getFreedom4())
                .append("freedom5", getFreedom5())
                .toString();
    }
}
