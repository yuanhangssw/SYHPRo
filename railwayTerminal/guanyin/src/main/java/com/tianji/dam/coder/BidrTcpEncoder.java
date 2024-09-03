package com.tianji.dam.coder;

import com.tianji.dam.domain.BidrMessage;
import com.tianji.dam.domain.DeviceState;
import com.tianji.dam.domain.RollingData;
import com.tianji.dam.domain.SysRate;
import com.tianji.dam.utils.SysUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;

/**
 * 碾压数据协议编码器
 */
public class BidrTcpEncoder extends MessageToByteEncoder<BidrMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, BidrMessage message, ByteBuf out) throws Exception {
        // 将Message转换成二进制数据
        // 写入Header信息
        MessageType messageType = MessageType.fromInt(message.getBidrHeader().getType());
        switch (messageType) {
            case Heart: {
                DeviceState deviceState = (DeviceState) message.getRollingData();
                byte[] bytes = SysUtil.BuildResponseStream(MessageType.Heart.toInt(),
                        deviceState.getDeviceId().getBytes(CharsetUtil.UTF_8));
                out.writeBytes(bytes);
                break;
            }
            case SysTrack: {
                RollingData rollingData = (RollingData) message.getRollingData();
                byte[] bytes = SysUtil.BuildResponseStream(MessageType.SysTrack.toInt(), rollingData.toBinary());
                out.writeBytes(bytes);
                break;
            }
            case SysTrackFOUR: {
                RollingData rollingData = (RollingData) message.getRollingData();
                byte[] bytes = SysUtil.BuildResponseStream(MessageType.SysTrack.toInt(), rollingData.toBinary3());
                out.writeBytes(bytes);
                break;
            }
            case SysBulldozer: {
                RollingData rollingData = (RollingData) message.getRollingData();
                byte[] bytes = SysUtil.BuildResponseStream(MessageType.SysBulldozer.toInt(), rollingData.toBinary2());
                out.writeBytes(bytes);
                break;
            }
            case SysRate: {
                SysRate sysRate = (SysRate) message.getRollingData();
                byte[] bytes = SysUtil.BuildResponseStream(MessageType.SysRate.toInt(), sysRate.toBinary());
                out.writeBytes(bytes);
                break;
            }
        }
    }
}
