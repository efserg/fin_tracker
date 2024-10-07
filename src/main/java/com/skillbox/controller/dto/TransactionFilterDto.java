package com.skillbox.controller.dto;

import com.skillbox.data.model.Commentable;
import com.skillbox.data.model.Recurring;
import com.skillbox.data.model.Transaction;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

/**
 * Класс для хранения фильтра по транзакциям.
 */
@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class TransactionFilterDto {

    String category;
    LocalDate startDate;
    LocalDate endDate;
    BigDecimal minAmount;
    BigDecimal maxAmount;
    String comment;

    @Override
    public String toString() {
        return (category == null ? "" : "категория - '" + category + '\'') +
                (startDate == null ? "" : ", начальная дата - " + startDate) +
                (endDate == null ? "" : ", конечная дата - " + endDate) +
                (minAmount == null ? "" : ", минимальная сумма - " + minAmount) +
                (maxAmount == null ? "" : ", максимальная сумма - " + maxAmount) +
                (comment == null ? "" : ", комментарий: '" + comment + '\'');
    }

    /**
     * Создает предикат для фильтрации транзакций по диапазону дат.
     * Также вернет те Recurring транзакции, которые будут или были
     * выполнены в указанный диапазон дат
     *
     * @return Предикат для фильтрации транзакций по диапазону дат.
     */
    private Predicate<Transaction> datePredicate() {
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
     * @return Предикат для фильтрации транзакций по комментарию.
     */
    private Predicate<Transaction> commentPredicate() {
        return transaction -> (comment == null || comment.isEmpty())
                || (transaction instanceof Commentable)
                &&  ((Commentable) transaction).getComments().stream().anyMatch(c -> c.contains(comment));
    }

    /**
     * Создает предикат для фильтрации транзакций по диапазону суммы.
     *
     * @return Предикат для фильтрации транзакций по диапазону суммы.
     */
    private Predicate<Transaction> amountPredicate() {
        return transaction -> {
            BigDecimal amount = transaction.getAmount();
            return (minAmount == null || amount.compareTo(minAmount) >= 0) &&
                    (maxAmount == null || amount.compareTo(maxAmount) <= 0);
        };
    }

    /**
     * Создает предикат для фильтрации транзакций по категории.
     *
     * @return Предикат для фильтрации транзакций по категории.
     */
    private Predicate<Transaction> categoryPredicate() {
        return transaction -> category == null
                || category.isEmpty()
                || category.contains(transaction.getCategory());
    }

    /**
     * Собирает предикат для фильтрации транзакции.
     *
     * @return Предикат для фильтрации транзакции.
     */
    public Predicate<Transaction> buildPredicate() {
        return categoryPredicate()
                .and(amountPredicate())
                .and(commentPredicate())
                .and(datePredicate())
                .and(amountPredicate());
    }
}
