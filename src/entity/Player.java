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
            speed = gp.TILESIZE/3; //keep it modular but this is used for collision detection!
            if (!keyH.upPressed && !keyH.downPressed && !keyH.leftPressed && !keyH.rightPressed){
                idleUpdates ++;
            }
            if (keyH.upPressed){
                setDirection("up");
                idleUpdates = 0;

                collisionON = false;
                gp.cChecker.checkTile(this);
                if (!collisionON){
                    worldY -= speed;
                }
            }
            if (keyH.downPressed){
                setDirection("down");
                idleUpdates = 0;

                collisionON = false;
                gp.cChecker.checkTile(this);
                if (!collisionON){
                    worldY += speed;
                }
            }
            if (keyH.leftPressed){
                setDirection("left");
                idleUpdates = 0;

                collisionON = false;
                gp.cChecker.checkTile(this);
                if (!collisionON){
                    worldX -= speed;
                }
            }
            if (keyH.rightPressed){
                setDirection("right");
                idleUpdates = 0;

                collisionON = false;
                gp.cChecker.checkTile(this);
                if (!collisionON){
                    worldX += speed;
                }
            }
            updateCount = 0;
        }
        if (idleUpdates >= 5){
            setDirection("idle");
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
