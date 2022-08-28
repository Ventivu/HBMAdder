package HA.Converter;

import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransferRecipe {
    public static final List<RecipeContainer> storage = new ArrayList<>();
    public static final Map<Fluid, FluidType> recipeMap = new HashMap<>();

    public static RecipeContainer[] sample() {
        List<RecipeContainer> list = new ArrayList<>();
        Map<String, Fluid> forgecache = FluidRegistry.getRegisteredFluids();
        for (String name : forgecache.keySet()) {
            String Hname = name.toUpperCase();
            //TODO:喊BOB改了他
            if(name.equals("schrabidic_fluid")) {
                list.add(new RecipeContainer(name,Fluids.SCHRABIDIC.getName()));
                continue;
            }
            if (Fluids.fromName(Hname) != Fluids.NONE || Fluids.fromName(name) != Fluids.NONE) {

                list.add(new RecipeContainer(name,Fluids.fromName(Hname) == Fluids.NONE?name:Hname));
            }
        }
        return list.toArray(new RecipeContainer[0]);
    }

    public static void Construct() {
        recipeMap.put(null, null);
        for (RecipeContainer container : storage) {
            recipeMap.put(container.getInput(), container.getOutput());
        }
    }

    public static void rollBack(){
        storage.clear();
        recipeMap.clear();
    }

    public static class RecipeContainer {
        String input;
        String output;

        public RecipeContainer(String input, String output) {
            this.input = input;
            this.output = output;
        }

        public Fluid getInput() {
            return FluidRegistry.getFluid(input);
        }

        public FluidType getOutput() {
            return Fluids.fromName(output);
        }
    }
}

