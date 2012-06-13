package net.minecraft.src;

import java.util.*;

public abstract class MagicContainer
{
    /** the list of all magics(stacks) for the corresponding magicslot */
    public List inventoryMagicStacks;

    /** the list of all magicslots in the inventory */
    public List inventoryMagicSlots;
    public int windowId;
    private short transactionID;

    /**
     * list of all people that need to be notified when this craftinventory changes
     */
    private Set field_20918_b;

    public MagicContainer()
    {
        inventoryMagicStacks = new ArrayList();
        inventoryMagicSlots = new ArrayList();
        windowId = 0;
        transactionID = 0;
        field_20918_b = new HashSet();
    }

    /**
     * adds the magicslot to the inventory it is in
     */
    protected void addMagicSlot(MagicSlot par1MagicSlot)
    {
        par1MagicSlot.slotNumber = inventoryMagicSlots.size();
        inventoryMagicSlots.add(par1MagicSlot);
        inventoryMagicStacks.add(null);
    }

    /**
     * Updates crafting matrix; called from onCraftMatrixChanged. Args: none
     */
    public void updateCraftingResults()
    {

    }

    /**
     * enchants the magic on the table using the specified magicslot; also deducts XP from player
     */
    public boolean enchantMagic(EntityPlayer par1EntityPlayer, int par2)
    {
        return false;
    }

    public MagicSlot getMagicSlot(int par1)
    {
        return (MagicSlot)inventoryMagicSlots.get(par1);
    }

    /**
     * Called to transfer a stack from one inventory to the other eg. when shift clicking.
     */
    public MagicStack transferStackInMagicSlot(int par1)
    {
        MagicSlot magicslot = (MagicSlot)inventoryMagicSlots.get(par1);

        if (magicslot != null)
        {
            return magicslot.getStack();
        }
        else
        {
            return null;
        }
    }

    public MagicStack magicslotClick(int par1, int par2, boolean par3, EntityPlayer par4EntityPlayer)
    {
        MagicStack magicstack = null;

        if (par2 > 1)
        {
            return null;
        }

        if (par2 == 0 || par2 == 1)
        {
            MagicInventoryPlayer inventoryplayer = par4EntityPlayer.magicinventory;

            if (par1 == -999)
            {
                if (inventoryplayer.getMagicStack() != null && par1 == -999)
                {
                    if (par2 == 0)
                    {
                        //par4EntityPlayer.dropPlayerItem(inventoryplayer.getMagicStack());
                        inventoryplayer.setMagicStack(null);
                    }

                    if (par2 == 1)
                    {
                        //par4EntityPlayer.dropPlayerMagic(inventoryplayer.getMagicStack().splitStack(1));

                        if (inventoryplayer.getMagicStack().stackSize == 0)
                        {
                            inventoryplayer.setMagicStack(null);
                        }
                    }
                }
            }
            else if (par3)
            {
                MagicStack magicstack1 = transferStackInMagicSlot(par1);

                if (magicstack1 != null)
                {
                    int i = magicstack1.magicID;
                    magicstack = magicstack1.copy();
                    MagicSlot magicslot1 = (MagicSlot)inventoryMagicSlots.get(par1);

                    if (magicslot1 != null && magicslot1.getStack() != null && magicslot1.getStack().magicID == i)
                    {
                        retryMagicSlotClick(par1, par2, par3, par4EntityPlayer);
                    }
                }
            }
            else
            {
                if (par1 < 0)
                {
                    return null;
                }

                MagicSlot magicslot = (MagicSlot)inventoryMagicSlots.get(par1);

                if (magicslot != null)
                {
                    magicslot.onMagicSlotChanged();
                    MagicStack magicstack2 = magicslot.getStack();
                    MagicStack magicstack4 = inventoryplayer.getMagicStack();

                    if (magicstack2 != null)
                    {
                        magicstack = magicstack2.copy();
                    }

                    if (magicstack2 == null)
                    {
                        if (magicstack4 != null && magicslot.isMagicValid(magicstack4))
                        {
                            int j = par2 != 0 ? 1 : magicstack4.stackSize;

                            if (j > magicslot.getMagicSlotStackLimit())
                            {
                                j = magicslot.getMagicSlotStackLimit();
                            }

                            magicslot.putStack(magicstack4.splitStack(j));

                            if (magicstack4.stackSize == 0)
                            {
                                inventoryplayer.setMagicStack(null);
                            }
                        }
                    }
                    else if (magicstack4 == null)
                    {
                        int k = par2 != 0 ? (magicstack2.stackSize + 1) / 2 : magicstack2.stackSize;
                        MagicStack magicstack6 = magicslot.decrStackSize(k);
                        inventoryplayer.setMagicStack(magicstack6);

                        if (magicstack2.stackSize == 0)
                        {
                            magicslot.putStack(null);
                        }

                        magicslot.onPickupFromMagicSlot(inventoryplayer.getMagicStack());
                    }
                    else if (magicslot.isMagicValid(magicstack4))
                    {
                        if (magicstack2.magicID != magicstack4.magicID || magicstack2.getHasSubtypes() && magicstack2.getMagicDamage() != magicstack4.getMagicDamage() || !MagicStack.func_46154_a(magicstack2, magicstack4))
                        {
                            if (magicstack4.stackSize <= magicslot.getMagicSlotStackLimit())
                            {
                                MagicStack magicstack5 = magicstack2;
                                magicslot.putStack(magicstack4);
                                inventoryplayer.setMagicStack(magicstack5);
                            }
                        }
                        else
                        {
                            int l = par2 != 0 ? 1 : magicstack4.stackSize;

                            if (l > magicslot.getMagicSlotStackLimit() - magicstack2.stackSize)
                            {
                                l = magicslot.getMagicSlotStackLimit() - magicstack2.stackSize;
                            }

                            if (l > magicstack4.getMaxStackSize() - magicstack2.stackSize)
                            {
                                l = magicstack4.getMaxStackSize() - magicstack2.stackSize;
                            }

                            magicstack4.splitStack(l);

                            if (magicstack4.stackSize == 0)
                            {
                                inventoryplayer.setMagicStack(null);
                            }

                            magicstack2.stackSize += l;
                        }
                    }
                    else if (magicstack2.magicID == magicstack4.magicID && magicstack4.getMaxStackSize() > 1 && (!magicstack2.getHasSubtypes() || magicstack2.getMagicDamage() == magicstack4.getMagicDamage()) && MagicStack.func_46154_a(magicstack2, magicstack4))
                    {
                        int i1 = magicstack2.stackSize;

                        if (i1 > 0 && i1 + magicstack4.stackSize <= magicstack4.getMaxStackSize())
                        {
                            magicstack4.stackSize += i1;
                            MagicStack magicstack3 = magicslot.decrStackSize(i1);

                            if (magicstack3.stackSize == 0)
                            {
                                magicslot.putStack(null);
                            }

                            magicslot.onPickupFromMagicSlot(inventoryplayer.getMagicStack());
                        }
                    }
                }
            }
        }

        return magicstack;
    }

    protected void retryMagicSlotClick(int par1, int par2, boolean par3, EntityPlayer par4EntityPlayer)
    {
        magicslotClick(par1, par2, par3, par4EntityPlayer);
    }

    /**
     * Callback for when the crafting gui is closed.
     */
    public void onCraftGuiClosed(EntityPlayer par1EntityPlayer)
    {
        MagicInventoryPlayer inventoryplayer = par1EntityPlayer.magicinventory;

        if (inventoryplayer.getMagicStack() != null)
        {
            //par1EntityPlayer.dropPlayerMagic(inventoryplayer.getMagicStack());
            inventoryplayer.setMagicStack(null);
        }
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    public void onCraftMatrixChanged(IInventory par1IInventory)
    {
        updateCraftingResults();
    }

    /**
     * args: magicslotID, magicStack to put in magicslot
     */
    public void putStackInMagicSlot(int par1, MagicStack par2MagicStack)
    {
        getMagicSlot(par1).putStack(par2MagicStack);
    }

    /**
     * places magicstacks in first x magicslots, x being amagicstack.lenght
     */
    public void putStacksInMagicSlots(MagicStack par1ArrayOfMagicStack[])
    {
        for (int i = 0; i < par1ArrayOfMagicStack.length; i++)
        {
            getMagicSlot(i).putStack(par1ArrayOfMagicStack[i]);
        }
    }

    public void updateProgressBar(int i, int j)
    {
    }

    /**
     * Gets a unique transaction ID. Parameter is unused.
     */
    public short getNextTransactionID(InventoryPlayer par1InventoryPlayer)
    {
        transactionID++;
        return transactionID;
    }

    public void func_20113_a(short word0)
    {
    }

    public void func_20110_b(short word0)
    {
    }

    public abstract boolean canInteractWith(EntityPlayer entityplayer);

    /**
     * merges provided MagicStack with the first avaliable one in the container/player inventory
     */
    protected boolean mergeMagicStack(MagicStack par1MagicStack, int par2, int par3, boolean par4)
    {
        boolean flag = false;
        int i = par2;

        if (par4)
        {
            i = par3 - 1;
        }

        if (par1MagicStack.isStackable())
        {
            while (par1MagicStack.stackSize > 0 && (!par4 && i < par3 || par4 && i >= par2))
            {
                MagicSlot magicslot = (MagicSlot)inventoryMagicSlots.get(i);
                MagicStack magicstack = magicslot.getStack();

                if (magicstack != null && magicstack.magicID == par1MagicStack.magicID && (!par1MagicStack.getHasSubtypes() || par1MagicStack.getMagicDamage() == magicstack.getMagicDamage()) && MagicStack.func_46154_a(par1MagicStack, magicstack))
                {
                    int k = magicstack.stackSize + par1MagicStack.stackSize;

                    if (k <= par1MagicStack.getMaxStackSize())
                    {
                        par1MagicStack.stackSize = 0;
                        magicstack.stackSize = k;
                        magicslot.onMagicSlotChanged();
                        flag = true;
                    }
                    else if (magicstack.stackSize < par1MagicStack.getMaxStackSize())
                    {
                        par1MagicStack.stackSize -= par1MagicStack.getMaxStackSize() - magicstack.stackSize;
                        magicstack.stackSize = par1MagicStack.getMaxStackSize();
                        magicslot.onMagicSlotChanged();
                        flag = true;
                    }
                }

                if (par4)
                {
                    i--;
                }
                else
                {
                    i++;
                }
            }
        }

        if (par1MagicStack.stackSize > 0)
        {
            int j;

            if (par4)
            {
                j = par3 - 1;
            }
            else
            {
                j = par2;
            }

            do
            {
                if ((par4 || j >= par3) && (!par4 || j < par2))
                {
                    break;
                }

                MagicSlot magicslot1 = (MagicSlot)inventoryMagicSlots.get(j);
                MagicStack magicstack1 = magicslot1.getStack();

                if (magicstack1 == null)
                {
                    magicslot1.putStack(par1MagicStack.copy());
                    magicslot1.onMagicSlotChanged();
                    par1MagicStack.stackSize = 0;
                    flag = true;
                    break;
                }

                if (par4)
                {
                    j--;
                }
                else
                {
                    j++;
                }
            }
            while (true);
        }

        return flag;
    }
}
