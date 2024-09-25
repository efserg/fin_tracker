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

    public RegularTransaction(int transactionId, LocalDateTime date, BigDecimal amount, String category) {
        super(transactionId, date, category, amount);
    }
}
