package com.tianji.dam.coder;

/**
 * 协议标识
 */
public class Identification {
    //系统返回：框架默认标识头+协议内容长度+协议类型+协议内容（登录状态+用户ID）8+4+4+（4+4）
    public static byte[] BidrProtocolHead = new byte[] {0x36, 0x37, 0x38, 0x39};
    //心跳0x36 37 38 39 00 00 00 04 00 00 00 00 00 00 00 00
    public static final int HeadLength = 4;
    public static final int ProtocolLength = 4;
    public static final int TypeLength = 4;
    public static final int ContentLength = 4;
    public static final int TypeStart = HeadLength + ProtocolLength;
    public static final int ContentStart = HeadLength + ProtocolLength + TypeLength;
}
