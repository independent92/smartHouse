package ru.smart.smartHouse.component;

import jssc.*;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Component("Arduino")
public class ArduinoSerialPortSingleton {
    private static SerialPort instance;
    private static short angle,partsPerMillion;

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


    static class SerialPortReader implements SerialPortEventListener {

        public void serialEvent(SerialPortEvent event){
            System.out.println("event.getEventValue():"+event.getEventValue()+" "+event.isRXCHAR());
            if (event.isRXCHAR()) {//If data is available
                if (event.getEventValue() > 1) {//Check bytes count in the input buffer
                    //Read data, if 10 bytes available
                    try {
                        byte[] buffer = instance.readBytes(event.getEventValue());
                        int i;
                        for (i=0;i<buffer.length;i++){
                            if(buffer[i]==13)
                                break;

                        }

                        String word = new String(Arrays.copyOfRange(buffer,0,i), StandardCharsets.UTF_8);

                        int outPin = Short.parseShort(word.substring(0, 1));

                        Short value = Short.parseShort(word.substring(1, word.length()));

                        System.out.println("outPin: "+outPin);
                        System.out.println("value: "+value);
                        if(outPin==9)
                            ArduinoSerialPortSingleton.angle = value;
                        if(outPin==8)
                            ArduinoSerialPortSingleton.partsPerMillion = value;

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
