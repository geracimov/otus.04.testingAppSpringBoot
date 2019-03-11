package ru.otus.hw4.testingapp.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw4.testingapp.domain.Question;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Testing service")
class LocalFileTestDataServiceTest {

    @Autowired
    private TestDataService dataService;

    @Test
    @DisplayName("correctly get test list")
    void getAvailTests() {
        Set<String> tests = dataService.getAvailTests();
        System.out.println(tests);
        assertEquals(tests.size(), 2);
        assertTrue(tests.contains("testdata_en") && tests.contains("testdata_ru"));
    }

    @Test
    @DisplayName("get test by name")
    void getTest() {
        ru.otus.hw4.testingapp.domain.Test test1 = dataService.getTest("testdata_en");
        assertEquals("testdata_en", test1.getName());
        assertEquals(4,
                     test1.getQuestions()
                          .size());
    }

    @Test
    @DisplayName("return null if test name is empty")
    void getTestEmpty() {
        assertNull(dataService.getTest(""));
    }

    @Test
    @DisplayName("exclude wrong test declaration")
    void testContainsWrongQuestion() {
        ru.otus.hw4.testingapp.domain.Test test = dataService.getTest("testdata_en");
        assertFalse(test.getQuestions()
                        .stream()
                        .map(Question::getText)
                        .anyMatch(s -> s.equals("question4")));
    }
}