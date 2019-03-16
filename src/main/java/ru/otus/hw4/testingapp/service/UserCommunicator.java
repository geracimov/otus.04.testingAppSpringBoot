package ru.otus.hw4.testingapp.service;

import ru.otus.hw4.testingapp.domain.Question;

public interface UserCommunicator {

    void startSession();

    String getAnswer(String question);

    void showQuestion(Question question);
}
