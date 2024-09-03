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

    public static void gpslogin() {}

}
