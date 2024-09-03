package com.tianji.dam.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestIterator {


    public static void main(String[] args) {

        List<Integer> all =new ArrayList<>();

        all.add(1);
        all.add(2);
        all.add(3);
        all.add(4);
        all.add(5);
        all.add(6);
        all.add(7);
        all.add(8);
        all.add(9);
        all.add(10);

        Iterator<Integer>  it = all.iterator();

        while (it.hasNext()){

             int a = it.next();




            System.out.println(a);
        }




    }
}
