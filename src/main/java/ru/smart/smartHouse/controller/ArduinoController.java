package ru.smart.smartHouse.controller;

import jssc.SerialPort;
import jssc.SerialPortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.smart.smartHouse.component.ArduinoSerialPortSingleton;


@Controller
@RequestMapping("arduino")
public class ArduinoController {

    @Autowired
    private static SerialPort serialPort;

    public ArduinoController(SerialPort serialPort) {
        this.serialPort = serialPort;
    }

    @RequestMapping(value = "/cmd/write/{id}",method = RequestMethod.GET)
    @ResponseBody
    public String write(@PathVariable("id") int num){
        try {
            serialPort.writeInt(num);
        }
        catch (SerialPortException ex) {
            System.out.println(ex);
        }

        return "ARDUINO CMD №"+num;
    }

    @RequestMapping(value = "/cmd/read/{cmd}",method = RequestMethod.GET)
    @ResponseBody
    public String read(@PathVariable("cmd") int cmd){
        //byte bs[] = new byte[7];
        if (cmd == 9) {
            try {
                serialPort.writeInt(183);
                new Thread().sleep(100);
                System.out.println("ArduinoSerialPortSingleton.getAngle():"+ ArduinoSerialPortSingleton.getAngle());
            }
            catch (SerialPortException ex) {
                System.out.println(ex);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "ARDUINO SERVO ANGLE="+ArduinoSerialPortSingleton.getAngle();
        }else if(cmd == 8){
            try {
                serialPort.writeInt(184);
                new Thread().sleep(100);
                System.out.println("ArduinoSerialPortSingleton.getPartsPerMillion():"+ArduinoSerialPortSingleton.getPartsPerMillion());
            } catch (SerialPortException ex) {
                System.out.println(ex);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "ARDUINO PARTS PER MILLION ="+ArduinoSerialPortSingleton.getPartsPerMillion();
        } else if(cmd == 80) {
            try {
                serialPort.writeInt(185);
                new Thread().sleep(100);
                System.out.println("ArduinoSerialPortSingleton.getId():" + ArduinoSerialPortSingleton.getId());
            } catch (SerialPortException ex) {
                System.out.println(ex);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "ARDUINO ID ="+ArduinoSerialPortSingleton.getId();
        }
        return "ARDUINO INCORRECT PIN_ID";
    }
}

