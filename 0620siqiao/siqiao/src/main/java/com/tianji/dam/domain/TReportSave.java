package com.tianji.dam.domain;

import com.tj.common.annotation.Excel;
import com.tj.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 报表保存对象 t_report_save
 *
 * @author liyan
 * @date 2022-10-14
 */
public class TReportSave extends BaseEntity {
    private static final long serialVersionUID = 1L;

    //  d.materialname,d.types

    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMaterialname() {
        return materialname;
    }

    public void setMaterialname(String materialname) {
        this.materialname = materialname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String materialname;


    private String title;

    /**
     * $column.columnComment
     */
    private Long id;

    /**
     * $column.columnComment
     */
    @Excel(name = "仓位", readConverterExp = "$column.readConverterExp()")
    private Long damgid;
    private String ceng;

    /**
     * 报表类型
     */
    @Excel(name = "报表类型")
    private Long types;

    /**
     * 单位工程
     */
    @Excel(name = "单位工程")
    private String unitname;

    /**
     * 分部工程
     */
    @Excel(name = "分部工程")
    private String areaname;

    /**
     * 单元工程
     */
    @Excel(name = "单元工程")
    private String partname;

    /**
     * 施工日期
     */

    @Excel(name = "施工日期", width = 30, dateFormat = "yyyy-MM-dd")
    private String sgtime;

    /**
     * 起止桩号
     */
    @Excel(name = "起止桩号")
    private String zhuanghao;

    /**
     * 起止高程
     */
    @Excel(name = "起止高程")
    private String gaocheng;

    /**
     * 碾压遍数
     */
    @Excel(name = "碾压遍数")
    private String bianshu;

    /**
     * 碾压面积
     */
    @Excel(name = "碾压面积")
    private String mianji;

    /**
     * 结果图
     */
    @Excel(name = "结果图")
    private String base64;

    /**
     * 参数
     */
    @Excel(name = "参数")
    private String param1;

    /**
     * 参数
     */
    @Excel(name = "参数")
    private String param2;

    /**
     * 参数
     */
    @Excel(name = "参数")
    private String param3;

    /**
     * 参数
     */
    @Excel(name = "参数")
    private String param4;

    /**
     * 参数
     */
    @Excel(name = "参数")
    private String param5;

    /**
     * 参数
     */
    @Excel(name = "参数")
    private String param6;

    /**
     * 参数
     */
    @Excel(name = "参数")
    private String param7;

    /**
     * 参数
     */
    @Excel(name = "参数")
    private String param8;

    /**
     * 参数
     */
    @Excel(name = "参数")
    private String param9;

    /**
     * 参数
     */
    @Excel(name = "参数")
    private String param10;

    public String getCeng() {
        return ceng;
    }

    public void setCeng(String ceng) {
        this.ceng = ceng;
    }

    /**
     * $column.columnComment
     */
/*    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String status;*/
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setDamgid(Long damgid) {
        this.damgid = damgid;
    }

    public Long getDamgid() {
        return damgid;
    }

    public void setTypes(Long types) {
        this.types = types;
    }

    public Long getTypes() {
        return types;
    }

    public void setUnitname(String unitname) {
        this.unitname = unitname;
    }

    public String getUnitname() {
        return unitname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }

    public String getAreaname() {
        return areaname;
    }

    public void setPartname(String partname) {
        this.partname = partname;
    }

    public String getPartname() {
        return partname;
    }

    public void setSgtime(String sgtime) {
        this.sgtime = sgtime;
    }

    public String getSgtime() {
        return sgtime;
    }

    public void setZhuanghao(String zhuanghao) {
        this.zhuanghao = zhuanghao;
    }

    public String getZhuanghao() {
        return zhuanghao;
    }

    public void setGaocheng(String gaocheng) {
        this.gaocheng = gaocheng;
    }

    public String getGaocheng() {
        return gaocheng;
    }

    public void setBianshu(String bianshu) {
        this.bianshu = bianshu;
    }

    public String getBianshu() {
        return bianshu;
    }

    public void setMianji(String mianji) {
        this.mianji = mianji;
    }

    public String getMianji() {
        return mianji;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public String getBase64() {
        return base64;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam3(String param3) {
        this.param3 = param3;
    }

    public String getParam3() {
        return param3;
    }

    public void setParam4(String param4) {
        this.param4 = param4;
    }

    public String getParam4() {
        return param4;
    }

    public void setParam5(String param5) {
        this.param5 = param5;
    }

    public String getParam5() {
        return param5;
    }

    public void setParam6(String param6) {
        this.param6 = param6;
    }

    public String getParam6() {
        return param6;
    }

    public void setParam7(String param7) {
        this.param7 = param7;
    }

    public String getParam7() {
        return param7;
    }

    public void setParam8(String param8) {
        this.param8 = param8;
    }

    public String getParam8() {
        return param8;
    }

    public void setParam9(String param9) {
        this.param9 = param9;
    }

    public String getParam9() {
        return param9;
    }

    public void setParam10(String param10) {
        this.param10 = param10;
    }

    public String getParam10() {
        return param10;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("damgid", getDamgid())
                .append("types", getTypes())
                .append("unitname", getUnitname())
                .append("areaname", getAreaname())
                .append("partname", getPartname())
                .append("sgtime", getSgtime())
                .append("zhuanghao", getZhuanghao())
                .append("gaocheng", getGaocheng())
                .append("bianshu", getBianshu())
                .append("mianji", getMianji())
                .append("base64", getBase64())
                .append("param1", getParam1())
                .append("param2", getParam2())
                .append("param3", getParam3())
                .append("param4", getParam4())
                .append("param5", getParam5())
                .append("param6", getParam6())
                .append("param7", getParam7())
                .append("param8", getParam8())
                .append("param9", getParam9())
                .append("param10", getParam10())
                .append("createTime", getCreateTime())
                .append("createBy", getCreateBy())
                .append("status", getStatus())
                .toString();
    }
}
