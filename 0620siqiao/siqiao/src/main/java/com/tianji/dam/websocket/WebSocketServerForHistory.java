package com.tianji.dam.websocket;


import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.domain.DamsConstruction;
import com.tianji.dam.domain.MatrixItem;
import com.tianji.dam.domain.StoreHouseMap;
import com.tianji.dam.mapper.TDamsconstructionMapper;
import com.tianji.dam.thread.HistoryDataTaskBlockingList;
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

@Slf4j
@ServerEndpoint(value = "/websocket/history/{storehouseName}/{carId}/{timestamp}", configurator = GetHttpSessionConfigurator.class)
@Component
public class WebSocketServerForHistory {
    @Autowired
    private BeanContext beancontext;

    TDamsconstructionMapper damsConstructionMapper = beancontext.getApplicationContext().getBean(TDamsconstructionMapper.class);
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServerForHistory.class);
    public String carId;
    public String timestamp;
    private String username;
    //用户标识
    private String key;
    @Autowired
    private static StoreHouseMap storeHouseMap;
    private static Map<String, List<String>> storehouseIdUsernameListMap = storeHouseMap.getHistoryUsernameListMap();
    private static Map<String, MatrixItem[][]> storeHouseMaps2RollingData = storeHouseMap.getHistory2RollingData();
    private static Map<String, List<HistoryDataTaskBlockingList>> threadMap = storeHouseMap.getHistoryDataTaskBlockingList();

    private static final String CLIENT_ID = "username";
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    private static final ConcurrentHashMap<String, Session> users;

    static {
        users = new ConcurrentHashMap<String, Session>();
    }

    public static ConcurrentHashMap getUsersMap() {
        return users;
    }

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private HttpSession httpSession;
    private Session session;
    public String storehouseName;
    /*private String sessionId;*/

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("storehouseName") String storehouseName, @PathParam("carId") String carId, @PathParam("timestamp") String timestamp, EndpointConfig config) {
            this.session =session;
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        /* this.sessionId = httpSession.getId();*/
        this.storehouseName = storehouseName;
        this.carId = carId;
        this.timestamp = timestamp;
        this.username = username;
        key = storehouseName +":" + timestamp;
        /*判断是否有storehouseName的key*/
        if (storehouseIdUsernameListMap.containsKey(storehouseName)) {
            /*如果有*/
            storehouseIdUsernameListMap.get(storehouseName).add(key);
        } else {
            /*如果没有*/
            List newList = new LinkedList<String>();
            newList.add(key);
            storehouseIdUsernameListMap.put(storehouseName, newList);
        }

        users.put(key, session);
        //session.sendMessage(new TextMessage("成功建立socket连接"));
        log.info(key);
        log.info(session.toString());
        addOnlineCount();           //在线数加1
        this.storehouseName = storehouseName;
        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            log.error("websocket IO异常");
        }
        logger.info("有一连接建立！当前在线人数为" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        /*key =sessionId + ":"+storehouseName+":"+carId+":"+timestamp;*/
        key = storehouseName + ":" + carId + ":" + timestamp;
        subOnlineCount();           //在线数减1
        logger.error("有一连接关闭！当前在线人数为" + getOnlineCount());
        /**/
        users.remove(key);
        DamsConstruction damsConstruction = damsConstructionMapper.selectByPrimaryKey(Integer.parseInt(storehouseName));
        storehouseIdUsernameListMap.get(storehouseName).remove(key);
        if (storehouseIdUsernameListMap.get(storehouseName).size() < 1) {
            storeHouseMaps2RollingData.remove(damsConstruction.getTablename());
            storehouseIdUsernameListMap.remove(storehouseName);
            /*遍历线程池 终止线程*/
            List<HistoryDataTaskBlockingList> threadList = threadMap.get(timestamp);
            for (int i = 0; i < threadList.size(); i++) {
                HistoryDataTaskBlockingList thread = (HistoryDataTaskBlockingList) threadList.get(i);
                thread.stopThread(true);
                log.error("删除线程" + i);
            }
            /*删除线程map*/
            threadMap.remove(timestamp);
        }
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
        onClose(session);
        /* String key =sessionId + ":"+storehouseName +":"+carId;*/
        /*String key = storehouseName + ":" + carId;
        subOnlineCount();           //在线数减1
        logger.error("有一连接关闭！当前在线人数为" + getOnlineCount());
        users.remove(key);*/
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
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
        WebSocketServerForHistory.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        if (WebSocketServerForHistory.onlineCount < 0) {
            WebSocketServerForHistory.onlineCount = 0;
        } else {
            WebSocketServerForHistory.onlineCount--;
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
