package com.tianji.dam.websocket;


import com.alibaba.fastjson.JSONObject;
import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.bean.GlobCache;
import com.tianji.dam.domain.Car;
import com.tianji.dam.domain.RollingData;
import com.tianji.dam.domain.StoreHouseMap;
import com.tianji.dam.mapper.CarMapper;
import com.tianji.dam.mapper.TDamsconstructionMapper;
import com.tianji.dam.mileageutil.Mileage;
import com.tianji.dam.thread.RedisRightPopTaskBlockingforRealDoneCang;
import com.tianji.dam.utils.DataTimeUtil;
import com.tianji.dam.utils.RedisUtil;
import com.tianji.dam.utils.TrackConstant;
import com.tj.common.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@ServerEndpoint(value = "/websocket/car/upload/{carId}")
@Component
@Lazy()
public class WebSocketServerforCarUploadData {

    //
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServerforCarUploadData.class);
    private String carId;
    private String timestamp;
    final String WS = "_carupdata";
    //用户标识
    private String key;
    @Autowired
    private static StoreHouseMap storeHouseMap;

    RedisUtil redisUtil = BeanContext.getApplicationContext().getBean(RedisUtil.class);
    private static final String CLIENT_ID = "username";
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    private static final ConcurrentHashMap<String, Session> users;

    static {
        users = new ConcurrentHashMap<>();
    }

    public static ConcurrentHashMap<String, Session> getUsersMap() {
        return users;
    }

    private String storehouseName;
    private List<String> driverList = new LinkedList<>();
    private Session session;
    final String HISTORY = "_history";
    final String REAL = "_real";
    final String NORMAL = "normaldata_";
    private static Double alpha = TrackConstant.alpha;//顺时针旋转角度，弧度制d
    private static Double kk = TrackConstant.kk;//缩放比

    private ConcurrentHashMap<String, RedisRightPopTaskBlockingforRealDoneCang> threadRegister = WebsocketServerForPosition.getThreadRegister();
    private CarMapper carMapper = BeanContext.getApplicationContext().getBean(CarMapper.class);

    private Map<String, Object> caroneline = new HashMap<>();
    Mileage m = Mileage.getmileage();
    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("carId") String carId,  EndpointConfig config) {
        this.session = session;
        //与某个客户端的连接会话，需要通过它来给客户端发送数据
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        this.carId = carId;
        System.out.println(session.getId() + ">车辆" + carId + "会话建立。");
        users.put(key, session);
        redisUtil.set("caronlinetime_" + carId, System.currentTimeMillis());
        //session.sendMessage(new TextMessage("成功建立socket连接"));
        //  log.info(key);
        //log.info(session.toString());
        try {
            System.out.println(session.getId() + "连接成功");
            sendMessage("连接成功");

            //记录车辆上下线。
            if (!caroneline.containsKey(carId)) {
                Car temp = new Car();
                temp.setCarID(Integer.parseInt(carId));
                temp.setUpdateTime(DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", new Date()));
                temp.setStatus("在线");
                carMapper.updateByPrimaryKeySelective(temp);

                //第一次首先将该车未更新离线时间的数据更新。
                List<Date> all = carMapper.getcaronlinetime(Integer.parseInt(carId));
                if (all.size() > 0) {
                    Date onlinetime = all.get(0);
                    long ontimepass = DataTimeUtil.getdate2dateminute(onlinetime, new Date());
                    carMapper.updatecaroffline(Integer.parseInt(carId), new Date(), (double) ontimepass);
                }

                carMapper.insertcaronline(Integer.parseInt(carId), new Date());
                caroneline.put(carId, new Date());
            }



        } catch (IOException e) {
            //log.error("websocket IO异常");
        }
        //logger.error("有一连接建立！当前在线人数为" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        TDamsconstructionMapper damsConstructionMapper = BeanContext.getApplicationContext().getBean(TDamsconstructionMapper.class);
        System.out.println(session.getId() + "连接断开。");
        if (caroneline.containsKey(carId)) {
            Car temp = new Car();
            temp.setCarID(Integer.parseInt(carId));
            temp.setUpdateTime(DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", new Date()));
            temp.setStatus("离线");
            carMapper.updateByPrimaryKeySelective(temp);

            //第一次首先将该车未更新离线时间的数据更新。
            List<Date> all = carMapper.getcaronlinetime(Integer.parseInt(carId));
            if (all.size() > 0) {
                Date onlinetime = all.get(0);
                long ontimepass = DataTimeUtil.getdate2dateminute(onlinetime, new Date());
                carMapper.updatecaroffline(Integer.parseInt(carId), new Date(), (double) ontimepass);
            }
            caroneline.remove(carId);
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {

        RollingData rollingData = JSONObject.parseObject(message, RollingData.class);
        if (null != rollingData && null != rollingData.getVehicleID()) {
            Double x0 = TrackConstant.x0;
            //东方下
            Double y0 = TrackConstant.y0;
            Double zhuangY = -1 * (((rollingData.getCoordX() - x0) / kk) * Math.cos(alpha) + ((rollingData.getCoordY() - y0) / kk) * Math.sin(alpha));
            Double zhuangX = ((rollingData.getCoordX() - x0) / kk) * Math.sin(alpha) + ((rollingData.getCoordY() - y0) / kk) * Math.cos(alpha);
            rollingData.setZhuangX(zhuangX);
            rollingData.setZhuangY(zhuangY);

            //计算里程和偏距
            try {

                double[] value = m.coord2Mileage2(rollingData.getCoordY(), rollingData.getCoordX(), "0");
                rollingData.setZhuanghao(value[0]);
                rollingData.setPianju(value[1]);
            } catch (Exception e) {
                System.out.println("里程偏距计算错误。");
            }

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
        }

    }


    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {

        System.out.println(session.getId() + "websocket连接异常。车辆："+carId);
        error.printStackTrace();

    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {

        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            System.out.println(session);
            e.printStackTrace();
        }
    }

    /**
     * 群发自定义消息
     */
    public static void sendInfo(String message, @PathParam("storehouseName") String storehouseName) throws IOException {
        // log.info("推送消息到窗口"+sid+"，推送内容:"+message);

    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServerforCarUploadData.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServerforCarUploadData.onlineCount--;
    }

    public static boolean isStartWithNumber(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str.charAt(0) + "");
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
}
