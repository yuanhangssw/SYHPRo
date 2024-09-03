package com.tianji.dam.domain;

import com.tj.common.annotation.Excel;
import com.tj.common.core.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 短信发送记录对象 t_sms_send_record
 *
 * @author ly
 * @date 2023-12-11
 */
@Data
public class SmsSendRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private Long gid;

    /**
     * 发送号码
     */
    @Excel(name = "发送号码")
    private String smsTel;

    /**
     * 发送用户
     */
    @Excel(name = "发送用户")
    private String telUser;

    /**
     * 车辆名称
     */
    @Excel(name = "车辆名称")
    private String carName;

    /**
     * 当前值
     */
    @Excel(name = "当前值")
    private BigDecimal currentValue;

    /**
     * 限制值
     */
    @Excel(name = "限制值")
    private BigDecimal normalValue;

    /**
     * 仓位id
     */
    @Excel(name = "仓位id")
    private Long damGid;
    private String title;
    private String ceng;

    /**
     * 备用
     */
    @Excel(name = "备用")
    private String freedom1;

    /**
     * 备用
     */
    @Excel(name = "备用")
    private String freedom2;
    private String begintime;
    private String endtime;

    @Excel(name = "freedom3")
    private String freedom3;

    @Excel(name = "freedom4")
    private String freedom4;


}
