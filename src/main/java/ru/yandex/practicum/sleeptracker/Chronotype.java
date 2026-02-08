package ru.yandex.practicum.sleeptracker;

public enum Chronotype {
    OWL("Сова"),
    LARK("Жаворонок"),
    DOVE("Голубь");

    private final String title;

    Chronotype(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        // Это влияет только на вывод в консоль
        // В логике и тестах по прежнему используются значения OWL LARK DOVE
        return title;
    }
}
