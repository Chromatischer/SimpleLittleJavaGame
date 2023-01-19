package main;

import main.object.OBJKey;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UI {
    GamePanel gp;
    Font ariel_40 = new Font("Arial", Font.BOLD, 30); //TODO: make font size adaptable
    BufferedImage keyIMG;
    public boolean messageON = false;
    public String message = "";
    int messageFrames = 0;
    public UI(GamePanel gp){
        this.gp = gp;
        OBJKey key = new OBJKey();
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
        if (messageON){
            //g2.setFont(g2.getFont().deriveFont(30F)); //this is how to change font size on the fly
            g2.drawString(message, gp.SCREENWIDTH/2 - (int) g2.getFontMetrics().getStringBounds(message, g2).getWidth()/2, gp.SCREENHEIGHT/2);
            messageFrames ++;
        }
        if (messageFrames > gp.fps*3 && messageON){
            messageON = false;
            messageFrames = 0;
        }
    }
    public void openInventory(){

    }
}
