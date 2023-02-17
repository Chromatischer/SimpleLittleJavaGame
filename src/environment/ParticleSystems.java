package environment;

import main.GamePanel;
import org.jetbrains.annotations.NotNull;
import utilities.ArrayHelper;
import utilities.Logger;

import java.awt.*;

public class ParticleSystems {
    static ParticleSystem[] particleSystems = new ParticleSystem[100];
    GamePanel gp;
    String lastAliveParticleSystems, aliveParticleSystems;
    public ParticleSystems(GamePanel gp){
        this.gp = gp;
        //particleSystems = new ParticleSystem[100];
    }
    public void drawParticleSystems(Graphics2D g2){
        for (ParticleSystem particleSystem : particleSystems) {
            if (particleSystem != null) {
                particleSystem.draw(g2);
                gp.player.ui.drawRect(0,0, particleSystem.getWidth(), particleSystem.getHeight(), particleSystem.getX(), particleSystem.getY(), Color.RED);
                gp.player.ui.updateDebugString(particleSystem.getOldDebugString(), particleSystem.getDebugString());
                lastAliveParticleSystems = aliveParticleSystems;
                aliveParticleSystems = "Alive Particle-Systems: " + ArrayHelper.lengthNonNull(particleSystems) + "/" + particleSystems.length;
                gp.player.ui.updateDebugString(lastAliveParticleSystems, aliveParticleSystems);
            }
        }
    }
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
    public static void updateParticleSystems(){
        for (int i = 0; i < particleSystems.length; i++){
            if (particleSystems[i] != null) {
                particleSystems[i].update();
                if (particleSystems[i].isDead()){
                    particleSystems[i] = null;
                }
            }
        }
    }
    public void killParticleSystem(int position){
        Logger.log("killing particle System");
        particleSystems[position] = null;
    }
}
