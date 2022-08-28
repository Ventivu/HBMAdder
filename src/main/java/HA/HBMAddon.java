package HA;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ventivu.core.Core.Commands;

@Mod(modid = HBMAddon.MODID, name = HBMAddon.MODNAME, version = HBMAddon.VERSION, dependencies = "required-after:hbm@[1.0.27,);required-after:Forge@[10.13.2,);after:magcore")

public class HBMAddon {

    public static final String MODID = "HA";
    public static final String MODNAME = "HBMAddon";
    public static final String VERSION = "0.0.7.1";
    public static boolean libLoaded=false;

    @SidedProxy(clientSide = "HA.ClientProxy", serverSide = "HA.CommonProxy")
    public static CommonProxy proxy;

    @Instance(value = HBMAddon.MODID)
    public static HBMAddon instance;

    public static final Logger log = LogManager.getLogger("HBMAddon");

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
        libLoaded=Loader.isModLoaded("magcore");
    }

    @EventHandler
    public void init(FMLInitializationEvent evt) {
        proxy.init(evt);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent evt) {
        proxy.postInit(evt);
        if(libLoaded) {
            Commands.addVersionMessage(MODNAME, VERSION);
            Commands.addReloadControl(MODNAME ,new HA.Loader());
        }
    }

    @EventHandler
    public void Exit(FMLServerStoppingEvent event){proxy.gameExit(event);}

    public static boolean isClient(){
        return FMLCommonHandler.instance().getEffectiveSide().isClient();
    }
}
