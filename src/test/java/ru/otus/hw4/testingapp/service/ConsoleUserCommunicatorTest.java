package ru.otus.hw4.testingapp.service;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

class ConsoleUserCommunicatorTest {

    @Test
    void startSession() {
        InputStream stdin = System.in;
        String input = "Hello";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }

    @Test
    void getAnswer() {
    }

    @Test
    void showQuestion() {
    }
}