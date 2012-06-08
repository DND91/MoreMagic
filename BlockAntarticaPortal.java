package net.minecraft.src;

import java.util.List;
import java.util.ArrayList;

public class BlockAntarticaPortal extends BlockPortalBase
{
//Look in BlockPortalBase.java to see the full array of hooks you can use for your portal block.

public BlockAntarticaPortal(int i)
{
super(i, ModLoader.addOverride("/terrain.png", "/Blocks/PortalBlock.png"), Material.portal);
//Get a unique sprite index for the portal texture so that it doesn't override another
//texture.
}

public WorldProviderBase getDimension()
{
return new WorldProviderAntartica();
}

public Teleporter getTeleporter()
{
return new AntarticaTelepoter();
}

//You can get to this dimension from the overworld (0) and Nether (-1).


//You should probably make the portal non-solid so you can step into it...
public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
{
return null;
}

/**
* Updates the blocks bounds based on its current state. Args: world, x, y, z
*/
public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
{
if (par1IBlockAccess.getBlockId(par2 - 1, par3, par4) == blockID || par1IBlockAccess.getBlockId(par2 + 1, par3, par4) == blockID)
{
float f = 0.5F;
float f2 = 0.125F;
setBlockBounds(0.5F - f, 0.0F, 0.5F - f2, 0.5F + f, 1.0F, 0.5F + f2);
}
else
{
float f1 = 0.125F;
float f3 = 0.5F;
setBlockBounds(0.5F - f1, 0.0F, 0.5F - f3, 0.5F + f1, 1.0F, 0.5F + f3);
}
}
public List canTeleportFromDimension()
{
        ArrayList arraylist = new ArrayList();
        arraylist.add(Integer.valueOf(0));//player can teleport from overworld to this dimension
        arraylist.add(Integer.valueOf(-1));//player can teleport from Nether to this dimension
        return arraylist;
}
/**
* Is this block (a) opaque and ( a full 1m cube? This determines whether or not to render the shared face of two
* adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
*/
public boolean isOpaqueCube()
{
return false;
}

/**
* If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
*/
public boolean renderAsNormalBlock()
{
return false;
}

/**
* Checks to see if this location is valid to create a portal and will return True if it does. Args: world, x, y, z
*/
public boolean tryToCreatePortal(World par1World, int par2, int par3, int par4)
{
    int i = 0;
    int j = 0;

    if (par1World.getBlockId(par2 - 1, par3, par4) == mod_Antartica.AntarticaIce.blockID || par1World.getBlockId(par2 + 1, par3, par4) == mod_Antartica.MysticalStone.blockID)
    {
        i = 1;
    }

    if (par1World.getBlockId(par2, par3, par4 - 1) == mod_Antartica.MysticalStone.blockID || par1World.getBlockId(par2, par3, par4 + 1) == mod_Antartica.MysticalStone.blockID)
    {
        j = 1;
    }

    if (i == j)
    {
        return false;
    }

    if (par1World.getBlockId(par2 - i, par3, par4 - j) == 0)
    {
        par2 -= i;
        par4 -= j;
    }

    for (int k = -1; k <= 2; k++)
    {
        for (int i1 = -1; i1 <= 3; i1++)
        {
            boolean flag = k == -1 || k == 2 || i1 == -1 || i1 == 3;

            if ((k == -1 || k == 2) && (i1 == -1 || i1 == 3))
            {
                continue;
            }

            int k1 = par1World.getBlockId(par2 + i * k, par3 + i1, par4 + j * k);

            if (flag)
            {
                if (k1 != mod_Antartica.MysticalStone.blockID)
                {
                    return false;
                }

                continue;
            }

            if (k1 != 0 && k1 != Block.fire.blockID)
            {
                return false;
            }
        }
    }

    par1World.editingBlocks = true;

    for (int l = 0; l < 2; l++)
    {
        for (int j1 = 0; j1 < 3; j1++)
        {
            par1World.setBlockWithNotify(par2 + i * l, par3 + j1, par4 + j * l, mod_Antartica.ForbiddenPortal.blockID);
        }
    }

    par1World.editingBlocks = false;
    return true;
}

/**
 * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
 * their own) Args: x, y, z, neighbor blockID
 */
public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
{
    int i = 0;
    int j = 1;

    if (par1World.getBlockId(par2 - 1, par3, par4) == blockID || par1World.getBlockId(par2 + 1, par3, par4) == blockID)
    {
        i = 1;
        j = 0;
    }

    int k;

    for (k = par3; par1World.getBlockId(par2, k - 1, par4) == blockID; k--) { }

    if (par1World.getBlockId(par2, k - 1, par4) != mod_Antartica.MysticalStone.blockID)
    {
        par1World.setBlockWithNotify(par2, par3, par4, 0);
        return;
    }

    int l;

    for (l = 1; l < 4 && par1World.getBlockId(par2, k + l, par4) == blockID; l++) { }

    if (l != 3 || par1World.getBlockId(par2, k + l, par4) != mod_Antartica.MysticalStone.blockID)
    {
        par1World.setBlockWithNotify(par2, par3, par4, 0);
        return;
    }

    boolean flag = par1World.getBlockId(par2 - 1, par3, par4) == blockID || par1World.getBlockId(par2 + 1, par3, par4) == blockID;
    boolean flag1 = par1World.getBlockId(par2, par3, par4 - 1) == blockID || par1World.getBlockId(par2, par3, par4 + 1) == blockID;

    if (flag && flag1)
    {
        par1World.setBlockWithNotify(par2, par3, par4, 0);
        return;
    }

    if ((par1World.getBlockId(par2 + i, par3, par4 + j) != mod_Antartica.MysticalStone.blockID || par1World.getBlockId(par2 - i, par3, par4 - j) != blockID) && (par1World.getBlockId(par2 - i, par3, par4 - j) != mod_Antartica.MysticalStone.blockID || par1World.getBlockId(par2 + i, par3, par4 + j) != blockID))
    {
        par1World.setBlockWithNotify(par2, par3, par4, 0);
        return;
    }
    else
    {
        return;
    }
}

/**
* Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
* coordinates. Args: blockAccess, x, y, z, side
*/
public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
{
if (par1IBlockAccess.getBlockId(par2, par3, par4) == blockID)
{
return false;
}

boolean flag = par1IBlockAccess.getBlockId(par2 - 1, par3, par4) == blockID && par1IBlockAccess.getBlockId(par2 - 2, par3, par4) != blockID;
boolean flag1 = par1IBlockAccess.getBlockId(par2 + 1, par3, par4) == blockID && par1IBlockAccess.getBlockId(par2 + 2, par3, par4) != blockID;
boolean flag2 = par1IBlockAccess.getBlockId(par2, par3, par4 - 1) == blockID && par1IBlockAccess.getBlockId(par2, par3, par4 - 2) != blockID;
boolean flag3 = par1IBlockAccess.getBlockId(par2, par3, par4 + 1) == blockID && par1IBlockAccess.getBlockId(par2, par3, par4 + 2) != blockID;
boolean flag4 = flag || flag1;
boolean flag5 = flag2 || flag3;

if (flag4 && par5 == 4)
{
return true;
}

if (flag4 && par5 == 5)
{
return true;
}

if (flag5 && par5 == 2)
{
return true;
}

return flag5 && par5 == 3;
}



public boolean displayPortalOverlay()
{
return true;
}

public int getOverlayTexture()
{
return blockIndexInTexture;
}

public int getPortalDelay()
{
        return 85;//default
}



public String getEnteringMessage() {

	
	
return "Entering A Icy Wasteland";







}


public String getLeavingMessage() {
	
return "Exiting The Continent That Should Not Be One (Greonland/Baffin Island are good candidates";
}

}