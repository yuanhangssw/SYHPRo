package com.tianji.dam.websocket;


import com.tianji.dam.domain.MatrixItem;
import com.tianji.dam.domain.StoreHouseMap;
import com.tianji.dam.thread.HistoryCarDataTaskBlockingList;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义车辆
 */
@Slf4j
@ServerEndpoint(value = "/websocket/historyCar/{carId}/{timestamp}", configurator = GetHttpSessionConfigurator.class)
@Component
public class WebSocketServerForHistoryCar {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketServerForHistoryCar.class);
    public String carId;
    public String timestamp;
    private String username;
    //用户标识
    private String key;

    @Autowired
    private static StoreHouseMap storeHouseMap;
    private static Map<String, List<HistoryCarDataTaskBlockingList>> threadMap = storeHouseMap.getHistoryCarDataTaskBlockingList();

    private static final String CLIENT_ID = "username";
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    private static final ConcurrentHashMap<String, Session> users;

    static {
        users = new ConcurrentHashMap<String, Session>();
    }

    public static ConcurrentHashMap<String, Session> getUsersMap() {
        return users;
    }

    //开辟内存空间做缓存
    private static ConcurrentHashMap<Long, MatrixItem[][]> cache = new ConcurrentHashMap<>();
    private static List<Integer> long2Cols = new LinkedList<>();//列
    private static List<Integer> long2Rows = new LinkedList<>();//行

    public static ConcurrentHashMap getCache() {
        return cache;
    }

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private HttpSession httpSession;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("carId") String carId, @PathParam("timestamp") String timestamp, EndpointConfig config) {
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        this.carId = carId;
        this.timestamp = timestamp;

        key = carId + ":" + timestamp;
        users.put(key, session);
        log.info(key);
        addOnlineCount();           //在线数加1
        logger.info("有一连接建立！当前在线人数为" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        key = carId + ":" + timestamp;
        subOnlineCount();           //在线数减1
        logger.error("有一连接关闭！当前在线人数为" + getOnlineCount());
        /**/
        users.remove(key);
        /*遍历线程池 终止线程*/
        List<HistoryCarDataTaskBlockingList> threadList = threadMap.get("HISTORYCAR");
        for (int i = 0; i < threadList.size(); i++) {
            HistoryCarDataTaskBlockingList thread = threadList.get(i);
            thread.breakthread();
            log.error("删除线程" + i);
        }
        /*删除线程map*/
        threadMap.remove("HISTORYCAR");
        log.error("删除线程");
        if (onlineCount == 0) {
            /*暂无逻辑*/
        }

    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("收到来自窗口" + carId + "的信息:" + message);
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
        onClose(session);
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {

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
        WebSocketServerForHistoryCar.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        if (WebSocketServerForHistoryCar.onlineCount < 0) {
            WebSocketServerForHistoryCar.onlineCount = 0;
        } else {
            WebSocketServerForHistoryCar.onlineCount--;
        }
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
