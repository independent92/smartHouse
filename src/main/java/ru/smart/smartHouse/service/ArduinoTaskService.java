package ru.smart.smartHouse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import ru.smart.smartHouse.component.ArduinoSerialPortInitializer;
import ru.smart.smartHouse.component.ArduinoSerialPortListener;
import ru.smart.smartHouse.entity.Arduino;

@Service
public class ArduinoTaskService {

    private ArduinoSerialPortInitializer arduinoSerialPortInitializer;
    private final TaskScheduler taskScheduler;

    @Autowired
    public ArduinoTaskService(ArduinoSerialPortInitializer arduinoSerialPortInitializer,
                              TaskScheduler taskScheduler) {
        this.arduinoSerialPortInitializer = arduinoSerialPortInitializer;
        this.taskScheduler = taskScheduler;
    }

    public void addTask(Arduino arduino, Integer cmd, String expression) {
        try {
            ArduinoSerialPortListener listener = arduinoSerialPortInitializer.getArduinoSerialPortListener(arduino);//"0/10 * * * * *"
            listener.schedules.put(cmd, taskScheduler.schedule(() -> listener.execute(cmd), new CronTrigger(expression)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeTask(Arduino arduino, Integer cmd) {
        try {
            ArduinoSerialPortListener listener = arduinoSerialPortInitializer.getArduinoSerialPortListener(arduino);//"0/10 * * * * *"
            listener.schedules.get(cmd).cancel(true);
            listener.schedules.remove(cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
