package com.tianji.dam.domain;
public class BidrHeader {
    //协议标识
    private int symbol;
    //协议长度
    private int length;
    //协议类型
    private int type;

    public BidrHeader() {
    }

    public BidrHeader(int symbol, int length, int type) {
        this.symbol = symbol;
        this.length = length;
        this.type = type;
    }

    public int getSymbol() {
        return symbol;
    }

    public void setSymbol(int symbol) {
        this.symbol = symbol;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
