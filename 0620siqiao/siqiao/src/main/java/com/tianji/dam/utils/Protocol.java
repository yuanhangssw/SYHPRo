package com.tianji.dam.utils;

/**
 * 自定义协议
 */
public class Protocol {
    private int protocolType;
    private byte[] protocolContent;

    public int getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(int protocolType) {
        this.protocolType = protocolType;
    }

    public byte[] getProtocolContent() {
        return protocolContent;
    }

    public void setProtocolContent(byte[] protocolContent) {
        this.protocolContent = protocolContent;
    }

    public Protocol(int protocolType, byte[] protocolContent) {
        this.protocolType = protocolType;
        this.protocolContent = protocolContent;
    }
}
