package com.tianji.dam.bean;

import com.tianji.dam.service.CarService;
import com.tianji.dam.thread.RedisRightPopHistoryTaskBlocking;
import com.tianji.dam.thread.RedisRightPopRealTaskBlocking;
import com.tianji.dam.thread.RedisRightPopTaskBlockingObServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/*springboot启动前，先启动三个弹出线程*/
@Component
public class StartRunner implements CommandLineRunner {
    @Autowired
    BeanContext beancontext;

    private CarService carService = beancontext.getApplicationContext().getBean(CarService.class);
    private static final Logger logger = LoggerFactory.getLogger(StartRunner.class);

    @Override
    public void run(String... args) {
        logger.info("服务器启动后，启动弹出线程");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        RedisRightPopTaskBlockingObServer ob_1 = new RedisRightPopTaskBlockingObServer();
        ob_1.setKey("-1");

        RedisRightPopHistoryTaskBlocking history = new RedisRightPopHistoryTaskBlocking();
        history.addObserver(ob_1);
        Thread thread = new Thread(history);
        thread.start();

        RedisRightPopRealTaskBlocking real = new RedisRightPopRealTaskBlocking();
        real.setCartype(1);
        RedisRightPopRealTaskBlocking real2 = new RedisRightPopRealTaskBlocking();
        real2.setCartype(2);
        RedisRightPopTaskBlockingObServer ob1 = new RedisRightPopTaskBlockingObServer();
        ob1.setKey("1");
        RedisRightPopTaskBlockingObServer ob2 = new RedisRightPopTaskBlockingObServer();
        ob2.setKey("2");
        real.addObserver(ob1);
        real2.addObserver(ob2);
        Thread threadReal = new Thread(real);
        threadReal.start();
        Thread threadReal2 = new Thread(real2);
        threadReal2.start();
//
//
//


    }


}
