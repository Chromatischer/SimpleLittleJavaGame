package gui;

import entity.HEALTH_EFFECTS;
import inventory.Inventory;
import items.ITEM_TYPE;
import items.ItemStack;
import main.GamePanel;
import main.object.OBJKey;
import managers.ImageManager;
import managers.MouseClickManager;
import managers.MouseMoveManager;
import org.jetbrains.annotations.NotNull;
import utilities.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static main.Main.DEBUG;

public class UI {
    GamePanel gp;
    MouseClickManager clickManager;
    MouseMoveManager moveListener;
    /**
     * the main font to use!
     */
    Font ariel_40 = new Font("Arial", Font.BOLD, 30);
    /**
     * the image for the KEY ui
     */
    BufferedImage keyIMG;
    /**
     * the inventory background image
     */
    BufferedImage inv, invTop, invBottom, invLeft, invRight, invCornerLeftTop, invCornerRightTop, invCornerLeftBottom, invCornerRightBottom;
    /**
     * indicator for message
     */
    public boolean messageON = false;

    int lastX = 0;
    int lastY = 0;
    int clickedY = 0;
    int clickedX = 0;
    ClickType clickType = ClickType.none;
    int pauseframes = 0;
    //region debug percent strings
    int noDebugDrawFrame = 20;
    String percentObjects = "";
    String percentEnvironment = "";
    String percentPlayer = "";
    String percentTiles = "";
    String percentUI = "";
    String drawTime = "";
    String percentVignette = "";
    String collisionCount = "";
    //endregion
    int spaceX = 0;
    int spaceY = 0;
    int col = 9; //amount of slots in one line
    int row = 0; //amount of rows (round up)
    //region message display
    /**
     * the amount of frames the message was already displayed for
     */
    int messageFrames = 0;
    //endregion
    /**
     * the currently open inventory if non set to null
     */
    Inventory openInventory = null;
    /**
     * the item stack currently in hand if non: null
     */
    ItemStack itemStackInHand = null;
    //region health draw shit
    /**
     * the amount of full hearts to render
     */
    int fullHearts;
    /**
     * the amount of half hearts to render
     */
    int halfHearts;
    /**
     * the effect to display the hearts as
     */
    HEALTH_EFFECTS heartEffect;
    /**
     * images to use for the hearts
     */
    BufferedImage fullHeartImg, halfHeartImg;
    //endregion
    BufferedImage imageInHand = null;
    Rectangle[] drawRect;
    Color[] rectColor;
    DialogueField dialogueField = new DialogueField();
    Graphics2D g2;
    Integer dialogueY, dialogueX, dialogueWidth, dialogueHeight;
    boolean setDialogueDisplayed;
    String[] dialogueText;

    public UI(GamePanel gp, MouseClickManager clickManager, MouseMoveManager moveListener) {
        this.gp = gp;
        this.clickManager = clickManager;
        this.moveListener = moveListener;
        drawRect = new Rectangle[20];
        rectColor = new Color[20];

        Arrays.fill(drawRect, null);
        Arrays.fill(rectColor, null);

        OBJKey key = new OBJKey();
        String invTileMap = "/res/inv/inventory_tile_map.png";
        Logger.log("reading images for UI!", MESSAGE_PRIO.DEBUG);
        try {
            inv = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/inv/inv2.png")));
            invRight = ImageManager.getTile(16, 0, 16, 16, 32, 64, invTileMap);
            invLeft = ImageManager.getTile(0, 0, 16, 16, 32, 64, invTileMap);
            invTop = ImageManager.getTile(16, 16, 16, 16, 32, 64, invTileMap);
            invBottom = ImageManager.getTile(0, 16, 16, 16, 32, 64, invTileMap);
            invCornerLeftBottom = ImageManager.getTile(0, 48, 16, 16, 32, 64, invTileMap);
            invCornerRightBottom = ImageManager.getTile(16, 48, 16, 16, 32, 64, invTileMap);
            invCornerLeftTop = ImageManager.getTile(0, 32, 16, 16, 32, 64, invTileMap);
            invCornerRightTop = ImageManager.getTile(16, 32, 16, 16, 32, 64, invTileMap);
            Logger.log("reading images for UI: DONE", MESSAGE_PRIO.NORMAL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        keyIMG = key.image;
    }

    /**
     * displays a message on screen for 3 seconds at a time!
     *
     * @param text the message to be displayed in the middle of the screen
     */
    public void showMessage(String text) {
        messageON = true;
        createDialogue((gp.getWidth() - getDialogueDimension(text).width * gp.TILESIZE) / 2, (gp.getHeight() - getDialogueDimension(text).height * gp.TILESIZE) / 4, text);
    }

    /**
     * the draw methode of the GUI
     */
    public void draw(@NotNull Graphics2D g2) { //dont initiate things here!
        this.g2 = g2;
        g2.setFont(ariel_40);
        g2.setColor(Color.WHITE);
        drawAllDebugStrings();
        drawAllHearts();
        if (openInventory == null) {
            drawAllMessages();
        } else {
            drawInventory();
        }
        drawAllDialogue();
        drawAllRects();
    }

    /**
     * opens a given inventory
     *
     * @param inventory the inventory to open!
     */
    public void openInventory(Inventory inventory) {
        if (openInventory == null) {
            Logger.log("inventory: " + inventory.getType() + " now open", MESSAGE_PRIO.FINER);
            openInventory = inventory;
        }
    }

    /**
     * closes a given inventory if it is open
     *
     * @param inventory the inventory to close
     */
    public void closeInventory(Inventory inventory) {
        if (openInventory == inventory) {
            openInventory = null;
            destroyDialogue();
        }
    }

    /**
     * closes all open inventories regardless of which is currently open
     */
    public void closeAllInventory() {
        openInventory = null;
    }

    /**
     * gets the currently open inventory null if non is open!
     *
     * @return the currently open inventory
     */
    public Inventory getOpenInventory() {
        return openInventory;
    }

    /**
     * draws given health on screen
     *
     * @param health the health to draw
     * @param effect the effect to draw the hearts at
     */
    public void drawHealth(double health, HEALTH_EFFECTS effect) {
        this.heartEffect = effect;
        if (health % 1 == 0) {
            fullHearts = (int) health;
            halfHearts = 0;
        } else {
            fullHearts = (int) (health - 0.5);
            halfHearts = 1;
        }
        Logger.log("full hearts: " + fullHearts + " half hearts: " + halfHearts, MESSAGE_PRIO.FINER);
        try {
            switch (effect) {
                case NORMAL -> {
                    halfHeartImg = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/ui/half_heart.png")));
                    fullHeartImg = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/ui/full_heart.png")));
                }
                case OTHER -> {

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ItemStack getItemStackInHand() {
        return itemStackInHand;
    }

    /**
     * gets the currently clicked Itemslot and saves it to the clickedX and clickedY values!
     */
    private void getClickedItemSlot() {
        if ((clickManager.mouseX != lastX || clickManager.mouseY != lastY || (clickManager.LeftMouseClicked && clickType != ClickType.left) || (clickManager.RightMouseClicked && clickType != ClickType.right) ) && !(clickManager.mouseY > spaceY + row * gp.TILESIZE) && !(clickManager.mouseX > spaceX + col * gp.TILESIZE) && !(clickManager.mouseY < spaceY) && !(clickManager.mouseX < spaceX)) {
            if (clickManager.RightMouseClicked){
                clickType = ClickType.right;
            } else if (clickManager.LeftMouseClicked){
                clickType = ClickType.left;
            }

            lastY = clickManager.mouseY;
            lastX = clickManager.mouseX;
            for (int i = spaceY; i < row * gp.TILESIZE + spaceY; i += gp.TILESIZE) {
                if (i < lastY && i + gp.TILESIZE > lastY) {
                    clickedY = (i - spaceY) / gp.TILESIZE;
                    break;
                }
            }
            for (int i = spaceX; i < col * gp.TILESIZE + spaceX; i += gp.TILESIZE) {
                if (i < lastX && i + gp.TILESIZE > lastX) {
                    clickedX = (i - spaceX) / gp.TILESIZE;
                    break;
                }
            }
            Logger.log(clickManager.mouseX + ":" + clickManager.mouseY + ":" + clickManager.LeftMouseClicked, MESSAGE_PRIO.FINER);
        }
    }

    /**
     * creates a rectangle then draws it at the specified location on screen
     * @param x the x offset of the rectangle
     * @param y the y offset of the rectangle
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     * @param color the color to draw with
     * @param posX the x position to draw the rectangle at
     * @param posY the y position to draw the rectangle at
     */
    public void drawRect(int x, int y, int width, int height,  int posX, int posY, Color color){
        for (int i = 0; i < drawRect.length; i++) {
            if (drawRect[i] == null){
                drawRect[i] = new Rectangle(x + posX,y + posY,width,height);
                rectColor[i] = color;
                break;
            }
        }
    }

    /**
     * draws a given rectangle at the given position
     * @param rectangle the rectangle to draw
     * @param color the color to draw with
     * @param posX the x pos to draw at
     * @param posY the y pos to draw at
     */
    public void drawRect(Rectangle rectangle, int posX, int posY, Color color){
        for (int i = 0; i < drawRect.length; i++) {
            if (drawRect[i] == null){
                drawRect[i] = new Rectangle(rectangle.x + posX,rectangle.y + posY,rectangle.width,rectangle.height);
                rectColor[i] = color;
                break;
            }
        }
    }

    public void eraseRect(Rectangle rectangle, int posX, int posY, Color color){
        Rectangle actualRect = new Rectangle(rectangle.x + posX,rectangle.y + posY,rectangle.width,rectangle.height);
        for (int i = 0; i < drawRect.length; i++) {
            if (drawRect[i] != null) {
                if (RectangleHelper.compare(drawRect[i], actualRect) && rectColor[i] == color) {
                    drawRect[i] = null;
                    rectColor[i] = null;
                    break;
                }
            }
        }
    }

    /**
     * for the given values will look for a matching rectangle and change its color
     * @param rectangle the rectangle to look for
     * @param posX the x position of the rectangle
     * @param posY the y position of the rectangle
     * @param startColor the current color of the rectangle
     * @param endColor the new color of the rectangle
     */
    public void changerectColor(Rectangle rectangle, int posX, int posY, Color startColor, Color endColor){
        Rectangle actualRect = new Rectangle(rectangle.x + posX,rectangle.y + posY,rectangle.width,rectangle.height);
        for (int i = 0; i < drawRect.length; i++) {
            if (drawRect[i] != null) {
                if (RectangleHelper.compare(drawRect[i], actualRect) && rectColor[i] == startColor) {
                    rectColor[i] = endColor;
                    break;
                }
            }
        }
    }

    /**
     * draws the inventory
     */
    private void drawInventory(){
        //region inventory draw methode
        row = (int) Math.ceil(openInventory.getSize() / (double) col);
        try {
            int count = 0; //used for displaying correct number of slots
            spaceX = (gp.getWidth() - (col * gp.TILESIZE)) / 2; //pixel to center inv
            spaceY = (gp.getHeight() - (row * gp.TILESIZE)) / 2; //pixels to center inv
            for (int y = 0; y < row; y++) {
                for (int x = 0; x < col; x++) {
                    if (openInventory.getSize() > count) { //displaying correct amount of inv-slots
                        //region draw sprites
                        if (x == 0 && y == 0) {
                            g2.drawImage(invCornerLeftTop, spaceX, spaceY, gp.TILESIZE, gp.TILESIZE, null); //drawing the tiles
                        } else if (x == col - 1 && y == 0) {
                            g2.drawImage(invCornerRightTop, x * gp.TILESIZE + spaceX, spaceY, gp.TILESIZE, gp.TILESIZE, null); //drawing the tiles
                        } else if (y == row - 1 && x == 0) {
                            g2.drawImage(invCornerLeftBottom, spaceX, y * gp.TILESIZE + spaceY, gp.TILESIZE, gp.TILESIZE, null); //drawing the tiles
                        } else if (y == row - 1 && x == col - 1) {
                            g2.drawImage(invCornerRightBottom, x * gp.TILESIZE + spaceX, y * gp.TILESIZE + spaceY, gp.TILESIZE, gp.TILESIZE, null); //drawing the tiles
                        } else if (y == 0) {
                            g2.drawImage(invTop, x * gp.TILESIZE + spaceX, spaceY, gp.TILESIZE, gp.TILESIZE, null); //drawing the tiles
                        } else if (x == 0) {
                            g2.drawImage(invLeft, spaceX, y * gp.TILESIZE + spaceY, gp.TILESIZE, gp.TILESIZE, null); //drawing the tiles
                        } else if (x == col - 1) {
                            g2.drawImage(invRight, x * gp.TILESIZE + spaceX, y * gp.TILESIZE + spaceY, gp.TILESIZE, gp.TILESIZE, null); //drawing the tiles
                        } else if (y == row - 1) {
                            g2.drawImage(invBottom, x * gp.TILESIZE + spaceX, y * gp.TILESIZE + spaceY, gp.TILESIZE, gp.TILESIZE, null); //drawing the tiles
                        } else {
                            g2.drawImage(inv, x * gp.TILESIZE + spaceX, y * gp.TILESIZE + spaceY, gp.TILESIZE, gp.TILESIZE, null); //drawing the tiles
                        }
                        count++;
                        //endregion
                    }
                    ItemStack current = openInventory.getItemstack(y * 9 + x);
                    if (current.getType() != ITEM_TYPE.AIR) {
                        g2.drawImage(current.getImage(), x * gp.TILESIZE + spaceX + (gp.TILESIZE / 16), y * gp.TILESIZE + spaceY + (gp.TILESIZE / 16), (gp.TILESIZE - gp.TILESIZE / 8), (gp.TILESIZE - gp.TILESIZE / 8), null); //drawing the item in the correct slot
                        //region format stack amount
                        String stackAmount = "";
                        if (current.getStackSize() < 1000) {
                            stackAmount = String.valueOf(current.getStackSize());
                        } else if (current.getStackSize() == 1000) {
                            stackAmount = current.getStackSize() / 1000 + "k";
                        } else if (current.getStackSize() > 1000 && current.getStackSize() < 1_000_000) {
                            stackAmount = Math.round(current.getStackSize() / 100D) / 10D + "k";
                        }
                        //endregion
                        //region draw stack amount
                        g2.setFont(g2.getFont().deriveFont(Font.BOLD, (float) gp.TILESIZE / 3));
                        int textX = (x * gp.TILESIZE + spaceX + gp.TILESIZE) - (int) g2.getFontMetrics().getStringBounds(stackAmount, g2).getWidth() - (gp.TILESIZE / 13);
                        int textY = (y * gp.TILESIZE + spaceY + gp.TILESIZE) - ((int) g2.getFontMetrics().getStringBounds(stackAmount, g2).getHeight() / 6) - (gp.TILESIZE / 13);
                        g2.drawString(stackAmount, textX, textY);
                        //endregion
                    }
                }
            }
            //region render itemstack in hand
            if (itemStackInHand != null){
                g2.drawImage(imageInHand, moveListener.locationX - gp.TILESIZE /2, moveListener.locationY - gp.TILESIZE /2, gp.TILESIZE, gp.TILESIZE, null);
            }
            //endregion
            //region debugIcons
            if (DEBUG.ordinal() <= MESSAGE_PRIO.DEBUG.ordinal()) {
                if (clickType == ClickType.left) {
                    g2.setColor(Color.RED);
                } else if (clickType == ClickType.right){
                    g2.setColor(Color.BLUE);
                }
                g2.drawOval(lastX, lastY, 10, 10);
                g2.setColor(Color.GREEN);
                g2.drawRect(clickedX * gp.TILESIZE + spaceX, clickedY * gp.TILESIZE + spaceY, gp.TILESIZE, gp.TILESIZE);
                g2.setColor(Color.ORANGE);
                g2.drawOval(moveListener.locationX, moveListener.locationY, 10, 10);
                g2.setColor(Color.WHITE);
            }
            //endregion
            getClickedItemSlot();
            if (clickType == ClickType.left) {
                //region move items
                if (pauseframes > 10) {
                    if (setDialogueDisplayed){
                        destroyDialogue();
                    }
                    if (itemStackInHand == null) {
                        if (openInventory.getItemstack(clickedY * col + clickedX).getType() != ITEM_TYPE.AIR) {
                            imageInHand = openInventory.getItemstack(clickedY * col + clickedX).getImage();
                            itemStackInHand = openInventory.getItemstack(clickedY * col + clickedX);
                            openInventory.setItemStack(new ItemStack(ITEM_TYPE.AIR, 0), clickedY * col + clickedX);
                        }
                    } else {
                        if (openInventory.getItemstack(clickedY * col + clickedX).getType() == ITEM_TYPE.AIR) {
                            openInventory.setItemStack(itemStackInHand, clickedY * col + clickedX);
                            itemStackInHand = null;
                        }
                    }
                    pauseframes = 0;
                    clickType = ClickType.none;
                }
                pauseframes++;
                //endregion
            } else if (clickType == ClickType.right){
                switch (openInventory.getItemstack(clickedY * col + clickedX).getType()){
                    case HEALTH_POTION -> createDialogue(clickManager.mouseX, clickManager.mouseY, "this is a health potion!");
                    case KEY -> createDialogue(clickManager.mouseX, clickManager.mouseY, "this is a key!");
                }
            }
        } catch (NullPointerException e) {
            Logger.log("null-pointer in ui class: '" + e.initCause(null) + "' that is sad but not a problem that cannot wait!", MESSAGE_PRIO.FAILED);
        }
        //endregion
    }

    /**
     * draws the rectangles to the screen (only if DEBUG flag is set to DEBUG or lower)
     * @see main.Main
     */
    private void drawAllRects(){
        if (DEBUG.ordinal() <= MESSAGE_PRIO.DEBUG.ordinal()) {
            for (int i = 0; i < drawRect.length; i++) {
                if (drawRect[i] != null) {
                    if (rectColor[i] != null) {
                        g2.setColor(rectColor[i]);
                    } else {
                        g2.setColor(Color.BLACK);
                    }
                    g2.drawRect(drawRect[i].x, drawRect[i].y, (int) drawRect[i].getWidth(), (int) drawRect[i].getHeight());
                }
            }
        }
    }

    /**
     * draws all hearts to the Graphics input
     */
    private void drawAllHearts(){
        int heartcol = 10; //amount of hearts in one line
        int heartrow = (int) Math.ceil(fullHearts / (double) heartcol); //amount of rows (round up)
        int heartcount = 0;
        for (int y = 0; y < heartrow; y++) {
            for (int x = 0; x < heartcol; x++) {
                if (fullHearts > heartcount) {
                    g2.drawImage(fullHeartImg, x * (gp.TILESIZE / 2) + gp.TILESIZE / 4, y * (gp.TILESIZE / 2) + gp.TILESIZE / 4, gp.TILESIZE / 2, gp.TILESIZE / 2, null);
                    heartcount++;
                }
                if (halfHearts == 1) {
                    if (y * 10 + x == heartcount) {
                        g2.drawImage(halfHeartImg, x * (gp.TILESIZE / 2) + gp.TILESIZE / 4, y * (gp.TILESIZE / 2) + gp.TILESIZE / 4, gp.TILESIZE / 2, gp.TILESIZE / 2, null);
                    }
                }
            }
        }
    }

    /**
     * draws the text-message to the screen
     */
    private void drawAllMessages(){
        if (messageON) {
            messageFrames++;
        }
        if (messageFrames > gp.fps * 3 && messageON) {
            messageON = false;
            destroyDialogue();
            messageFrames = 0;
        }
    }

    /**
     * draws all the debug strings to the bottom right corner of the screen
     */
    private void drawAllDebugStrings(){
        if (DEBUG.ordinal() <= MESSAGE_PRIO.DEBUG.ordinal()){
            if (noDebugDrawFrame > 20) {
                percentObjects = gp.percentObjects;
                percentEnvironment = gp.percentEnvironment;
                percentVignette = gp.percentVignette;
                percentPlayer = gp.percentPlayer;
                percentTiles = gp.percentTiles;
                percentUI = gp.percentUI;
                drawTime = "Time: " + (double) Math.round(gp.deltaDraw / 100_0F) / 100 + "ms";
                collisionCount = "Collisions: " + gp.collisionCount;
                noDebugDrawFrame = 0;
            }
            noDebugDrawFrame ++;
            g2.setFont(g2.getFont().deriveFont(15F));
            g2.drawString(collisionCount, Math.round(gp.getSize().width - g2.getFontMetrics().getStringBounds(collisionCount, g2).getWidth()), Math.round(gp.getSize().getHeight() - 8 * g2.getFontMetrics().getStringBounds("empTy", g2).getHeight()));
            g2.drawString(drawTime, Math.round(gp.getSize().width - g2.getFontMetrics().getStringBounds(drawTime, g2).getWidth()), Math.round(gp.getSize().getHeight() - 7 * g2.getFontMetrics().getStringBounds("empTy", g2).getHeight()));
            g2.drawString(percentVignette, Math.round(gp.getSize().width - g2.getFontMetrics().getStringBounds(percentVignette, g2).getWidth()), Math.round(gp.getSize().getHeight() - 6 * g2.getFontMetrics().getStringBounds("empTy", g2).getHeight()));
            g2.drawString(percentEnvironment, Math.round(gp.getSize().width - g2.getFontMetrics().getStringBounds(percentEnvironment, g2).getWidth()), Math.round(gp.getSize().getHeight() - 5 * g2.getFontMetrics().getStringBounds("empTy", g2).getHeight()));
            g2.drawString(percentObjects, Math.round(gp.getSize().width - g2.getFontMetrics().getStringBounds(percentObjects, g2).getWidth()), Math.round(gp.getSize().getHeight() - 4 * g2.getFontMetrics().getStringBounds("empTy", g2).getHeight()));
            g2.drawString(percentPlayer, Math.round(gp.getSize().width - g2.getFontMetrics().getStringBounds(percentPlayer, g2).getWidth()), Math.round(gp.getSize().getHeight() - 3 * g2.getFontMetrics().getStringBounds("empTy", g2).getHeight()));
            g2.drawString(percentTiles, Math.round(gp.getSize().width - g2.getFontMetrics().getStringBounds(percentTiles, g2).getWidth()), Math.round(gp.getSize().getHeight() - 2 * g2.getFontMetrics().getStringBounds("empTy", g2).getHeight()));
            g2.drawString(percentUI, Math.round(gp.getSize().width - g2.getFontMetrics().getStringBounds(percentUI, g2).getWidth()), Math.round(gp.getSize().getHeight() - 1 * g2.getFontMetrics().getStringBounds("empTy", g2).getHeight()));

        }
    }

    /**
     * creates a dialogue field with the specified text auto generates size!
     * @param x the x position
     * @param y the y position
     * @param text the text to display
     */
    public void createDialogue(int x, int y, String text){
        if (dialogueX == null && dialogueY == null && dialogueWidth == null && dialogueHeight == null && dialogueText == null && !setDialogueDisplayed) {
            g2.setFont(g2.getFont().deriveFont(gp.TILESIZE / 3F));
            int width       = (int) Math.max((Math.ceil(g2.getFontMetrics().getStringBounds(text, g2).getWidth() + gp.TILESIZE / 3D + gp.TILESIZE / 16D) / gp.TILESIZE) + 1, 2);
            int height      = (int) Math.max((Math.round(g2.getFontMetrics().getStringBounds(text, g2).getHeight() + gp.TILESIZE / 2D + gp.TILESIZE / 16D) / gp.TILESIZE),2);
            dialogueWidth   = width;
            dialogueHeight  = height;
            dialogueX = x;
            dialogueY = y;
            setDialogueDisplayed = true;
            dialogueText = new String[1];
            dialogueText[0] = text;
        }
    }

    /**
     * creates a dialogue field with the specified lines. Each array field will be displayed in its own line! auto generates size!
     * @param x the x position
     * @param y the y position
     * @param text the lines to display
     */
    public void createDialogue(int x, int y, String[] text){
        if (dialogueX == null && dialogueY == null && dialogueWidth == null && dialogueHeight == null && dialogueText == null && !setDialogueDisplayed) {
            g2.setFont(g2.getFont().deriveFont(gp.TILESIZE / 3F));
            int width       = (int) Math.max((Math.ceil(g2.getFontMetrics().getStringBounds(text[1], g2).getWidth() + gp.TILESIZE / 3D + gp.TILESIZE / 16D) / gp.TILESIZE) + 1, 2);
            int height      = (int) Math.max((Math.round(g2.getFontMetrics().getStringBounds(text[1], g2).getHeight() + gp.TILESIZE / 2D + gp.TILESIZE / 16D) / gp.TILESIZE) * text.length,2);
            dialogueWidth   = width;
            dialogueHeight  = height;
            dialogueX = x;
            dialogueY = y;
            setDialogueDisplayed = true;
            System.arraycopy(text, 0, dialogueText, 0, text.length);
        }
    }

    /**
     * draws the dialogue field if possible
     */
    public void drawAllDialogue(){
        if (dialogueX != null && dialogueY != null && dialogueWidth != null && dialogueHeight != null && dialogueText != null) {
            if (! ((dialogueWidth < 2 || dialogueWidth * gp.TILESIZE > gp.getWidth()) && (dialogueY < 0 || dialogueY + dialogueHeight * gp.TILESIZE > gp.getHeight()) && (dialogueHeight < 2 || dialogueHeight * gp.TILESIZE > gp.getHeight()) && (dialogueX < 0 || dialogueX + dialogueWidth * gp.TILESIZE > gp.getWidth()))) {
                if (setDialogueDisplayed) {
                    g2.setFont(g2.getFont().deriveFont(gp.TILESIZE / 3F));
                    assert dialogueX != null;
                    assert dialogueY != null;
                    assert dialogueWidth != null;
                    assert dialogueHeight != null;
                    assert dialogueText != null;
                    for (int currY = 0; currY < dialogueHeight; currY++) {
                        for (int currX = 0; currX < dialogueWidth; currX++) {
                            //region draw
                            if (currX == 0 && currY == 0)
                                g2.drawImage(dialogueField.topLeft, dialogueX, dialogueY, gp.TILESIZE, gp.TILESIZE, null);
                            else if (currX == dialogueWidth - 1 && currY == 0)
                                g2.drawImage(dialogueField.topRight, dialogueX + currX * gp.TILESIZE, dialogueY, gp.TILESIZE, gp.TILESIZE, null);
                            else if (currY == dialogueHeight - 1 && currX == 0)
                                g2.drawImage(dialogueField.bottomLeft, dialogueX, dialogueY + currY * gp.TILESIZE, gp.TILESIZE, gp.TILESIZE, null);
                            else if (currY == dialogueHeight - 1 && currX == dialogueWidth - 1)
                                g2.drawImage(dialogueField.bottomRight, dialogueX + currX * gp.TILESIZE, dialogueY + currY * gp.TILESIZE, gp.TILESIZE, gp.TILESIZE, null);
                            else if (currX == 0)
                                g2.drawImage(dialogueField.middleLeft, dialogueX, dialogueY + currY * gp.TILESIZE, gp.TILESIZE, gp.TILESIZE, null);
                            else if (currY == 0)
                                g2.drawImage(dialogueField.middleTop, dialogueX + currX * gp.TILESIZE, dialogueY, gp.TILESIZE, gp.TILESIZE, null);
                            else if (currY == dialogueHeight - 1)
                                g2.drawImage(dialogueField.middleBottom, dialogueX + currX * gp.TILESIZE, dialogueY + currY * gp.TILESIZE, gp.TILESIZE, gp.TILESIZE, null);
                            else if (currX == dialogueWidth - 1)
                                g2.drawImage(dialogueField.middleRight, dialogueX + currX * gp.TILESIZE, dialogueY + currY * gp.TILESIZE, gp.TILESIZE, gp.TILESIZE, null);
                            else
                                g2.drawImage(dialogueField.middle, dialogueX + currX * gp.TILESIZE, dialogueY + currY * gp.TILESIZE, gp.TILESIZE, gp.TILESIZE, null);
                            //endregion
                        }
                    }
                    for (int i = 0; i < dialogueText.length; i++) {
                        g2.drawString(dialogueText[i], dialogueX + gp.TILESIZE / 3 + gp.TILESIZE / 32, (int) (dialogueY + gp.TILESIZE / 2 + gp.TILESIZE / 16 + i * g2.getFontMetrics().getStringBounds(dialogueText[i], g2).getHeight()));
                    }
                }
            }
        }
    }

    /**
     * resets all the variables used for the dialogue field.
     */
    public void destroyDialogue(){
        setDialogueDisplayed = false;
        dialogueY = null;
        dialogueX = null;
        dialogueWidth = null;
        dialogueHeight = null;
        dialogueText = null;
    }

    /**
     * adds dialogue text lines to the currently displayed dialogue field!
     * @param lines the lines to add
     */
    public void addDialogueText(String[] lines){
        if (dialogueX != null && dialogueY != null && dialogueWidth != null && dialogueHeight != null && dialogueText != null && setDialogueDisplayed) {
            g2.setFont(g2.getFont().deriveFont(gp.TILESIZE / 3F));
            Logger.log((Math.ceil(g2.getFontMetrics().getStringBounds(lines[0], g2).getHeight() + gp.TILESIZE / 2D + gp.TILESIZE / 16D) / gp.TILESIZE) + "");
            int width       = (int) Math.max((Math.ceil(g2.getFontMetrics().getStringBounds(lines[0], g2).getWidth() + gp.TILESIZE / 3D + gp.TILESIZE / 16D) / gp.TILESIZE) + 1, 2);
            int height      = (int) Math.max((Math.round(g2.getFontMetrics().getStringBounds(lines[0], g2).getHeight() + gp.TILESIZE / 2D + gp.TILESIZE / 16D) / gp.TILESIZE) * dialogueText.length + lines.length,2);
            dialogueWidth   = width;
            dialogueHeight  = height;
            setDialogueDisplayed = true;
            dialogueText = ArrayHelper.combineArrays(dialogueText, lines);
            Logger.log(Arrays.toString(dialogueText) + "");
        }
    }

    /**
     * for a given input String returns the dimensions in px that the Dialogue will use up!
     * @param text the String input to get the dimension for!
     * @return the width and height as a Dimension which the Dialogue-field will have
     */
    public Dimension getDialogueDimension(String text){
        int width = (int) Math.max((Math.ceil(g2.getFontMetrics().getStringBounds(text, g2).getWidth() + gp.TILESIZE / 3D + gp.TILESIZE / 16D) / gp.TILESIZE) + 1, 2);
        int height = (int) Math.max((Math.round(g2.getFontMetrics().getStringBounds(text, g2).getHeight() + gp.TILESIZE / 2D + gp.TILESIZE / 16D) / gp.TILESIZE),2);
        return new Dimension(width,height);
    }
}
