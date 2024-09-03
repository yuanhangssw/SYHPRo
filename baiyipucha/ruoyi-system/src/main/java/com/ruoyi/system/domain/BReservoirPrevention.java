package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 水库防治情况对象 b_reservoir_prevention
 * 
 * @author
 * @date 2024-04-08
 */
public class BReservoirPrevention extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 危害类型 */
    @Excel(name = "危害类型")
    private String detrimentType;

    /** 项目 */
    @Excel(name = "项目")
    private Long projectId;

    /** 单元id */
    @Excel(name = "单元id")
    private Long unitId;

    /** 危害等级 */
    @Excel(name = "危害等级")
    private Long detrimentLevel;

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
    private Long investCapital;

    /** 动物种类 */
    @Excel(name = "动物种类")
    private String zoonType;

    /** 动物治理数量 */
    @Excel(name = "动物治理数量")
    private Long zoonGovernNumber;

    /** 动物治理方式 */
    @Excel(name = "动物治理方式")
    private String zoonGovernType;

    @Excel(name = "I级危害长度")
    private Long oneDetrimentLength;

    /** II级危害长度 */
    @Excel(name = "II级危害长度")
    private Long twoDetrimentLength;

    /** III级危害长度 */
    @Excel(name = "III级危害长度")
    private Long threeDetrimentLength;

    public Long getOneDetrimentLength() {
        return oneDetrimentLength;
    }

    public void setOneDetrimentLength(Long oneDetrimentLength) {
        this.oneDetrimentLength = oneDetrimentLength;
    }

    public Long getTwoDetrimentLength() {
        return twoDetrimentLength;
    }

    public void setTwoDetrimentLength(Long twoDetrimentLength) {
        this.twoDetrimentLength = twoDetrimentLength;
    }

    public Long getThreeDetrimentLength() {
        return threeDetrimentLength;
    }

    public void setThreeDetrimentLength(Long threeDetrimentLength) {
        this.threeDetrimentLength = threeDetrimentLength;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setDetrimentType(String detrimentType) 
    {
        this.detrimentType = detrimentType;
    }

    public String getDetrimentType() 
    {
        return detrimentType;
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
    public void setDetrimentLevel(Long detrimentLevel) 
    {
        this.detrimentLevel = detrimentLevel;
    }

    public Long getDetrimentLevel() 
    {
        return detrimentLevel;
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
    public void setInvestCapital(Long investCapital) 
    {
        this.investCapital = investCapital;
    }

    public Long getInvestCapital() 
    {
        return investCapital;
    }
    public void setZoonType(String zoonType) 
    {
        this.zoonType = zoonType;
    }

    public String getZoonType() 
    {
        return zoonType;
    }
    public void setZoonGovernNumber(Long zoonGovernNumber) 
    {
        this.zoonGovernNumber = zoonGovernNumber;
    }

    public Long getZoonGovernNumber() 
    {
        return zoonGovernNumber;
    }
    public void setZoonGovernType(String zoonGovernType) 
    {
        this.zoonGovernType = zoonGovernType;
    }

    public String getZoonGovernType() 
    {
        return zoonGovernType;
    }

    @Override
    public String toString() {
        return "BReservoirPrevention{" +
                "id=" + id +
                ", detrimentType='" + detrimentType + '\'' +
                ", projectId=" + projectId +
                ", unitId=" + unitId +
                ", detrimentLevel=" + detrimentLevel +
                ", leaksNumber=" + leaksNumber +
                ", throughNumber=" + throughNumber +
                ", dropSocketNumber=" + dropSocketNumber +
                ", nestDigging=" + nestDigging +
                ", chargeArea=" + chargeArea +
                ", groutingQuantity=" + groutingQuantity +
                ", investCapital=" + investCapital +
                ", zoonType='" + zoonType + '\'' +
                ", zoonGovernNumber=" + zoonGovernNumber +
                ", zoonGovernType='" + zoonGovernType + '\'' +
                ", oneDetrimentLength=" + oneDetrimentLength +
                ", twoDetrimentLength=" + twoDetrimentLength +
                ", threeDetrimentLength=" + threeDetrimentLength +
                '}';
    }
}
