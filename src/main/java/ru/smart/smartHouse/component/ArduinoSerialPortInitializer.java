package ru.smart.smartHouse.component;

import jssc.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.smart.smartHouse.entity.Arduino;
import ru.smart.smartHouse.service.ArduinoService;

import java.util.ArrayList;
import java.util.List;

@Component
public class ArduinoSerialPortInitializer {
    private List<ArduinoSerialPortListener> arduinoSerialPortListeners;

    private ArduinoService arduinoService;

    @Autowired
    public ArduinoSerialPortInitializer(ArduinoService arduinoService) {
        this.arduinoService = arduinoService;
        arduinoSerialPortListeners = new ArrayList<>();

        String[] portNames = SerialPortList.getPortNames();
        for(String port : portNames) {
            System.out.println("_ "+port);
            ArduinoSerialPortListener arduinoSerialPortListener = new ArduinoSerialPortListener();
            arduinoSerialPortListener.setSerialPort(new SerialPort(port));

            try {
                //Открываем порт
                arduinoSerialPortListener.getSerialPort().openPort();
                if(arduinoSerialPortListener.getSerialPort().isOpened()) {
                    arduinoSerialPortListener.getSerialPort().setParams(SerialPort.BAUDRATE_9600,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
                    arduinoSerialPortListener.getSerialPort().addEventListener(arduinoSerialPortListener);//Add SerialPortEventListener
                    new Thread().sleep(5000);

                    arduinoSerialPortListener.getSerialPort().writeInt(48);
                    new Thread().sleep(3000);

                    Arduino arduino = this.arduinoService.findById(arduinoSerialPortListener.getId()).orElseThrow(Exception::new);
                    arduino.setPortName(arduinoSerialPortListener.getSerialPort().getPortName());
                    this.arduinoService.save(arduino);

                    arduinoSerialPortListener.setArduino(arduino);

                    arduinoSerialPortListeners.add(arduinoSerialPortListener);

                }
            } catch (SerialPortException ex) {
                System.out.println(ex);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<ArduinoSerialPortListener> getArduinoSerialPortListeners() {
        return arduinoSerialPortListeners;
    }

    public void setArduinoSerialPortListeners(List<ArduinoSerialPortListener> arduinoSerialPortListeners) {
        this.arduinoSerialPortListeners = arduinoSerialPortListeners;
    }

    public ArduinoSerialPortListener getArduinoSerialPort(Arduino arduino) throws Exception {
        return  arduinoSerialPortListeners.stream().filter(port -> port.getArduino().getId().equals(arduino.getId())).findFirst().orElseThrow(Exception::new);
    };
}
