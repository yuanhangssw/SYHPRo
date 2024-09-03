package com.tianji.dam.websocket;


import com.alibaba.fastjson.JSON;
import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.bean.GlobCache;
import com.tianji.dam.domain.Car;
import com.tianji.dam.domain.MatrixItem;
import com.tianji.dam.domain.T1;
import com.tianji.dam.domain.TColorConfig;
import com.tianji.dam.domain.vo.T1VO;
import com.tianji.dam.mapper.CarMapper;
import com.tianji.dam.mapper.T1Mapper;
import com.tianji.dam.mapper.TColorConfigMapper;
import com.tianji.dam.thread.CustomCarTaskResult;
import com.tianji.dam.thread.DrawPicForCarReport;
import com.tianji.dam.utils.RidUtil;
import com.tj.common.utils.DateUtils;
import com.tj.common.utils.RGBHexUtil;
import com.tj.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.awt.*;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 平面分析-按车辆查询-websocket推送数据
 */
@Slf4j
@ServerEndpoint(value = "/websocket/forcarreport", configurator = GetHttpSessionConfigurator.class)
@Component
public class WebSocketServerForCarReport {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServerForCarReport.class);
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    @Autowired
    private BeanContext beancontext;
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private HttpSession httpSession;
    private Session session;
    private int cartype;
    T1Mapper t1Mapper;
    CarMapper carMapper = BeanContext.getApplicationContext().getBean(CarMapper.class);

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
        WebSocketServerForCarReport.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        if (WebSocketServerForCarReport.onlineCount < 0) {
            WebSocketServerForCarReport.onlineCount = 0;
        } else {
            WebSocketServerForCarReport.onlineCount--;
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
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());

        try {
            t1Mapper = BeanContext.getApplicationContext().getBean(T1Mapper.class);
            sendMessage("连接成功");
        } catch (Exception e) {

        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {


    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("收到来自窗口" + session.getId() + "的信息:" + message);

        try {
            String param = URLDecoder.decode(message, "utf-8");
            List<T1VO> vos = JSON.parseArray(param, T1VO.class);


            List<Integer> long2Cols = new LinkedList<>();//列
            List<Integer> long2Rows = new LinkedList<>();//行
            int cartype2 = 0;
            String cartype = "";
            int mina =1;
            int maxa =Runtime.getRuntime().availableProcessors() ;
            for (T1VO vo : vos) {
                // 创建一个线程池
                ExecutorService exec = Executors.newSingleThreadExecutor();
                // 定义一个任务集合
                List<Callable<String>> tasks = new LinkedList<Callable<String>>();
                Callable<String> task ;
                List<T1> list = new ArrayList<>();
                T1VO t2 ;

                Car car = carMapper.selectByPrimaryKey(Integer.valueOf(vo.getVehicleID()));
                cartype2 = car.getType();
                cartype = GlobCache.cartableprfix[cartype2];
                //筛选出车辆数据
                vo.setBeginTime(StringUtils.isNotNull(vo.getBeginTimestamp()) ? timeToStamp(vo.getBeginTimestamp()) : null);
                vo.setEndTime(StringUtils.isNotNull(vo.getEndTimestamp()) ? timeToStamp(vo.getEndTimestamp()) : null);
                vo.setTimestamp(0L);
                vo.setTablename(cartype + "_t_1");
                // System.out.print(vo);
                List<T1> t1s = t1Mapper.selectPage(vo);


                if (StringUtils.isNotEmpty(t1s)) {
                    boolean whileFlag = true;
                    LongSummaryStatistics resultNum = t1s.stream().mapToLong((item) -> item.getTimestamp()).summaryStatistics();
                    Long timestamp = resultNum.getMax();
                    int pageSize = 10000;
                    int ifj =0;
                    while (whileFlag) {
                        t2 = new T1VO();
                        t2.setVehicleID(vo.getVehicleID());
                        t2.setTimestamp(timestamp);
                        t2.setPageSize(pageSize+2);
                        t2.setBeginElevation(vo.getBeginElevation());
                        t2.setEndElevation(vo.getEndElevation());
                        t2.setBeginTime(StringUtils.isNotNull(vo.getBeginTimestamp()) ? timeToStamp(vo.getBeginTimestamp()) : null);
                        t2.setEndTime(StringUtils.isNotNull(vo.getEndTimestamp()) ? timeToStamp(vo.getEndTimestamp()) : null);
                        t2.setTablename(vo.getTablename());
                        list = t1Mapper.selectPage(t2);
                        System.out.println(DateUtils.getTime()+">>车辆平面分析将处理：" + list.size() + "条数据");
                        //直接推点
//                        if(list.size()>0){
//                             session.getBasicRemote().sendText(JSONObject.toJSONString(list));
//                        }else{
//                            whileFlag = false;
//                        }
                        //直接推图
                        if (StringUtils.isNotEmpty(list)) {
                            LongSummaryStatistics newNum = list.stream().mapToLong((item) -> item.getTimestamp()).summaryStatistics();
                            timestamp = newNum.getMax();
                            t1s.addAll(list);
                            task = new CustomCarTaskResult(GlobCache.cartableprfix[car.getType()], list, long2Cols, long2Rows);
                            tasks.add(task);
                        } else {
                            whileFlag = false;
                        }
                        ifj++;
                        if(ifj==3){
                            try {
                                List<Future<String>> results = exec.invokeAll(tasks);
                                for (Future<String> future : results) {
                                    log.info(future.get());
                                }
                              //  exec.shutdown();
                            } catch (InterruptedException ie) {
                                log.error(ie.getMessage());
                            } catch (ExecutionException e) {
                                exec.shutdownNow();
                                log.error(e.getMessage());
                            }
                            tasks.clear();
                            ifj=0;
                            Thread.sleep(1000);
                        }
                    }

                /*方案二*/
                /* cacheData(t1s,cache,long2Cols,long2Rows);*/

            }
                // 关闭线程池
                try {
                    List<Future<String>> results = exec.invokeAll(tasks);
                    for (Future<String> future : results) {
                        log.info(future.get());
                    }
                    exec.shutdown();

                } catch (InterruptedException ie) {
                    log.error(ie.getMessage());
                } catch (ExecutionException e) {
                    exec.shutdownNow();
                    log.error(e.getMessage());
                }
                tasks.clear();
                list.clear();
                t1s.clear();

            }

                Thread.sleep(3000);

            ExecutorService exec2 = Executors.newSingleThreadExecutor();
            List<Callable<Integer>> tasks2 = new ArrayList<>();
            Callable<Integer> task2 = null;

            ConcurrentHashMap<Long, MatrixItem[][]> values = GlobCache.t1T0redishistorycarCachData.get(cartype);
            if (null != values && null != values.entrySet()) {
                List<Float> allcurrentevolution = new ArrayList<>();
                Float avgevolution = null;
                for (Map.Entry<Long, MatrixItem[][]> entry : values.entrySet()) {
                    // 10*10 小方格
                    MatrixItem[][] matrixItems = entry.getValue();
                    for (MatrixItem[] allmatrix : matrixItems) {
                        for (MatrixItem matrixItem : allmatrix) {
                            if (null != matrixItem && null != matrixItem.getCurrentEvolution() && matrixItem.getCurrentEvolution().size() > 0) {
                                allcurrentevolution.add(matrixItem.getCurrentEvolution().getLast());
                            }
                        }
                    }
                    avgevolution = allcurrentevolution.stream().filter(x -> x != null).collect(Collectors.averagingDouble(Float::floatValue)).floatValue();
                }

                TColorConfigMapper        colorConfigMapper = BeanContext.getApplicationContext().getBean(TColorConfigMapper.class);
                Map<Integer,Color> colormap =new HashMap<>();

                getColorMap((long) cartype2,colorConfigMapper,colormap);
                TColorConfig vo = new TColorConfig();
                vo.setType(3L);//超限
                List<TColorConfig> colorConfigs = colorConfigMapper.select(vo);
                vo.setType(6L);//动静碾压
                List<TColorConfig> colorConfigs6 = colorConfigMapper.select(vo);
                vo.setType(44l);//摊铺平整度颜色
                List<TColorConfig> colorConfigs44 = colorConfigMapper.select(vo);
                vo.setType(45l);//摊铺平整度颜色
                List<TColorConfig> colorConfigs45 = colorConfigMapper.select(vo);
                    int ifj =0;
                long rid ;
                MatrixItem[][] matrixItems;
                ConcurrentHashMap<Long, MatrixItem[][]> cacheData = new ConcurrentHashMap<>();
                List<Integer> cols = new LinkedList<>();//列
                List<Integer> rows = new LinkedList<>();//行
                for (Map.Entry<Long, MatrixItem[][]> entry : values.entrySet()) {
                    // 10*10 小方格
                     rid = entry.getKey();
                    matrixItems = entry.getValue();
                   cacheData = new ConcurrentHashMap<>();
                     cols = new LinkedList<>();//列
                    rows = new LinkedList<>();//行

                    cols.add(RidUtil.long2Col(rid));
                    rows.add(RidUtil.long2Row(rid));
                    cacheData.put(rid, matrixItems);
                    task2 = new DrawPicForCarReport(cacheData, cols, rows, session, cartype2, avgevolution,colormap,colorConfigs,colorConfigs6,colorConfigs44);
                    tasks2.add(task2);

                    ifj++;
                    if(ifj==4){
                        try {
                            System.out.println("线程池任务开始执行》" + DateUtils.getTime());
                            List<Future<Integer>> results = exec2.invokeAll(tasks2);
                            for (Future<Integer> future : results) {
                                log.info(String.valueOf(future.get()));
                            }
                            //  exec.shutdown();
                        } catch (InterruptedException ie) {
                            log.error(ie.getMessage());
                        } catch (ExecutionException e) {
                            exec2.shutdownNow();
                            log.error(e.getMessage());
                        }
                        tasks2.clear();
                        ifj=0;
                        Thread.sleep(1000);
                    }
                }
            }
            System.out.println("线程池任务开始执行》" + DateUtils.getTime());
            List<Future<Integer>> results = exec2.invokeAll(tasks2);

            try {
                for (Future<Integer> future : results) {
                    future.get();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            sendMessage("绘图完成");
            exec2.shutdown();
            GlobCache.t1T0redishistorycarCachData.get(cartype).clear();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("websocket IO异常");
            try {
                sendMessage("连接失败");
            } catch (Exception e2) {

            }
        }

        //群发消息
    }

    public void getColorMap(Long type, TColorConfigMapper colorConfigMapper,Map<Integer,Color> colorMap) {

        TColorConfig vo = new TColorConfig();
        vo.setType(type);//碾压遍次
        List<TColorConfig> colorConfigs = colorConfigMapper.select(vo);
        for (TColorConfig color : colorConfigs) {
            if (color.getNum().intValue() == 0) {
                colorMap.put(0, new Color(255, 255, 255, 0));
            } else {
                int[] rgb = RGBHexUtil.hex2RGB(color.getColor());
                colorMap.put(Integer.valueOf(String.valueOf(color.getNum())), new Color(rgb[0], rgb[1], rgb[2]));
            }
        }

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
