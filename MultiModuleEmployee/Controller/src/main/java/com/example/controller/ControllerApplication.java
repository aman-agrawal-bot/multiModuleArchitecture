package com.example.controller;

import static com.example.commonInfrastructure.StringConstants.BASE_PACKAGE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = BASE_PACKAGE)
@EnableCaching
@EntityScan(basePackages = BASE_PACKAGE)
@EnableJpaRepositories(basePackages = BASE_PACKAGE)
public class ControllerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ControllerApplication.class, args);
  }

}
