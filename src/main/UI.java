package main;

import inventory.Inventory;
import items.ITEM_TYPE;
import items.ItemStack;
import main.object.OBJKey;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import static main.Main.DEBUG;

public class UI {
    GamePanel gp;
    MouseClickManager clickManager;
    /**
     * the main font to use!
     */
    Font ariel_40 = new Font("Arial", Font.BOLD, 30); //TODO: make font size adaptable
    /**
     * the image for the KEY ui
     */
    BufferedImage keyIMG; //TODO: remove when completely changed to inventory display
    /**
     * the inventory background image
     */
    BufferedImage inv, invTop, invBottom, invLeft, invRight, invCornerLeftTop, invCornerRightTop, invCornerLeftBottom, invCornerRightBottom;
    /**
     * indicator for message
     */
    public boolean messageON = false;

    int lastX = 0;
    int lastY = 0;

    /**
     * the message to be displayed
     */
    public String message = "";
    /**
     * the amount of frames the message was already displayed for
     */
    int messageFrames = 0;
    /**
     * the currently open inventory if non set to null
     */
    Inventory openInventory = null;
    /**
     * the item stack currently in hand if non: null
     */
    ItemStack itemStackInHand = null;
    /**
     * the amount of full hearts to render
     */
    int fullHearts;
    /**
     * the amount of half hearts to render
     */
    int halfHearts;
    /**
     * the effect to display the hearts as
     */
    HEALTH_EFFECTS heartEffect;
    /**
     * images to use for the hearts
     */
    BufferedImage fullHeartImg, halfHeartImg;

    public UI(GamePanel gp, MouseClickManager clickManager){
        this.gp = gp;
        this.clickManager = clickManager;
        OBJKey key = new OBJKey();
        String invTileMap = "/res/inv/inventory_tile_map.png";
        System.out.println("reading images for UI!");
        try {
            inv = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/inv/inv2.png")));
            invRight = ImageManager.getTile(16,0,16,16,32,64,invTileMap);
            invLeft = ImageManager.getTile(0,0,16,16,32,64,invTileMap);
            invTop = ImageManager.getTile(16,16,16,16,32,64,invTileMap);
            invBottom = ImageManager.getTile(0,16,16,16,32,64,invTileMap);
            invCornerLeftBottom = ImageManager.getTile(0,48,16,16,32,64,invTileMap);
            invCornerRightBottom = ImageManager.getTile(16,48,16,16,32,64,invTileMap);
            invCornerLeftTop = ImageManager.getTile(0,32,16,16,32,64,invTileMap);
            invCornerRightTop = ImageManager.getTile(16,32,16,16,32,64,invTileMap);
            System.out.println("reading images for UI: DONE");
        } catch (IOException e){
            e.printStackTrace();
        }
        keyIMG = key.image;
    }

    /**
     * displays a message on screen for 3 seconds at a time!
     * @param text the message to be displayed in the middle of the screen
     */
    public void showMessage(String text){
        message = text;
        messageON = true;
    }

    /**
     * the draw methode of the GUI
     */
    public void draw(@NotNull Graphics2D g2){ //dont initiate things here!
        g2.setFont(ariel_40);
        g2.setColor(Color.WHITE);
        //region key draw methode
        if (gp.player.hasKey <= 3) {
            for (int i = 0; i < gp.player.hasKey; i++){
                g2.drawImage(keyIMG, gp.TILESIZE / 2, gp.TILESIZE / 2 + i * 9, gp.TILESIZE, gp.TILESIZE, null);
            }
        } else {
            for (int i = 0; i <= 3; i++) {
                g2.drawImage(keyIMG, gp.TILESIZE / 2, gp.TILESIZE / 2 + i * 9, gp.TILESIZE, gp.TILESIZE, null);
            }
            g2.drawString("+" + gp.player.hasKey, 75, 75); //TODO: make position adaptable
        }
        //endregion
        //region heart draw methode
        int heartcol = 10; //amount of hearts in one line
        int heartrow = (int) Math.ceil(fullHearts / (double) heartcol); //amount of rows (round up)
        int heartcount = 0;
        for (int y = 0; y < heartrow; y++) {
            for (int x = 0; x < heartcol; x ++){
                if (fullHearts > heartcount){
                    g2.drawImage(fullHeartImg, x * (gp.TILESIZE/2) + gp.TILESIZE/4, y * (gp.TILESIZE/2) + gp.TILESIZE/4, gp.TILESIZE/2, gp.TILESIZE/2, null);
                    heartcount++;
                }
                if (halfHearts == 1) {
                    if (y * 10 + x == heartcount) {
                        g2.drawImage(halfHeartImg, x * (gp.TILESIZE / 2) + gp.TILESIZE / 4, y * (gp.TILESIZE / 2) + gp.TILESIZE / 4, gp.TILESIZE / 2, gp.TILESIZE / 2, null);
                    }
                }
            }
        }
        //endregion
        //region inventory draw methode
        if (openInventory == null) {
            if (messageON) {
                //g2.setFont(g2.getFont().deriveFont(30F)); //this is how to change font size on the fly
                g2.drawString(message, gp.SCREENWIDTH / 2 - (int) g2.getFontMetrics().getStringBounds(message, g2).getWidth() / 2, gp.SCREENHEIGHT / 2);
                messageFrames++;
            }
            if (messageFrames > gp.fps * 3 && messageON) {
                messageON = false;
                messageFrames = 0;
            }
        } else {
            try {
                int col = 9; //amount of slots in one line
                int row = (int) Math.ceil(openInventory.getSize() / (double) col); //amount of rows (round up)
                int count = 0; //used for displaying correct number of slots
                int spaceX = (gp.getWidth() - (col * gp.TILESIZE)) / 2; //pixel to center inv
                int spaceY = (gp.getHeight() - (row * gp.TILESIZE)) / 2; //pixels to center inv
                for (int y = 0; y < row; y++) {
                    for (int x = 0; x < col; x++) {
                        if (openInventory.getSize() > count) { //displaying correct amount of inv-slots
                            if (x == 0 && y == 0){
                                g2.drawImage(invCornerLeftTop, spaceX, spaceY, gp.TILESIZE, gp.TILESIZE, null); //drawing the tiles
                            } else if (x == col-1 && y == 0){
                                g2.drawImage(invCornerRightTop, x * gp.TILESIZE + spaceX, spaceY, gp.TILESIZE, gp.TILESIZE, null); //drawing the tiles
                            } else if (y == row -1 && x == 0) {
                                g2.drawImage(invCornerLeftBottom, spaceX, y * gp.TILESIZE + spaceY, gp.TILESIZE, gp.TILESIZE, null); //drawing the tiles
                            } else if (y == row -1 && x == col -1){
                                g2.drawImage(invCornerRightBottom, x * gp.TILESIZE + spaceX, y * gp.TILESIZE + spaceY, gp.TILESIZE, gp.TILESIZE, null); //drawing the tiles
                            } else if (y == 0){
                                g2.drawImage(invTop, x * gp.TILESIZE + spaceX, spaceY, gp.TILESIZE, gp.TILESIZE, null); //drawing the tiles
                            } else if (x == 0){
                                g2.drawImage(invLeft, spaceX, y * gp.TILESIZE + spaceY, gp.TILESIZE, gp.TILESIZE, null); //drawing the tiles
                            } else if (x == col -1){
                                g2.drawImage(invRight, x * gp.TILESIZE + spaceX, y * gp.TILESIZE + spaceY, gp.TILESIZE, gp.TILESIZE, null); //drawing the tiles
                            } else if (y == row - 1) {
                                g2.drawImage(invBottom, x * gp.TILESIZE + spaceX, y * gp.TILESIZE + spaceY, gp.TILESIZE, gp.TILESIZE, null); //drawing the tiles
                            } else {
                                g2.drawImage(inv, x * gp.TILESIZE + spaceX, y * gp.TILESIZE + spaceY, gp.TILESIZE, gp.TILESIZE, null); //drawing the tiles
                            }
                            count++;
                        }
                        ItemStack current = openInventory.getItemstack(y*9 + x);
                        if (current.getType() != ITEM_TYPE.AIR) {
                            g2.drawImage(current.getImage(), x * gp.TILESIZE + spaceX + (gp.TILESIZE / 16), y * gp.TILESIZE + spaceY + (gp.TILESIZE / 16), (gp.TILESIZE - gp.TILESIZE / 8), (gp.TILESIZE - gp.TILESIZE / 8), null); //drawing the item in the correct slot
                            String stackAmount = "";
                            if (current.getStackSize() < 1000) {
                                stackAmount = String.valueOf(current.getStackSize());
                            } else if (current.getStackSize() == 1000){
                                stackAmount = current.getStackSize()/1000 + "k";
                            } else if (current.getStackSize() > 1000 && current.getStackSize() < 1_000_000){
                                stackAmount = Math.round(current.getStackSize() / 100D) / 10D + "k";
                            }
                            g2.setFont(g2.getFont().deriveFont(Font.BOLD,(float) gp.TILESIZE/3));
                            int textX = (x * gp.TILESIZE + spaceX + gp.TILESIZE) - (int) g2.getFontMetrics().getStringBounds(stackAmount, g2).getWidth() - (gp.TILESIZE/13);
                            int textY = (y * gp.TILESIZE + spaceY + gp.TILESIZE) - ((int) g2.getFontMetrics().getStringBounds(stackAmount, g2).getHeight()/6) - (gp.TILESIZE/13);
                            g2.drawString(stackAmount, textX, textY);
                        }
                        getClickedItemSlot();
                        g2.setColor(Color.RED);
                        g2.drawOval(lastX, lastY, 10, 10);
                        g2.setColor(Color.GREEN);
                        g2.drawRect(lastX, lastY, 10, 10);
                        g2.setColor(Color.WHITE);
                    }
                }
            } catch (NullPointerException e){
                if(DEBUG){System.out.println("null-pointer in ui class: '" + e.initCause(null) + "' that is sad but not a problem that can not wait!");}
            }
        }
        //endregion
    }

    /**
     * opens a given inventory
     * @param inventory the inventory to open!
     */
    public void openInventory(Inventory inventory){
        if (openInventory == null){
            if (DEBUG){System.out.println("inventory: " + inventory.getType() + " now open");}
            openInventory = inventory;
        }
    }

    /**
     * closes a given inventory if it is open
     * @param inventory the inventory to close
     */
    public void closeInventory(Inventory inventory){
        if (openInventory == inventory){
            openInventory = null;
        }
    }

    /**
     * closes all open inventories regardless of which is currently open
     */
    public void closeAllInventory(){
        openInventory = null;
    }

    /**
     * gets the currently open inventory null if non is open!
     * @return the currently open inventory
     */
    public Inventory getOpenInventory(){
        return openInventory;
    }

    /**
     * draws given health on screen
     * @param health the health to draw
     * @param effect the effect to draw the hearts at
     */
    public void drawHealth(double health, HEALTH_EFFECTS effect){
        this.heartEffect = effect;
        if (health % 1 == 0){
            fullHearts = (int) health;
            halfHearts = 0;
        } else {
            fullHearts = (int) (health - 0.5);
            halfHearts = 1;
        }
        if (DEBUG){
            System.out.println("full hearts: " + fullHearts + " half hearts: " + halfHearts);
        }
        try {
            switch (effect){
                case NORMAL -> {
                    halfHeartImg = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/ui/half_heart.png")));
                    fullHeartImg = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/ui/full_heart.png")));
                }
                case OTHER -> {

                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void renderItemstackInHand(ItemStack itemStack){
        if (itemStack != itemStackInHand){
            if (itemStackInHand == null){
                itemStackInHand = itemStack;
            }
        }
    }
    private ItemStack getItemStackInHand(){
        return itemStackInHand;
    }
    private void getClickedItemSlot(){

        boolean lastClicked = false;
        if (clickManager.mouseX != lastX || clickManager.mouseY != lastY || clickManager.mouseClicked != lastClicked) {
            if(DEBUG){System.out.println(clickManager.mouseX + ":" + clickManager.mouseY + ":" + clickManager.mouseClicked);}
            lastY = clickManager.mouseY;
            lastX = clickManager.mouseX;
            lastClicked = clickManager.mouseClicked;
        }
    }
}
