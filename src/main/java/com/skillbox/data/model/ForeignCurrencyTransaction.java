package com.skillbox.data.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ForeignCurrencyTransaction extends Transaction {

    private final BigDecimal exchangeRate;

    public ForeignCurrencyTransaction(int transactionId, LocalDateTime date, String category, BigDecimal amount,
                                      BigDecimal exchangeRate) {
        super(transactionId, date, category, amount);

        this.exchangeRate = exchangeRate;
    }

    @Override
    public BigDecimal getAmount() {
        return super.getAmount().multiply(exchangeRate);
    }
}
