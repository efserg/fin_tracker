package com.skillbox.console;

import com.skillbox.console.dto.GroupOption;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static com.skillbox.console.dto.GroupOption.GROUP_BY_CATEGORY;
import static com.skillbox.console.dto.GroupOption.GROUP_BY_DAY_OF_WEEK;
import static com.skillbox.console.dto.GroupOption.GROUP_BY_INCOME_AND_EXPENSE;
import static com.skillbox.console.dto.GroupOption.GROUP_BY_MONTHS;
import static com.skillbox.console.dto.GroupOption.GROUP_BY_YEARS;
import static com.skillbox.console.dto.GroupOption.WITHOUT_GROUPING;

/**
 * Консольный контроллер для управления навигацией по аналитике транзакций.
 */
public class GroupMenuController extends AbstractMenuController {

    private static final Set<Integer> OPTIONS = Arrays
            .stream(GroupOption.values())
            .map(GroupOption::getOption)
            .collect(Collectors.toSet());

    public GroupMenuController() {
        super(OPTIONS);
    }

    public GroupOption getGroupOption() {
        while (true) {
            int i = selectMenu();
            if (i == 0) {
                return WITHOUT_GROUPING;
            }
            return GroupOption.of(i);
        }
    }

    @Override
    protected String getMenuDescription() {
        return "\nВыберите опцию группировки транзакции\n"
                + "\n"
                + GROUP_BY_MONTHS + "\n"
                + GROUP_BY_YEARS + "\n"
                + GROUP_BY_DAY_OF_WEEK + "\n"
                + GROUP_BY_CATEGORY + "\n"
                + GROUP_BY_INCOME_AND_EXPENSE + "\n"
                + WITHOUT_GROUPING + "\n"

                ;
    }
}
