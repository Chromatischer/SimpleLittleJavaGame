package environment;

import main.GamePanel;
import main.Logger;
import main.MESSAGE_PRIO;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Lighting {
    GamePanel gp;
    BufferedImage darknessFilter;
    public Lighting(GamePanel gp, int circleSize){
        this.gp = gp;

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
        g2.setColor(new Color(0,0,0,0.95F));
        g2.fill(screenArea);

        g2.dispose();
        Logger.log("setting up lighting: DONE", MESSAGE_PRIO.DEBUG);
    }
    public void draw(Graphics2D g2){
        //drawing the image to the screen
        g2.drawImage(darknessFilter, 0 , 0, null);
    }
    public void updateLighting(int circleSize){
        if (gp != null){
            Logger.log("updating lighting!", MESSAGE_PRIO.DEBUG);
            darknessFilter = new BufferedImage(gp.getWidth(), gp.getHeight(), BufferedImage.TYPE_INT_ARGB); //creates an Image with the screens Dimesions
            Graphics2D g2 = (Graphics2D) darknessFilter.getGraphics();

            Area screenArea = new Area(new Rectangle2D.Double(0, 0, gp.getWidth(), gp.getHeight()));

            //setting the center of the light-point
            Logger.log("updating center point!", MESSAGE_PRIO.DEBUG);
            int centerX = gp.player.screenX + (gp.TILESIZE)/2;
            int centerY = gp.player.screenY + (gp.TILESIZE)/2;

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
            g2.setColor(new Color(0,0,0,0.95F));
            g2.fill(screenArea);

            g2.dispose();
            Logger.log("updating lighting: DONE", MESSAGE_PRIO.DEBUG);
        }
    }
}
