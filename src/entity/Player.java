package entity;

import inventory.INVENTORY_TYPE;
import inventory.Inventory;
import items.ITEM_TYPE;
import items.ItemStack;
import main.GamePanel;
import main.KeyManager;
import main.UI;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class Player extends Entity {
    KeyManager keyH;

    public int hasKey = 0;

    /**
     * main player constructor
     * @param gp the main game panel
     * @param keyH the keyInputs to receive
     * @param ui the ui to use
     */
    public Player(@NotNull GamePanel gp, KeyManager keyH, UI ui){
        this.gp = gp;
        this.keyH = keyH;
        this.ui = ui;
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
        inventory = new Inventory(27, INVENTORY_TYPE.PLAYER);
        System.out.println("opening inventory");
        inventory.addItemStack(new ItemStack(ITEM_TYPE.KEY, 1));
        System.out.println(inventory.getAllTypesAsSting());
        ui.openInventory(inventory);
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
            if (keyH.inventoryPressed){
                ui.openInventory(inventory);
            } else {
                ui.closeInventory(inventory);
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

    /**
     * the main draw methode of the player
     * @param g2 the graphics to draw on
     */
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
