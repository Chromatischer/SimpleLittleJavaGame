package environment;

import gameExceptions.GameException;
import gameExceptions.InvalidParameterException;
import main.GamePanel;
import main.Main;
import utilities.MESSAGE_PRIO;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Particle {
    /**
     * the image to use
     */
    private BufferedImage image;
    /**
     * local size variable
     */
    private int size;
    /**
     * x and y position in relative coordinates TO the Particle-Systems coordinates
     */
    private int x, y;
    /**
     * rotation value (range: 0-359)
     */
    private int rotation;
    /**
     * lifespan amount of time till the particle gets deleted in ms
     */
    private int lifespan;
    /**
     * vectors ranging from -1 to 1 used to always move the particle in a certain way where x+ corresponds to right and y+ to downwards movement
     */
    private double vectorX, vectorY;

    /**
     * creates a new Particle Object with the given input values
     * @param image the image to use
     * @param size the size in px to draw with
     * @param x the x position relative to the Particle-System
     * @param y the y position relative to the Particle-System
     * @param rotation the rotation (range: 0-359)
     * @param lifespan the lifespan in ms
     * @param vectorX a value between -1 and 1 indicating the movement direction and strength on the x-axis
     * @param vectorY a value between -1 and 1 indicating the movement direction and strength on the y-axis
     * @throws GameException if something goes wrong e.g: Invalid parameters etc.
     */
    public Particle(BufferedImage image, int size, int x, int y, int rotation, int lifespan, double vectorX, double vectorY) throws GameException {
        //region check for invalid input values
        if (size < 0){
            throw new InvalidParameterException("size value out of bounds expected: size > 0");
        }
        if (image == null){
            throw new InvalidParameterException("image is null");
        }
        if (x < 0){
            throw new InvalidParameterException("x value out of bounds expected: x > 0");
        }
        if (y < 0){
                throw new InvalidParameterException("y value out of bounds expected: y > 0");
        }
        if (rotation > 359 || rotation < 0){
            throw new InvalidParameterException("rotation value out of bounds expected: 0 < rotation < 359 for value: " + rotation);
        }
        if (lifespan > ParticleSystem.MAX_LIFE_SPAN || lifespan < 0){
            throw new InvalidParameterException("lifespan value out of bounds expected:" + ParticleSystem.MAX_LIFE_SPAN +  " > lifespan > 0 for value: " + lifespan);
        }
        if (vectorX > 1 || vectorX < -1){
            throw new InvalidParameterException("vectorX value out of bounds expected: 0 < vectorX < 1 for value: " + vectorX);
        }
        if (vectorY > 1 || vectorY < -1){
            throw new InvalidParameterException("vectorY value out of bounds expected: 0 < vectorY < 1 for value: " + vectorY);
        }
        //endregion

        //region setting local variables
        this.image = image;
        this.size = size;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.lifespan = lifespan;
        this.vectorX = vectorX;
        this.vectorY = vectorY;
        //endregion
    }

    /**
     * if not already 0 or lower, will reduce the lifespan of the particle
     */
    public void decreaseLifespan(){
        if (lifespan > 0){
            lifespan --;
        }
    }

    /**
     * decreases both vectors closer to 0 to make the particle 'slow down'
     * @param decayMultiplicator the value to multiply the decay by
     */
    public void decreaseVector(double decayMultiplicator){
        if (vectorX > 0){
            vectorX -= 0.001 * decayMultiplicator;
        } else {
            vectorX += 0.001 * decayMultiplicator;
        }
        if (vectorY > 0){
            vectorY -= 0.001 * decayMultiplicator;
        } else {
            vectorY += 0.001 * decayMultiplicator;
        }
    }

    /**
     * @param x the new x value of the Particle (relative  to the Particle-System)
     */
    public void setX(int x){
        this.x = x;
    }
    /**
     * @param y the new y value of the Particle (relative  to the Particle-System)
     */
    public void setY(int y){
        this.y = y;
    }

    /**
     * @return the x value of the Particle (relative  to the Particle-System)
     */
    public int getX(){
        return x;
    }
    /**
     * @return the y value of the Particle (relative  to the Particle-System)
     */
    public int getY(){
        return y;
    }

    /**
     * @param size the new size of the particle (has to be greater 0)
     * @throws InvalidParameterException if the size is < 0
     */
    public void setSize(int size) throws InvalidParameterException{
        if (size < 0){
            throw new InvalidParameterException("size value out of bounds expected: size > 0");
        }
        this.size = size;
    }

    /**
     * @return the size of the Particle
     */
    public int getSize(){
        return size;
    }

    /**
     * @return the x-vector of the Particle
     */
    public double getVectorX(){
        return vectorX;
    }

    /**
     * @return the y-vector of the Particle
     */
    public double getVectorY() {
        return vectorY;
    }

    /**
     * @param rotation the new rotation value of the Particle (will be automatically brought in range of 0-359)
     */
    public void setRotation(int rotation){
        if (rotation < 0){
            this.rotation = 359;

        } else if (rotation > 359){
            this.rotation = 0;
        } else {
            this.rotation = rotation;
        }
    }

    /**
     * @return the rotation of the Particle
     */
    public int getRotation(){
        return rotation;
    }

    /**
     * @return the lifespan of the Particle
     */
    public int getLifespan(){
        return lifespan;
    }

    /**
     * draws the Particle to the Screen
     * @param g2 the Graphics to draw on
     * @param gp the Game-Panel (used for calculations)
     * @param systemX the x position of the Particle-System in world-coordinates
     * @param systemY the y position of the Particle-System in world-coordinates
     */
    public void draw(Graphics2D g2, GamePanel gp, int systemX, int systemY) {
        //(brings the systems coordinates into the right format) + the position of the particle relative to the player - the players world position + the payers screen position
        int screenX = (systemX * gp.TILESIZE) + x - gp.player.worldX + gp.player.screenX;
        int screenY = (systemY * gp.TILESIZE) + y - gp.player.worldY + gp.player.screenY;

        if (systemX * gp.TILESIZE + x + gp.TILESIZE > gp.player.worldX - gp.player.screenX &&
                systemX * gp.TILESIZE + x - gp.TILESIZE < gp.player.worldX + gp.player.screenX &&
                systemY * gp.TILESIZE + y + gp.TILESIZE > gp.player.worldY - gp.player.screenY &&
                systemY * gp.TILESIZE + y - gp.TILESIZE < gp.player.worldY + gp.player.screenY) {
            if (rotation != 0) { //if there is any, will apply a AffineTransform filter to the image and draw it to the screen
                double rotationRequired = Math.toRadians(rotation);
                double locationX = image.getWidth() / 2D;
                double locationY = image.getHeight() / 2D;
                AffineTransform affineTransform = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
                AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR);
                g2.drawImage(affineTransformOp.filter(image, null), screenX, screenY, size, size, null);
            } else { //apply no filter and draw directly to screen
                g2.drawImage(image, screenX, screenY, size, size, null);
            }
            if (Main.DEBUG.ordinal() <= MESSAGE_PRIO.DEBUG.ordinal()) {
                if (Math.min(vectorX, vectorY) != 0) {
                    g2.drawLine(screenX, screenY, screenX + (int) Math.round(vectorX * gp.TILESIZE), screenY + (int) Math.round(vectorY * gp.TILESIZE)); //if the x and y vectors are non 0 AND the DEBUG mode is enabled, there will be lines drawn, from the image to indicate the vectors direction and amount
                }
            }
        }
    }
}
