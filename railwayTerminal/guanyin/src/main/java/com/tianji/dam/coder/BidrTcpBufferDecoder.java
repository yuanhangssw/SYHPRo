package com.tianji.dam.coder;
import com.tianji.dam.domain.*;
import com.tianji.dam.utils.ExChange;
import com.tianji.dam.utils.Protocol;
import com.tianji.dam.utils.SysUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public class BidrTcpBufferDecoder extends ByteToMessageDecoder {
    private final static int HEADER_LENGTH = 12;// header的长度

    /**
     * 解码获得协议
     */
    private byte[] recv(byte[] value, int cursor) {
        byte[] buf = null;
        int len = value.length;
        int startIndex = -1;
        //寻找协议头 len-4排除value中恰好只有4byte协议头的情况
        for (int i = cursor; i < (len - 4); i++) {
            if (value[i] == (byte) 0x36 && value[i + 1] == (byte) 0x37 &&
                    value[i + 2] == (byte) 0x38 && value[i + 3] == (byte) 0x39) {
                startIndex = i;
                break;
            }
        }
        if (startIndex != -1) {
            //协议头(4byte)+协议内容长度(4byte)+协议类型(4byte)
            int endIndex = startIndex + 4;
            if (endIndex < len) {
                int protocolLen = bytesToInt(value, endIndex);
                endIndex += 4;
                int protocolType = bytesToInt(value, endIndex);
                endIndex += 4;
                endIndex += protocolLen;
                if (endIndex <= len) {
                    int frameLen = endIndex - startIndex;
                    buf = new byte[frameLen];
                    System.arraycopy(value, startIndex, buf, 0, frameLen);
//                    log.error("获得一条完整数据:" + protocolType);
                }
            }
        }
        return buf;
    }

    /**
     * 解析协议为pojo
     *
     * @param recv:完整的一条协议
     */
    private BidrMessage parser(byte[] recv) {
        BidrMessage message = null;
        Map<String, Integer> spover = new HashMap<>();
        try {
            ExChange exChange = new ExChange(false);
            Protocol protocol = SysUtil.decodeProtocol(recv);
            if (protocol != null) {
                byte[] content = protocol.getProtocolContent();

                MessageType messageType = MessageType.fromInt(protocol.getProtocolType());
                int symbol = 959985462;
                switch (messageType) {
                    case Heart: {
                        DeviceState deviceState = new DeviceState();
                        int cursor = 0;
                        int length = exChange.BytestoInt(content, cursor);
                        cursor += 4;
                        String device = exChange.BytestoString(content, cursor, length, false);
                        deviceState.setDeviceId(device);
                        cursor += length;
                        deviceState.setState(exChange.BytestoInt(content, cursor));
                        BidrHeader header = new BidrHeader(symbol, content.length, MessageType.Heart.toInt());
                        message = new BidrMessage(header, deviceState);
//                        log.info("心跳:"+ device);
                        break;
                    }
                    case SysRate: {
                        SysRate sysRate = new SysRate();
                        int cursor = 0;
                        int uuidLength = exChange.BytestoInt(content, cursor);
                        cursor += 4;
                        sysRate.setUuid(exChange.BytestoString(content, cursor, uuidLength, false));
                        cursor += uuidLength;
                        int ownerLength = exChange.BytestoInt(content, cursor);
                        cursor += 4;
                        sysRate.setOwnerCar(exChange.BytestoString(content, cursor, ownerLength, false));
                        cursor += ownerLength;
                        int otherLength = exChange.BytestoInt(content, cursor);
                        cursor += 4;
                        sysRate.setOtherCar(exChange.BytestoString(content, cursor, otherLength, false));
                        cursor += otherLength;
                        sysRate.setOrderNum(exChange.BytestoInt(content, cursor));
                        BidrHeader header = new BidrHeader(symbol, content.length, MessageType.SysRate.toInt());
                        message = new BidrMessage(header, sysRate);
                        break;
                    }
                    case SysTrack: {
                        RollingData model = new RollingData();
                        int cursor = 0;
                        model.setCoordX(exChange.BytestoDouble(content, cursor));

                        cursor += 8;
                        model.setCoordY(exChange.BytestoDouble(content, cursor));
                        cursor += 8;
                        model.setLatitude(exChange.BytestoDouble(content, cursor));
                        cursor += 8;
                        model.setLongitude(exChange.BytestoDouble(content, cursor));
                        cursor += 8;
                        model.setElevation(exChange.BytestoFloat(content, cursor));
                        cursor += 4;
                        model.setTimestamp(exChange.BytestoLong(content, cursor));
                        cursor += 8;
                        int length = exChange.BytestoInt(content, cursor);
                        cursor += 4;
                        model.setVehicleID(exChange.BytestoString(content, cursor, length, false));
                        cursor += length;
                        float speed = exChange.BytestoFloat(content, cursor);
                        model.setSpeed(speed);
                        cursor += 4;
                        model.setLayerID(exChange.BytestoInt(content, cursor));
                        cursor += 4;
                        model.setIsForward(exChange.BytestoInt(content, cursor));
                        cursor += 4;
                        model.setIsVibrate(exChange.BytestoInt(content, cursor));
                        cursor += 4;

                        double vib = exChange.BytestoDouble(content, cursor);
                        //   vib =vib*1.33;
                        if (vib > 100) {
                            vib = 99;
                        }
                        BigDecimal bd = new BigDecimal(vib).setScale(2, RoundingMode.HALF_DOWN);
                        model.setVibrateValue(bd.doubleValue());
                        cursor += 8;
                        model.setFrequency(exChange.BytestoDouble(content, cursor));
                        cursor += 8;
                        model.setAcceleration(exChange.BytestoDouble(content, cursor));
                        cursor += 8;
                        model.setAmplitude(exChange.BytestoDouble(content, cursor));
                        cursor += 8;
                        model.setSatellites(exChange.BytestoInt(content, cursor));
                        cursor += 4;
                        model.setQualitylnd(exChange.BytestoInt(content, cursor));
                        cursor += 4;
                        model.setMaterialname(exChange.BytestoInt(content, cursor));
                        cursor += 4;
                        model.setOrderNum(exChange.BytestoInt(content, cursor));
                        cursor += 4;
                        model.setAngle(exChange.BytestoFloat(content, cursor));
                        cursor += 4;
                        model.setCoordLX(exChange.BytestoDouble(content, cursor));
                        cursor += 8;
                        model.setCoordLY(exChange.BytestoDouble(content, cursor));
                        cursor += 8;
                        model.setCoordRX(exChange.BytestoDouble(content, cursor));
                        cursor += 8;
                        model.setCoordRY(exChange.BytestoDouble(content, cursor));
                        cursor += 8;
                        model.setIshistory(exChange.BytestoInt(content, cursor));
                        cursor += 4;

                        //   cursor += 4;
                        //model.setZyangle(exChange.BytestoFloat(content, cursor));
                        //   log.info("Or:"+ model.getOrderNum() + " IsHistory:" + model.getIshistory());
                        BidrHeader header = new BidrHeader(symbol, content.length, MessageType.SysTrack.toInt());
                        message = new BidrMessage(header, model);
                        break;
                    }
                    case SysTrackFOUR: {
                        RollingData model = new RollingData();
                        int cursor = 0;
                        model.setCoordX(exChange.BytestoDouble(content, cursor));

                      //  System.out.println("数据长度："+content.length);
                        if(content.length<169){
                            System.out.println("数据长度不足。"+content.length);
                            return null;

                        }
                        cursor += 8;
                        model.setCoordY(exChange.BytestoDouble(content, cursor));
                        cursor += 8;
                        model.setLatitude(exChange.BytestoDouble(content, cursor));
                        cursor += 8;
                        model.setLongitude(exChange.BytestoDouble(content, cursor));
                        cursor += 8;
                        model.setElevation(exChange.BytestoFloat(content, cursor));
                        cursor += 4;
                        model.setTimestamp(exChange.BytestoLong(content, cursor));
                        cursor += 8;
                        int length = exChange.BytestoInt(content, cursor);
                        cursor += 4;
                        model.setVehicleID(exChange.BytestoString(content, cursor, length, false));
                        cursor += length;
                        float speed = exChange.BytestoFloat(content, cursor);
                        model.setSpeed(speed);
                        cursor += 4;
                        model.setLayerID(exChange.BytestoInt(content, cursor));
                        cursor += 4;
                        model.setIsForward(exChange.BytestoInt(content, cursor));
                        cursor += 4;
                        model.setIsVibrate(exChange.BytestoInt(content, cursor));
                        cursor += 4;

                        double vib = exChange.BytestoDouble(content, cursor);
                        //   vib =vib*1.33;
                        if (vib > 100) {
                            vib = 99;
                        }
                        BigDecimal bd = new BigDecimal(vib).setScale(2, RoundingMode.HALF_DOWN);
                        model.setVibrateValue(bd.doubleValue());
                        cursor += 8;
                        model.setFrequency(exChange.BytestoDouble(content, cursor));
                        cursor += 8;
                        model.setAcceleration(exChange.BytestoDouble(content, cursor));
                        cursor += 8;
                        model.setAmplitude(exChange.BytestoDouble(content, cursor));
                        cursor += 8;
                        model.setSatellites(exChange.BytestoInt(content, cursor));
                        cursor += 4;
                        model.setQualitylnd(exChange.BytestoInt(content, cursor));
                        cursor += 4;
                        model.setMaterialname(exChange.BytestoInt(content, cursor));
                        cursor += 4;
                        model.setOrderNum(exChange.BytestoInt(content, cursor));
                        cursor += 4;
                        model.setAngle(exChange.BytestoFloat(content, cursor));
                        cursor += 4;
                        model.setCoordLX(exChange.BytestoDouble(content, cursor));
                        cursor += 8;
                        model.setCoordLY(exChange.BytestoDouble(content, cursor));
                        cursor += 8;
                        model.setCoordRX(exChange.BytestoDouble(content, cursor));
                        cursor += 8;
                        model.setCoordRY(exChange.BytestoDouble(content, cursor));
                        cursor += 8;
                        model.setIshistory(exChange.BytestoInt(content, cursor));
                        cursor += 4;

                        model.setCurrentEvolution(exChange.BytestoFloat(content, cursor));
                        cursor += 4;
                        model.setBeforeCoordLX(exChange.BytestoDouble(content, cursor));
                        cursor += 8;
                        model.setBeforeCoordLY(exChange.BytestoDouble(content, cursor));
                        cursor += 8;
                        model.setBeforeCoordRX(exChange.BytestoDouble(content, cursor));
                        cursor += 8;
                        model.setBeforeCoordRY(exChange.BytestoDouble(content, cursor));
                        cursor += 8;
                        model.setBeforeElevation(exChange.BytestoFloat(content, cursor));
                        cursor += 4;
                        //压实中该字段表示 沉降量
                        model.setQhangle(exChange.BytestoFloat(content, cursor));

                        //   cursor += 4;
                        //model.setZyangle(exChange.BytestoFloat(content, cursor));
                        //   log.info("Or:"+ model.getOrderNum() + " IsHistory:" + model.getIshistory());
                        BidrHeader header = new BidrHeader(symbol, content.length, MessageType.SysTrack.toInt());
                        message = new BidrMessage(header, model);
                        break;
                    }

                    case SysBulldozer: {
                        RollingData model = new RollingData();
                        int cursor = 0;
                        model.setCoordX(exChange.BytestoDouble(content, cursor));
                        if(content.length!=169){
                            return null;
                        }

                        cursor += 8;
                        model.setCoordY(exChange.BytestoDouble(content, cursor));
                        cursor += 8;
                        model.setLatitude(exChange.BytestoDouble(content, cursor));
                        cursor += 8;
                        model.setLongitude(exChange.BytestoDouble(content, cursor));
                        cursor += 8;
                        model.setElevation(exChange.BytestoFloat(content, cursor));
                        cursor += 4;
                        model.setTimestamp(exChange.BytestoLong(content, cursor));
                        cursor += 8;
                        int length = exChange.BytestoInt(content, cursor);
                        cursor += 4;
                        model.setVehicleID(exChange.BytestoString(content, cursor, length, false));
                        cursor += length;
                        float speed = exChange.BytestoFloat(content, cursor);
                        model.setSpeed(speed);
                        cursor += 4;
                        model.setLayerID(exChange.BytestoInt(content, cursor));
                        cursor += 4;
                        model.setIsForward(exChange.BytestoInt(content, cursor));
                        cursor += 4;
                        model.setIsVibrate(exChange.BytestoInt(content, cursor));
                        cursor += 4;
                        model.setSatellites(exChange.BytestoInt(content, cursor));
                        cursor += 4;
                        model.setQualitylnd(exChange.BytestoInt(content, cursor));
                        cursor += 4;
                        model.setMaterialname(exChange.BytestoInt(content, cursor));
                        cursor += 4;
                        model.setOrderNum(exChange.BytestoInt(content, cursor));
                        cursor += 4;
                        model.setAngle(exChange.BytestoFloat(content, cursor));
                        cursor += 4;
                        model.setCoordLX(exChange.BytestoDouble(content, cursor));
                        cursor += 8;
                        model.setCoordLY(exChange.BytestoDouble(content, cursor));
                        cursor += 8;
                        model.setCoordRX(exChange.BytestoDouble(content, cursor));
                        cursor += 8;
                        model.setCoordRY(exChange.BytestoDouble(content, cursor));
                        cursor += 8;
                        model.setIshistory(exChange.BytestoInt(content, cursor));
                        cursor += 4;
                        model.setCurrentEvolution(exChange.BytestoFloat(content, cursor));
                        cursor += 4;
                        model.setBeforeCoordLX(exChange.BytestoDouble(content, cursor));
                        cursor += 8;
                        model.setBeforeCoordLY(exChange.BytestoDouble(content, cursor));
                        cursor += 8;
                        model.setBeforeCoordRX(exChange.BytestoDouble(content, cursor));
                        cursor += 8;
                        model.setBeforeCoordRY(exChange.BytestoDouble(content, cursor));
                        cursor += 8;
                        model.setBeforeElevation(exChange.BytestoFloat(content, cursor));
                        cursor += 4;
                        model.setQhangle(exChange.BytestoFloat(content, cursor));
                        cursor += 4;
                        model.setZyangle(exChange.BytestoFloat(content, cursor));

                        //   log.info("Or:"+ model.getOrderNum() + " IsHistory:" + model.getIshistory());
                        BidrHeader header = new BidrHeader(symbol, content.length, MessageType.SysTrack.toInt());
                        message = new BidrMessage(header, model);
                        break;
                    }
                }

            }
        } catch (Exception ex) {
          ex.printStackTrace();
        }
        return message;
    }

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
            byte[] frame = recv(value, readerIndex);
            if (frame != null) {
                //如果解析成功,进行处理
                int frameLen = frame.length;
                readerIndex += frameLen;
                in.readerIndex(readerIndex);


              //  System.out.println("获取都的原始数据》"+bytesToHex(frame));
                BidrMessage result = parser(frame);
                if (result != null) {
                    // 解析成功 添加到out中
//                    log.info("丢弃前:" + in.readerIndex() + "--->" + in.writerIndex());
                    out.add(result);
                }
            } else {
                break;
            }
        }
        in.discardReadBytes();
//        log.info("丢弃后:" + in.readerIndex() + "--->" + in.writerIndex());
    }


    public static int bytesToInt(byte[] src, int offset) {
        int value;
        value = (int) ((src[offset] & 0xFF)
                | ((src[offset + 1] & 0xFF) << 8)
                | ((src[offset + 2] & 0xFF) << 16)
                | ((src[offset + 3] & 0xFF) << 24));
        return value;
    }

    public static String bytesToHex(byte[] bytes) {
       char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
                '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        // 一个byte为8位，可用两个十六进制位标识
        char[] buf = new char[bytes.length * 2];
        int a = 0;
        int index = 0;
        for(byte b : bytes) { // 使用除与取余进行转换
            if(b < 0) {
                a = 256 + b;
            } else {
                a = b;
            }

            buf[index++] = HEX_CHAR[a / 16];
            buf[index++] = HEX_CHAR[a % 16];
        }

        return new String(buf);
    }

}
