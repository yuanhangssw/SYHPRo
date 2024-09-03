package com.tianji.dam.coder;

import java.util.List;

import com.tianji.dam.domain.BidrHeader;
import com.tianji.dam.domain.BidrMessage;
import com.tianji.dam.domain.RollingData;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * 碾压数据协议解码器
 */
@Slf4j
public class BidrTcpDecoder extends ByteToMessageDecoder {
    private final static int HEADER_LENGTH = 12;// header的长度
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        try{
            // 长度不足，退出
            if (in.readableBytes() < HEADER_LENGTH) {
                return;
            }
            // 获取协议标识
            byte[] symbolByte = new byte[4];
            in.readBytes(symbolByte);
            Integer symbol = bytesToInt(symbolByte,0);
            if(symbol.equals(959985462)){
                RollingData rollingData = new RollingData();
                // 获取消息长度
                int length = in.readIntLE();
                // 获取协议类型
                int type = in.readIntLE();
                // 组装协议头
                BidrHeader header = new BidrHeader(symbol, length, type);
                // 长度不足重置读index，退出
                if (in.readableBytes() < length) {
                    in.setIndex(in.readerIndex() - HEADER_LENGTH, in.writerIndex());
                    return;
                }
                //todo:isHository也作为协议内的一个字段进行上传
                //todo:这里只需要判断协议类型是SysTrack还是SysRate，
                //todo:如果是SysTrack,BidrMessage写入RollingData,如果是SysRate,找到register中对应的Channel进行转发
                if(type==105){
                    log.info("历史续传");
                    rollingData.setIshistory(1);
                }
                if(type==103){
                    log.info("实时数据");
                    rollingData.setIshistory(0);
                }
                rollingData.setCoordX(in.readDoubleLE());
                rollingData.setCoordY(in.readDoubleLE());
                rollingData.setLongitude(in.readDoubleLE());
                rollingData.setLatitude(in.readDoubleLE());
                rollingData.setElevation(in.readFloatLE());
                rollingData.setTimestamp(in.readLongLE());
                int ByteVehicleIdLength = in.readIntLE();
                byte[] ByteVehicle = new byte[ByteVehicleIdLength];
                in.readBytes(ByteVehicle);
                rollingData.setVehicleID(new String(ByteVehicle));
                rollingData.setSpeed(in.readFloatLE());
                rollingData.setLayerID(in.readIntLE());
                rollingData.setIsForward(in.readIntLE());
                rollingData.setIsVibrate(in.readIntLE());
                rollingData.setVibrateValue(in.readDoubleLE());
                rollingData.setFrequency(in.readDoubleLE());
                rollingData.setAcceleration(in.readDoubleLE());
                rollingData.setAmplitude(in.readDoubleLE());
                rollingData.setSatellites(in.readIntLE());
                rollingData.setQualitylnd(in.readIntLE());
                rollingData.setMaterialname(in.readIntLE());
                rollingData.setOrderNum(in.readIntLE());
                log.error("Or："+rollingData.getOrderNum());
                // 读取消息内容
                BidrMessage message = new BidrMessage(header,rollingData);
                out.add(message);
            }
        }catch (Exception ex){
            log.error("" + ex.getMessage());
        }
    }
    public static int bytesToInt(byte[] src, int offset) {
        int value;
        value = (int) ((src[offset] & 0xFF)
                | ((src[offset+1] & 0xFF)<<8)
                | ((src[offset+2] & 0xFF)<<16)
                | ((src[offset+3] & 0xFF)<<24));
        return value;
    }
}
