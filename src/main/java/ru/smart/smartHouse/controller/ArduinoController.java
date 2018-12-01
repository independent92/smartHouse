package ru.smart.smartHouse.controller;

import jssc.SerialPortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.smart.smartHouse.component.ArduinoSerialPortListener;
import ru.smart.smartHouse.component.ArduinoSerialPortInitializer;
import ru.smart.smartHouse.config.CentralScheduler;
import ru.smart.smartHouse.config.ScheduledTasks;
import ru.smart.smartHouse.entity.Arduino;
import ru.smart.smartHouse.service.ArduinoService;

import java.util.Set;


@Controller
@RequestMapping("/arduino")
public class ArduinoController {

    @Autowired
    private ArduinoSerialPortInitializer arduinoSerialPortInitializer;
    @Autowired
    private ArduinoService arduinoService;

    @Autowired
    private ScheduledAnnotationBeanPostProcessor postProcessor;

    @Autowired
    private ScheduledTasks tasks;

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
        postProcessor.postProcessBeforeDestruction(tasks , "scheduledTasks");
        return "/arduino/list";
    }

    @RequestMapping(value = "/{id}/execute/{cmd}",method = RequestMethod.GET)
    @ResponseBody
    public String write(@PathVariable("id") Arduino arduino, @PathVariable("cmd") int cmd) {
        try {
            postProcessor.postProcessAfterInitialization(tasks,"scheduledTasks");
            ArduinoSerialPortListener arduinoSerialPortListener = arduinoSerialPortInitializer.getArduinoSerialPortListener(arduino);
            return String.format("Result of writing command №%d to %s is %b", cmd, arduino.getDescription(), arduinoSerialPortListener.execute(cmd));
        }
        catch (SerialPortException ex) {
            System.out.println(ex);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "ArduinoSerialPortListener throws exception";
    }
}

