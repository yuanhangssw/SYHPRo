package com.tianji.dam.utils.productareapoints.scan;

/**
 * 新边表
 * 新边表”通常在算法开始时建立
 * 定义“新边表”为一个数组，数组的每个元素存放对应扫描线的所有“新边”
 * 扫描线算法的核心就是围绕“活动边表（AET）”展开的，为了方便活性边表的建立与更新，我们为每一条扫描线建立一个“新边表（NET）”，存放该扫描线第一次出现的边
 * 当算法处理到某条扫描线时，就将这条扫描线的“新边表”中的所有边逐一插入到“活动边表”中
 * 建立“新边表”的规则就是：如果某条边的较低端点（y坐标较小的那个点）的y坐标与扫描线y相等，则该边就是扫描线y的新边，应该加入扫描线y的“新边表”
 */

public class NET {
    private static final int MAXINDEX = 500;
    private static final float FACTORY = 0.75f;//放大因子 未保证新边表可以包含扫描线的所有新边
    private AET[] net;
    private int captity = MAXINDEX;
    public NET() {
        this(MAXINDEX);
    }

    public NET(int value) {
//        this.captity = (int) (value / FACTORY);
        this.captity = value;
        net = new AET[captity];
        for(int i = 0;i < captity;i++){
            net[i] = new AET();
        }
    }

    public AET[] getAll(){
        return net;
    }
    public AET get(int index){
        if(index < 0 || index >= captity){
            //TODO:超过初始值的时候需要进行数组迁移
//            int newCaptity = (int) (captity / FACTORY);
//            AET[] newNet = new AET[newCaptity];
//            System.arraycopy(net, 0, newNet, 0, captity);
//            captity = newCaptity;
//            net = newNet;
            throw new IllegalArgumentException("NET index must > 0 and < " + captity);
        }
        return net[index];
    }

}
