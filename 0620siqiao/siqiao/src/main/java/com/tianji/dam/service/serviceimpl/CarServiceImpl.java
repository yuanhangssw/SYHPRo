package com.tianji.dam.service.serviceimpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tianji.dam.domain.Car;
import com.tianji.dam.mapper.CarMapper;
import com.tianji.dam.service.CarService;
import com.tj.common.annotation.DataSource;
import com.tj.common.enums.DataSourceType;

import java.util.List;

@Service
@DataSource(value = DataSourceType.SLAVE)
public class CarServiceImpl implements CarService {
    @Autowired
    CarMapper carMapper;
    @Override
    public List<Car> findCar() {
        return carMapper.findCar();
    }
    @Override
    public Integer insertCar(Car car) {
        // TODO Auto-generated method stub
        return carMapper.insert(car);
    }
    @Override   
    public int deleteCar(Integer carID) {
        // TODO Auto-generated method stub
        return carMapper.deleteByPrimaryKey(carID);
    }
    @Override
    public int updateCar(Car car) {
        // TODO Auto-generated method stub
        return carMapper.updateByPrimaryKeySelective(car);
    }
    @Override
    public Car selectCarByID(Integer carID) {
        // TODO Auto-generated method stub
        return carMapper.selectByPrimaryKey(carID);
    }
}
