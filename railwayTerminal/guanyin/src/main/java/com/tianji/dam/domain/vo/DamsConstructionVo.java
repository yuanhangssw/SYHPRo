package com.tianji.dam.domain.vo;

import lombok.Data;

@Data
public class DamsConstructionVo {

    private int page;
    private int limit;
    private  int begin;
    private String id;
    private String title;
    private String planstarttime;
    private String planendtime;
    private String actualstarttime;
    private String actualendtime;
    private int status;


}
