package net.minecraft.src;

public class MagicSlot
{
    /** The index of the slot in the inventory. */
    private final int slotIndex;

    /** The inventory we want to extract a slot from. */
    public final IMagicInventory inventory;

    /** the id of the slot(also the index in the inventory arraylist) */
    public int slotNumber;

    /** display position of the inventory slot on the screen x axis */
    public int xDisplayPosition;

    /** display position of the inventory slot on the screen y axis */
    public int yDisplayPosition;

    public MagicSlot(IMagicInventory par1IInventory, int par2, int par3, int par4)
    {
        inventory = par1IInventory;
        slotIndex = par2;
        xDisplayPosition = par3;
        yDisplayPosition = par4;
    }

    public void func_48433_a(MagicStack par1MagicStack, MagicStack par2MagicStack)
    {
        if (par1MagicStack == null || par2MagicStack == null)
        {
            return;
        }

        if (par1MagicStack.magicID != par2MagicStack.magicID)
        {
            return;
        }

        int i = par2MagicStack.stackSize - par1MagicStack.stackSize;

        if (i > 0)
        {
            func_48435_a(par1MagicStack, i);
        }
    }

    protected void func_48435_a(MagicStack magicstack, int i)
    {
    }

    protected void func_48434_c(MagicStack magicstack)
    {
    }

    /**
     * Called when the player picks up an magic from an inventory slot
     */
    public void onPickupFromMagicSlot(MagicStack par1MagicStack)
    {
        onMagicSlotChanged();
    }

    /**
     * Check if the stack is a valid magic for this slot. Always true beside for the armor slots.
     */
    public boolean isMagicValid(MagicStack par1MagicStack)
    {
        return true;
    }

    /**
     * Helper fnct to get the stack in the slot.
     */
    public MagicStack getStack()
    {
        return inventory.getStackInSlot(slotIndex);
    }

    /**
     * Returns if this slot contains a stack.
     */
    public boolean getHasStack()
    {
        return getStack() != null;
    }

    /**
     * Helper method to put a stack in the slot.
     */
    public void putStack(MagicStack par1MagicStack)
    {
        inventory.setInventorySlotContents(slotIndex, par1MagicStack);
        onMagicSlotChanged();
    }

    /**
     * Called when the stack in a MagicSlot changes
     */
    public void onMagicSlotChanged()
    {
        inventory.onInventoryChanged();
    }

    /**
     * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the case
     * of armor slots)
     */
    public int getMagicSlotStackLimit()
    {
        return inventory.getInventoryStackLimit();
    }

    /**
     * Returns the icon index on magics.png that is used as background image of the slot.
     */
    public int getBackgroundIconIndex()
    {
        return -1;
    }

    /**
     * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
     * stack.
     */
    public MagicStack decrStackSize(int par1)
    {
        return inventory.decrStackSize(slotIndex, par1);
    }
}