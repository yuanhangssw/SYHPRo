package com.tj.quartz.task;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.domain.Gpsinfo;
import com.tianji.dam.mapper.GpsinfoMapper;
import com.tianji.dam.utils.HttpClientUtils;
import com.tj.common.utils.DateUtils;
import org.apache.http.util.TextUtils;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component("GPSLocation")
public class GPSLocationTask {

    private static GpsinfoMapper mapper;


    public static void main(String[] args) {
        gpslogin();


    }

    public static void gpslogin() {
//        try{
//            mapper = BeanContext.getApplicationContext().getBean(GpsinfoMapper.class);
//            //获取用户key
//            String loginurl = "http://211.162.76.156:89/gpsonline/GPSAPI?version=1&method=loginSystem&name=tjkj&pwd=123456";
//            String a = HttpClientUtils.doGet(loginurl);
//           // System.out.println(a);
//            JSONObject ob = JSONObject.parseObject(a);
//            String uid = ob.getString("uid");
//            String ukey = ob.getString("uKey");
//
//            //获取车辆列表
//            String carlisturl = "http://211.162.76.156:89/gpsonline/GPSAPI?version=1&method=loadVehicles&uid=" + uid + "&uKey=" + ukey;
//
//            String carlist = HttpClientUtils.doGet(carlisturl);
//
//            JSONObject ob_carlist = JSONObject.parseObject(carlist);
//            JSONArray carlistarry = JSONArray.parseArray(ob_carlist.getString("groups"));
//
//            String today = DateUtils.getDate();
//            String begintime =" 07:30:00";
//            String endtime =" 17:30:00";
//
//            Long beginl  = DateUtils.dateTime("yyyy-MM-dd HH:mm:ss",today+begintime).getTime();
//            Long endl =DateUtils.dateTime("yyyy-MM-dd HH:mm:ss",today+endtime).getTime();
//            Long nowtime =DateUtils.getNowDate().getTime();
//            for (Object o : carlistarry) {
//
//                JSONArray vehicles = JSONArray.parseArray(JSONObject.parseObject(o.toString()).getString("vehicles"));
//
//                for (Object vehicle : vehicles) {
//                    JSONObject vehicleob = JSONObject.parseObject(vehicle.toString());
//
//                    String carid = vehicleob.get("id").toString();
//                    String carname = vehicleob.get("name").toString();
//                    String carkey = vehicleob.get("vKey").toString();
//                    String locationurl = "http://211.162.76.156:89/gpsonline/GPSAPI?version=1&method=loadLocation&vid=" + carid + "&vKey=" + carkey;
//
//                    String carlocation = HttpClientUtils.doGet(locationurl);
//
//                    JSONObject ob_location = JSONObject.parseObject(carlocation);
//
//                    JSONArray locationarry = JSONArray.parseArray(ob_location.getString("locs"));
//                    boolean  iswork =true;
//                    for (Object object : locationarry) {
//                        JSONObject location = JSONObject.parseObject(object.toString());
//
//                        long timestamp = Long.parseLong(location.getString("gpstime")) ;
//                        timestamp  =new Date().getTime();
////                        if(timestamp < 1672502400000L){
////                            //当gps时间错误小于2023-01-01 00:00:00时 使用服务器接收时间
////                            timestamp = Long.parseLong(location.getString("recvtime"));
////                        }
////                        //如果接受时间和当前时间差距2个小时，则不再处理这一条数据。
////                        Long timepass =  (timestamp - new Date().getTime())/1000;
////
////                        if(timepass > 7200){
////                            continue;
////                        }
//
//                        //只上传工作时间段内的数据。
//                        if(timestamp<beginl||timestamp>endl||nowtime<beginl||nowtime>endl){
//                            iswork =false;
//                        }
//
//                        // System.out.println(location);
//                        String idCard = location.getString("gprs");
//                        LatLngfilter latLngfilter = FilterMap.getFilter(idCard);
//                        //只保存在线的设备信息
//                        Gpsinfo info = new Gpsinfo();
//                        info.setIdcard(idCard);
//                        info.setLatitude(location.get("lat").toString());
//                        info.setLongitude(location.get("lng").toString());
//                        info.setSpeed(location.get("speed").toString());
//
//                        String status = location.getString("state");//车辆状态 包含电量 定位状态 停车状态
//                        String av = location.getString("av");//有效性 1有效 0无效
//                        boolean islbs =false;
//                        boolean ispower =false;
//                        if(!av.equals("1")){
//                            System.out.println(""+carid+"的卡，定位无效"+location.toString());
//                            //无效定位 剔除
//                            continue;
//                        }
//                        if(status.contains("补发")){
//                                continue;
//                        }
////                        if(status.contains("WIFI")&&!carid.equals("47502008996")&&!carid.equals("47502008990")&&!carid.equals("47502008968")){
////                            islbs = true;
////                        }
////                        if(status.contains("LBS")&&!carid.equals("47502008996")&&!carid.equals("47502008990")&&!carid.equals("47502008968")){
////                             islbs = true;
////                        }
//                        if (status.contains("电量")) {
//                            ispower = true;
//                            status = status.substring(status.indexOf("电量") + 2);
//                            status = status.substring(1, status.indexOf(")"));
//                            info.setElectricity(status + "");
//                        } else {
//                            //没有电量的情况为 未定位 不在线 不上报
//                            continue;
//                        }
//                        String lastStatus = latLngfilter.getIsOnline();
//
//                        if(info.getElectricity().equals("0%")) {
//                            //没有电量认定为离线 从在线变离线 需要是上报一条记录
//                            if(lastStatus.equals("0")){
//                                info.setStatus("1");
//                                latLngfilter.setIsOnline(info.getStatus());
//                            }else{
//                                //设备离线
//                                continue;
//                            }
//                        }else{
//                            //有电量认定为在线
//                            info.setStatus("0");
//                            latLngfilter.setIsOnline(info.getStatus());
//                        }
//                        info.setCheck(location.getString("isOnline"));
//                        info.setDirectionangle(location.getString("direct"));
//
//
//                        Date gpstime = new Date(timestamp);
//                        String datetime = DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", gpstime);
//                        Float lat1 = Float.parseFloat(location.get("lat").toString());
//                        Float lng1 = Float.parseFloat(location.get("lng").toString());
//                        if (lat1 != 0.0 && lng1 != 0.0) {
//                            if(islbs){
//                                //如果是LBS数据则舍弃经纬度
//                                lat1 = 0f;
//                                lng1 = 0f;
//                            }
//                            //单独过滤每个号牌 不采用统一过滤的方式
//                            float finallat = lat1;
//                            float finallng = lng1;
//                            try {
//                                if(finallat < 29.08 && finallat>29.00 && finallng > 118.7 && finallng < 118.8 && iswork){
//                                    //在电子围栏区域内 进行坐标过滤
//                                    finallat = (float) latLngfilter.filterLat(finallat);
//                                    finallng = (float) latLngfilter.filterLng(finallng);
//                                } else{
//                                    //电子围栏外 坐标直接归零
//                                    finallat = 0;
//                                    finallng = 0;
//                                }
//                            } catch (NumberFormatException e) {
//                                finallat = 0;
//                                finallng = 0;
//                                System.out.println("经纬度过滤器出现问题："+e.getMessage());
//                            } finally {
//                                info.setLatitude(String.valueOf(finallat));
//                                info.setLongitude(String.valueOf(finallng));
//                                info.setTime(datetime);
//                                //找一个字段存储json串  获取所有数据
//                                info.setLength(location.toString());
//                                mapper.insertGpsinfo(info);
//                            }
//                        }
//                    }
//                }
//            }
//        }catch (Exception ex){
//            System.out.println("gpslogin Exception:" + ex.getMessage());
//        }
    }

}
