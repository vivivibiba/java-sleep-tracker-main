package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class MaxDurationAnalyzer implements SleepAnalyzer<Long> {
    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {
        long value = sessions.stream()
                .mapToLong(SleepingSession::getDurationMinutes)
                .max()
                .orElse(0);

        return new SleepAnalysisResult<>("Максимальная продолжительность сессии, минут", value);
    }
}
