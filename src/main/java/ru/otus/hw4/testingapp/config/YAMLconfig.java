package ru.otus.hw4.testingapp.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@Getter
@Setter
@ConfigurationProperties("testapp")
public class YAMLconfig {
    private Resource csvPath;
    private String globTemplate;
    private String separator;
    private String isCorrectSuffix;
    private Resource inputFromFile;
}
