package com.tianji.dam.thread;


import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.bean.GlobCache;
import com.tianji.dam.domain.T1;
import com.tianji.dam.domain.vo.T1VO;
import com.tianji.dam.mapper.CarMapper;
import com.tianji.dam.mapper.T1Mapper;
import com.tianji.dam.utils.GenerateRedisKey;
import com.tianji.dam.utils.RedisUtil;
import com.tianji.dam.websocket.WebsocketServerForPosition;
import com.tj.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.concurrent.*;


/*取数线程 不用轮训 采用阻塞的方式
 * 多线程：从redis List中弹出数据
 * 处理数据 将处理完的数据以 rid为key存入redis
 * 有车辆数据来了以后将获取车辆的最新位置，然后从redis中获取出数据然后直接推送
 * */
@Slf4j
//public class RedisRightPopTaskBlocking extends Observable implements Runnable {
public class TransferT1toRedisThread implements Callable<Integer> {
    //更改动态参数
    static Map<Integer, Color> colorMap = new HashMap<>(); //导致重启后配置的参数才会生效
    /*----------------------合并实时通道开始-----------------------*/

    @Autowired
    private WebsocketServerForPosition websocketServerForPosition;

    private String key;
    private List<T1VO> vos;

    private boolean last;
    private String Keytype;

    public String getKeytype() {
        return Keytype;
    }

    public void setKeytype(String keytype) {
        Keytype = keytype;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public void setVos(List<T1VO> vos) {
        this.vos = vos;
    }


    public String getKey() {
        return key;
    }

    public boolean isclear = false;

    public void setIsclear(boolean isclear) {
        this.isclear = isclear;
    }

    public void setKey(String key) {
        this.key = key;
    }


    @Override
    public Integer call() {
        log.info(key + "的数据重新处理启动。。。");
        T1Mapper t1Mapper = BeanContext.getApplicationContext().getBean(T1Mapper.class);
        RedisUtil redis = BeanContext.getApplicationContext().getBean(RedisUtil.class);
        CarMapper carMapper = BeanContext.getApplicationContext().getBean(CarMapper.class);

        //首先清除Redis中改日期的所有数据。
        Set<String> oldkeys = redis.keys(GenerateRedisKey.realTimeAllRidKey2(key, Keytype));

        Set<String> mids = redis.keys(GenerateRedisKey.realTimeAllRidKey(Keytype));

        if (isclear) {
            GlobCache.t1T0redishistorycarCachData.clear();
            for (String oldkey : oldkeys) {
                redis.del(oldkey);
            }
        }
        //将redis中这个车辆待处理的MatrixItem数据清除。
        for (String mid : mids) {
            redis.del(mid);
        }

        //    ConcurrentHashMap<Long, MatrixItem[][]> cache = GlobCache.t1T0redishistorycarCachData;
        List<Integer> long2Cols = new LinkedList<>();//列
        List<Integer> long2Rows = new LinkedList<>();//行

        for (T1VO vo : vos) {
            //筛选出车辆数据
            vo.setBeginTime(StringUtils.isNotNull(vo.getBeginTimestamp()) ? timeToStamp(vo.getBeginTimestamp()) : null);
            vo.setEndTime(StringUtils.isNotNull(vo.getEndTimestamp()) ? timeToStamp(vo.getEndTimestamp()) : null);
            vo.setTimestamp(0L);
            vo.setTablename(Keytype + "_t_1");
            System.out.print(vo);
            List<T1> t1s = t1Mapper.selectPage(vo);
            // 创建一个线程池
            BlockingQueue<Runnable> executorQueue = new LinkedBlockingQueue<>(500);
            ExecutorService exec = new ThreadPoolExecutor(
                    1,
                    10 ,
                    60, TimeUnit.SECONDS, executorQueue);
            // 定义一个任务集合
            List<Callable<String>> tasks = new LinkedList<Callable<String>>();
            int total =0;
            if (StringUtils.isNotEmpty(t1s)) {
                boolean whileFlag = true;
                LongSummaryStatistics resultNum = t1s.stream().mapToLong((item) -> item.getTimestamp()).summaryStatistics();
                Long timestamp = resultNum.getMax();
                int pageSize = 100000;
                while (whileFlag) {
                    T1VO t2 = new T1VO();
                    t2.setVehicleID(vo.getVehicleID());
                    t2.setTimestamp(timestamp);
                    t2.setPageSize(pageSize+5);
                    t2.setBeginElevation(vo.getBeginElevation());
                    t2.setEndElevation(vo.getEndElevation());
                   // t2.setBeginTime(StringUtils.isNotNull(vo.getBeginTimestamp()) ? timeToStamp(vo.getBeginTimestamp()) : null);
                  //  t2.setEndTime(StringUtils.isNotNull(vo.getEndTimestamp()) ? timeToStamp(vo.getEndTimestamp()) : null);
                    t2.setTablename(Keytype + "_t_1");
                    List<T1> list = t1Mapper.selectPage(t2);
                    total+=list.size();
                    if (StringUtils.isNotEmpty(list)) {
                        LongSummaryStatistics newNum = list.stream().mapToLong((item) -> item.getTimestamp()).summaryStatistics();
                        timestamp = newNum.getMax();
                        t1s.addAll(list);
                        Callable<String> task = new CustomCarTaskResult(Keytype, list, long2Cols, long2Rows);
                        tasks.add(task);
                    } else {
                        whileFlag = false;
                    }
                }
                System.out.println("一共："+total);
                try {
                    List<Future<String>> results = exec.invokeAll(tasks);
                    for (Future<String> future : results) {
                        future.get();
                    }
                    // 关闭线程池
                    exec.shutdown();
                } catch (InterruptedException ie) {
                    log.error(ie.getMessage());
                } catch (ExecutionException e) {
                    exec.shutdownNow();
                    log.error(e.getMessage());
                }
            }

        }
        System.out.println("Redis数据从新生成完成。。");


        return 1;

    }

    public static long timeToStamp(String timers) {
        long timeStemp = 0;
        try {
            Date d = new Date();
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            d = sf.parse(timers);// 日期转换为时间戳
            timeStemp = d.getTime();
        } catch (ParseException e) {
            log.error(e.getMessage());
        }
        return timeStemp;
    }

}

