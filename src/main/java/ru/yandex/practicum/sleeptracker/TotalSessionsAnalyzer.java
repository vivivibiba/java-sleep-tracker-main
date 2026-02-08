package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class TotalSessionsAnalyzer implements SleepAnalyzer {
    @Override
    public SleepAnalysisResult<?> apply(List<SleepingSession> sessions) {
        return new SleepAnalysisResult<>("Всего сессий сна", sessions.size());
    }
}
