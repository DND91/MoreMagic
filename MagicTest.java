package net.minecraft.src;

public class MagicTest extends Magic
{
    public MagicTest(int par1)
    {
    	super(par1);
    	this.type = "wind";
    	this.cost = 5;
    	this.minLvl = 5;
    }
    
    public MagicStack onMagicRightClick(MagicStack par1MagicStack, World par2World, EntityPlayer par3EntityPlayer)
    {
    	if(minLvl > par3EntityPlayer.windLvl)
    		return par1MagicStack;

        par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 0.5F, 0.4F / (magicRand.nextFloat() * 0.4F + 0.8F));

        if (!par2World.isRemote)
        {
            par2World.spawnEntityInWorld(new EntityEgg(par2World, par3EntityPlayer));
            par3EntityPlayer.addExpToMagic(type, 100);
        }

        return par1MagicStack;
    }
}
