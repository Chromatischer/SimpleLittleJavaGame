package entity;

import gui.UI;
import inventory.INVENTORY_TYPE;
import inventory.Inventory;
import items.ITEM_TYPE;
import items.ItemStack;
import main.GamePanel;
import managers.KeyManager;
import managers.MouseClickManager;
import managers.MouseMoveManager;
import org.jetbrains.annotations.NotNull;
import utilities.Logger;
import utilities.MESSAGE_PRIO;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class Player extends Entity {
    KeyManager keyH;
    MouseClickManager mouseKM;
    MouseMoveManager moveListener;

    /**
     * main player constructor
     * @param gp the main game panel
     * @param keyH the keyInputs to receive
     * @param ui the ui to use
     */
    public Player(@NotNull GamePanel gp, KeyManager keyH, MouseClickManager mouseKM, MouseMoveManager moveListener, UI ui){
        this.gp = gp;
        this.keyH = keyH;
        this.ui = ui;
        this.mouseKM = mouseKM;
        this.moveListener = moveListener;
        Logger.log("setting screen pos!", MESSAGE_PRIO.FINE);
        screenX = 384;
        screenY = 288;
        Logger.log(screenX + ":" + screenY, MESSAGE_PRIO.FINEST);
        solidArea = new Rectangle(gp.TILESIZE/6, gp.TILESIZE/3, (int) (gp.TILESIZE/1.5), (int) (gp.TILESIZE/1.5));
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        setDefaultValues();
        getPlayerImage();
    }

    /**
     * sets the standard values
     */
    public void setDefaultValues(){
        maxHealth = 11.5;
        health = maxHealth;
        inventory = new Inventory(27, INVENTORY_TYPE.PLAYER);
        ui.drawHealth(health, HEALTH_EFFECTS.NORMAL);
        damageValue = 1;
        //starting point on the map
        worldX = 50 * gp.TILESIZE;
        worldY = 50 * gp.TILESIZE;
        setDirection("idle");
        gp.tileM.mapTileNum[50][50] = 0;
        gp.tileM.mapTileNum[51][51] = 1;
        //gp.tileM.writeMap("src/res/maps/world01.txt");
    }

    /**
     * loads the player images
     */
    public void getPlayerImage(){
        try {
            Logger.log("reading images for player!", MESSAGE_PRIO.DEBUG);
            upAnimation = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/player_walk_up_animation.png")));

            downAnimation = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/player_walk_right.png")));

            leftAnimation = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/player_walk_left.png")));

            rightAnimation = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/player_walk_up.png")));

            idleAnimation = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/player_idle_animation.png")));
            Logger.log("reading images for player: DONE", MESSAGE_PRIO.NORMAL);
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
    public void update() { //called every milisecond
        solidArea.setRect(gp.TILESIZE / 6D, gp.TILESIZE / 3D, gp.TILESIZE / 1.5, gp.TILESIZE / 2D);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        ui.drawHealth(health, HEALTH_EFFECTS.NORMAL);

        objIndex = gp.cChecker.checkObject(this, true);
        if (updateCount >= 60) { //executed every 60 miliseconds
            speed = gp.TILESIZE / 3; //keep it modular but this is used for collision detection!
            if (! keyH.upPressed && ! keyH.downPressed && ! keyH.leftPressed && ! keyH.rightPressed) {
                idleUpdates++;
            }

            if (keyH.upPressed) {
                moveY(true, this, true);
            }
            if (keyH.downPressed) {
                moveY(false, this, true);
            }
            if (keyH.leftPressed) {
                moveX(true, this, true);
            }
            if (keyH.rightPressed) {
                moveX(false, this, true);
            }
            if (keyH.inventoryPressed) {
                ui.openInventory(inventory);
            } else {
                ui.closeInventory(inventory);
            }
        }

        if (idleUpdates >= 5) {
            setDirection("idle");
        }

        interactObject(objIndex);
        updateCount++;

        screenX = Math.toIntExact(Math.round(gp.getSize().getWidth() / 2));
        screenY = Math.toIntExact(Math.round(gp.getSize().getHeight() / 2));
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
     * @param index the index of the main.object that was touched!
     */
    public void interactObject(int index){
        if (index != 999){ //not no main.object
            switch (gp.obj[index].name) {
                case "Key":
                    inventory.addItemStack(new ItemStack(ITEM_TYPE.KEY, 1));
                    gp.obj[index] = null;
                    break;
                case "Chest":
                    if (inventory.subtractItemstack(new ItemStack(ITEM_TYPE.KEY, 1))){
                        gp.obj[index] = null;
                    }
                    break;
                case "Door":
                    break;
                case "Health_potion":
                    inventory.addItemStack(new ItemStack(ITEM_TYPE.HEALTH_POTION, 1));
                    gp.obj[index] = null;
                    break;
            }
        }
    }
}

/*
ParticleSystem particleSystem = null;
ParticleSystem particleSystem1 = null;
ParticleSystem particleSystem2 = null;
try {
    particleSystem = new ParticleSystem(100, true, 100, true, "/res/ui/full_heart.png", 1, worldX / gp.TILESIZE, worldY / gp.TILESIZE, 3*48, 3*48, 20, false, 1.3, true, 1);
    particleSystem1 = new ParticleSystem(100, true, 100, true, "/res/object/key.png", 1, (worldX + 2 * gp.TILESIZE) / gp.TILESIZE, (worldY + 2 * gp.TILESIZE) / gp.TILESIZE, 5*48, 5*48, 40, true, 1.6, true, 1);
    particleSystem2 = new ParticleSystem(200, true, 800, true, "/res/object/chest.png", 1, worldX / gp.TILESIZE, worldY / gp.TILESIZE, 1 * gp.TILESIZE, 1 * gp.TILESIZE, 10, false, 4, false, 1);
} catch (GameException e){
    e.printStackTrace();
}
assert particleSystem != null;
ParticleSystems.addParticleSystem(particleSystem);
assert particleSystem1 != null;
ParticleSystems.addParticleSystem(particleSystem1);
assert particleSystem2 != null;
ParticleSystems.addParticleSystem(particleSystem2);
 */
