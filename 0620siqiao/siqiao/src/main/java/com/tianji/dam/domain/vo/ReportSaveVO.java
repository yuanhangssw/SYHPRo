package com.tianji.dam.domain.vo;


import com.tj.common.annotation.Excel;
import lombok.Data;

@Data
public class ReportSaveVO {
    @Excel(name = "仓ID")
    private Integer id;

    @Excel(name = "单位工程")
    private String areaname;

    @Excel(name = "起始高程")
    private Double begin_gaocheng;

    @Excel(name = "结束高程")
    private Double gaocheng_top;

    @Excel(name = "填筑面积")
    private Double finalarea;

    @Excel(name = "碾压厚度")
    private Double houdu;

    @Excel(name = "碾压方量(m³)")
    private Double volume;

    @Excel(name = "最大速度(km/h)")
    private Double maxspeed;

    @Excel(name = "平均速度(km/h)")
    private Double avgspeed;

    @Excel(name = "碾压次数")
    private Integer bianshu;

    @Excel(name = "碾压合格率")
    private String bianshurate;

    @Excel(name = "用料类型")
    private String materialname;

    @Excel(name = "实际施工时间")
    private String startendtime;


}
