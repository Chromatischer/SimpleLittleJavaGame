package main;

import main.object.*;

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

        gp.obj[2] = new OBJKey();
        gp.obj[2].worldX = 25 * gp.TILESIZE;
        gp.obj[2].worldY = 23 * gp.TILESIZE;

        gp.obj[3] = new OBJKey();
        gp.obj[3].worldX = 26 * gp.TILESIZE;
        gp.obj[3].worldY = 23 * gp.TILESIZE;

        gp.obj[4] = new OBJKey();
        gp.obj[4].worldX = 27 * gp.TILESIZE;
        gp.obj[4].worldY = 23 * gp.TILESIZE;

        gp.obj[5] = new OBJKey();
        gp.obj[5].worldX = 28 * gp.TILESIZE;
        gp.obj[5].worldY = 23 * gp.TILESIZE;

        gp.obj[6] = new OBJKey();
        gp.obj[6].worldX = 29 * gp.TILESIZE;
        gp.obj[6].worldY = 23 * gp.TILESIZE;

        gp.obj[7] = new OBJHealthPotion();
        gp.obj[7].worldX = 51 * gp.TILESIZE;
        gp.obj[7].worldY = 51 * gp.TILESIZE;
    }
}
