package main;

import main.object.OBJChest;
import main.object.OBJKey;

public class ObjectManager {
    GamePanel gp;
    public ObjectManager(GamePanel gp){
        this.gp = gp;
    }
    public void setObject(){
        gp.obj[0] = new OBJChest();
        gp.obj[0].worldX = 24 * gp.TILESIZE;
        gp.obj[0].worldY = 7 * gp.TILESIZE;

        gp.obj[1] = new OBJChest();
        gp.obj[1].worldX = 23 * gp.TILESIZE;
        gp.obj[1].worldY = 21 * gp.TILESIZE;

        gp.obj[1] = new OBJKey();
        gp.obj[1].worldX = 25 * gp.TILESIZE;
        gp.obj[1].worldY = 23 * gp.TILESIZE;
    }
}
