package entity;

import main.GamePanel;
import main.KeyManager;
import java.awt.*;
import java.io.IOException;
import javax.imageio.*;

public class Player extends Entity {
    KeyManager keyH;
    int updateCount = 0;
    int idleUpdates = 0;

    public Player(GamePanel gp, KeyManager keyH){
        this.gp = gp;
        this.keyH = keyH;
        screenX = Math.toIntExact(Math.round(gp.getSize().getWidth() /2));
        screenY = Math.toIntExact(Math.round(gp.getSize().getHeight() /2));
        solidArea = new Rectangle(8, 16, 32, 32);
        setDefaultValues();
        getPlayerImage();
    }
    public void setDefaultValues(){
        //starting point on the map
        worldX = 22*gp.TILESIZE;
        worldY = 21*gp.TILESIZE;
        setDirection("idle");
    }
    public void getPlayerImage(){
        try {
            System.out.println("reading images for player");
            upAnimation = ImageIO.read(getClass().getResourceAsStream("/res/player/player_walk_up_animation.png"));

            downAnimation = ImageIO.read(getClass().getResourceAsStream("/res/player/player_walk_down_animation.png"));

            leftAnimation = ImageIO.read(getClass().getResourceAsStream("/res/player/player_walk_left_animation.png"));

            rightAnimation = ImageIO.read(getClass().getResourceAsStream("/res/player/player_walk_right_animation.png"));

            idleAnimation = ImageIO.read(getClass().getResourceAsStream("/res/player/player_idle_animation.png"));
            System.out.println("reading images for player" + "DONE");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void update(){ //called every milisecond
        if (updateCount >= 60){ //executed every 60 miliseconds
            if (!keyH.upPressed && !keyH.downPressed && !keyH.leftPressed && !keyH.rightPressed){
                idleUpdates ++;
            }
            if (keyH.upPressed){
                setDirection("up");
                worldY -= gp.TILESIZE/3;
                idleUpdates = 0;

            }
            if (keyH.downPressed){
                setDirection("down");
                worldY += gp.TILESIZE/3;
                idleUpdates = 0;

            }
            if (keyH.leftPressed){
                setDirection("left");
                worldX -= gp.TILESIZE/3;
                idleUpdates = 0;

            }
            if (keyH.rightPressed){
                setDirection("right");
                worldX += gp.TILESIZE/3;
                idleUpdates = 0;
            }
            updateCount = 0;
            spriteCounter ++;
        }
        if (idleUpdates >= 5){
            setDirection("idle");
        }
        if (updateCount == 0 || updateCount == 20 || updateCount == 40){ //update at 20 millisek interval
            spriteCounter ++;
        }
        collisionON = false;
        gp.cChecker.checkTile(this);
        if (spriteCounter >= 10){
            if(spriteNum < 3){
                spriteNum++;
            } else if (spriteNum == 3){
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
        updateCount ++;
        screenX = Math.toIntExact(Math.round(gp.getSize().getWidth() /2));
        screenY = Math.toIntExact(Math.round(gp.getSize().getHeight() /2));
    }
    public void draw(Graphics2D g2){
        if (getDirection() == "up"){

            playAnimation(upAnimation, 16, g2, getAnimationFrame(3, 150));

        } else if (getDirection() == "down"){

            playAnimation(downAnimation, 16, g2, getAnimationFrame(3, 150));

        } else if (getDirection() == "left"){

            playAnimation(leftAnimation, 16, g2, getAnimationFrame(3, 150));

        } else if (getDirection() == "right"){

            playAnimation(rightAnimation, 16, g2, getAnimationFrame(3, 150));

        } else if (getDirection() == "idle"){

            playAnimation(idleAnimation, 16, g2, getAnimationFrame(2, 150));
        }
    }
}
