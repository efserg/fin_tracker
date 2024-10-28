package com.skillbox.data.model;

import com.skillbox.exception.TaxRateNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TaxableTransaction extends Transaction implements Taxable {

    BigDecimal taxRate;

    public TaxableTransaction(int accountId, int transactionId, LocalDateTime date,
                              String category, BigDecimal amount,
                              List<String> infos) {
        super(accountId, transactionId, date, category, amount);
        this.taxRate = infos.stream().findFirst().map(BigDecimal::new).orElseThrow(() -> new TaxRateNotFoundException(transactionId));
    }

    @Override
    public BigDecimal calculateTax() {
        return super.getAmount().multiply(taxRate);
    }

    @Override
    public BigDecimal getAmount() {
        return super.getAmount().subtract(calculateTax());
    }
}
