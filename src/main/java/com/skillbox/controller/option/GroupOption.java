package com.skillbox.controller.option;

import lombok.Getter;

@Getter
public enum GroupOption implements MenuOption {
    WITHOUT_GROUPING("вернуться назад (без группировки)"),
    GROUP_BY_MONTHS("группировка по месяцам"),
    GROUP_BY_YEARS("группировка по годам"),
    GROUP_BY_DAY_OF_WEEK("группировка по дню недели"),
    GROUP_BY_CATEGORY("группировка по категории"),
    GROUP_BY_INCOME_AND_EXPENSE("считать доходы и расходы"),
    GROUP_BY_ACCOUNT_TYPE("группировка по типу счета"),
    GROUP_BY_USER_ID("группировка по id пользователя");

    private final String name;

    GroupOption(String name) {
        this.name = name;
    }

    public static GroupOption of(int option) {
        return OptionUtils.of(GroupOption.class, option);
    }

    @Override
    public int getOption() {
        return ordinal();
    }
}
