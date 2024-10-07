package com.skillbox.data.model;

import com.skillbox.controller.dto.TransactionFilterDto;
import com.skillbox.controller.option.AggregateOption;
import com.skillbox.controller.option.GroupOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Analytic(GroupOption groupOption,
                    AggregateOption aggregateOption,
                    TransactionFilterDto transactionFilter,
                    Map<?, ?> data) {
        this.date = LocalDateTime.now();
        this.groupOption = groupOption.getName();
        this.aggregateOption = aggregateOption.getName();
        this.filter = transactionFilter.toString();
        this.data = data;
    }

    @Override
    public String toString() {
        return "===================================\n" +
                "Дата: " + FORMATTER.format(date) + "\n" +
                "Имя: '" + groupOption + " (" +
                aggregateOption + ")" + '\'' + "\n" +
                "Фильтр: " + filter + "\n" +
                "-----------------------------------\n" +
                "            Аналитика: \n" +
                data.entrySet().stream().map(entry -> entry.getKey() + ": " + entry.getValue()).collect(Collectors.joining("\n"));
    }
}
