package com.tianji.dam.thread;

import java.util.Observable;
import java.util.Observer;

/*重启线程*/
public class RedisRightPopTaskBlockingObServer implements Observer {
    private String key;
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    } 
    @Override 
    public void update(Observable o, Object arg) {
       //重启线程
        System.out.println("5秒后重启线程>>"+key);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if("-1".equals(key)){
            RedisRightPopHistoryTaskBlocking history = new RedisRightPopHistoryTaskBlocking();
            RedisRightPopTaskBlockingObServer ob = new RedisRightPopTaskBlockingObServer();
            ob.setKey(key);
            history.addObserver(ob);
            Thread threadReal = new Thread(history);
            threadReal.start();
        }else {
            RedisRightPopRealTaskBlocking real = new RedisRightPopRealTaskBlocking();
            real.setCartype(Integer.valueOf(key));
            RedisRightPopTaskBlockingObServer ob = new RedisRightPopTaskBlockingObServer();
            ob.setKey(key);
            real.addObserver(ob);
            Thread threadReal = new Thread(real);
            threadReal.start();
        }
    }
}
