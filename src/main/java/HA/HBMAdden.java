package HA;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = HBMAdden.MODID, name = HBMAdden.MODNAME, version = HBMAdden.VERSION, dependencies = "required-after:Forge@[10.13.2,);")

public class HBMAdden {

    public static final String MODID = "HA";
    public static final String MODNAME = "HBMAdden";
    public static final String VERSION = "0.0.3";

    @SidedProxy(clientSide = "HA.CommonProxy", serverSide = "HA.CommonProxy")
    public static CommonProxy proxy;

    @Instance(value = HBMAdden.MODID)
    public static HBMAdden instance;

    public static final Logger log = LogManager.getLogger("HBMFluidAdden");

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent evt) {
        proxy.init(evt);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent evt) {
        proxy.postInit(evt);
    }

}
