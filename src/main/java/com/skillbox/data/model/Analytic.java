package com.skillbox.data.model;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Value;

@Value
public class Analytic {
    LocalDateTime date;
    String groupOption;
    String aggregateOption;
    String filter;
    Map<?, ?> data;

    @Override
    public String toString() {
        return "Дата: " + date + "\n"
                + "Имя: '" + groupOption + " (" +
                aggregateOption + ")" + '\'' + "\n" +
                "Фильтр: " + filter + "\n" +
                "===================================\n" +
                "           Аналитика: \n" +
                data.entrySet().stream().map(entry -> entry.getKey() + ": " + entry.getValue()).collect(Collectors.joining("\n"));
    }
}
