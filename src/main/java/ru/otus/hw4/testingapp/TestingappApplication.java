package ru.otus.hw4.testingapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.otus.hw4.testingapp.config.YAMLconfig;

@SpringBootApplication
@EnableConfigurationProperties(YAMLconfig.class)
public class TestingappApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestingappApplication.class, args);
    }
}
