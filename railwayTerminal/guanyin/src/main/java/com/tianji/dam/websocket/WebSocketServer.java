package com.tianji.dam.websocket;


import java.io.IOException;
import java.util.LinkedList;import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.domain.DamsConstruction;
import com.tianji.dam.domain.MatrixItem;
import com.tianji.dam.domain.RollingData;
import com.tianji.dam.domain.StoreHouseMap;
import com.tianji.dam.mapper.TDamsconstructionMapper;
import com.tianji.dam.thread.RealTimeRedisRightPopTaskBlockingZhuang;
import com.tianji.dam.utils.RedisUtil;

import lombok.extern.slf4j.Slf4j;

//ws://193.112.170.179:80/springboot/websocket/ws?storehouseName='+storehouseName)
@Slf4j
@ServerEndpoint(value = "/websocket/ws/{storehouseName}/{carId}/{timestamp}/{username}", configurator = GetHttpSessionConfigurator.class)
@Component
@Lazy(true)
public class WebSocketServer {
	 
   // 
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
    private String carId;
    private String timestamp;
    final String WS = "_ws";
    //用户标识
    private String key;
    @Autowired
    private static StoreHouseMap storeHouseMap;
    
   

    //实时数据
//    Map<String, List<RollingData>> realTimeDataMap  = storeHouseMap.getRealTimeDataList();
    Map<String, Queue<RollingData>> realTimeDataMap  = storeHouseMap.getRealTimeDataList();

    private static Map<String, List<String>> storehouseIdUsernameListMap = storeHouseMap.getStorehouseIdUsernameListMap();
    private static Map<String, MatrixItem[][]> storeHouseMaps2RollingData = storeHouseMap.getStoreHouses2RollingData();
    private static Map<String, List<RealTimeRedisRightPopTaskBlockingZhuang>> threadMap = storeHouseMap.getRealTimeRedisRightPopTaskBlockingZhuangList();

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

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private HttpSession httpSession;
    private String storehouseName;
    private List<String> driverList =new LinkedList<>();
    /*private String sessionId;*/

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("storehouseName") String storehouseName, @PathParam("carId") String carId, @PathParam("timestamp") String timestamp, @PathParam("username") String username, EndpointConfig config) {
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        /* this.sessionId = httpSession.getId();*/
        this.storehouseName = storehouseName;
        this.carId = carId;
        this.timestamp = timestamp;
        key = storehouseName + ":" + carId + ":" + timestamp;
        /*判断是否有storehouseName的key*/
        if (storehouseIdUsernameListMap.containsKey(storehouseName)) {
            /*如果有*/
            storehouseIdUsernameListMap.get(storehouseName).add(key);
           
        } else {
            /*如果没有*/
            List<String> newList = new LinkedList<String>();
            newList.add(key);
            storehouseIdUsernameListMap.put(storehouseName, newList);
            driverList.add(carId);
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
            //log.error("websocket IO异常");
        }
        logger.error("有一连接建立！当前在线人数为" + getOnlineCount());
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
        logger.error("有一连接关闭！当前在线人数为" + getOnlineCount());
        /**/
        users.remove(key);
        DamsConstruction damsConstruction = damsConstructionMapper.selectByPrimaryKey(Integer.parseInt(storehouseName));
        /*storehouseIdUsernameListMap.get(storehouseName).remove(key);*/
        storehouseIdUsernameListMap.get(storehouseName).clear();//直接清空对应的集合
        logger.info("============清空对应的通道===============");
        if (storehouseIdUsernameListMap.get(storehouseName).size() < 1) {
            storeHouseMaps2RollingData.remove(damsConstruction.getTablename());
            storehouseIdUsernameListMap.remove(storehouseName);
            /*遍历线程池 终止线程*/

            List<RealTimeRedisRightPopTaskBlockingZhuang> threadList = threadMap.get(storehouseName);
            if(null!=threadList) {
            	for (int i = 0; i < threadList.size(); i++) {
            		RealTimeRedisRightPopTaskBlockingZhuang thread = (RealTimeRedisRightPopTaskBlockingZhuang) threadList.get(i);
//                thread.interrupt(); 
            		//停止线程
            		thread.stopThread(true);
            	}
            	
            }
            
            /*删除线程map*/
            threadMap.remove(storehouseName);
            log.error("删除线程");

            //清空实时数据
            realTimeDataMap.clear();
 
            /*删除以storehouseName开头的rediskey*/
            RedisUtil redisUtil = BeanContext.getApplicationContext().getBean(RedisUtil.class);
            Set<String> rkeys = redisUtil.keys("*");
            for (String str : rkeys) {
                if (isStartWithNumber(str)) {
                    if (!str.equalsIgnoreCase("1") && !str.equalsIgnoreCase("2") && !str.equalsIgnoreCase("3")) {
                        String[] sourceArray = str.split("-");
                        if (sourceArray[0].equals(storehouseName)) {
                            redisUtil.del(str);
                        }
                    }
                }
            }
           if(null!=driverList) {
        	   for (String carid : driverList) {
        		   redisUtil.del(WS+ carid+"_"+session.getId() +WS);
        	   }
        	   
           }
            
            logger.info("============删除redis实时通道数据key===============");
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
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
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
