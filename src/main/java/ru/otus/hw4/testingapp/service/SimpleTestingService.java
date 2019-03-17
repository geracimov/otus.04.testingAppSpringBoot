package ru.otus.hw4.testingapp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.hw4.testingapp.dao.TestDataService;
import ru.otus.hw4.testingapp.domain.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * логика подсчета баллов выдуманная:суммируется количество верно отмеченных
 * ответов, несмотря на неправильно отмеченные
 */
@Slf4j
@Service
public class SimpleTestingService implements TestingService {

    private final TestDataService testDataService;
    private Test test;
    private Person person;
    private List<Answer> answers;
    private int currQuestionIndex;

    @Autowired
    public SimpleTestingService(TestDataService testDataService) {
        this.testDataService = testDataService;
    }

    @Override
    public Set<String> getAvailTests(Locale locale) {
        return testDataService.getAvailTests()
                              .stream()
                              .filter(t -> t.matches(".+_" + locale.getLanguage()))
                              .collect(Collectors.toSet());
    }

    @Override
    public void startTest(String testName, String firstname, String surname) {
        Test test = testDataService.getTest(testName);
        if (test == null) {
            throw new IllegalArgumentException("Incorrect test name!");
        }
        this.test = test;
        this.person = new Person(surname, firstname);
        this.answers = new ArrayList<>();
        currQuestionIndex = 0;
    }

    @Override
    public boolean testIsExists(String testName) {
        return testDataService.getTest(testName) != null;
    }

    @Override
    public Question next() {
        if (hasNext()) {
            return test.getQuestions()
                       .get(currQuestionIndex++);
        }
        return null;
    }

    @Override
    public boolean hasNext() {
        if (test == null) {
            return false;
        }
        return answers.size() < this.test.getQuestions()
                                         .size();
    }

    @Override
    public boolean doAnswer(Question question, String textAnswer) {
        try {
            List<Choice> choices = Stream.of(textAnswer.split("[\\s]"))
                                         .mapToInt(Integer::parseInt)
                                         .mapToObj(i -> question.getChoices()
                                                                .get(i))
                                         .collect(Collectors.toList());


            answers.add(new Answer(question, choices));
            return true;
        } catch (Exception e) {
            log.error(String.format("Error during answer the question/answer (%s)/(%s)", question, textAnswer));
            return false;
        }
    }

    @Override
    public Optional<Result> getResult() {
        return Optional.ofNullable(calcResult());
    }

    @Override
    public void breakTest() {
        test = null;
        person = null;
        answers = null;
        currQuestionIndex = 0;
    }


    private Result calcResult() {
        if (test == null) {
            return null;
        }
        int score = 0;
        for (Answer answer : answers) {
            score += answer.getChoices()
                           .stream()
                           .mapToInt(choice -> choice.isCorrect()
                                               ? 1
                                               : 0)
                           .sum();
        }
        int totalScore = totalScore(this.test);
        double prc = roundAvoid(100.0 * score / totalScore, 2);
        return new Result(person, score, prc);
    }

    private int totalScore(Test test) {
        return test.getQuestions()
                   .stream()
                   .mapToInt(this::totalScore)
                   .sum();
    }

    private int totalScore(Question question) {
        return question.getChoices()
                       .stream()
                       .mapToInt(choice -> choice.isCorrect()
                                           ? 1
                                           : 0)
                       .sum();
    }

    private double roundAvoid(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }
}
