package hu.webesjava.dao;

import java.util.List;
import java.util.Optional;

import hu.webesjava.core.Car;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CarDao extends MongoRepository<Car, String> {

    List<Car> findByBrandIgnoreCase(String brand);

    Optional<Car> deleteCarById(String id);
}
