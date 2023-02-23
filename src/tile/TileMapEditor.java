package tile;

import gameExceptions.InvalidParameterException;
import main.GamePanel;
import utilities.ClickType;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TileMapEditor {
    GamePanel gp;
    int lastX = 0, lastY = 0, worldX = 0, worldY = 0;
    ClickType clickType = ClickType.none;
    int countModified = 0;
    public TileMapEditor(GamePanel gp){
        this.gp = gp;
    }

    /**
     * if clicked on a Tile will change that tile to the parameter ID if right mouse button is pressed will 'reset' that tile (place block ID 20)
     * @param currentBlockID if left mouse is clicked this is the block ID to place
     * @throws InvalidParameterException
     */
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

            //calculates the correct tiles from the clicked point
            worldX = (int) Math.round((double) ((lastX - gp.player.screenX + gp.player.worldX) / gp.TILESIZE)); //the casting is there to remove the error "integer division in floating point context"
            worldY = (int) Math.round((double) ((lastY - gp.player.screenY + gp.player.worldY) / gp.TILESIZE));

            if (worldX > 0 && worldX < gp.MAXWORLDCOL && worldY > 0 && worldY < gp.MAXWORLDROW && gp.ui.getOpenInventory() == null) {
                if (clickType == ClickType.left) { //if it is a left click, uses the selected BlockID
                    gp.tileM.setMapTile(worldX, worldY, currentBlockID);
                    countModified ++;
                } else if (clickType == ClickType.right) { //if it is a right click, uses the default block ID of 20
                    gp.tileM.setMapTile(worldX, worldY, 20);
                    countModified ++;
                }
            }
            if(countModified > 20){
                countModified = 0;
                gp.tileM.writeMap("src/res/maps/world01.txt");
            }
        }
    }
}
