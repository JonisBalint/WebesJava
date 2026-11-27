package hu.webesjava.core;

public class CarAlreadyExistsException extends RuntimeException {

    public CarAlreadyExistsException(Car car) {
        super("Már létezik ilyen autó: " + car.getBrand() + " " + car.getModel()
                + " (" + car.getManufactureDate() + ")");
    }
}
