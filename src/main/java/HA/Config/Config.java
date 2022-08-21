package HA.Config;

import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Config {
    static Configuration conf;
    Map<Property,Boolean> map=new HashMap<>();
    public static boolean needRefreshFluid=false;
    public static boolean alwaysRefreshFluid=false;
    public static boolean needRefreshRecipe=false;
    public static boolean alwaysRefreshRecipe=false;
    public static boolean allcustomMode=false;

    public static Property nFluid;
    public static Property aFluid;
    public static Property nRecipe;
    public static Property aRecipe;
    public static Property custom;

    public Config(File filepath){
        conf=new Configuration(filepath);
        conf.load();
        nFluid=loadProp(conf,"Fluid","RefreshFluid",false, StatCollector.translateToLocal("desc.RefreshFluid"));

        aFluid=loadProp(conf,"Fluid","AlwaysRefreshFluid",false, StatCollector.translateToLocal("desc.AlwaysRefreshFluid"));

        nRecipe=loadProp(conf,"Fluid","RefreshRecipe",false, StatCollector.translateToLocal("desc.RefreshRecipe"));

        aRecipe=loadProp(conf,"Fluid","AlwaysRefreshRecipe",false, StatCollector.translateToLocal("desc.AlwaysRefreshRecipe"));

        custom=loadProp(conf,"Common","AllCustomMode",false, StatCollector.translateToLocal("desc.AllCustomMode"));
        auto();
        if(conf.hasChanged())conf.save();
    }

    static void auto(){
        needRefreshFluid= nFluid.getBoolean();
        needRefreshRecipe=nRecipe.getBoolean();
        alwaysRefreshFluid=aFluid.getBoolean();
        alwaysRefreshRecipe=aRecipe.getBoolean();
        allcustomMode=custom.getBoolean();
    }

    static Property loadProp(Configuration conf,String category, String key, boolean default_, String desc) {
        Property prop = conf.get(category, key, default_);
        prop.comment = desc+"(Default:"+default_+")";
        return prop;
    }

    public static void Set(Property property,Boolean bl){
        property.set(bl);
        auto();
        conf.save();
    }
}
