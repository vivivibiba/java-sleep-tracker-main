package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class MinDurationAnalyzer implements SleepAnalyzer {
    @Override
    public SleepAnalysisResult<?> apply(List<SleepingSession> sessions) {
        long value = sessions.stream()
                .mapToLong(SleepingSession::getDurationMinutes)
                .min()
                .orElse(0);

        return new SleepAnalysisResult<>("Минимальная продолжительность сессии, минут", value);
    }
}
