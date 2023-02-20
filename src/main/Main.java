package main;

import utilities.Logger;
import utilities.MESSAGE_PRIO;

import javax.swing.*;

public class Main {
    public static MESSAGE_PRIO DEBUG = MESSAGE_PRIO.DEBUG;
    public static void main(String[] args) throws Exception {
        if (args.length >= 1) {
            if (! MESSAGE_PRIO.contains(args[0])) {
                Logger.log("usage: <message-priority to use>", MESSAGE_PRIO.HIGHEST);
                System.exit(- 1);
            } else {
                DEBUG = MESSAGE_PRIO.valueOf(args[0]);
            }
        } else {
            Logger.log("usage: <message-priority to use>", MESSAGE_PRIO.HIGHEST);
            System.exit(-1);
        }

        Logger.log("logging level: " + DEBUG.name(), MESSAGE_PRIO.NORMAL);
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setTitle("2D Game (Loading...)");
        Logger.log("setting up window: DONE", MESSAGE_PRIO.NORMAL);

        Logger.log("creating game-panel!", MESSAGE_PRIO.DEBUG);
        GamePanel gamePanel = new GamePanel(window);
        window.add(gamePanel);
        window.pack();
        Logger.log("creating game-panel: DONE", MESSAGE_PRIO.NORMAL);

        window.setLocationRelativeTo(null);
        window.setVisible(true);
        while (! gamePanel.IS_READY) {
            gamePanel.setupGame();
        }
        gamePanel.startGameThread();
        Logger.log("Thread started, Game Running!", MESSAGE_PRIO.NORMAL);
    }
}
