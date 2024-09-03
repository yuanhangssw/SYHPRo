package com.tianji.dam.domain.vo;

import com.tianji.dam.domain.Row;

import java.util.List;

public class ResponseData {
    private Long total;
    private int code;
    private String msg;
    private List<Row> rows;

    // Getters and setters


    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }
}