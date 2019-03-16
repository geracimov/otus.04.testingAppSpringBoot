package ru.otus.hw4.testingapp.service;

import ru.otus.hw4.testingapp.domain.Question;
import ru.otus.hw4.testingapp.domain.Result;

import java.util.Locale;
import java.util.Set;

public interface TestingService {

    Set<String> getAvailTests(Locale locale);

    void startTest(String testName, String firstname, String surname);

    boolean testIsExists(String testName);

    Question next();

    boolean hasNext();

    boolean doAnswer(Question question, String textAnswer);

    Result getResult();

    void breakTest();
}
