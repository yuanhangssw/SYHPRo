package com.tianji.dam.utils.productareapoints.scan;


import java.util.LinkedList;import java.util.ArrayList;
import java.util.List;

/**
 * 活化边表AET 代表扫描线 i 与多边形边相交的边
 * 把与当前扫描线相交的边称活化边AEL(Active Edge List) 。组成的表称为活性表AET
 * 每次只有相关的几条边可能与扫描线有交点，不必对所有的边进行求交计算；
 *
 * 避免填充越过边界线，采用左闭右开的原则
 * 水平边与扫描线重合，会产生很多交点，通常的做法是将水平边直接画出（填充），然后在后面的处理中就忽略水平边，不对其进行求交计算。
 */

public class AET {
    private List<ET> aet = new LinkedList<>();
    public List<ET> getAll(){
        return aet;
    }
    public void add(AET otherAET){
        aet.addAll(otherAET.getAll());
    }
    public void add(ET otherET){
        aet.add(otherET);
    }
    public void remove(int index){
        if(index < 0 || index >= aet.size()){
            throw new IllegalArgumentException(String.format("remove function:%d must > 0 and < aet.size()",index));
        }
        aet.remove(index);
    }
    public ET get(int index){
        if(index < 0 || index >=aet.size()){
            throw new IllegalArgumentException(String.format("get function:%d must > 0 and < aet.size()",index));
        }
        return aet.get(index);
    }
}
