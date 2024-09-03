package com.tj.web.controller.dam;


import com.alibaba.fastjson.JSONArray;
import com.tianji.dam.domain.DamsConstruction;
import com.tianji.dam.domain.vo.RangesVO;
import com.tianji.dam.mapper.TDamsconstructionMapper;
import com.tianji.dam.mileageutil.Mileage;
import com.tianji.dam.service.TDamsconstructionService;
import com.tj.common.annotation.Log;
import com.tj.common.core.controller.BaseController;
import com.tj.common.core.domain.AjaxResult;
import com.tj.common.enums.BusinessType;
import com.vividsolutions.jts.geom.Coordinate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Api(value="分部工程controller",tags={"分部工程操作接口"})
@RestController
@RequestMapping("/reservoir/td")
public class TDamsconstructionController  extends BaseController {

    @Autowired
    private TDamsconstructionService tdService;
    @Autowired
    private TDamsconstructionMapper mapper;

    @Log(title = "分部工程管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation(value = "修改单元工程边界", nickname = "editRanges")
    public AjaxResult editRanges( @Validated @RequestBody RangesVO vo)
    {
        return toAjax(tdService.updateRanges(vo.getId(),vo.getRanges()));
    }


    @GetMapping("/getdamcoord")
    public AjaxResult getdamcoord(Integer pid){

      List<DamsConstruction> alldam = mapper.getdambypid(pid);
        List<Map> resultlist= new ArrayList<>();
        Mileage  mileage =Mileage.getmileage();
        for (DamsConstruction damsConstruction : alldam) {
            List<Coordinate> list = JSONArray.parseArray(damsConstruction.getRanges(), Coordinate.class);
            List<Map<String,Object>> listcoord = new ArrayList<>();

            for (Coordinate coordinate : list) {
                Map<String,Object> temp =new HashMap<>();
             double x = coordinate.x;
             double y = coordinate.y;
             double[] coords =     mileage.pixels2Coord(1,x,y);
                Coordinate  newcoord = new Coordinate(coords[0],coords[1]);
                temp.put("coordx",newcoord.y);
                temp.put("coordy",newcoord.x);
                listcoord.add(temp);
            }
            System.out.println(damsConstruction.getId()+">"+ damsConstruction.getTitle()+">"+damsConstruction.getGaocheng()+">"+ listcoord);


            Map<String,Object> dm =new HashMap<>();

            dm.put("ranges",JSONArray.toJSONString(listcoord));
            dm.put("title",damsConstruction.getTitle());
            dm.put("gaocheng",damsConstruction.getGaocheng());
            dm.put("gaochengact",damsConstruction.getGaochengact());
            dm.put("id",damsConstruction.getId());
            resultlist.add(dm);
        }

     return  AjaxResult.success(resultlist);
    }

}
