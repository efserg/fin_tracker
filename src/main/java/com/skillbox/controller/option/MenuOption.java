package com.skillbox.controller.option;

import java.util.Arrays;
import java.util.stream.Collectors;

public interface MenuOption {

    /**
     * Возвращает значение option элемента перечисления.
     *
     * @return значение option
     */
    int getOption();

    /**
     * Возвращает описание элемента перечисления.
     * @return описание
     */
    String getName();

    /**
     * Возвращает строковое представление элемента перечисления.
     *
     * @return строковое представление
     */
    default String toStringRepresentation() {
        return getOption() + " - " + getName();
    }

}
