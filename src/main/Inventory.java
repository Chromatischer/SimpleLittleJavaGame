package main;

import items.ItemStack;


public class Inventory {
    ItemStack[] itemStacks;
    int size;
    public Inventory(int size, String type){
        this.size = size;
        itemStacks = new ItemStack[size];
        for (int i = 0; i < itemStacks.length; i++) {
            itemStacks[i] = new ItemStack("air", 0);
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
            if (itemStacks[i].getType() == null){
                itemStacks[i] = itemStack;
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
    //TODO: figure this shit out I am to tired for any of this!
    public void setSize(int size) {
        itemStacks = new ItemStack[size];
        for (int i = 0; i < itemStacks.length; i++) {
            itemStacks[i] = new ItemStack("air", 0);
        }
    }
    public String getAllTypesAsSting(){
        StringBuilder value = new StringBuilder();
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
        return value.toString();
    }

    /**
     * gets the amount of itemstacks that can be stored in a given inventory
     * @return the size of the inventory
     */
    public int getSize(){
        return itemStacks.length;
    }
}
