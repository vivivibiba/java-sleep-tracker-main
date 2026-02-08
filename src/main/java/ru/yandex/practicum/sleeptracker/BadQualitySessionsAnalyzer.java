package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class BadQualitySessionsAnalyzer implements SleepAnalyzer {
    @Override
    public SleepAnalysisResult<?> apply(List<SleepingSession> sessions) {
        long value = sessions.stream()
                .filter(session -> session.getQuality() == SleepQuality.BAD)
                .count();

        return new SleepAnalysisResult<>("Сессий с плохим качеством сна", value);
    }
}
