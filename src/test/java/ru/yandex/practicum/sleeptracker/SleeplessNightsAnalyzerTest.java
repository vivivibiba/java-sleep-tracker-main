package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SleeplessNightsAnalyzerTest {

    @Test
    void shouldReturnZeroWhenEveryNightHasSleep() {
        // Две ночные сессии подряд значит бессонных ночей быть не должно
        SleeplessNightsAnalyzer analyzer = new SleeplessNightsAnalyzer();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 0), LocalDateTime.of(2025, 10, 2, 8, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 23, 0), LocalDateTime.of(2025, 10, 3, 8, 0), SleepQuality.NORMAL)
        );

        assertEquals(0, analyzer.countSleeplessNights(sessions));
    }

    @Test
    void shouldCountSleeplessNightInTheMiddle() {
        // Сон есть в первую и третью ночь а во вторую ночь сна нет
        SleeplessNightsAnalyzer analyzer = new SleeplessNightsAnalyzer();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 0), LocalDateTime.of(2025, 10, 2, 8, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 3, 23, 0), LocalDateTime.of(2025, 10, 4, 8, 0), SleepQuality.NORMAL)
        );

        assertEquals(1, analyzer.countSleeplessNights(sessions));
    }

    @Test
    void shouldReturnZeroWhenThereAreNoNightsInLoggingInterval() {
        // Лог начинается после 12:00 и заканчивается до следующей ночи поэтому ночей для подсчёта нет
        SleeplessNightsAnalyzer analyzer = new SleeplessNightsAnalyzer();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 14, 30), LocalDateTime.of(2025, 10, 1, 15, 20), SleepQuality.NORMAL)
        );

        assertEquals(0, analyzer.countSleeplessNights(sessions));
    }

    @Test
    void shouldWorkAcrossMonthBoundary() {
        // Проверяем что подсчёт работает при переходе между месяцами
        SleeplessNightsAnalyzer analyzer = new SleeplessNightsAnalyzer();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 1, 31, 23, 0), LocalDateTime.of(2025, 2, 1, 8, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 2, 2, 23, 0), LocalDateTime.of(2025, 2, 3, 8, 0), SleepQuality.GOOD)
        );

        assertEquals(1, analyzer.countSleeplessNights(sessions));
    }
}
