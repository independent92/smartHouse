package ru.smart.smartHouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.smart.smartHouse.entity.Arduino;

@Repository
public interface ArduinoRepository extends JpaRepository<Arduino, Long> {
}
