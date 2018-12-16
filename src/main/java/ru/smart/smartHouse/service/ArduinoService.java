package ru.smart.smartHouse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smart.smartHouse.entity.Arduino;
import ru.smart.smartHouse.repository.ArduinoRepository;

import java.util.Optional;

@Service
public class ArduinoService {
    private final ArduinoRepository repository;

    @Autowired
    public ArduinoService(ArduinoRepository repository) {
        this.repository = repository;
    }

    public Optional<Arduino> findById(Long id) {
        return repository.findById(id);
    }

    public Arduino save(Arduino arduino) {
        return repository.save(arduino);
    }
}
