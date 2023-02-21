package tile;

import gameExceptions.InvalidParameterException;
import main.GamePanel;
import utilities.ClickType;

public class TileMapEditor {
    GamePanel gp;
    int lastX = 0, lastY = 0;
    ClickType clickType = ClickType.none;
    public TileMapEditor(GamePanel gp){
        this.gp = gp;
    }
    public void run(int currentBlockID) throws InvalidParameterException {
        int worldX = 0, worldY = 0;
        if (gp.mouseKM.mouseX != lastX || gp.mouseKM.mouseY != lastY || gp.mouseKM.LeftMouseClicked && clickType != ClickType.left || gp.mouseKM.RightMouseClicked && clickType != ClickType.right) {
            if (gp.mouseKM.RightMouseClicked){
                clickType = ClickType.right;
            } else if (gp.mouseKM.LeftMouseClicked){
                clickType = ClickType.right;
            }

            lastX = gp.mouseKM.mouseX;
            lastY = gp.mouseKM.mouseY;
            for (int i = 0; i < gp.getHeight(); i += gp.TILESIZE) {
                if (i < lastY && i + gp.TILESIZE > lastY) {
                    worldX = i / gp.TILESIZE;
                    break;
                }
            }
            for (int i = 0; i < gp.getWidth(); i += gp.TILESIZE) {
                if (i < lastX && i + gp.TILESIZE > lastX) {
                    worldY = i / gp.TILESIZE;
                    break;
                }
            }

            if (clickType == ClickType.left) {
                gp.tileM.setMapTile(worldX, worldY, currentBlockID);
            } else if (clickType == ClickType.right) {
                gp.tileM.setMapTile(worldX, worldY, 21);
            }
        }
    }
}
