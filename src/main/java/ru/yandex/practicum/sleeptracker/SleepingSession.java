package ru.yandex.practicum.sleeptracker;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class SleepingSession {
    private static final DateTimeFormatter LOG_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    // Начало сессии сна
    private final LocalDateTime start;
    // Окончание сессии сна
    private final LocalDateTime end;
    // Качество сна по оценке часов
    private final SleepQuality quality;

    public SleepingSession(LocalDateTime start, LocalDateTime end, SleepQuality quality) {
        this.start = Objects.requireNonNull(start);
        this.end = Objects.requireNonNull(end);
        this.quality = Objects.requireNonNull(quality);

        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("Окончание сессии должно быть позже начала");
        }
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public SleepQuality getQuality() {
        return quality;
    }

    public long getDurationMinutes() {
        return Duration.between(start, end).toMinutes();
    }

    public static SleepingSession fromLogLine(String line) {
        // Парсим строку формата: дата время начала, дата время конца, качество

        String[] parts = line.split(";");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Некорректная строка лога: " + line);
        }

        LocalDateTime start = LocalDateTime.parse(parts[0].trim(), LOG_FORMAT);
        LocalDateTime end = LocalDateTime.parse(parts[1].trim(), LOG_FORMAT);
        SleepQuality quality = SleepQuality.valueOf(parts[2].trim());

        return new SleepingSession(start, end, quality);
    }
}
