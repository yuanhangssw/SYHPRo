package com.tianji.dam.mapper;

import java.text.MessageFormat;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tianji.dam.domain.RollingDataJson;

public class SysRateProvider {
    public String insertAll(@Param("uuid") String uuid, @Param("ownerCar") String ownerCar,
                            @Param("otherCar") String otherCar, @Param("list") List<RollingDataJson> list){
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO sysrate ");
        sb.append("(uuid,ownerCar,otherCar,orderNum) ");
        sb.append("VALUES ");
        MessageFormat mf = new MessageFormat("(#'{'uuid'}'," +
                "#'{'ownerCar'}'," +
                "#'{'otherCar'}'," +
                "#'{'list[{0}].OrderNum'}')");
        for (int i = 0; i < list.size(); i++) {
            sb.append(mf.format(new Object[]{String.valueOf(i)}));
            if (i < list.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }
}
