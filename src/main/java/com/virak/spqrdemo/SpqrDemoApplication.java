package com.virak.spqrdemo;

import com.virak.spqrdemo.model.Food;
import com.virak.spqrdemo.service.FoodService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.stream.Stream;

@SpringBootApplication
public class SpqrDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpqrDemoApplication.class, args);
	}
	@Bean
	ApplicationRunner init(FoodService foodService) {
		return args -> {
			Stream.of("Pizza", "Spam", "Eggs", "Avocado").forEach(name -> {
				Food food = new Food();
				food.setName(name);
				foodService.saveFood(food);
			});
			foodService.getFoods().forEach(System.out::println);
		};
	}

}
