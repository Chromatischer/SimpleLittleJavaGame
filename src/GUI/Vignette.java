package GUI;

import main.GamePanel;
import utilities.ImageNoise;
import utilities.Logger;
import utilities.MESSAGE_PRIO;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Vignette {
    /**
     * the main vignette image
     */
    private static BufferedImage vignette;
    /**
     * color array used for the gradient
     */
    static Color[] color = new Color[12];
    /**
     * float array used for the gradient
     */
    static float[] fraction = new float[12];
    Area cutOut;
    Area area;
    /**
     * the width of the vignette in pixels
     */
    int tileCalcW;
    /**
     * the height of the vignette in pixels
     */
    int tileCalcH;
    int entireWidth;
    int entireHeight;
    int midX;
    int midY;
    RadialGradientPaint gradientPaint;
    GamePanel gp;

    /**
     * creates a new vignette with the specified size!
     * @param gp the GamePanel
     * @param tileW the width of the vignette in tiles
     * @param tileH the height of the vignette in tiles
     */
    public Vignette(GamePanel gp, int tileW, int tileH){
        Logger.log("setting up Vignette!", MESSAGE_PRIO.DEBUG);
        this.gp = gp;

        tileCalcW = tileW*gp.TILESIZE;
        tileCalcH = tileH*gp.TILESIZE;

        setupVariables();

        vignette = new BufferedImage(entireWidth, entireHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = (Graphics2D) vignette.getGraphics();

        Logger.log(tileCalcW + ":" + tileCalcH, MESSAGE_PRIO.FINEST);
        Logger.log(gp.SCREENWIDTH + ":" + gp.SCREENHEIGHT, MESSAGE_PRIO.FINEST);

        //das ist das eigentliche Oval, welches die helle Fläche der Vignette ist.
        cutOut = new Area(new Ellipse2D.Double(midX, midY, entireWidth, entireHeight));

        //Area muss also nur genauso groß sein, wie das cutout!
        area = new Area(new Rectangle2D.Double(0,0, entireWidth, entireHeight));

        //subtrahiert das cutout von der area
        area.subtract(cutOut);

        //region color und fraction initialisierung
        color[0] = new Color(0, 0, 0, 0.01F);
        color[1] = new Color(0, 0, 0, 0.01F);
        color[2] = new Color(0, 0, 0, 0.02F);
        color[3] = new Color(0, 0, 0, 0.02F);
        color[4] = new Color(0, 0, 0, 0.02F);
        color[5] = new Color(0, 0, 0, 0.02F);
        color[6] = new Color(0, 0, 0, 0.02F);
        color[7] = new Color(0, 0, 0, 0.02F);
        color[8] = new Color(0, 0, 0, 0.5F);
        color[9] = new Color(0, 0, 0, 0.6F);
        color[10] = new Color(0, 0, 0, 0.85F);
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
        //endregion

        setupGradientPaint();
        g2.setPaint(gradientPaint);
        g2.fill(cutOut);

        g2.fill(area);

        //hier muss jetzt festgelegt werden, welcher Bereich des BufferedImages angezeigt werden soll!
        vignette = vignette.getSubimage((vignette.getWidth() - gp.SCREENWIDTH) /2,(vignette.getHeight() - gp.SCREENHEIGHT ) /2, gp.SCREENWIDTH, gp.SCREENHEIGHT);
        Logger.log("vignette W: " + vignette.getWidth() + " vignette H: " + vignette.getHeight() + " game-panel W: " + gp.getWidth() + " game-panel H: " + gp.getHeight() + " SCREENWIDTH: " + gp.SCREENWIDTH + " SCREENHEIGHT: " + gp.SCREENHEIGHT, MESSAGE_PRIO.FINEST);
        Logger.log((vignette.getWidth() - gp.SCREENWIDTH) /2 + ":" + (vignette.getHeight() - gp.SCREENHEIGHT) /2, MESSAGE_PRIO.FINEST);

        ImageNoise.noise(vignette, 50, 5, false, 3);
        ImageNoise.noise(vignette, 100, 3, true, 0);

        g2.dispose();
        Logger.log("setting up Vignette: DONE", MESSAGE_PRIO.NORMAL);
    }

    /**
     * updates the vignette using the pre-defined size values
     */
    public void update(){
        setupVariables();
        vignette = new BufferedImage(entireWidth, entireHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) vignette.getGraphics();

        cutOut = new Area(new Ellipse2D.Double(midX, midY, entireWidth, entireHeight));
        area = new Area(new Rectangle2D.Double(0,0, entireWidth, entireHeight));
        area.subtract(cutOut);
        setupGradientPaint();
        g2.setPaint(gradientPaint);
        g2.fill(cutOut);
        g2.fill(area);
        vignette = vignette.getSubimage((vignette.getWidth() - gp.getWidth()) /2,(vignette.getHeight() - gp.getHeight()) /2, gp.getWidth(), gp.getHeight());
        g2.dispose();

    }

    /**
     * draws the vignette
     * @param g2 the Graphics to draw on
     */
    public void draw(Graphics2D g2){
        g2.drawImage(vignette, 0,0,null);
    }

    /**
     * sets all the variables used
     */
    public void setupVariables() {
            //entweder soll das IMG so groß sein, wie die vignette, oder die screen-size!
            entireWidth = Math.max(tileCalcW, gp.SCREENWIDTH);
            entireHeight = Math.max(tileCalcH, gp.SCREENHEIGHT);

            //errechnet den Mittelpunkt der vignette, anhand der gesamthöhe/-länge
            midX = entireWidth / 2;
            midY = entireHeight / 2;
    }

    /**
     * sets up the gradient-paint
     */
    public void setupGradientPaint(){
        //TODO: hier ist das Problem, diese gradient paint kann kein Oval sein, wahrscheinlich erstmal nicht so schlimm aber muss vlt. noch behoben werden
        gradientPaint = new RadialGradientPaint(midX, midY, tileCalcW / 2F, fraction, color);
    }
}
