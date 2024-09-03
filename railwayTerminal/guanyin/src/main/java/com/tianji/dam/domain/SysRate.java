package com.tianji.dam.domain;

import java.io.Serializable;

import com.tianji.dam.utils.ExChange;

import lombok.extern.slf4j.Slf4j;

/**
 * 轨迹确认实体
 * UUID:层号-材料号
 * ownerCar:发出轨迹的车辆ID
 * otherCar:接收轨迹的车辆ID
 * OrderNum:轨迹点的顺序号
 */
@Slf4j
public class SysRate implements Serializable {
    private int id;
    private String uuid;
    private String ownerCar;
    private String otherCar;
    private int orderNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getOwnerCar() {
        return ownerCar;
    }

    public void setOwnerCar(String ownerCar) {
        this.ownerCar = ownerCar;
    }

    public String getOtherCar() {
        return otherCar;
    }

    public void setOtherCar(String otherCar) {
        this.otherCar = otherCar;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public byte[] toBinary() {
        ExChange ex = new ExChange(false);
        try{
            ex.AddStringAsBytes(uuid);
            ex.AddStringAsBytes(ownerCar);
            ex.AddStringAsBytes(otherCar);
            ex.AddIntAsBytes(orderNum);
        }catch (Exception e){
            log.error("toBinary:" + e.getMessage());
        }
        return ex.GetAllBytes();
    }
}
