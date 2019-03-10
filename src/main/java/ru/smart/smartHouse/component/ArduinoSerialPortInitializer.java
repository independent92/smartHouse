package ru.smart.smartHouse.component;

import jssc.SerialPort;
import jssc.SerialPortList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.smart.smartHouse.entity.Arduino;
import ru.smart.smartHouse.service.ArduinoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

// TODO: as singleton
@Component
public class ArduinoSerialPortInitializer {
    // TODO: as Set
    private List<ArduinoSerialPortListener> arduinoSerialPortListeners = new ArrayList<>();
    private final ArduinoService arduinoService;

    @Autowired
    public ArduinoSerialPortInitializer(ArduinoService arduinoService) {
        this.arduinoService = arduinoService;

        String[] portNames = SerialPortList.getPortNames();
        for(String port : portNames) {
            System.out.println(String.format("Serial port: %s", port));
            ArduinoSerialPortListener arduinoSerialPortListener = new ArduinoSerialPortListener();

            SerialPort serialPort = new SerialPort(port);
            arduinoSerialPortListener.setSerialPort(serialPort);

            try {
                //Открываем порт
                serialPort.openPort();
                serialPort.setParams(SerialPort.BAUDRATE_9600,
                        SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);
                new Thread().sleep(5000);

                serialPort.addEventListener(arduinoSerialPortListener);
                serialPort.writeInt(48);
                new Thread().sleep(1000);

                Arduino arduino = arduinoService.findById(arduinoSerialPortListener.getArduinoId()).orElseThrow(Exception::new);
                arduino.setPortName(serialPort.getPortName());
                arduinoSerialPortListener.setArduino(arduino);

                arduinoService.save(arduino);

                arduinoSerialPortListeners.add(arduinoSerialPortListener);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ArduinoSerialPortListener getArduinoSerialPortListener(Arduino arduino) throws Exception {
        return  arduinoSerialPortListeners
                .stream()
                .filter(arduinoSerialPortListener -> Objects.equals(arduinoSerialPortListener.getArduino().getId(), arduino.getId()))
                .findFirst()
                .orElseThrow(Exception::new);
    }
}
