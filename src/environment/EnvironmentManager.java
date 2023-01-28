package environment;

import main.GamePanel;
import utilities.Logger;
import utilities.MESSAGE_PRIO;

import java.awt.*;

public class EnvironmentManager {
    static GamePanel gp;
    Lighting lighting;
    public EnvironmentManager(GamePanel gp){
        EnvironmentManager.gp = gp;
    }
    public void setup(){
        lighting = new Lighting(gp, 100);
    }
    public void draw(Graphics2D g2){
        try {
            lighting.draw(g2);
        } catch (NullPointerException e){
            Logger.log("this is a reoccurring error in the EnvironmentManager class! I do not know why! But it works anyways! So ignore this!", MESSAGE_PRIO.ERROR);
            Logger.log("Message: " + e.getMessage(), MESSAGE_PRIO.ERROR);
        }
    }
    public static void updateAll(){
        Lighting.updateLighting(Math.min(gp.getWidth(), gp.getHeight())-gp.TILESIZE-2);
    }
    //Math.min(gp.getHeight(), gp.getWidth())-1
}
