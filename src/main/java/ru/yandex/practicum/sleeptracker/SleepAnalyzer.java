package ru.yandex.practicum.sleeptracker;

import java.util.List;
import java.util.function.Function;

public interface SleepAnalyzer<T> extends Function<List<SleepingSession>, SleepAnalysisResult<T>> {
}
