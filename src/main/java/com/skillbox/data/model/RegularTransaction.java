package com.skillbox.data.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RegularTransaction extends Transaction {

    public RegularTransaction(int accountId, int transactionId,
                              LocalDateTime date, BigDecimal amount,
                              String category) {
        super(accountId, transactionId, date, category, amount);
    }
}
