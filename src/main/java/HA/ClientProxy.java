package HA;

import HA.Fluiddder.FluidColor;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;

public class ClientProxy extends CommonProxy {
    public static boolean unColored = false;

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        ((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(FluidColor.instance);
        if (unColored) FluidColor.setAllColor();
    }
}
