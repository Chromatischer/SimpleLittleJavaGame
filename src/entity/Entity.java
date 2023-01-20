package entity;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.awt.Rectangle;

import main.GamePanel;
import inventory.Inventory;
import main.UI;
import org.jetbrains.annotations.NotNull;

import java.awt.Graphics2D;
public class Entity {
    /**
     * world position values
     */
    public int worldX,worldY;
    /**
     * screen position values
     */
    public int screenX, screenY;
    /**
     * speed value
     */
    public int speed;
    /**
     * used for going into idle state
     */
    int idleUpdates = 0;
    /**
     * the intersecting object
     */
    int objIndex;
    int updateCount;
    /**
     * player sprite maps
     */
    public BufferedImage upAnimation, downAnimation, leftAnimation, rightAnimation, idleAnimation;
    /**
     * used for direction - do not set directly (use setDirection() and getDirection())
     */
    public String direction;
    public Inventory inventory;
    public GamePanel gp;
    UI ui;
    /**
     * hit-box
     */
    public Rectangle solidArea;
    /**
     * the default values used for checking intersecting objects
     */
    public int solidAreaDefaultX, solidAreaDefaultY;
    /**
     * collision boolean
     */
    public boolean collisionON = false;
    /**
     * the current animation frame
     */
    int animationFrame = 1;
    /**
     * used for the calculations of the animation frame methode
     */
    long currentTime, lastTime, timer;

    /**
     * draws the Entity using the specified image at the screenX and screenY position with the TILESIZE size from the Gamepanel class
     * @param image the image to display the Entity as
     * <p>
     * add the g2d param here when you understand what it is!
     */
    public void setImage(BufferedImage image, @NotNull Graphics2D g2d){
        g2d.drawImage(image, screenX, screenY, gp.TILESIZE, gp.TILESIZE, null);//temp fix (GamePanel TILESIZE muss noch durchgereicht werden)
    }

    /**
     * safely sets the direction for the entity
     * @param dir one of: up, down, left, right, idle
     */
    public void setDirection(@NotNull String dir){
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

        if (timer >= 1_000_000L * pauseMS){
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
    public void playAnimation(@NotNull BufferedImage image, int size, Graphics2D g2d, int frame){
        frame -= 1; //BSP: frame 1: wird zu frame 0 y coord = 0*16 = 0 dann die nächsten 16px sind 16
        //frame 3: wird frame 2 2*16=32+16=48
        try {
            //hier koennte eine bessere Loesung hin, ist aber nicht noetig, da es noch funktioniert!
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

    /**
     * moves the entity on the x-axis
     * @param left if true: move left else move right
     * @param entity the entity to move
     * @param player set true if entity is player
     */
    public void moveX(boolean left, Entity entity, boolean player){
        if (ui.getOpenInventory() == null) {
            if (left) {
                if (worldX - speed > 0) {
                    setDirection("left");
                    idleUpdates = 0;

                    collisionON = false;
                    gp.cChecker.checkTile(entity);
                    objIndex = gp.cChecker.checkObject(entity, player);
                    if (! collisionON) {
                        worldX -= speed;
                    }
                    updateCount = 0;
                }
            } else {
                if (worldX + speed < gp.MAXWORLDCOL * gp.TILESIZE - gp.TILESIZE) {
                    setDirection("right");
                    idleUpdates = 0;

                    collisionON = false;
                    gp.cChecker.checkTile(entity);
                    objIndex = gp.cChecker.checkObject(entity, player);
                    if (! collisionON) {
                        worldX += speed;
                    }
                    updateCount = 0;
                }
            }
        }
    }

    /**
     * moves the entity on the y-axis
     * @param up if true: moves entity up else: moves down
     * @param entity the entity to move
     * @param player set true if player
     */
    public void moveY(boolean up, Entity entity, boolean player) {
        if (ui.getOpenInventory() == null) {
            if (up) {
                if (worldY - speed > 0) {
                    setDirection("up");
                    idleUpdates = 0;

                    collisionON = false;
                    gp.cChecker.checkTile(entity);
                    objIndex = gp.cChecker.checkObject(entity, player);
                    if (! collisionON) {
                        worldY -= speed;
                    }
                    updateCount = 0;
                }
            } else {
                if (worldY + speed < gp.MAXWORLDCOL * gp.TILESIZE - gp.TILESIZE) {
                    setDirection("down");
                    idleUpdates = 0;

                    collisionON = false;
                    gp.cChecker.checkTile(entity);
                    objIndex = gp.cChecker.checkObject(entity, player);
                    if (! collisionON) {
                        worldY += speed;
                    }
                    updateCount = 0;
                }
            }
        }
    }
}
