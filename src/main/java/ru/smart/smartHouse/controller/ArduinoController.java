package ru.smart.smartHouse.controller;

import jssc.SerialPortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.smart.smartHouse.component.ArduinoSerialPort;
import ru.smart.smartHouse.component.ArduinoSerialPortInitializer;
import ru.smart.smartHouse.entity.Arduino;
import ru.smart.smartHouse.service.ArduinoService;


@Controller
@RequestMapping("/arduino")
public class ArduinoController {

    @Autowired
    private ArduinoSerialPortInitializer arduinoSerialPortInitializer;
    @Autowired
    private ArduinoService arduinoService;

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView set(@PathVariable("id") Arduino arduino) {
        return new ModelAndView("/arduino/edit")
                .addObject("arduino", arduino);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Validated @ModelAttribute("arduino") Arduino arduino, BindingResult result) {
        if(result.hasErrors()) {
            return "/arduino/edit";
        }

        arduinoService.save(arduino);
        return "/arduino/list";
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String list() {
        return "/arduino/list";
    }

    @RequestMapping(value = "/{id}/execute/{cmd}",method = RequestMethod.GET)
    @ResponseBody
    public String read(@PathVariable("id") Arduino arduino, @PathVariable("cmd") long cmd) {
        try {
            ArduinoSerialPort arduinoSerialPort = arduinoSerialPortInitializer.getArduinoSerialPort(arduino);
            return arduinoSerialPort.execute(cmd);
        }
        catch (SerialPortException ex) {
            System.out.println(ex);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "INCORRECT ARDUINO CMD";
    }
}

