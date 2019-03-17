package ru.otus.hw4.testingapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw4.testingapp.domain.Person;
import ru.otus.hw4.testingapp.domain.Question;
import ru.otus.hw4.testingapp.domain.Result;
import ru.otus.hw4.testingapp.utils.LocaleHelper;

import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Testing service")
class SimpleTestingServiceTest {
    private static final Locale ENG = Locale.ENGLISH;
    private static final Locale RUS = LocaleHelper.getLocale("ru_RU");
    @Autowired
    private TestingService ts;


    @BeforeEach
    void init() {
        ts.breakTest();
    }

    @Test
    @DisplayName("return all available test")
    void getAvailTests() {
        assertAll("Testing service contains all available test",
                  () -> assertTrue(ts.getAvailTests(ENG)
                                     .contains("testdata_en")),
                  () -> assertEquals(1,
                                     ts.getAvailTests(ENG)
                                       .size()),
                  () -> assertTrue(ts.getAvailTests(RUS)
                                     .contains("testdata_ru")),
                  () -> assertEquals(1,
                                     ts.getAvailTests(RUS)
                                       .size()));
    }

    @Test
    @DisplayName("throw exception on getting next question before start of testing")
    void getNextBeforeStartTest() {
        assertFalse(ts.hasNext());
        assertNull(ts.next());
    }

    @Test
    @DisplayName("not allow start testing by illegal testing name")
    void startWrongTest() {
        assertThrows(IllegalArgumentException.class, () -> ts.startTest("wrongNameTest", "firstname", "surname"));
    }

    @Test
    @DisplayName("correctly return Question by next method")
    void getNextAfterStartTest() {
        ts.startTest("testdata_en", "firstname", "surname");
        assertTrue(ts.hasNext());
        assertThat(ts.next()).isInstanceOf(Question.class);
    }

    @Test
    @DisplayName("correctly finished testing case")
    void getMoreNextQuestionWithAnswerToFinish() {
        ts.startTest("testdata_en", "firstname", "surname");

        Question question = ts.next();
        assertTrue(ts.doAnswer(question, "1"));

        question = ts.next();
        assertFalse(ts.doAnswer(question, "99"));
        assertTrue(ts.doAnswer(question, "0"));

        question = ts.next();
        assertFalse(ts.doAnswer(question, "aaa"));
        assertTrue(ts.doAnswer(question, "1"));

        question = ts.next();
        assertFalse(ts.doAnswer(question, "5"));
        assertTrue(ts.doAnswer(question, "0"));

        assertFalse(ts.hasNext());
        assertNull(ts.next());
    }

    @Test
    @DisplayName("not allow get result before testing finished")
    void getResultBeforeFinish() {
        assertTrue(ts.getResult()
                     .isEmpty());
    }

    @Test
    @DisplayName("allow get result after testing finished")
    void getResultAfterFinish() {
        ts.startTest("testdata_en", "firstname", "surname");

        while (ts.hasNext()) {
            Question question = ts.next();
            ts.doAnswer(question, "0");
        }
        Optional<Result> resOpt = ts.getResult();

        assertTrue(resOpt.isPresent());
        Result res = resOpt.get();
        assertThat(res).isInstanceOf(Result.class);
        assertNotNull(res);
        assertEquals(res.getScore(), 2);
        assertThat(res.getPerson()).isEqualTo(new Person("surname", "firstname"));
    }
}