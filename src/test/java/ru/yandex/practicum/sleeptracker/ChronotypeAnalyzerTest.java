package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChronotypeAnalyzerTest {

    @Test
    void shouldPickOwlWhenItAppearsMoreOften() {
        // Делаем три ночи совы и одну ночь жаворонка и ожидаем что итог будет сова
        ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 30), LocalDateTime.of(2025, 10, 2, 10, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 23, 30), LocalDateTime.of(2025, 10, 3, 10, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 3, 23, 30), LocalDateTime.of(2025, 10, 4, 10, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 4, 21, 0), LocalDateTime.of(2025, 10, 5, 6, 0), SleepQuality.GOOD)
        );

        assertEquals(Chronotype.OWL, analyzer.determineChronotype(sessions));
    }

    @Test
    void shouldReturnDoveWhenThereIsATie() {
        // Делаем одну ночь совы и одну ночь жаворонка и при равенстве ожидаем голубя
        ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 30), LocalDateTime.of(2025, 10, 2, 10, 0), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 21, 0), LocalDateTime.of(2025, 10, 3, 6, 30), SleepQuality.GOOD)
        );

        assertEquals(Chronotype.DOVE, analyzer.determineChronotype(sessions));
    }

    @Test
    void shouldTreatAfterMidnightStartAsLateBedTime() {
        // Сон начинается в 01:00 и заканчивается в 10:00 и это подходит под сову
        ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 1, 0), LocalDateTime.of(2025, 10, 2, 10, 0), SleepQuality.GOOD)
        );

        assertEquals(Chronotype.OWL, analyzer.determineChronotype(sessions));
    }

    @Test
    void shouldIgnoreDaySessionsAndReturnDoveIfNoNightSleep() {
        // Есть только дневной сон и ночей нет поэтому ожидаем голубя
        ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.of(2025, 10, 1, 14, 0), LocalDateTime.of(2025, 10, 1, 15, 0), SleepQuality.NORMAL),
                new SleepingSession(LocalDateTime.of(2025, 10, 2, 14, 0), LocalDateTime.of(2025, 10, 2, 15, 0), SleepQuality.NORMAL)
        );

        assertEquals(Chronotype.DOVE, analyzer.determineChronotype(sessions));
    }
}
