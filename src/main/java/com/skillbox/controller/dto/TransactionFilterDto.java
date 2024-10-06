package com.skillbox.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class TransactionFilterDto {

    String category;
    LocalDate startDate;
    LocalDate endDate;
    BigDecimal amountFrom;
    BigDecimal amountTo;
    String comment;
    Integer accountType;
    Integer userId;

    @Override
    public String toString() {
        return (category == null ? "" : "категория - '" + category + '\'') +
                (startDate == null ? "" : ", начальная дата - " + startDate) +
                (endDate == null ? "" : ", конечная дата - " + endDate) +
                (amountFrom == null ? "" : ", минимальная сумма - " + amountFrom) +
                (amountTo == null ? "" : ", максимальная сумма - " + amountTo) +
                (accountType == null ? "" : ", тип счета - " + accountType) +
                (userId == null ? "" : ", ID пользователя - " + userId) +
                (comment == null ? "" : ", комментарий: '" + comment + '\'');
    }
}
