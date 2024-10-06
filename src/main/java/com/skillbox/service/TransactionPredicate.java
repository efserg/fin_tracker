package com.skillbox.service;

import com.skillbox.controller.dto.TransactionFilterDto;
import com.skillbox.data.model.Commentable;
import com.skillbox.data.model.Recurring;
import com.skillbox.data.model.Transaction;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Predicate;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TransactionPredicate {

    /**
     * Создает предикат для фильтрации транзакций по диапазону дат.
     * Также вернет те Recurring транзакции, которые будут или были
     * выполнены в указанный диапазон дат
     *
     * @param startDate Начальная дата диапазона.
     * @param endDate Конечная дата диапазона.
     * @return Предикат для фильтрации транзакций по диапазону дат.
     */
    private Predicate<Transaction> datePredicate(LocalDate startDate, LocalDate endDate) {
        return transaction -> {
            LocalDateTime date = transaction.getDate();
            LocalDateTime start = startDate == null ? null : startDate.atStartOfDay();
            LocalDateTime end = endDate == null ? null : endDate.atStartOfDay();
            return (start == null || !date.isBefore(start)) &&
                    (end == null || !date.isAfter(end))
                    || (transaction instanceof Recurring && ((Recurring) transaction).isExecutedBetween(start, end));
        };
    }

    /**
     * Создает предикат для фильтрации транзакций по комментарию или его части. Фильтруются только транзакции, имплементирующие интерфейс Commentable. Если токен пустой или null, то возвращается предикат, который всегда вернет true
     *
     * @param token часть комментария.
     * @return Предикат для фильтрации транзакций по комментарию.
     */
    private Predicate<Transaction> commentPredicate(String token) {
        return transaction -> (token == null || token.isEmpty())
                || (transaction instanceof Commentable)
                &&  ((Commentable) transaction).getComments().stream().anyMatch(comment -> comment.contains(token));
    }

    /**
     * Создает предикат для фильтрации транзакций по диапазону суммы.
     *
     * @param minAmount Минимальная сумма.
     * @param maxAmount Максимальная сумма.
     * @return Предикат для фильтрации транзакций по диапазону суммы.
     */
    private Predicate<Transaction> amountPredicate(BigDecimal minAmount, BigDecimal maxAmount) {
        return transaction -> {
            BigDecimal amount = transaction.getAmount();
            return (minAmount == null || amount.compareTo(minAmount) >= 0) &&
                    (maxAmount == null || amount.compareTo(maxAmount) <= 0);
        };
    }

    /**
     * Создает предикат для фильтрации транзакций по категории.
     *
     * @param category категория.
     * @return Предикат для фильтрации транзакций по категории.
     */
    private Predicate<Transaction> categoryPredicate(String category) {
        return transaction -> category == null
                || category.isEmpty()
                || category.contains(transaction.getCategory());
    }

    public Predicate<Transaction> predicateByFilter(TransactionFilterDto filter) {
        if (filter == null) {
            return transaction -> true;
        }
        return categoryPredicate(filter.getCategory())
                .and(amountPredicate(filter.getAmountFrom(), filter.getAmountTo()))
                .and(commentPredicate(filter.getComment()))
                .and(datePredicate(filter.getStartDate(), filter.getEndDate()))
                .and(amountPredicate(filter.getAmountFrom(), filter.getAmountTo()));
    }
}
