package com.skillbox.service;

import com.skillbox.console.dto.AggregateOption;
import com.skillbox.console.dto.GroupOption;
import com.skillbox.console.dto.TransactionFilterDto;
import com.skillbox.console.dto.Analytic;

public interface TransactionService {


    Analytic calculateAnalytics(TransactionFilterDto transactionFilter,
                                GroupOption groupOption,
                                AggregateOption aggregateOption);
}
