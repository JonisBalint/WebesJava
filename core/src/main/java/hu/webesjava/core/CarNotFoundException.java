package hu.webesjava.core;

public class CarNotFoundException extends RuntimeException {

    public CarNotFoundException(String id) {
        super("Nem található autó ezzel az azonosítóval: " + id);
    }
}
