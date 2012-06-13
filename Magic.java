package net.minecraft.src;

import java.io.PrintStream;
import java.util.List;
import java.util.Random;

public class Magic
{
    /** The RNG used by the Item subclasses. */
    protected static Random magicRand = new Random();
    public static Magic magicList[] = new Magic[32000];
    //public static ItemMap map = (ItemMap)(new ItemMap(102)).setIconCoord(12, 3).setItemName("map"); ???


    /** Item index + 256 */
    public final int shiftedIndex;

    /** Maximum size of the stack. */
    protected int maxStackSize;

    /** Maximum damage an item can handle. */
    private int maxDamage;

    /** Icon index in the icons table. */
    protected int iconIndex;

    /** If true, render the object in full 3D, like weapons and tools. */
    protected boolean bFull3D;

    /**
     * Some items (like dyes) have multiple subtypes on same item, this is field define this behavior
     */
    protected boolean hasSubtypes;
    private Magic containerMagic;
    private String potionEffect;

    /** full name of item from language file */
    private String magicName;
    private String realName;
    
    /** 
     * Magicstuff
     */
    
    public String type;
    public int cost;
    public int minLvl;

    protected Magic(int par1)
    {
        maxStackSize = 64;
        maxDamage = 0;
        bFull3D = false;
        hasSubtypes = false;
        containerMagic = null;
        potionEffect = null;
        shiftedIndex = 256 + par1;

        if (magicList[256 + par1] != null)
        {
            System.out.println((new StringBuilder()).append("CONFLICT @ ").append(par1).toString());
        }

        magicList[256 + par1] = this;
    }

    /**
     * Sets the icon index for this item. Returns the item.
     */
    public Magic setIconIndex(int par1)
    {
        iconIndex = par1;
        return this;
    }

    public Magic setMaxStackSize(int par1)
    {
        maxStackSize = par1;
        return this;
    }

    public Magic setIconCoord(int par1, int par2)
    {
        iconIndex = par1 + par2 * 16;
        return this;
    }

    /**
     * Gets an icon index based on an item's damage value
     */
    public int getIconFromDamage(int par1)
    {
        return iconIndex;
    }

    /**
     * Returns the icon index of the stack given as argument.
     */
    public final int getIconIndex(MagicStack par1MagicStack)
    {
        return getIconFromDamage(par1MagicStack.getMagicDamage());
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS !
     */
    public boolean onMagicUse(MagicStack par1MagicStack, EntityPlayer par2EntityPlayer, World par3World, int i, int j, int k, int l)
    {
        return false;
    }

    /**
     * Returns the strength of the stack against a given block. 1.0F base, (Quality+1)*2 if correct blocktype, 1.5F if
     * sword
     */
    public float getStrVsBlock(MagicStack par1MagicStack, Block par2Block)
    {
        return 1.0F;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: MagicStack, world, entityPlayer
     */
    public MagicStack onMagicRightClick(MagicStack par1MagicStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        return par1MagicStack;
    }

    public MagicStack onFoodEaten(MagicStack par1MagicStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        return par1MagicStack;
    }

    /**
     * Returns the maximum size of the stack for a specific item. *Isn't this more a Set than a Get?*
     */
    public int getMagicStackLimit()
    {
        return maxStackSize;
    }

    /**
     * Returns the metadata of the block which this Item (ItemBlock) can place
     */
    public int getMetadata(int par1)
    {
        return 0;
    }

    public boolean getHasSubtypes()
    {
        return hasSubtypes;
    }

    protected Magic setHasSubtypes(boolean par1)
    {
        hasSubtypes = par1;
        return this;
    }

    /**
     * Returns the maximum damage an item can take.
     */
    public int getMaxDamage()
    {
        return maxDamage;
    }

    /**
     * set max damage of an Item
     */
    protected Magic setMaxDamage(int par1)
    {
        maxDamage = par1;
        return this;
    }

    public boolean isDamageable()
    {
        return maxDamage > 0 && !hasSubtypes;
    }

    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    public boolean hitEntity(MagicStack par1MagicStack, EntityLiving par2EntityLiving, EntityLiving par3EntityLiving)
    {
        return false;
    }

    public boolean onBlockDestroyed(MagicStack par1MagicStack, int par2, int par3, int i, int j, EntityLiving entityliving)
    {
        return false;
    }

    /**
     * Returns the damage against a given entity.
     */
    public int getDamageVsEntity(Entity par1Entity)
    {
        return 1;
    }

    /**
     * Returns if the item (tool) can harvest results from the block type.
     */
    public boolean canHarvestBlock(Block par1Block)
    {
        return false;
    }

    /**
     * Called when a player right clicks a entity with a item.
     */
    public void useMagicOnEntity(MagicStack magicstack, EntityLiving entityliving)
    {
    }

    /**
     * Sets bFull3D to True and return the object.
     */
    public Magic setFull3D()
    {
        bFull3D = true;
        return this;
    }

    /**
     * Returns True is the item is renderer in full 3D when hold.
     */
    public boolean isFull3D()
    {
        return bFull3D;
    }

    /**
     * Returns true if this item should be rotated by 180 degrees around the Y axis when being held in an entities
     * hands.
     */
    public boolean shouldRotateAroundWhenRendering()
    {
        return false;
    }

    /**
     * set name of item from language file
     */
    public Magic setMagicName(String par1Str, String real)
    {
        magicName = (new StringBuilder()).append("magic.").append(par1Str).toString();
        realName = real;
        return this;
    }

    public String getLocalMagicName(MagicStack par1MagicStack)
    {
        String s = getMagicNameIS(par1MagicStack);

        if (s == null)
        {
            return "";
        }
        else
        {
            return StatCollector.translateToLocal(s);
        }
    }

    public String getMagicName()
    {
        return magicName;
    }

    public String getMagicNameIS(MagicStack par1MagicStack)
    {
        return magicName;
    }

    public Magic setContainerMagic(Magic par1Magic)
    {
        containerMagic = par1Magic;
        return this;
    }

    /**
     * If this returns true, after a recipe involving this item is crafted the container item will be added to the
     * player's inventory instead of remaining in the crafting grid.
     */
    public boolean doesContainerMagicLeaveCraftingGrid(MagicStack par1MagicStack)
    {
        return true;
    }

    public boolean func_46056_k()
    {
        return false;
    }

    public Magic getContainerMagic()
    {
        return containerMagic;
    }

    /**
     * True if this Item has a container item (a.k.a. crafting result)
     */
    public boolean hasContainerMagic()
    {
        return containerMagic != null;
    }

    public String getStatName()
    {
        return StatCollector.translateToLocal((new StringBuilder()).append(getMagicName()).append(".name").toString());
    }

    public int getColorFromDamage(int par1, int par2)
    {
        return 0xffffff;
    }

    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
     * update it's contents.
     */
    public void onUpdate(MagicStack magicstack, World world, Entity entity, int i, boolean flag)
    {
    }

    /**
     * Called when item is crafted/smelted. Used only by maps so far.
     */
    public void onCreated(MagicStack magicstack, World world, EntityPlayer entityplayer)
    {
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getMagicUseAction(MagicStack par1MagicStack)
    {
        return EnumAction.none;
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getMaxMagicUseDuration(MagicStack par1MagicStack)
    {
        return 0;
    }

    /**
     * called when the player releases the use item button. Args: MagicStack, world, entityplayer, itemInUseCount
     */
    public void onPlayerStoppedUsing(MagicStack magicstack, World world, EntityPlayer entityplayer, int i)
    {
    }

    /**
     * Sets the string representing this item's effect on a potion when used as an ingredient.
     */
    protected Magic setPotionEffect(String par1Str)
    {
        potionEffect = par1Str;
        return this;
    }

    /**
     * Returns a string representing what this item does to a potion.
     */
    public String getPotionEffect()
    {
        return potionEffect;
    }

    /**
     * Returns true if this item serves as a potion ingredient (its ingredient information is not null).
     */
    public boolean isPotionIngredient()
    {
        return potionEffect != null;
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    public void addInformation(MagicStack magicstack, List list)
    {
    }

    public String getMagicDisplayName(MagicStack par1MagicStack)
    {
        return this.realName;
    }

    public boolean hasEffect(MagicStack par1MagicStack)
    {
        return par1MagicStack.isMagicEnchanted();
    }

    /**
     * Return an item rarity from EnumRarity
     */
    public EnumRarity getRarity(MagicStack par1MagicStack)
    {
        if (par1MagicStack.isMagicEnchanted())
        {
            return EnumRarity.rare;
        }
        else
        {
            return EnumRarity.common;
        }
    }

    /**
     * Checks isDamagable and if it cannot be stacked
     */
    public boolean isMagicTool(MagicStack par1MagicStack)
    {
        return getMagicStackLimit() == 1 && isDamageable();
    }

    protected MovingObjectPosition getMovingObjectPositionFromPlayer(World par1World, EntityPlayer par2EntityPlayer, boolean par3)
    {
        float f = 1.0F;
        float f1 = par2EntityPlayer.prevRotationPitch + (par2EntityPlayer.rotationPitch - par2EntityPlayer.prevRotationPitch) * f;
        float f2 = par2EntityPlayer.prevRotationYaw + (par2EntityPlayer.rotationYaw - par2EntityPlayer.prevRotationYaw) * f;
        double d = par2EntityPlayer.prevPosX + (par2EntityPlayer.posX - par2EntityPlayer.prevPosX) * (double)f;
        double d1 = (par2EntityPlayer.prevPosY + (par2EntityPlayer.posY - par2EntityPlayer.prevPosY) * (double)f + 1.6200000000000001D) - (double)par2EntityPlayer.yOffset;
        double d2 = par2EntityPlayer.prevPosZ + (par2EntityPlayer.posZ - par2EntityPlayer.prevPosZ) * (double)f;
        Vec3D vec3d = Vec3D.createVector(d, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.01745329F - (float)Math.PI);
        float f4 = MathHelper.sin(-f2 * 0.01745329F - (float)Math.PI);
        float f5 = -MathHelper.cos(-f1 * 0.01745329F);
        float f6 = MathHelper.sin(-f1 * 0.01745329F);
        float f7 = f4 * f5;
        float f8 = f6;
        float f9 = f3 * f5;
        double d3 = 5D;
        Vec3D vec3d1 = vec3d.addVector((double)f7 * d3, (double)f8 * d3, (double)f9 * d3);
        MovingObjectPosition movingobjectposition = par1World.rayTraceBlocks_do_do(vec3d, vec3d1, par3, !par3);
        return movingobjectposition;
    }

    /**
     * Return the enchantability factor of the item, most of the time is based on material.
     */
    public int getMagicEnchantability()
    {
        return 0;
    }

    public boolean func_46058_c()
    {
        return false;
    }

    public int func_46057_a(int par1, int par2)
    {
        return getIconFromDamage(par1);
    }

}
