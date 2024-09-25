package com.skillbox.data.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TaxableTransaction extends Transaction implements Taxable {

    BigDecimal taxRate;

    public TaxableTransaction(int transactionId, LocalDateTime date,
                              String category, BigDecimal amount,
                              BigDecimal taxRate) {
        super(transactionId, date, category, amount);
        this.taxRate = taxRate;
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
