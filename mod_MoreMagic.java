package net.minecraft.src;

import java.awt.event.KeyEvent;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;

public class mod_MoreMagic extends BaseMod{
	//KEDYS
	private KeyBinding KeyShowMagicQui = new KeyBinding("mod_moremagic.KeyShowMagicQui", Keyboard.KEY_LCONTROL);
	private KeyBinding KeyShowMagicInv = new KeyBinding("mod_moremagic.KeyShowMagicInv", Keyboard.KEY_U);
	private KeyBinding KeyChangeLeftMagic = new KeyBinding("mod_moremagic.leftmagic", Keyboard.KEY_V);
	private KeyBinding KeyChangeRightMagic = new KeyBinding("mod_moremagic.rightmagic", Keyboard.KEY_B);
	private KeyBinding KeyUseMagic = new KeyBinding("mod_moremagic.usemagic", Keyboard.KEY_C);
	private boolean KeyShiftB = false;
	private boolean KeyUse = false;
	private boolean KeyInv = false;
	
	//MAGIC
	public static final Magic magicTest = (new MagicTest(2000)).setIconCoord(5, 0).setMagicName("magicTest", "Test Magic");
	
	public mod_MoreMagic(){
		/* Look in GuiIngame.java(KEYCTRL) and this class.*/
		ModLoader.registerKey(this,this.KeyShowMagicQui, false); //NOT IN USE, ITS DIRECTLY INSERTED IN THE GUIIngame
		ModLoader.addLocalization("mod_moremagic.KeyShowMagicQui", "Show Magic Qui");
		
		ModLoader.registerKey(this,this.KeyShowMagicInv, false);
		ModLoader.addLocalization("mod_moremagic.KeyShowMagicInv", "Show Magic Inv");
		
		ModLoader.registerKey(this,this.KeyChangeLeftMagic, false);
		ModLoader.addLocalization("mod_moremagic.leftmagic", "Change Magic Left");
		
		ModLoader.registerKey(this,this.KeyChangeRightMagic, false);
		ModLoader.addLocalization("mod_moremagic.rightmagic", "Change Magic Right");
		
		ModLoader.registerKey(this,this.KeyUseMagic, false);
		ModLoader.addLocalization("mod_moremagic.usemagic", "Use Magic");
		
		ModLoader.addLocalization("mod_moremagic.magicinv", "Spellbook");
		
		ModLoader.setInGameHook(this,true,false);
		
		/* PLAYER API */
		PlayerAPI.register("MoreMagic Alpha v0.1", PlayerMagicBase.class);
		
	}

	public void load(){
		//magic.iconIndex = ModLoader.addOverride("/gui/items.png", "texture");
		
		//new ItemStack(Item.field_44019_bC.shiftedIndex, 1, <eggID>)
		
		/*
		ModLoader.registerEntityID(EntityPig.class, "SUPER PIG", 57, 10, 12);
		
		ModLoader.addLocalization("entity.SUPER PIG.name", "SUPER PIG");
		
		ModLoader.addRecipe(new ItemStack(Item.monsterPlacer.shiftedIndex, 8, 57), new Object[]{
		    "C", 
		    'C', Block.dirt
		});
		*/
	}
	
	public void keyboardEvent(KeyBinding keybinding)
    {
		KeyShiftB = GuiScreen.isCtrlKeyDown();
		if(KeyShiftB)
			if(keybinding == this.KeyChangeLeftMagic){
				ModLoader.getMinecraftInstance().thePlayer.magicinventory.currentMagic--;
				if(ModLoader.getMinecraftInstance().thePlayer.magicinventory.currentMagic < 0)
					ModLoader.getMinecraftInstance().thePlayer.magicinventory.currentMagic = 8;
			}else if(keybinding == this.KeyChangeRightMagic){
				ModLoader.getMinecraftInstance().thePlayer.magicinventory.currentMagic++;
				if(ModLoader.getMinecraftInstance().thePlayer.magicinventory.currentMagic > 8)
					ModLoader.getMinecraftInstance().thePlayer.magicinventory.currentMagic = 0;
			}
		
		if(keybinding == this.KeyUseMagic){
			MagicInventoryPlayer mip = ModLoader.getMinecraftInstance().thePlayer.magicinventory;
			if(mip.getCurrentMagic() != null){
				this.KeyUse = true;
			}
		}
		
		if(keybinding == this.KeyShowMagicInv){
			this.KeyInv = true;
		}
		
		
    }
	
	private GuiScreen cScreen = null;
	public boolean onTickInGame(float f,Minecraft minecraft){
		if(KeyUse){
			MagicInventoryPlayer mip = ModLoader.getMinecraftInstance().thePlayer.magicinventory;
			mip.getCurrentMagic().useMagicRightClick(ModLoader.getMinecraftInstance().theWorld, ModLoader.getMinecraftInstance().thePlayer);
			KeyUse = false;
		}
		
		if(KeyInv){
			ModLoader.openGUI(minecraft.thePlayer, new GuiMagicInventory(minecraft.thePlayer));
			KeyInv = false;
		}
		
		return true;
	}
	
	public String getVersion(){
		return "MoreMagic Alpha v0.0";
	}
	
}
