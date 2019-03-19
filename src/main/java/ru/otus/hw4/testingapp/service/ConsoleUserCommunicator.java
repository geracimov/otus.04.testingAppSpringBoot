package ru.otus.hw4.testingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.hw4.testingapp.domain.Question;
import ru.otus.hw4.testingapp.domain.Result;
import ru.otus.hw4.testingapp.utils.LocaleHelper;

import java.io.InputStream;
import java.util.Locale;
import java.util.Optional;
import java.util.Scanner;

@Service
public class ConsoleUserCommunicator implements UserCommunicator {
    private final TestingService service;
    private final Scanner scanner;
    private final MessageSource ms;
    private Locale locale;

    @Autowired
    public ConsoleUserCommunicator(TestingService service, MessageSource messageSource, InputStream inputStream) {
        this.service = service;
        this.ms = messageSource;
        this.scanner = new Scanner(inputStream);
        this.locale = Locale.getDefault();
    }

    @Override
    public void startSession() {
        changeLocale();

        if (service.getAvailTests(locale)
                   .size() == 0) {
            textDialog(message("test.not.found.in.path"));
            return;
        }

        boolean repeatTest = true;
        String y = message("test.repeat.y");
        String yes = message("test.repeat.yes");
        String no = message("test.repeat.no");

        while (repeatTest) {
            textDialog(message("test.avail.list", service.getAvailTests(locale)));

            String testName = inputDialog(message("test.select"));
            if (!service.testIsExists(testName)) {
                textDialog(message("test.is.not.exists"));
                continue;
            }
            String surname = inputDialog(message("person.surname"));
            String firstname = inputDialog(message("person.firstname"));

            service.startTest(testName, firstname, surname);
            while (service.hasNext()) {
                Question q = service.next();
                boolean accepted = false;
                while (!accepted) {
                    showQuestion(q);
                    String textAnswer = inputDialog(message("answer.text"));
                    accepted = service.doAnswer(q, textAnswer);
                }
            }
            Optional<Result> resultOpt = getResult();
            if (resultOpt.isPresent()) {
                Result result = resultOpt.get();
                textDialog(message("result", result.getScore(), result.getPrc()));
            } else {
                textDialog(message("test.is.not.competed"));
            }

            repeatTest =
                    inputDialog(message("test.repeat") + " (" + yes + "/" + no + ")").matches("(?i)" + y + "|" + yes);
        }
    }

    @Override
    public String getAnswer(String question) {
        return inputDialog(question);
    }

    @Override
    public void showQuestion(Question question) {
        if (question == null) {
            return;
        }
        textDialog(question.getText());
        for (int i = 0;
             i < question.getChoices()
                         .size();
             i++) {
            textDialog(String.format("%s) %s\n",
                                     i,
                                     question.getChoices()
                                             .get(i)
                                             .getText()));
        }
    }

    @Override
    public Optional<Result> getResult() {
        return service.getResult();
    }

    @Override
    public boolean resultIsAvail() {
        return service.getResult()
                      .isPresent();
    }

    public void changeLocale() {
        textDialog(message("lang.current", locale, locale.getDisplayLanguage(locale)));
        String lang = inputDialog(message("lang.change", locale.getDisplayLanguage(locale)));
        changeLocale(lang);
    }

    public void changeLocale(String lang) {
        if (!lang.isEmpty()) {
            locale = LocaleHelper.getLocale(lang);
            Locale.setDefault(locale);
            textDialog(message("lang.changed", locale.getDisplayLanguage(locale)));
        }
    }

    public String currentLocale() {
        return locale.getLanguage();
    }

    private String inputDialog(String shownText) {
        textDialog(shownText + "> ");
        return scanner.nextLine();
    }

    private void textDialog(String shownText) {
        System.out.print("\n" + shownText);
    }

    private String message(String textBundle, Object... objects) {
        return ms.getMessage(textBundle, objects, locale);
    }
}
