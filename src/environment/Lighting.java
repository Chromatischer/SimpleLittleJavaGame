package environment;

import main.*;
import utilities.Logger;
import utilities.MESSAGE_PRIO;
import utilities.Random;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Lighting {
    static GamePanel gp;
    static BufferedImage darknessFilter;
    static Color[] color = new Color[12];
    static float[] fraction = new float[12];

    public Lighting(GamePanel gp, int circleSize){
        Lighting.gp = gp;

        Logger.log("setting up lighting!", MESSAGE_PRIO.DEBUG);
        darknessFilter = new BufferedImage(gp.getWidth(), gp.getHeight(), BufferedImage.TYPE_INT_ARGB); //creates an Image with the screens Dimesions
        Graphics2D g2 = (Graphics2D) darknessFilter.getGraphics();

        Area screenArea = new Area(new Rectangle2D.Double(0, 0, gp.getWidth(), gp.getHeight()));

        //setting the center of the light-point
        Logger.log("setting center point!", MESSAGE_PRIO.DEBUG);
        int centerX = gp.player.screenX + (gp.TILESIZE)/2;
        int centerY = gp.player.screenY + (gp.TILESIZE)/2;

        Logger.log("player screenX: " + gp.player.screenX, MESSAGE_PRIO.DEBUG);
        Logger.log("player screenY: " + gp.player.screenY, MESSAGE_PRIO.DEBUG);

        //getting the x and y coordinates for the image-subtraction placement
        double x = centerX - (circleSize/2F);
        double y = centerY - (circleSize/2F);

        //creating the light-point
        Logger.log("setting up the light shape!", MESSAGE_PRIO.DEBUG);
        Shape circleShape = new Ellipse2D.Double(x, y, circleSize, circleSize);
        Area lightArea = new Area(circleShape);

        //making a cutout shape for the light-point
        Logger.log("cutting out the light area!", MESSAGE_PRIO.DEBUG);
        screenArea.subtract(lightArea);

        color[0] = new Color(0, 0, 0, 0.01F);
        color[1] = new Color(0, 0, 0, 2F/12-0.02F);
        color[2] = new Color(0, 0, 0, 3F/12+0.02F);
        color[3] = new Color(0, 0, 0, 4F/12);
        color[4] = new Color(0, 0, 0, 5F/12);
        color[5] = new Color(0, 0, 0, 6F/12);
        color[6] = new Color(0, 0, 0, 7F/12-0.02F);
        color[7] = new Color(0, 0, 0, 8F/12+0.02F);
        color[8] = new Color(0, 0, 0, 9F/12);
        color[9] = new Color(0, 0, 0, 10F/12);
        color[10] = new Color(0, 0, 0, 11F/12);
        color[11] = new Color(0, 0, 0, 0.96F);

        for (int i = 0; i < fraction.length; i++) { //evenly splits up the lighting into a gradient of fractions for the length making sure first entry is 0 and last is 1!
            if (i == 0){
                fraction[i] = 0F;
            } else if (i == fraction.length -1){
                fraction[i] = 1F;
            } else {
                fraction[i] = (float) i / fraction.length;
            }
        }

        RadialGradientPaint gradientPaint = new RadialGradientPaint(centerX, centerY, circleSize/2F, fraction, color);
        g2.setPaint(gradientPaint);
        g2.fill(lightArea);

        g2.fill(screenArea);

        noise(50000, 5, false);
        noise(100000, 3, true);

        g2.dispose();
        Logger.log("setting up lighting: DONE", MESSAGE_PRIO.DEBUG);
    }
    public void draw(Graphics2D g2){
        //drawing the image to the screen
        g2.drawImage(darknessFilter, 0 , 0, null);
    }
    public static void updateLighting(int circleSize){
        if (gp != null){
            Logger.log("updating lighting!", MESSAGE_PRIO.DEBUG);
            darknessFilter = new BufferedImage(gp.getWidth(), gp.getHeight(), BufferedImage.TYPE_INT_ARGB); //creates an Image with the screens Dimesions
            Graphics2D g2 = (Graphics2D) darknessFilter.getGraphics();

            Area screenArea = new Area(new Rectangle2D.Double(0, 0, gp.getWidth(), gp.getHeight()));

            //setting the center of the light-point
            Logger.log("updating center point!", MESSAGE_PRIO.DEBUG);
            int centerX = gp.player.screenX + (gp.TILESIZE)/2;
            int centerY = gp.player.screenY + (gp.TILESIZE)/2;
            Logger.log(gp.player.screenX + " : " + gp.player.screenY, MESSAGE_PRIO.FINEST);

            //getting the x and y coordinates for the image-subtraction placement
            double x = centerX - (circleSize/2F);
            double y = centerY - (circleSize/2F);

            //creating the light-point
            Logger.log("updating up the light shape!", MESSAGE_PRIO.DEBUG);
            Shape circleShape = new Ellipse2D.Double(x, y, circleSize, circleSize);
            Area lightArea = new Area(circleShape);

            //making a cutout shape for the light-point
            Logger.log("updating light area!", MESSAGE_PRIO.DEBUG);
            screenArea.subtract(lightArea);

            RadialGradientPaint gradientPaint = new RadialGradientPaint(centerX, centerY, circleSize/2F, fraction, color);
            g2.setPaint(gradientPaint);
            g2.fill(lightArea);

            g2.fill(screenArea);

            noise(50000, 5, false);
            noise(100000, 3, true);

            g2.dispose();
            Logger.log("updating lighting: DONE", MESSAGE_PRIO.DEBUG);
        }
    }

    /**
     * generates noise on the darkness-filter
     * @param amount the amount of noise samples to change (can overlap)
     * @param value the value to lighten the noise point up by (random from 0 to the given value)
     * @param includeBlacks whether to include the default "black" alpha pixels (96% opacity)
     */
    private static void noise(int amount, int value, boolean includeBlacks){
        for (int i = 0; i < amount; i++) {
            int x = Random.getRandom(0, darknessFilter.getWidth() - 1);
            int y = Random.getRandom(0, darknessFilter.getHeight() - 1);
            int randVal = Random.getRandom(0, value);
            Color colori = new Color(darknessFilter.getRGB(x, y), true);
            if (colori.getAlpha() < 255*0.96 || includeBlacks) {
                int newRed = colori.getRed() + randVal;
                int newGreen = colori.getGreen() + randVal;
                int newBlue = colori.getBlue() + randVal;
                int newAlpha = colori.getAlpha();
                if (colori.getAlpha() > value) {
                    newAlpha = colori.getAlpha() - randVal;
                }
                Color coloriFinal = new Color(newRed, newGreen, newBlue, newAlpha);
                darknessFilter.setRGB(x, y, coloriFinal.getRGB());
            }
        }
    }
}
