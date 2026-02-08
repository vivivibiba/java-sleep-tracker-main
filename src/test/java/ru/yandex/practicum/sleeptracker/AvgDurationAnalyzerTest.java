package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AvgDurationAnalyzerTest {

    @Test
    void shouldReturnZeroForEmptyList() {
        // Проверяем что при пустом списке средняя длительность равна 0
        AvgDurationAnalyzer analyzer = new AvgDurationAnalyzer();
        SleepAnalysisResult<?> result = analyzer.apply(List.of());

        assertEquals(0.0, (Double) result.getValue(), 0.0001);
    }

    @Test
    void shouldCalculateAverageDuration() {
        // Создаём две сессии на 60 и 120 минут и проверяем что среднее равно 90
        AvgDurationAnalyzer analyzer = new AvgDurationAnalyzer();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 10, 0), LocalDateTime.of(2025, 10, 1, 11, 0), SleepQuality.NORMAL),
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 12, 0), LocalDateTime.of(2025, 10, 1, 14, 0), SleepQuality.NORMAL)
        );

        SleepAnalysisResult<?> result = analyzer.apply(sessions);
        assertEquals(90.0, (Double) result.getValue(), 0.0001);
    }
}
