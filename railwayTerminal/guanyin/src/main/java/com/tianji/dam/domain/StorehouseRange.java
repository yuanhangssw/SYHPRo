package com.tianji.dam.domain;
import org.springframework.stereotype.Component;
@Component
public class StorehouseRange {
    private Double minOfxList;
    private Double maxOfxList;
    private Double minOfyList;
    private Double maxOfyList;

    public Double getMinOfxList() {
        return minOfxList;
    }

    public void setMinOfxList(Double minOfxList) {
        this.minOfxList = minOfxList;
    }

    public Double getMaxOfxList() {
        return maxOfxList;
    }

    public void setMaxOfxList(Double maxOfxList) {
        this.maxOfxList = maxOfxList;
    }

    public Double getMinOfyList() {
        return minOfyList;
    }

    public void setMinOfyList(Double minOfyList) {
        this.minOfyList = minOfyList;
    }

    public Double getMaxOfyList() {
        return maxOfyList;
    }

    public void setMaxOfyList(Double maxOfyList) {
        this.maxOfyList = maxOfyList;
    }
}
