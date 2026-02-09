package ru.yandex.practicum.sleeptracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;

public class SleepTrackerApp {
    // Список анализаторов. Каждый анализатор считает одну метрику и возвращает результат
    private final List<SleepAnalyzer<?>> analyzers;


    public SleepTrackerApp() {
        // Собираем все функции анализа в одном месте
        // Чтобы добавить новую метрику, достаточно создать новый класс анализатора и добавить его сюда
        analyzers = List.of(
                new TotalSessionsAnalyzer(),
                new MinDurationAnalyzer(),
                new MaxDurationAnalyzer(),
                new AvgDurationAnalyzer(),
                new BadQualitySessionsAnalyzer(),
                new SleeplessNightsAnalyzer(),
                new ChronotypeAnalyzer()
        );
    }

    public static void main(String[] args) {
        new SleepTrackerApp().run(args);
    }

    public void run(String[] args) {
        // Проверяем аргументы командной строки
        // Ожидаем ровно один аргумент. Это путь к файлу с логом сна

        if (args.length != 1) {
            System.out.println("Укажи путь к файлу с логом сна");
            System.out.println("Пример: java SleepTrackerApp /path/to/sleep_log.txt");
            return;
        }

        // Читаем файл и получаем список сессий сна
        Path logPath = Paths.get(args[0]);
        List<SleepingSession> sessions = readSessions(logPath);

        // Запускаем все анализаторы по очереди и печатаем результат
        analyzers.stream()
                .map(analyzer -> analyzer.apply(sessions))
                .forEach(result -> System.out.println(result));
    }

    private List<SleepingSession> readSessions(Path logPath) {
        try {
            // Читаем файл построчно
            // Пустые строки игнорируем
            // Каждую строку превращаем в объект сессии
            return Files.lines(logPath)
                    .map(String::trim)
                    .filter(line -> !line.isBlank())
                    .map(SleepingSession::fromLogLine)
                    .sorted(Comparator.comparing(SleepingSession::getStart))
                    .toList();
        } catch (IOException e) {
            throw new IllegalStateException("Не удалось прочитать файл: " + logPath, e);
        }
    }
}
