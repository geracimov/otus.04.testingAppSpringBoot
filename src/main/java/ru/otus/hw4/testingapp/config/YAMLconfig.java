package ru.otus.hw4.testingapp.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.InputStream;
import java.nio.file.Path;

@Getter
@Setter
@ConfigurationProperties("testapp")
public class YAMLconfig {
    private Path csvPath;
    private String globTemplate;
    private String separator;
    private String isCorrectSuffix;
    private String inputStream;

    public void setCsvPath(String csvPath) {
        this.csvPath = Path.of(csvPath);
    }
}
