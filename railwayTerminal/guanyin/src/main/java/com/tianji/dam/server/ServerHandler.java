package com.tianji.dam.server;


import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.bean.GlobCache;
import com.tianji.dam.coder.MessageType;
import com.tianji.dam.domain.*;
import com.tianji.dam.mapper.CarMapper;
import com.tianji.dam.mapper.GpsinfoMapper;
import com.tianji.dam.mapper.SysRateMapper;
import com.tianji.dam.mileageutil.Mileage;
import com.tianji.dam.service.CarService;
import com.tianji.dam.thread.RedisRightPopTaskBlockingforRealDone2;
import com.tianji.dam.thread.RedisRightPopTaskBlockingforRealDoneCang;
import com.tianji.dam.utils.DataTimeUtil;
import com.tianji.dam.utils.RedisUtil;
import com.tianji.dam.utils.SysUtil;
import com.tianji.dam.utils.TrackConstant;
import com.tianji.dam.websocket.WebsocketServerForPosition;
import com.tj.common.utils.DateUtils;
import com.tj.common.utils.StringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class ServerHandler extends SimpleChannelInboundHandler<BidrMessage> {
    private static final String SYSRATE_TABLE = "sysrate";
    private static final String SERVER_ALIAS = "server";

    private static Double alpha = TrackConstant.alpha;//顺时针旋转角度，弧度制d
    private static Double kk = TrackConstant.kk;//缩放比

    private Thread thread = new Thread();



    //通道注册表 Map<ChannelId,Channel>
    private static ConcurrentHashMap<Integer, Channel> channelRegister = new ConcurrentHashMap<>();
    //设备注册表 Map<DeviceName,ChannelId>
    private static ConcurrentHashMap<String, Integer> deviceRegister = new ConcurrentHashMap<>();

    //线程注册表
    private ConcurrentHashMap<String, RedisRightPopTaskBlockingforRealDoneCang> threadRegister = WebsocketServerForPosition.getThreadRegister();
    private CarMapper carMapper = BeanContext.getApplicationContext().getBean(CarMapper.class);
    //人员识别卡
    private GpsinfoMapper gpsMapper = BeanContext.getApplicationContext().getBean(GpsinfoMapper.class);;
    private Map<String, Object> caroneline = new HashMap<>();

    final String HISTORY = "_history";
    final String REAL = "_real";
    final String NORMAL = "normaldata_";
    final String WS = "_ws";
    /* (non-Javadoc)
     * @see io.netty.channel.SimpleChannelInboundHandler#channelRead0(io.netty.channel.ChannelHandlerContext, java.lang.Object)
     */


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BidrMessage msg) {
        RedisUtil redisUtil = BeanContext.getApplicationContext().getBean(RedisUtil.class);
        MessageType type = MessageType.fromInt(msg.getBidrHeader().getType());

        switch (type) {
            case Heart: {
                try {
                    if (msg.getRollingData() instanceof DeviceState) {
                        DeviceState deviceState = (DeviceState) msg.getRollingData();
                        String name = deviceState.getDeviceId();


                        redisUtil.set("caronlinetime_" + name, System.currentTimeMillis());

                        byte[] stateTrack = SysUtil.BuildResponseStream(MessageType.Heart.toInt(), deviceState.toBinary());

                        //将当前通道ID放在设备表中
                        deviceRegister.put(name, ctx.hashCode());

                        transMsg(name, stateTrack);
                    }
                } catch (Exception ex) {
                    log.error("Heart:" + ex.getMessage());
                }
                break;
            }
            case SysTrack:
            case SysTrackFOUR:
            case SysBulldozer: {
                try {
                    if (msg.getRollingData() instanceof RollingData) {
                        RollingData rollingData = (RollingData) msg.getRollingData();
                        if (StringUtils.isNotNull(rollingData)) {
                            //北方向
                            Double x0 = TrackConstant.x0;
                            //东方下
                            Double y0 = TrackConstant.y0;
                            Double zhuangY = -1 * (((rollingData.getCoordX() - x0) / kk) * Math.cos(alpha) + ((rollingData.getCoordY() - y0) / kk) * Math.sin(alpha));
                            Double zhuangX = ((rollingData.getCoordX() - x0) / kk) * Math.sin(alpha) + ((rollingData.getCoordY() - y0) / kk) * Math.cos(alpha);
                            rollingData.setZhuangX(zhuangX);
                            rollingData.setZhuangY(zhuangY);

                            //计算里程和偏距
                            try {
                                Mileage m = Mileage.getmileage();
                                double[] value = m.coord2Mileage2(rollingData.getCoordY(), rollingData.getCoordX(), "0");
                                rollingData.setZhuanghao(value[0]);
                                rollingData.setPianju(value[1]);
                            } catch (Exception e) {
                                System.out.println("里程偏距计算错误。");
                            }


                            /*严谨一点*/
                            /*lct===============修改开始*/

                            //实时
                            if (rollingData.getIshistory() == 0) {
                                // 根据 RollingData的vehicleID,将数据放入redis用于数据存入数据库。
                                // System.out.println("收到数据存入redis" + DateUtils.getTime());
                                redisUtil.leftPush(REAL + rollingData.getVehicleID() + REAL, rollingData);
                                redisUtil.leftPush(NORMAL + rollingData.getVehicleID() + NORMAL, rollingData);
                                if (!threadRegister.containsKey(NORMAL + rollingData.getVehicleID())) {
                                    //启动时传入车辆类型
                                    Car carinfo = carMapper.selectByPrimaryKey(Integer.parseInt(rollingData.getVehicleID()));
                                    //每一辆车启动一个数据处理线程
                                    RedisRightPopTaskBlockingforRealDoneCang blocking = new RedisRightPopTaskBlockingforRealDoneCang();

                                    blocking.setKey(rollingData.getVehicleID());
                                    blocking.setKeytype(GlobCache.cartableprfix[carinfo.getType()]);
                                    Thread thread = new Thread(blocking);
                                    thread.start();
                                    threadRegister.put(NORMAL + rollingData.getVehicleID(), blocking);
                                }

                            } else {//历史
                                // 根据 RollingData的_history+vehicleID+_history,将数据放入redis
                                redisUtil.leftPush(HISTORY + rollingData.getVehicleID() + HISTORY, rollingData);
                            }
                            //记录车辆上下线。
                            if (!caroneline.containsKey(rollingData.getVehicleID())) {

                                Car temp = new Car();
                                temp.setCarID(Integer.parseInt(rollingData.getVehicleID()));
                                temp.setUpdateTime(DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", new Date()));
                                temp.setStatus("在线");
                                carMapper.updateByPrimaryKeySelective(temp);

                                //第一次首先将该车未更新离线时间的数据更新。
                                List<Date> all = carMapper.getcaronlinetime(Integer.parseInt(rollingData.getVehicleID()));
                                if (all.size() > 0) {
                                    Date onlinetime = all.get(0);
                                    long ontimepass = DataTimeUtil.getdate2dateminute(onlinetime, new Date());
                                    carMapper.updatecaroffline(Integer.parseInt(rollingData.getVehicleID()), new Date(), (double) ontimepass);
                                }

                                carMapper.insertcaronline(Integer.parseInt(rollingData.getVehicleID()), new Date());
                                caroneline.put(rollingData.getVehicleID(), new Date());
                            }

                        }
                        /*lct===============修改结束*/

//                        log.info("将数据放入Redis");
                        //回复确认消息
                        SysRate sysRate = new SysRate();
                        String uuid = rollingData.getLayerID() + "-" + rollingData.getMaterialname();
//                        log.info("UUID:" + uuid);
                        sysRate.setUuid(uuid);
                        sysRate.setOwnerCar(rollingData.getVehicleID());
                        sysRate.setOtherCar(SERVER_ALIAS);
                        sysRate.setOrderNum(rollingData.getOrderNum());
                        insertSysRate(sysRate);
                        byte[] bytes = SysUtil.BuildResponseStream(MessageType.SysRate.toInt(), sysRate.toBinary());
                        ByteBuf buf = Unpooled.buffer(bytes.length);
                        buf.writeBytes(bytes);
                        ctx.writeAndFlush(buf);

                        transRollingData(rollingData);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            }
            case SysRate: {
                try {
                    if (msg.getRollingData() instanceof SysRate) {
                        SysRate sysRate = (SysRate) msg.getRollingData();
//                        insertSysRate(sysRate);
                        String name = sysRate.getOwnerCar();
                        Integer channelId = deviceRegister.get(name);
                        if (channelId != null) {
                            Channel channel = channelRegister.get(channelId);
                            if (channel != null) {
                                byte[] bytes = SysUtil.BuildResponseStream(MessageType.SysRate.toInt(), sysRate.toBinary());

                                ByteBuf buf = Unpooled.buffer(bytes.length);
                                buf.writeBytes(bytes);
                                ctx.writeAndFlush(buf);
                            }
                        }
                    }
                } catch (Exception ex) {
                    log.error("SysRate:" + ex.getMessage());
                }
                break;
            }
            case SysCar:{
                //写入人员识别卡数据库
                try{
                    if (msg.getRollingData() instanceof Gpsinfo) {
                        Gpsinfo gpsInfo = (Gpsinfo) msg.getRollingData();
                        gpsMapper.insertGpsinfo(gpsInfo);
                        System.out.println("人员识别卡:" + gpsInfo.toString());
                    }
                }catch (Exception ex){
                    System.out.println("channelRead0 SysCar Exception:" + ex.getMessage());
                }
                break;
            }
        }
    }

    /**
     * 转发心跳
     *
     * @param url:转发url
     * @param bytes:转发内容
     */
    private void transMsg(String url, byte[] bytes) {
        //遍历设备表 寻找是否有当前车辆的通道ID
        Iterator<Map.Entry<String, Integer>> iterable = deviceRegister.entrySet().iterator();
        while (iterable.hasNext()) {
            Map.Entry<String, Integer> entry = iterable.next();
            String key = entry.getKey();
            //将当前信息转发到除了当前车辆外所有车辆
            if (!url.equals(key)) {
                //设备表中读取车辆的通道ID
                Integer channelId = entry.getValue();
                if (channelId != null) {
                    //根据该ID在通道表中找对应通道
                    Channel channel = channelRegister.get(channelId);
                    if (channel != null) {
                        //通道表中存在该ID的通道 进行数据发送
                        if (channel.isActive()) {
                            ByteBuf buf = Unpooled.buffer(bytes.length);
                            buf.writeBytes(bytes);
                            channel.writeAndFlush(buf);
                        }
                    } else {
                        //通道表中不存在该ID的通道-将设备表中该设备移除，默认该设备下线
                        iterable.remove();

                        //关闭改车线程
                     //   threadRegister.get(key).stopThread(true);
                        //移除改车缓存线程
                     //   threadRegister.remove(key);
                        RedisUtil redisUtil = BeanContext.getApplicationContext().getBean(RedisUtil.class);
                        //移除redis缓存信息
                        redisUtil.del(WS + key + WS);
                        log.info("该车辆在设备表中存在,但对应ChannelId在通道表中无法找到,主动下线");
                    }
                }
            }
        }
    }

    /**
     * 转发轨迹点并写入SysRate
     */
    private void transRollingData(RollingData rollingData) {
        //遍历设备表 寻找是否有当前车辆的通道ID
        Iterator<Map.Entry<String, Integer>> iterable = deviceRegister.entrySet().iterator();
        String url = rollingData.getVehicleID();
        int layer = rollingData.getLayerID();
        int material = rollingData.getMaterialname();
        int orderNum = rollingData.getOrderNum();
        while (iterable.hasNext()) {
            Map.Entry<String, Integer> entry = iterable.next();
            String key = entry.getKey();
            //将当前信息转发到除了当前车辆外所有车辆
            if (!url.equals(key)) {
                //设备表中读取车辆的通道ID
                Integer channelId = entry.getValue();
                if (channelId != null) {
                    //根据该ID在通道表中找对应通道
                    Channel channel = channelRegister.get(channelId);
                    if (channel != null) {
                        //通道表中存在该ID的通道 进行数据发送
                        if (channel.isActive()) {
                            SysRate sysRate = new SysRate();
                            String uuid = layer + "-" + material;
                            sysRate.setUuid(uuid);
                            sysRate.setOwnerCar(url);
                            sysRate.setOtherCar(key);
                            sysRate.setOrderNum(orderNum);
                            insertSysRate(sysRate);

                            byte[] bytes = SysUtil.BuildResponseStream(MessageType.SysTrack.toInt(), rollingData.toBinary());
                            ByteBuf buf = Unpooled.buffer(bytes.length);
                            buf.writeBytes(bytes);
                            channel.writeAndFlush(buf);

                        }
                    } else {
                        //通道表中不存在该ID的通道-将设备表中该设备移除，默认该设备下线
                        iterable.remove();
                        log.info("该车辆在设备表中存在,但对应ChannelId在通道表中无法找到,主动下线");
                    }
                }
            }
        }
    }

    /**
     * 空闲次数
     */
    private int idle_count = 1;
    /* 发送次数 */
    /**
     * 超时处理
     * 如果5秒没有接受客户端的心跳，就触发;
     * 如果超过两次，则直接关闭;
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object obj) {
        //todo:该事件触发条件为断网
        if (obj instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) obj;
            if (IdleState.READER_IDLE.equals(event.state())) {  //如果读通道处于空闲状态，说明没有接收到心跳命令
                System.out.println("已经5秒没有接收到客户端的信息了");
                if (idle_count > 2) {
                    System.out.println("关闭这个不活跃的channel");
                    int channelId = ctx.hashCode();
                    //根据ChanneId在设备表中找到对应设备
                    Iterator<Map.Entry<String, Integer>> deviceIterator = deviceRegister.entrySet().iterator();
                    while (deviceIterator.hasNext()) {
                        Map.Entry<String, Integer> entry = deviceIterator.next();
                        int deviceChannelId = entry.getValue();
                        if (deviceChannelId == channelId) {
                            //如果设备表中存在该通道ID，将对应设备移除
                            log.info("设备移除:" + entry.getKey());
                            deviceIterator.remove();

                            String key = entry.getKey();
                            //关闭改车线程
                        //    threadRegister.get(key).stopThread(true);
                            //移除改车缓存线程
                           // threadRegister.remove(key);

                            RedisUtil redisUtil = BeanContext.getApplicationContext().getBean(RedisUtil.class);
                            //移除redis缓存信息

                            redisUtil.del(WS + key + WS);

                            CarService carservce = BeanContext.getApplicationContext().getBean(CarService.class);
                            Car temp = new Car();
                            temp.setCarID(Integer.parseInt(entry.getKey()));
                            temp.setUpdateTime(DateUtils.getTime());
                            temp.setStatus("离线");
                            carservce.updateCar(temp);

                        }
                    }
                    //在通道表中将channelId移除并关闭通道
                    Channel channel = ctx.channel();
                    channelRegister.remove(channelId);
                    channel.close();
                }
                idle_count++;
            }
        } else {
            try {
                super.userEventTriggered(ctx, obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        /*RedisUtil redisUtil = ServerApplication.getBean(RedisUtil.class);
        redisUtil.set("loc2","loc2");
        ServerApplication.destroy(RedisUtil.class.getName());*/
        // 简单地打印出server接收到的消息
        String name = ctx.toString();
        String regex = "\\[(.*?)]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);
        while (matcher.find()) {
            String str = matcher.group(1);
            String[] arr = str.split(","); // 用,分割
            String id = arr[0];
            log.info("建立个新连接！" + "连接名为：" + id);
            //新起个线程发送心跳
            SendHeartBeat sendHeartBeat = new SendHeartBeat();
            sendHeartBeat.setName(id);
            sendHeartBeat.setChannel(ctx.channel());
           /* Thread thread = new Thread(sendHeartBeat);
            thread.setName(id);
            thread.start();*/
            thread = new Thread(sendHeartBeat);
            thread.start();
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        thread.stop();
        //todo:该事件触发条件为软件关闭
        log.info("客户端被移除");
        int hashcode = ctx.hashCode();
        Iterator<Map.Entry<Integer, Channel>> channelIterator = channelRegister.entrySet().iterator();
        while (channelIterator.hasNext()) {
            Map.Entry<Integer, Channel> channel = channelIterator.next();
            //当前断开客户端的ctx的id在registers中被查到
            if (hashcode == channel.getKey()) {
                //设备表中存在记录,进行下线通知，然后移除该条记录
                Iterator<Map.Entry<String, Integer>> iterator = deviceRegister.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, Integer> entry = iterator.next();
                    String device = entry.getKey();
                    Integer channelId = entry.getValue();
                    if (channelId == hashcode) {
                        //进行下线通知
                        DeviceState deviceState = new DeviceState();
                        deviceState.setDeviceId(device);
                        deviceState.setState(1);
                        byte[] bytes = SysUtil.BuildResponseStream(MessageType.Heart.toInt(), deviceState.toBinary());
                        transMsg(device, bytes);
                        //移除该条记录
                        iterator.remove();
                        try {
                            //关闭改车线程
                            //	threadRegister.get(NORMAL +device).stopThread(true);
                            //移除改车缓存线程
                            //	threadRegister.remove(NORMAL +device);
                            //更新车辆下线时间。
                            List<Date> all = carMapper.getcaronlinetime(Integer.parseInt(device));
                            if (all.size() > 0) {
                                Date onlinetime = all.get(0);
                                long ontimepass = DataTimeUtil.getdate2dateminute(onlinetime, new Date());
                                carMapper.updatecaroffline(Integer.parseInt(device), new Date(), (double) ontimepass);
                                caroneline.remove(device);
                            }
                            Car temp = new Car();
                            temp.setCarID(Integer.parseInt(device));
                            temp.setUpdateTime(DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", new Date()));
                            temp.setStatus("离线");
                            carMapper.updateByPrimaryKeySelective(temp);

                        } catch (Exception e) {
                            System.out.println(e.getLocalizedMessage());
                        }

                        break;

                    }
                }
                //更新车辆下线记录


                //将当前通道移除 并断开
                channelIterator.remove();
                Channel realChannel = channel.getValue();
                try {
                    realChannel.closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        //todo:该事件触发条件为软件打开 紧接着触发channelActive
        log.info("收到了新的客户端");
        int hashcode = ctx.hashCode();


        //将新客户端连接通道ID和通道放在通道表中
        channelRegister.put(hashcode, ctx.channel());
    }

    /**
     * 插入SysRate记录到数据库
     */
    public void insertSysRate(SysRate sysRate) {
        SysRateMapper sysRateMapper = BeanContext.getApplicationContext().getBean(SysRateMapper.class);
        sysRateMapper.insertSysRate(SYSRATE_TABLE, sysRate);
    }


}
