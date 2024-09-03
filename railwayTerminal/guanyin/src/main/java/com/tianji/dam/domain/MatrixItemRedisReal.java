package com.tianji.dam.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.LinkedList;

@Component
@ApiModel(description = "矩阵项")
public class MatrixItemRedisReal implements Serializable {
    @ApiModelProperty(name = "serialVersionUID", value = "用户ID", required = true, example = "828776519500192025L")
    private static final long serialVersionUID = 828776519500192025L;

    @ApiModelProperty(name = "RollingTimes", value = "碾压遍数", required = true, example = "0")
    private LinkedList<Integer> RollingTimeList = new LinkedList<>();
    private Integer RollingTimes = 0;//碾压遍数

    private LinkedList<Float> CurrentEvolution = new LinkedList<>();//当前点高程

    public LinkedList<Integer> getRollingTimeList() {
        return RollingTimeList;
    }

    public void setRollingTimeList(LinkedList<Integer> rollingTimeList) {
        RollingTimeList = rollingTimeList;
    }

    public Integer getRollingTimes() {
        return RollingTimes;
    }

    public void setRollingTimes(Integer rollingTimes) {
        RollingTimes = rollingTimes;
    }

    public LinkedList<Float> getCurrentEvolution() {
        return CurrentEvolution;
    }

    public void setCurrentEvolution(LinkedList<Float> currentEvolution) {
        CurrentEvolution = currentEvolution;
    }
}
