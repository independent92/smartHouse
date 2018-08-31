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

    @RequestMapping(value = "/cmd/read/{pin_id}",method = RequestMethod.GET)
    @ResponseBody
    public String read(@PathVariable("pin_id") int pin_id){
        //byte bs[] = new byte[7];
        if (pin_id == 9) {
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
        }else if(pin_id == 8){
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
        }
        return "ARDUINO INCORRECT PIN_ID";
    }
}

