package com.example.demo;

import com.example.demo.controller.Controller;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Queue;

@SpringBootApplication
@EnableScheduling
public class DemoApplication {
	public static void main(String[] args) {


		SpringApplication.run(DemoApplication.class, args);

	}
}
