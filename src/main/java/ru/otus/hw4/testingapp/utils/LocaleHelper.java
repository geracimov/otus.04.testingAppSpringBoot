package ru.otus.hw4.testingapp.utils;

import java.util.Locale;
import java.util.StringTokenizer;

public class LocaleHelper {


    public static Locale getLocale(String localeName) {
        if (localeName == null || localeName.isEmpty()) {
            return null;
        }
        StringTokenizer tokenizer = new StringTokenizer(localeName, "_");

        String lang = tokenizer.hasMoreTokens()
                      ? (String) tokenizer.nextElement()
                      : "";
        String country = tokenizer.hasMoreTokens()
                         ? (String) tokenizer.nextElement()
                         : "";
        String variant = tokenizer.hasMoreTokens()
                         ? (String) tokenizer.nextElement()
                         : "";
        return new Locale(lang, country, variant);
    }
}
