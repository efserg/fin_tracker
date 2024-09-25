package com.skillbox.console.dto;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public enum GroupOption {
    GROUP_BY_MONTHS(1, "группировка по месяцам"),
    GROUP_BY_YEARS(2, "группировка по годам"),
    GROUP_BY_DAY_OF_WEEK(3, "группировка по дню недели"),
    GROUP_BY_CATEGORY(4, "группировка по категории"),
    GROUP_BY_INCOME_AND_EXPENSE(5, "считать доходы и расходы"),
    WITHOUT_GROUPING(6, "без группировки (сумма всех транзакций)");

    private final int option;
    private final String name;

    private static final Map<Integer, GroupOption> MAP = Arrays
            .stream(GroupOption.values())
            .collect(Collectors.toMap(GroupOption::getOption, Function.identity()));

    GroupOption(int option, String name) {
        this.option = option;
        this.name = name;
    }

    public static GroupOption of(int option) {
        return MAP.get(option);
    }

    @Override
    public String toString() {
        return option + " - " + name;
    }
}
