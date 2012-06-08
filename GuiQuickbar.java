package net.minecraft.src;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;

public class GuiQuickbar extends Gui{
	
	public static int slot = 0;
	
	private EntityPlayer player;
	private Minecraft mc;
	public GuiQuickbar(Minecraft mine){
		player = mine.thePlayer;
		mc = mine;
	}
	
	public void drawScreen(int i, int j, float f){
		
		ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		int x = scaledresolution.getScaledWidth();
        int y = scaledresolution.getScaledHeight();

        drawTexturedModalRect(x / 2 - 91, y - 62, 0, 0, 182, 22);
        drawTexturedModalRect((x / 2 - 91 - 1) + slot * 20, y - 62 - 1, 0, 22, 24, 22);
		//super.drawScreen(i, j, f);
	}
	
}
