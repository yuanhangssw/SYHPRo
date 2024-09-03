package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 填报登记对象 b_fill
 * 
 * @author ruoyi
 * @date 2024-03-07
 */
public class BFill
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 项目 */
    @Excel(name = "项目")
    private Long projectId;

    /** 单元 */
    @Excel(name = "单元")
    private Long unitId;

    /** 渗漏处数 */
    @Excel(name = "渗漏处数")
    private Long leaksNumber;

    /** 穿坝处数 */
    @Excel(name = "穿坝处数")
    private Long throughNumber;

    /** 跌窝处数 */
    @Excel(name = "跌窝处数")
    private Long dropSocketNumber;

    /** 挖巢数量 （个） */
    @Excel(name = "挖巢数量 ", readConverterExp = "个=")
    private Long nestDigging;

    /** 施药面积 （m2） */
    @Excel(name = "施药面积 ", readConverterExp = "m=2")
    private Long chargeArea;

    /** 灌浆量 （延米） */
    @Excel(name = "灌浆量 ", readConverterExp = "延=米")
    private Long groutingQuantity;

    /** 投入资金 （万元） */
    @Excel(name = "投入资金 ", readConverterExp = "万=元")
    private Double investCapital;

    /** 治理数量 */
    @Excel(name = "治理数量")
    private Long quantityGovernance;

    /** 填报类型1白蚁2动物 */
    @Excel(name = "填报类型1白蚁2动物")
    private Long fillType;

    /** 巡查用户 */
    @Excel(name = "巡查用户")
    private Long inspector;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

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
    public void setProjectId(Long projectId) 
    {
        this.projectId = projectId;
    }

    public Long getProjectId() 
    {
        return projectId;
    }
    public void setUnitId(Long unitId) 
    {
        this.unitId = unitId;
    }

    public Long getUnitId() 
    {
        return unitId;
    }
    public void setLeaksNumber(Long leaksNumber) 
    {
        this.leaksNumber = leaksNumber;
    }

    public Long getLeaksNumber() 
    {
        return leaksNumber;
    }
    public void setThroughNumber(Long throughNumber) 
    {
        this.throughNumber = throughNumber;
    }

    public Long getThroughNumber() 
    {
        return throughNumber;
    }
    public void setDropSocketNumber(Long dropSocketNumber) 
    {
        this.dropSocketNumber = dropSocketNumber;
    }

    public Long getDropSocketNumber() 
    {
        return dropSocketNumber;
    }
    public void setNestDigging(Long nestDigging) 
    {
        this.nestDigging = nestDigging;
    }

    public Long getNestDigging() 
    {
        return nestDigging;
    }
    public void setChargeArea(Long chargeArea) 
    {
        this.chargeArea = chargeArea;
    }

    public Long getChargeArea() 
    {
        return chargeArea;
    }
    public void setGroutingQuantity(Long groutingQuantity) 
    {
        this.groutingQuantity = groutingQuantity;
    }

    public Long getGroutingQuantity() 
    {
        return groutingQuantity;
    }

    public Double getInvestCapital() {
        return investCapital;
    }

    public void setInvestCapital(Double investCapital) {
        this.investCapital = investCapital;
    }

    public void setQuantityGovernance(Long quantityGovernance)
    {
        this.quantityGovernance = quantityGovernance;
    }

    public Long getQuantityGovernance() 
    {
        return quantityGovernance;
    }
    public void setFillType(Long fillType) 
    {
        this.fillType = fillType;
    }

    public Long getFillType() 
    {
        return fillType;
    }
    public void setInspector(Long inspector) 
    {
        this.inspector = inspector;
    }

    public Long getInspector() 
    {
        return inspector;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("projectId", getProjectId())
            .append("unitId", getUnitId())
            .append("leaksNumber", getLeaksNumber())
            .append("throughNumber", getThroughNumber())
            .append("dropSocketNumber", getDropSocketNumber())
            .append("nestDigging", getNestDigging())
            .append("chargeArea", getChargeArea())
            .append("groutingQuantity", getGroutingQuantity())
            .append("investCapital", getInvestCapital())
            .append("quantityGovernance", getQuantityGovernance())
            .append("fillType", getFillType())
            .append("inspector", getInspector())
            .append("createTime", getCreateTime())
            .append("delFlag", getDelFlag())
            .append("freedom1", getFreedom1())
            .append("freedom2", getFreedom2())
            .append("freedom3", getFreedom3())
            .append("freedom4", getFreedom4())
            .append("freedom5", getFreedom5())
            .toString();
    }
}
