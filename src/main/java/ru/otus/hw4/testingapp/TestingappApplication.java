package ru.otus.hw4.testingapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import ru.otus.hw4.testingapp.config.YAMLconfig;
import ru.otus.hw4.testingapp.service.UserCommunicator;

@SpringBootApplication
@EnableConfigurationProperties(YAMLconfig.class)
public class TestingappApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(TestingappApplication.class, args);
        UserCommunicator uc = context.getBean(UserCommunicator.class);
        uc.startSession();
    }
}
