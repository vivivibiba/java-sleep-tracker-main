package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MinDurationAnalyzerTest {

    @Test
    void shouldReturnZeroForEmptyList() {
        // Проверяем что при пустом списке минимум равен 0
        MinDurationAnalyzer analyzer = new MinDurationAnalyzer();
        SleepAnalysisResult<?> result = analyzer.apply(List.of());

        assertEquals(0L, result.getValue());
    }

    @Test
    void shouldFindMinimalDuration() {
        // Создаём две сессии и проверяем минимальную длительность
        MinDurationAnalyzer analyzer = new MinDurationAnalyzer();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 14, 30), LocalDateTime.of(2025, 10, 1, 15, 30), SleepQuality.NORMAL),
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 22, 0), LocalDateTime.of(2025, 10, 2, 8, 0), SleepQuality.GOOD)
        );

        SleepAnalysisResult<?> result = analyzer.apply(sessions);
        assertEquals(60L, result.getValue());
    }
}
