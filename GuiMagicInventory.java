package net.minecraft.src;

import java.util.*;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiMagicInventory extends GuiMagicContainer
{
    /**
     * x size of the inventory window in pixels. Defined as float, passed as int
     */
    private float xSize_lo;

    /**
     * y size of the inventory window in pixels. Defined as float, passed as int.
     */
    private float ySize_lo;

    public GuiMagicInventory(EntityPlayer par1EntityPlayer)
    {
        super(par1EntityPlayer.magicinventorySlots);
        allowUserInput = true;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
    	
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        controlList.clear();

        super.initGui();

        if (!mc.thePlayer.getActivePotionEffects().isEmpty())
        {
        	guiLeft = 160 + (width - xSize - 200) / 2;
        }
    }

    /**
     * Draw the foreground layer for the GuiContainer (everythin in front of the items)
     */
    protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString(StatCollector.translateToLocal("mod_moremagic.magicinv"), 6, 6, 0x404040);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        super.drawScreen(par1, par2, par3);
        xSize_lo = par1;
        ySize_lo = par2;
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        int i = mc.renderEngine.getTexture("/gui/magicinventory.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
        int j = guiLeft;
        int k = guiTop;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        GL11.glTranslatef(j + 51, k + 75, 50F);
        float f = 30F;
        GL11.glScalef(-f, f, f);
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        float f1 = mc.thePlayer.renderYawOffset;
        float f2 = mc.thePlayer.rotationYaw;
        float f3 = mc.thePlayer.rotationPitch;
        float f4 = (float)(j + 51) - xSize_lo;
        float f5 = (float)((k + 75) - 50) - ySize_lo;
        GL11.glRotatef(135F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        /*GL11.glRotatef(-135F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-(float)Math.atan(f5 / 40F) * 20F, 1.0F, 0.0F, 0.0F);
        mc.thePlayer.renderYawOffset = (float)Math.atan(f4 / 40F) * 20F;
        mc.thePlayer.rotationYaw = (float)Math.atan(f4 / 40F) * 40F;
        mc.thePlayer.rotationPitch = -(float)Math.atan(f5 / 40F) * 20F;
        mc.thePlayer.rotationYawHead = mc.thePlayer.rotationYaw;
        GL11.glTranslatef(0.0F, mc.thePlayer.yOffset, 0.0F);
        RenderManager.instance.playerViewY = 180F;
        RenderManager.instance.renderEntityWithPosYaw(mc.thePlayer, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        mc.thePlayer.renderYawOffset = f1;
        mc.thePlayer.rotationYaw = f2;
        mc.thePlayer.rotationPitch = f3;*/
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
    	
    }
}
