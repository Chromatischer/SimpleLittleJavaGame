package main;

public class Logger {
    public static void log(String msg, MESSAGE_PRIO prio){
        if (prio.ordinal() >= Main.DEBUG.ordinal()){
            String PREFIX = "[" + prio.name().toUpperCase() + "]" + " ";
            String colour = ConsoleColors.RESET;
            switch (prio){
                case NORMAL -> colour = ConsoleColors.GREEN;
                case UNEXPECTED -> colour = ConsoleColors.YELLOW;
                case ERROR, FAILED, WARNING -> colour = ConsoleColors.RED;
                case FINE, FINER, FINEST -> colour = ConsoleColors.WHITE;
                case HIGHEST -> colour = ConsoleColors.RED_BOLD_BRIGHT;
                case DEBUG -> colour = ConsoleColors.WHITE_BOLD;
            }
            System.out.println(PREFIX + colour + msg + ConsoleColors.RESET);
        }
    }
    public static void log (String msg){
        String PREFIX = "[" + "NONE" + "]" + " ";
        System.out.println(PREFIX + msg);
        System.out.println(ConsoleColors.RED + "the above line did not have a message priority! Please only use this for debug and remove afterwards!" + ConsoleColors.RESET);
    }
}
