package ru.smart.smartHouse.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Arduino {
    @Id
    private Long id;
    private String portName;
    @NotEmpty
    private String description;

    @OneToMany(mappedBy = "arduino")
    private List<ArduinoCommand> commands;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ArduinoCommand> getCommands() {
        return commands;
    }

    public void setCommands(List<ArduinoCommand> commands) {
        this.commands = commands;
    }
}
