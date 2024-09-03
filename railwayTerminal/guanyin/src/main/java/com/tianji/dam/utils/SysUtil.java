package com.tianji.dam.utils;


import com.tianji.dam.coder.Identification;
import com.tianji.dam.coder.MessageType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SysUtil {
    /**
     * 构建byte流
     * @param ProtocolType:协议头
     * @param ProtocolContent:协议内容
     */
    public static byte[] BuildResponseStream(int ProtocolType, byte[] ProtocolContent) {
        ExChange ExchB = new ExChange(false);
        ExchB.AddBytes(Identification.BidrProtocolHead, false);
        if (ProtocolContent != null) {
            ExchB.AddIntAsBytes(ProtocolContent.length);
            ExchB.AddIntAsBytes(ProtocolType);
            ExchB.AddBytes(ProtocolContent, false);
        } else {
            ExchB.AddIntAsBytes(0);
            ExchB.AddIntAsBytes(ProtocolType);
        }
        return ExchB.GetAllBytes();
    }

    /**
     * 字节流解析为自定义协议
     */
    public static Protocol decodeProtocol(byte[] recv){
        Protocol protocol = null;
        try{
            ExChange exA = new ExChange(false);
            byte[] protocolType = new byte[Identification.TypeLength];//协议类型
            byte[] contentLength = new byte[Identification.ContentLength];//协议内容长度
            System.arraycopy(recv, Identification.TypeStart,protocolType,0, Identification.TypeLength);
            System.arraycopy(recv, Identification.HeadLength,contentLength,0, Identification.ContentLength);
            int msgType = exA.BytestoInt(protocolType,0);
            int msgLength = exA.BytestoInt(contentLength,0);
            byte[] content = new byte[msgLength];
            System.arraycopy(recv, Identification.ContentStart,content,0,msgLength);
            MessageType messageType = MessageType.fromInt(msgType);
            protocol = new Protocol(messageType.toInt(),content);
        }catch (Exception ex){
            log.error("decodeProtocol:" + ex.getMessage());
        }
        return protocol;
    }
}
