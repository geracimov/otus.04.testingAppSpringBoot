package ru.otus.hw4.testingapp.dao;

import ru.otus.hw4.testingapp.domain.Test;

import java.util.Set;

public interface TestDataService {

    Set<String> getAvailTests();

    Test getTest(String testName);
}
