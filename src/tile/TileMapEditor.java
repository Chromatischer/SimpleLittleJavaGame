package tile;

import gameExceptions.InvalidParameterException;
import main.GamePanel;
import utilities.ClickType;
import utilities.Logger;

public class TileMapEditor {
    GamePanel gp;
    int lastX = 0, lastY = 0;
    ClickType clickType = ClickType.none;
    public TileMapEditor(GamePanel gp){
        this.gp = gp;
    }
    public void run(int currentBlockID) throws InvalidParameterException {
        //Logger.log("run executed!");
        if (gp.mouseKM.mouseX != lastX || gp.mouseKM.mouseY != lastY || (gp.mouseKM.LeftMouseClicked && clickType != ClickType.left) || (gp.mouseKM.RightMouseClicked && clickType != ClickType.right)) {
            Logger.log("im inside!");
            if (gp.mouseKM.RightMouseClicked){
                clickType = ClickType.right;
            } else if (gp.mouseKM.LeftMouseClicked){
                clickType = ClickType.right;
            }

            lastX = gp.mouseKM.mouseX;
            lastY = gp.mouseKM.mouseY;
            /*
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            jetzt ein klein wenig Mathe! worldX ist die World Coordinate * gp.TILESIZE (worldXCoordinate)
            auflösen nach worldX von screenX
            screenX = worldXCoordinate * gp.TILESIZE - gp.Player.worldX + gp.player.sceenX
            worldCoordinate =
             */
            int worldX = Math.round((lastX - gp.player.screenX + gp.player.worldX) / gp.TILESIZE);
            int worldY = Math.round((lastY - gp.player.screenY + gp.player.worldY) / gp.TILESIZE);
            Logger.log(lastX + ":" + lastY + " " + worldX + ":" + worldY + " " + gp.tileM.mapTileNum[worldX][worldY]);

            if (clickType == ClickType.left) {
                gp.tileM.setMapTile(worldX, worldY, currentBlockID);
                Logger.log("changed Tile: " + worldX + ":" + worldY + " to: " + currentBlockID);
            } else if (clickType == ClickType.right) {
                gp.tileM.setMapTile(worldX, worldY, 21);
            }
        }
    }
}
