package entity;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.awt.Rectangle;

import main.GamePanel;

import java.awt.Graphics2D;
public class Entity {
    
    public int worldX,worldY; //world position
    public int screenX, screenY; //screen position
    public int speed;
    public BufferedImage upAnimation, downAnimation, leftAnimation, rightAnimation, idleAnimation;
    public String direction;
    public GamePanel gp;
    //used for animation
    public Rectangle solidArea;
    public boolean collisionON = false;

    public int getX() {
        return worldX;
    }

    public int getY() {
        return worldY;
    }

    public int getSpeed() {
        return speed;
    }

    /**
     * draws the Entity using the specified image at the screenX and screenY position with the TILESIZE size from the Gamepanel class
     * @param image the image to display the Entity as
     * <p>
     * add the g2d param here when you understand what it is!
     */
    public void setImage(BufferedImage image, Graphics2D g2d){
        g2d.drawImage(image, screenX, screenY, gp.TILESIZE, gp.TILESIZE, null);//temp fix (GamePanel TILESIZE muss noch durchgereicht werden)
    }

    /**
     * safely sets the direction for the entity
     * @param dir one of: up, down, left, right, idle
     */
    public void setDirection(String dir){
        if (dir.equals("up") || dir.equals("down") || dir.equals("left") || dir.equals("right") || dir.equals("idle")){
            direction = dir;
        }
    }

    /**
     * gets the direction of the entity
     * @return the direction of the Entity if for some reason not set properly returns null
     */
    public String getDirection(){
        if (direction.equals("up") || direction.equals("down") || direction.equals("left") || direction.equals("right") || direction.equals("idle")){
            return direction;
        } else {
            return null;
        }
    }

    int animationFrame = 1;
    long currentTime;
    long lastTime;
    long timer;

    /**
     * a methode to get a continuing counter for animations
     * @param framesInAnimation the number of frames to count before resetting
     * @param pauseMS the time between each count
     * @return the current frame number
     */
    public int getAnimationFrame(int framesInAnimation, int pauseMS){
        currentTime = System.nanoTime();
        timer += (currentTime - lastTime);
        lastTime = currentTime;

        if (timer >= 1_000_000 * pauseMS){
            if (animationFrame >= framesInAnimation){
                animationFrame = 0;
            }
            animationFrame ++;
            timer = 0;
        }
        return animationFrame;
    }

    /**
     * plays an animation for the Entity from a spritemap wich has all the frames vertically below each other
     * @param image the image of the animation
     * @param size the width and height of each frame
     * @param frame the number of frame that should be shown
     */
    public void playAnimation(BufferedImage image, int size, Graphics2D g2d, int frame){
        frame -= 1; //BSP: frame 1: wird zu frame 0 y coord = 0*16 = 0 dann die nächsten 16px sind 16
        //frame 3: wird frame 2 2*16=32+16=48
        try {
            //TODO: bessere Lösung finden!
            if (((frame * size) + size) <= image.getHeight() ){ //bei der idle animation gibt es das Problem, dass die frame zahl 2 ist, obwohl sie nur 1 sein dürfte
                //diese bedingung umgeht das Problem
                setImage(image.getSubimage(0, frame*size, size, size), g2d); //gets a sprite at the x position 0 the y position corresponding to the current animation frame
            } else {
                setImage(image.getSubimage(0, 0, size, size), g2d);
            }
        } catch (RasterFormatException e){
            System.out.println(e.getMessage());
            System.out.println("image:" + image.getWidth() + "*" + image.getHeight());
            System.out.println("frame:" + frame);
            System.out.println("size:" + size);
            System.out.println("looking at pixels:" + (frame*size) + "-" + ((frame*size) + size));
            System.out.println(animationFrame);
        }
    }
}
