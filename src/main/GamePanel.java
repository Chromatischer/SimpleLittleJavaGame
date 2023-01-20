package main;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.DimensionUIResource;

import entity.Player;
import main.object.SuperObject;
import tile.TileManager;

import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    /**
     * everything that is debug related should only be displayed when this boolean is set to true!
     */
    public final boolean DEBUG = false;

    //SCREEN SETTINGS
    final int ORIGINALTILESIZE = 16;
    int SCALE = 3;
    public int TILESIZE = ORIGINALTILESIZE * SCALE; //48px
    public final int MAXSCREENCOL = 16;
    public final int MAXSCREENROW = 12;
    public final int SCREENWIDTH = MAXSCREENCOL * TILESIZE; //768px
    public final int SCREENHEIGHT = MAXSCREENROW * TILESIZE; //576px
    JFrame mainFrame2;

    TileManager tileM = new TileManager(this);
    Thread gameThread;
    KeyManager keyH = new KeyManager();
    MouseClickManager mouseKM = new MouseClickManager();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public ObjectManager objManager = new ObjectManager(this);
    public UI ui = new UI(this);
    public Player player = new Player(this, keyH,mouseKM, ui);
    public SuperObject[] obj = new SuperObject[10]; //10 objects at once in game (high performance impact)

    //WORLD SETTINGS:
    public final int MAXWORLDCOL = 100;
    public final int MAXWORLDROW = 100;

    //FPS:
    public int fps = 75;

    public GamePanel(JFrame mainFrame){
        mainFrame2 = mainFrame;
        setPreferredSize(new DimensionUIResource(SCREENWIDTH, SCREENHEIGHT)); //maybe do some trickery for resizable windows here in the future!
        setBackground(ColorUIResource.BLACK);
        setDoubleBuffered(true); //I acturally dont fucking know what this does! It is supposed to help though
        addKeyListener(keyH); //adding the key listener
        setFocusable(true); //used for catching userinputs
        System.out.println("gamepanel set up!");
    }
    public void setupGame(){
        System.out.println("setting up game!");
        objManager.setObject();
        System.out.println("setting up game: DONE");
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run(){
        double drawInterval = 1_000_000_000F/fps;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        long timer2 = 0;
        int drawCount = 0;

        while (gameThread != null){

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval; //difference of time till the next frame has to be drawn!     USE: Frame based operations
            timer += (currentTime - lastTime); //timer counting in nano seconds                                             USE: Time based operations
            timer2 += (currentTime - lastTime); //timer counting in nano seconds                                            USE: Time based operations
            lastTime = currentTime;

            if (delta >= 1){ //updating and redrawing the game at 60FPS
                repaint();
                delta --;
                drawCount ++;
            }
            if (timer2 >= 1_000_000 ) { //updates the game every milisecond not based on FPS
                update();
                timer2 = 0;
            }

            if (timer >= 1_000_000_000){ //displaying the FPS every 1 second
                mainFrame2.setTitle("2D Game" + " (" + drawCount + "FPS)");
                drawCount = 0;
                timer = 0;
            }

            //function to control the resizability of the game!
            if ((MAXSCREENCOL * (TILESIZE + ORIGINALTILESIZE) < getSize().width) && (MAXSCREENROW * (TILESIZE + ORIGINALTILESIZE) < getSize().height)){
                SCALE ++;
                TILESIZE = ORIGINALTILESIZE * SCALE; //48px
            }
            if ((MAXSCREENCOL * (TILESIZE - ORIGINALTILESIZE) > getSize().width) && (MAXSCREENROW * (TILESIZE - ORIGINALTILESIZE) > getSize().height)){
                SCALE --;
                TILESIZE = ORIGINALTILESIZE * SCALE; //48px
            }
        }
    }

    public void update(){
        player.update();
    }

    int avrgLenght = 80;
    long [] passedTimes = new long[avrgLenght];
    int cycles = 0;
    long result = 0;
    public void paintComponent(Graphics g){
        //DEBUG
        long drawStart;
        drawStart = System.nanoTime();

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        tileM.draw(g2); //make sure to draw the tileMap first cause otherwise the map will layer on top of the player!
        for (SuperObject superObject : obj) {
            if (superObject != null) {
                superObject.draw(g2, this);
            }
        }
        player.draw(g2);
        ui.draw(g2);
        if (cycles < avrgLenght) {
            long drawEnd = System.nanoTime();
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
            if (DEBUG) {
                System.out.println(avrgLenght + " drawtimes took: " + result + "ns on average!");
                System.out.println("that is: " + (double) Math.round(result / 100_0F) / 100 + "ms");
            }
        }
        g2.dispose(); //disposes of the recource it is using
    }
}
