package ru.smart.smartHouse.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Sensor {
    @Id
    private Long id;

    private String name;

    private String description;

    @OneToMany(mappedBy = "sensor")
    private List<Measurement> measurements;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }
}
