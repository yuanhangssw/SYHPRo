package com.tj.web.controller.dam;

import com.tianji.dam.domain.Car;
import com.tianji.dam.service.CarService;
import com.tj.common.core.controller.BaseController;
import com.tj.common.core.domain.AjaxResult;
  
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@Api(value="车辆信息controller",tags={"车辆信息操作接口"})
@RestController
@RequestMapping("/reservoir/car")
public class TCarController extends BaseController {
   
    @Autowired
    private CarService carService;

    @GetMapping
    @ApiOperation(value = "获取车辆信息列表", response = Car.class, nickname = "getInfo")
    public AjaxResult getInfo()
    {
        return AjaxResult.success(carService.findCar());
    }
}
