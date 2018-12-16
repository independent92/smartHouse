package ru.smart.smartHouse.component;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import ru.smart.smartHouse.entity.Arduino;
import ru.smart.smartHouse.service.ArduinoService;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

public class ArduinoSerialPortListener implements SerialPortEventListener {
    private Arduino arduino;
    private SerialPort serialPort;
    private long angle, partsPerMillion;
    public Map<Integer, ScheduledFuture<?>> schedules = new HashMap<>();

    private ArduinoService arduinoService;

    public ArduinoSerialPortListener(ArduinoService arduinoService) {
        this.arduinoService = arduinoService;
    }

    public void setArduino(Arduino arduino) {
        this.arduino = arduino;
    }

    public Arduino getArduino() {
        return arduino;
    }
    public SerialPort getSerialPort() {
        return serialPort;
    }

    public void setSerialPort(SerialPort serialPort) {
        this.serialPort = serialPort;
    }

    public long getAngle() {
        return angle;
    }

    public void setAngle(long angle) {
        this.angle = angle;
    }

    public long getPartsPerMillion() {
        return partsPerMillion;
    }

    public void setPartsPerMillion(long partsPerMillion) {
        this.partsPerMillion = partsPerMillion;
    }

    public boolean execute(int cmd){
        try {
            return this.getSerialPort().writeInt(cmd);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void serialEvent(SerialPortEvent event){
        System.out.println("event.getEventValue():"+event.getEventValue()+" "+event.isRXCHAR());
        if (event.isRXCHAR()) {//If data is available
            if (event.getEventValue() > 1) {//Check bytes count in the input buffer
                //Read data, if 10 bytes available
                try {
                    byte[] buffer = serialPort.readBytes(event.getEventValue());

                    String word = new String(buffer, StandardCharsets.UTF_8);

                    short commandId = Short.parseShort(word.substring(0, 2).trim());

                    Short value = Short.parseShort(word.substring(2, word.length()));

                    System.out.println("commandId: "+commandId);
                    System.out.println("value: "+value);
                    if(commandId==9)
                        angle = value;
                    if(commandId==8)
                        partsPerMillion = value;
                    if(commandId==80) {
                        arduino = arduinoService.findById(Long.valueOf(value)).orElseThrow(Exception::new);
                        arduino.setPortName(serialPort.getPortName());
                        arduinoService.save(arduino);
                    }

                } catch (SerialPortException ex) {
                    System.out.println(ex);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (event.isCTS()) {//If CTS line has changed state
            if (event.getEventValue() == 1) {//If line is ON
                System.out.println("CTS - ON");
            } else {
                System.out.println("CTS - OFF");
            }
        } else if (event.isDSR()) {///If DSR line has changed state
            if (event.getEventValue() == 1) {//If line is ON
                System.out.println("DSR - ON");
            } else {
                System.out.println("DSR - OFF");
            }
        }
    }
}
