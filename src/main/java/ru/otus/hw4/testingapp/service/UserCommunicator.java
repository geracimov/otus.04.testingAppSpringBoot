package ru.otus.hw4.testingapp.service;

import ru.otus.hw4.testingapp.domain.Question;
import ru.otus.hw4.testingapp.domain.Result;

import java.util.Optional;

public interface UserCommunicator {

    Optional<Result> getResult();

    boolean resultIsAvail();

    String currentLocale();

    void changeLocale(String lang);

    void startSession();

    String getAnswer(String question);

    void showQuestion(Question question);
}
