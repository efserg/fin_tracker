package com.skillbox.data.model;

import com.skillbox.exception.ExchangeRateNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ForeignCurrencyTransaction extends Transaction {

    BigDecimal exchangeRate;

    public ForeignCurrencyTransaction(int accountId, int transactionId,
                                      LocalDateTime date, String category,
                                      BigDecimal amount, List<String> infos) {
        super(accountId, transactionId, date, category, amount);
        this.exchangeRate = infos.stream().findFirst().map(BigDecimal::new).orElseThrow(() -> new ExchangeRateNotFoundException(transactionId));
    }

    @Override
    public BigDecimal getAmount() {
        return super.getAmount().multiply(exchangeRate);
    }
}
