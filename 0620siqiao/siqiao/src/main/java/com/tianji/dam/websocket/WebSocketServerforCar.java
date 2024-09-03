package com.tianji.dam.websocket;


import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.domain.StoreHouseMap;
import com.tianji.dam.mapper.TDamsconstructionMapper;
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
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@ServerEndpoint(value = "/websocket/ws/{carId}/{timestamp}", configurator = GetHttpSessionConfigurator.class)
@Component
@Lazy()
public class WebSocketServerforCar {

    //
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServerforCar.class);
    private String carId;
    private String timestamp;
    final String WS = "_ws";
    //用户标识
    private String key;
    @Autowired
    private static StoreHouseMap storeHouseMap;


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


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("carId") String carId, @PathParam("timestamp") String timestamp, EndpointConfig config) {
        this.session = session;
        //与某个客户端的连接会话，需要通过它来给客户端发送数据
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        this.carId = carId;
        this.timestamp = timestamp;
        key = storehouseName + ":" + carId + ":" + timestamp;
        System.out.println(session.getId() + ">车辆" + carId + "会话建立。");
        users.put(key, session);
        //session.sendMessage(new TextMessage("成功建立socket连接"));
        //  log.info(key);
        //log.info(session.toString());
        try {
            System.out.println(session.getId() + "连接成功");
            sendMessage("连接成功");
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
        /*key =sessionId + ":"+storehouseName+":"+carId+":"+timestamp;*/
        key = storehouseName + ":" + carId + ":" + timestamp;
        // subOnlineCount();           //在线数减1
        logger.error("有一连接关闭！当前在线人数为" + key);
        /**/
        users.remove(key);

    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("收到来自窗口" + storehouseName + "的信息:" + message);
        //群发消息
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {

        logger.error("发生错误");
        error.printStackTrace();
        /* String key =sessionId + ":"+storehouseName +":"+carId;*/
        String key = storehouseName + ":" + carId;
        subOnlineCount();           //在线数减1
        logger.error("有一连接关闭！当前在线人数为" + getOnlineCount());
        users.remove(key);
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
        WebSocketServerforCar.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServerforCar.onlineCount--;
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
