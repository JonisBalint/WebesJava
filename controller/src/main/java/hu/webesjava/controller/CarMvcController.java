package hu.webesjava.controller;

import hu.webesjava.core.Car;
import hu.webesjava.core.CarAlreadyExistsException;
import hu.webesjava.core.FuelType;
import hu.webesjava.service.CarService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cars")
public class CarMvcController {

    private final CarService carService;

    public CarMvcController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public String listCars(Model model) {
        model.addAttribute("cars", carService.getAllCars());
        return "cars";
    }

    @GetMapping("/new")
    public String newCar(Model model) {
        model.addAttribute("car", new Car());
        return "car-form";
    }

    @PostMapping
    public String addCar(@ModelAttribute("car") Car car, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "car-form";
        }

        try {
            carService.addCar(car);
            return "redirect:/cars";
        } catch (IllegalArgumentException | CarAlreadyExistsException exception) {
            bindingResult.reject("car.invalid", exception.getMessage());
            return "car-form";
        }
    }

    @ModelAttribute("fuelTypes")
    public FuelType[] fuelTypes() {
        return FuelType.values();
    }
}
