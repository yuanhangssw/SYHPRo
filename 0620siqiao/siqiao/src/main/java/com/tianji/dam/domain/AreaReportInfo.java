package com.tianji.dam.domain;

public class AreaReportInfo {
    private Integer pid;

    private long types;

    private String title;
    private String ceng;

    private String bianshurate1;

    private String bianshurate2;

    private String starttime;

    private String endtime;

    private Double speed;

    public String getCeng() {
        return ceng;
    }

    public void setCeng(String ceng) {
        this.ceng = ceng;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public long getTypes() {
        return types;
    }

    public void setTypes(long types) {
        this.types = types;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBianshurate1() {
        return bianshurate1;
    }

    public void setBianshurate1(String bianshurate1) {
        this.bianshurate1 = bianshurate1;
    }

    public String getBianshurate2() {
        return bianshurate2;
    }

    public void setBianshurate2(String bianshurate2) {
        this.bianshurate2 = bianshurate2;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

}
