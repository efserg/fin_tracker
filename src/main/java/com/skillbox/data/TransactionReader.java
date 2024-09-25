package com.skillbox.data;

import com.skillbox.data.model.Transaction;
import java.util.List;

public interface TransactionReader {

    List<Transaction> readFile();
}
