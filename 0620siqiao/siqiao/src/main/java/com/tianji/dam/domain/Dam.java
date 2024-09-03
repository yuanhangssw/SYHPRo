package com.tianji.dam.domain;

public class Dam {
    private int id;
    private String title;
    private String engcode;
    private double gaocheng;
    private double cenggao;

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEngcode() {
        return engcode;
    }

    public void setEngcode(String engcode) {
        this.engcode = engcode;
    }

    public double getGaocheng() {
        return gaocheng;
    }

    public void setGaocheng(double gaocheng) {
        this.gaocheng = gaocheng;
    }

    public double getCenggao() {
        return cenggao;
    }

    public void setCenggao(double cenggao) {
        this.cenggao = cenggao;
    }
}
