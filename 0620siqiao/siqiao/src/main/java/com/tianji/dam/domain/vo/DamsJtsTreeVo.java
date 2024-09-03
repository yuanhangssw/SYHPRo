package com.tianji.dam.domain.vo;

import lombok.Data;

@Data
public class DamsJtsTreeVo {
    private int id;
    private String name;
    private String tablename;
    private Double gaocheng;
    private Double cenggao;
    private String ranges;
    public DamsJtsTreeVo() {
    }
    public DamsJtsTreeVo(int id, String name, String tablename, Double gaocheng, Double cenggao, String ranges) {
        this.id = id;
        this.name = name;
        this.tablename = tablename;
        this.gaocheng = gaocheng;
        this.cenggao = cenggao;
        this.ranges = ranges;
    }

    @Override
    public String toString() {
        return "DamsJtsTreeVo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tablename='" + tablename + '\'' +
                ", gaocheng=" + gaocheng +
                ", cenggao=" + cenggao +
                ", ranges='" + ranges + '\'' +
                '}';
    }
}
