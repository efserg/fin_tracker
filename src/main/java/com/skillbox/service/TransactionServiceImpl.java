package com.skillbox.service;

import com.skillbox.console.dto.AggregateOption;
import com.skillbox.console.dto.GroupOption;
import com.skillbox.console.dto.TransactionFilterDto;
import com.skillbox.console.dto.Analytic;
import com.skillbox.data.TransactionReader;
import com.skillbox.data.TransactionReaderImpl;
import com.skillbox.data.model.Transaction;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class TransactionServiceImpl implements TransactionService {

    private final TransactionReader reader;

    public TransactionServiceImpl(String inputFilename) {
        this.reader = new TransactionReaderImpl(inputFilename);
    }

    @Override
    public Analytic calculateAnalytics(TransactionFilterDto transactionFilter,
                                       GroupOption groupOption,
                                       AggregateOption aggregateOption) {
        List<Transaction> transactions = reader.readFile();
        Predicate<Transaction> predicate = TransactionPredicate.predicateByFilter(transactionFilter);
        Function<Transaction, ?> groupFunction = switch (groupOption) {
            case GROUP_BY_MONTHS -> transaction -> transaction.getDate()
                    .getMonth().getValue();
            case GROUP_BY_YEARS -> transaction -> transaction.getDate()
                    .getYear();
            case GROUP_BY_DAY_OF_WEEK -> transaction -> transaction.getDate()
                    .getDayOfWeek().getValue();
            case GROUP_BY_CATEGORY -> Transaction::getCategory;
            case GROUP_BY_INCOME_AND_EXPENSE -> transaction -> transaction.getAmount().compareTo(BigDecimal.ZERO) >= 0;
            default -> Function.identity();
        };
        Collector<Transaction, ?, ?> collector = switch (aggregateOption) {
            case SUM -> Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add);
            case AVERAGE ->
                    Collectors.averagingDouble((Transaction transaction) -> transaction.getAmount().doubleValue());
            case COUNT -> Collectors.counting();
        };
        Map<?, ?> data = transactions
                .stream()
                .filter(predicate)
                .collect(Collectors.groupingBy(groupFunction, collector));
        return new Analytic(LocalDateTime.now(), groupOption.getName(), aggregateOption.getName(), transactionFilter.toString(), data);
    }
}
