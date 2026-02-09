package ru.yandex.practicum.sleeptracker;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChronotypeAnalyzer implements SleepAnalyzer<Chronotype> {
    // Для хронотипа берём только ночной сон
    // Дневные сессии и бессонные ночи игнорируем

    private static final LocalTime NIGHT_START = LocalTime.MIDNIGHT;
    private static final LocalTime NIGHT_END = LocalTime.of(6, 0);

    @Override
    public SleepAnalysisResult<Chronotype> apply(List<SleepingSession> sessions) {
        Chronotype type = determineChronotype(sessions);
        return new SleepAnalysisResult<>("Хронотип пользователя", type);
    }

    public Chronotype determineChronotype(List<SleepingSession> sessions) {
        // Если данных нет, считаем что пользователь голубь

        if (sessions.isEmpty()) {
            return Chronotype.DOVE;
        }

        SleepingSession firstSession = sessions.get(0);
        SleepingSession lastSession = sessions.get(sessions.size() - 1);

        LocalDate firstStartDate = firstSession.getStart().toLocalDate();
        // По ТЗ первая ночь выбирается так же, как в подсчёте бессонных ночей
        LocalDate startNightDate = firstSession.getStart().toLocalTime().isAfter(LocalTime.NOON)
                ? firstStartDate.plusDays(1)
                : firstStartDate;

        LocalDate endNightDate = lastSession.getEnd().toLocalDate();

        long nightsCount = countNightDates(startNightDate, endNightDate);

        // Для каждой ночи ищем основную сессию, которая пересекает окно 00:00 06:00
        // Если сессии нет, ночь не участвует в хронотипе
        Map<Chronotype, Long> counts = Stream.iterate(startNightDate, date -> date.plusDays(1))
                .limit(nightsCount)
                .map(nightDate -> findMainNightSession(nightDate, sessions)
                        .map(session -> classifyNight(session, nightDate)))
                .flatMap(Optional::stream)
                .collect(Collectors.groupingBy(type -> type, Collectors.counting()));

        return pickFinalType(counts);
    }

    private static long countNightDates(LocalDate startNightDate, LocalDate endNightDate) {
        long diff = ChronoUnit.DAYS.between(startNightDate, endNightDate);
        return Math.max(0, diff + 1);
    }

    private static Optional<SleepingSession> findMainNightSession(LocalDate nightDate, List<SleepingSession> sessions) {
        // Если несколько сессий пересекают ночь, берём ту, у которой больше пересечение с окном ночи

        LocalDateTime nightStart = LocalDateTime.of(nightDate, NIGHT_START);
        LocalDateTime nightEnd = LocalDateTime.of(nightDate, NIGHT_END);

        return sessions.stream()
                .filter(session -> SleeplessNightsAnalyzer.intersects(session.getStart(), session.getEnd(), nightStart, nightEnd))
                .max(Comparator.comparingLong(session -> overlapMinutes(session, nightStart, nightEnd)));
    }

    private static long overlapMinutes(SleepingSession session, LocalDateTime nightStart, LocalDateTime nightEnd) {
        LocalDateTime start = session.getStart().isAfter(nightStart) ? session.getStart() : nightStart;
        LocalDateTime end = session.getEnd().isBefore(nightEnd) ? session.getEnd() : nightEnd;

        return Math.max(0, Duration.between(start, end).toMinutes());
    }

    private static Chronotype classifyNight(SleepingSession session, LocalDate nightDate) {
        // Сравниваем время засыпания и пробуждения с правилами из ТЗ
        // Учитываем что начало сна может быть до полуночи, а конец после полуночи

        long sleepMinutes = minutesSincePreviousMidnight(session.getStart(), nightDate);
        long wakeMinutes = minutesSinceNightMidnight(session.getEnd(), nightDate);

        boolean isOwl = sleepMinutes > 23 * 60L && wakeMinutes > 9 * 60L;
        boolean isLark = sleepMinutes < 22 * 60L && wakeMinutes < 7 * 60L;

        if (isOwl) {
            return Chronotype.OWL;
        }
        if (isLark) {
            return Chronotype.LARK;
        }
        return Chronotype.DOVE;
    }

    private static long minutesSincePreviousMidnight(LocalDateTime dateTime, LocalDate nightDate) {
        long base = dateTime.toLocalTime().toSecondOfDay() / 60L;
        boolean afterMidnight = dateTime.toLocalDate().isEqual(nightDate);
        return afterMidnight ? base + 24 * 60L : base;
    }

    private static long minutesSinceNightMidnight(LocalDateTime dateTime, LocalDate nightDate) {
        long base = dateTime.toLocalTime().toSecondOfDay() / 60L;
        boolean nextDay = dateTime.toLocalDate().isAfter(nightDate);
        return nextDay ? base + 24 * 60L : base;
    }

    private static Chronotype pickFinalType(Map<Chronotype, Long> counts) {
        // Выбираем тип с максимальным количеством ночей
        // Если есть ничья, возвращаем голубя

        long max = counts.values().stream().mapToLong(v -> v).max().orElse(0);

        List<Chronotype> top = counts.entrySet().stream()
                .filter(e -> e.getValue() == max)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return top.size() == 1 ? top.get(0) : Chronotype.DOVE;
    }
}
