package com.tianji.dam.coder;


import com.tianji.dam.domain.BidrHeader;
import com.tianji.dam.domain.BidrMessage;
import com.tianji.dam.domain.Gpsinfo;
import com.tianji.dam.utils.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.EncoderException;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;

@Slf4j
public class CarcpBufferDecoder extends ByteToMessageDecoder {
    private final static int HEADER_LENGTH = 1;// header的长度


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 长度不足，退出
        if (in.readableBytes() < HEADER_LENGTH) {
            return;
        }
        // 游标开始位置
        int readerIndex = in.readerIndex();
        // 游标结束位置
        int writerIndex = in.writerIndex();
        while (readerIndex < writerIndex) {
            // 标记包头开始的index
            int bufLen = in.readableBytes();
            byte[] value = new byte[bufLen];
            in.getBytes(0, value);
            //获取完整数据帧
            byte[] frame = recv(value, readerIndex);
            if (frame != null) {
                //如果解析成功,进行处理
                int frameLen = frame.length;
                readerIndex += frameLen;
                //重置开始标识
                in.readerIndex(readerIndex);
                BidrMessage message = parser(frame);
                out.add(message);
            } else {
                break;
            }
        }
        //将数组开头到处理的字节尾清空
        in.discardReadBytes();
    }

    /**
     * 解码获得协议
     */
    private byte[] recv(byte[] value, int cursor) {
        byte[] buf = null;
        int len = value.length;
        int startIndex = -1;
        //寻找协议头 len-4排除value中恰好只有4byte协议头的情况
        for (int i = cursor; i < (len - 1); i++) {
            if (value[i] == (byte) 0x7E) {
                startIndex = i;
                break;
            }
        }
        if (startIndex != -1) {
            //协议头(4byte)+协议内容长度(4byte)+协议类型(4byte)
            int endIndex = startIndex + 46;
            if (endIndex < len) {
                int frameLen = endIndex - startIndex + 1;
                //todo:根据数据帧长度 区分GPS WIFI LBS SOS模式
                buf = new byte[frameLen];
                System.arraycopy(value, startIndex, buf, 0, frameLen);
            }
        }
        return buf;
    }

    /**
     * 解析协议为pojo
     */
    private BidrMessage parser(byte[] bytes) {
        BidrMessage message = null;
        try {
            //7E 02 00 00 20 06 75 02 15 52 69 00 22 00 00 00 00 00 00 00 03 01 5B 77 73 06 C9 B4 90 00 00 00 00 01 3F 21 01 25 09 55 08 56 02 04 00 EF 7E
            String hex = ByteUtils.bytesToHex(bytes);
            int cursor = 0;
            String head = ByteUtils.bytesToHex(bytes,cursor,1);
            cursor += 1;
            String messageId = ByteUtils.bytesToHex(bytes,cursor,2);
            cursor += 2;
            String attribute =  ByteUtils.bytesToHex(bytes,cursor,2);
            cursor += 2;
            String id =  ByteUtils.bytesToHex(bytes,cursor,6);
            long carId = Long.parseLong(id.replaceAll(" +",""));
            cursor += 6;
            String waterNumber =  ByteUtils.bytesToHex(bytes,cursor,2);
            cursor += 2;
            String warnSign = ByteUtils.bytesToHex(bytes,cursor,4);
            cursor += 4;
            byte[] stateSignbytes = new byte[4];
            System.arraycopy(bytes,cursor,stateSignbytes,0,4);
            int stateInt = ByteUtils.byte4ToInt(stateSignbytes,0);
            int state = ByteUtils.int32ToBit(stateInt,4);
            cursor += 4;
            byte[] latbytes = new byte[4];
            System.arraycopy(bytes,cursor,latbytes,0,4);
            float lat = (float) (ByteUtils.byte4ToInt(latbytes,0)*1.0f/Math.pow(10,6));
            cursor += 4;
            byte[] lngbytes = new byte[4];
            System.arraycopy(bytes,cursor,lngbytes,0,4);
            float lng = (float) (ByteUtils.byte4ToInt(lngbytes,0)*1.0f/Math.pow(10,6));
            cursor += 4;
            byte[] elevationbytes = new byte[2];
            System.arraycopy(bytes,cursor,elevationbytes,0,2);
            int elevation = ByteUtils.byte2ToShort(elevationbytes);
            cursor += 2;
            byte[] speedbytes = new byte[2];
            System.arraycopy(bytes,cursor,speedbytes,0,2);
            int speed = ByteUtils.bytesToShort2(speedbytes);
            cursor += 2;
            byte[] anglebytes = new byte[2];
            System.arraycopy(bytes,cursor,anglebytes,0,2);
            int angle = ByteUtils.byte2ToShort(anglebytes);
            cursor += 2;
            byte[] timebytes = new byte[6];
            System.arraycopy(bytes,cursor,timebytes,0,6);
            String time = new Date().toString();
            cursor += 6;
            String electricityId = ByteUtils.bytesToHex(bytes,cursor,1);
            cursor += 1;
            byte[] electricityLenBytes = new byte[1];
            System.arraycopy(bytes,cursor,electricityLenBytes,0,1);
            int electricityLen = electricityLenBytes[0];
            cursor += 1;
            byte[] electricityBytes = new byte[electricityLen];
            System.arraycopy(bytes,cursor,electricityBytes,0,electricityLen);
            float electricity = ByteUtils.byte2ToShort(electricityBytes) * 1.0f/ 100;
            cursor += electricityLen;
            String xorSigh = ByteUtils.bytesToHex(bytes,cursor,1);
            cursor += 1;
            String end = ByteUtils.bytesToHex(bytes,cursor,1);
            cursor += 1;

            Gpsinfo gpsInfo=new Gpsinfo();
            gpsInfo.setHead(head);
            gpsInfo.setMessageid (messageId);
            gpsInfo.setWaternumber (waterNumber);
            gpsInfo.setIdcard(String.valueOf(carId));
            gpsInfo.setAttribute(attribute);
            gpsInfo.setWarning(warnSign);
            gpsInfo.setStatus(String.valueOf(state));
            gpsInfo.setLatitude (String.valueOf(lat));
            gpsInfo.setLongitude(String.valueOf(lng));
            gpsInfo.setGaocheng (String.valueOf(elevation));
            gpsInfo.setSpeed(String.valueOf(speed));
            gpsInfo.setDirectionangle (String.valueOf(angle));
            gpsInfo.setTime(String.valueOf(time));
            gpsInfo.setElectricity(String.valueOf(electricity > 100 ? 100 : electricity));
            gpsInfo.setLength(String.valueOf(electricityLen));
            gpsInfo.setCheck(String.valueOf(xorSigh));
            gpsInfo.setEnd(String.valueOf(end));

            int symbol = 959985462;
            BidrHeader header = new BidrHeader(symbol, bytes.length, MessageType.SysCar.toInt());
            message = new BidrMessage(header,gpsInfo);
        } catch (EncoderException e) {
            throw e;
        }
        return message;
    }
}
