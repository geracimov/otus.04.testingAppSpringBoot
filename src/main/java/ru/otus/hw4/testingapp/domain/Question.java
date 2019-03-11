package ru.otus.hw4.testingapp.domain;

import lombok.Data;

import java.util.List;

@Data
public class Question {
    private final String text;
    private final List<Choice> choices;
}
