package com.skillbox.data.repository;

import com.skillbox.data.model.CommentableTransaction;
import com.skillbox.data.model.ForeignCurrencyTransaction;
import com.skillbox.data.model.RecurrentTransaction;
import com.skillbox.data.model.RegularTransaction;
import com.skillbox.data.model.TaxableTransaction;
import com.skillbox.data.model.Transaction;
import com.skillbox.exception.TransactionReadException;
import com.skillbox.exception.UnknownTransactionTypeException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileTransactionRepository implements TransactionRepository {

    private final String inputFilename;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    public static final String DELIMITER = ",";
    public static final String INFO_DELIMITER = ";";

    public FileTransactionRepository(String inputFilename) {
        this.inputFilename = inputFilename;
    }

    @Override
    public List<Transaction> readAll() {
        List<Transaction> transactions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Transaction transaction = createTransaction(line);
                transactions.add(transaction);
            }
        } catch (IOException e) {
            throw new TransactionReadException(e);
        }

        return transactions;
    }

    private Transaction createTransaction(String line) {
        String[] parts = line.split(DELIMITER, 7);
        int accountId = Integer.parseInt(parts[0]);
        int transactionId = Integer.parseInt(parts[1]);
        LocalDateTime date = LocalDateTime.parse(parts[2], DATE_TIME_FORMATTER);
        String category = parts[3];
        BigDecimal amount = new BigDecimal(parts[4]);
        String type = parts[5];
        List<String> transactionInfos = List.of(parts[6].split(INFO_DELIMITER));
        return switch (type) {
            case "Regular" -> new RegularTransaction(accountId, transactionId, date, amount, category);
            case "Taxable" -> new TaxableTransaction(accountId, transactionId, date, category, amount, transactionInfos);
            case "Recurrent" ->
                    new RecurrentTransaction(accountId, transactionId, date, category, amount, transactionInfos);
            case "ForeignCurrency" ->
                    new ForeignCurrencyTransaction(accountId, transactionId, date, category, amount, transactionInfos);
            case "Commentable" ->
                    new CommentableTransaction(accountId, transactionId, date, category, amount, transactionInfos);
            default -> throw new UnknownTransactionTypeException(type);
        };
    }

}
