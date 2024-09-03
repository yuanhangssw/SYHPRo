package com.tianji.dam.server;


import com.tianji.dam.coder.BidrTcpBufferDecoder;
import com.tianji.dam.coder.BidrTcpEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Server {

    public void start(InetSocketAddress socketAddress) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 指定socket的一些属性
            serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)  // 指定是一个NIO连接通道
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>(){
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //心跳保活
                            pipeline.addLast(new IdleStateHandler(15, 0, 0, TimeUnit.SECONDS));
                           // pipeline.addLast(new CarcpBufferDecoder());
                            pipeline.addLast(new BidrTcpBufferDecoder());
                            pipeline.addLast(new BidrTcpEncoder());
                            // 添加逻辑控制层
                            pipeline.addLast(new ServerHandler());
                            pipeline.addLast(new ExceptionHandler());
                        } 
                    });
            // 绑定对应的端口号,并启动开始监听端口上的连接
            Channel ch = serverBootstrap.bind(socketAddress.getPort()).sync().channel();
            log.info("luck协议启动地址：127.0.0.1:%d/\n", socketAddress.getPort());
            // 等待关闭,同步端口
            ch.closeFuture().sync();
        } catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
