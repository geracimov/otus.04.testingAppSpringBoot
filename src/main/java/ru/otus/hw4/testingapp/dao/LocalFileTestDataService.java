package ru.otus.hw4.testingapp.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import ru.otus.hw4.testingapp.config.YAMLconfig;
import ru.otus.hw4.testingapp.domain.Choice;
import ru.otus.hw4.testingapp.domain.Question;
import ru.otus.hw4.testingapp.domain.Test;
import ru.otus.hw4.testingapp.utils.FileHelper;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class LocalFileTestDataService implements TestDataService {
    private final Map<String, Test> tests;
    private final YAMLconfig config;

    @Autowired
    public LocalFileTestDataService(YAMLconfig config) {
        this.config = config;
        tests = new HashMap<>();
        loadTestList();
    }

    @Override
    public Set<String> getAvailTests() {
        return tests.keySet();
    }

    @Override
    public Test getTest(String testName) {
        if (testName == null || testName.isEmpty()) {
            return null;
        }
        return tests.get(testName);
    }

    private void loadTestList() {
        Resource csvPath = config.getCsvPath();
        String glob = config.getGlobTemplate();
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(csvPath.getFile()
                                                                        .toPath(), glob)) {
            Stream<Path> pathStream = StreamSupport.stream(ds.spliterator(), false);
            pathStream.map(this::buildTest)
                      .filter(Objects::nonNull)
                      .forEach(test -> tests.put(test.getName(), test));
        } catch (IOException e) {
            log.error("Error loading tests from directory " + csvPath, e);
        }
    }

    private Test buildTest(Path file) {
        if (file == null) {
            return null;
        }
        try {
            List<Question> questions = Files.lines(file)
                                            .map(this::buildQuestion)
                                            .filter(Objects::nonNull)
                                            .collect(Collectors.toList());
            String testName = FileHelper.getBaseName(file.toString());
            return new Test(testName, questions);
        } catch (IOException e) {
            log.error("Error parsing file: " + file.toString(), e);
            return null;
        }
    }

    private Question buildQuestion(String str) {
        String[] description = str.split(config.getSeparator());
        //в строке должен как минимум 1 вопрос и 1 ответ
        if (description.length < 2) {
            log.error("Incorrect question format! Skip question: " + str);
            return null;
        }
        String[] questions = Arrays.copyOfRange(description, 1, description.length);

        List<Choice> choices = buildChoices(questions);
        return new Question(description[0], choices);
    }

    private List<Choice> buildChoices(String[] stringChoises) {
        return Stream.of(stringChoises)
                     .map(s -> {
                         boolean isCorrect = s.matches(".*" + config.getIsCorrectSuffix());
                         String text = isCorrect
                                       ? s.replaceAll(config.getIsCorrectSuffix(), "")
                                          .trim()
                                       : s;
                         return new Choice(text, isCorrect);
                     })
                     .collect(Collectors.toList());
    }
}
