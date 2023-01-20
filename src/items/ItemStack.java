package items;

import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class ItemStack {
    /**
     * the amount of items
     */
    int stackSize;
    /**
     * the type of the itemstack
     */
    ITEM_TYPE type;
    BufferedImage key;
    BufferedImage chest;

    /**
     * constructor for an itemstack
     * @param type the type of the itemstack
     * @param amount the number of items in the itemstack
     */
    public ItemStack(@NotNull ITEM_TYPE type, int amount){
        this.type = type;
        stackSize = amount;
        try {
            key = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/object/key.png")));
            chest = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/object/chest.png")));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * methode for getting the type of the itemstack
     * @return the type of the given itemstack
     */
    public ITEM_TYPE getType(){
        return type;
    }

    /**
     * methode for getting the number of items of the itemstack
     * @return the amount of items that the itemstack has
     */
    public int getStackSize(){
        return stackSize;
    }

    /**
     * sets the stack size for the item stack
     * @param stackSize the new stack size
     */
    public void setStackSize(int stackSize){
        this.stackSize = stackSize;
    }

    /**
     * overrides the type for the itemstack
     * @param type the new type for the itemstack
     */
    public void setType(ITEM_TYPE type){
        this.type = type;
    }

    public BufferedImage getImage(){
        switch (type){
            case KEY -> {return key;}
            case CHEST -> {return chest;}
        }
        return null;
    }
}
