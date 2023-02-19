package environment;

import main.GamePanel;
import main.Main;
import org.jetbrains.annotations.NotNull;
import utilities.ArrayHelper;
import utilities.Logger;
import utilities.MESSAGE_PRIO;

import java.awt.*;

public class ParticleSystems {
    /**
     * a Array containing all the Particle Systems (at max 100)
     */
    static ParticleSystem[] particleSystems = new ParticleSystem[100];
    GamePanel gp;
    String lastAliveParticleSystems, aliveParticleSystems;
    private int oldX = 0, oldY = 0;
    public ParticleSystems(GamePanel gp){
        this.gp = gp;
    }

    /**
     * draws all the Particle-Systems to the Screen
     * @param g2 the Graphics to draw on
     */
    public void drawParticleSystems(Graphics2D g2){
        for (ParticleSystem particleSystem : particleSystems) {
            if (particleSystem != null) {
                particleSystem.draw(g2, gp); // draws the particle System
                if (Main.DEBUG.ordinal() <= MESSAGE_PRIO.DEBUG.ordinal()) { // only executes when debug mode is enabled!
                    gp.player.ui.eraseRect(0, 0, particleSystem.getWidth(), particleSystem.getHeight(), oldX, oldY, Color.RED); // removes the old rectangle first
                    // converting map-based positions into screen-spaced positions
                    int screenX = particleSystem.getX() * gp.TILESIZE - gp.player.worldX + gp.player.screenX;
                    int screenY = particleSystem.getY() * gp.TILESIZE - gp.player.worldY + gp.player.screenY;
                    gp.player.ui.drawRect(0, 0, particleSystem.getWidth(), particleSystem.getHeight(), screenX, screenY, Color.RED); // draws the new bounding box
                    // sets the variables for the removal of the particle System in the nex go
                    oldX = screenX;
                    oldY = screenY;
                    gp.player.ui.updateDebugString(particleSystem.getOldDebugString(), particleSystem.getDebugString()); // updates the Debug String on the ui
                    lastAliveParticleSystems = aliveParticleSystems;
                    aliveParticleSystems = "Alive Particle-Systems: " + ArrayHelper.lengthNonNull(particleSystems) + "/" + particleSystems.length;
                    gp.player.ui.updateDebugString(lastAliveParticleSystems, aliveParticleSystems);
                }
            }
        }
    }

    /**
     * adds the given Particle-System to the renderer
     * @Example new ParticleSystem(100, true, 100, true, "file location as String", 1, (worldX + 2 * gp.TILESIZE) / gp.TILESIZE, (worldY + 2 * gp.TILESIZE) / gp.TILESIZE, 5*48, 5*48, 40, true, 1.6, true, 1) <- this is an inflowing Particle System wich spawns just infront of the player
     * @Example new ParticleSystem(200, true, 800, true, "file location as String", 1, worldX / gp.TILESIZE, worldY / gp.TILESIZE, 1 * gp.TILESIZE, 1 * gp.TILESIZE, 10, false, 4, false, 1);  <- this could be death particles. Quite small and lie around for quite some time, small spread
     * @param particleSystem the particle System to add
     * @see ParticleSystem
     */
    public static void addParticleSystem(@NotNull ParticleSystem particleSystem){
        Logger.log("adding particle System!");
        for (int i = 0; i < particleSystems.length; i++) {
            if (particleSystems[i] == null){
                particleSystems[i] = particleSystem;
                Logger.log("particle System added! At location: " + particleSystem.getX() + ":" + particleSystem.getY());
                break;
            }
        }
    }

    /**
     * updates all the particle systems wich are currently active
     */
    public void updateParticleSystems(){
        for (ParticleSystem particleSystem : particleSystems){
            if (particleSystem != null) {
                particleSystem.update();
                if (particleSystem.isDead()){ // checks if the Particle-System is Dead and killing it, if that is the case
                    killParticleSystem(particleSystem);
                }
            }
        }
    }

    /**
     * removes a particle System from the list
     * @param position the array-position of the Particle-System to remove
     */
    public void killParticleSystem(int position){
        Logger.log("killing particle System", MESSAGE_PRIO.DEBUG);
        gp.player.ui.eraseRect(0,0,particleSystems[position].getWidth(), particleSystems[position].getHeight(), oldX, oldY, Color.RED); // additionally removes the bounding box rendered on screen
        particleSystems[position] = null;
    }
    /**
     * removes a particle System from the list
     * @param particleSystem the particle system to remove
     */
    public void killParticleSystem(ParticleSystem particleSystem){
        for (int i = 0; i < particleSystems.length; i++) {
            if (particleSystems[i] == particleSystem){
                particleSystems[i] = null;
            }
        }
    }
}
