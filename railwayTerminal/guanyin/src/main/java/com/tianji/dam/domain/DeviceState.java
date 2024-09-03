package com.tianji.dam.domain;


import com.tianji.dam.utils.ExChange;

import lombok.extern.slf4j.Slf4j;

/**
 * 车辆状态
 */
@Slf4j
public class DeviceState {
    private String deviceId;
    private int state;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public byte[] toBinary() {
        ExChange ex = new ExChange(false);
        try{
            ex.AddStringAsBytes(deviceId);
            ex.AddIntAsBytes(state);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return ex.GetAllBytes();
    }

}
