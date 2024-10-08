package com.skillbox.data.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public abstract class Transaction  {
    private final int accountId;
    private final int transactionId;
    private final LocalDateTime date;
    private final String category;
    private final BigDecimal amount;

    Transaction(int accountId, int transactionId, LocalDateTime date, String category, BigDecimal amount) {
        this.accountId = accountId;
        this.transactionId = transactionId;
        this.date = date;
        this.category = category;
        this.amount = amount;
    }
}
