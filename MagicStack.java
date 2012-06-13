package net.minecraft.src;

import java.util.*;

public class MagicStack {
	   /** Size of the stack. */
    public int stackSize;

    /**
     * Number of animation frames to go when receiving an item (by walking into it, for example).
     */
    public int animationsToGo;

    /** ID of the magic. */
    public int magicID;

    /**
     * A NBTTagMap containing data about an MagicStack. Can only be used for non stackable items
     */
    public NBTTagCompound stackTagCompound;

    /** Damage dealt to the item or number of use. Raise when using items. */
    private int magicDamage;



    public MagicStack(Magic par1Magic)
    {
        this(par1Magic.shiftedIndex, 1, 0);
    }

    public MagicStack(Magic par1Magic, int par2)
    {
        this(par1Magic.shiftedIndex, par2, 0);
    }

    public MagicStack(Magic par1Magic, int par2, int par3)
    {
        this(par1Magic.shiftedIndex, par2, par3);
    }

    public MagicStack(int par1, int par2, int par3)
    {
        stackSize = 0;
        magicID = par1;
        stackSize = par2;
        magicDamage = par3;
    }

    public static MagicStack loadMagicStackFromNBT(NBTTagCompound par0NBTTagCompound)
    {
        MagicStack magicstack = new MagicStack();
        magicstack.readFromNBT(par0NBTTagCompound);
        return magicstack.getMagic() != null ? magicstack : null;
    }

    private MagicStack()
    {
        stackSize = 0;
    }

    /**
     * Remove the argument from the stack size. Return a new stack object with argument size.
     */
    public MagicStack splitStack(int par1)
    {
        MagicStack MagicStack = new MagicStack(magicID, par1, magicDamage);

        if (stackTagCompound != null)
        {
            MagicStack.stackTagCompound = (NBTTagCompound)stackTagCompound.copy();
        }

        stackSize -= par1;
        return MagicStack;
    }

    /**
     * Returns the object corresponding to the stack.
     */
    public Magic getMagic()
    {
        return Magic.magicList[magicID];
    }

    /**
     * Returns the icon index of the current stack.
     */
    public int getIconIndex()
    {
        return getMagic().getIconIndex(this);
    }

    /**
     * Uses the item stack by the player. Gives the coordinates of the block its being used against and the side. Args:
     * player, world, x, y, z, side
     */
    public boolean useMagic(EntityPlayer par1EntityPlayer, World par2World, int par3, int par4, int par5, int par6)
    {
        boolean flag = getMagic().onMagicUse(this, par1EntityPlayer, par2World, par3, par4, par5, par6);

        if (flag)
        {
            par1EntityPlayer.addStat(StatList.objectUseStats[magicID], 1);
        }

        return flag;
    }

    /**
     * Returns the strength of the stack against a given block.
     */
    public float getStrVsBlock(Block par1Block)
    {
        return getMagic().getStrVsBlock(this, par1Block);
    }

    /**
     * Called whenever this item stack is equipped and right clicked. Returns the new item stack to put in the position
     * where this item is. Args: world, player
     */
    public MagicStack useMagicRightClick(World par1World, EntityPlayer par2EntityPlayer)
    {
       return getMagic().onMagicRightClick(this, par1World, par2EntityPlayer);

    }

    public MagicStack onFoodEaten(World par1World, EntityPlayer par2EntityPlayer)
    {
        return getMagic().onFoodEaten(this, par1World, par2EntityPlayer);
    }

    /**
     * Write the stack fields to a NBT object. Return the new NBT object.
     */
    public NBTTagCompound writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setShort("id", (short)magicID);
        par1NBTTagCompound.setByte("Count", (byte)stackSize);
        par1NBTTagCompound.setShort("Damage", (short)magicDamage);

        if (stackTagCompound != null)
        {
            par1NBTTagCompound.setTag("tag", stackTagCompound);
        }

        return par1NBTTagCompound;
    }

    /**
     * Read the stack fields from a NBT object.
     */
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        magicID = par1NBTTagCompound.getShort("id");
        stackSize = par1NBTTagCompound.getByte("Count");
        magicDamage = par1NBTTagCompound.getShort("Damage");

        if (par1NBTTagCompound.hasKey("tag"))
        {
            stackTagCompound = par1NBTTagCompound.getCompoundTag("tag");
        }
    }

    /**
     * Returns maximum size of the stack.
     */
    public int getMaxStackSize()
    {
        return getMagic().getMagicStackLimit();
    }

    /**
     * Returns true if the MagicStack can hold 2 or more units of the item.
     */
    public boolean isStackable()
    {
        return getMaxStackSize() > 1 && (!isMagicStackDamageable() || !isMagicDamaged());
    }

    /**
     * true if this MagicStack is damageable
     */
    public boolean isMagicStackDamageable()
    {
        return Magic.magicList[magicID].getMaxDamage() > 0;
    }

    public boolean getHasSubtypes()
    {
        return Magic.magicList[magicID].getHasSubtypes();
    }

    /**
     * returns true when a damageable item is damaged
     */
    public boolean isMagicDamaged()
    {
        return isMagicStackDamageable() && magicDamage > 0;
    }

    /**
     * gets the damage of an MagicStack, for displaying purposes
     */
    public int getMagicDamageForDisplay()
    {
        return magicDamage;
    }

    /**
     * gets the damage of an MagicStack
     */
    public int getMagicDamage()
    {
        return magicDamage;
    }

    /**
     * Sets the item damage of the MagicStack.
     */
    public void setItemDamage(int par1)
    {
        magicDamage = par1;
    }

    /**
     * Returns the max damage an item in the stack can take.
     */
    public int getMaxDamage()
    {
        return Item.itemsList[magicID].getMaxDamage();
    }

    /**
     * Damages the item in the MagicStack
     */
    public void damageMagic(int par1, EntityLiving par2EntityLiving)
    {
        if (!isMagicStackDamageable())
        {
            return;
        }

        if (par1 > 0 && (par2EntityLiving instanceof EntityPlayer))
        {
            int i = EnchantmentHelper.getUnbreakingModifier(((EntityPlayer)par2EntityLiving).inventory);

            if (i > 0 && par2EntityLiving.worldObj.rand.nextInt(i + 1) > 0)
            {
                return;
            }
        }

        magicDamage += par1;

        if (magicDamage > getMaxDamage())
        {
            //par2EntityLiving.renderBrokenMagicStack(this); THIS SHOULD LATER PLAY SOUND IF MAGIC DIES!

            if (par2EntityLiving instanceof EntityPlayer)
            {
                ((EntityPlayer)par2EntityLiving).addStat(StatList.objectBreakStats[magicID], 1);
            }

            stackSize--;

            if (stackSize < 0)
            {
                stackSize = 0;
            }

            magicDamage = 0;
        }
    }

    /**
     * Calls the corresponding fct in di
     */
    public void hitEntity(EntityLiving par1EntityLiving, EntityPlayer par2EntityPlayer)
    {
        boolean flag = Magic.magicList[magicID].hitEntity(this, par1EntityLiving, par2EntityPlayer);

        if (flag)
        {
            par2EntityPlayer.addStat(StatList.objectUseStats[magicID], 1);
        }
    }

    public void onDestroyBlock(int par1, int par2, int par3, int par4, EntityPlayer par5EntityPlayer)
    {
        boolean flag = Magic.magicList[magicID].onBlockDestroyed(this, par1, par2, par3, par4, par5EntityPlayer);

        if (flag)
        {
            par5EntityPlayer.addStat(StatList.objectUseStats[magicID], 1);
        }
    }

    /**
     * Returns the damage against a given entity.
     */
    public int getDamageVsEntity(Entity par1Entity)
    {
        return Magic.magicList[magicID].getDamageVsEntity(par1Entity);
    }

    /**
     * Checks if the MagicStack object can harvest a specified block
     */
    public boolean canHarvestBlock(Block par1Block)
    {
        return Magic.magicList[magicID].canHarvestBlock(par1Block);
    }

    /**
     * Called when a given item stack is about to be destroyed due to its damage level expiring when used on a block or
     * entity. Typically used by tools.
     */
    public void onMagicDestroyedByUse(EntityPlayer entityplayer)
    {
    }

    /**
     * Uses the stack on the entity.
     */
    public void useMagicOnEntity(EntityLiving par1EntityLiving)
    {
        Magic.magicList[magicID].useMagicOnEntity(this, par1EntityLiving);
    }

    /**
     * Returns a new stack with the same properties.
     */
    public MagicStack copy()
    {
        MagicStack MagicStack = new MagicStack(magicID, stackSize, magicDamage);

        if (stackTagCompound != null)
        {
            MagicStack.stackTagCompound = (NBTTagCompound)stackTagCompound.copy();

            if (!MagicStack.stackTagCompound.equals(stackTagCompound))
            {
                return MagicStack;
            }
        }

        return MagicStack;
    }

    public static boolean func_46154_a(MagicStack par0MagicStack, MagicStack par1MagicStack)
    {
        if (par0MagicStack == null && par1MagicStack == null)
        {
            return true;
        }

        if (par0MagicStack == null || par1MagicStack == null)
        {
            return false;
        }

        if (par0MagicStack.stackTagCompound == null && par1MagicStack.stackTagCompound != null)
        {
            return false;
        }
        else
        {
            return par0MagicStack.stackTagCompound == null || par0MagicStack.stackTagCompound.equals(par1MagicStack.stackTagCompound);
        }
    }

    /**
     * compares MagicStack argument1 with MagicStack argument2; returns true if both MagicStacks are equal
     */
    public static boolean areMagicStacksEqual(MagicStack par0MagicStack, MagicStack par1MagicStack)
    {
        if (par0MagicStack == null && par1MagicStack == null)
        {
            return true;
        }

        if (par0MagicStack == null || par1MagicStack == null)
        {
            return false;
        }
        else
        {
            return par0MagicStack.isMagicStackEqual(par1MagicStack);
        }
    }

    /**
     * compares MagicStack argument to the instance MagicStack; returns true if both MagicStacks are equal
     */
    private boolean isMagicStackEqual(MagicStack par1MagicStack)
    {
        if (stackSize != par1MagicStack.stackSize)
        {
            return false;
        }

        if (magicID != par1MagicStack.magicID)
        {
            return false;
        }

        if (magicDamage != par1MagicStack.magicDamage)
        {
            return false;
        }

        if (stackTagCompound == null && par1MagicStack.stackTagCompound != null)
        {
            return false;
        }
        else
        {
            return stackTagCompound == null || stackTagCompound.equals(par1MagicStack.stackTagCompound);
        }
    }

    /**
     * compares MagicStack argument to the instance MagicStack; returns true if the Items contained in both MagicStacks are
     * equal
     */
    public boolean isItemEqual(MagicStack par1MagicStack)
    {
        return magicID == par1MagicStack.magicID && magicDamage == par1MagicStack.magicDamage;
    }

    /**
     * Creates a copy of a MagicStack, a null parameters will return a null.
     */
    public static MagicStack copyMagicStack(MagicStack par0MagicStack)
    {
        return par0MagicStack == null ? null : par0MagicStack.copy();
    }

    public String toString()
    {
        return (new StringBuilder()).append(stackSize).append("x").append(Magic.magicList[magicID].getMagicName()).append("@").append(magicDamage).toString();
    }

    /**
     * Called each tick as long the MagicStack in on player inventory. Used to progress the pickup animation and update
     * maps.
     */
    public void updateAnimation(World par1World, Entity par2Entity, int par3, boolean par4)
    {
        if (animationsToGo > 0)
        {
            animationsToGo--;
        }

        Magic.magicList[magicID].onUpdate(this, par1World, par2Entity, par3, par4);
    }

    public void onCrafting(World par1World, EntityPlayer par2EntityPlayer, int par3)
    {
        par2EntityPlayer.addStat(StatList.objectCraftStats[magicID], par3);
        Magic.magicList[magicID].onCreated(this, par1World, par2EntityPlayer);
    }

    public boolean isStackEqual(MagicStack par1MagicStack)
    {
        return magicID == par1MagicStack.magicID && stackSize == par1MagicStack.stackSize && magicDamage == par1MagicStack.magicDamage;
    }

    public int getMaxItemUseDuration()
    {
        return getMagic().getMaxMagicUseDuration(this);
    }

    public EnumAction getMagicUseAction()
    {
        return getMagic().getMagicUseAction(this);
    }

    /**
     * Called when the player releases the use item button. Args: world, entityplayer, itemInUseCount
     */
    public void onPlayerStoppedUsing(World par1World, EntityPlayer par2EntityPlayer, int par3)
    {
        getMagic().onPlayerStoppedUsing(this, par1World, par2EntityPlayer, par3);
    }

    /**
     * Returns true if the MagicStack has an NBTTagCompound. Currently used to store enchantments.
     */
    public boolean hasTagCompound()
    {
        return stackTagCompound != null;
    }

    /**
     * Returns the NBTTagCompound of the MagicStack.
     */
    public NBTTagCompound getTagCompound()
    {
        return stackTagCompound;
    }

    public NBTTagList getEnchantmentTagList()
    {
        if (stackTagCompound == null)
        {
            return null;
        }
        else
        {
            return (NBTTagList)stackTagCompound.getTag("ench");
        }
    }

    /**
     * Assigns a NBTTagCompound to the MagicStack, minecraft validates that only non-stackable items can have it.
     */
    public void setTagCompound(NBTTagCompound par1NBTTagCompound)
    {
        stackTagCompound = par1NBTTagCompound;
    }

    /**
     * gets a list of strings representing the item name and successive extra data, eg Enchantments and potion effects
     */
    public List getMagicNameandInformation()
    {
        ArrayList arraylist = new ArrayList();
        Magic magic = Magic.magicList[magicID];
        arraylist.add(magic.getMagicDisplayName(this));
        String s = magic.type + " level > " + magic.minLvl;
        arraylist.add(s);
        magic.addInformation(this, arraylist);

        if (hasTagCompound())
        {
            NBTTagList nbttaglist = getEnchantmentTagList();

            if (nbttaglist != null)
            {
                for (int i = 0; i < nbttaglist.tagCount(); i++)
                {
                    short word0 = ((NBTTagCompound)nbttaglist.tagAt(i)).getShort("id");
                    short word1 = ((NBTTagCompound)nbttaglist.tagAt(i)).getShort("lvl");

                    if (Enchantment.enchantmentsList[word0] != null)
                    {
                        arraylist.add(Enchantment.enchantmentsList[word0].getTranslatedName(word1));
                    }
                }
            }
        }

        return arraylist;
    }

    public boolean hasEffect()
    {
        return getMagic().hasEffect(this);
    }

    public EnumRarity getRarity()
    {
        return getMagic().getRarity(this);
    }

    /**
     * True if it is a tool and has no enchantments to begin with
     */
    public boolean isMagicEnchantable()
    {
        if (!getMagic().isMagicTool(this))
        {
            return false;
        }
        else
        {
            return !isMagicEnchanted();
        }
    }

    /**
     * Adds an enchantment with a desired level on the MagicStack.
     */
    public void addEnchantment(Enchantment par1Enchantment, int par2)
    {
        if (stackTagCompound == null)
        {
            setTagCompound(new NBTTagCompound());
        }

        if (!stackTagCompound.hasKey("ench"))
        {
            stackTagCompound.setTag("ench", new NBTTagList("ench"));
        }

        NBTTagList nbttaglist = (NBTTagList)stackTagCompound.getTag("ench");
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setShort("id", (short)par1Enchantment.effectId);
        nbttagcompound.setShort("lvl", (byte)par2);
        nbttaglist.appendTag(nbttagcompound);
    }

    /**
     * True if the item has enchantment data
     */
    public boolean isMagicEnchanted()
    {
        return stackTagCompound != null && stackTagCompound.hasKey("ench");
    }
}

