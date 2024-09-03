package com.tj.quartz.task;

public class LatLngfilter {
    private  String carId;

    private String isOnline = "1";//是否在线
    private  RecycleQueue latQuene=new RecycleQueue(5,3);
    private  RecycleQueue lngQuene=new RecycleQueue(5,3);

    public LatLngfilter(String carId) {
        this.carId = carId;
    }

    public double filterLat(double lat){
        return  latQuene.autoFilter(lat);
    }

    public double filterLng(double lng){
        return  lngQuene.autoFilter(lng);
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }
}
