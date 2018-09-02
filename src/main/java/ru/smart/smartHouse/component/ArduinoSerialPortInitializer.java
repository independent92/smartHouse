package ru.smart.smartHouse.component;

import jssc.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.smart.smartHouse.entity.Arduino;
import ru.smart.smartHouse.service.ArduinoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ArduinoSerialPortInitializer {
    private List<ArduinoSerialPort> arduinoSerialPorts;

    private ArduinoService arduinoService;

    @Autowired
    public ArduinoSerialPortInitializer(ArduinoService arduinoService) {
        this.arduinoService = arduinoService;
        arduinoSerialPorts = new ArrayList<>();

        String[] portNames = SerialPortList.getPortNames();
        for(String port : portNames) {
            System.out.println("_ "+port);
            ArduinoSerialPort arduinoSerialPort = new ArduinoSerialPort();
            arduinoSerialPort.setSerialPort(new SerialPort(port));

            try {
                //Открываем порт
                arduinoSerialPort.getSerialPort().openPort();
                arduinoSerialPort.getSerialPort().setParams(SerialPort.BAUDRATE_9600,
                        SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);
                arduinoSerialPort.getSerialPort().addEventListener(arduinoSerialPort);//Add SerialPortEventListener
                new Thread().sleep(1000);

                arduinoSerialPort.getSerialPort().writeInt(185);
                new Thread().sleep(3000);

                Arduino arduino = this.arduinoService.findById(arduinoSerialPort.getId()).orElseThrow(Exception::new);
                arduino.setPortName(arduinoSerialPort.getSerialPort().getPortName());
                this.arduinoService.save(arduino);

                arduinoSerialPort.setArduino(arduino);

                arduinoSerialPorts.add(arduinoSerialPort);
            } catch (SerialPortException ex) {
                System.out.println(ex);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<ArduinoSerialPort> getArduinoSerialPorts() {
        return arduinoSerialPorts;
    }

    public void setArduinoSerialPorts(List<ArduinoSerialPort> arduinoSerialPorts) {
        this.arduinoSerialPorts = arduinoSerialPorts;
    }

    public ArduinoSerialPort getArduinoSerialPort(Arduino arduino) throws Exception {
        return  arduinoSerialPorts.stream().filter(port -> port.getArduino().getId().equals(arduino.getId())).findFirst().orElseThrow(Exception::new);
    };
}
