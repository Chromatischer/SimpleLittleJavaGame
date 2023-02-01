package main;

import GUI.UI;
import entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class CollisionChecker {
    GamePanel gp;
    UI ui;
    public CollisionChecker(GamePanel gp, UI ui){
        this.gp = gp;
        this.ui = ui;
    }

    /**
     * checks the tiles the hitbox of the entity is colliding with on the collision flag.
     * @param entity the entity to check collision for
     */
    public void checkTile(@NotNull Entity entity){
        //get the world position of the entitys collision box (only sides!)
        int entityLeftWorldX    = entity.worldX + entity.solidArea.x;
        int entityRightWorldX   = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY     = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY  = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol   = entityLeftWorldX/gp.TILESIZE;
        int entityRightCol  = entityRightWorldX/gp.TILESIZE;
        int entityTopRow    = entityTopWorldY/gp.TILESIZE;
        int entityBottomRow = entityBottomWorldY/gp.TILESIZE;

        int tileNum1, tileNum2;

        switch (entity.direction) {
            case "up" -> {
                entityTopRow = (entityTopWorldY - entity.speed) / gp.TILESIZE;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                ui.drawRect(0, 0,gp.TILESIZE, gp.TILESIZE, Color.ORANGE, tileNum1, tileNum2);
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionON = true;
                }
            }
            case "down" -> {
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.TILESIZE;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionON = true;
                }
            }
            case "left" -> {
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.TILESIZE;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionON = true;
                }
            }
            case "right" -> {
                entityRightCol = (entityRightWorldX + entity.speed) / gp.TILESIZE;
                tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionON = true;
                }
            }
        }
    }

    /**
     * checks if the entity is intersecting with any objects
     * @param entity the entity to check for
     * @param player is the entity player
     * @return the index of the main.object in the obj array (only if player flag is true) else returns 999 if no main.object touched: 999
     */
    public int checkObject(Entity entity, boolean player){
        int index = 999;
        for (int i = 0; i < gp.obj.length; i ++){
            if (gp.obj[i] != null){
                //get entity solid area position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;
                //get the objects area position
                gp.obj[i].solidArea.x = gp.obj[i].worldX + gp.obj[i].solidArea.x;
                gp.obj[i].solidArea.y = gp.obj[i].worldY + gp.obj[i].solidArea.y;

                switch (entity.direction) {
                    case "up" -> {
                        entity.solidArea.y -= entity.speed;
                        if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
                            if (gp.obj[i].collision){
                                entity.collisionON = true;
                            }
                            if (player){
                                index = i;
                            }
                        }
                    }
                    case "down" -> {
                        entity.solidArea.y += entity.speed;
                        if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
                            if (gp.obj[i].collision){
                                entity.collisionON = true;
                            }
                            if (player){
                                index = i;
                            }
                        }
                    }
                    case "left" -> {
                        entity.solidArea.x -= entity.speed;
                        if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
                            if (gp.obj[i].collision){
                                entity.collisionON = true;
                            }
                            if (player){
                                index = i;
                            }
                        }
                    }
                    case "right" -> {
                        entity.solidArea.x += entity.speed;
                        if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
                            if (gp.obj[i].collision){
                                entity.collisionON = true;
                            }
                            if (player){
                                index = i;
                            }
                        }
                    }
                }
                //resetting values to default!
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gp.obj[i].solidArea.x = gp.obj[i].solidAreaDefaultX;
                gp.obj[i].solidArea.y = gp.obj[i].solidAreaDefaultY;

            }
        }
        return index;
    }
}
