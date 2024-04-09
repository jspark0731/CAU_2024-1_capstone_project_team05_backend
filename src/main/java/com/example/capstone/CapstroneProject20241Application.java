package com.example.capstone;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.capstone.mapper")
public class CapstroneProject20241Application
{
	public static void main(String[] args)
	{
		SpringApplication.run(CapstroneProject20241Application.class, args);
	}
}
