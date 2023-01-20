package entity;

import main.GamePanel;
import main.KeyManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class Player extends Entity {
    KeyManager keyH;

    public int hasKey = 0;
    public Player(GamePanel gp, KeyManager keyH){
        this.gp = gp;
        this.keyH = keyH;
        screenX = Math.toIntExact(Math.round(gp.getSize().getWidth() /2));
        screenY = Math.toIntExact(Math.round(gp.getSize().getHeight() /2));
        solidArea = new Rectangle(8, 16, 32, 32); //TODO: make adaptable to current TileSize!
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        setDefaultValues();
        getPlayerImage();
    }

    /**
     * sets the standard values
     */
    public void setDefaultValues(){
        //starting point on the map
        worldX = 50*gp.TILESIZE;
        worldY = 50*gp.TILESIZE;
        setDirection("idle");
    }

    /**
     * loads the player images
     */
    public void getPlayerImage(){
        try {
            System.out.println("reading images for player!");
            upAnimation = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/player_walk_up_animation.png")));

            downAnimation = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/player_walk_down_animation.png")));

            leftAnimation = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/player_walk_left_animation.png")));

            rightAnimation = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/player_walk_right_animation.png")));

            idleAnimation = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/player_idle_animation.png")));
            System.out.println("reading images for player: DONE");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * main player update methode
     * <p>
     *     holding things like: movement and collision!
     * </p>
     */
    public void update(){ //called every milisecond
        objIndex = gp.cChecker.checkObject(this, true);
        if (updateCount >= 60){ //executed every 60 miliseconds
            speed = gp.TILESIZE/3; //keep it modular but this is used for collision detection!
            if (!keyH.upPressed && !keyH.downPressed && !keyH.leftPressed && !keyH.rightPressed){
                idleUpdates ++;
            }

            if (keyH.upPressed){
                moveY(true, this, true);
            }
            if (keyH.downPressed){
                moveY(false, this, true);
            }
            if (keyH.leftPressed){
                moveX(true, this, true);
            }
            if (keyH.rightPressed){
                moveX(false, this, true);
            }
        }

        if (idleUpdates >= 5){
            setDirection("idle");
        }

        interactObject(objIndex);
        updateCount ++;

        screenX = Math.toIntExact(Math.round(gp.getSize().getWidth() /2));
        screenY = Math.toIntExact(Math.round(gp.getSize().getHeight() /2));
    }

    public void draw(Graphics2D g2){
        if (Objects.equals(getDirection(), "up")){

            playAnimation(upAnimation, 16, g2, getAnimationFrame(3, 150));

        } else if (Objects.equals(getDirection(), "down")){

            playAnimation(downAnimation, 16, g2, getAnimationFrame(3, 150));

        } else if (Objects.equals(getDirection(), "left")){

            playAnimation(leftAnimation, 16, g2, getAnimationFrame(3, 150));

        } else if (Objects.equals(getDirection(), "right")){

            playAnimation(rightAnimation, 16, g2, getAnimationFrame(3, 150));

        } else if (Objects.equals(getDirection(), "idle")){

            playAnimation(idleAnimation, 16, g2, getAnimationFrame(2, 150));
        }
    }

    /**
     * main interaction methode used for well: interaction of player with objects
     * @param index the index of the object that was touched!
     */
    public void interactObject(int index){
        if (index != 999){ //not no object
            switch (gp.obj[index].name) {
                case "Key":
                    hasKey++;
                    gp.obj[index] = null;
                    gp.ui.showMessage("you have picked up a key!");
                    break;
                case "Chest":
                    if (hasKey > 0) {
                        gp.obj[index] = null;
                        hasKey --;
                    }
                    break;
                case "Door":
                    break;
            }
        }
    }
}
