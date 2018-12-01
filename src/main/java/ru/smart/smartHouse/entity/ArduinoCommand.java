package ru.smart.smartHouse.entity;

import javax.persistence.*;

@Entity
public class ArduinoCommand {
    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name="arduino_id")
    private Arduino arduino;
    private Long commandNumber;
    private String commandTitle;
    private String commandDescription;

    public Long getId() {
        return id;
    }

    public Arduino getArduino() {
        return arduino;
    }

    public void setArduino(Arduino arduino) {
        this.arduino = arduino;
    }

    public Long getCommandNumber() {
        return commandNumber;
    }

    public void setCommandNumber(Long commandNumber) {
        this.commandNumber = commandNumber;
    }

    public String getCommandTitle() {
        return commandTitle;
    }

    public void setCommandTitle(String commandTitle) {
        this.commandTitle = commandTitle;
    }

    public String getCommandDescription() {
        return commandDescription;
    }

    public void setCommandDescription(String commandDescription) {
        this.commandDescription = commandDescription;
    }
}
