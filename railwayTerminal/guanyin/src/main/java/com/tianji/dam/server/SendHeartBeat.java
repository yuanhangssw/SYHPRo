package com.tianji.dam.server;

import com.tianji.dam.coder.MessageType;
import com.tianji.dam.domain.DeviceState;
import com.tianji.dam.utils.SysUtil;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

public class SendHeartBeat implements Runnable {
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    private  String name;
    private Channel channel;
    public Channel getChannel() {
        return channel;
    }
    public void setChannel(Channel channel) {
        this.channel = channel;
    }
    @Override
    public void run() {
        while (true){
            String serverName = "server";
            DeviceState deviceState = new DeviceState();
            deviceState.setDeviceId(serverName);
            deviceState.setState(0);
            byte[] heartBeat = SysUtil.BuildResponseStream(MessageType.Heart.toInt(),deviceState.toBinary());
            deviceState = null;
            if(channel.isActive()){
                ByteBuf buf = Unpooled.buffer(heartBeat.length);
                buf.writeBytes(heartBeat);
                channel.writeAndFlush(buf);
            }
            try {
            	
            	Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
