package main;
import utilities.Logger;
import utilities.MESSAGE_PRIO;

import javax.swing.JFrame;

public class Main {
    public static final MESSAGE_PRIO DEBUG = MESSAGE_PRIO.NORMAL;
    public static void main(String[] args) throws Exception {
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
        gamePanel.setupGame();
        gamePanel.startGameThread();
        Logger.log("Thread started, Game Running!", MESSAGE_PRIO.NORMAL);
    }
}
