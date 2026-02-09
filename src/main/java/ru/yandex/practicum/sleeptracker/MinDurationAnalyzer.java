package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class MinDurationAnalyzer implements SleepAnalyzer<Long> {
    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {
        long value = sessions.stream()
                .mapToLong(SleepingSession::getDurationMinutes)
                .min()
                .orElse(0);

        return new SleepAnalysisResult<>("Минимальная продолжительность сессии, минут", value);
    }
}
