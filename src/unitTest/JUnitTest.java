package unitTest;

import main.GamePanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

public class JUnitTest {
    GamePanel gp;
    Graphics2D g2;
    @BeforeEach
    void setup(){
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("Test");
        window.setVisible(false);
        gp = new GamePanel(window);
        g2 = gp.g2Debug;
    }
    @Test
    void drawEnvironment(){
        gp.eManager.draw(g2);
        gp.TILESIZE = 48*2;
        gp.eManager.draw(g2);
        gp.TILESIZE = 0;
        gp.eManager.draw(g2);
        gp.TILESIZE = 48;
    }
    @Test
    void updateEnvironment(){
        gp.eManager.updateLighting();
    }
    @Test
    void uiDraw(){
        gp.ui.draw(g2);
    }
}
