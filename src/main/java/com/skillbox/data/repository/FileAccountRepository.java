package com.skillbox.data.repository;

import com.skillbox.data.model.Account;
import com.skillbox.exception.AccountReadException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileAccountRepository implements AccountRepository {

    private final String inputFilename;
    public static final String DELIMITER = ",";

    public FileAccountRepository(String inputFilename) {
        this.inputFilename = inputFilename;
    }
    @Override
    public List<Account> readAll() {
        List<Account> accounts = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(DELIMITER);
                int accountId = Integer.parseInt(parts[0]);
                int accountType = Integer.parseInt(parts[1]);
                int userId = Integer.parseInt(parts[2]);
                Account account = new Account(accountId, accountType, userId);
                accounts.add(account);
            }
        } catch (IOException e) {
            throw new AccountReadException(e);
        }

        return accounts;
    }
}
