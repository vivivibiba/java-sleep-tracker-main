package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BadQualitySessionsAnalyzerTest {

    @Test
    void shouldReturnZeroWhenNoBadSessions() {
        // Создаём список без BAD и ожидаем 0
        BadQualitySessionsAnalyzer analyzer = new BadQualitySessionsAnalyzer();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 22, 0), LocalDateTime.of(2025, 10, 2, 8, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 23, 0), LocalDateTime.of(2025, 10, 3, 8, 0), SleepQuality.NORMAL)
        );

        SleepAnalysisResult<?> result = analyzer.apply(sessions);
        assertEquals(0L, result.getValue());
    }

    @Test
    void shouldCountBadSessions() {
        // Добавляем сессии с BAD и проверяем количество
        BadQualitySessionsAnalyzer analyzer = new BadQualitySessionsAnalyzer();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 22, 0), LocalDateTime.of(2025, 10, 2, 8, 0), SleepQuality.BAD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 14, 0), LocalDateTime.of(2025, 10, 2, 15, 0), SleepQuality.BAD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 23, 0), LocalDateTime.of(2025, 10, 3, 8, 0), SleepQuality.NORMAL)
        );

        SleepAnalysisResult<?> result = analyzer.apply(sessions);
        assertEquals(2L, result.getValue());
    }
}
