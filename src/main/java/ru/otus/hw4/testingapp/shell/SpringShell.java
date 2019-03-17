package ru.otus.hw4.testingapp.shell;

import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.otus.hw4.testingapp.service.UserCommunicator;

@ShellComponent
public class SpringShell {

    private final UserCommunicator userCommunicator;

    public SpringShell(UserCommunicator userCommunicator) {this.userCommunicator = userCommunicator;}

    @ShellMethod(value = "Start session", group = "Testing Commands", key = "start")
    public void startSession() {
        userCommunicator.startSession();
    }

    @ShellMethod(value = "Get test result", group = "Testing Commands", key = "result")
    public void getResult() {
        System.out.println(userCommunicator.getResult());
    }

    @ShellMethod(value = "Print/Change locale", group = "Locale Commands", key = "locale", prefix = "-")
    protected void changeLocale(@ShellOption(defaultValue = ShellOption.NULL) String lang) {
        if (lang == null) {
            System.out.println(userCommunicator.currentLocale());
        } else {
            userCommunicator.changeLocale(lang);
        }
    }

    @ShellMethodAvailability({"result"})
    public Availability availabilityResultCheck() {
        return userCommunicator.resultIsAvail()
               ? Availability.available()
               : Availability.unavailable("you are not started session");
    }
}
