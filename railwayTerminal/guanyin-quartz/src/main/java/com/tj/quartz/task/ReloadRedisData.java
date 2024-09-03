package com.tj.quartz.task;

import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.bean.SMSBean;
import com.tianji.dam.domain.Car;
import com.tianji.dam.domain.TWarningUser;
import com.tianji.dam.mapper.CarMapper;
import com.tianji.dam.mapper.TWarningUserMapper;
import com.tianji.dam.thread.TransferT1toRedisThreadTempCang;
import com.tianji.dam.thread.TransferT1toRedisThreadTempCangDeleteRedis;
import com.tianji.dam.utils.RedisUtil;
import com.tj.common.utils.DateUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component("ReloadRedisData")
public class ReloadRedisData {

    public void autoredisdata(String damId) {
        int damIdi = Integer.valueOf(damId);
        RedisUtil redis = BeanContext.getApplicationContext().getBean(RedisUtil.class);
        CarMapper carMapper = BeanContext.getApplicationContext().getBean(CarMapper.class);
        System.out.println("从新生成任务碾压。。");
        TransferT1toRedisThreadTempCang tempCang = new TransferT1toRedisThreadTempCang();
        tempCang.setCartype("ylj");
        tempCang.setDamId(damIdi);
        Thread tcang = new Thread(tempCang);
        tcang.start();
    }


    //按照仓位删除redis数据

    public void redisDeleteByDamId(String damId) {
        int damIdi = Integer.valueOf(damId);
        RedisUtil redis = BeanContext.getApplicationContext().getBean(RedisUtil.class);
        CarMapper carMapper = BeanContext.getApplicationContext().getBean(CarMapper.class);
        System.out.println("删除id为" + damId + "的任务.......");
        TransferT1toRedisThreadTempCangDeleteRedis tempCang = new TransferT1toRedisThreadTempCangDeleteRedis();
        tempCang.setCartype("ylj");
        tempCang.setDamId(damIdi);
        Thread tcang = new Thread(tempCang);
        tcang.start();
    }





  /*  public void autoredisdata(String params) {

        String time = params.split("_")[0];
        String target = params.split("_")[1];
        Integer cartype = Integer.valueOf(params.split("_")[2]);

        RedisUtil redis = BeanContext.getApplicationContext().getBean(RedisUtil.class);
        CarMapper carMapper = BeanContext.getApplicationContext().getBean(CarMapper.class);

        if ("".equals(time)) {
            time = DateUtils.getDate();
        }
        //任务数据从新生成
        if (target.equals("task")) {
            System.out.println("从新生成任务碾压。。");
            TransferT1toRedisThreadTempCang tempCang = new TransferT1toRedisThreadTempCang();
            tempCang.setCartype(GlobCache.cartableprfix[cartype]);
            Thread tcang = new Thread(tempCang);
            tcang.start();
        } else {
            System.out.println("从新生成当日碾压。。");
            List<Car> carList = carMapper.findCar();
            //本地测试时使用
            //String time = "2023-02-22";
            boolean last = false;

            BlockingQueue<Runnable> executorQueue = new LinkedBlockingQueue<>(50000);
            ExecutorService exec = new ThreadPoolExecutor(1, 4, 60, TimeUnit.SECONDS, executorQueue);
            // 定义一个任务集合
            List<TransferT1toRedisThread> tasks = new LinkedList<>();

            for (int i = 0; i < carList.size(); i++) {

                if (carList.get(i).getType() != cartype) {
                    continue;
                }

                boolean clear = false;
                if (i == 0) {
                    clear = true;
                }
                if (i == carList.size() - 1) {
                    last = true;
                }
                Integer carid = carList.get(i).getCarID();

                T1VO param = new T1VO();
                param.setBeginTimestamp(time + " 00:00:00");
                param.setEndTimestamp(time + " 23:59:59");
                param.setSort(1);
                param.setVehicleID(carid.toString());
                TransferT1toRedisThread tt = new TransferT1toRedisThread();
                tt.setVos(Collections.singletonList(param));
                tt.setKey(time);
                tt.setIsclear(clear);
                tt.setKeytype(GlobCache.cartableprfix[cartype]);
                tasks.add(tt);
            }
            try {
                List<Future<Integer>> results = exec.invokeAll(tasks);
                for (Future<Integer> future : results) {
                    future.get();
                }
                // 关闭线程池
                exec.shutdown();
            } catch (Exception ie) {
                ie.printStackTrace();
            }
            //所有生成完成以后再将数据设置回redis中
            if (GlobCache.t1T0redishistorycarCachData.size() > 0) {
                System.out.println("即将保存。" + GlobCache.t1T0redishistorycarCachData.get(GlobCache.cartableprfix[cartype]).entrySet().size() + "条数据");

                for (Map.Entry<Long, MatrixItem[][]> entry : GlobCache.t1T0redishistorycarCachData.get(GlobCache.cartableprfix[cartype]).entrySet()) {
                    long rid = entry.getKey();
                    MatrixItem[][] value = entry.getValue();
                    //本地测试时使用
                    time = DateUtils.getDate();
                    String redisKey = GenerateRedisKey.realTimeRidKey2(time, rid, GlobCache.cartableprfix[cartype]);
                    redis.set(redisKey, value);
                }

                System.out.println("Redis数据保存完成。。");
            }
            GlobCache.t1T0redishistorycarCachData.clear();


        }


    }*/


    @Scheduled(cron = "00 34 17 * * ? ")
    @Async
    public void checkdeviceonline() {
        CarMapper carMapper = BeanContext.getApplicationContext().getBean(CarMapper.class);

        List<Car> all = carMapper.findCar();

        for (Car car : all) {
            System.out.println("车辆" + car.getRemark() + "上线监测。。");
            List<Date> times = carMapper.getcaronlinetime2(car.getCarID());

            if (times.size() == 1) {
                Date online = times.get(0);
                Date now = new Date();
                Date onelinepass = DateUtils.addDays(online, 1);
                long timepass = now.getTime() - onelinepass.getTime();
                long daypass = timepass * 1 / (1000 * 60 * 60 * 24);
                if (onelinepass.before(now) && daypass > 0) {
                    TWarningUserMapper warningUserMapper = BeanContext.getApplicationContext().getBean(TWarningUserMapper.class);
                    List<TWarningUser> alluser = warningUserMapper.selectuserlist();
                    for (TWarningUser tWarningUser : alluser) {
                        SMSBean ms = new SMSBean();
                        ms.setUsertel(tWarningUser.getTel());
                        ms.setTime(DateUtils.getTime());
                        ms.setDevicename(car.getRemark());
                        ms.setSitename("寺桥水库工程");
                        ms.setUsername(tWarningUser.getName());
                        ms.setWarning(daypass + "");
                        //   String  res =	 MLTSMSutils.send_caronline(ms);
                        //   System.out.println("发送结果："+res);
                    }


                }


            }


        }


    }

}
