package tile;

import gameExceptions.InvalidParameterException;
import main.GamePanel;
import managers.ImageManager;
import utilities.Logger;
import utilities.MESSAGE_PRIO;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Objects;

public class TileManager {
    String worldMap, collisionMap;
    GamePanel gp;
    public Tile[] tile;
    public int[][] mapTileNum;

    public TileManager (GamePanel gp){
        this.gp = gp;
        //tile = new Tile[20]; //different TYPES of Tiles
        mapTileNum = new int [gp.MAXWORLDCOL] [gp.MAXWORLDROW];
        //getTileImage();
        worldMap = "/res/world/tileMap.png";
        collisionMap = "/res/world/tileMap.collisions";
        getTileMapImages(worldMap, collisionMap);
        loadMap("/res/maps/world01.txt");
    }

    /**
     *  fills the Tile[] array with the sprites from the given image also: setting the collision values correspondingly
     * <p>
     * @param image the String for the location to the Sprite map e.g /res/world/tileMap.png
     */
    public void getTileMapImages(String image, String collisionMap){

        Logger.log("resolving image tile map!", MESSAGE_PRIO.DEBUG);

        BufferedImage tileMapImage = null;
        try {
            tileMapImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(image)));
        } catch (IOException | NullPointerException e){
            Logger.log(e.getMessage(), MESSAGE_PRIO.ERROR);
        }
        assert tileMapImage != null;
        Boolean[][] collisions = null;
        try {
            InputStream is = getClass().getResourceAsStream(collisionMap);
            assert is != null;
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            collisions = new Boolean[tileMapImage.getHeight()/16][tileMapImage.getWidth()/16];
            for (int y = 0; y < tileMapImage.getHeight()/16; y++){
                for (int x = 0; x < tileMapImage.getWidth()/16; x++){
                    String line = br.readLine();
                    if (line != null) {
                        //simple debug line (could be used later!)
                        Logger.log("y:" + y + " x: " + x + " fileY: " + line.split(" ")[0] + " fileX: " + line.split(" ")[1] + " fileBool: " + line.split(" ")[2] + " getBool: " + Boolean.parseBoolean(line.split(" ")[2]), MESSAGE_PRIO.FINEST);
                        if (Integer.parseInt(line.split(" ")[0]) == y && Integer.parseInt(line.split(" ")[1]) == x) {
                            collisions[y][x] = Boolean.parseBoolean(line.split(" ")[2]);
                        } else {
                            collisions[y][x] = false;
                        }
                    }
                }
            }
        } catch (IOException e){
            Logger.log(e.getMessage(), MESSAGE_PRIO.ERROR);
        }
        tile = new Tile[tileMapImage.getWidth()/16 * tileMapImage.getHeight()/16];
        for (int y = 0; y < tileMapImage.getHeight(); y +=16){
            for (int x = 0; x < tileMapImage.getWidth(); x+=16){
                int tileNum = (y/16)*10 + (x/16);
                tile [tileNum] = new Tile();
                tile[tileNum].image = ImageManager.getTile(x,y,16,16, tileMapImage.getWidth(), tileMapImage.getHeight(), worldMap);
                tile[tileNum].collision = collisions[x / 16][y / 16];
            }
        }
        Logger.log("resolving image tile map: DONE", MESSAGE_PRIO.DEBUG);
    }

    /**
     * loads the specified world map into the mapTileNum array using the [y] [x] coordinates
     * @param fileMap the location to the .txt map file
     */
    public void loadMap(String fileMap){
        Logger.log("reading map file!", MESSAGE_PRIO.DEBUG);
        try {
            InputStream is = getClass().getResourceAsStream(fileMap);
            assert is != null;
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            int col = 0;
            int row = 0;
            while (col < gp.MAXWORLDCOL && row < gp.MAXWORLDROW){
                String line = br.readLine();
                while (col < gp.MAXWORLDCOL){
                    String[] numbers = line.split(" ");

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
        Logger.log("reading map file: DONE", MESSAGE_PRIO.NORMAL);
    }

    /**
     * writes the current mapTileNum array to the specified file
     * @param fileMap the file to write the map data to
     */
    public void writeMap(String fileMap) {
        Logger.log("writing map file!", MESSAGE_PRIO.DEBUG);
        try (FileWriter fw = new FileWriter(fileMap, false)) {
            String[] line;
            line = new String[gp.MAXWORLDROW];
            int counter = 0; //TODO: add a slider or something indicating it is doing something!
            //iterates over each row and colum, adds all the values of one row int a String in the right format
            for (int row = 0; row < gp.MAXWORLDROW; row++) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int col = 0; col < gp.MAXWORLDCOL; col++) {
                    if (mapTileNum[col][row] < 10) { //if the tile-num is smaller than 10, the format should be 01 or 04 not 2 or 5
                        stringBuilder.append("0").append(mapTileNum[col][row]).append(" "); // format: ' 09 '
                    } else {
                        stringBuilder.append(mapTileNum[col][row]).append(" ");              // format: '21'
                    }
                    counter ++;
                }
                if (row != gp.MAXWORLDROW - 1) { //if it's the end of the file, does not add another free line!
                    line[row] = stringBuilder.toString().trim() + "\n";
                } else {
                    line[row] = stringBuilder.toString().trim();
                }
            }
            //writes all the lines from the prev. step to the file
            for (String s : line) {
                fw.write(s);
            }
            fw.flush(); //necessary for not forgetting any data
        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger.log("writing map file: DONE", MESSAGE_PRIO.NORMAL);
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

                //g2.drawImage(tile[20].image, screenX, screenY, gp.TILESIZE, gp.TILESIZE, null);
                g2.drawImage(tile[tileNum].image, screenX, screenY, gp.TILESIZE, gp.TILESIZE, null);
            }
            worldCol ++;

            if (worldCol == gp.MAXWORLDCOL){
                worldCol = 0;
                worldRow ++;
            }
        }
    }
    public void setMapTile(int col, int row, int num) throws InvalidParameterException {
        if (num < 0 || num > 99){
            throw new InvalidParameterException("image-num out of bounds, expected 0 < image-num < 100");
        }
        mapTileNum[col] [row] = num;
    }
}
