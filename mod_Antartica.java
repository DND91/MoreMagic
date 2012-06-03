package net.minecraft.src;

import java.util.Random;
import java.util.List;
import java.util.jar.*;


public class mod_Antartica extends BaseMod
{
	
	public static final Block AntarticaSnow = (new AntarticaSnow(210, ModLoader.addOverride("/terrain.png", "/Blocks/SnowBlock.png")).setStepSound(Block.soundWoodFootstep).setHardness(0.5F).setResistance(1F).setBlockName("AntarticaSnow"));
	public static final Block AntarticaIce = (new AntarticaIce(211, ModLoader.addOverride("/terrain.png", "/Blocks/IceBlock.png")).setStepSound(Block.soundWoodFootstep).setHardness(0.5F).setResistance(1F).setBlockName("AntarticaSnow"));
	public static final Block ForbiddenPortal = (new BlockAntarticaPortal(212).setStepSound(Block.soundWoodFootstep).setHardness(0.5F).setResistance(1F).setBlockName("AntarticaSnow"));
	public static final Block MysticalStone= (new PortalShell(213, ModLoader.addOverride("/terrain.png", "/Blocks/PortalShell.png")).setStepSound(Block.soundWoodFootstep).setHardness(0.5F).setResistance(1F).setBlockName("AntarticaSnow"));

	
	public void load()
	{
	
		ModLoader.registerBlock(AntarticaSnow);
		ModLoader.registerBlock(AntarticaIce);

		
		ModLoader.registerBlock(ForbiddenPortal);

		ModLoader.registerBlock(MysticalStone);

		
		ModLoader.addRecipe(new ItemStack(ForbiddenPortal, 64), new Object[] { "XXX","ddd","ZZZ", Character.valueOf('d'), Block.dirt, Character.valueOf('X'), Block.sand, Character.valueOf('Z'), Block.planks}); 

	}

	public String getVersion()
	{
		return " Antartica Mod By TheInstitutions & His Awesome Subscribers";
	}
	
}