package utilities;

public enum MESSAGE_PRIO {
    FINEST(999), FINER(-10), FINE(-5), DEBUG(-1), NORMAL(0), UNEXPECTED(1), WARNING(5), FAILED(10), ERROR(99), HIGHEST(999);

    MESSAGE_PRIO(int value) {
    }
    public static boolean contains(String string){
        for (MESSAGE_PRIO m: MESSAGE_PRIO.values()){
            if (m.name().equals(string)){
                return true;
            }
        }
        return false;
    }
}
