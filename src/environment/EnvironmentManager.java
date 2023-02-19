package environment;

import main.GamePanel;
import utilities.Logger;
import utilities.MESSAGE_PRIO;

import java.awt.*;

public class EnvironmentManager {
    static GamePanel gp;
    private ParticleSystems particleSystems;
    Lighting lighting;
    public EnvironmentManager(GamePanel gp){
        EnvironmentManager.gp = gp;
    }
    /**
     * sets up all the necessary things  for the Environment-manager
     */
    public void setup(){
        //lighting = new Lighting(gp, 100);
        lighting = new Lighting(gp, 10000);
        particleSystems = new ParticleSystems(gp);
    }

    /**
     * draws the environment to the Screen e.g: Lighting, particles, etc.
     * @param g2 the Graphics to draw on
     */
    public void draw(Graphics2D g2){
        try {
            lighting.draw(g2);
            particleSystems.drawParticleSystems(g2);
        } catch (NullPointerException e){
            Logger.log("this is a reoccurring error in the EnvironmentManager class! I do not know why! But it works anyways! So ignore this!", MESSAGE_PRIO.ERROR);
            Logger.log("Message: " + e.getMessage(), MESSAGE_PRIO.ERROR);
        }
    }

    /**
     * updates the lighting
     */
    public void updateLighting(){
        lighting.updateLighting(10000);
    }

    /**
     * updates the particle Systems
     */
    public void updateParticleSystems(){
        particleSystems.updateParticleSystems();
    }
    //Math.min(gp.getHeight(), gp.getWidth())-1
}
