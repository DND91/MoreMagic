package net.minecraft.src;

import java.awt.event.KeyEvent;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;

public class mod_MoreMagic extends BaseMod{
	private KeyBinding KeyShift = new KeyBinding("KeyShift", KeyEvent.VK_SHIFT);
	private KeyBinding KeyChangeLeftMagic = new KeyBinding("mod_moremagic.leftmagic", Keyboard.KEY_V);
	private KeyBinding KeyChangeRightMagic = new KeyBinding("mod_moremagic.rightmagic", Keyboard.KEY_B);
	private KeyBinding KeyUseMagic = new KeyBinding("mod_moremagic.usemagic", Keyboard.KEY_RETURN);
	private boolean KeyShiftB = false;
	
	public mod_MoreMagic(){
		ModLoader.registerKey(this,this.KeyShift, false);
		ModLoader.addLocalization("KeyShift", "KeyShift");
		
		ModLoader.registerKey(this,this.KeyChangeLeftMagic, false);
		ModLoader.addLocalization("KeyChangeLeftMagic", "KeyChangeLeftMagic");
		
		ModLoader.registerKey(this,this.KeyChangeRightMagic, false);
		ModLoader.addLocalization("KeyChangeRightMagic", "KeyChangeRightMagic");
		
		ModLoader.registerKey(this,this.KeyUseMagic, false);
		ModLoader.addLocalization("KeyUseMagic", "KeyUseMagic");
		
		ModLoader.setInGameHook(this,true,false);
		
	}

	public void load(){
		
	}
	
	public void keyboardEvent(KeyBinding keybinding)
    {
		KeyShiftB = GuiScreen.isShiftKeyDown();
		if(KeyShiftB)
			if(keybinding == this.KeyChangeLeftMagic){
				GuiQuickbar.slot--;
				if(GuiQuickbar.slot < 0)
					GuiQuickbar.slot = 8;
			}else if(keybinding == this.KeyChangeRightMagic){
				GuiQuickbar.slot++;
				if(GuiQuickbar.slot > 8)
					GuiQuickbar.slot = 0;
			}
    }
	
	private GuiScreen cScreen = null;
	public boolean onTickInGame(float f,Minecraft minecraft){
	/*	KeyShiftB = GuiScreen.isShiftKeyDown();
		
		if(minecraft != null && this.KeyShiftB && !ModLoader.isGUIOpen(GuiQuickbar.class)){
			EntityPlayer ep = minecraft.thePlayer;
			cScreen = new GuiQuickbar(minecraft);
			ModLoader.openGUI(ep, cScreen);			
		}else if(!this.KeyShiftB && ModLoader.isGUIOpen(GuiQuickbar.class)){
			minecraft.currentScreen = null;
			cScreen= null;
		}
		*/
		return true;
	}
	
	public String getVersion(){
		return "MoreMagic Alpha v0.0";
	}
	
}
