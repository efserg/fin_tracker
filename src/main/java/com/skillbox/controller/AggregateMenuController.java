package com.skillbox.controller;

import com.skillbox.controller.option.AggregateOption;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static com.skillbox.controller.option.AggregateOption.EXIT;
import static com.skillbox.controller.option.AggregateOption.SUM;

/**
 * Консольный контроллер для управления навигацией по аналитике транзакций.
 */
public class AggregateMenuController extends AbstractMenuController<AggregateOption> {

    public AggregateMenuController() {
        super(AggregateOption.class, "Выберите способ агрегации транзакций");
    }

    public AggregateOption getAggregateOption() {
        AggregateOption option = selectMenu();
        return option == EXIT ? SUM : option;
    }
}
