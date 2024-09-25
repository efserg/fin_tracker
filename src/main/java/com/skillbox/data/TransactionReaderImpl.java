package com.skillbox.data;

import com.skillbox.data.model.CommentableTransaction;
import com.skillbox.data.model.ForeignCurrencyTransaction;
import com.skillbox.data.model.RecurrencePattern;
import com.skillbox.data.model.RecurrentTransaction;
import com.skillbox.data.model.RegularTransaction;
import com.skillbox.data.model.TaxableTransaction;
import com.skillbox.data.model.Transaction;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TransactionReaderImpl implements TransactionReader {

    private final String inputFilename;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    public static final String DELIMITER = ",";
    public static final String INFO_DELIMITER = ";";

    public TransactionReaderImpl(String inputFilename) {
        this.inputFilename = inputFilename;
    }

    @Override
    public List<Transaction> readFile() {
        List<Transaction> transactions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(DELIMITER, 6);
                int transactionId = Integer.parseInt(parts[0]);
                LocalDateTime date = LocalDateTime.parse(parts[1], DATE_TIME_FORMATTER);
                String category = parts[2];
                BigDecimal amount = new BigDecimal(parts[3]);
                String type = parts[4];
                String transactionInfo = parts[5];

                Transaction transaction = createTransaction(transactionId, date, category, amount, type,
                        transactionInfo);
                transactions.add(transaction);
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения файла", e);
        }

        return transactions;
    }

    private Transaction createTransaction(int transactionId, LocalDateTime date,
                                          String category, BigDecimal amount,
                                          String type, String transactionInfo) {
        return switch (type) {
            case "Regular" -> new RegularTransaction(transactionId, date, amount, category);
            case "Taxable" -> {
                BigDecimal taxRate = getTaxRate(transactionInfo);
                yield new TaxableTransaction(transactionId, date, category, amount, taxRate);
            }
            case "Recurrent" -> {
                RecurrencePattern recurrencePattern = getRecurrencePattern(transactionInfo);
                yield new RecurrentTransaction(transactionId, date, category, amount, recurrencePattern);
            }
            case "ForeignCurrency" -> {
                BigDecimal exchangeRate = getExchangeRate(transactionInfo);
                yield new ForeignCurrencyTransaction(transactionId, date, category, amount, exchangeRate);
            }
            case "Commentable" -> {
                List<String> comments = getComments(transactionInfo);
                yield new CommentableTransaction(transactionId, date, category, amount, comments);
            }
            default -> throw new IllegalArgumentException("Unknown transaction type: " + type);
        };
    }

    private List<String> getComments(String transactionInfo) {
        return List.of(transactionInfo.split(INFO_DELIMITER));
    }

    private RecurrencePattern getRecurrencePattern(String transactionInfo) {
        return RecurrencePattern.of(transactionInfo);
    }

    private BigDecimal getExchangeRate(String transactionInfo) {
        return new BigDecimal(transactionInfo);
    }

    private BigDecimal getTaxRate(String transactionInfo) {
        return new BigDecimal(transactionInfo);
    }
}
