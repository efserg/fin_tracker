package com.skillbox.console;

import com.skillbox.console.dto.AggregateOption;
import com.skillbox.console.dto.GroupOption;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static com.skillbox.console.dto.AggregateOption.AVERAGE;
import static com.skillbox.console.dto.AggregateOption.COUNT;
import static com.skillbox.console.dto.AggregateOption.SUM;

/**
 * Консольный контроллер для управления навигацией по аналитике транзакций.
 */
public class AggregateMenuController extends AbstractMenuController {

    private static final Set<Integer> OPTIONS = Arrays
            .stream(GroupOption.values())
            .map(GroupOption::getOption)
            .collect(Collectors.toSet());

    public AggregateMenuController() {
        super(OPTIONS);
    }

    public AggregateOption getAggregateOption() {
        while (true) {
            int i = selectMenu();
            if (i == 0) {
                return SUM;
            }
            return AggregateOption.of(i);
        }
    }

    @Override
    protected String getMenuDescription() {
        return "\nВыберите способ агрегации транзакций\n"
                + "\n"
                + SUM + "\n"
                + AVERAGE + "\n"
                + COUNT + "\n";
    }
}
