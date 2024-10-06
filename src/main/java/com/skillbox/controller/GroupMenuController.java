package com.skillbox.controller;

import com.skillbox.controller.option.GroupOption;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static com.skillbox.controller.option.GroupOption.EXIT;
import static com.skillbox.controller.option.GroupOption.WITHOUT_GROUPING;

/**
 * Консольный контроллер для управления навигацией по аналитике транзакций.
 */
public class GroupMenuController extends AbstractMenuController<GroupOption> {

    public GroupMenuController() {
        super(GroupOption.class, "Выберите опцию группировки транзакции");
    }

    public GroupOption getGroupOption() {
        GroupOption option = selectMenu();
        return option == EXIT ? WITHOUT_GROUPING : option;
    }
}
