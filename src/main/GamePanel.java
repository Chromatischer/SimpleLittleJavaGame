package main;

import entity.Player;
import environment.EnvironmentManager;
import gameExceptions.GameException;
import gui.UI;
import gui.Vignette;
import managers.KeyManager;
import managers.MouseClickManager;
import managers.MouseMoveManager;
import tile.TileManager;
import tile.TileMapEditor;
import utilities.Logger;
import utilities.MESSAGE_PRIO;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.DimensionUIResource;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    //region screen settings
    final int ORIGINALTILESIZE = 16;
    int SCALE = 3;
    public int TILESIZE = ORIGINALTILESIZE * SCALE; //48px
    public final int MAXSCREENCOL = 16;
    public final int MAXSCREENROW = 12;
    public final int SCREENWIDTH = MAXSCREENCOL * TILESIZE; //768px
    public final int SCREENHEIGHT = MAXSCREENROW * TILESIZE; //576px
    //endregion
    JFrame mainFrame2;
    //region percent strings
    public String percentUI = "";
    public String percentPlayer = "";
    public String percentTiles = "";
    public String percentObjects = "";
    public String percentEnvironment = "";
    public String percentVignette = "";
    //endregion
    public float deltaDraw = 0F;
    public TileManager tileM = new TileManager(this);
    Thread gameThread;
    public KeyManager keyH = new KeyManager();
    public MouseClickManager mouseKM = new MouseClickManager();
    public MouseMoveManager mouseML = new MouseMoveManager();
    public ObjectManager objManager = new ObjectManager(this);
    public UI ui = new UI(this, mouseKM, mouseML);
    public CollisionChecker cChecker = new CollisionChecker(this, ui);

    public Player player = new Player(this, keyH, mouseKM, mouseML, ui);
    int lastPlayerScreenX = player.screenX, lastPlayerScreenY = player.screenY;
    Rectangle lastPlayerSolidArea = player.solidArea;
    public SuperObject[] obj = new SuperObject[10]; //10 objects at once in game (high performance impact)
    public EnvironmentManager eManager = new EnvironmentManager(this);
    public Vignette vignette = new Vignette(this, 30, 30);
    public TileMapEditor mapEditor = new TileMapEditor(this);
    //region world size
    public final int MAXWORLDCOL = 100;
    public final int MAXWORLDROW = 100;
    //endregion
    public int fps = 60;
    public int drawCount = 0;
    public int currentFPS = 0;
    public long currentTime, lastTime;
    public Graphics2D g2Debug;
    public int collisionCount = 0;
    public boolean IS_READY = false;

    public GamePanel(JFrame mainFrame){
        Logger.log("setting up game-panel!", MESSAGE_PRIO.DEBUG);
        mainFrame2 = mainFrame;
        setPreferredSize(new DimensionUIResource(SCREENWIDTH, SCREENHEIGHT)); //maybe do some trickery for resizable windows here in the future!
        setBackground(ColorUIResource.BLACK);
        setDoubleBuffered(true); //I acturally dont fucking know what this does! It is supposed to help though
        addKeyListener(keyH); //adding the key listener
        addMouseListener(mouseKM);
        addMouseMotionListener(mouseML);
        setFocusable(true); //used for catching userinputs
        Logger.log("setting up game-panel: DONE", MESSAGE_PRIO.NORMAL);
    }
    public void setupGame() {
        Logger.log("setting up game!", MESSAGE_PRIO.DEBUG);
        System.out.println();
        Logger.log("setting up environment!", MESSAGE_PRIO.DEBUG);
        eManager.setup();
        Logger.log("setting up environment: DONE", MESSAGE_PRIO.DEBUG);
        objManager.setObject();
        Logger.log("setting up game: DONE", MESSAGE_PRIO.NORMAL);
        IS_READY = true;
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
        Logger.log("Threadstate: " + gameThread.getState(), MESSAGE_PRIO.DEBUG);
    }

    @Override
    public void run() {

        double drawInterval = 1_000_000_000F / fps;
        double delta = 0;
        lastTime = System.nanoTime();
        long timer = 0;
        long timer2 = 0;
        int lastScreenX = 0, lastScreenY = 0;

        while (gameThread != null) {
            //region timing variables
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval; //difference of time till the next frame has to be drawn!     USE: Frame based operations
            timer += (currentTime - lastTime); //timer counting in nano seconds                                             USE: Time based operations
            timer2 += (currentTime - lastTime); //timer counting in nano seconds                                            USE: Time based operations
            lastTime = currentTime;
            //endregion

            //region time-based operations
            if (delta >= 1) { //updating and redrawing the game at FPS
                repaint();
                delta--;
                drawCount++;
            }
            if (timer2 >= 1_000_000) { //updates the game every milisecond not based on FPS
                update();
                timer2 = 0;
            }

            if (timer >= 1_000_000_000) { //displaying the FPS every 1 second
                mainFrame2.setTitle("2D Game" + " (" + drawCount + "FPS)");
                currentFPS = drawCount;
                drawCount = 0;
                timer = 0;
            }
            //endregion

            //region function to control the resizability of the game!
            if ((MAXSCREENCOL * (TILESIZE + ORIGINALTILESIZE) < getSize().width) && (MAXSCREENROW * (TILESIZE + ORIGINALTILESIZE) < getSize().height)) {
                SCALE++;
                TILESIZE = ORIGINALTILESIZE * SCALE; //48px
            }
            if ((MAXSCREENCOL * (TILESIZE - ORIGINALTILESIZE) > getSize().width) && (MAXSCREENROW * (TILESIZE - ORIGINALTILESIZE) > getSize().height)) {
                SCALE--;
                TILESIZE = ORIGINALTILESIZE * SCALE; //48px
            }
            if (lastScreenX != getWidth() || lastScreenY != getHeight()) {
                int playerX = player.screenX;
                int playerY = player.screenY;
                Rectangle playerBox = player.solidArea;
                lastScreenX = getWidth();
                lastScreenY = getHeight();
                eManager.updateLighting();
                vignette.update();
                if (lastPlayerScreenX != playerX || lastPlayerScreenY != playerY || lastPlayerSolidArea != playerBox) {
                    player.ui.eraseRect(lastPlayerSolidArea, lastPlayerScreenX, lastPlayerScreenY, Color.RED);
                    player.ui.drawRect(playerBox, playerX, playerY, Color.RED);
                    lastPlayerScreenX = playerX;
                    lastPlayerScreenY = playerY;
                    lastPlayerSolidArea = playerBox;
                }
            }
            //endregion
        }
    }

    public void update(){
        player.update();
        eManager.updateParticleSystems();
        /*if (Main.DEBUG.ordinal() <= MESSAGE_PRIO.DEBUG.ordinal()){
            try {
                mapEditor.run(ui.currentSelectedBlockID);
            } catch (GameException e) {
                e.printStackTrace();
            }
        }*/
    }

    int avrgLenght = 80;
    long [] passedTimes = new long[avrgLenght];
    int cycles = 0;
    long result = 0;
    public void paintComponent(Graphics g){
        //DEBUG
        long drawStart = System.nanoTime();

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2Debug = g2;
        //region tile
        long drawTile = System.nanoTime();
        tileM.draw(g2); //make sure to draw the tileMap first cause otherwise the map will layer on top of the player!
        long drawTileEnd = System.nanoTime();
        //endregion
        //region objects
        long drawObjects = System.nanoTime();
        for (SuperObject superObject : obj) {
            if (superObject != null) {
                superObject.draw(g2, this);
            }
        }
        long drawObjectsEnd = System.nanoTime();
        //endregion
        //region vignette
        long drawVignette = System.nanoTime();
        vignette.draw(g2);
        long drawVignetteEnd = System.nanoTime();
        //endregion
        //region environment
        long drawEnvironment = System.nanoTime();
        if (IS_READY) { //this is a bad fix (better than the last one) to NOT produce a null-pointer with the eManager.draw() methode (lighting is null)
            eManager.draw(g2);
        }
        long drawEnvironmentEnd = System.nanoTime();
        //endregion
        //region player
        long drawPlayer = System.nanoTime();
        player.draw(g2);
        long drawPlayerEnd = System.nanoTime();
        //endregion
        //region ui
        long drawUI = System.nanoTime();
        ui.draw(g2);
        long drawUIEnd = System.nanoTime();
        //endregion
        //region calculations
        long drawEnd = System.nanoTime();
        deltaDraw = drawEnd - drawStart;
        percentUI = "UI: " + String.format("%.0f",(drawUIEnd - drawUI) / deltaDraw*100) + "%";
        percentObjects = "Objects: " + String.format("%.0f",(drawObjectsEnd - drawObjects) / deltaDraw*100) + "%";
        percentTiles = "Tiles: " + String.format("%.0f",(drawTileEnd - drawTile) / deltaDraw*100) + "%";
        percentPlayer = "Player: " + String.format("%.0f",(drawPlayerEnd - drawPlayer) / deltaDraw*100) + "%";
        percentEnvironment = "Environment: " + String.format("%.0f", (drawEnvironmentEnd - drawEnvironment)/deltaDraw * 100) + "%";
        percentVignette = "Vignette: " + String.format("%.0f", (drawVignetteEnd - drawVignette)/deltaDraw * 100) + "%";

        //endregion
        //region avrgDrawTime
        if (cycles < avrgLenght) {
            passedTimes[cycles] = drawEnd - drawStart;
            cycles ++;
        } else {
            cycles = 0;
            long added = 0;
            for (int i = 0; i < avrgLenght; i++){
                added += passedTimes[i];
                passedTimes[i] = 0;
            }
            result = added/avrgLenght;
            Logger.log(avrgLenght + " drawtimes took: " + result + "ns on average!", MESSAGE_PRIO.FINE);
            Logger.log("that is: " + (double) Math.round(result / 100_0F) / 100 + "ms", MESSAGE_PRIO.FINE);
            Logger.log(percentUI + " " + percentObjects + " " + percentTiles + " " + percentPlayer, MESSAGE_PRIO.FINE);
        }
        //endregion
        g2.dispose(); //disposes of the recource it is using
    }
}
