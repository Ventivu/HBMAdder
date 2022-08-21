package HA.Transfer;

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
        RecipeContainer[] a = new RecipeContainer[2];
        a[0] = new RecipeContainer("water", "WATER");
        a[1] = new RecipeContainer("lava", "LAVA");
        return a;
    }

    public static void Construct() {
        recipeMap.put(null, null);
        for (RecipeContainer container : storage) {
            recipeMap.put(container.getInput(), container.getOutput());
        }
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

