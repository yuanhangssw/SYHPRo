package com.tianji.dam.websocket;


import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.bean.GlobCache;
import com.tianji.dam.domain.DamsConstruction;
import com.tianji.dam.domain.MatrixItemRedisReal;
import com.tianji.dam.mapper.TDamsconstructionMapper;
import com.tianji.dam.mapper.TDayTaskMapper;
import com.tianji.dam.thread.DrawPicBeforeRealTimeThreadCang;
import com.tianji.dam.utils.GenerateRedisKey;
import com.tianji.dam.utils.RedisUtil;
import com.tianji.dam.utils.RidUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@ServerEndpoint(value = "/websocket/beforerealtimecang/{timestamp}/{showtype}/{cartype}", configurator = GetHttpSessionConfigurator.class)
@Component
public class WebSocketServerBeforeRealTimeByTypeCang {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServerBeforeRealTimeByTypeCang.class);
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    @Autowired
    private BeanContext beancontext;
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private HttpSession httpSession;
    private Session session;
    private static Map<Long, Integer> showtype = new HashMap<>();
    private int showtype_int;
    private Integer cartype;
    private Boolean sessionisclose = false;

    public static Map<Long, Integer> getShowtype() {
        return showtype;
    }

    public static void setShowtype(Map<Long, Integer> showtype) {
        WebSocketServerBeforeRealTimeByTypeCang.showtype = showtype;
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
        WebSocketServerBeforeRealTimeByTypeCang.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        if (WebSocketServerBeforeRealTimeByTypeCang.onlineCount < 0) {
            WebSocketServerBeforeRealTimeByTypeCang.onlineCount = 0;
        } else {
            WebSocketServerBeforeRealTimeByTypeCang.onlineCount--;
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
    public void onOpen(Session session, EndpointConfig config, @PathParam("timestamp") Long timestamp, @PathParam("showtype") Integer showtype, @PathParam("cartype") Integer cartype) {
        this.session = session;
        this.showtype.put(timestamp, showtype);
        this.showtype_int = showtype;
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        this.cartype = cartype;

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
        System.out.println("离开页面、结束任务");
        sessionisclose = true;
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("收到来自窗口" + session.getId() + "的信息:" + message);
        RedisUtil redis = BeanContext.getApplicationContext().getBean(RedisUtil.class);

        TDayTaskMapper taskmapper = BeanContext.getApplicationContext().getBean(TDayTaskMapper.class);
        TDamsconstructionMapper damsconstructionMapper = BeanContext.getApplicationContext().getBean(TDamsconstructionMapper.class);
        try {
            List<String> kediskeys = new LinkedList<>();
            //当日碾压
            if (showtype_int == 1) {

                //遍历临时缓存 然后更新实时缓存 再清除临时缓存

                Set<String> keys = redis.keys(GenerateRedisKey.realTimeAllRidKeybycartype(GlobCache.cartableprfix[cartype]));
                for (String key : keys) {
                    kediskeys.add(key);
                }

            }
            else {// 任务碾压
                String ranges ="";

                List<DamsConstruction> all = damsconstructionMapper.findByStatus(8);
                for (DamsConstruction tempCangForReal : all) {
                    System.out.println("开仓仓位id："+tempCangForReal.getId());
                    System.out.println("需要加载的仓位ID："+message);
                    try {
                        if (!tempCangForReal.getId() .equals( Integer.valueOf(message))&&!tempCangForReal.getId().toString().equals(message)) {
                            continue;
                        } else {
                            System.out.println("只加载" + tempCangForReal.getTitle() + "的任务");
                            ranges =tempCangForReal.getRanges();
                            session.getBasicRemote().sendText("ranges"+ranges);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("非平板指定任务。");
                    }
                    Set<String> keys = redis.keys(GenerateRedisKey.taskrediskeysbycartypeCang(tempCangForReal.getTablename(), GlobCache.cartableprfix[cartype]));
                    for (String key : keys) {
                        kediskeys.add(key);
                    }
                }

            }


            if (null != kediskeys && kediskeys.size() > 0) {

                System.out.println("获取到Rid:" + kediskeys.size());//第一次进入时报错

                List<Thread> fourth = new ArrayList<>();

                int i = 1;
                for (String key : kediskeys) {
                    if (sessionisclose) {
                        break;
                    }
                    ConcurrentHashMap<Long, MatrixItemRedisReal[][]> cacheData = new ConcurrentHashMap<>();
                    List<Integer> cols = new LinkedList<>();//列
                    List<Integer> rows = new LinkedList<>();//行
                    long start = System.currentTimeMillis();
                    MatrixItemRedisReal[][] matrixItems = (MatrixItemRedisReal[][]) redis.get(key);
                    String rid = GenerateRedisKey.splitRealTimeStorehouseCang(key);
                    long lRid = Long.parseLong(rid);
                    cols.add(RidUtil.long2Col(lRid));
                    rows.add(RidUtil.long2Row(lRid));
                    cacheData.put(lRid, matrixItems);
                    DrawPicBeforeRealTimeThreadCang mythread = new DrawPicBeforeRealTimeThreadCang(cacheData, cols, rows, session, cartype, showtype_int, key);
                    Thread thread = new Thread(mythread);
                    thread.start();
                }

            }
            Thread.sleep(3000);
            sendMessage("绘图完毕");
            System.gc();

        } catch (Exception e) {
            e.printStackTrace();
            log.error("websocket IO异常");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            e.printStackTrace(new PrintStream(baos));
            String exception = baos.toString();
            log.error(exception);
            try {
                sendMessage("连接失败");
            } catch (Exception e2) {

            }
        }

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
    public synchronized void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }
}
