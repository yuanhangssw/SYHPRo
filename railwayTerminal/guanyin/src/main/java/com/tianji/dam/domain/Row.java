package com.tianji.dam.domain;

import java.util.List;
public class Row {
    private int id;
    private String title;
    private List<Dam> dams;

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

    public List<Dam> getDams() {
        return dams;
    }

    public void setDams(List<Dam> dams) {
        this.dams = dams;
    }
}

