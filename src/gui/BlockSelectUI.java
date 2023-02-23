package gui;

import main.GamePanel;
import managers.MouseWheelManager;
import tile.TileManager;
import utilities.Logger;
import utilities.MESSAGE_PRIO;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class BlockSelectUI {
    private int sBlockID;
    private final GamePanel gp;
    private final int maxBlockNum;
    private int lastMouseWheelPos;
    private BufferedImage backGroundIMG;
    public BlockSelectUI(GamePanel gp){
        this.gp = gp;
        maxBlockNum = gp.tileM.tile.length;
        sBlockID = 0;
        try {
            backGroundIMG = ImageIO.read(getClass().getResourceAsStream("/res/ui/dialogue_map.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void draw(Graphics2D g2, int x, int y){
        g2.drawImage(backGroundIMG, x, y, 2*gp.TILESIZE, 2*gp.TILESIZE, null);
        g2.drawImage(gp.tileM.tile[sBlockID].image, x + gp.TILESIZE / 2, y + gp.TILESIZE / 2, gp.TILESIZE, gp.TILESIZE, null);
        g2.drawString(String.valueOf(sBlockID), x + gp.TILESIZE / 2 + gp.TILESIZE, y + gp.TILESIZE / 2 + gp.TILESIZE);
    }
    public int getSelectedBlockID(){
        return sBlockID;
    }
    public void run(){
        if (gp.mouseWM.currentCount > lastMouseWheelPos) {
            if (sBlockID + (gp.mouseWM.currentCount - lastMouseWheelPos) >= maxBlockNum) {
                sBlockID = 0;
            } else {
                sBlockID += gp.mouseWM.currentCount - lastMouseWheelPos;
            }

            lastMouseWheelPos = gp.mouseWM.currentCount;
        } else if (gp.mouseWM.currentCount < lastMouseWheelPos){
            if (sBlockID + (gp.mouseWM.currentCount - lastMouseWheelPos) <= 0){
                sBlockID = maxBlockNum -1;
            } else {
                sBlockID += gp.mouseWM.currentCount - lastMouseWheelPos;
            }

            lastMouseWheelPos = gp.mouseWM.currentCount;
        }
    }
}
