package com.projekt.dbs;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.spring.annotation.EnableVaadin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.projekt.dbs")
@EnableVaadin("com.projekt.dbs")
@CssImport("stylesheet.css")
public class MainApp {
	public static void main(String[] args) {
		SpringApplication.run(MainApp.class, args);
	}

}
