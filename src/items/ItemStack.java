package items;

import org.jetbrains.annotations.NotNull;

public class ItemStack {
    /**
     * the amount of items
     */
    int stackSize;
    /**
     * the type of the itemstack
     */
    String type;

    /**
     * constructor for an itemstack
     * @param type the type of the itemstack
     * @param amount the number of items in the itemstack
     */
    public ItemStack(@NotNull String type, int amount){
        this.type = type;
        stackSize = amount;
    }

    /**
     * methode for getting the type of the itemstack
     * @return the type of the given itemstack
     */
    public String getType(){
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
     * overrides the type for the itemstack
     * @param type the new type for the itemstack
     */
    public void setType(String type){
        this.type = type;
    }
}
