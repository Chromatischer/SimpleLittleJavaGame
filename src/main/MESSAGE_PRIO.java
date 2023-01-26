package main;

public enum MESSAGE_PRIO {
    FINEST(-999), FINER(-99), FINE(-5), DEBUG(-1), NORMAL(0), UNEXPECTED(1), WARNING(5), FAILED(10), ERROR(99), HIGHEST(999);

    MESSAGE_PRIO(int i) {
    }
}
