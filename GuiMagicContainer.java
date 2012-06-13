package net.minecraft.src;

import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public abstract class GuiMagicContainer extends GuiScreen
{
    /** Stacks renderer. Icons, stack size, health, etc... */
    protected static RenderMagic magicRenderer = new RenderMagic();

    /** The X size of the inventory window in pixels. */
    protected int xSize;

    /** The Y size of the inventory window in pixels. */
    protected int ySize;

    /** A list of the players inventory slots. */
    public MagicContainer inventorySlots;

    /**
     * Starting X position for the Gui. Inconsistent use for Gui backgrounds.
     */
    protected int guiLeft;

    /**
     * Starting Y position for the Gui. Inconsistent use for Gui backgrounds.
     */
    protected int guiTop;

    public GuiMagicContainer(MagicContainer par1Container)
    {
        xSize = 176;
        ySize = 166;
        inventorySlots = par1Container;
    }
    /**
     * String builder. Builds the field for one row of the magic skill.
     */
    
    protected String buildString(String type, int lvl, int exp){
    	String s = (new StringBuilder()).append(type + ": ").append(lvl).append("   " + exp + "/" + (EntityPlayer.base_xp * ((lvl + 1) ^ EntityPlayer.factor))).toString();
    	return s;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        super.initGui();
        //mc.thePlayer.craftingInventory = inventorySlots;
        guiLeft = (width - xSize) / 2;
        guiTop = (height - ySize) / 2;
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        drawDefaultBackground();
        int i = guiLeft;
        int j = guiTop;
        drawGuiContainerBackgroundLayer(par3, par1, par2);
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glPushMatrix();
        GL11.glTranslatef(i, j, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        MagicSlot slot = null;
        int k = 240;
        int i1 = 240;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)k / 1.0F, (float)i1 / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        for (int l = 0; l < inventorySlots.inventoryMagicSlots.size(); l++)
        {
            MagicSlot slot1 = (MagicSlot)inventorySlots.inventoryMagicSlots.get(l);
            drawSlotInventory(slot1);

            if (isMouseOverSlot(slot1, par1, par2))
            {
                slot = slot1;
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                int j1 = slot1.xDisplayPosition;
                int k1 = slot1.yDisplayPosition;
                drawGradientRect(j1, k1, j1 + 16, k1 + 16, 0x80ffffff, 0x80ffffff);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                
            }
        }

        drawGuiContainerForegroundLayer();
        MagicInventoryPlayer inventoryplayer = mc.thePlayer.magicinventory;

        if (inventoryplayer.getMagicStack() != null)
        {
            GL11.glTranslatef(0.0F, 0.0F, 32F);
            zLevel = 200F;
            magicRenderer.zLevel = 200F;
            magicRenderer.renderMagicIntoGUI(fontRenderer, mc.renderEngine, inventoryplayer.getMagicStack(), par1 - i - 8, par2 - j - 8);
            magicRenderer.renderMagicOverlayIntoGUI(fontRenderer, mc.renderEngine, inventoryplayer.getMagicStack(), par1 - i - 8, par2 - j - 8);
            zLevel = 0.0F;
            magicRenderer.zLevel = 0.0F;
        }

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        if (inventoryplayer.getMagicStack() == null && slot != null && slot.getHasStack())
        {
            MagicStack magicstack = slot.getStack();
            List list = magicstack.getMagicNameandInformation();

            if (list.size() > 0)
            {
                int l1 = 0;

                for (int i2 = 0; i2 < list.size(); i2++)
                {
                    int k2 = fontRenderer.getStringWidth((String)list.get(i2));

                    if (k2 > l1)
                    {
                        l1 = k2;
                    }
                }

                int j2 = (par1 - i) + 12;
                int l2 = par2 - j - 12;
                int i3 = l1;
                int j3 = 8;

                if (list.size() > 1)
                {
                    j3 += 2 + (list.size() - 1) * 10;
                }

                zLevel = 300F;
                magicRenderer.zLevel = 300F;
                int k3 = 0xf0100010;
                drawGradientRect(j2 - 3, l2 - 4, j2 + i3 + 3, l2 - 3, k3, k3);
                drawGradientRect(j2 - 3, l2 + j3 + 3, j2 + i3 + 3, l2 + j3 + 4, k3, k3);
                drawGradientRect(j2 - 3, l2 - 3, j2 + i3 + 3, l2 + j3 + 3, k3, k3);
                drawGradientRect(j2 - 4, l2 - 3, j2 - 3, l2 + j3 + 3, k3, k3);
                drawGradientRect(j2 + i3 + 3, l2 - 3, j2 + i3 + 4, l2 + j3 + 3, k3, k3);
                int l3 = 0x505000ff;
                int i4 = (l3 & 0xfefefe) >> 1 | l3 & 0xff000000;
                drawGradientRect(j2 - 3, (l2 - 3) + 1, (j2 - 3) + 1, (l2 + j3 + 3) - 1, l3, i4);
                drawGradientRect(j2 + i3 + 2, (l2 - 3) + 1, j2 + i3 + 3, (l2 + j3 + 3) - 1, l3, i4);
                drawGradientRect(j2 - 3, l2 - 3, j2 + i3 + 3, (l2 - 3) + 1, l3, l3);
                drawGradientRect(j2 - 3, l2 + j3 + 2, j2 + i3 + 3, l2 + j3 + 3, i4, i4);

                for (int j4 = 0; j4 < list.size(); j4++)
                {
                    String s = (String)list.get(j4);

                    if (j4 == 0)
                    {
                        s = (new StringBuilder()).append("\247").append(Integer.toHexString(magicstack.getRarity().nameColor)).append(s).toString();
                    }
                    else
                    {
                        s = (new StringBuilder()).append("\2477").append(s).toString();
                    }

                    fontRenderer.drawStringWithShadow(s, j2, l2, -1);

                    if (j4 == 0)
                    {
                        l2 += 2;
                    }

                    l2 += 10;
                }
                
                

                zLevel = 0.0F;
                magicRenderer.zLevel = 0.0F;
            }
        }
        
        zLevel = 300.0F;
        magicRenderer.zLevel = 300.0F;
        int x = 6;
        int y = 18;
        int jump = 10;
        EntityPlayer pl = ModLoader.getMinecraftInstance().thePlayer;
        
        String s = buildString("Fire", pl.fireLvl, pl.fireExp);
        fontRenderer.drawString(s, x, y + jump * 0, 0xFF0000);
        
        s = buildString("Earth", pl.earthLvl, pl.earthExp);
        fontRenderer.drawString(s, x, y + jump * 1, 0x996600);
        
        s = buildString("Water", pl.waterLvl, pl.waterExp);
        fontRenderer.drawString(s, x, y + jump * 2, 0x1A0099);
        
        s = buildString("Wind", pl.windLvl, pl.windExp);
        fontRenderer.drawString(s, x, y + jump * 3, 0x8FFFFF);
        
        s = buildString("Essences", pl.essensLvl, pl.essensExp);
        fontRenderer.drawString(s, x, y + jump * 4, 0xB114FF);
        
        zLevel = 0.0F;
        magicRenderer.zLevel = 0.0F;
        
        GL11.glPopMatrix();
        super.drawScreen(par1, par2, par3);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everythin in front of the magics)
     */
    protected void drawGuiContainerForegroundLayer()
    {
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the magics)
     */
    protected abstract void drawGuiContainerBackgroundLayer(float f, int i, int j);

    /**
     * Draws an inventory slot
     */
    private void drawSlotInventory(MagicSlot par1Slot)
    {
        int i = par1Slot.xDisplayPosition;
        int j = par1Slot.yDisplayPosition;
        MagicStack magicstack = par1Slot.getStack();
        boolean flag = false;
        int k = i;
        int l = j;
        zLevel = 100F;
        magicRenderer.zLevel = 100F;

        if (magicstack == null)
        {
            int i1 = par1Slot.getBackgroundIconIndex();

            if (i1 >= 0)
            {
                GL11.glDisable(GL11.GL_LIGHTING);
                mc.renderEngine.bindTexture(mc.renderEngine.getTexture("/gui/items.png"));
                drawTexturedModalRect(k, l, (i1 % 16) * 16, (i1 / 16) * 16, 16, 16);
                GL11.glEnable(GL11.GL_LIGHTING);
                flag = true;
            }
        }

        if (!flag)
        {
            magicRenderer.renderMagicIntoGUI(fontRenderer, mc.renderEngine, magicstack, k, l);
            magicRenderer.renderMagicOverlayIntoGUI(fontRenderer, mc.renderEngine, magicstack, k, l);
        }

        magicRenderer.zLevel = 0.0F;
        zLevel = 0.0F;
    }

    /**
     * Returns the slot at the given coordinates or null if there is none.
     */
    private MagicSlot getSlotAtPosition(int par1, int par2)
    {
        for (int i = 0; i < inventorySlots.inventoryMagicSlots.size(); i++)
        {
            MagicSlot slot = (MagicSlot)inventorySlots.inventoryMagicSlots.get(i);

            if (isMouseOverSlot(slot, par1, par2))
            {
                return slot;
            }
        }

        return null;
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);

        if (par3 == 0 || par3 == 1)
        {
            MagicSlot slot = getSlotAtPosition(par1, par2);
            int i = guiLeft;
            int j = guiTop;
            boolean flag = par1 < i || par2 < j || par1 >= i + xSize || par2 >= j + ySize;
            int k = -1;

            if (slot != null)
            {
                k = slot.slotNumber;
            }

            if (flag)
            {
                k = -999;
            }

            if (k != -1)
            {
                boolean flag1 = k != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
                handleMouseClick(slot, k, par3, flag1);
            }
        }
    }

    /**
     * Returns if the passed mouse position is over the specified slot.
     */
    private boolean isMouseOverSlot(MagicSlot par1Slot, int par2, int par3)
    {
        int i = guiLeft;
        int j = guiTop;
        par2 -= i;
        par3 -= j;
        return par2 >= par1Slot.xDisplayPosition - 1 && par2 < par1Slot.xDisplayPosition + 16 + 1 && par3 >= par1Slot.yDisplayPosition - 1 && par3 < par1Slot.yDisplayPosition + 16 + 1;
    }

    protected void handleMouseClick(MagicSlot par1Slot, int par2, int par3, boolean par4)
    {
        if (par1Slot != null)
        {
            par2 = par1Slot.slotNumber;
        }

        mc.playerController.magicWindowClick(inventorySlots.windowId, par2, par3, par4, mc.thePlayer);
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2)
    {
        if (par2 == 1 || par2 == mc.gameSettings.keyBindInventory.keyCode)
        {
            mc.thePlayer.closeScreen();
        }
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        if (mc.thePlayer == null)
        {
            return;
        }
        else
        {
            inventorySlots.onCraftGuiClosed(mc.thePlayer);
            mc.playerController.func_20086_a(inventorySlots.windowId, mc.thePlayer);
            return;
        }
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();

        if (!mc.thePlayer.isEntityAlive() || mc.thePlayer.isDead)
        {
            mc.thePlayer.closeScreen();
        }
    }
}
