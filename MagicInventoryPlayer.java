package net.minecraft.src;

public class MagicInventoryPlayer implements IMagicInventory
{
    public MagicStack mainInventory[];

    /** The index of the currently held magic (0-8). */
    public int currentMagic;

    /** The player whose inventory this is. */
    public EntityPlayer player;
    private MagicStack magicStack;

    /**
     * Set true whenever the inventory changes. Nothing sets it false so you will have to write your own code to check
     * it and reset the value.
     */
    public boolean inventoryChanged;

    public MagicInventoryPlayer(EntityPlayer par1EntityPlayer)
    {
        mainInventory = new MagicStack[36];
        mainInventory[0] = new MagicStack(mod_MoreMagic.magicTest);
        currentMagic = 0;
        inventoryChanged = false;
        player = par1EntityPlayer;
    }

    /**
     * Returns the magic stack currently held by the player.
     */
    public MagicStack getCurrentMagic()
    {
        if (currentMagic < 9 && currentMagic >= 0)
        {
            return mainInventory[currentMagic];
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns a slot index in main inventory containing a specific magicID
     */
    private int getInventorySlotContainMagic(int par1)
    {
        for (int i = 0; i < mainInventory.length; i++)
        {
            if (mainInventory[i] != null && mainInventory[i].magicID == par1)
            {
                return i;
            }
        }

        return -1;
    }

    private int getInventorySlotContainMagicAndDamage(int par1, int par2)
    {
        for (int i = 0; i < mainInventory.length; i++)
        {
            if (mainInventory[i] != null && mainInventory[i].magicID == par1 && mainInventory[i].getMagicDamage() == par2)
            {
                return i;
            }
        }

        return -1;
    }

    /**
     * stores an magicstack in the users inventory
     */
    private int storeMagicStack(MagicStack par1MagicStack)
    {
        for (int i = 0; i < mainInventory.length; i++)
        {
            if (mainInventory[i] != null && mainInventory[i].magicID == par1MagicStack.magicID && mainInventory[i].isStackable() && mainInventory[i].stackSize < mainInventory[i].getMaxStackSize() && mainInventory[i].stackSize < getInventoryStackLimit() && (!mainInventory[i].getHasSubtypes() || mainInventory[i].getMagicDamage() == par1MagicStack.getMagicDamage()) && MagicStack.func_46154_a(mainInventory[i], par1MagicStack))
            {
                return i;
            }
        }

        return -1;
    }

    /**
     * Returns the first magic stack that is empty.
     */
    private int getFirstEmptyStack()
    {
        for (int i = 0; i < mainInventory.length; i++)
        {
            if (mainInventory[i] == null)
            {
                return i;
            }
        }

        return -1;
    }

    /**
     * Sets a specific magicID as the current magic being held (only if it exists on the hotbar)
     */
    public void setCurrentMagic(int par1, int par2, boolean par3, boolean par4)
    {
        int i = -1;

        if (par3)
        {
            i = getInventorySlotContainMagicAndDamage(par1, par2);
        }
        else
        {
            i = getInventorySlotContainMagic(par1);
        }

        if (i >= 0 && i < 9)
        {
            currentMagic = i;
            return;
        }

        if (par4 && par1 > 0)
        {
            int j = getFirstEmptyStack();

            if (j >= 0 && j < 9)
            {
            	currentMagic = j;
            }

            func_52006_a(Magic.magicList[par1], par2);
        }
    }

    /**
     * Switch the current magic to the next one or the previous one
     */
    public void changeCurrentMagic(int par1)
    {
        if (par1 > 0)
        {
            par1 = 1;
        }

        if (par1 < 0)
        {
            par1 = -1;
        }

        for (currentMagic -= par1; currentMagic < 0; currentMagic += 9) { }

        for (; currentMagic >= 9; currentMagic -= 9) { }
    }

    public void func_52006_a(Magic par1Magic, int par2)
    {
        if (par1Magic != null)
        {
            int i = getInventorySlotContainMagicAndDamage(par1Magic.shiftedIndex, par2);

            if (i >= 0)
            {
                mainInventory[i] = mainInventory[currentMagic];
            }

            mainInventory[currentMagic] = new MagicStack(Magic.magicList[par1Magic.shiftedIndex], 1, par2);
        }
    }

    /**
     * This function stores as many magics of an MagicStack as possible in a matching slot and returns the quantity of
     * left over magics.
     */
    private int storePartialMagicStack(MagicStack par1MagicStack)
    {
        int i = par1MagicStack.magicID;
        int j = par1MagicStack.stackSize;

        if (par1MagicStack.getMaxStackSize() == 1)
        {
            int k = getFirstEmptyStack();

            if (k < 0)
            {
                return j;
            }

            if (mainInventory[k] == null)
            {
                mainInventory[k] = MagicStack.copyMagicStack(par1MagicStack);
            }

            return 0;
        }

        int l = storeMagicStack(par1MagicStack);

        if (l < 0)
        {
            l = getFirstEmptyStack();
        }

        if (l < 0)
        {
            return j;
        }

        if (mainInventory[l] == null)
        {
            mainInventory[l] = new MagicStack(i, 0, par1MagicStack.getMagicDamage());
            if (par1MagicStack.hasTagCompound())
            {
                mainInventory[l].setTagCompound((NBTTagCompound)par1MagicStack.getTagCompound().copy());
            }
        }

        int i1 = j;

        if (i1 > mainInventory[l].getMaxStackSize() - mainInventory[l].stackSize)
        {
            i1 = mainInventory[l].getMaxStackSize() - mainInventory[l].stackSize;
        }

        if (i1 > getInventoryStackLimit() - mainInventory[l].stackSize)
        {
            i1 = getInventoryStackLimit() - mainInventory[l].stackSize;
        }

        if (i1 == 0)
        {
            return j;
        }
        else
        {
            j -= i1;
            mainInventory[l].stackSize += i1;
            mainInventory[l].animationsToGo = 5;
            return j;
        }
    }

    /**
     * Decrement the number of animations remaining. Only called on client side. This is used to handle the animation of
     * receiving a block.
     */
    public void decrementAnimations()
    {
        for (int i = 0; i < mainInventory.length; i++)
        {
            if (mainInventory[i] != null)
            {
                mainInventory[i].updateAnimation(player.worldObj, player, i, currentMagic == i);
            }
        }
    }

    /**
     * removed one magic of specified magicID from inventory (if it is in a stack, the stack size will reduce with 1)
     */
    public boolean consumeInventoryMagic(int par1)
    {
        int i = getInventorySlotContainMagic(par1);

        if (i < 0)
        {
            return false;
        }

        if (--mainInventory[i].stackSize <= 0)
        {
            mainInventory[i] = null;
        }

        return true;
    }

    /**
     * Get if a specifiied magic id is inside the inventory.
     */
    public boolean hasMagic(int par1)
    {
        int i = getInventorySlotContainMagic(par1);
        return i >= 0;
    }

    /**
     * Adds the magic stack to the inventory, returns false if it is impossible.
     */
    public boolean addMagicStackToInventory(MagicStack par1MagicStack)
    {
        if (!par1MagicStack.isMagicDamaged())
        {
            int i;

            do
            {
                i = par1MagicStack.stackSize;
                par1MagicStack.stackSize = storePartialMagicStack(par1MagicStack);
            }
            while (par1MagicStack.stackSize > 0 && par1MagicStack.stackSize < i);

            if (par1MagicStack.stackSize == i && player.capabilities.isCreativeMode)
            {
                par1MagicStack.stackSize = 0;
                return true;
            }
            else
            {
                return par1MagicStack.stackSize < i;
            }
        }

        int j = getFirstEmptyStack();

        if (j >= 0)
        {
            mainInventory[j] = MagicStack.copyMagicStack(par1MagicStack);
            mainInventory[j].animationsToGo = 5;
            par1MagicStack.stackSize = 0;
            return true;
        }

        if (player.capabilities.isCreativeMode)
        {
            par1MagicStack.stackSize = 0;
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
     * stack.
     */
    public MagicStack decrStackSize(int par1, int par2)
    {
        MagicStack magicstack[] = mainInventory;

        if (par1 >= mainInventory.length)
        {
            par1 -= mainInventory.length;
        }

        if (magicstack[par1] != null)
        {
            if (magicstack[par1].stackSize <= par2)
            {
                MagicStack magicstack1 = magicstack[par1];
                magicstack[par1] = null;
                return magicstack1;
            }

            MagicStack magicstack1 = magicstack[par1].splitStack(par2);

            if (magicstack[par1].stackSize == 0)
            {
                magicstack[par1] = null;
            }

            return magicstack1;
        }
        else
        {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityMagic -
     * like when you close a workbench GUI.
     */
    public MagicStack getStackInSlotOnClosing(int par1)
    {
        MagicStack magicstack[] = mainInventory;

        if (par1 >= mainInventory.length)
        {
            par1 -= mainInventory.length;
        }

        if (magicstack[par1] != null)
        {
            MagicStack magicstack1 = magicstack[par1];
            magicstack[par1] = null;
            return magicstack1;
        }
        else
        {
            return null;
        }
    }

    /**
     * Sets the given magic stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int i, MagicStack magicstack)
    {
        MagicStack magicstackA[] = mainInventory;

        if (i >= magicstackA.length)
        {
            i -= magicstackA.length;
        }

        magicstackA[i] = magicstack;
    }

    /**
     * Gets the strength of the current magic (tool) against the specified block, 1.0f if not holding anything.
     */
    public float getStrVsBlock(Block par1Block)
    {
        float f = 1.0F;

        if (mainInventory[currentMagic] != null)
        {
            f *= mainInventory[currentMagic].getStrVsBlock(par1Block);
        }

        return f;
    }

    /**
     * Writes the inventory out as a list of compound tags. This is where the slot indices are used (+100 for armor, +80
     * for crafting).
     */
    public NBTTagList writeToNBT(NBTTagList par1NBTTagList)
    {
        for (int i = 0; i < mainInventory.length; i++)
        {
            if (mainInventory[i] != null)
            {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                mainInventory[i].writeToNBT(nbttagcompound);
                par1NBTTagList.appendTag(nbttagcompound);
            }
        }

        return par1NBTTagList;
    }

    /**
     * Reads from the given tag list and fills the slots in the inventory with the correct magics.
     */
    public void readFromNBT(NBTTagList par1NBTTagList)
    {
        mainInventory = new MagicStack[36];

        for (int i = 0; i < par1NBTTagList.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound = (NBTTagCompound)par1NBTTagList.tagAt(i);
            int j = nbttagcompound.getByte("Slot") & 0xff;
            MagicStack magicstack = MagicStack.loadMagicStackFromNBT(nbttagcompound);

            if (magicstack == null)
            {
                continue;
            }

            if (j >= 0 && j < mainInventory.length)
            {
                mainInventory[j] = magicstack;
            }
        }
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return mainInventory.length + 4;
    }

    /**
     * Returns the stack in slot i
     */
    public MagicStack getStackInSlot(int par1)
    {
        MagicStack magicstack[] = mainInventory;

        if (par1 >= magicstack.length)
        {
            par1 -= magicstack.length;
        }

        return magicstack[par1];
    }

    /**
     * Returns the name of the inventory.
     */
    public String getInvName()
    {
        return "container.magicinventory";
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    /**
     * Return damage vs an entity done by the current held weapon, or 1 if nothing is held
     */
    public int getDamageVsEntity(Entity par1Entity)
    {
        MagicStack magicstack = getStackInSlot(currentMagic);

        if (magicstack != null)
        {
            return magicstack.getDamageVsEntity(par1Entity);
        }
        else
        {
            return 1;
        }
    }

    /**
     * Returns whether the current magic (tool) can harvest from the specified block (actually get a result).
     */
    public boolean canHarvestBlock(Block par1Block)
    {
        if (par1Block.blockMaterial.isHarvestable())
        {
            return true;
        }

        MagicStack magicstack = getStackInSlot(currentMagic);

        if (magicstack != null)
        {
            return magicstack.canHarvestBlock(par1Block);
        }
        else
        {
            return false;
        }
    }

    /**
     * returns a player armor magic (as magicstack) contained in specified armor slot.
     */
    public MagicStack armorMagicInSlot(int par1)
    {
        return null;
    }

    /**
     * Based on the damage values and maximum damage values of each armor magic, returns the current armor value.
     */
    public int getTotalArmorValue()
    {
        int i = 0;

        return i;
    }

    /**
     * Damages armor in each slot by the specified amount.
     */
    public void damageArmor(int par1)
    {

    }

    /**
     * Drop all armor and main inventory magics.
     */
    public void dropAllMagics()
    {

    }

    /**
     * Called when an the contents of an Inventory change, usually
     */
    public void onInventoryChanged()
    {
        inventoryChanged = true;
    }

    public void setMagicStack(MagicStack par1MagicStack)
    {
        magicStack = par1MagicStack;
        player.onMagicStackChanged(par1MagicStack);
    }

    public MagicStack getMagicStack()
    {
        return magicStack;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        if (player.isDead)
        {
            return false;
        }

        return par1EntityPlayer.getDistanceSqToEntity(player) <= 64D;
    }

    /**
     * Returns true if the specified MagicStack exists in the inventory.
     */
    public boolean hasMagicStack(MagicStack par1MagicStack)
    {
        for (int j = 0; j < mainInventory.length; j++)
        {
            if (mainInventory[j] != null && mainInventory[j].isStackEqual(par1MagicStack))
            {
                return true;
            }
        }

        return false;
    }

    public void openChest()
    {
    }

    public void closeChest()
    {
    }

    /**
     * Copy the MagicStack contents from another InventoryPlayer instance
     */
    public void copyInventory(MagicInventoryPlayer par1InventoryPlayer)
    {
        for (int i = 0; i < mainInventory.length; i++)
        {
            mainInventory[i] = MagicStack.copyMagicStack(par1InventoryPlayer.mainInventory[i]);
        }

    }
}
