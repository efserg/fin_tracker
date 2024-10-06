package com.skillbox.controller;

import com.skillbox.controller.dto.TransactionFilterDto;
import com.skillbox.controller.option.SearchOption;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Консольный контроллер для управления навигацией по функционалу поиска транзакций.
 */
public class SearchMenuController extends AbstractMenuController<SearchOption> {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public SearchMenuController() {
        super(SearchOption.class, "Выберите способ поиска транзакции");
    }

    public TransactionFilterDto getTransactionFilter() {
        TransactionFilterDto filter = new TransactionFilterDto();
        while (true) {
            SearchOption option = selectMenu();
            switch (option) {
                case EXIT:
                    return filter;
                case ALL_TRANSACTION:
                    return new TransactionFilterDto();
                case SEARCH_BY_CATEGORY:
                    filter = inputCategory(filter);
                    break;
                case SEARCH_BY_DATES:
                    filter = inputDates(filter);
                    break;
                case SEARCH_BY_AMOUNT:
                    filter = inputAmount(filter);
                    break;
                case SEARCH_BY_COMMENT:
                    filter = inputComment(filter);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + option);
            }
        }
    }

    private TransactionFilterDto inputComment(TransactionFilterDto filter) {
        System.out.print("Введите комментарий или его часть "
                + "(Enter - для поиска всех комментариев): ");
        String comment = scanner.next();
        return filter.withComment(comment);
    }

    private TransactionFilterDto inputAmount(TransactionFilterDto filter) {
        System.out.print("Введите минимальную сумму транзакции "
                + "(Enter - не ограничивать минимальную сумму): ");
        String min = scanner.next();
        System.out.print("Введите максимальную сумму транзакции "
                + "(Enter - не ограничивать максимальную сумму): ");
        String max = scanner.next();
        BigDecimal minAmount = (min == null || min.isBlank())
                ? null : new BigDecimal(min);
        BigDecimal maxAmount = (max == null || max.isBlank())
                ? null : new BigDecimal(max);
        return filter.withAmountFrom(minAmount).withAmountTo(maxAmount);
    }

    private TransactionFilterDto inputDates(TransactionFilterDto filter) {
        System.out.println(
                "Будут найдены транзакции, которые находятся в диапазоне дат и повторяющиеся транзакции, которые выполнятся в указанном диапазоне. Формат даты: ГГГГ-ММ-ДД, например 2024-09-27.");
        System.out.print("Введите начальную дату транзакции "
                + "(Enter - не ограничивать начальную дату): ");
        String start = scanner.next();
        System.out.print("Введите конечную дату транзакции "
                + "(Enter - не ограничивать конечную дату): ");
        String end = scanner.next();
        LocalDate startDate = (start == null || start.isBlank())
                ? null : LocalDate.parse(start, DATE_TIME_FORMATTER);
        LocalDate endDate = (end == null || end.isBlank())
                ? null : LocalDate.parse(end, DATE_TIME_FORMATTER);
        return filter.withStartDate(startDate).withEndDate(endDate);

    }

    private TransactionFilterDto inputCategory(TransactionFilterDto filter) {
        System.out.print("Введите категорию "
                + "(Enter - поиск по всем категориям): ");
        String category = scanner.next();
        return filter.withCategory(category);
    }

}
