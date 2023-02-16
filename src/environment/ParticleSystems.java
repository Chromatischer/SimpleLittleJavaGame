package environment;

import main.GamePanel;
import org.jetbrains.annotations.NotNull;
import utilities.Logger;

import java.awt.*;

public class ParticleSystems {
    static ParticleSystem[] particleSystems = new ParticleSystem[100];
    GamePanel gp;
    public ParticleSystems(GamePanel gp){
        this.gp = gp;
        //particleSystems = new ParticleSystem[100];
    }
    public void drawParticleSystems(Graphics2D g2){
        for (ParticleSystem particleSystem : particleSystems) {
            if (particleSystem != null) {
                particleSystem.draw(g2);
            }
        }
    }
    public static void addParticleSystem(@NotNull ParticleSystem particleSystem){
        Logger.log("adding particle System!");
        for (int i = 0; i < particleSystems.length; i++) {
            if (particleSystems[i] == null){
                particleSystems[i] = particleSystem;
                Logger.log("particle System added!");
                break;
            }
        }
    }
    public static void updateParticleSystems(){
        for (ParticleSystem particleSystem : particleSystems){
            if (particleSystem != null) {
                particleSystem.update();
                if (particleSystem.isDead()){
                    particleSystem = null;
                }
            }
        }
    }
    public void killParticleSystem(int position){
        Logger.log("killing particle System");
        particleSystems[position] = null;
    }
}
