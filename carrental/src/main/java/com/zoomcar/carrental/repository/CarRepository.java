package com.zoomcar.carrental.repository;

import com.zoomcar.carrental.models.Car;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class CarRepository {
    private final Map<Long, Car> carTable = new TreeMap<>();
    private long previousId = 0L;

    public Car saveCar(Car car) {
        if (car.getId() == 0) {
            previousId += 1;
            car.setId(previousId);
        }
        carTable.put(car.getId(), car);
        return car;
    }

    public Optional<Car> findCarById(long carId) {
        return Optional.ofNullable(carTable.get(carId));
    }

    public List<Car> findCarsByLocation(String location) {
        List<Car> cars = new ArrayList<>();
        for (Car car : carTable.values()) {
            if (car.getLocation().equalsIgnoreCase(location)) {
                cars.add(car);
            }
        }
        return cars;
    }
}
