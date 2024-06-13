package com.example.capstone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CapstoneProject20241Application
{
	public static void main(String[] args)
	{
		SpringApplication.run(CapstoneProject20241Application.class, args);
	}
}
