package environment;

import main.GamePanel;
import utilities.Logger;
import utilities.MESSAGE_PRIO;

import java.awt.*;

public class EnvironmentManager {
    GamePanel gp;
    private ParticleSystems particleSystems;
    private Lighting lighting;
    public EnvironmentManager(GamePanel gp){
        this.gp = gp;
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
     * <p>
     *     NOTE: there is still an unfixed nullpointer being thrown everytime, the game starts up!
     * </p>
     * @param g2 the Graphics to draw on
     */
    public void draw(Graphics2D g2){
        lighting.draw(g2);
        particleSystems.drawParticleSystems(g2);
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
