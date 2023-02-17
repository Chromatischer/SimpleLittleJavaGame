package environment;

import gameExceptions.GameException;
import gameExceptions.InvalidParameterException;
import main.GamePanel;
import managers.ImageManager;
import utilities.ArrayHelper;
import utilities.Logger;
import utilities.MESSAGE_PRIO;
import utilities.Random;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ParticleSystem{
    /**
     * max lifespan in ms
     */
    public static final int MAX_LIFE_SPAN = 10_000;
    private String imageFileMap;
    private Particle[] particles;
    private int x, y, width, height, lifespan, images, particleSize, amount, spread;
    private String debugString, oldDebugString;
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
        //region setting variables accordingly
        this.spread = spread;
        this.imageFileMap = imageFileMap;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.lifespan = lifespan;
        this.particleSize = particleSize;
        particles = new Particle[amount];
        //endregion

        int current = 0;
        for (int i = 0; i < amount; i++) {
            int particleX = Random.randomAddSubtract(x, width, x, x + width);
            int particleY = Random.randomAddSubtract(y, height, y, y + height);
            int singleParticleSize = Random.randomAddSubtract(particleSize, 5, 0, Math.max(width, height));
            int particleLifespan;
            if (randomLifespan){
                particleLifespan = Random.randomAddSubtract(lifespan, 20, 0, MAX_LIFE_SPAN);
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
        for (int i = 0; i < particles.length; i++){
            if (particles[i] != null) {
                particles[i].rotation = Random.randomAddSubtract(particles[i].rotation, 3, 0, 359);
                particles[i].size = Random.randomAddSubtract(particles[i].size, 1, 0, 200);
                //Logger.log(particle.x + ":" + particle.y, MESSAGE_PRIO.DEBUG);

                particles[i].x = Random.randomAddSubtract(particles[i].x, spread, x, x + width);
                particles[i].y = Random.randomAddSubtract(particles[i].y, spread, y, y + height);
                //Logger.log(particle.x + ":" + particle.y, MESSAGE_PRIO.DEBUG);
                //Logger.log("\n", MESSAGE_PRIO.DEBUG);
                particles[i].decreseLifespan();
                if (particles[i].lifespan <= 0) {
                    particles[i] = null;
                }
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
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public int getLifespan(){
        return lifespan;
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
    public int getImages(){
        return images;
    }
    public int getParticleSize(){
        return particleSize;
    }
    public int getAmount(){
        return amount;
    }
    public int getSpread(){
        return spread;
    }
    public int getLifeSpan(){
        return lifespan;
    }
    public int getCurrentAmount(){
        return ArrayHelper.lengthNonNull(particles);
    }

    public Particle[] getParticles() {
        return particles;
    }
    public String getDebugString(){
        oldDebugString = debugString;
        int avrgLifeSpan = 0;
        int[] lifeSpans = new int[particles.length];
        for (int i = 0; i < particles.length; i++){
            if (particles[i] != null) {
                lifeSpans[i] = particles[i].getLifespan();
            } else {
                lifeSpans[i] = 0;
            }
        }
        for (int lifeSpan : lifeSpans) {
            avrgLifeSpan += lifeSpan;
        }
        avrgLifeSpan = Math.round(avrgLifeSpan / lifeSpans.length);
        debugString = "x: " + x + " y: " + y + " width: " + width + " height: " + height + " isDead: " + isDead() + " lifeSpan: " + lifespan + " amount: " + amount + " spread: " + spread + " non dead particles: " + ArrayHelper.lengthNonNull(particles) + " avrgLifeSpan: " + avrgLifeSpan + " Test: " + Random.randomAddSubtract(100, 10, 0, 200);
        return debugString;
    }
    public String getOldDebugString(){
        return oldDebugString;
    }
}
