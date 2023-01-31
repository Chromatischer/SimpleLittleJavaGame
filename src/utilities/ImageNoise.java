package utilities;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageNoise {
    /**
     * generates noise on the given BufferedImage
     * @param amount the amount of noise samples to change (can overlap). Multiplied by 1000
     * @param value the value to lighten the noise point up by (random from 0 to the given value)
     * @param includeBlacks whether to include the default "black" alpha pixels (96% opacity)
     * @param colorshift whether to shift individual colors not only make the pixel brighter! if 0 no color-shift is going to happen!
     */
    public static BufferedImage noise(BufferedImage image, int amount, int value, boolean includeBlacks, int colorshift){
        Logger.log("generating noise for image: " + image.toString() + " with amount: " + amount*1000 + " and a value of: " + value + " black include: " + includeBlacks + " colorshift: " + colorshift, MESSAGE_PRIO.FINER);
        amount = amount * 1000;
        for (int i = 0; i < amount; i++) {
            int x = Random.getRandom(0, image.getWidth() - 1);
            int y = Random.getRandom(0, image.getHeight() - 1);
            int shiftR = Random.getRandom(0, colorshift);
            int shiftG = Random.getRandom(0, colorshift);
            int shiftB = Random.getRandom(0, colorshift);
            int randVal = Random.getRandom(0, value);
            Color colori = new Color(image.getRGB(x, y), true);
            if (colori.getAlpha() < 255*0.96 || includeBlacks) {
                int newRed = colori.getRed() + randVal + shiftR;
                int newGreen = colori.getGreen() + randVal + shiftG;
                int newBlue = colori.getBlue() + randVal + shiftB;
                int newAlpha = colori.getAlpha();
                if (colori.getAlpha() > value) {
                    newAlpha = colori.getAlpha() - randVal;
                }
                Color coloriFinal = new Color(newRed, newGreen, newBlue, newAlpha);
                image.setRGB(x, y, coloriFinal.getRGB());
            }
        }
        return image;
    }
}
