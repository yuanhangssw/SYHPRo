package com.tj.web.controller.dam;

import com.tianji.dam.mileageutil.Mileage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/tm")
public class TestMileageConroller {


    @GetMapping("/t1")
    @ResponseBody
    public void testp2c(double x, double y) {

        Mileage mileage = Mileage.getmileage();
        double[] value = mileage.pixels2Coord(1, x, y);

        System.out.println("平面坐标：" + value[0] + ">>>" + value[1]);
        double[] value2 = mileage.coord2Mileage2(value[0], value[1], "0");

        System.out.println("里程偏距：" + value2[0] + ">>>" + value2[1]);

    }

    @GetMapping("/t2")
    @ResponseBody
    public void testm2p(double x, double y) {

        Mileage mileage = Mileage.getmileage();

        double[] value2 = mileage.mileage2Pixel(1, x, y, "0");

        System.out.println("平面坐标：" + value2[0] + ">>>" + value2[1]);

    }

    @GetMapping("/t3")
    @ResponseBody
    public void testc2p(double x, double y) {

        Mileage mileage = Mileage.getmileage();

        double[] value2 = mileage.coord2pixel(x, y);

        System.out.println("平面坐标：" + value2[0] + ">>>" + value2[1]);

    }

}
