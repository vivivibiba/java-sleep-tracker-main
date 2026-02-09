package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TotalSessionsAnalyzerTest {

    @Test
    void shouldReturnZeroWhenThereAreNoSessions() {
        // Проверяем что при пустом списке количество равно 0
        TotalSessionsAnalyzer analyzer = new TotalSessionsAnalyzer();
        SleepAnalysisResult<?> result = analyzer.apply(List.of());

        assertEquals(0, result.getValue());
    }

    @Test
    void shouldCountAllSessions() {
        // Создаём три сессии и проверяем что они все посчитались
        TotalSessionsAnalyzer analyzer = new TotalSessionsAnalyzer();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 22, 15), LocalDateTime.of(2025, 10, 2, 8, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 23, 0), LocalDateTime.of(2025, 10, 3, 8, 0), SleepQuality.NORMAL),
                new SleepingSession(LocalDateTime.of(2025, 10, 3, 14, 30), LocalDateTime.of(2025, 10, 3, 15, 20), SleepQuality.NORMAL)
        );

        SleepAnalysisResult<?> result = analyzer.apply(sessions);
        assertEquals(3, result.getValue());
    }
}
