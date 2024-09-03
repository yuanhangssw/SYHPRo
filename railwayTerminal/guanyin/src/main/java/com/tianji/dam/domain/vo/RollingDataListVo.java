package com.tianji.dam.domain.vo;

import com.tianji.dam.domain.RollingData;
import lombok.Data;

import java.util.List;

@Data
public class RollingDataListVo {

    private String tableName;
    private List<RollingData> dataList;


}
