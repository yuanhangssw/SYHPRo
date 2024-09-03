package com.tianji.dam.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tj.common.annotation.Excel;
import com.tj.common.core.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 每日方量对象 t_day_volume
 * 
 * @author liyan
 * @date 2023-12-12
 */
@Data
public class DayVolume extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long gid;

    /** 日期 */
    @Excel(name = "日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date days;

    /** 当日方量 */
    @Excel(name = "当日方量")
    private BigDecimal volume;

    /** 累计方量 */
    @Excel(name = "累计方量")
    private BigDecimal totalvolume;

    /** 录入人 */
    @Excel(name = "录入人")
    private String createUser;

    /** 备用 */
    @Excel(name = "备用")
    private String freedom1;

    /** 备用 */
    @Excel(name = "备用")
    private String freedom2;

    /** 备用 */
    @Excel(name = "备用")
    private String freedom3;

}
