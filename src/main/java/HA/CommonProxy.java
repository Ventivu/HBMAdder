package HA;

import HA.Config.Config;
import HA.Converter.ConverterBlock;
import HA.Converter.TransferRecipe;
import HA.Fluiddder.FluidAdder;
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
        Loader.setFolder(event.getModConfigurationDirectory());
        transfer = new ConverterBlock();
        //counter = new BlockCounter();
    }

    public void init(FMLInitializationEvent event) {
        Loader.loadFluidFromJson(true);
    }

    public void postInit(FMLPostInitializationEvent event) {
        FluidAdder.construct();
        Loader.loadRecipeFromJson(true);
        TransferRecipe.Construct();
    }

    public void gameExit(FMLServerStoppingEvent event) {
        if (Config.first.getBoolean()) {
            Config.set(Config.first, false);
            Config.set(Config.custom, false);
        }
        if (!Config.alwaysRefreshFluid) Config.set(Config.nFluid, false);
        if(!Config.alwaysRefreshRecipe)Config.set(Config.nRecipe, false);
    }
}
