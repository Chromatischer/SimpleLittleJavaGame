package environment;

import gameExceptions.GameException;
import gameExceptions.InvalidParameterException;
import main.GamePanel;
import managers.ImageManager;
import utilities.Logger;
import utilities.Random;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ParticleSystem{
    /**
     * max lifespan in ms
     */
    public static final int MAX_LIFE_SPAN = 10_000;
    int amount;
    String imageFileMap;
    Particle[] particles;
    int spread;
    public ParticleSystem(int amount, int lifespan, boolean randomAmount, boolean randomLifespan, String imageFileMap, int images, int spread, int x, int y, int width, int height, int particleSize, boolean randomParticleSize) throws GameException {
        Logger.log("creating new particle System! with values: ");
        Logger.log("amount: " + amount + " lifespan: " + lifespan + " randomAmount? " + randomAmount + " randomLifespan? " + randomLifespan + " image location: '" + imageFileMap + "' image count: " + images + " spread: " + spread + " x location: " + x + " y location: " + y + " width: " + width + " height: " + height + " particle Size: " + particleSize + " random particle Size? " + randomParticleSize);
        if (amount > 255 || amount < 0){
            throw new InvalidParameterException("amount out of bounds expected: 0-255 for value: " + amount);
        }
        if (lifespan < 0 || lifespan > MAX_LIFE_SPAN){
            throw new InvalidParameterException("lifespan out of bounds expected: 0-" + MAX_LIFE_SPAN);
        }
        if (imageFileMap == null){
            throw new InvalidParameterException("imageMap cant be null");
        }
        if (images < 0){
            throw new InvalidParameterException("images value out of bounds expected: images > 0");
        }
        if (spread < 0){
            throw new InvalidParameterException("spread value out of bounds expected: spread > 0");
        }
        if (x < 0){
            throw new InvalidParameterException("x value out of bounds expected: x > 0");
        }
        if (y < 0){
            throw new InvalidParameterException("y value out of bounds expected: y > 0");
        }
        if (width < 0){
            throw new InvalidParameterException("width value out of bounds expected: width > 0");
        }
        if (height < 0){
            throw new InvalidParameterException("height value out of bounds expected: height > 0");
        }
        if (particleSize < 0){
            throw new InvalidParameterException("particleSize value out of bounds expected: particleSize > 0");
        }

        if (randomAmount){
            this.amount = amount + Random.randomAddSubtract(amount, 20, 0, 255);
        } else {
            this.amount = amount;
        }
        this.spread = spread;
        this.imageFileMap = imageFileMap;
        particles = new Particle[amount];
        int current = 0;
        for (int i = 0; i < amount; i++) {
            int particleX = x + Random.randomAddSubtract(x, width, 0, width);
            int particleY = y + Random.randomAddSubtract(y, height, 0, height);
            int singleParticleSize = particleSize + Random.randomAddSubtract(particleSize, 5, 0, Math.max(width, height));
            int particleLifespan;
            if (randomLifespan){
                particleLifespan = lifespan + Random.randomAddSubtract(lifespan, 20, 0, MAX_LIFE_SPAN);
            } else {
                particleLifespan = lifespan;
            }

            if (current >= images){
                current = 0;
            }
            BufferedImage particleImage = ImageManager.getTile(0, 16 * current, 16, 16, 16, 16 * images, imageFileMap);

            particles[i] = new Particle(particleImage, singleParticleSize, particleX, particleY, 0, particleLifespan);
            current ++;
        }
    }
    void draw(Graphics2D g2){
        for(Particle particle : particles){
            if (particle != null) {
                particle.draw(g2);
            }
        }
    }
    void update(){
        for (Particle particle : particles){
            particle.rotation = particle.rotation + Random.randomAddSubtract(particle.rotation, 3, 0, 359);
            particle.size = particle.size + Random.randomAddSubtract(particle.size, 1, 0, 200);
            particle.x = particle.x + 1;
            particle.y = particle.y + 1;
            particle.lifespan --;
            if (particle.lifespan <= 0){
                particle = null;
            }
        }
    }
    public boolean isDead(){
        for (Particle particle : particles){
            if (particle != null){
                return false;
            }
        }
        return true;
    }
}
