package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class BiomeGenAntartica extends BiomeGenBase
{
    public BiomeGenAntartica(int par1)
    {
        super(par1);
        spawnableCreatureList.clear();
        topBlock = (byte)mod_Antartica.AntarticaSnow.blockID;
        fillerBlock = (byte)mod_Antartica.AntarticaSnow.blockID;
        biomeDecorator.treesPerChunk = -999;
        biomeDecorator.deadBushPerChunk = 0;
        biomeDecorator.reedsPerChunk = 0;
        biomeDecorator.cactiPerChunk = 0;
    }
}