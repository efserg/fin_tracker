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

        assert transactionFilter != null : "transactionFilter == null";
        assert groupOption != null : "groupOption == null";
        assert aggregateOption != null : "aggregateOption == null";

        List<Transaction> transactions = transactionRepository.readAll();
        List<Account> accounts = accountRepository.readAll();

        Map<Integer, AccountType> accountTypeMap = accounts.stream()
                .collect(Collectors.toMap(Account::getAccountId, Account::getAccountType));

        Map<Integer, Integer> accountUserIdMap = accounts.stream()
                .collect(Collectors.toMap(Account::getAccountId, Account::getUserId));

        Predicate<Transaction> predicate = transactionFilter.buildPredicate();

        Function<Transaction, ?> groupFunction = switch (groupOption) {
            case GROUP_BY_MONTHS -> transaction -> transaction.getDate()
                    .getMonth().getValue();
            case GROUP_BY_YEARS -> transaction -> transaction.getDate()
                    .getYear();
            case GROUP_BY_DAY_OF_WEEK -> transaction -> transaction.getDate()
                    .getDayOfWeek().getValue();
            case GROUP_BY_CATEGORY -> Transaction::getCategory;
            case GROUP_BY_INCOME_AND_EXPENSE -> transaction -> transaction.getAmount().compareTo(BigDecimal.ZERO) >= 0;
            case GROUP_BY_ACCOUNT_TYPE -> createAccountGroupFunction(accountTypeMap);
            case GROUP_BY_USER_ID -> createUserGroupFunction(accountUserIdMap);
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

        return new Analytic(groupOption, aggregateOption, transactionFilter, data);
    }

    private Function<Transaction, AccountType> createAccountGroupFunction(Map<Integer, AccountType> accountTypeMap) {
        return transaction -> accountTypeMap.get(transaction.getAccountId());
    }

    private Function<Transaction, Integer> createUserGroupFunction(Map<Integer, Integer> accountUserIdMap) {
        return transaction -> accountUserIdMap.get(transaction.getAccountId());
    }
}
