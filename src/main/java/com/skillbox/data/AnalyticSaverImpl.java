package com.skillbox.data;

import com.skillbox.console.dto.Analytic;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class AnalyticSaverImpl implements AnalyticSaver {

    private final String outputFilename;

    public AnalyticSaverImpl(String outputFilename) {
        this.outputFilename = outputFilename;
    }

    @Override
    public void save(Analytic analytic) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilename, true))) {
            writer.write(analytic.toString());
            writer.newLine();
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка записи аналитики в файл", e);
        }
    }
}
