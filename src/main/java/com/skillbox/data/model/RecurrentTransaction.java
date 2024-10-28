package com.skillbox.data.model;

import com.skillbox.exception.WrongTransactionInfoException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RecurrentTransaction extends Transaction implements Recurring {

    RecurrencePattern recurrencePattern;
    int count;

    public RecurrentTransaction(int accountId, int transactionId, LocalDateTime date, String category, BigDecimal amount, List<String> infos) {
        super(accountId, transactionId, date, category, amount);
        if (infos.size() < 2) {
            throw new WrongTransactionInfoException(transactionId);
        }
        this.recurrencePattern = RecurrencePattern.of(infos.get(0));
        this.count = Integer.parseInt(infos.get(1));
    }


    @Override
    public LocalDateTime getNextOccurrence(LocalDateTime dateTime) {
        Duration duration = recurrencePattern.getDuration();
        LocalDateTime nextOccurrence = getDate();
        while (nextOccurrence.isBefore(dateTime) || nextOccurrence.isEqual(dateTime)) {
            nextOccurrence = nextOccurrence.plus(duration);
        }
        return nextOccurrence;
    }

    @Override
    public LocalDateTime getPreviousOccurrence(LocalDateTime dateTime) {
        Duration duration = recurrencePattern.getDuration();
        LocalDateTime previousOccurrence = getDate();
        while (previousOccurrence.isBefore(dateTime)) {
            previousOccurrence = previousOccurrence.plus(duration);
        }
        previousOccurrence = previousOccurrence.minus(duration);
        return previousOccurrence;
    }

    @Override
    public BigDecimal getTransactionAmount(LocalDateTime dateTime) {
        Duration duration = recurrencePattern.getDuration();
        LocalDateTime initialDate = getDate();
        if (dateTime.isBefore(initialDate)) {
            return BigDecimal.ZERO;
        }
        long totalOccurrences = ChronoUnit.SECONDS.between(initialDate, dateTime) / (duration.toSeconds());
        return getAmount().multiply(new BigDecimal(totalOccurrences));
    }

    @Override
    public boolean isExecutedBetween(LocalDateTime startDate, LocalDateTime endDate) {
        LocalDateTime initialDate = getDate();
        Duration duration = recurrencePattern.getDuration();

        // Проверка начальной даты с учетом возможных null значений
        if (startDate != null && initialDate.isAfter(startDate)) {
            return false;
        }
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            return false;
        }

        // Найдем первую дату исполнения после или равной startDate
        LocalDateTime nextOccurrence = initialDate;
        int count = 0;
        while (startDate != null && nextOccurrence.isBefore(startDate)) {
            nextOccurrence = nextOccurrence.plus(duration);
            count++;
        }

        // Проверим, была ли транзакция выполнена между стартовой и конечной датами
        return (endDate == null || !nextOccurrence.isAfter(endDate)) && count < this.count;
    }
}
