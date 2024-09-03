package com.tianji.dam.thread;

import java.util.concurrent.Callable;

public class TestInvokeThread implements Callable {
    @Override
    public Object call() throws Exception {

        String name = Thread.currentThread().getName();
        System.out.println(name + "执行");

        Thread.sleep(3000);
        return 1;
    }
}
