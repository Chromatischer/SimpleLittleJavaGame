package main;

public class Logger {
    public static void log(String msg, MESSAGE_PRIO prio){
        if (prio.ordinal() >= Main.DEBUG.ordinal()){
            String PREFIX = "[" + prio.name().toUpperCase() + "]" + " ";
            System.out.println(PREFIX + msg);
        }
    }
}
