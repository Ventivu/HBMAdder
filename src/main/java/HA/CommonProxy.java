package HA;

import HA.Config.Config;
import HA.Converter.ConverterBlock;
import HA.temp.BlockCounter;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import net.minecraft.block.Block;

public class CommonProxy {
    Block transfer;
    //Block counter;

    public void preInit(FMLPreInitializationEvent event) {
        new Config(event.getSuggestedConfigurationFile());
        new Event();
        transfer = new ConverterBlock();
        //counter = new BlockCounter();
    }

    public void init(FMLInitializationEvent event) {
        Loader.loadFluidFromJson(true);
        Loader.loadRecipeFromJson(true);
    }

    public void postInit(FMLPostInitializationEvent event) {

    }

    public void gameExit(FMLServerStoppingEvent event) {
        if (Config.first.getBoolean()) {
            Config.set(Config.first, false);
            Config.set(Config.custom, false);
        }
    }

}
