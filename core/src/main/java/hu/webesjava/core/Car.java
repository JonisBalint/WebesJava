package hu.webesjava.core;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

@Document(collection = "cars")
@CompoundIndex(name = "unique_car", def = "{'brand': 1, 'model': 1, 'manufactureDate': 1}", unique = true)
public class Car {

    @Id
    private String id;
    private String brand;
    private String model;
    private int horsepower;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate manufactureDate;
    private FuelType fuelType;

    public Car() {
    }

    public Car(String brand, String model, int horsepower, LocalDate manufactureDate, FuelType fuelType) {
        this.brand = brand;
        this.model = model;
        this.horsepower = horsepower;
        this.manufactureDate = manufactureDate;
        this.fuelType = fuelType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getHorsepower() {
        return horsepower;
    }

    public void setHorsepower(int horsepower) {
        this.horsepower = horsepower;
    }

    public LocalDate getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(LocalDate manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }
}
