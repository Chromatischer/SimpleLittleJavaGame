package main;

import inventory.Inventory;
import items.ITEM_TYPE;
import items.ItemStack;
import main.object.OBJKey;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class UI {
    GamePanel gp;
    /**
     * the main font to use!
     */
    Font ariel_40 = new Font("Arial", Font.BOLD, 30); //TODO: make font size adaptable
    /**
     * font for small description e.g: item amounts
     */
    Font ariel_10 = new Font("Arial", Font.PLAIN, 20);
    /**
     * the image for the KEY ui
     */
    BufferedImage keyIMG; //TODO: remove when completely changed to inventory display
    /**
     * the inventory background image
     */
    BufferedImage inv;
    /**
     * indicator for message
     */
    public boolean messageON = false;
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
    public UI(GamePanel gp){
        this.gp = gp;
        OBJKey key = new OBJKey();
        try {
            inv = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/inv/inv.png")));
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
    public void draw(Graphics2D g2){ //dont initiate things here!
        g2.setFont(ariel_40);
        g2.setColor(Color.WHITE);
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
                            g2.drawImage(inv, x * gp.TILESIZE + spaceX, y * gp.TILESIZE + spaceY, gp.TILESIZE, gp.TILESIZE, null); //drawing the tiles
                            ItemStack current = openInventory.getItemstack(y + x);
                            if (current.getType() != ITEM_TYPE.AIR) {
                                g2.drawImage(current.getImage(), x * gp.TILESIZE + spaceX + (gp.TILESIZE / 16), y * gp.TILESIZE + spaceY + (gp.TILESIZE / 16), (gp.TILESIZE - gp.TILESIZE / 8), (gp.TILESIZE - gp.TILESIZE / 8), null); //drawing the item in the correct slot
                                g2.setFont(ariel_10);
                                String stackAmount = String.valueOf(current.getStackSize());
                                //g2.drawString(stackAmount, x*gp.TILESIZE, y * gp.TILESIZE);
                            }
                            count++;
                        }
                    }
                }
            } catch (NullPointerException e){
                System.out.println("null-pointer in ui class: '" + e.initCause(null) + "' that is sad but not a problem that can not wait!");
            }
        }
    }

    /**
     * opens a given inventory
     * @param inventory the inventory to open!
     */
    public void openInventory(Inventory inventory){
        if (openInventory == null){
            if (gp.DEBUG){System.out.println("inventory: " + inventory.getType() + " now open");}
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
}
