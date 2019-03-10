package ru.smart.smartHouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.smart.smartHouse.component.ArduinoSerialPortInitializer;
import ru.smart.smartHouse.component.ArduinoSerialPortListener;
import ru.smart.smartHouse.entity.Arduino;
import ru.smart.smartHouse.service.ArduinoService;
import ru.smart.smartHouse.service.ArduinoTaskService;

import java.util.concurrent.ScheduledFuture;

import static java.util.Objects.isNull;


@Controller
@RequestMapping("/arduino")
public class ArduinoController {

    private final ArduinoSerialPortInitializer arduinoSerialPortInitializer;
    private final ArduinoService arduinoService;
    private final ArduinoTaskService arduinoTaskService;

    @Autowired
    public ArduinoController(ArduinoSerialPortInitializer arduinoSerialPortInitializer,
                             ArduinoService arduinoService,
                             ArduinoTaskService arduinoTaskService) {
        this.arduinoSerialPortInitializer = arduinoSerialPortInitializer;
        this.arduinoService = arduinoService;
        this.arduinoTaskService = arduinoTaskService;
    }

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
    public String write(@PathVariable("id") Arduino arduino, @PathVariable("cmd") int cmd) {
        return execute(arduino, cmd);
    }

    @RequestMapping(value = "/start/{id}/execute/{cmd}", method = RequestMethod.GET)
    public ResponseEntity<?> start(@PathVariable("id") Arduino arduino,
                        @PathVariable("cmd") int cmd,
                        @RequestParam String expression) {
        arduinoTaskService.addTask(arduino, cmd, expression);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/stop/{id}/execute/{cmd}", method = RequestMethod.GET)
    public ResponseEntity<?> stop(@PathVariable("id") Arduino arduino, @PathVariable("cmd") int cmd) {
        arduinoTaskService.removeTask(arduino, cmd);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private String execute(Arduino arduino, int cmd) {
        try {
            ArduinoSerialPortListener arduinoSerialPortListener = arduinoSerialPortInitializer.getArduinoSerialPortListener(arduino);
            return String.format("Result of writing command №%d to %s is %b", cmd, arduino.getDescription(), arduinoSerialPortListener.execute(cmd));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "ArduinoSerialPortListener throws exception";
    }
}

