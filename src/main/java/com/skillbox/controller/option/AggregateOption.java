package com.skillbox.controller.option;

import lombok.Getter;

@Getter
public enum AggregateOption implements MenuOption {
    SUM("подсчет суммы"),
    AVERAGE("подсчет среднего значения"),
    COUNT("подсчет количества");

    private final String name;

    AggregateOption(String name) {
        this.name = name;
    }

    public static AggregateOption of(int option) {
        return OptionUtils.of(AggregateOption.class, option);
    }

    @Override
    public int getOption() {
        return ordinal();
    }
}
