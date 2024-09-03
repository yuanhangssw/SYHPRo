package com.ruoyi.system.domain.vo;

import java.io.Serializable;

public class Fujian5  implements Serializable {

    //工程总数	普查完成数	主坝Ⅰ级危害数	 主坝Ⅱ级危害数	主坝Ⅲ级危害数	  副坝处数总数	副坝Ⅰ级危害数	 副坝Ⅱ级危害数	副坝Ⅲ级危害数	 投入资金2022	 投入资金2021	投入资金2020

    private int totalProjects;           // 工程总数
    private int completedSurveys;        // 普查完成数
    private int mainDamLevel1Hazards;    // 主坝Ⅰ级危害数
    private int mainDamLevel2Hazards;    // 主坝Ⅱ级危害数
    private int mainDamLevel3Hazards;    // 主坝Ⅲ级危害数
    private int auxiliaryDamTotal;       // 副坝处数总数
    private int auxiliaryDamLevel1Hazards;// 副坝Ⅰ级危害数
    private int auxiliaryDamLevel2Hazards;// 副坝Ⅱ级危害数
    private int auxiliaryDamLevel3Hazards;// 副坝Ⅲ级危害数
    private double investment2022;        // 投入资金2022
    private double investment2021;        // 投入资金2021
    private double investment2020;        // 投入资金2020
    private double investment2023;        // 投入资金2023

    private  String deptName;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public double getInvestment2023() {
        return investment2023;
    }

    public void setInvestment2023(double investment2023) {
        this.investment2023 = investment2023;
    }

    // Getters and Setters

    public int getTotalProjects() {
        return totalProjects;
    }

    public void setTotalProjects(int totalProjects) {
        this.totalProjects = totalProjects;
    }

    public int getCompletedSurveys() {
        return completedSurveys;
    }

    public void setCompletedSurveys(int completedSurveys) {
        this.completedSurveys = completedSurveys;
    }

    public int getMainDamLevel1Hazards() {
        return mainDamLevel1Hazards;
    }

    public void setMainDamLevel1Hazards(int mainDamLevel1Hazards) {
        this.mainDamLevel1Hazards = mainDamLevel1Hazards;
    }

    public int getMainDamLevel2Hazards() {
        return mainDamLevel2Hazards;
    }

    public void setMainDamLevel2Hazards(int mainDamLevel2Hazards) {
        this.mainDamLevel2Hazards = mainDamLevel2Hazards;
    }

    public int getMainDamLevel3Hazards() {
        return mainDamLevel3Hazards;
    }

    public void setMainDamLevel3Hazards(int mainDamLevel3Hazards) {
        this.mainDamLevel3Hazards = mainDamLevel3Hazards;
    }

    public int getAuxiliaryDamTotal() {
        return auxiliaryDamTotal;
    }

    public void setAuxiliaryDamTotal(int auxiliaryDamTotal) {
        this.auxiliaryDamTotal = auxiliaryDamTotal;
    }

    public int getAuxiliaryDamLevel1Hazards() {
        return auxiliaryDamLevel1Hazards;
    }

    public void setAuxiliaryDamLevel1Hazards(int auxiliaryDamLevel1Hazards) {
        this.auxiliaryDamLevel1Hazards = auxiliaryDamLevel1Hazards;
    }

    public int getAuxiliaryDamLevel2Hazards() {
        return auxiliaryDamLevel2Hazards;
    }

    public void setAuxiliaryDamLevel2Hazards(int auxiliaryDamLevel2Hazards) {
        this.auxiliaryDamLevel2Hazards = auxiliaryDamLevel2Hazards;
    }

    public int getAuxiliaryDamLevel3Hazards() {
        return auxiliaryDamLevel3Hazards;
    }

    public void setAuxiliaryDamLevel3Hazards(int auxiliaryDamLevel3Hazards) {
        this.auxiliaryDamLevel3Hazards = auxiliaryDamLevel3Hazards;
    }

    public double getInvestment2022() {
        return investment2022;
    }

    public void setInvestment2022(double investment2022) {
        this.investment2022 = investment2022;
    }

    public double getInvestment2021() {
        return investment2021;
    }

    public void setInvestment2021(double investment2021) {
        this.investment2021 = investment2021;
    }

    public double getInvestment2020() {
        return investment2020;
    }

    public void setInvestment2020(double investment2020) {
        this.investment2020 = investment2020;
    }
    }


