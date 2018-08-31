package ru.smart.smartHouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.smart.smartHouse.entity.Sensor;

public interface SensorRepository extends JpaRepository<Sensor, Long> {
}
