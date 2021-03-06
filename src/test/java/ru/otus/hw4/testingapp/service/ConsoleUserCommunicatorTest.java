package ru.otus.hw4.testingapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import ru.otus.hw4.testingapp.config.YAMLconfig;
import ru.otus.hw4.testingapp.domain.Question;
import ru.otus.hw4.testingapp.domain.Result;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Communicator service")
class ConsoleUserCommunicatorTest {
    @Autowired
    private UserCommunicator uc;
    @Autowired
    private TestingService ts;
    @Autowired
    private YAMLconfig config;

    @BeforeEach
    void init() {
        config.setCsvPath(new ClassPathResource("classpath:InputTest/inputFullData.txt"));
    }

    @Test
    @DisplayName("complete test successfully")
    void startSession() {
        uc.startSession();
        Optional<Result> resultOpt = ts.getResult();
        assertTrue(resultOpt.isPresent());
        Result res = resultOpt.get();
        assertThat(res).isInstanceOf(Result.class);
        assertEquals(2, res.getScore());
    }

    @Test
    void getAnswer() {
        String answer = uc.getAnswer("question");
        assertEquals("my_answer", answer);
    }

    @Test
    void showQuestion() {
        assertDoesNotThrow(() -> uc.showQuestion(new Question("name", new ArrayList<>())));
    }
}