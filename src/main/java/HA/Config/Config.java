package HA.Config;

import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;

public class Config extends Configuration{
    static Config instance;
    static final String Fluid="HAFluid.json",Recipe="HARecipes.json";
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
    public static Property first;

    public Config(File filepath){
        super(filepath);
        instance=this;
        load();
        String fluidString =buildStrings("FluidInfo.1","FluidInfo.2","FluidInfo.3","FluidInfo.4");
        String commonString =buildStrings("CommonInfo.1","CommonInfo.2","CommonInfo.3","CommonInfo.4");
        addCustomCategoryComment("Fluid", fluidString);
        addCustomCategoryComment("Common",commonString);
        addCustomCategoryComment("Don't touch it!","请不要随意变动该范围内的属性，这是用作判定的记号");

        custom=loadProp(this,"Common","AllCustomMode",true, StatCollector.translateToLocal("desc.AllCustomMode.1")+"\n"+StatCollector.translateToLocal("desc.AllCustomMode.2")+"\n"+StatCollector.translateToLocal("desc.AllCustomMode.3"));

        nFluid=loadProp(this,"Fluid","RefreshFluid",false, StatCollector.translateToLocal("desc.RefreshFluid")+"\n"+StatCollector.translateToLocalFormatted("desc.warnN",Fluid));

        aFluid=loadProp(this,"Fluid","AlwaysRefreshFluid",false, StatCollector.translateToLocal("desc.AlwaysRefreshFluid")+"\n"+StatCollector.translateToLocalFormatted("desc.warnA","RefreshFluid",Fluid));

        nRecipe=loadProp(this,"Fluid","RefreshRecipe",false, StatCollector.translateToLocal("desc.RefreshRecipe")+"\n"+StatCollector.translateToLocalFormatted("desc.warnN",Recipe));

        aRecipe=loadProp(this,"Fluid","AlwaysRefreshRecipe",false, StatCollector.translateToLocal("desc.AlwaysRefreshRecipe")+"\n"+StatCollector.translateToLocalFormatted("desc.warnA","RefreshRecipe",Recipe));

        first=loadProp(this,"Don't touch it!","firstSetup",true);
        auto();
        if(this.hasChanged())this.save();
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

    static Property loadProp(Configuration conf,String category, String key, boolean default_) {
        Property prop = conf.get(category, key, default_);
        return prop;
    }

    public static void set(Property property, Boolean bl){
        property.set(bl);
        auto();
        instance.save();
    }

    String buildStrings(String... strs){
        StringBuilder builder=new StringBuilder();
        for(String str:strs){
            String ss=StatCollector.translateToLocal(str);
            builder.append(ss);
            builder.append("\n");
        }
        builder.delete(builder.length()-2,builder.length());
        return builder.toString();
    }
}
