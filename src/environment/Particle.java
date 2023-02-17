package environment;

import gameExceptions.GameException;
import gameExceptions.InvalidParameterException;
import utilities.Logger;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Particle {
    BufferedImage image;
    private int size;
    private int x, y;
    private int rotation;
    private int lifespan;
    private double vectorX, vectorY;
    public Particle(BufferedImage image, int size, int x, int y, int rotation, int lifespan, double vectorX, double vectorY) throws GameException {
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
        this.image = image;
        this.size = size;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.lifespan = lifespan;
        this.vectorX = vectorX;
        this.vectorY = vectorY;
    }
    public void decreseLifespan(){
        if (lifespan > 0){
            lifespan --;
        }
    }
    public void setX(int x) throws InvalidParameterException {
        if (x < 0){
            throw new InvalidParameterException("x value is out of bounds expected: x > 0");
        }
        this.x = x;
    }
    public void setY(int y) throws InvalidParameterException {
        if (y < 0){
            throw new InvalidParameterException("y value is out of bounds expected: y > 0");
        }
        this.y = y;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public void setSize(int size) throws InvalidParameterException{
        if (size < 0){
            throw new InvalidParameterException("size value out of bounds expected: size > 0");
        }
        this.size = size;
    }
    public int getSize(){
        return size;
    }
    public double getVectorX(){
        return vectorX;
    }
    public double getVectorY() {
        return vectorY;
    }

    public void setRotation(int rotation) throws InvalidParameterException {
        if (rotation < 0 || rotation > 359){
            throw new InvalidParameterException("rotation value out of bounds expected: 0 > rotation < 359 for value: " + rotation);
        }
        this.rotation = rotation;
    }
    public int getRotation(){
        return rotation;
    }
    public int getLifespan(){
        return lifespan;
    }
    public void draw(Graphics2D g2){
        //TODO: test this code!
        double rotationRequired = Math.toRadians(rotation);
        double locationX = image.getWidth() / 2D;
        double locationY = image.getHeight() / 2D;
        AffineTransform affineTransform = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
        AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR);
        g2.drawImage(affineTransformOp.filter(image, null), x, y, null);
    }
}
