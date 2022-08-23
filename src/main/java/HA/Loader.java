package HA;

import HA.Config.Config;
import HA.Fluiddder.FluidAdder;
import HA.Fluiddder.Storage;
import HA.Converter.TransferRecipe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static HA.jsonHelper.JsonReads;
import static HA.jsonHelper.creatFile;

public class Loader {
    static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String Fluids = "HAFluids", Recipes = "HARecipes";

    public static void loadFluidFromJson(Boolean first) {
        if (Config.alwaysRefreshFluid && !Config.needRefreshFluid) Config.set(Config.aFluid, false);
        String json = JsonReads(Fluids);
        if (json == null || Config.needRefreshFluid) {
            creatFile(Fluids, gson.toJson(Storage.sample()));
            if (first) loadFluidFromJson(false);
            if (!Config.alwaysRefreshFluid) Config.set(Config.nFluid, false);
            return;
        }
        Storage.Model[] models = gson.fromJson(json, Storage.Model[].class);

        List<Storage.Model> list = new ArrayList<>();
        boolean needRefresh = false;
        for (Storage.Model model : models) {
            if (!Config.allcustomMode && FluidRegistry.getFluid(model.name) != null) list.add(model);
            else needRefresh = true;
        }
        if (!Config.allcustomMode && needRefresh) creatFile(Fluids, gson.toJson(list.toArray()));
        Storage.storage.addAll((list));
        makeLocalized(list);
        FluidAdder.construct();
    }

    public static void loadRecipeFromJson(boolean first) {
        if (Config.alwaysRefreshRecipe && !Config.needRefreshRecipe) Config.set(Config.nRecipe, false);
        String json = JsonReads(Recipes);
        if (json == null||Config.needRefreshRecipe) {
            creatFile(Recipes, gson.toJson(TransferRecipe.sample()));
            if (first) loadRecipeFromJson(false);
            if(!Config.alwaysRefreshRecipe)Config.set(Config.nRecipe,false);
            return;
        }
        TransferRecipe.RecipeContainer[] recipes=gson.fromJson(json, TransferRecipe.RecipeContainer[].class);

        List<TransferRecipe.RecipeContainer> list=new ArrayList<>();
        boolean needRefresh = false;
        for (TransferRecipe.RecipeContainer recipe:recipes) {
            if (!Config.allcustomMode && recipe.getInput() != null&&recipe.getOutput()!= com.hbm.inventory.fluid.Fluids.NONE) list.add(recipe);
            else needRefresh = true;
        }
        if (!Config.allcustomMode && needRefresh) creatFile(Fluids, gson.toJson(list.toArray()));

        TransferRecipe.storage.addAll((list));
        TransferRecipe.Construct();
    }

    public static void makeLocalized(List<Storage.Model> list) {
        HashMap<String, String> en_USnames = new HashMap<>();
        HashMap<String, String> zh_CNnames = new HashMap<>();
        for (Storage.Model model : list) {
            String unlocal = "hbmfluid." + model.name;
            Fluid fluid = FluidRegistry.getFluid(model.name);
            if (fluid == null) continue;
            String temp = fluid.getUnlocalizedName();
            if (StatCollector.canTranslate(temp)) {
                en_USnames.put(unlocal, LanguageRegistry.instance().getStringLocalization(temp, "en_US"));
                zh_CNnames.put(unlocal, LanguageRegistry.instance().getStringLocalization(temp, "zh_CN"));
            } else {
                en_USnames.put(unlocal, model.name);
                zh_CNnames.put(unlocal, model.name);
            }
        }

        LanguageRegistry.instance().injectLanguage("en_US", en_USnames);
        LanguageRegistry.instance().injectLanguage("zh_CN", zh_CNnames);
    }
}
