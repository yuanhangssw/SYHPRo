package com.tianji.dam.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Byte数组与基础类型转换工具类
 */
public class ByteUtils {
    /**
     * <pre>
     * 将4个byte数字组成的数组合并为一个float数.
     * </pre>
     */
    public static float byte4ToFloat(byte[] arr,int index) {
        if (arr == null || arr.length != 4) {
            throw new IllegalArgumentException("byte数组必须不为空,并且是4位!");
        }
        int i = byte4ToInt(arr,index);
        return Float.intBitsToFloat(i);
    }

    /**
     * <pre>
     * 将一个float数字转换为4个byte数字组成的数组.
     * </pre>
     */
    public static byte[] floatToByte4(float f) {
        int i = Float.floatToIntBits(f);
        return intToByte4(i);
    }

    /**
     * <pre>
     * 将八个byte数字组成的数组转换为一个double数字.
     * </pre>
     */
    public static double byte8ToDouble(byte[] arr) {
        if (arr == null || arr.length != 8) {
            throw new IllegalArgumentException("byte数组必须不为空,并且是8位!");
        }
        long l = byte8ToLong(arr);
        return Double.longBitsToDouble(l);
    }

    /**
     * <pre>
     * 将一个double数字转换为8个byte数字组成的数组.
     * </pre>
     */
    public static byte[] doubleToByte8(double i) {
        long j = Double.doubleToLongBits(i);
        return longToByte8(j);
    }

    /**
     * <pre>
     * 将一个char字符转换为两个byte数字转换为的数组.
     * </pre>
     */
    public static byte[] charToByte2(char c) {
        byte[] arr = new byte[2];
        arr[0] = (byte) (c >> 8);
        arr[1] = (byte) (c & 0xff);
        return arr;
    }

    /**
     * <pre>
     * 将2个byte数字组成的数组转换为一个char字符.
     * </pre>
     */
    public static char byte2ToChar(byte[] arr) {
        if (arr == null || arr.length != 2) {
            throw new IllegalArgumentException("byte数组必须不为空,并且是2位!");
        }
        return (char) (((char) (arr[0] << 8)) | ((char) arr[1]));
    }

    /**
     * <pre>
     * 将一个16位的short转换为长度为2的8位byte数组.
     * </pre>
     */
    public static byte[] shortToByte2(Short s) {
        byte[] arr = new byte[2];
        arr[0] = (byte) (s >> 8);
        arr[1] = (byte) (s & 0xff);
        return arr;
    }

    /**
     * <pre>
     * 长度为2的8位byte数组转换为一个16位short数字.
     * </pre>
     */
    public static short byte2ToShort(byte[] arr) {
        if (arr != null && arr.length != 2) {
            throw new IllegalArgumentException("byte数组必须不为空,并且是2位!");
        }
        return (short) (((short) arr[0] << 8) | ((short) arr[1] & 0xff));
    }

    /**
     * <pre>
     * 将short转换为长度为16的byte数组.
     * 实际上每个8位byte只存储了一个0或1的数字
     * 比较浪费.
     * </pre>
     */
    public static byte[] shortToBit16(short s) {
        byte[] arr = new byte[16];
        for (int i = 15; i >= 0; i--) {
            arr[i] = (byte) (s & 1);
            s >>= 1;
        }
        return arr;
    }

    /**
     * 将长度为8的byte数组转换为short
     */
    public static short bit16ToShort(byte[] arr) {
        if (arr == null || arr.length != 16) {
            throw new IllegalArgumentException("byte数组必须不为空,并且长度为16!");
        }
        short sum = 0;
        for (int i = 0; i < 16; ++i) {
            sum |= (arr[i] << (15 - i));
        }
        return sum;
    }

    /**
     * 字节数组转float
     * 采用IEEE 754标准
     */
    public static float bytes2Float(byte[] bytes){
        //获取 字节数组转化成的2进制字符串
        String BinaryStr = bytes2BinaryStr(bytes);
        //符号位S
        Long s = Long.parseLong(BinaryStr.substring(0, 1));
        //指数位E
        Long e = Long.parseLong(BinaryStr.substring(1, 9),2);
        //位数M
        String M = BinaryStr.substring(9);
        float m = 0,a,b;
        for(int i=0;i<M.length();i++){
            a = Integer.valueOf(M.charAt(i)+"");
            b = (float) Math.pow(2, i+1);
            m =m + (a/b);
        }
        Float f = (float) ((Math.pow(-1, s)) * (1+m) * (Math.pow(2,(e-127))));
        return f;
    }

    /**
     * 将字节数组转换成2进制字符串
     * @param bytes
     */
    public static String bytes2BinaryStr(byte[] bytes){
        StringBuffer binaryStr = new StringBuffer();
        for(int i=0;i<bytes.length;i++){
            String str = Integer.toBinaryString((bytes[i] & 0xFF) + 0x100).substring(1);
            binaryStr.append(str);
        }
        return binaryStr.toString();
    }


    /**
     * <pre>
     * 将32位int转换为由四个8位byte数字.
     * </pre>
     */
    public static byte[] intToByte4(int sum) {
        byte[] arr = new byte[4];
        arr[0] = (byte) (sum >> 24);
        arr[1] = (byte) (sum >> 16);
        arr[2] = (byte) (sum >> 8);
        arr[3] = (byte) (sum & 0xff);
        return arr;
    }

    /**
     * <pre>
     * 将长度为4的8位byte数组转换为32位int.
     * </pre>
     */
    public static int byte4ToInt(byte[] arr,int index) {
        if (arr == null || arr.length != 4) {
            throw new IllegalArgumentException("byte数组必须不为空,并且是4位!");
        }
        return (int) (((arr[index] & 0xff) << 24) |
                ((arr[index+1] & 0xff) << 16) |
                ((arr[index+2] & 0xff) << 8) |
                ((arr[index+3] & 0xff)));
    }

    /**
     * 32bit整数 返回对应bit位数据
     * @param num
     * @param cursor
     * @return
     */
    public static int int32ToBit(int num,int cursor){
        int[] binaryArray = new int[32];
        for(int i = 31;i >=0;i--){
            binaryArray[i] = num & 1;//取出最低位
            num = num >>> 1;//无符号右移一位
        }
        return binaryArray[cursor];
    }

    /**
     * <pre>
     * 将长度为8的8位byte数组转换为64位long.
     * </pre>
     * <p>
     * 0xff对应16进制,f代表1111,0xff刚好是8位 byte[]
     * arr,byte[i]&0xff刚好满足一位byte计算,不会导致数据丢失. 如果是int计算. int[] arr,arr[i]&0xffff
     */
    private static long byte8ToLong(byte[] arr) {
        if (arr == null || arr.length != 8) {
            throw new IllegalArgumentException("byte数组必须不为空,并且是8位!");
        }
        return (long) (((long) (arr[0] & 0xff) << 56) | ((long) (arr[1] & 0xff) << 48) | ((long) (arr[2] & 0xff) << 40)
                | ((long) (arr[3] & 0xff) << 32) | ((long) (arr[4] & 0xff) << 24)
                | ((long) (arr[5] & 0xff) << 16) | ((long) (arr[6] & 0xff) << 8) | ((long) (arr[7] & 0xff)));
    }

    /**
     * 将一个long数字转换为8个byte数组组成的数组.
     */
    private static byte[] longToByte8(long sum) {
        byte[] arr = new byte[8];
        arr[0] = (byte) (sum >> 56);
        arr[1] = (byte) (sum >> 48);
        arr[2] = (byte) (sum >> 40);
        arr[3] = (byte) (sum >> 32);
        arr[4] = (byte) (sum >> 24);
        arr[5] = (byte) (sum >> 16);
        arr[6] = (byte) (sum >> 8);
        arr[7] = (byte) (sum & 0xff);
        return arr;
    }

    /**
     * <pre>
     * 将int转换为32位byte.
     * 实际上每个8位byte只存储了一个0或1的数字
     * 比较浪费.
     * </pre>
     */
    public static byte[] intToBit32(int num) {
        byte[] arr = new byte[32];
        for (int i = 31; i >= 0; i--) {
            // &1 也可以改为num&0x01,表示取最地位数字.
            arr[i] = (byte) (num & 1);
            // 右移一位.
            num >>= 1;
        }
        return arr;
    }

    /**
     * <pre>
     * 将长度为32的byte数组转换为一个int类型值.
     * 每一个8位byte都只存储了0或1的数字.
     * </pre>
     */
    public static int bit32ToInt(byte[] arr) {
        if (arr == null || arr.length != 32) {
            throw new IllegalArgumentException("byte数组必须不为空,并且长度是32!");
        }
        int sum = 0;
        for (int i = 0; i < 32; ++i) {
            sum |= (arr[i] << (31 - i));
        }
        return sum;
    }

    /**
     * <pre>
     * 将长度为64的byte数组转换为一个long类型值.
     * 每一个8位byte都只存储了0或1的数字.
     * </pre>
     */
    public static long bit64ToLong(byte[] arr) {
        if (arr == null || arr.length != 64) {
            throw new IllegalArgumentException("byte数组必须不为空,并且长度是64!");
        }
        long sum = 0L;
        for (int i = 0; i < 64; ++i) {
            sum |= ((long) arr[i] << (63 - i));
        }
        return sum;
    }

    /**
     * <pre>
     * 将一个long值转换为长度为64的8位byte数组.
     * 每一个8位byte都只存储了0或1的数字.
     * </pre>
     */
    public static byte[] longToBit64(long sum) {
        byte[] arr = new byte[64];
        for (int i = 63; i >= 0; i--) {
            arr[i] = (byte) (sum & 1);
            sum >>= 1;
        }
        return arr;
    }

    /**
     * byte使用16进制字符串表示
     */
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            //SignedInteger -> UnsignedInteger
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        String strHex = new String(hexChars);
        String regex = "(.{2})";
        return strHex.replaceAll(regex,"$1 ");
    }


    public static String bytesToHex(byte[] bytes,int cursor,int len) {
        char[] hexChars = new char[len * 2];
        for ( int j = 0; j < len; j++) {
            //SignedInteger -> UnsignedInteger
            int v = bytes[cursor+j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        String strHex = new String(hexChars);
        String regex = "(.{2})";
        return strHex.replaceAll(regex,"$1 ");
    }


    /**
     * byte[] 转 4位整型
     * 小端模式
     */
    public static int bytesToShort4(byte[] b) {
        int intValue = 0;
        for (int i = 0; i < b.length; i++) {
            intValue += (b[i] & 0xFF) << (8 * (3 - i));
        }
        return intValue;
    }

    /**
     * byte[] 转 2位整型
     * 小端模式
     */
    public static int bytesToShort2(byte[] arr) {
        int mask = 0xFF;
        int temp = 0;
        int result = 0;
        for (int i = 0; i < 2; i++) {
            result <<= 8;
            temp = arr[i] & mask;
            result |= temp;
        }
        return result;
    }

    /**
     * byte[] 转 无符号16位整型
     * 小端模式
     */
    public static short toInt16(byte[] bytes, int offset) {
        short result = (short) ((int)bytes[offset]&0xff);
        result |= ((int)bytes[offset+1]&0xff) << 8;
        return (short) (result & 0xffff);
    }

    /**
     * byte[] 转 无符号32位整型
     * 小端模式
     */
    public static int toInt32(byte[] bytes, int offset) {
        int result = (int)bytes[offset]&0xff;
        result |= ((int)bytes[offset+1]&0xff) << 8;
        result |= ((int)bytes[offset+2]&0xff) << 16;
        result |= ((int)bytes[offset+3]&0xff) << 24;

        return result;
    }

    /**
     * byte[] 转 无符号64位整型
     * 小端模式
     */
    public static long toUInt64(byte[] bytes, int offset) {
        long result = 0;
        for (int i = 0; i <= 56; i += 8) {
            result |= ((int)bytes[offset++]&0xff) << i;
        }

        return result;
    }

    /**
     * byte[] 转 单精度float
     * @param order:大端小端模式
     */
    public static float toSingle(byte[] b, long offset, ByteOrder order) {
        ByteBuffer buf = ByteBuffer.wrap(b, (int) offset, 4);
        buf.order(order);
        return buf.getFloat();
    }

    /**
     * byte[] 转 double
     * 小端模式
     */
    public static double toDouble(byte[] b, long offset) {
        ByteBuffer buf = ByteBuffer.wrap(b, (int) offset, 8);
        double outp = buf.getDouble();
        return outp;
    }

    /**
     * byte[] 转 double
     * @param order:大端小端模式
     */
    public static double toDouble(byte[] b, long offset, ByteOrder order) {
        ByteBuffer buf = ByteBuffer.wrap(b, (int) offset, 8);
        buf.order(order);
        double outp = buf.getDouble();
        return outp;
    }

    /**
     * byte[] 转 long
     * @param order:大端小端模式
     */
    public static long toLong(byte[] b, long offset, ByteOrder order) {
        ByteBuffer buf = ByteBuffer.wrap(b, (int) offset, 8);
        buf.order(order);
        return buf.getLong();
    }

    //低端字节序
    public static byte[] intToBytes(int value) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (value >> 24);
        bytes[1] = (byte) (value >> 16);
        bytes[2] = (byte) (value >> 8);
        bytes[3] = (byte) (value);
        return bytes;
    }
    //大端字节序
    public static byte[] getBytes(int value) {
        byte[] bytes = new byte[4];
        bytes[3] = (byte) (value >> 24);
        bytes[2] = (byte) (value >> 16);
        bytes[1] = (byte) (value >> 8);
        bytes[0] = (byte) (value);
        return bytes;
    }

    //大端字节序
    public static byte[] getBytes(float f)
    {
        byte writeBuffer[]= new byte[4];
        long v = Float.floatToIntBits(f)  ;
        writeBuffer[0] = (byte)(v >>> 24);
        writeBuffer[1] = (byte)(v >>> 16);
        writeBuffer[2] = (byte)(v >>>  8);
        writeBuffer[3] = (byte)(v >>>  0);
        return writeBuffer;
    }

    //小端字节序
    public static byte[] getLBytes(float f)
    {
        byte writeBuffer[]= new byte[4];
        long v = Float.floatToIntBits(f)  ;
        writeBuffer[3] = (byte)(v >>> 24);
        writeBuffer[2] = (byte)(v >>> 16);
        writeBuffer[1] = (byte)(v >>>  8);
        writeBuffer[0] = (byte)(v >>>  0);
        return writeBuffer;
    }

    //低端字节序
    public static byte[] getLBytes(long value) {
        byte[] bytes = new byte[8];
        bytes[0] = (byte) (value >> 56);
        bytes[1] = (byte) (value >> 48);
        bytes[2] = (byte) (value >> 40);
        bytes[3] = (byte) (value >> 32);
        bytes[4] = (byte) (value >> 24);
        bytes[5] = (byte) (value >> 16);
        bytes[6] = (byte) (value >> 8);
        bytes[7] = (byte) (value);
        return bytes;
    }

    //大端字节序
    public static byte[] getBytes(long value) {
        byte[] bytes = new byte[8];
        bytes[7] = (byte) (value >> 56);
        bytes[6] = (byte) (value >> 48);
        bytes[5] = (byte) (value >> 40);
        bytes[4] = (byte) (value >> 32);
        bytes[3] = (byte) (value >> 24);
        bytes[2] = (byte) (value >> 16);
        bytes[1] = (byte) (value >> 8);
        bytes[0] = (byte) (value);
        return bytes;
    }

    //端
    public static byte[] getLBytes(double d)
    {
        byte writeBuffer[]= new byte[8];
        long v = Double.doubleToLongBits(d);
        writeBuffer[0] = (byte)(v >>> 56);
        writeBuffer[1] = (byte)(v >>> 48);
        writeBuffer[2] = (byte)(v >>> 40);
        writeBuffer[3] = (byte)(v >>> 32);
        writeBuffer[4] = (byte)(v >>> 24);
        writeBuffer[5] = (byte)(v >>> 16);
        writeBuffer[6] = (byte)(v >>>  8);
        writeBuffer[7] = (byte)(v >>>  0);
        return writeBuffer;
    }
    //大端
    public static byte[] getBytes(double d)
    {
        byte writeBuffer[]= new byte[8];
        long v = Double.doubleToLongBits(d);
        writeBuffer[7] = (byte)(v >>> 56);
        writeBuffer[6] = (byte)(v >>> 48);
        writeBuffer[5] = (byte)(v >>> 40);
        writeBuffer[4] = (byte)(v >>> 32);
        writeBuffer[3] = (byte)(v >>> 24);
        writeBuffer[2] = (byte)(v >>> 16);
        writeBuffer[1] = (byte)(v >>>  8);
        writeBuffer[0] = (byte)(v >>>  0);
        return writeBuffer;
    }
}
