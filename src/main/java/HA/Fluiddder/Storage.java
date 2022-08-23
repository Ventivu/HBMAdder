package HA.Fluiddder;

import com.hbm.inventory.fluid.Fluids;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Storage {
    public static final List<Model> storage = new ArrayList<>();

    public static Model[] sample() {
        List<Model> list = new ArrayList<>();
        Map<String, Fluid> forgecache = FluidRegistry.getRegisteredFluids();
        for (String name : forgecache.keySet()) {
            String Hname = name.toUpperCase();
            if(name.equals("schrabidic_fluid"))continue;
            if (Fluids.fromName(Hname) == Fluids.NONE && Fluids.fromName(name) == Fluids.NONE) {
                Fluid forgefluid = forgecache.get(name);
                int color = forgefluid.getColor();
                list.add(new Model(name, color, 0, forgefluid.getTemperature(), 0));
            }
        }
        return list.toArray(new Model[0]);
    }

    public static class Model {
        public String name;
        public int poison, flammability, reactivity;
        public int[] RGB = new int[3];

        public Model(String name, int[] rgb, int poison, int flammability, int reactivity) {
            this.name = name;
            this.RGB = rgb;
            this.poison = poison;
            this.flammability = flammability;
            this.reactivity = reactivity;
        }

        public Model(String name, int rgb, int poison, int flammability, int reactivity) {
            this.name = name;
            this.RGB[0] = rgb >> 16;
            this.RGB[1] = rgb >> 8 & 0xff;
            this.RGB[2] = rgb & 0xff;
            this.poison = poison;
            this.flammability = flammability;
            this.reactivity = reactivity;
        }
    }
}
