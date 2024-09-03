package com.tianji.dam.utils.productareapoints;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Track {
    public double n;
    public double e;
    public double speed;
    public long time;

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "(" + e + "," + n + ")" + "-" + speed + "km/h" + "-" + dateFormat.format(new Date(time));
    }
}
