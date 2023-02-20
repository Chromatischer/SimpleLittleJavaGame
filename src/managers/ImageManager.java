package managers;

import utilities.Logger;
import utilities.MESSAGE_PRIO;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.IOException;
import java.util.Objects;

public class ImageManager {
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
     * @return the defined subimage if possible else: "missing_texture.png"
     */
    public static BufferedImage getTile(int x, int y, int sizeX, int sizeY, int imgX, int imgY, String tileMapLocation){
        BufferedImage nullImg = null;
        Logger.log("reading file: " + tileMapLocation, MESSAGE_PRIO.FINER);
        BufferedImage image = null;
        try {
            image = ImageIO.read(Objects.requireNonNull(ImageManager.class.getResourceAsStream(tileMapLocation)));
            nullImg = ImageIO.read(Objects.requireNonNull(ImageManager.class.getResourceAsStream("/res/missing_texture.png")));
        } catch (IOException e){
            Logger.log("reading file: " + tileMapLocation + ": FAILED!", MESSAGE_PRIO.ERROR);
            Logger.log(e.getMessage(), MESSAGE_PRIO.ERROR);
        }
        assert image != null;
        assert nullImg != null;
        try{
            if (x < 0 || y < 0 || imgX < 0 || imgY < 0 || sizeX < 0 || sizeY < 0){ //checking valid parameters
                Logger.log("reading file: " + tileMapLocation + ": FAILED!", MESSAGE_PRIO.FAILED);
                Logger.log("cause: one value smaller 0!", MESSAGE_PRIO.FAILED);
                return nullImg;
            }
            if ((x + sizeX <= imgX) || (y + sizeY <= imgY)){
                Logger.log("reading file: " + tileMapLocation + ": SUCCESSFULL!", MESSAGE_PRIO.DEBUG);
                return image.getSubimage(x, y, sizeX, sizeY);
            }
        } catch (RasterFormatException e){
            Logger.log("reading file: " + tileMapLocation + ": FAILED!", MESSAGE_PRIO.ERROR);
            Logger.log(e.getMessage(), MESSAGE_PRIO.ERROR);
        }
        Logger.log("reading file: " + tileMapLocation + ": FAILED!", MESSAGE_PRIO.ERROR);
        Logger.log("cause: unknown", MESSAGE_PRIO.ERROR);
        Logger.log("debug values:", MESSAGE_PRIO.ERROR);
        Logger.log("x: " + x + " y: " + y + " imgX: " + imgX + " imgY: " + imgY + " sizeX: " + sizeX + " sizeY: " + sizeY +
                           " w: " + image.getWidth() + " h: " + image.getHeight() + " xCalc: " + (x * sizeX + sizeX) + " yCalc: " + (y * sizeY + sizeY) +
                           " xCalcB: " + (x * sizeX + sizeX < imgX) + " yCalcB: " + (y * sizeY + sizeY < imgY), MESSAGE_PRIO.ERROR);
        Logger.log("---------------------------", MESSAGE_PRIO.ERROR);
        return nullImg;
    }
}
