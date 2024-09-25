package com.skillbox;


import com.skillbox.console.MainMenuController;
import com.skillbox.data.AnalyticSaver;
import com.skillbox.data.AnalyticSaverImpl;
import com.skillbox.service.TransactionService;
import com.skillbox.service.TransactionServiceImpl;

public class App {

    public static void main(String[] args) {
        String inputFilename = args[0];
        String outputFilename = args[1];
        TransactionService transactionService = new TransactionServiceImpl(inputFilename);
        AnalyticSaver saver = new AnalyticSaverImpl(outputFilename);
        new MainMenuController(transactionService, saver).start();
    }
}
