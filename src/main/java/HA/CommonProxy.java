
package HA;

import HA.Config.Config;
import HA.Fluiddder.FluidAdder;
import HA.Transfer.TransforBlock;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.block.Block;

public class CommonProxy {
    Block transfer;

    public void preInit(FMLPreInitializationEvent event) {
        new Config(event.getSuggestedConfigurationFile());
        transfer=new TransforBlock();

    }

    public void init(FMLInitializationEvent event) {
        Loader.loadFluidFromJson(true);
        FluidAdder.construct();
        Loader.loadRecipeFromJson(true);
    }

    public void postInit(FMLPostInitializationEvent event) {

    }

}
