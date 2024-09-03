package com.tianji.dam.domain;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Component

public class MatrixItemNews implements Serializable {

    private static final long serialVersionUID = 828776519500192025L;
        List<RollingData> alldata =new LinkedList<>();
    private Integer RollingTimes = 0;//碾压遍数

    public List<RollingData> getAlldata() {
        return alldata;
    }

    public void setAlldata(List<RollingData> alldata) {
        this.alldata = alldata;
    }

    public Integer getRollingTimes() {
        return RollingTimes;
    }

    public void setRollingTimes(Integer rollingTimes) {
        RollingTimes = rollingTimes;
    }
}
