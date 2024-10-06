package com.skillbox.data.repository;

import com.skillbox.data.model.Analytic;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileAnalyticRepository implements AnalyticRepository {

    private final String outputFilename;

    public FileAnalyticRepository(String outputFilename) {
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
