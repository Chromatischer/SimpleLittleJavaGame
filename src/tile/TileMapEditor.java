package tile;

import gameExceptions.InvalidParameterException;
import main.GamePanel;
import utilities.ClickType;
import utilities.Logger;
import utilities.MESSAGE_PRIO;

import java.awt.*;

public class TileMapEditor {
    GamePanel gp;
    int lastX = 0, lastY = 0, worldX = 0, worldY = 0;
    ClickType clickType = ClickType.none;
    public TileMapEditor(GamePanel gp){
        this.gp = gp;
    }
    public void run(int currentBlockID) throws InvalidParameterException {
        //check if something changed since last run!
        if (gp.mouseKM.mouseX != lastX || gp.mouseKM.mouseY != lastY || (gp.mouseKM.LeftMouseClicked && clickType != ClickType.left) || (gp.mouseKM.RightMouseClicked && clickType != ClickType.right)) {

            if (gp.mouseKM.RightMouseClicked){
                clickType = ClickType.right;
            } else if (gp.mouseKM.LeftMouseClicked){
                clickType = ClickType.left;
            }

            lastX = gp.mouseKM.mouseX;
            lastY = gp.mouseKM.mouseY;

            //erases the debug rectangles
            gp.player.ui.eraseRect(worldX * gp.TILESIZE - gp.player.worldX + gp.player.screenX, worldY * gp.TILESIZE - gp.player.worldY + gp.player.screenY, gp.TILESIZE, gp.TILESIZE, 0, 0, Color.RED);
            gp.player.ui.eraseRect(worldX * gp.TILESIZE - gp.player.worldX + gp.player.screenX, worldY * gp.TILESIZE - gp.player.worldY + gp.player.screenY, gp.TILESIZE, gp.TILESIZE, 0, 0, Color.ORANGE);

            //calculates the correct tiles from the clicked point
            worldX = (int) Math.round((double) ((lastX - gp.player.screenX + gp.player.worldX) / gp.TILESIZE)); //the casting is there to remove the error "integer division in floating point context"
            worldY = (int) Math.round((double) ((lastY - gp.player.screenY + gp.player.worldY) / gp.TILESIZE));

            Logger.log(lastX + ":" + lastY + " " + worldX + ":" + worldY + " " + gp.tileM.mapTileNum[worldX][worldY], MESSAGE_PRIO.DEBUG);


            if (clickType == ClickType.left) { //if it is a left click, uses the selected BlockID
                gp.tileM.setMapTile(worldX, worldY, currentBlockID);
                gp.player.ui.drawRect(worldX * gp.TILESIZE - gp.player.worldX + gp.player.screenX, worldY * gp.TILESIZE - gp.player.worldY + gp.player.screenY, gp.TILESIZE, gp.TILESIZE, 0, 0, Color.RED);

                Logger.log("changed Tile: " + worldX + ":" + worldY + " to: " + currentBlockID);
            } else if (clickType == ClickType.right) { //if it is a right click, uses the default block ID of 20
                gp.tileM.setMapTile(worldX, worldY, 20);
                gp.player.ui.drawRect(worldX * gp.TILESIZE - gp.player.worldX + gp.player.screenX, worldY * gp.TILESIZE - gp.player.worldY + gp.player.screenY, gp.TILESIZE, gp.TILESIZE, 0, 0, Color.ORANGE);

            }
        }
    }
}
