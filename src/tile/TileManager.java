package tile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.util.ArrayList;
import java.util.List;

import main.GamePanel;

public class TileManager {
    String worldMap;
    GamePanel gp;
    public Tile[] tile;
    public int[][] mapTileNum;

    public TileManager (GamePanel gp){
        this.gp = gp;
        //tile = new Tile[20]; //different TYPES of Tiles
        mapTileNum = new int [gp.MAXWORLDCOL] [gp.MAXWORLDROW];
        //getTileImage();
        worldMap = "/res/world/tileMap.png";
        getTileMapImages(worldMap);
        System.out.println(tile.length);
        System.out.println();
        loadMap("/res/maps/world01.txt");
        System.out.println(tile[0].collision);
    }

    /**
     *  fills the Tile[] array with the sprites from the given image
     * <p>
     * @param image the String for the location to the Sprite map e.g /res/world/tileMap.png
     */
    public void getTileMapImages(String image){

        System.out.println("resolving image tile map");

        String collisionFile = "/res/world/tileMap.collisions";
        BufferedImage tileMapImage = null;
        try {
            tileMapImage = ImageIO.read(getClass().getResourceAsStream(image));
        } catch (IOException | NullPointerException e){
            System.out.println(e.getMessage());
        }
        assert tileMapImage != null;
        Boolean[][] collisions = null;
        try {
            InputStream is = getClass().getResourceAsStream(collisionFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            collisions = new Boolean[tileMapImage.getHeight()/16][tileMapImage.getWidth()/16];
            for (int y = 0; y < tileMapImage.getHeight()/16; y++){
                for (int x = 0; x < tileMapImage.getWidth()/16; x++){
                    String line = br.readLine();
                    if (line != null) {
                        //simple debug line (could be used later!)
                        //System.out.println("y:" + y + " x: " + x + " fileY: " + line.split(" ")[0] + " fileX: " + line.split(" ")[1] + " fileBool: " + line.split(" ")[2] + " getBool: " + Boolean.parseBoolean(line.split(" ")[2]));
                        if (Integer.parseInt(line.split(" ")[0]) == y && Integer.parseInt(line.split(" ")[1]) == x) {
                            collisions[y][x] = Boolean.parseBoolean(line.split(" ")[2]);
                        } else {
                            collisions[y][x] = false;
                        }
                    }
                }
            }
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
        assert collisions != null;
        tile = new Tile[tileMapImage.getWidth()/16 * tileMapImage.getHeight()/16];
        for (int y = 0; y < tileMapImage.getHeight(); y +=16){
            for (int x = 0; x < tileMapImage.getWidth(); x+=16){
                int tileNum = (y/16)*10 + (x/16);
                tile [tileNum] = new Tile();
                tile[tileNum].image = getTile(x,y,16,16, tileMapImage.getWidth(), tileMapImage.getHeight(), worldMap);
                if (collisions[x/16] [y/16] == true){
                    tile[tileNum].collision = true;
                } else {
                    tile [tileNum].collision = false;
                }
            }
        }
        System.out.println("resolving image tile map: DONE");
    }

    /**
     * loads the specified world map into the mapTileNum array using the [y] [x] coordinates
     * @param fileMap the location to the .txt map file
     */
    public void loadMap(String fileMap){
        System.out.println("reading map file!");
        try {
            InputStream is = getClass().getResourceAsStream(fileMap);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            int col = 0;
            int row = 0;
            while (col < gp.MAXWORLDCOL && row < gp.MAXWORLDROW){
                String line = br.readLine();
                while (col < gp.MAXWORLDCOL){
                    String numbers[] = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col] [row] = num;
                    col++;
                }
                if (col == gp.MAXWORLDCOL) {
                    col = 0;
                    row++;
                }
            }
            
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("reading done!");
    }

    /**
     * the draw function for the background
     * <p>
     * drawing two layers: first a blank layer of the standard grass image, second the specified tile from the tileNum array
     * <p>
     * <p>
     * additionally only draws the tiles shown on screen
     */
    public void draw(Graphics2D g2){
        int worldCol = 0;
        int worldRow = 0;
        while (worldCol < gp.MAXWORLDCOL && worldRow < gp.MAXWORLDROW){
            int tileNum = mapTileNum[worldCol][worldRow];
            int worldX = worldCol * gp.TILESIZE;
            int worldY = worldRow * gp.TILESIZE;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;
            if (worldX + gp.TILESIZE > gp.player.worldX - gp.player.screenX &&
                worldX - gp.TILESIZE < gp.player.worldX + gp.player.screenX &&
                worldY + gp.TILESIZE > gp.player.worldY - gp.player.screenY &&
                worldY - gp.TILESIZE < gp.player.worldY + gp.player.screenY){ //used for rendering only tiles shown on screen
            g2.drawImage(tile[20].image, screenX, screenY, gp.TILESIZE, gp.TILESIZE, null);
            g2.drawImage(tile[tileNum].image, screenX, screenY, gp.TILESIZE, gp.TILESIZE, null);
            }
            worldCol ++;

            if (worldCol == gp.MAXWORLDCOL){
                worldCol = 0;
                worldRow ++;
            }
        }
    }

    /**
     * gets a specified partial image from a larger image
     * like a sprite from a spritemap
     *
     * @param x the x position of the subimage to get
     * @param y the y position of the subimage to get
     * @param sizeX the width of the subimage to get
     * @param sizeY the height of the subimage to get
     * @param imgX the total width of the subimage to get
     * @param imgY the total height of the subimage to get
     * @param tileMapLocation the location of the image as a String
     * @return the defined subimage if possible else: null
     */
    public BufferedImage getTile(int x, int y, int sizeX, int sizeY, int imgX, int imgY, String tileMapLocation){
        BufferedImage image = null;
        try {
        image = ImageIO.read(getClass().getResourceAsStream(tileMapLocation));
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
        assert image != null;
        try{
            if (x < 0 || y < 0 || imgX < 0 || imgY < 0 || sizeX < 0 || sizeY < 0 || image == null){ //checking valid parameters
                return null;
            }
            if ((x * sizeX + sizeX < imgX) || (y * sizeY + sizeY < imgY)){
                return image.getSubimage(x, y, sizeX, sizeY);
            }
        } catch (RasterFormatException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
