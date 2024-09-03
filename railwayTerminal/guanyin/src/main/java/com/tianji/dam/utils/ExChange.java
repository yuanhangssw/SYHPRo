package com.tianji.dam.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * 实现基本数据类型与字节数组之间的相互转换;
 * 利用java提供的转化机制;
 * 序列化出去的数据使用小字节序列与C#的序列化一致;
 * 反序列化的原始数组是小字节序列
 */
@Slf4j
public class ExChange {

    public ExChange() {
        boutput = new ByteArrayOutputStream();
        doutput = new DataOutputStream(boutput);
    }

    public ExChange(boolean bBigEnding) {
        this.bBigEnding = bBigEnding;
        boutput = new ByteArrayOutputStream();
        doutput = new DataOutputStream(boutput);
    }

    public byte[] InttoBytes(int value) {
        try {
            doutput.writeInt(value);
            doutput.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ByteArrayReverse(boutput.toByteArray());
    }

    public byte[] LongtoBytes(long value) {
        try {
            doutput.writeLong(value);
            doutput.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ByteArrayReverse(boutput.toByteArray());
    }

    public byte[] FloattoBytes(float value) {
        try {
            doutput.writeFloat(value);
            doutput.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ByteArrayReverse(boutput.toByteArray());
    }

    public byte[] DoubletoBytes(double value) {
        try {
            doutput.writeDouble(value);
            doutput.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ByteArrayReverse(boutput.toByteArray());
    }

    /**
     * 只支持UTF-8编码
     *
     * @return
     */
    public byte[] StringtoBytes(String value) {
        try {
            byte[] bs = value.getBytes("UTF-8");
            return bs;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public void AddIntAsBytes(int value) {
        try {
            ByteArrayOutputStream temboutput = new ByteArrayOutputStream();
            DataOutputStream temdoutput = new DataOutputStream(temboutput);
            temdoutput.writeInt(value);
            temdoutput.flush();
            doutput.write(ByteArrayReverse(temboutput.toByteArray()));
            temdoutput.close();
            temboutput.close();
            doutput.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void AddLongAsBytes(long value) {
        try {
            ByteArrayOutputStream temboutput = new ByteArrayOutputStream();
            DataOutputStream temdoutput = new DataOutputStream(temboutput);
            temdoutput.writeLong(value);
            doutput.write(ByteArrayReverse(temboutput.toByteArray()));
            temdoutput.close();
            temboutput.close();
            doutput.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void AddFloatAsBytes(float value) {
        try {
            ByteArrayOutputStream temboutput = new ByteArrayOutputStream();
            DataOutputStream temdoutput = new DataOutputStream(temboutput);
            temdoutput.writeFloat(value);
            doutput.write(ByteArrayReverse(temboutput.toByteArray()));
            temdoutput.close();
            temboutput.close();
            doutput.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void AddDoubleAsBytes(double value) {
        try {
            ByteArrayOutputStream temboutput = new ByteArrayOutputStream();
            DataOutputStream temdoutput = new DataOutputStream(temboutput);
            temdoutput.writeDouble(value);
            doutput.write(ByteArrayReverse(temboutput.toByteArray()));
            temdoutput.close();
            temboutput.close();
            doutput.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void AddStringAsBytes(String value) {
        try {
            byte[] bs = value.getBytes("UTF-8");
            // 处理长度写入过程
            ByteArrayOutputStream temboutput = new ByteArrayOutputStream();
            DataOutputStream temdoutput = new DataOutputStream(temboutput);
            temdoutput.writeInt(bs.length);
            doutput.write(ByteArrayReverse(temboutput.toByteArray()));
            // 写入字符串内容
            doutput.write(bs);
            doutput.flush();
            temdoutput.close();
            temboutput.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void AddBooleanAsBytes(boolean value) {
        if (value) {
            try {
                doutput.write(1);
                doutput.flush();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else {
            try {
                doutput.write(0);
                doutput.flush();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void AddByte(byte val) {
        try {
            doutput.write(val);
            doutput.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void AddBytes(byte[] bs, boolean isNeedPrefixLength) {
        try {
            if (isNeedPrefixLength) {
                // 处理长度写入过程
                ByteArrayOutputStream temboutput = new ByteArrayOutputStream();
                DataOutputStream temdoutput = new DataOutputStream(temboutput);
                temdoutput.writeInt(bs.length);
                doutput.write(ByteArrayReverse(temboutput.toByteArray()));
            }
            // 处理内容写入过程
            doutput.write(bs);
            doutput.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public byte[] GetAllBytes() {
        byte[] bs = boutput.toByteArray();
        try {
            boutput.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            boutput.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bs;
    }

    public int BytestoInt(byte[] buf, int index) throws IOException {
        byte[] data = ByteArrayReverse(buf,index,4);
        ByteArrayInputStream bintput = new ByteArrayInputStream(data);
        DataInputStream dintput = new DataInputStream(bintput);
        try {
            int value = dintput.readInt();
            dintput.close();
            bintput.close();
            return value;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            dintput.close();
            bintput.close();
            throw e;
        }
    }

    public  int BytestoUInt16(byte[] buf, int index) throws IOException {
        ByteArrayInputStream bintput = new ByteArrayInputStream(ByteArrayReverse(buf,index,2));
        DataInputStream dintput = new DataInputStream(bintput);
        try {
            int value = dintput.readUnsignedShort();
            dintput.close();
            bintput.close();
            return value;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            dintput.close();
            bintput.close();
            throw e;
        }
    }

    public long BytestoLong(byte[] buf, int index) throws IOException {
        ByteArrayInputStream bintput = new ByteArrayInputStream(ByteArrayReverse(buf,index,8));
        DataInputStream dintput = new DataInputStream(bintput);
        try {
            long value = dintput.readLong();
            dintput.close();
            bintput.close();
            return value;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            dintput.close();
            bintput.close();
            throw e;
        }
    }

    public float BytestoFloat(byte[] buf, int index) throws IOException {

        ByteArrayInputStream bintput = new ByteArrayInputStream(ByteArrayReverse(buf,index,4));

        DataInputStream dintput = new DataInputStream(bintput);
        try {
            float value = dintput.readFloat();
            dintput.close();
            bintput.close();
            return value;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            dintput.close();
            bintput.close();
            throw e;
        }
    }

    public double BytestoDouble(byte[] buf, int index) throws IOException {
        ByteArrayInputStream bintput = new ByteArrayInputStream(ByteArrayReverse(buf,index,8));
        DataInputStream dintput = new DataInputStream(bintput);
        try {
            double value = dintput.readDouble();
            dintput.close();
            bintput.close();
            return value;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            dintput.close();
            bintput.close();
            throw e;
        }
    }

    /**
     * 只支持UTF-8编码，在该函数中处理字符长度，并提取字符串
     * @param resolveLen:字符前有4byte描述后边字符长度,先根据4byte取得字符长度后进行解析
     */
    public String BytestoString(byte[] buf, int index, int length,
                                boolean resolveLen) throws IOException {
        byte[] bs;
        if (resolveLen) {
            int len = BytestoInt(buf, index);
            // 拷贝足够长度的字节数组
            bs = new byte[len];
            System.arraycopy(buf, index + 4, bs, 0, bs.length);
        } else {
            bs = new byte[length];
            System.arraycopy(buf, index, bs, 0, bs.length);
        }
        return new String(bs, "UTF-8");
    }

    public boolean ByteToBoolean(byte[] buf, int index) throws IOException {
        if (index >= buf.length)
            throw new IOException();
        else {
            if (buf[index] == 1)
                return true;
            else
                return false;
        }
    }

    public byte[] ExtractBytes(byte[] buf, int index, int length, boolean resolveLen) throws IOException {
        byte[] bs;
        if (resolveLen) {
            int len = BytestoInt(buf, index);
            // 拷贝足够长度的字节数组
            bs = new byte[len];
        } else {
            bs = new byte[length];
        }
        System.arraycopy(buf, index + 4, bs, 0, bs.length);
        return bs;
    }


    private byte[] ByteArrayReverse(byte[] bs,int index,int len) {
        if(bBigEnding)
            return bs;
        byte[] dest = new byte[len];
        System.arraycopy(bs, index, dest, 0, len);
        return ByteArrayReverse(dest);
    }

    // 将字节数组倒序，从而与windows平台兼容 小端模式
    private byte[] ByteArrayReverse(byte[] bs) {
        if(bBigEnding)
            return bs;
        for (int i = 0; i < bs.length / 2; i++) {
            byte temp = bs[i];
            bs[i] = bs[bs.length - 1 - i];
            bs[bs.length - 1 - i] = temp;
        }
        return bs;
    }
    private boolean bBigEnding=true;
    private ByteArrayOutputStream boutput;
    private DataOutputStream doutput;
}

