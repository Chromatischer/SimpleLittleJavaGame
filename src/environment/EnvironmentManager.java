package environment;

import main.GamePanel;
import main.Logger;
import main.MESSAGE_PRIO;

import java.awt.*;

public class EnvironmentManager {
    GamePanel gp;
    Lighting lighting;
    public EnvironmentManager(GamePanel gp){
        this.gp = gp;
    }
    public void setup(){
        lighting = new Lighting(gp, 350);
    }
    public void draw(Graphics2D g2){
        try {
            lighting.draw(g2);
        } catch (NullPointerException e){
            Logger.log("this is a reoccurring error in the EnvironmentManager class! I do not know why! But it works anyways! So ignore this!", MESSAGE_PRIO.ERROR);
            Logger.log("Message: " + e.getMessage(), MESSAGE_PRIO.ERROR);
        }
    }
    public void updateAll(){
        lighting.updateLighting(350);
    }
}
