package main;
import javax.swing.JFrame;

public class Main {
    public static final MESSAGE_PRIO DEBUG = MESSAGE_PRIO.DEBUG;
    public static void main(String[] args) throws Exception {
        Logger.log("logging level: " + DEBUG.name());
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
        Logger.log("setting up game!", MESSAGE_PRIO.DEBUG);
        gamePanel.setupGame();
        gamePanel.startGameThread();
        Logger.log("Thread started!", MESSAGE_PRIO.NORMAL);
    }
}
