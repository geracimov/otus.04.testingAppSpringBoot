package ru.otus.hw4.testingapp;


import org.junit.FixMethodOrder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import ru.otus.hw4.testingapp.config.MSconfig;
import ru.otus.hw4.testingapp.dao.TestDataService;
import ru.otus.hw4.testingapp.service.TestingService;
import ru.otus.hw4.testingapp.service.UserCommunicator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@DisplayName("Context")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class TestingappApplicationTests {

    @Autowired
    private ApplicationContext context;

    @Test
    @DisplayName("created correctly")
    void contextLoads() {
        assertNotNull(context, "context is not null");
    }

    @Test
    @DisplayName("contains MessageSource bean")
    void MSconfigExists() {
        assertThat(context.getBean(MSconfig.class)).isNotNull();
    }

    @Test
    @DisplayName("contains TestDataService bean")
    void TestDataServiceExists() {
        assertThat(context.getBean(TestDataService.class)).isNotNull();
    }

    @Test
    @DisplayName("contains TestingService bean")
    void TestServiceExists() {
        assertThat(context.getBean(TestingService.class)).isNotNull();
    }

    @Test
    @DisplayName("contains UserCommunicator bean")
    void UserCommunicatorExists() {
        assertThat(context.getBean(UserCommunicator.class)).isNotNull();
    }
}
