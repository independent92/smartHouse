package ru.smart.smartHouse.component;

import jssc.*;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Component("Arduino")
public class ArduinoSerialPortSingleton {
    private static SerialPort instance;
    private static short angle,partsPerMillion,id;

    private ArduinoSerialPortSingleton(){
        String[] portNames = SerialPortList.getPortNames();
        for(String port : portNames) {
            System.out.println("_ "+port);
        }

        if(portNames.length!=0) {
            instance = new SerialPort(portNames[0]);

            try {
                //Открываем порт
                instance.openPort();
                instance.setParams(SerialPort.BAUDRATE_9600,
                        SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);
                instance.addEventListener(new SerialPortReader());//Add SerialPortEventListener
            } catch (SerialPortException ex) {
                System.out.println(ex);
            }
        }
    }

    public static SerialPort getInstance() {
        if(instance==null){
            new ArduinoSerialPortSingleton();
        }

        return instance;
    }

    public static void getPortname(){
        System.out.println("Arduino serial port name:"+instance.getPortName());
    }

    public static short getAngle() {
        return angle;
    }

    public static short getPartsPerMillion() {
        return partsPerMillion;
    }

    public static short getId() {
        return id;
    }

    static class SerialPortReader implements SerialPortEventListener {

        public void serialEvent(SerialPortEvent event){
            System.out.println("event.getEventValue():"+event.getEventValue()+" "+event.isRXCHAR());
            if (event.isRXCHAR()) {//If data is available
                if (event.getEventValue() > 1) {//Check bytes count in the input buffer
                    //Read data, if 10 bytes available
                    try {
                        byte[] buffer = instance.readBytes(event.getEventValue());

                        String word = new String(buffer, StandardCharsets.UTF_8);

                        short commandId = Short.parseShort(word.substring(0, 2).trim());

                        Short value = Short.parseShort(word.substring(2, word.length()));

                        System.out.println("commandId: "+commandId);
                        System.out.println("value: "+value);
                        if(commandId==9)
                            ArduinoSerialPortSingleton.angle = value;
                        if(commandId==8)
                            ArduinoSerialPortSingleton.partsPerMillion = value;
                        if(commandId==80)
                            ArduinoSerialPortSingleton.id = value;

                    } catch (SerialPortException ex) {
                        System.out.println(ex);
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
}
