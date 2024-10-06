package com.skillbox.data.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
public class Account implements AccountInfo, BalanceOperations, AccountStatement {

    private final int accountId;
    private final AccountType accountType;
    private final int userId;
    private final Collection<Transaction> transactions;
    @Setter(AccessLevel.NONE)
    private BigDecimal balance;

    public Account(int accountId, int accountType, int userId) {
        this.accountId = accountId;
        this.accountType = AccountType.of(accountType);
        this.userId = userId;
        this.transactions = new ArrayList<>();
        this.balance = BigDecimal.ZERO;
    }

    @Override
    public List<Transaction> getTransactions() {
        return List.copyOf(transactions);
    }

    @Override
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        balance = balance.add(transaction.getAmount());
    }
}
