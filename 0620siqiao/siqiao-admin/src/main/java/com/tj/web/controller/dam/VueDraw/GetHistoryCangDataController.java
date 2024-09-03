package com.tj.web.controller.dam.VueDraw;


import com.tianji.dam.domain.DamsConstruction;
import com.tianji.dam.domain.RollingData;
import com.tianji.dam.mapper.RollingDataMapper;
import com.tianji.dam.mapper.TDamsconstructionMapper;
import com.tj.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping("/vuedrawforreal")
@RestController
public class GetHistoryCangDataController {
        @Autowired
        TDamsconstructionMapper damsconstructionMapper;
        @Autowired
        RollingDataMapper rollingDataMapper;

    @GetMapping("/gethistorybytype")
    public AjaxResult getCangHistoryData(String type){

        //获取当前车辆所在任务的仓，然后查询仓的历史数据。分为摊铺tpj、碾压ylj
        List<DamsConstruction> allopencang =    damsconstructionMapper.findByStatus(8);
        Map<String,Map<String,List<RollingData>>> cangcardata = new HashMap<>();
        for (DamsConstruction damsConstruction : allopencang) {
            String tablename =       damsConstruction.getTablename();
            Map<String,List<RollingData>> cardata;
            List<RollingData> alldata =   rollingDataMapper.getAllRollingDatasimple(type+"_"+tablename);
            cardata =  alldata.stream().collect(Collectors.groupingBy(RollingData::getVehicleID));
            cangcardata .put(tablename,cardata);
        }
        return AjaxResult.success(cangcardata);
    }

}
