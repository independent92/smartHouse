package ru.smart.smartHouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.smart.smartHouse.entity.ArduinoCommand;

public interface ArduinoCommandRepository extends JpaRepository<ArduinoCommand, Long> {
}
