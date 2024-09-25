package com.skillbox.data;

import com.skillbox.console.dto.Analytic;

public interface AnalyticSaver {

    /**
     * Сохраняет аналитику в файл, добавляя ее к уже существующим записям.
     *
     * @param analytic данные аналитики.
     */
    void save(Analytic analytic);
}
