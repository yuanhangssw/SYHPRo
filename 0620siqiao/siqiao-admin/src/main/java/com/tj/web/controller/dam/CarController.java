package com.tj.web.controller.dam;

import com.alibaba.fastjson.JSONObject;
import com.tianji.dam.domain.Car;
import com.tianji.dam.mapper.CarMapper;
import com.tianji.dam.service.CarService;
import com.tj.web.controller.common.TableResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

@CrossOrigin
@RequestMapping("/bidr/car")
@Controller("CarController")
public class CarController {
    @Autowired
    private CarService carService;
    @Autowired
    private CarMapper carMapper;

    //查询车辆

    @RequestMapping(value = "/findCar", method = RequestMethod.GET)
    @ResponseBody
    public TableResult findCar(HttpServletRequest request) {
        //查询每个车的当日在线时长
        List<Car> carList = carService.findCar();
        TableResult result = new TableResult();
        result.setCode(0);
        result.setMsg(null);
        result.setCount(carList.size());
        result.setData(carList);
        return result;
    }


    //查询车辆

    /**
     * 除去司机关联的列表查询-
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/findCar2", method = RequestMethod.GET)
    @ResponseBody
    public TableResult findCar2(HttpServletRequest request) {

        List<Car> carList = carService.findCar();
        for (Car oneCar : carList) {
            Integer carid = oneCar.getCarID();
            Integer timemu =   carMapper.cartodaytimepass(carid+"");
            if(null==timemu){
                timemu =0;
            }
            BigDecimal timemub =new BigDecimal(timemu);
            BigDecimal[] datas =    timemub.divideAndRemainder(new BigDecimal(60));
            oneCar.setDriver(datas[0]+"小时"+datas[1]+"分钟");
        }

        TableResult result = new TableResult();
        result.setCode(0);
        result.setMsg(null);
        result.setCount(carList.size());
        result.setData(carList);
        return result;
    }


    //增加车辆
    @RequestMapping(value = "/addCar", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject addCar(@RequestBody Car car) {

        int result = carService.insertCar(car);
        return new JSONObject();
    }

    //删除车辆
    @RequestMapping(value = "/deleteCar", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject deleteCar(HttpServletRequest request) {
        Integer carID = Integer.valueOf(request.getParameter("carID"));
        int result = carService.deleteCar(carID);
        return new JSONObject();
    }

    //修改车辆
    @RequestMapping(value = "/updateCar", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject updateCar(@RequestBody Car car) {
        int result = carService.updateCar(car);
        return new JSONObject();
    }

}
