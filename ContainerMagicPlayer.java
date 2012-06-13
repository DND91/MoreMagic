package net.minecraft.src;

import java.util.List;

public class ContainerMagicPlayer extends MagicContainer
{
    /** The crafting matrix inventory. */

    /** Determines if inventory manipulation should be handled. */
    public boolean isLocalWorld;

    public ContainerMagicPlayer(MagicInventoryPlayer par1InventoryPlayer)
    {
        this(par1InventoryPlayer, true);
    }

    public ContainerMagicPlayer(MagicInventoryPlayer par1InventoryPlayer, boolean par2)
    {
        isLocalWorld = false;
        isLocalWorld = par2;


        for (int k = 0; k < 3; k++)
        {
            for (int k1 = 0; k1 < 9; k1++)
            {
            	addMagicSlot(new MagicSlot(par1InventoryPlayer, k1 + (k + 1) * 9, 8 + k1 * 18, 84 + k * 18));
            }
        }

        for (int l = 0; l < 9; l++)
        {
        	addMagicSlot(new MagicSlot(par1InventoryPlayer, l, 8 + l * 18, 142));
        }

    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    public void onCraftMatrixChanged(IInventory par1IInventory)
    {
        
    }

    /**
     * Callback for when the crafting gui is closed.
     */
    public void onCraftGuiClosed(EntityPlayer par1EntityPlayer)
    {
    	
    }

    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return true;
    }

    /**
     * Called to transfer a stack from one inventory to the other eg. when shift clicking.
     */
    public MagicStack transferStackInSlot(int par1)
    {
        MagicStack magicstack = null;
        MagicSlot slot = (MagicSlot)inventoryMagicSlots.get(par1);

        if (slot != null && slot.getHasStack())
        {
            MagicStack magicstack1 = slot.getStack();
            magicstack = magicstack1.copy();

            if (par1 == 0)
            {
                if (!mergeMagicStack(magicstack1, 9, 45, true))
                {
                    return null;
                }

                slot.func_48433_a(magicstack1, magicstack);
            }
            else if (par1 >= 9 && par1 < 36)
            {
                if (!mergeMagicStack(magicstack1, 36, 45, false))
                {
                    return null;
                }
            }
            else if (par1 >= 36 && par1 < 45)
            {
                if (!mergeMagicStack(magicstack1, 9, 36, false))
                {
                    return null;
                }
            }
            else if (!mergeMagicStack(magicstack1, 9, 45, false))
            {
                return null;
            }

            if (magicstack1.stackSize == 0)
            {
                slot.putStack(null);
            }
            else
            {
                slot.onMagicSlotChanged();
            }

            if (magicstack1.stackSize != magicstack.stackSize)
            {
                slot.onPickupFromMagicSlot(magicstack1);
            }
            else
            {
                return null;
            }
        }

        return magicstack;
    }
}
