package hu.webesjava;

import java.time.LocalDate;
import java.util.List;

import hu.webesjava.core.Car;
import hu.webesjava.core.FuelType;
import hu.webesjava.dao.CarDao;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CarApplication {

    private final CarDao carDao;

    public CarApplication(CarDao carDao) {
        this.carDao = carDao;
    }

    public static void main(String[] args) {
        SpringApplication.run(CarApplication.class, args);
    }

    @Bean
    ApplicationRunner initializeCars() {
        return args -> {
            if (carDao.count() == 0) {
                carDao.insert(List.of(
                        new Car("Subaru", "WRX STI", 300, LocalDate.of(2018, 5, 14), FuelType.PETROL),
                        new Car("Audi", "A4", 190, LocalDate.of(2019, 3, 8), FuelType.DIESEL),
                        new Car("Toyota", "Corolla", 132, LocalDate.of(2020, 7, 2), FuelType.PETROL),
                        new Car("BMW", "i3", 170, LocalDate.of(2021, 2, 19), FuelType.ELECTRIC),
                        new Car("Volkswagen", "Golf", 150, LocalDate.of(2022, 9, 23), FuelType.DIESEL)
                ));
            }
        };
    }
}
