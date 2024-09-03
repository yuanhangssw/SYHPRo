package com.ruoyi.system.domain.vo;

import com.ruoyi.common.annotation.Excel;


/**
 * 主坝VO
 */
public class MainbReservoir
{


    /** 坝型 */
    @Excel(name = "坝型")
    private String damType;

    /** 坝长 */
    @Excel(name = "坝长")
    private Long damLength;

    /** 坝高 */
    @Excel(name = "坝高")
    private Long damHigth;

    /** 护坡方式 */
    @Excel(name = "护坡方式")
    private String slopeProtection;





    public Long getDamLength() {
        return damLength;
    }

    public void setDamLength(Long damLength) {
        this.damLength = damLength;
    }

    public Long getDamHigth() {
        return damHigth;
    }

    public void setDamHigth(Long damHigth) {
        this.damHigth = damHigth;
    }


    public String getDamType() {
        return damType;
    }

    public void setDamType(String damType) {
        this.damType = damType;
    }

    public String getSlopeProtection() {
        return slopeProtection;
    }

    public void setSlopeProtection(String slopeProtection) {
        this.slopeProtection = slopeProtection;
    }
}
