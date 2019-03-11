package ru.otus.hw4.testingapp.domain;

import lombok.Data;

@Data
public class Choice {
    private final String text;
    private final boolean correct;
}
