package com.tianji.dam.websocket;


import com.alibaba.fastjson.JSONObject;
import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.bean.GlobCache;
import com.tianji.dam.domain.MatrixItem;
import com.tianji.dam.utils.GenerateRedisKey;
import com.tianji.dam.utils.RedisUtil;
import com.tianji.dam.utils.RidUtil;
import com.tj.common.utils.DateUtils;
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
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@ServerEndpoint(value = "/websocket/beforerealtime/{cartype}", configurator = GetHttpSessionConfigurator.class)
@Component
public class WebSocketServerBeforeRealTime {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServerBeforeRealTime.class);
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    @Autowired
    private BeanContext beancontext;
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private HttpSession httpSession;
    private Session session;

    private Integer cartype;

    private static Map<Long, Integer> showtype = new HashMap<>();
    private int showtype_int;

    public static Map<Long, Integer> getShowtype() {
        return showtype;
    }

    public static void setShowtype(Map<Long, Integer> showtype) {
        WebSocketServerBeforeRealTime.showtype = showtype;
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
        WebSocketServerBeforeRealTime.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        if (WebSocketServerBeforeRealTime.onlineCount < 0) {
            WebSocketServerBeforeRealTime.onlineCount = 0;
        } else {
            WebSocketServerBeforeRealTime.onlineCount--;
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

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig config, @PathParam("cartype") Integer cartype) {
        this.session = session;
        this.cartype = cartype;
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());


        try {
            sendMessage("连接成功");
        } catch (Exception e) {

        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {


    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("收到来自窗口" + session.getId() + "的信息:" + message);

        try {

            //遍历临时缓存 然后更新实时缓存 再清除临时缓存
            RedisUtil redis = BeanContext.getApplicationContext().getBean(RedisUtil.class);
            Set<String> keys = redis.keys(GenerateRedisKey.realTimeAllRidKeybycartype(GlobCache.cartableprfix[cartype]));
            if (null != keys && keys.size() > 0) {

                System.out.println("获取到Rid:" + keys.size());//第一次进入时报错
                ExecutorService exec = Executors.newFixedThreadPool(keys.size());
                List<Callable<JSONObject>> tasks = new LinkedList<>();
                Callable<JSONObject> task = null;
                for (String key : keys) {
                    ConcurrentHashMap<Long, MatrixItem[][]> cacheData = new ConcurrentHashMap<>();
                    List<Integer> cols = new LinkedList<>();//列
                    List<Integer> rows = new LinkedList<>();//行
                    long start = System.currentTimeMillis();
                    MatrixItem[][] matrixItems = (MatrixItem[][]) redis.get(key);
                    String rid = GenerateRedisKey.splitRealTimeStorehouse2(key);
                    long lRid = Long.parseLong(rid);
                    cols.add(RidUtil.long2Col(lRid));
                    rows.add(RidUtil.long2Row(lRid));
                   // cacheData.put(lRid, matrixItems);
//                    task = new DrawPicBeforeRealTimeThread(cacheData, cols, rows, session, cartype);
//                    tasks.add(task);
                }
                System.out.println("线程池任务开始执行》" + DateUtils.getTime());
                List<Future<JSONObject>> results = exec.invokeAll(tasks);
                for (Future<JSONObject> future : results) {
                    future.get();
                }
                exec.shutdown();
                sendMessage("绘图完毕");
            } else {
                sendMessage("绘图完毕");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("websocket IO异常");
            try {
                sendMessage("连接失败");
            } catch (Exception e2) {

            }

        }

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
    public synchronized void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }
}
