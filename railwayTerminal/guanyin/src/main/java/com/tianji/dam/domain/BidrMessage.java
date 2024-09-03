package com.tianji.dam.domain;

public class BidrMessage {
    private BidrHeader bidrHeader;
//    private RollingData rollingData;
    private Object rollingData;
    public BidrMessage(BidrHeader bidrHeader, Object rollingData) {
        this.bidrHeader = bidrHeader;
        this.rollingData = rollingData;
    }

    public BidrHeader getBidrHeader() {
        return bidrHeader;
    }

    public void setBidrHeader(BidrHeader bidrHeader) {
        this.bidrHeader = bidrHeader;
    }

    public Object getRollingData() {
        return rollingData;
    }

    public void setRollingData(Object rollingData) {
        this.rollingData = rollingData;
    }
}
