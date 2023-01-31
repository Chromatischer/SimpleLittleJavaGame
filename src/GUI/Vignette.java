package GUI;

import main.GamePanel;
import utilities.ImageNoise;
import utilities.Logger;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Vignette {
    /**
     * the main vignette image
     */
    private static BufferedImage vignette;
    static Color[] color = new Color[12];
    static float[] fraction = new float[12];
    Area cutOut;
    Area area;

    GamePanel gp;
    public Vignette(GamePanel gp, int tileW, int tileH){
        this.gp = gp;

        int tileCalcW = tileW*gp.TILESIZE;
        int tileCalcH = tileH*gp.TILESIZE;

        //entweder soll das IMG so groß sein, wie die vignette, oder die screen-size!
        int entireWidth = Math.max(tileCalcW, gp.SCREENWIDTH);
        int entireHeight = Math.max(tileCalcH, gp.SCREENHEIGHT);

        vignette = new BufferedImage(entireWidth, entireHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = (Graphics2D) vignette.getGraphics();

        Logger.log(tileCalcW + ":" + tileCalcH);
        Logger.log(gp.SCREENWIDTH + ":" + gp.SCREENHEIGHT);

        //errechnet den Mittelpunkt der vignette, anhand der gesamthöhe/-länge
        int midX = entireWidth/2;
        int midY = entireHeight/2;

        //das ist das eigentliche Oval, welches die helle Fläche der Vignette ist.

        cutOut = new Area(new Ellipse2D.Double(midX, midY, entireWidth, entireHeight));

        //Area muss also nur genauso groß sein, wie das cutout!
        area = new Area(new Rectangle2D.Double(0,0, entireWidth, entireHeight));

        //subtrahiert das cutout von der area
        area.subtract(cutOut);

        //region color und fraction initialisierung
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
        //endregion

        //TODO: hier ist das Problem, diese gradient paint kann kein Oval sein, wahrscheinlich erstmal nicht so schlimm aber muss vlt. noch behoben werden
        RadialGradientPaint gradientPaint = new RadialGradientPaint(midX,midY,(tileCalcW)/2, fraction, color);

        g2.setPaint(gradientPaint);
        g2.fill(cutOut);

        g2.fill(area);

        //hier muss jetzt festgelegt werden, welcher Bereich des BufferedImages angezeigt werden soll!
        //
        g2.setClip((vignette.getWidth() - gp.getWidth()) /2,(vignette.getHeight() - gp.getHeight() ) /2, gp.getWidth(), gp.getHeight());
        Logger.log((vignette.getWidth() - gp.getWidth()) /2 + ":" + (vignette.getHeight() - gp.getHeight() ) /2);

        ImageNoise.noise(vignette, 50, 5, false, 3);

        g2.dispose();
    }
    public void update(){
        assert cutOut != null;
        assert area != null;
        assert vignette != null;
    }
    public void draw(Graphics2D g2){
        g2.drawImage(vignette,0,0, null);
    }
}
