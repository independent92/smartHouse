package ru.smart.smartHouse.component;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.smart.smartHouse.entity.Arduino;
import ru.smart.smartHouse.service.ArduinoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class ArduinoSerialPortInitializer {
    private List<ArduinoSerialPortListener> arduinoSerialPortListeners = new ArrayList<>();
    private final ArduinoService arduinoService;

    @Autowired
    public ArduinoSerialPortInitializer(ArduinoService arduinoService) {
        this.arduinoService = arduinoService;

        String[] portNames = SerialPortList.getPortNames();
        for(String port : portNames) {
            System.out.println("Serial port: "+port);
            ArduinoSerialPortListener arduinoSerialPortListener = new ArduinoSerialPortListener(this.arduinoService);
            arduinoSerialPortListener.setSerialPort(new SerialPort(port));

            SerialPort serialPort = arduinoSerialPortListener.getSerialPort();
            try {
                //Открываем порт
                serialPort.openPort();
                if(serialPort.isOpened()) {
                    serialPort.setParams(SerialPort.BAUDRATE_9600,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
                    serialPort.addEventListener(arduinoSerialPortListener);//Add SerialPortEventListener
                    new Thread().sleep(5000);

                    serialPort.writeInt(48);
                    new Thread().sleep(1000);
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

    public ArduinoSerialPortListener getArduinoSerialPortListener(Arduino arduino) throws Exception {
        return  arduinoSerialPortListeners
                .stream()
                .filter(arduinoSerialPortListener -> Objects.equals(arduinoSerialPortListener.getArduino().getId(), arduino.getId()))
                .findFirst()
                .orElseThrow(Exception::new);
    }
}
