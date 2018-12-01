package ru.smart.smartHouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.smart.smartHouse.entity.Measurement;

public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
}
