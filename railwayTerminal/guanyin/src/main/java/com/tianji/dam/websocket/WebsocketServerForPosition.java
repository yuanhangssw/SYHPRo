package com.tianji.dam.websocket;

import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.domain.StoreHouseMap;
import com.tianji.dam.model.RealTimeRedisDataModel;
import com.tianji.dam.thread.RedisRightPopTaskBlockingforRealDoneCang;
import com.tianji.dam.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@ServerEndpoint(value = "/websocket/position")
@Component
public class WebsocketServerForPosition {
    String id = "wang";
    private static HashMap<String, Set<Session>> positionUses;

    static {
        positionUses = new HashMap<String, Set<Session>>();
    }

    private static HashMap<String, Session> sokectuser = new HashMap<>();
    final String WS = "_ws";

    //用于session断开连接时，清除所有缓存的实时推送数据。
    private static ConcurrentHashMap<String, List<String>> sessioncarlist;

    static {

        sessioncarlist = new ConcurrentHashMap<String, List<String>>();

    }

    public static ConcurrentHashMap<String, List<String>> getSessioncarlist() {
        return sessioncarlist;
    }

    public static void setSessioncarlist(ConcurrentHashMap<String, List<String>> sessioncarlist) {
        WebsocketServerForPosition.sessioncarlist = sessioncarlist;
    }


    public static HashMap<String, Session> getSokectuser() {
        return sokectuser;
    }

    public static void setSokectuser(HashMap<String, Session> sokectuser) {
        WebsocketServerForPosition.sokectuser = sokectuser;
    }

    public static Map<String, Set<Session>> getPositionUses() {
        return positionUses;
    }

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebsocketServerForPosition> webSocketSet = new CopyOnWriteArraySet<WebsocketServerForPosition>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    private static List<String> positionCang;

    static ConcurrentHashMap<String, RedisRightPopTaskBlockingforRealDoneCang> threadRegister;

    static {
        threadRegister = new ConcurrentHashMap<>();

    }

    public static ConcurrentHashMap<String, RedisRightPopTaskBlockingforRealDoneCang> getThreadRegister() {
        return threadRegister;
    }

    public static void setThreadRegister(ConcurrentHashMap<String, RedisRightPopTaskBlockingforRealDoneCang> threadRegister) {
        WebsocketServerForPosition.threadRegister = threadRegister;
    }

    static {
        positionCang = new LinkedList<>();
    }

    public static List<String> getPositionCang() {
        return positionCang;
    }

    @Autowired
    private static StoreHouseMap storeHouseMap;
    //实时数据缓存
    Map<String, RealTimeRedisDataModel> rollingDataMap = storeHouseMap.getRealTimeRedisDataModelMap();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(@PathParam("storehouseId") String storehouseId, Session session) {
        id = "wang";
        this.session = session;
        Set<Session> set = positionUses.get(id);
        if (set == null) {
            set = new HashSet<Session>();
        }
        set.add(session);
        positionUses.put(id, set);
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        log.info("有新连接加入！当前在线人数为" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        Set<Session> set = positionUses.get(id);
        set.remove(session);
        if (set.size() == 0) {
            positionUses.remove(id);
        } else {
            positionUses.put(id, set);
        }
        webSocketSet.remove(this);  //从set中删除

        subOnlineCount();           //在线数减1

        //清除
        //清除redis中该会话的所有缓存。
        List<String> allcar = sessioncarlist.get(session.getId());
        RedisUtil redisUtil = BeanContext.getApplicationContext().getBean(RedisUtil.class);
        if (null != allcar) {

            for (String string : allcar) {
                System.out.println("会话" + session.getId() + "断开、删除实时推送缓存。");
                redisUtil.del(WS + string + "_" + session.getId() + WS);

                RedisRightPopTaskBlockingforRealDoneCang removeblocking = threadRegister.get(string + "_" + session.getId());
           //     removeblocking.stopThread(true);
                threadRegister.remove(string + "_" + session.getId());

            }
        }

        positionCang.clear();
        sessioncarlist.remove(session.getId());
        sokectuser.remove(session.getId());

        rollingDataMap.clear();

        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("来自客户端的消息:" + message);

        //群发消息
        for (WebsocketServerForPosition item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.info("发生错误");
        error.printStackTrace();
    }


    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }


    /**
     * 群发自定义消息
     */
    public static void sendInfo(String message) throws IOException {
        for (WebsocketServerForPosition item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                continue;
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebsocketServerForPosition.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebsocketServerForPosition.onlineCount--;
    }
}
