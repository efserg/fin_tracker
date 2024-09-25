package com.skillbox.console;

import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Абстрактный класс контроллеров.
 * Содержит общий функционал вывода меню и получения ввода пользователя
 */
public abstract class AbstractMenuController {

    protected static final int EXIT_OPTION = 0;
    protected final Scanner scanner;
    private final Set<Integer> options;

    protected AbstractMenuController(Set<Integer> options) {
        this.options = Stream.concat(
                        options.stream(),
                        Stream.of(EXIT_OPTION))
                .collect(Collectors.toSet());
        this.scanner = new Scanner(System.in).useDelimiter("\n");
    }

    /**
     * Возвращает список возможных действий в меню.
     *
     * @return список возможных действий в меню.
     */
    protected abstract String getMenuDescription();

    protected int selectMenu() {
        int option;

        while (true) {
            System.out.print(getMenuDescription()
                    + EXIT_OPTION + " – выход\n"
                    + "\n"
                    + "Введите нужную опцию и нажмите Enter: ");

            option = scanner.nextInt();
            if (options.contains(option)) {
                break;
            }
            System.out.println("Выбрана неверная опция!\n"
                    + "Попробуйте заново.\n");

        }
        return option;
    }
}
