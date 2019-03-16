package ru.otus.hw4.testingapp.domain;

import lombok.Data;

import java.util.List;


@Data
public class Test {
    private final String name;
    private final List<Question> questions;
    private List<Result> results;
}
