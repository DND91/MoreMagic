package net.minecraft.src;

import java.util.Random;

public class WorldProviderAntartica extends WorldProviderBase

{

	public WorldProviderAntartica()
	{
		
	}
	
public int getDimensionID()
{
	return 65;
}

public boolean renderClouds()
{
	return true;
}

public boolean renderVoidFog()
{
	return true;
}

public float setSunSize()
{
	return 1.0F;
}


public float setMoonSize()
{
	return 1.0F;
}

public String getSunTexture()
{
	return "/sun.png";
}

public String getMoonTextre()
{
	return "/moon.png";
}


public boolean renderStars()
{
	return true;
}

public boolean darkenSkyDuringRain()
{
	return true;
}

public String getRespawnMessage()
{
	return "Leaving The Icy Hell Of Antartica";
}

public void registerWorldChunkManager()
{
	worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.Antartic, 1.0F, 0.0F);
}

public IChunkProvider getChunkProvider()
{
	return new ChunkProviderGenerate (worldObj, worldObj.getSeed(), false);
}


public boolean canRespawnHere()
{
	return false;
}

public float calculateCelestialAngle(long par1, float par3)
{
	return 1.0F;
}

}
