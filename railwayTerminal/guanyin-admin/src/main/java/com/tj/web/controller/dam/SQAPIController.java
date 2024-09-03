package com.tj.web.controller.dam;


import com.tianji.dam.mapper.SQMapper;
import com.tj.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/devicegpslocation")
public class SQAPIController {
    @Autowired
    SQMapper mapper;

    /**
     * 获取设备的状态
     * @return
     */
    @GetMapping("/devicestatus")
    public AjaxResult getdevicestatus(){

        List<Map<String,Object>> alldevicestatus =new ArrayList<>();
        List<String> devicelist = mapper.getdevicelist();
        System.out.println("获取到设备："+devicelist.size()+"个");
        for (String integer : devicelist) {
            Map<String, Object> devicestatus = mapper.getdevicestatus(integer);
            alldevicestatus.add(devicestatus);
        }
        System.out.println("设备状态："+alldevicestatus.size()+"个");
        return AjaxResult.success(alldevicestatus);

    }

    /**
     * 获取设备的定位数据
     * @return
     */
    @GetMapping("/devicelocation")
    public AjaxResult getdevicelocation(){
    List<Map<String,Object>> alldevice =new ArrayList<>();
    List<String> devicelist = mapper.getdevicelist();
        System.out.println("获取到设备："+devicelist.size()+"个");
        for (String integer : devicelist) {
            Map<String,Object> deviceinfo =    mapper.getdeviveinfobyid(integer);
            alldevice.add(deviceinfo);
        }
        System.out.println("设备定位："+alldevice.size()+"个");

        return  AjaxResult.success(alldevice);
    }

}


