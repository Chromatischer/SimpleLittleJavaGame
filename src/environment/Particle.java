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
    int size;
    int x, y;
    int rotation;
    int lifespan;
    public Particle(BufferedImage image, int size, int x, int y, int rotation, int lifespan) throws GameException {
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
            throw new InvalidParameterException("rotation value out of bounds expected: 0 > rotation < 359 for value: " + rotation);
        }
        if (lifespan > ParticleSystem.MAX_LIFE_SPAN || lifespan < 0){
            throw new InvalidParameterException("lifespan value out of bounds expected:" + ParticleSystem.MAX_LIFE_SPAN +  " > lifespan > 0 for value: " + lifespan);
        }
        this.image = image;
        this.size = size;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.lifespan = lifespan;
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
    public void setRotation(int rotation) throws InvalidParameterException {
        if (rotation < 0 || rotation > 359){
            throw new InvalidParameterException("rotation value out of bounds expected: 0 > rotation < 359 for value: " + rotation);
        }
        this.rotation = rotation;
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
