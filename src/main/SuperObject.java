package main;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SuperObject {
    public BufferedImage image;
    public String name;
    public boolean collision = false;
    public int worldX, worldY;

    public Rectangle solidArea = new Rectangle(0, 0, 48, 48); //TODO: make adaptable to screensize!
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;

    public void draw(Graphics2D g2, GamePanel gp){
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.TILESIZE > gp.player.worldX - gp.player.screenX &&
                worldX - gp.TILESIZE < gp.player.worldX + gp.player.screenX &&
                worldY + gp.TILESIZE > gp.player.worldY - gp.player.screenY &&
                worldY - gp.TILESIZE < gp.player.worldY + gp.player.screenY){ //used for rendering only tiles shown on screen
            g2.drawImage(image, screenX, screenY, gp.TILESIZE, gp.TILESIZE, null);
        }
    }
}
