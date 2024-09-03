package com.tj.quartz.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tianji.dam.bean.BeanContext;
import com.tianji.dam.domain.Gpsinfo;
import com.tianji.dam.mapper.GpsinfoMapper;
import com.tianji.dam.utils.HttpClientUtils;
import org.springframework.stereotype.Component;
import com.tj.common.utils.StringUtils;

/**
 * 定时任务调度测试
 * 
 * @author ruoyi
 */
@Component("ryTask")
public class RyTask
{
    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i)
    {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }
    public static void gpslogin(){

        GpsinfoMapper     mapper = BeanContext.getApplicationContext().getBean(GpsinfoMapper.class);
        //获取用户key
        String loginurl ="http://211.162.76.156:89/gpsonline/GPSAPI?version=1&method=loginSystem&name=tjkj&pwd=123456";
        String a =  HttpClientUtils.doGet(loginurl);
        System.out.println(a);
        JSONObject ob =JSONObject.parseObject( a)  ;
        String uid = ob.getString("uid");
        String ukey =ob.getString("uKey");

        //获取车辆列表
        String carlisturl ="http://211.162.76.156:89/gpsonline/GPSAPI?version=1&method=loadVehicles&uid="+uid+"&uKey="+ukey;

        String carlist =  HttpClientUtils.doGet(carlisturl);

        JSONObject ob_carlist =JSONObject.parseObject(carlist);
        JSONArray carlistarry = JSONArray.parseArray(ob_carlist.getString("groups"));
        for (Object o : carlistarry) {

            JSONArray vehicles = JSONArray.parseArray(JSONObject.parseObject(o.toString()).getString("vehicles"));

            for (Object vehicle : vehicles) {
                JSONObject vehicleob=    JSONObject.parseObject(vehicle.toString());

                String carid =vehicleob.get("id").toString();
                String carname=  vehicleob.get("name").toString();
                String carkey =  vehicleob.get("vKey").toString();
                String locationurl ="http://211.162.76.156:89/gpsonline/GPSAPI?version=1&method=loadLocation&vid="+carid+"&vKey="+carkey;

                String carlocation =  HttpClientUtils.doGet(locationurl);

                JSONObject ob_location = JSONObject.parseObject(carlocation);

                JSONArray  locationarry = JSONArray.parseArray(ob_location.getString("locs"));

                for (Object object : locationarry) {
                    JSONObject location = JSONObject.parseObject(object.toString());
                    System.out.println(location);

                    Boolean  ison =location.getBoolean("isOnline");
                    if(ison){
                        Gpsinfo info = new Gpsinfo();
//{"distance":0,"direct":0,"isOnline":true,"temp2":"","temp3":"","speed":0,"stopDefDis":0,"temp1":"","oil":0,"oil2":0,"lng_xz":0.004959,"voicecode":"","stopDefLat":0,"gprs":"47502008985",
// "id":18390526,"state":"停车超时(7时7分) LBS(精度65) WIFI(精度65)  电量(100%)","lat":29.070475,"info":"浙江省 衢州市 柯城区 石梁镇 花木线 毛家口北219米","vhcofflinemin":10,"temp":0,
// "lng":118.76766,"stopDefLng":0,"baidu_lng_xz":0.011466,"mobile":"1441498355908","alt":0,"oil1":0,"temp4":"","recvtime":1697730285000,"baidu_lat_xz":0.003192,"av":"1",
// "name":"47502008985","lat_xz":-0.002861,"gpstime":1697730182000,"oilMN2":0,"oilMN1":0,"totalDis":0,"datacode":"","istate":0}
                        info.setIdcard(location.getString("gprs"));
                        info.setLatitude(location.get("lat").toString());
                        info.setLongitude(location.get("lng").toString());
                        info.setStatus("0");
                        info.setSpeed(location.get("speed").toString());
                        String status =location.getString("status");
                        if(status.contains("电量")){
                            status =status.substring(status.indexOf("电量")+2);
                            status=status.substring(1,status.length()-2);
                            info.setElectricity(status+"");
                        }else{
                            info.setElectricity("100");
                        }
                        //Long time   =location.getLong("gpstime");
                        // Date gpstime =   new Date(time);

                        mapper.insertGpsinfo(info);
                    }

                    //只保存在线的设备信息




                }
            }
        }
    }


}
