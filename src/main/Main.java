package main;
import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) throws Exception {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setTitle("2D Game (Loading...)");
        System.out.println("winodw created!");

        GamePanel gamePanel = new GamePanel(window);
        window.add(gamePanel);
        window.pack();
        System.out.println("gamepanel created!");

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.setupGame();
        gamePanel.startGameThread();
        System.out.println("Thread started!");
    }
}
