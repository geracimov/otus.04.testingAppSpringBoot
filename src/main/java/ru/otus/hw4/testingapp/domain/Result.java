package ru.otus.hw4.testingapp.domain;

import lombok.Data;

@Data
public class Result {
    private final Person person;
    private final int score;
    private final double prc;
}

