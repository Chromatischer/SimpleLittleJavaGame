package inventory;

import items.ITEM_TYPE;
import items.ItemStack;

import java.util.ArrayList;
import java.util.List;


public class Inventory {
    /**
     * array containing all inventory items
     */
    ItemStack[] itemStacks;
    /**
     * type of inventory e.g: player, chest
     */
    INVENTORY_TYPE invType;
    public Inventory(int size, INVENTORY_TYPE invType){
        this.invType = invType;
        itemStacks = new ItemStack[size];
        for (int i = 0; i < itemStacks.length; i++) {
            itemStacks[i] = new ItemStack(ITEM_TYPE.AIR, 0);
        }
    }

    /**
     * gets the itemstack from a given slot
     * @param slot the itemstack to get
     * @return the itemstack for the given slot, null if non found
     */
    public ItemStack getItemstack(int slot){
        return itemStacks[slot];
    }

    /**
     * adds an itemstack to the inventory at the first free slot if there is any
     * @param itemStack the itemstack to put into the inventory
     */
    public void addItemStack(ItemStack itemStack){
        for (int i = 0; i < itemStacks.length; i++) {
            if (itemStacks[i].getType().equals(ITEM_TYPE.AIR) && itemStacks[i].getStackSize() == 0){
                itemStacks[i] = itemStack;
                break;
            } else if (itemStacks[i].getType() == itemStack.getType()){
                int oldStackSize = itemStacks[i].getStackSize();
                int newStackSize = oldStackSize + itemStack.getStackSize();
                itemStacks[i].setStackSize(newStackSize);
                break;
            }
        }
    }

    /**
     * sets the itemstack at a given position for the inventory
     * @param itemStack the itemstack to set
     * @param place the slot to put the itemstack in
     */
    public void setItemStack(ItemStack itemStack, int place){
        itemStacks[place] = itemStack;
    }

    /**
     * sets the size of an Inventory after initial creation
     * <p>
     *     note: if the array beforehand had contained more itemstacks the may be deleted!
     * </p>
     * @param size the new size
     */
    public void setSize(int size) {
        List<ItemStack> buffer = new ArrayList<>();
        //copying all not dummy items into new list
        for (ItemStack itemStack : itemStacks) {
            if (!itemStack.getType().equals(ITEM_TYPE.AIR) && itemStack.getStackSize() != 0) {
                buffer.add(itemStack);
            }
        }
        //converting into array for arraycopy
        ItemStack[] bufferArray = buffer.toArray(new ItemStack[0]);
        //setting new array size
        itemStacks = new ItemStack[size];
        System.arraycopy(bufferArray, 0, itemStacks, 0, bufferArray.length);
        //filling all now null spaces with dummy items
        for (int i = 0; i < itemStacks.length; i++){
            if (itemStacks[i] == null){
                itemStacks[i] = new ItemStack(ITEM_TYPE.AIR, 0);
            }
        }
    }

    /**
     * returns all the item types of an inventory as a String
     * <p>
     *     for debug purposes
     * </p>
     * @return a String with all item types
     */
    public String getAllTypesAsSting(){
        StringBuilder value = new StringBuilder();
        value.append("[");
        if (itemStacks.length != 0) {
            for (ItemStack itemStack : itemStacks) {
                if (itemStack != null) {
                    value.append(itemStack.getType()).append(" ");
                } else {
                    value.append("null").append(" ");
                }
            }
        } else {
            return "null";
        }
        value.append("]");
        return value.toString();
    }

    /**
     * gets the size of the inventory
     * @return the size of the inventory in question
     */
    public int getSize(){
        return itemStacks.length;
    }

    /**
     * gets the type of inventory
     * @return the inventory type
     */
    public INVENTORY_TYPE getType(){
        return invType;
    }
}
