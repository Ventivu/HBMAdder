package HA.Fluiddder;

import HA.HBMAddon;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.render.util.EnumSymbol;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class FluidAdder extends Fluids {
    static List<String> namespace=new ArrayList<>();
    public static void construct() {
        for(int a=0;a<Storage.storage.size();a++){
            Storage.Model model=Storage.storage.get(a);
            if(namespace.contains(model.name)) {
                System.out.println("Duplicate name: " + model.name);
                continue;
            }
            int rgb=model.RGB[0]<<16|model.RGB[1]<<8|model.RGB[2];
            OuterTextureFluid fluid=new OuterTextureFluid(model.name, rgb, model.poison,model.flammability,model.reactivity,EnumSymbol.NONE);
            if(HBMAddon.isClient())fluid.setNewLocation(((Storage.TexturedModel)model).resourceLocation);
            metaOrder.add(fluid);
            namespace.add(model.name);
        }
    }
    
    public static void reBuild(){
        for(Storage.Model model:Storage.storage){
            if(namespace.contains(model.name)&& Fluids.fromName(model.name) instanceof OuterTextureFluid){
                OuterTextureFluid fluid= (OuterTextureFluid) Fluids.fromName(model.name);
                if(HBMAddon.isClient())fluid.setNewLocation(((Storage.TexturedModel)model).resourceLocation);
                fluid.setColor(model.RGB);
                fluid.flammability= model.flammability;
                fluid.poison= model.poison;
                fluid.reactivity= model.reactivity;
            }
        }
    }
    

    static class OuterTextureFluid extends FluidType{
        ResourceLocation newLocation;
        int color;

        public OuterTextureFluid(String name, int color, int p, int f, int r, EnumSymbol symbol) {
            super(name, color, p, f, r, symbol);
            this.color=color;
        }

        @Override
        public ResourceLocation getTexture() {
            return newLocation==null?super.getTexture():newLocation;
        }

        public void setNewLocation(ResourceLocation newLocation) {
            this.newLocation = newLocation;
        }

        public int getColor() {
            return this.color;
        }
        
        public void setColor(int color){
            this.color=color;
        }
        
        public void setColor(int[] rgb){
            this.color=rgb[0]<<16|rgb[1]<<8|rgb[2];
        }
    }
}
