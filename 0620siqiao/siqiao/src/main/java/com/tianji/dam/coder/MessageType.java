package com.tianji.dam.coder;

import java.util.HashMap;

/**
 * 数据类型
 */
public enum MessageType {
    Heart(0),
    SysRate(1),
    SysTrack(2),
    SysTrackFOUR(4),
    SysBulldozer(3),
    SysCar(10);
    private int value;

    MessageType(int value) {
        this.value = value;
    }

    private static final HashMap<Integer, MessageType> intToEnum = new HashMap<Integer, MessageType>();

    static {
        for (MessageType val : values()) {
            intToEnum.put(val.value, val);
        }
    }

    public static MessageType fromInt(int i) {
        return intToEnum.get(i);
    }

    public int toInt() {
        return value;
    }
}
