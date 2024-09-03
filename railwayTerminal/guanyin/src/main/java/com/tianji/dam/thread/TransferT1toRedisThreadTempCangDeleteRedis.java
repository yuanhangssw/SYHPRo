package com.tianji.dam.thread;


import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.domain.DamsConstruction;
import com.tianji.dam.mapper.T1Mapper;
import com.tianji.dam.mapper.TDamsconstructionMapper;
import com.tianji.dam.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.concurrent.*;


/*取数线程 不用轮训 采用阻塞的方式
 * 多线程：从redis List中弹出数据
 * 处理数据 将处理完的数据以 rid为key存入redis
 * 有车辆数据来了以后将获取车辆的最新位置，然后从redis中获取出数据然后直接推送
 *
 * 用于redis数据没有备份，重启redis后实时监控无数无数据。
 * */
@Slf4j

public class TransferT1toRedisThreadTempCangDeleteRedis implements Runnable {

    private String cartype;

    private int damId;

    public String getCartype() {
        return cartype;
    }

    public void setCartype(String cartype) {
        this.cartype = cartype;
    }

    public int getDamId() {
        return damId;
    }

    public void setDamId(int damId) {
        this.damId = damId;
    }

    @Override
    public void run() {
        log.info("仓位数据重新处理启动。。。");
        RedisUtil redis = BeanContext.getApplicationContext().getBean(RedisUtil.class);
        T1Mapper t1Mapper = BeanContext.getApplicationContext().getBean(T1Mapper.class);
        TDamsconstructionMapper damsconstructionMapper = BeanContext.getApplicationContext().getBean(TDamsconstructionMapper.class);

        //根据damId来获取仓位数据
        DamsConstruction cangForReal = damsconstructionMapper.selectByPrimaryKey(damId);

        //创建一个容量为5000的BlockingQueue和一个ThreadPoolExecutor线程池，用于执行重新处理任务。
        BlockingQueue<Runnable> executorQueue = new LinkedBlockingQueue<>(5000);
        ExecutorService exec = new ThreadPoolExecutor(
                1,
                Runtime.getRuntime().availableProcessors(),
                60, TimeUnit.SECONDS, executorQueue);
        System.out.println("删除" + cangForReal.getTitle() + redis + "数据");
        //根据cartype和仓位的表名拼接出Redis的key的模糊匹配字符串。
        String keylike = "TempRealCang_" + cartype + "==" + cangForReal.getTablename() + "==*";
        //使用redis.keys()方法获取匹配到的所有key。
        Set<String> keys = redis.keys(keylike);
        //遍历所有key并使用redis.del()方法将其从Redis中删除。
        for (String key : keys) {
            redis.del(key);
        }
        System.out.println("删除了" + keys.size() + "个键");

    }

}






