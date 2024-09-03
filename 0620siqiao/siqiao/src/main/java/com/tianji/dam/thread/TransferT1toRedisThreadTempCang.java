package com.tianji.dam.thread;


import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.bean.GlobCache;
import com.tianji.dam.domain.DamsConstruction;
import com.tianji.dam.domain.MatrixItemRedisReal;
import com.tianji.dam.domain.T1;
import com.tianji.dam.domain.vo.T1VO;
import com.tianji.dam.mapper.T1Mapper;
import com.tianji.dam.mapper.TDamsconstructionMapper;
import com.tianji.dam.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;


/*取数线程 不用轮训 采用阻塞的方式
 * 多线程：从redis List中弹出数据
 * 处理数据 将处理完的数据以 rid为key存入redis
 * 有车辆数据来了以后将获取车辆的最新位置，然后从redis中获取出数据然后直接推送
 * */
@Slf4j

public class TransferT1toRedisThreadTempCang implements Runnable {

    private String cartype;

    public String getCartype() {
        return cartype;
    }

    public void setCartype(String cartype) {
        this.cartype = cartype;
    }

    @Override
    public void run() {
        log.info("仓位数据重新处理启动。。。");
        RedisUtil redis = BeanContext.getApplicationContext().getBean(RedisUtil.class);
        T1Mapper t1Mapper = BeanContext.getApplicationContext().getBean(T1Mapper.class);
        TDamsconstructionMapper damsconstructionMapper = BeanContext.getApplicationContext().getBean(TDamsconstructionMapper.class);
        //已经开仓的仓位数据也从新加载
     List<DamsConstruction>   allopen =  damsconstructionMapper.findByStatus(8);

        BlockingQueue<Runnable> executorQueue = new LinkedBlockingQueue<>(5000);
        ExecutorService exec = new ThreadPoolExecutor(
                1,
                Runtime.getRuntime().availableProcessors() ,
                60, TimeUnit.SECONDS, executorQueue);
        for (DamsConstruction cangForReal : allopen) {
            System.out.println("从新生成"+cangForReal.getTitle()+"的数据到redis中");
         //   String rediskeysv = "TempRealCang_" + cartype + "_" + cangForReal.getFreedom1() + "_*";
            String keylike = "TempRealCang_" + cartype + "==" + cangForReal.getTablename() + "==*" ;
            Set<String> keys = redis.keys(keylike);
            for (String key : keys) {
                redis.del(key);
            }

            //查询历史数据库中的数据
            List<Integer> long2Cols = new LinkedList<>();//列
            List<Integer> long2Rows = new LinkedList<>();//行
                List<Callable<String>> tasks = new LinkedList<Callable<String>>();

                //查询仓位的车辆信息

        List<String> cars =      t1Mapper.selectCangCar(cartype + "_"+cangForReal.getTablename());
            for (String carid : cars) {

                    int pageSize = 5000;
                    T1VO t2 = new T1VO();
                    t2.setTablename(cartype + "_"+cangForReal.getTablename());
                    t2.setVehicleID(carid);
                    List<T1> list =t1Mapper.selectCangData(t2);
                    int  datasize =list.size();
                    System.out.println("需要处理的数据》》"+datasize);
                    if(0==datasize){
                        continue;
                    }

                    int i =0;
                 List<T1> datalist;
                    while(true){
                        if(datasize-(i+1)*pageSize>=0){
                            datalist = list.subList(i*pageSize,(i+1)*pageSize);
                            Callable<String> task = new CustomCarTaskResultforRealCang(cartype, datalist, long2Cols, long2Rows);
                            tasks.add(task);
                        }else{
                            datalist = list.subList(i*pageSize,datasize);
                            Callable<String> task = new CustomCarTaskResultforRealCang(cartype, datalist, long2Cols, long2Rows);
                            tasks.add(task);
                            break;
                        }
                        i++;
                    }
            }
                    try {
                        List<Future<String>> results = exec.invokeAll(tasks);
                        for (Future<String> future : results) {
                            future.get();
                        }
                        // 关闭线程池
                      //  exec.shutdown();

                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    } catch (ExecutionException e) {
                        exec.shutdownNow();
                        e.printStackTrace();
                    }

            try {
                Thread.sleep(2000);
                System.out.println("将仓位数据" + GlobCache.t1T0redishistorycarCachDataCang.get(cartype).keySet().size() + "条存入redis");
            for (Map.Entry<Long, MatrixItemRedisReal[][]> entry : GlobCache.t1T0redishistorycarCachDataCang.get(cartype).entrySet()) {
                // 10*10 小方格
                long rid = entry.getKey();
                MatrixItemRedisReal[][] value = entry.getValue();
               // String rediskeysv2 = "TempRealCang_" + cartype + "_" + cangForReal.getFreedom1() + "_" + rid;
                String rediskeysv2 = "TempRealCang_" + cartype + "==" + cangForReal.getTablename() + "=="+rid ;
                redis.set(rediskeysv2, value);

            }
                GlobCache.t1T0redishistorycarCachDataCang.get(cartype).clear();
            } catch (InterruptedException e) {
                e.printStackTrace();

            }

        }


    }


}

