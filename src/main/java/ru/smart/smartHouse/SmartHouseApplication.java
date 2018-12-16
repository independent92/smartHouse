package ru.smart.smartHouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@SpringBootApplication
public class SmartHouseApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartHouseApplication.class, args);
	}
// Aliasing
/*
Автосвязывание в приоритете идет по бину,
если имя метода с аннотацией @Bean возвращает тип,
равный типу обьекта с аннотацией @Autowired,
если имя метода равно имени поля или сеттера.(самый приоритетный случай)

	@Bean({"test1"})
	public Test test() {
		return new Test("tetest1");
	}

	@Bean
	public Test test2() {
		return new Test("tetest2");
	}*/

	@Bean
	TaskScheduler threadPoolTaskScheduler() {
		return new ThreadPoolTaskScheduler();
	}

}
