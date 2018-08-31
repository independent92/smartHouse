package ru.smart.smartHouse;

import jssc.SerialPort;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.smart.smartHouse.component.ArduinoSerialPortSingleton;

@SpringBootApplication
public class SmartHouseApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartHouseApplication.class, args);
	}

	@Bean
	public SerialPort ArduinoSerialPortSingleton(){
		return ArduinoSerialPortSingleton.getInstance();
	}
}
