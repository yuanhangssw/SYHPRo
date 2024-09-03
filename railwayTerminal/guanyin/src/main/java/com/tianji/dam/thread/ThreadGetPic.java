package com.tianji.dam.thread;

import com.alibaba.fastjson.JSONObject;
import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.service.RollingDataService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.Callable;


public class ThreadGetPic implements Callable<JSONObject> {

    @Autowired
    private BeanContext beancontext;

    RollingDataService rollingDataService = beancontext.getApplicationContext().getBean(RollingDataService.class);
    String userName;
    String tableName;

    public ThreadGetPic(String userName, String tableName) {
        this.userName = userName;
        this.tableName = tableName;
    }

    @Override
    public JSONObject call() throws Exception {
        JSONObject jsonObject = rollingDataService.getHistoryPicMultiThreadingByZhuangByTod(tableName, 1);
        return jsonObject;
    }
}
