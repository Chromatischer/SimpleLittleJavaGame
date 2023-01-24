package main;

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
        if(Main.DEBUG){System.out.println("reading file: " + tileMapLocation);}
        BufferedImage image = null;
        try {
            image = ImageIO.read(Objects.requireNonNull(ImageManager.class.getResourceAsStream(tileMapLocation)));
            nullImg = ImageIO.read(Objects.requireNonNull(ImageManager.class.getResourceAsStream("/res/missing_texture.png")));
        } catch (IOException e){
            System.out.println("reading file: " + tileMapLocation + ": FAILED!");
            System.out.println(e.getMessage());
        }
        assert image != null;
        assert nullImg != null;
        try{
            if (x < 0 || y < 0 || imgX < 0 || imgY < 0 || sizeX < 0 || sizeY < 0){ //checking valid parameters
                System.out.println("reading file: " + tileMapLocation + ": FAILED!");
                System.out.println("cause: one value smaller 0!");
                return nullImg;
            }
            if ((x + sizeX <= imgX) || (y + sizeY <= imgY)){
                if(Main.DEBUG){System.out.println("reading file: " + tileMapLocation + ": SUCCESSFULL!");}
                return image.getSubimage(x, y, sizeX, sizeY);
            }
        } catch (RasterFormatException e){
            System.out.println("reading file: " + tileMapLocation + ": FAILED!");
            System.out.println(e.getMessage());
        }
        System.out.println("reading file: " + tileMapLocation + ": FAILED!");
        System.out.println("cause: unknown");
        if (Main.DEBUG){
            System.out.println("debug values:");
            System.out.println("x: " + x + " y: " + y + " imgX: " + imgX + " imgY: " + imgY + " sizeX: " + sizeX + " sizeY: " + sizeY +
                               " w: " + image.getWidth() + " h: " + image.getHeight() + " xCalc: " + (x * sizeX + sizeX) + " yCalc: " + (y * sizeY + sizeY) +
                               " xCalcB: " + (x * sizeX + sizeX < imgX) + " yCalcB: " + (y * sizeY + sizeY < imgY));
            System.out.println("---------------------------");
        }
        return nullImg;
    }
}
