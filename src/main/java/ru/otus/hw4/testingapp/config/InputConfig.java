package ru.otus.hw4.testingapp.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
@Slf4j
public class InputConfig {
    private final YAMLconfig yamLconfig;

    public InputConfig(YAMLconfig yamLconfig) {this.yamLconfig = yamLconfig;}

    /**
     * Хотел для создать дял возможности тестировать класс, отвечающий за взаимодейтсивие с порльзователем через
     * консоль.
     * Проблема в том, что если проперти с таким именем есть, но не существует такой файл - приложение валится по
     * ошибке, с причиной что невозможно создать бин ConsoleUserCommunicator которому спринг не может в
     * конструктор подобрать подходящий бин InputStream, так их нет. Непонятно почему такое происходит, по моей логике я пытаюсь
     * создать бин fileInput, в случае если создать не могу - то по ConditionalOnMissingBean создается второй бин для
     * чтения с консоли.
     * Подскажите, что я делаю не так? или при этом раз я возвращаю ссылку на объект типа InputStream, пусть и null,
     * то и бин наверно все равно создается? как  прервать создание бина fileInput?
     *
     * @since 16/03/2019 вопрос снят, заменил условие создания бина на ConditionalOnResource, в этом случае спринг сам
     * контроллирует наличие файла.
     */
    @Bean
    @ConditionalOnResource(resources = {"${testapp.input-from-file}"})
    public InputStream fileInput() throws IOException {
        Resource res = yamLconfig.getInputFromFile();
        return new FileInputStream(res.getFile());
    }

    @Bean
    @ConditionalOnMissingBean(name = "fileInput")
    public InputStream consoleInput() {
        return System.in;
    }

}
