package com.tj.quartz.task;


import java.util.Arrays;

/**
 * 循环队列
 */
public class RecycleQueue {
    private static final String TAG = "RecycleQueue";
    private double[] vector;
    private int maxSize;
    private int front = 0;
    private int rear = 0;
    //过滤半径
    private int radius = 3;

    /**
     * 采用空闲单元法
     * maxSize必须比capacity大1
     * @param capacity
     */
    public RecycleQueue(int capacity, int radius){
        this.maxSize = capacity + 1;
        this.radius = radius;
        vector = new double[maxSize];
        front = 0;
        rear = 0;
    }

    public RecycleQueue(double[] objects){
        this(objects.length,3);
        for(double object : objects){
            EnQueue(object);
        }
    }

    public boolean EnQueue(double element) {
        if(isFull()){
            return false;
        }
        vector[rear % maxSize] = element;
        rear = ++rear % maxSize;
        return true;
    }

    /**
     * 自动过滤
     */
    public double autoFilter(double value){
        boolean result = EnQueue(value);
        if(!result){
            DeQueue();
            EnQueue(value);
        }
        if(isFull()) {
            //集合满后才允许过滤
            return filter();
        }else{
            //集合不满直接返回最新值
            return value;
        }
    }

    public double DeQueue() {
        if(isEmpty()){
            throw new IllegalStateException("RecycleQueue is Empty");
        }
        double value = vector[front % maxSize];
        vector[front % maxSize] = 0;
        front = ++front % maxSize;
        return value;
    }

    /**
     * 队列满后，开始过滤
     */
    public double filter(){
        double value = 0;
        try{
            int size = maxSize - 1;
            double[] array = new double[size];
            double[] windows = new double[radius];
            for(int cursor = front;cursor < front + size;cursor++){
                int start = cursor - radius /2 + maxSize;
                int end = cursor + radius /2 + maxSize;
                for(int index = start;index <= end;index++){
                    //因为采用空闲单元法 所以当滑动窗口碰到空闲单元需要特殊处理
                    if((index + 1) % maxSize == front){
                        if(index > (cursor+maxSize)){
                            //滑动窗口从尾部开始，空开空闲单元，用fromt代替
                            windows[index - start] = vector[(index + 1 + maxSize) % maxSize];
                        }else{
                            //滑动窗口从头部开始，空开空闲单元，用rear代替
                            windows[index - start] = vector[(index - 1 + maxSize) % maxSize];
                        }
                    }else{
                        windows[index - start] = vector[(index + maxSize) % maxSize];
                    }
                }
                Arrays.sort(windows);
                array[cursor-front] = windows[radius /2];
            }
            Arrays.sort(array);
            value = array[size/2];
        }catch (Exception ex){
            //发生错误 清空队列 重新开始
            clear();
            //看手机
            System.out.println("出现错误"+ex.getMessage());
        }
        return value;
    }

    public void clear() {
        for(int i = 0;i<maxSize;i++){
            vector[i] = 0;
        }
        front = 0;
        rear = 0;
    }

    public int capicity(){
        return maxSize - 1;
    }
    /**
     * 空闲单元法计算队列长度
     * @return
     */
    public int count() {
        return (rear - front + maxSize) % maxSize;
    }

    public double[] toArray() {
        if(isEmpty()){
            throw new IllegalStateException("RecycleQueue is Empty");
        }
        int length = Math.abs(rear - front);
        double[] dst = new double[length];
        System.arraycopy(vector,front,dst,0,length);
        return dst;
    }

    /**
     * 空闲单元法判断队列是否满
     * @return
     */
    public boolean isFull() {
        return front == (rear + 1) % maxSize;
    }

    public boolean isEmpty() {
        return front == rear;
    }
}
