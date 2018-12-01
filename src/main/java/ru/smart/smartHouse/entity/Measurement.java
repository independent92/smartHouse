package ru.smart.smartHouse.entity;

import javax.persistence.*;

@Entity
public class Measurement {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="sensor_id")
    private Sensor sensor;

    private Double value;

    private String unit;

    public Sensor getSensor() {

        return sensor;
    }

    public Long getId() {
        return id;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
