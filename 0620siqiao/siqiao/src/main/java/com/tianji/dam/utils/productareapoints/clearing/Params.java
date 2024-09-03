package com.tianji.dam.utils.productareapoints.clearing;

public class Params {
    /**
     * 压路机轮宽 单位m
     */
    private float width = 2.4f;
    /**
     * 搭接 单位m
     */
    private float lap;
    /**
     * 开始时间戳 单位 毫秒
     */
    private long startTime;
    /**
     * 设计最小速度 单位km/h
     */
    private float minSpeed;
    /**
     * 设计最大速度 单位km/h
     */
    private float maxSpeed;
    /**
     * 目标遍数 一般为偶数
     */
    private int target;

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getLap() {
        return lap;
    }

    public void setLap(float lap) {
        this.lap = lap;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public float getMinSpeed() {
        return minSpeed;
    }

    public void setMinSpeed(float minSpeed) {
        this.minSpeed = minSpeed;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }
}
