package ru.smart.smartHouse.component;

import jssc.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.smart.smartHouse.entity.Arduino;
import ru.smart.smartHouse.service.ArduinoService;

import java.util.HashMap;
import java.util.Map;

@Component
public class ArduinoSerialPortInitializer {
    private Map<Arduino, ArduinoSerialPortListener> arduinoSerialPortListeners;
    private final ArduinoService arduinoService;

    @Autowired
    public ArduinoSerialPortInitializer(ArduinoService arduinoService) {
        this.arduinoService = arduinoService;
        arduinoSerialPortListeners = new HashMap<>();

        String[] portNames = SerialPortList.getPortNames();
        for(String port : portNames) {
            System.out.println("Serial port: "+port);
            ArduinoSerialPortListener arduinoSerialPortListener = new ArduinoSerialPortListener(this.arduinoService);
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
                    arduinoSerialPortListeners.put(arduinoSerialPortListener.getArduino(), arduinoSerialPortListener);

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
        return  arduinoSerialPortListeners.get(arduino);
    };
}
