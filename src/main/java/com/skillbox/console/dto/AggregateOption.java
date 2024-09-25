package com.skillbox.console.dto;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public enum AggregateOption {
    SUM(1, "подсчет суммы"),
    AVERAGE(2, "подсчет среднего значения"),
    COUNT(3, "подсчет количества");

    private final int option;
    private final String name;

    private static final Map<Integer, AggregateOption> MAP = Arrays
            .stream(AggregateOption.values())
            .collect(Collectors.toMap(AggregateOption::getOption, Function.identity()));

    AggregateOption(int option, String name) {
        this.option = option;
        this.name = name;
    }

    public static AggregateOption of(int option) {
        return MAP.get(option);
    }

    @Override
    public String toString() {
        return  option + " - " + name;
    }
}
