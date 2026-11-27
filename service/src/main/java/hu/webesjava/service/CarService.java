package hu.webesjava.service;

import java.util.List;

import hu.webesjava.core.Car;

public interface CarService {

    List<Car> getAllCars();

    Car getCarById(String id);

    Car addCar(Car car);

    Car updateCar(String id, Car car);

    void deleteCar(String id);

    List<Car> getCarsByBrand(String brand);
}
