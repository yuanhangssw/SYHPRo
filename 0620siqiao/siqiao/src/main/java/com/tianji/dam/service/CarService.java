package com.tianji.dam.service;


import java.util.List;

import com.tianji.dam.domain.Car;

public interface CarService {
    Integer insertCar(Car car);
    int deleteCar(Integer carID);
    int updateCar(Car car);
    List<Car> findCar();
    Car selectCarByID(Integer carID);
}
