package net.minecraft.src;

public interface IMagicInventory {
    /**
     * Returns the number of slots in the inventory.
     */
    public abstract int getSizeInventory();

    /**
     * Returns the stack in slot i
     */
    public abstract MagicStack getStackInSlot(int i);

    /**
     * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
     * stack.
     */
    public abstract MagicStack decrStackSize(int i, int j);

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityMagic -
     * like when you close a workbench GUI.
     */
    
    public abstract MagicStack getStackInSlotOnClosing(int i);

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public abstract void setInventorySlotContents(int i, MagicStack magicstack);

    /**
     * Returns the name of the inventory.
     */
    public abstract String getInvName();

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    public abstract int getInventoryStackLimit();

    /**
     * Called when an the contents of an Inventory change, usually
     */
    public abstract void onInventoryChanged();

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public abstract boolean isUseableByPlayer(EntityPlayer entityplayer);

    public abstract void openChest();

    public abstract void closeChest();
}
