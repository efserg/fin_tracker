package com.skillbox.data.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.skillbox.data.model.Analytic;
import com.skillbox.exception.AnalyticWriteException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class JsonAnalyticRepository implements AnalyticRepository {

    private final String outputFilename;
    private final ObjectMapper objectMapper;

    public JsonAnalyticRepository(String outputFilename) {
        this.outputFilename = outputFilename;
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void save(Analytic analytic) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilename, true))) {
            String analyticJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(analytic);
            writer.write(analyticJson);
        } catch (IOException e) {
            throw new AnalyticWriteException(e);
        }
    }
}
