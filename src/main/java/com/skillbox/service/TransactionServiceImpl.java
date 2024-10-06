package com.skillbox.service;

import com.skillbox.controller.option.AggregateOption;
import com.skillbox.controller.option.GroupOption;
import com.skillbox.controller.dto.TransactionFilterDto;
import com.skillbox.data.model.Analytic;
import com.skillbox.data.model.Account;
import com.skillbox.data.model.AccountType;
import com.skillbox.data.repository.AccountRepository;
import com.skillbox.data.repository.TransactionRepository;
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

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Analytic calculateAnalytics(TransactionFilterDto transactionFilter,
                                       GroupOption groupOption,
                                       AggregateOption aggregateOption) {
        List<Transaction> transactions = transactionRepository.readAll();
        List<Account> accounts = accountRepository.readAll();

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
            case GROUP_BY_ACCOUNT_TYPE -> createAccountGroupFunction(accounts);
            case GROUP_BY_USER_ID -> createUserGroupFunction(accounts);
            default -> Function.identity();
        };

        Collector<Transaction, ?, ?> collector = switch (aggregateOption) {
            case SUM, EXIT -> Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add);
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

    private Function<Transaction, AccountType> createAccountGroupFunction(List<Account> accounts) {
        Map<Integer, AccountType> accountTypeMap = accounts.stream()
                .collect(Collectors.toMap(Account::getAccountId, Account::getAccountType));
        return transaction -> accountTypeMap.get(transaction.getAccountId());
    }
    private Function<Transaction, Integer> createUserGroupFunction(List<Account> accounts) {
        Map<Integer, Integer> accountTypeMap = accounts.stream()
                .collect(Collectors.toMap(Account::getAccountId, Account::getUserId));
        return transaction -> accountTypeMap.get(transaction.getAccountId());
    }
}
