package ru.smart.smartHouse.controller;

import jssc.SerialPortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.smart.smartHouse.component.ArduinoSerialPortInitializer;
import ru.smart.smartHouse.component.ArduinoSerialPortListener;
import ru.smart.smartHouse.entity.Arduino;
import ru.smart.smartHouse.service.ArduinoService;

import java.util.concurrent.ScheduledFuture;

import static java.util.Objects.isNull;


@Controller
@RequestMapping("/arduino")
public class ArduinoController {

    private final ArduinoSerialPortInitializer arduinoSerialPortInitializer;
    private final ArduinoService arduinoService;
    private final TaskScheduler taskScheduler;

    @Autowired
    public ArduinoController(ArduinoSerialPortInitializer arduinoSerialPortInitializer,
                             ArduinoService arduinoService,
                             TaskScheduler taskScheduler) {
        this.arduinoSerialPortInitializer = arduinoSerialPortInitializer;
        this.arduinoService = arduinoService;
        this.taskScheduler = taskScheduler;
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
    public String start(@PathVariable("id") Arduino arduino, @PathVariable("cmd") int cmd) {
        try {
            ArduinoSerialPortListener arduinoSerialPortListener = arduinoSerialPortInitializer.getArduinoSerialPortListener(arduino);
            if(isNull(arduinoSerialPortListener.schedules.get(cmd))) {
                ScheduledFuture<?> task = taskScheduler.schedule(() -> arduinoSerialPortListener.execute(cmd), new CronTrigger("0/10 * * * * *"));
                arduinoSerialPortListener.schedules.put(cmd, task);
            }
        }
        catch (SerialPortException ex) {
            System.out.println(ex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "start";
    }

    @RequestMapping(value = "/stop/{id}/execute/{cmd}", method = RequestMethod.GET)
    public String stop(@PathVariable("id") Arduino arduino, @PathVariable("cmd") int cmd) {
        try {
            ArduinoSerialPortListener arduinoSerialPortListener = arduinoSerialPortInitializer.getArduinoSerialPortListener(arduino);
            ScheduledFuture<?> task = arduinoSerialPortListener.schedules.get(cmd);
            if(!isNull(task)) {
                task.cancel(false);
                arduinoSerialPortListener.schedules.remove(cmd);
            }
        }
            catch (SerialPortException ex) {
            System.out.println(ex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "stop";
    }

    private String execute(Arduino arduino, int cmd) {
        try {
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

