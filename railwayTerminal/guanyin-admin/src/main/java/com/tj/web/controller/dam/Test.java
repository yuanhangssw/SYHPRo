package com.tj.web.controller.dam;

import com.tianji.dam.bean.GlobCache;
import com.tianji.dam.domain.QueryConditions;

public class Test {

    public static void main(String[] args) {
        QueryConditions qc = new QueryConditions();
        qc.setCarId(1);
        String tablep = GlobCache.cartableprfix[qc.getCarId()];
        System.out.println(tablep);
    }

}
