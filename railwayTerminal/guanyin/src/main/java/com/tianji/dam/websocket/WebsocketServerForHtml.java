package com.tianji.dam.websocket;


import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;

import com.tianji.dam.domain.MatrixItem;
import com.tianji.dam.domain.StoreHouseMap;
import com.tianji.dam.domain.StorehouseRange;

import lombok.extern.slf4j.Slf4j;

/*
*用于判断html页面是否在线
* */
@Slf4j
//@ServerEndpoint(value = "/websocket/html/{userName}")
//@Component
public class WebsocketServerForHtml {
    @Autowired
    private static StoreHouseMap storeHouseMap;
    private static Map<String, MatrixItem[][]> storeHouseMaps2RollingData = storeHouseMap.getStoreHouses2RollingData();
    private static Map<String, StorehouseRange> shorehouseRange=storeHouseMap.getShorehouseRange();

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebsocketServerForHtml> webSocketSet = new CopyOnWriteArraySet<WebsocketServerForHtml>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    private String userName;

    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(@PathParam("userName")String userName, Session session) {
        this.session = session;
        this.userName = userName;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        log.info("有新连接加入！当前在线人数为" + getOnlineCount());

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
        /*清除storehouseRange*/
        /*清除storehouseMaos2RollingData*/

        Iterator<String> iterator = shorehouseRange.keySet().iterator();
        while(iterator.hasNext()){
            String key = iterator.next();
            String[] split1 = key.split("-");
            String userNameOfMatrix = split1[0];
            if(userName.equalsIgnoreCase(userNameOfMatrix)){
                iterator.remove();
            }
        }

        Iterator<String> iterator2 = storeHouseMaps2RollingData.keySet().iterator();
        while(iterator2.hasNext()){
            String key = iterator2.next();
            String[] split1 = key.split("-");
            String userNameOfMatrix = split1[0];
            if(userName.equalsIgnoreCase(userNameOfMatrix)){
                iterator2.remove();
            }
        }


    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("来自客户端的消息:" + message);

        //群发消息
        for (WebsocketServerForHtml item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发生错误时调用*/
     @OnError
     public void onError(Session session, Throwable error) {
         log.info("发生错误");
         log.error("尝试捕捉页面意外关闭的情况");

     }


     public void sendMessage(String message) throws IOException {
     this.session.getBasicRemote().sendText(message);
     //this.session.getAsyncRemote().sendText(message);
     }


     /**
      * 群发自定义消息
      * */
    public static void sendInfo(String message) throws IOException {
        for (WebsocketServerForHtml item : webSocketSet) {
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
        WebsocketServerForHtml.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebsocketServerForHtml.onlineCount--;
    }
}
