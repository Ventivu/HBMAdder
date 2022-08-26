package HA;

import HA.Config.Config;
import HA.Fluiddder.FluidColor;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;

public class ClientProxy extends CommonProxy{

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        ((IReloadableResourceManager)Minecraft.getMinecraft().getResourceManager()).registerReloadListener(FluidColor.instance);
        if(Config.nFluid.getBoolean())FluidColor.setAllColor();
    }
}
