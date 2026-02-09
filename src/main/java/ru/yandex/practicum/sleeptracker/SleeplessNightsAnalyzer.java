package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Stream;

public class SleeplessNightsAnalyzer implements SleepAnalyzer<Long> {
    // Ночь считаем как интервал с 00:00 до 06:00
    // Если ни одна сессия не пересекает этот интервал, ночь считается бессонной

    private static final LocalTime NIGHT_START = LocalTime.MIDNIGHT;
    private static final LocalTime NIGHT_END = LocalTime.of(6, 0);

    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {
        long value = countSleeplessNights(sessions);
        return new SleepAnalysisResult<>("Бессонных ночей", value);
    }

    public long countSleeplessNights(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return 0;
        }

        SleepingSession firstSession = sessions.get(0);
        SleepingSession lastSession = sessions.get(sessions.size() - 1);

        LocalDate firstStartDate = firstSession.getStart().toLocalDate();
        // По ТЗ ночь выбираем так
        // Если первая сессия началась после 12:00, берём следующую ночь
        // Если до 12:00, берём ночь этого же дня
        LocalDate startNightDate = firstSession.getStart().toLocalTime().isAfter(LocalTime.NOON)
                ? firstStartDate.plusDays(1)
                : firstStartDate;

        LocalDate endNightDate = lastSession.getEnd().toLocalDate();

        // Считаем сколько ночей попадает в интервал логирования
        long nightsCount = countNightDates(startNightDate, endNightDate);

        // Идём по всем датам ночей и считаем, какие из них бессонные
        return Stream.iterate(startNightDate, date -> date.plusDays(1))
                .limit(nightsCount)
                .filter(nightDate -> isSleeplessNight(nightDate, sessions))
                .count();
    }

    private static long countNightDates(LocalDate startNightDate, LocalDate endNightDate) {
        long diff = ChronoUnit.DAYS.between(startNightDate, endNightDate);
        return Math.max(0, diff + 1);
    }

    private static boolean isSleeplessNight(LocalDate nightDate, List<SleepingSession> sessions) {
        LocalDateTime nightStart = LocalDateTime.of(nightDate, NIGHT_START);
        LocalDateTime nightEnd = LocalDateTime.of(nightDate, NIGHT_END);

        // Проверяем есть ли хотя бы одна сессия, которая пересекает окно ночи
        boolean hasNightSleep = sessions.stream()
                .anyMatch(session -> intersects(session.getStart(), session.getEnd(), nightStart, nightEnd));

        return !hasNightSleep;
    }

    static boolean intersects(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        // Пересечение двух интервалов времени

        return start1.isBefore(end2) && end1.isAfter(start2);
    }
}
