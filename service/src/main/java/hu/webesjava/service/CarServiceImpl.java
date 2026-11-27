package hu.webesjava.service;

import java.time.LocalDate;
import java.util.List;

import hu.webesjava.core.Car;
import hu.webesjava.core.CarAlreadyExistsException;
import hu.webesjava.core.CarNotFoundException;
import hu.webesjava.dao.CarDao;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
public class CarServiceImpl implements CarService {

    private final CarDao carDao;

    public CarServiceImpl(CarDao carDao) {
        this.carDao = carDao;
    }

    @Override
    public List<Car> getAllCars() {
        return carDao.findAll();
    }

    @Override
    public Car getCarById(String id) {
        return carDao.findById(id).orElseThrow(() -> new CarNotFoundException(id));
    }

    @Override
    public Car addCar(Car car) {
        validate(car);
        car.setId(null);
        try {
            return carDao.insert(car);
        } catch (DuplicateKeyException exception) {
            throw new CarAlreadyExistsException(car);
        }
    }

    @Override
    public Car updateCar(String id, Car car) {
        Car existingCar = getCarById(id);
        validate(car);
        car.setId(existingCar.getId());
        try {
            return carDao.save(car);
        } catch (DuplicateKeyException exception) {
            throw new CarAlreadyExistsException(car);
        }
    }

    @Override
    public void deleteCar(String id) {
        carDao.deleteCarById(id).orElseThrow(() -> new CarNotFoundException(id));
    }

    @Override
    public List<Car> getCarsByBrand(String brand) {
        String searchBrand = brand.strip();
        if (searchBrand.isEmpty()) {
            throw new IllegalArgumentException("A keresett márka nem lehet üres.");
        }
        return carDao.findByBrandIgnoreCase(searchBrand);
    }

    private void validate(Car car) {
        String brand = car.getBrand();
        if (brand == null || brand.isBlank()) {
            throw new IllegalArgumentException("A márka megadása kötelező.");
        }
        String model = car.getModel();
        if (model == null || model.isBlank()) {
            throw new IllegalArgumentException("A modell megadása kötelező.");
        }
        int horsepower = car.getHorsepower();
        if (horsepower <= 0) {
            throw new IllegalArgumentException("A lóerő pozitív egész szám legyen: " + horsepower);
        }
        LocalDate manufactureDate = car.getManufactureDate();
        if (manufactureDate == null) {
            throw new IllegalArgumentException("A gyártási dátum megadása kötelező.");
        }
        if (manufactureDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A gyártási dátum nem lehet jövőbeli: " + manufactureDate);
        }
        if (car.getFuelType() == null) {
            throw new IllegalArgumentException("Az üzemanyag típusának megadása kötelező.");
        }

        car.setBrand(brand.strip());
        car.setModel(model.strip());
    }
}
