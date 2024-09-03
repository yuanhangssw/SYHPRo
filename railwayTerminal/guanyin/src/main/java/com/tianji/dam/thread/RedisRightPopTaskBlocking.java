package com.tianji.dam.thread;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.bean.JtsRTree;
import com.tianji.dam.domain.*;
import com.tianji.dam.domain.vo.DamsJtsTreeVo;
import com.tianji.dam.mapper.TColorConfigMapper;
import com.tianji.dam.mapper.TDamsconstructionMapper;
import com.tianji.dam.model.RealTimeRedisDataModel;
import com.tianji.dam.scan.*;
import com.tianji.dam.utils.*;
import com.tianji.dam.websocket.WebsocketServerForPosition;
import com.tj.common.utils.RGBHexUtil;
import com.tj.common.utils.StringUtils;
import com.vividsolutions.jts.algorithm.PointLocator;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.osgeo.proj4j.ProjCoordinate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import javax.websocket.Session;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Queue;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


/*取数线程 不用轮训 采用阻塞的方式
 * 多线程：从redis List中弹出数据*/
@Slf4j
//public class RedisRightPopTaskBlocking extends Observable implements Runnable {
public class RedisRightPopTaskBlocking implements Runnable {
    @Autowired
    private WebsocketServerForPosition websocketServerForPosition;
    Map<String, Set<Session>> positionUsers = websocketServerForPosition.getPositionUses();

    Map<String, Session> websocketuser = websocketServerForPosition.getSokectuser();
    /* 记录仓下的工作车辆 */
    List<String> cangList = websocketServerForPosition.getPositionCang();
    private String key;
    private String sessionkey;
    final String WS = "_ws";


    public String getSessionkey() {
        return sessionkey;
    }

    public void setSessionkey(String sessionkey) {
        this.sessionkey = sessionkey;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private volatile boolean isStop;//使用volatile关键字修饰，可以在多线程之间共享，成员变量来控制线程的停止.

    /**
     * 设置线程标识
     *
     * @param isStop true：停止 false：不停止
     */
    public void stopThread(boolean isStop) {
        this.isStop = isStop;
    }

    /*判断数据是否有*/
    Queue<Integer> queue = new LinkedList<Integer>();

    /*----------------------合并实时通道开始-----------------------*/
    @Autowired
    private static StoreHouseMap storeHouseMap;
    //更改动态参数
    static Map<Integer, Color> colorMap = new HashMap<>(); //导致重启后配置的参数才会生效


    @Override
    public void run() {
        if (null == storeHouseMap) {

            storeHouseMap = BeanContext.getApplicationContext().getBean(StoreHouseMap.class);

        }

        //实时数据缓存
        Map<String, RealTimeRedisDataModel> rollingDataMap = storeHouseMap.getRealTimeRedisDataModelMap();

        //更改动态参数
        TColorConfigMapper colorConfigMapper = BeanContext.getApplicationContext().getBean(TColorConfigMapper.class);
        getColorMap(colorConfigMapper);
        //开辟内存空间做缓存
        ConcurrentHashMap<Long, MatrixItem[][]> cache = new ConcurrentHashMap<>();
        ConcurrentHashMap<Long, MatrixItem[][]> cacheData = new ConcurrentHashMap<>();
        ConcurrentHashMap<Long, MatrixItem[][]> noStorehouseCache = new ConcurrentHashMap<>();

        RollingData rollingData01 = new RollingData();
        RollingData rollingData02 = new RollingData();
        WheelMarkData mLastPt = null;
        Quadrilateral ql = new Quadrilateral();
        PointCpb[] convex = new PointCpb[4];
        PointCpb[] ps = null;
        PointCpb ps1 = new PointCpb();
        PointCpb ps2 = new PointCpb();
        PointCpb ps3 = new PointCpb();
        PointCpb ps4 = new PointCpb();
        Pixel[] polygon = null;
        Pixel polygon1 = new Pixel();
        Pixel polygon2 = new Pixel();
        Pixel polygon3 = new Pixel();
        Pixel polygon4 = new Pixel();

        /*初始化*/
        for (int i = 0; i < 20; i++) {
            queue.offer(0);
        }
        log.info(key + "_" + sessionkey + "的实时推送启动。。。");
        RedisUtil redisUtil = BeanContext.getApplicationContext().getBean(RedisUtil.class);
        while (!isStop) {
            try {
                boolean speedover = false;
                RollingData rollingData = (RollingData) redisUtil.rightPop(WS + key + "_" + sessionkey + WS);  //key：车id
                if (StringUtils.isNotNull(rollingData)) {
                    //System.out.println("实时推送中桩号/偏距"+rollingData.getZhuanghao()+":"+rollingData.getPianju());
                    //System.out.println("将为"+key+"_"+sessionkey+"推送实时车辆绘制。。。");
                    int orderNum = rollingData.getOrderNum();//轨迹点序列号 用于防止数据丢失
                    // log.info(String.format("RedisRightPopTaskBlocking 推送数据为===%s",(rollingData.getIshistory() == 0?"实时数据":"历史数据")));
                    String ranges = "";
                    if (!queue.contains(orderNum)) {
                        //通过R树查找单元
                        int j = -1;
                        List<DamsJtsTreeVo> treeVos = JtsRTree.query(rollingData.getZhuangX(), rollingData.getZhuangY());
                        List<DamsConstruction> newCang = new LinkedList<>();
                        if (!treeVos.isEmpty()) {
                            for (int i = 0; i < treeVos.size(); i++) {
                                DamsJtsTreeVo vo = treeVos.get(i);
                                TDamsconstructionMapper damsConstructionMapper = BeanContext.getApplicationContext().getBean(TDamsconstructionMapper.class);
                                //0.获得工作仓的范围
                                DamsConstruction damsConstruction = damsConstructionMapper.selectByPrimaryKey(vo.getId());
                                PointLocator a = new PointLocator();
                                Coordinate point = new Coordinate();
                                point.x = rollingData.getZhuangX();//桩x
                                point.y = rollingData.getZhuangY();//桩y
                                point.z = rollingData.getElevation();//高程
                                GeometryFactory geometryFactory = new GeometryFactory();
                                List<Coordinate> list = JSONArray.parseArray(damsConstruction.getRanges(), Coordinate.class);
                                Coordinate[] array = new Coordinate[list.size() + 1];  //最重要，不能遗漏，预先申请好数组空间
                                list.toArray(array);
                                array[list.size()] = array[0];
                                Geometry bg = geometryFactory.createPolygon(array);
                                boolean p1 = a.intersects(point, bg);
                                if (p1) {//判断是否在边界内
                                    // todo: 判断数据的timestamp是不是大于等于这些仓位的 actualstarttime实际开始施工时间，把还没有到开仓时间的仓位踢出掉
                                    if (damsConstruction.getStatus().intValue() == 8) {//对已开仓的仓位进行匹配
                                        Long starttime = (new SimpleDateFormat("yyyy-MM-dd")).parse(damsConstruction.getActualstarttime(), new ParsePosition(0)).getTime();
                                        if (rollingData.getTimestamp().longValue() >= starttime.longValue()) {//数据的timestamp 大于等于仓位的 actualstarttime实际开始施工时间
                                            newCang.add(damsConstruction);//把已匹配的仓位加入新的集合中
                                        }
                                    }
                                }
                            }
                            if (StringUtils.isNotEmpty(newCang)) {//如果匹配到仓位
                                if (newCang.size() >= 1) {//并且多个仓位时
                                    for (int i = 0; i < newCang.size(); i++) {
                                        Long storehouseEndTime = (new SimpleDateFormat("yyyy-MM-dd")).parse(newCang.get(i).getActualendtime(), new ParsePosition(0)).getTime();
                                        Long rollingTime = rollingData.getTimestamp();
                                        if (rollingTime.longValue() <= storehouseEndTime.longValue()) {//当前时间非大于实际闭仓时间
                                            j = i;
                                            //  log.info("仓位"+newCang.get(i).getTitle()+"闭仓时间" + newCang.get(i).getActualendtime() +"大于当前轨迹时间" + rollingData.getTimestamp());
                                            Float currentspeed = rollingData.getSpeed();
                                            Double normalspeed = newCang.get(i).getSpeed();
                                            //  System.out.println("速度限制："+normalspeed+";实际速度："+currentspeed);
                                            if (currentspeed > normalspeed) {
                                                speedover = true;
                                            }
                                            ranges = newCang.get(i).getRanges();
                                            break;
                                        }
                                    }
                                }
                            } else {
//                                log.info("========未匹配到仓位====");
                            }
                        } else {
                            log.info("-----------------R tree is empty!!!.-----------------");
                        }
                        int cangId = 0;
                        String cangName = null;
                        if (j >= 0) {
                            DamsConstruction vo = newCang.get(j);
                            cangId = vo.getId();
                            cangName = vo.getTitle();
                        }
                        if (rollingData.getIshistory() == 0) {
                            /*给前端发送信息*/
                            try {
                                //todo: 1.判断当前点是否在仓内
                                //todo: 2.如果不在仓内 cache切换为当前类内的cache 如果在仓内 cache切换为缓存中的cache
                                if (StringUtils.isNotNull(cangName)) {
                                    cache = storeHouseMap.getRealTimeStorehouseDataItem(cangName);
                                } else {
                                    cache = noStorehouseCache;
                                }
                                //cache = noStorehouseCache;
                                if (!StringUtils.isNotNull(rollingData01.getVehicleID())) {
                                    rollingData01 = rollingData;
                                } else {
                                    rollingData02 = rollingData;
                                    double dis = Math.sqrt(Math.pow(rollingData01.getZhuangX() - rollingData02.getZhuangX(), 2) +
                                            Math.pow(rollingData01.getZhuangY() - rollingData02.getZhuangY(), 2));
                                    if (dis < TrackConstant.MIN_DIS) {
                                    } else if (dis > TrackConstant.MAX_DIS) {
                                        rollingData01 = rollingData02;
                                        //将前一个滚轮置空,否则将会把中间连接在一起
                                        mLastPt = null;
                                    } else {
                                        WheelMarkData data = WheelMarkData.calPosition(rollingData01, rollingData02, TrackConstant.WHEEL_LEFT, TrackConstant.WHEEL_RIGHT, 0);
                                        if (data != null) {
                                            rollingData02.setCoordLX(data.LPt.getX());
                                            rollingData02.setCoordLY(data.LPt.getY());
                                            rollingData02.setCoordRX(data.RPt.getX());
                                            rollingData02.setCoordRY(data.RPt.getY());
                                            if (mLastPt == null) {
                                                mLastPt = data;
                                            } else {
                                                if (ps == null) {
                                                    ps = new PointCpb[]{ps1, ps2, ps3, ps4};
                                                }
                                                ps1.x = mLastPt.LPt.getX();
                                                ps1.y = mLastPt.LPt.getY();
                                                ps2.x = mLastPt.RPt.getX();
                                                ps2.y = mLastPt.RPt.getY();
                                                ps3.x = data.LPt.getX();
                                                ps3.y = data.LPt.getY();
                                                ps4.x = data.RPt.getX();
                                                ps4.y = data.RPt.getY();
                                                int count = PolygonUtils.getPointsConvexClosure(ps, 0, 4, convex);
                                                if (count == 4) {
                                                    ql.pt1.setX(convex[0].x);
                                                    ql.pt1.setY(convex[0].y);
                                                    ql.pt2.setX(convex[1].x);
                                                    ql.pt2.setY(convex[1].y);
                                                    ql.pt3.setX(convex[2].x);
                                                    ql.pt3.setY(convex[2].y);
                                                    ql.pt4.setX(convex[3].x);
                                                    ql.pt4.setY(convex[3].y);
                                                    if (polygon == null) {
                                                        polygon = new Pixel[]{polygon1, polygon2, polygon3, polygon4};
                                                    }
                                                    polygon1.setX((int) (ql.pt1.getX()));
                                                    polygon1.setY((int) (ql.pt1.getY()));
                                                    polygon2.setX((int) (ql.pt2.getX()));
                                                    polygon2.setY((int) (ql.pt2.getY()));
                                                    polygon3.setX((int) (ql.pt3.getX()));
                                                    polygon3.setY((int) (ql.pt3.getY()));
                                                    polygon4.setX((int) (ql.pt4.getX()));
                                                    polygon4.setY((int) (ql.pt4.getY()));
                                                    List<Pixel> rasters = Scan.scanRaster(polygon);
                                                    int n = 0;
                                                    int m = 0;

                                                    //  ConcurrentHashMap<Long, MatrixItem[][]> cacheData = new ConcurrentHashMap<>();
                                                    Map<Long, Long> rids = new HashMap<>();
                                                    List<Integer> cols = new LinkedList<>();//列
                                                    List<Integer> rows = new LinkedList<>();//行
                                                    int allPassCount = 0;
                                                    //rasters 数据像素
                                                    for (Pixel pixel : rasters) {
                                                        try {
                                                            //根据像素获得rid 也就是10*10小格子
                                                            long rid = RidUtil.double2Long(pixel.getX(), pixel.getY());

                                                            rids.put(rid, rid);
                                                            cols.add(RidUtil.long2Col(rid));
                                                            rows.add(RidUtil.long2Row(rid));

                                                            int bottom = RidUtil.long2Bottom(rid);
                                                            int left = RidUtil.long2Left(rid);
                                                            n = (pixel.getY() - bottom);
                                                            m = (pixel.getX() - left);
                                                            MatrixItem[][] matrixItems = cache.get(rid);
                                                            if (!StringUtils.isNotEmpty(matrixItems)) {
                                                                matrixItems = new MatrixItem[RidUtil.R_LEN][RidUtil.R_LEN];
                                                                //TODO:10*10 PIXEL为一个矩形块
                                                                //根据Rid  获取 long2Col 列  long2Row 行
                                                                cache.put(rid, matrixItems);

                                                                cacheData.put(rid, new MatrixItem[RidUtil.R_LEN][RidUtil.R_LEN]);
                                                            }
                                                            if (n >= 0 && m >= 0 && n < RidUtil.R_LEN && m < RidUtil.R_LEN) {
                                                                MatrixItem item = matrixItems[m][n];
                                                                if (item == null) {
                                                                    item = new MatrixItem();
                                                                }
                                                                item.setRollingTimes(item.getRollingTimes() + 1);
                                                                item.getElevationList().add(rollingData02.getElevation().floatValue());
                                                                item.getAccelerationList().add(rollingData02.getAcceleration());
                                                                item.getFrequencyList().add(rollingData02.getFrequency());
                                                                item.getSpeedList().add(rollingData02.getSpeed().floatValue());
                                                                item.getTimestampList().add(rollingData02.getTimestamp());
                                                                item.getVehicleIDList().add(rollingData02.getVehicleID());
                                                                matrixItems[m][n] = item;

                                                                cacheData.put(rid, matrixItems);
                                                                allPassCount += item.getRollingTimes();
                                                            }
                                                        } catch (Exception ex) {
                                                            ex.printStackTrace();
                                                        }
                                                    }
                                                    int currentPassCount = allPassCount / rasters.size();
                                                    rasters.clear();
                                                    rollingData01 = rollingData02;
                                                    mLastPt = data;
                                                    //System.out.println("将为"+key+"_"+sessionkey+"绘制推送图片。。。。。");
                                                    JSONObject result0 = draw(cacheData, cols, rows, rollingData02);
                                                    result0.put("speedover", speedover);
                                                    result0.put("lastrollingtime", currentPassCount);
                                                    result0.put("ranges", ranges);
                                                    result0.put("zhuanghao", rollingData02.getZhuanghao());
                                                    result0.put("pianju", rollingData02.getPianju());


                                                    try {
                                                        result0.put("cangId", cangId);
                                                        result0.put("title", cangName == null ? " " : cangName + " ");
                                                        AtomicInteger isLoad = new AtomicInteger(1);
                                                        if (StringUtils.isNotEmpty(cangList)) {
                                                            if (cangId != 0) {
                                                                /*如果工作仓有值 则判断 工作仓+"-"+车 是否已存在*/
                                                                boolean flag = useList(cangList, cangId + "-" + rollingData.getVehicleID());
                                                                if (!flag) {
                                                                    /*不存在 则推送通知前端开启通道*/
                                                                    cangList.add(cangId + "-" + rollingData.getVehicleID());
                                                                    int finalCangId = cangId;
                                                                    cangList.stream().forEach(cang -> {
                                                                        int newCangId = Integer.valueOf(cang.split("-")[0]);
                                                                        if (newCangId == finalCangId) {
                                                                            isLoad.set(1);
                                                                        } else {
                                                                            isLoad.set(0);
                                                                        }
                                                                    });
                                                                }
                                                            }
                                                        } else {
                                                            /*如果记录工作仓为空 则不控制直接推送就好*/
                                                            if (cangId != 0) {
                                                                cangList.add(cangId + "-" + rollingData.getVehicleID());
//                                                                                result0.put("isLoad", 0);//仓未加载过
                                                                isLoad.set(0);
                                                            }
                                                        }
                                                        Thread.sleep(200);
                                                        result0.put("isLoad", isLoad);//仓未加载过

                                                        //获取当前线程。的websocket 会话session
                                                        Session session = websocketuser.get(sessionkey);
                                                        if (session.isOpen()) {
                                                            session.getBasicRemote().sendText(JSONObject.toJSONString(result0));
                                                        } else {
                                                            this.stopThread(true);
                                                        }
                                                        //  System.out.println(key+"_"+sessionkey+"推送完成>>>"+result0.toJSONString());
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }


                                                } else {
                                                }
                                            }
                                        }
                                    }
                                }

                                //更新车辆对应缓存数据


                                storeHouseMap.getRealTimeStorehouseDataMap().put(cangName, cache);
                                // rollingDataMap.put("0",new RealTimeRedisDataModel(cache));
                                //  rollingDataMap.put(rollingData.getVehicleID(),new RealTimeRedisDataModel(cache, rollingData01, rollingData02, mLastPt, ql, convex, ps, ps1, ps2, ps3, ps4, polygon, polygon1, polygon2, polygon3, polygon4));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        }
                        //3.放入对应的总表t_1
                    } else {
                        /*如果包含*/
                        /*删除第一个*/
                        queue.poll();
                        /*在最后增加一个*/
                        queue.offer(orderNum);
                    }
                }

                //连续50次无数据时 将睡眠5秒


                Thread.sleep(10);
            } catch (Exception e) {
                log.info("出现异常");
                e.printStackTrace();
            }
        }
    }

    public static boolean useList(List<String> arr, String targetValue) {
        return arr.contains(targetValue);
    }

    public JSONObject draw(ConcurrentHashMap<Long, MatrixItem[][]> cache, List<Integer> long2Cols,
                           List<Integer> long2Rows, RollingData rollingData02) {
        MatrixItem[][] matrixItems = null;
        MatrixItem item = null;

        //筛选出最大最小列
        int[] col = new int[long2Cols.size()];//列
        for (int coli = 0; coli < long2Cols.size(); coli++) {
            if (StringUtils.isNotNull(long2Cols.get(coli))) {
                col[coli] = long2Cols.get(coli);
            } else {
                log.error("下标【" + coli + "】列为null");
            }
        }
        int[] row = new int[long2Rows.size()];//行
        for (int rowi = 0; rowi < long2Rows.size(); rowi++) {
            if (StringUtils.isNotNull(long2Rows.get(rowi))) {
                row[rowi] = long2Rows.get(rowi);
            } else {
                log.error("下标【" + rowi + "】行为null");
            }
        }

        int minCol = Arrays.stream(col).min().getAsInt();//xbg 最小列
        int maxCol = Arrays.stream(col).max().getAsInt();//xend 最大列
        int minRow = Arrays.stream(row).min().getAsInt();//ybg 最小行
        int maxRow = Arrays.stream(row).max().getAsInt();//yed 最大行

        int xSize = (maxCol - minCol) * RidUtil.R_LEN + RidUtil.R_LEN;
        int ySize = (maxRow - minRow) * RidUtil.R_LEN + RidUtil.R_LEN;
        int baseX = minCol * RidUtil.R_LEN;
        int baseY = minRow * RidUtil.R_LEN;
        BufferedImage bi = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) bi.getGraphics();
        for (Map.Entry<Long, MatrixItem[][]> entry : cache.entrySet()) {
            // 10*10 小方格
            long rid = entry.getKey();
            matrixItems = entry.getValue();
            int dltaX = RidUtil.long2Col(rid) * RidUtil.R_LEN - baseX;
            int dltaY = RidUtil.long2Row(rid) * RidUtil.R_LEN - baseY;
            for (int i = 0; i < RidUtil.R_LEN - 1; i++) {
                for (int j = 0; j < RidUtil.R_LEN - 1; j++) {
                    item = matrixItems[i][j];
                    if (item != null) {
                        int rollingTimes = item.getRollingTimes();
                        g2.setColor(getColorByCount2(rollingTimes));
                        g2.fillRect(i + dltaX, j + dltaY, 2, 2);
                    }
                }
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String bsae64_string = "";
        try {
            ImageIO.write(bi, "PNG", baos);
            byte[] bytes = baos.toByteArray();//转换成字节
            bsae64_string = "data:image/png;base64," + Base64.encodeBase64String(bytes);
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject result = new JSONObject();
        result.put("tablename", "storehouseId");//图片占的网格编号
        result.put("index", item);//图片占的网格编号

        Double VibrateValue = 0D;//rollingData2.getVibrateValue();
        int iszhen = 0;
        if (VibrateValue > 40D) {
            iszhen = 1;
        }
        result.put("iszhen", iszhen);

        result.put("zhuangX", rollingData02.getZhuangX());
        result.put("zhuangY", rollingData02.getZhuangY());
        result.put("speed", rollingData02.getSpeed());//车辆速度
        result.put("elevation", rollingData02.getElevation());//车辆高程
        result.put("base64", bsae64_string);

        //通过轨迹多边形获得当前碾压区域的左上角点和右下角点
        RollingDataRange rollingDataRange = new RollingDataRange();
        rollingDataRange.setMaxCoordX(Double.valueOf(maxCol));
        rollingDataRange.setMinCoordX(Double.valueOf(minCol));
        rollingDataRange.setMaxCoordY(Double.valueOf(maxRow));
        rollingDataRange.setMinCoordY(Double.valueOf(minRow));
        result.put("rollingDataRange", rollingDataRange);

        ProjCoordinate projLeftBottom = new ProjCoordinate(minRow * RidUtil.R_LEN, minCol * RidUtil.R_LEN, 10);
        ProjCoordinate projRightTop = new ProjCoordinate(maxRow * RidUtil.R_LEN + RidUtil.R_LEN, maxCol * RidUtil.R_LEN + RidUtil.R_LEN, 10);
        result.put("pointLeftBottom", projLeftBottom);
        result.put("pointRightTop", projRightTop);

        result.put("height", 0);

        ProjCoordinate markPos = new ProjCoordinate(rollingData02.getZhuangY(), rollingData02.getZhuangX(), 10);
        result.put("markPos", markPos);

        result.put("carId", rollingData02.getVehicleID());//carId
        result.put("title", "1车正在施工......");
        result.put("angle", rollingData02.getAngle());
        double vcv = rollingData02.getVibrateValue();
//        if(vcv != 0){
//            vcv = 370 + 10 * Math.random();
//        }
        result.put("amplitude", vcv);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date t = new Date();
        t.setTime(rollingData02.getTimestamp());
        result.put("timestamp", sdf.format(t));
        return result;
    }

    public Color getColorByCount2(Integer count) {

        Color color = colorMap.get(count);
        if (StringUtils.isNotNull(color)) {
            return color;
        } else {
            if (count.intValue() > 0) {
                Integer maxKey = (Integer) MapUtil.getMaxKey(colorMap);//取最大key
                return colorMap.get(maxKey);
            } else if (count.intValue() == 0) {
                return new Color(255, 255, 255, 0);
            }
        }
        return null;
    }

    public void getColorMap(TColorConfigMapper colorConfigMapper) {
        TColorConfig vo = new TColorConfig();
        vo.setType(1L);//碾压遍次
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
}

