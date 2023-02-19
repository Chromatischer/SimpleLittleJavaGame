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
    /*----------------IMPORTANT---------------
     *
     * if you want to add a Particle-System: use the ParticleSystem*s*.addParticleSystem() methode!
     *
     ---------------------------------------*/


    /**
     * max lifespan in ms
     */
    public static final int MAX_LIFE_SPAN = 10_000;
    /**
     * the location of the image-file
     */
    private String imageFileMap;
    /**
     * all the particles contained in the Particle-System
     */
    private Particle[] particles;
    /**
     * local variables to use when creating particles
     */
    private int x, y, width, height, lifespan, images, particleSize, amount, generalMultiplicator;
    /**
     * Debug Strings
     */
    private String debugString, oldDebugString;
    /**
     * decay multiplicator
     */
    private double decayMultiplicator;
    /**
     * local variables to use when creating particles
     */
    private boolean inflow, randomLifespan, randomAmount, randomSize;

    /**
     * creates a new Particle-System using the given values
     * <p>
     *     note, that the images if there are multiple have to be each 16x16px on original size and in a vertical image-map!
     * </p>
     * furthermore the given width and height values will be used as the 'spawn-region' of the particles (they will ONLY spawn inside there)
     * @param amount the amount of particles to generate
     * @param randomAmount if this flag is set, the before specified amount may vary by 1/8
     * @param lifespan the lifespan each particle should have in ms
     * @param randomLifespan if this flag is set, the lifespan will vary by 1.500 ms
     * @param imageFileMap the (or a collection of) 16x16px large image files location to use the images from (they are pseudo randomly selected for each particle)
     * @param images the amount of images to use from the specified file-map's images
     * @param x the center x position of the Particle-Systems bounding box
     * @param y the center y position of the Particle-Systems bounding box
     * @param width the width of the Particle-Systems bounding box
     * @param height the height of the Particle-Systems bounding box
     * @param particleSize the size of the particles in px
     * @param randomParticleSize if this flag is set, the particle size will vary 1/8 of the originally specified size
     * @param decayTime indicates how fast the particles will lose momentum and stop doing anything
     * @param inflow if this flag is set, when a particle dies there will be a new one created conform to the originally specified parameters creating an inflow effect
     * @param generalMultiplicator used for multiple operations like the amount by wich a particle can move (kind of controls the temperament of the Particles)
     * @throws GameException if something goes wrong, a Game-Exception will be thrown e.g: parameter out of bounds
     */
    public ParticleSystem(int amount, boolean randomAmount, int lifespan, boolean randomLifespan, String imageFileMap, int images, int x, int y, int width, int height, int particleSize, boolean randomParticleSize, double decayTime, boolean inflow, int generalMultiplicator) throws GameException {
        Logger.log("creating new particle System!", MESSAGE_PRIO.NORMAL);
        Logger.log("with Values: amount: " + amount + " lifespan: " + lifespan + " randomAmount? " + randomAmount + " randomLifespan? " + randomLifespan + " image location: '" + imageFileMap + "' image count: " + images + " generalMultiplicator: " + generalMultiplicator + " x location: " + x + " y location: " + y + " width: " + width + " height: " + height + " particle Size: " + particleSize + " random particle Size? " + randomParticleSize, MESSAGE_PRIO.DEBUG);
        //region check values in bounds
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
        if (generalMultiplicator < 0){
            throw new InvalidParameterException("generalMultiplicator value out of bounds expected: generalMultiplicator > 0");
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
        if (decayTime < 0){
            throw new InvalidParameterException("decay-time value out of bounds expected: decay-time > 0");
        }
        //endregion

        if (randomAmount){
            this.amount = amount + Random.randomAddSubtract(amount, 20, 0, 255);
        } else {
            this.amount = amount;
        }
        //region setting variables accordingly
        this.generalMultiplicator = generalMultiplicator;
        this.imageFileMap = imageFileMap;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.lifespan = lifespan;
        this.particleSize = particleSize;
        this.inflow = inflow;
        this.randomAmount = randomAmount;
        this.randomLifespan = randomLifespan;
        this.randomSize = randomParticleSize;
        decayMultiplicator = decayTime;
        particles = new Particle[this.amount];
        //endregion

        createParticle(amount);
    }

    /**
     * creates the given number of particles
     * @param amount the amount of empty particle-slots to refill with a new particle using the preset local values
     * @throws GameException when the creation of a Particle throws an Exception
     */
    public void createParticle(int amount) throws GameException {
            int current = 0;
            for (int i = 0; i < amount; i++) {
                if (particles[i] == null) { //used for only creating particles, where there are none
                    //selects a random spawnpoint inside the Particle-System rectangle
                    int particleX = Random.randomAddSubtract(x + width / 2, width / 2, 0, width);
                    int particleY = Random.randomAddSubtract(y + height / 2, height / 2, 0, height);

                    int singleParticleSize = particleSize;

                    int particleLifespan;

                    double particleVectorX = (Random.getRandom(0, 20) - 10) / 10D; //gives a value between -1 and 1
                    double particleVectorY = (Random.getRandom(0, 20) - 10) / 10D; //gives a value between -1 and 1

                    //if the vector is 0, the particle would just be lying around, so it is increased by .2 to give it some movement!
                    if (particleVectorX == 0) {
                        particleVectorX = 0.2;
                    }
                    if (particleVectorY == 0) {
                        particleVectorY = 0.2;
                    }

                    if (randomLifespan) { //if the random-lifespan flag is set, the lifespan of each particle may vary by 1_500
                        particleLifespan = Random.randomAddSubtract(lifespan, 1_500, 0, MAX_LIFE_SPAN);
                    } else {
                        particleLifespan = lifespan;
                    }

                    if (randomSize) {
                        singleParticleSize = Random.randomAddSubtract(singleParticleSize, singleParticleSize / 8, 0, Math.min(width, height)); //if the random-size flag is set, the size may vary by up to 1/8 of the original size
                    }

                    if (current >= images) {
                        current = 0; //makes it so that if there are more than 1 image that should be used, each particle gets a different image
                    }
                    BufferedImage particleImage = ImageManager.getTile(0, 16 * current, 16, 16, 16, 16 * images, imageFileMap);

                    particles[i] = new Particle(particleImage, singleParticleSize, particleX, particleY, 0, particleLifespan, particleVectorX, particleVectorY);
                    current++;
                } else {
                    amount ++;
            }
        }
    }

    /**
     * draws the Particle-System onto the screen
     * @param g2 the Graphic to draw onto
     * @param gp the Game-Panel currently in use
     */
    void draw(Graphics2D g2, GamePanel gp){
        for(Particle particle : particles){ //iterates over each particle object and executes the draw methode
            if (particle != null) {
                particle.draw(g2, gp, x, y);
            }
        }
    }

    /**
     * updates the Particle-System (particle position, rotation etc.)
     */
    void update(){
        try {
            for (int i = 0; i < particles.length; i++) {
                if (particles[i] != null) {
                    if (Math.min(particles[i].getVectorX(), particles[i].getVectorY()) != 0) {
                        particles[i].setRotation(particles[i].getRotation() + (int) Math.round(generalMultiplicator * Math.abs(Math.max(particles[i].getVectorX(), particles[i].getVectorY())))); //using the general multiplicator and the vectors changes the rotation
                        // changes the x and y positions in the direction of the vectors of the particle by a random amount (dependent on the general multiplicator)
                        particles[i].setX((int) Math.round(particles[i].getX() + particles[i].getVectorX() * Random.getRandom(0, generalMultiplicator)));
                        particles[i].setY((int) Math.round(particles[i].getY() + particles[i].getVectorY() * Random.getRandom(0, generalMultiplicator)));
                        particles[i].decreaseVector(decayMultiplicator); // decreases the vectors of the particle to make it smoothly stop
                    }
                    if (particles[i].getLifespan() <= 0) {
                        particles[i] = null; //deletes the particle if lifespan is 0
                        if (inflow) {
                            createParticle(1); //if the 'inflow' flag is set true, all dead particles should respawn creating a flowing effect!
                        }
                    } else {
                        if (Random.getRandomBool(Math.round(((lifespan/100) / particles[i].getLifespan()) * 50))) { // depending on the lifespan uses a function similar to 1/x to make the particles get smaller faster dependent on the lifespan of the particle
                            if (particles[i].getSize() > 0 && particles[i].getSize() < Math.min(width, height)) {
                                particles[i].setSize(particles[i].getSize() - Random.getRandom(0, 1)); //reduces the size by either 0 or 1
                            }
                        }
                        particles[i].decreaseLifespan(); //decreases the particles lifespan to make it die slowly (Muhahaha)
                    }
                }
            }
        } catch (GameException e){
            e.printStackTrace();
        }
    }

    /**
     * USE: if a Particle-System isDead it can safely be deleted (there are NO more particles that are active anymore!)
     * @return true if there are no more alive particles in the Particle-System (all available places in the 'particles' Array are null)
     */
    public boolean isDead(){
        for (Particle particle : particles){
            if (particle != null){
                return false;
            }
        }
        return true;
    }

    /**
     * @return the x position of the Particle-System
     */
    public int getX(){
        return x;
    }

    /**
     * @return the y position of the Particle-System
     */
    public int getY(){
        return y;
    }

    /**
     * @return the width of the Particle-System
     */
    public int getWidth(){
        return width;
    }

    /**
     * @return the height of the Particle-System
     */
    public int getHeight(){
        return height;
    }

    /**
     * @return the number of images that can be used by the particles
     */
    public int getImages(){
        return images;
    }

    /**
     * @return the general particle size
     */
    public int getParticleSize(){
        return particleSize;
    }

    /**
     * @return the amount of initial particles in the Particle-System
     */
    public int getAmount(){
        return amount;
    }

    /**
     * @return the general multiplicator value for the Particle-System
     */
    public int getGeneralMultiplicator(){
        return generalMultiplicator;
    }

    /**
     * @return the general lifespan of the Particle-System
     */
    public int getLifeSpan(){
        return lifespan;
    }

    /**
     * @return the current amount of particles which are not dead
     */
    public int getCurrentAmount(){
        return ArrayHelper.lengthNonNull(particles);
    }

    /**
     * @return the particles in the Particle-System
     */
    public Particle[] getParticles() {
        return particles;
    }

    /**
     * @return a debug string giving much information about the Particle-System in question! USE: for debug purposes
     */
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
        debugString = "x: " + x + " y: " + y + " width: " + width + " height: " + height + " isDead: " + isDead() + " lifeSpan: " + lifespan + " amount: " + amount + " generalMultiplicator: " + generalMultiplicator + " non dead particles: " + getCurrentAmount() + " avrgLifeSpan: " + avrgLifeSpan;
        return debugString;
    }

    /**
     * @return the last debug String USE: for deletion of the on-screen information
     */
    public String getOldDebugString(){
        return oldDebugString;
    }
}
