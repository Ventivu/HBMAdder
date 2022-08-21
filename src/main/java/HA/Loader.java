package HA;

import HA.Config.Config;
import HA.Fluiddder.Storage;
import HA.Transfer.TransferRecipe;
import com.google.gson.Gson;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static HA.jsonHelper.*;

public class Loader {
    static Gson gson = new Gson();
    private static final String Fluids = "HAFluids", Recipes = "HARecipes";

    static HashMap<String, String> en_USnames = new HashMap<>();
    static HashMap<String, String> zh_CNnames = new HashMap<>();

    public static void loadFluidFromJson(Boolean first) {
        if (Config.alwaysRefreshFluid && !Config.needRefreshFluid) Config.Set(Config.aFluid, false);
        String json = JsonReads(Fluids);
        if (json == null || Config.needRefreshFluid) {
            creatFile(Fluids, gson.toJson(Storage.sample()));
            if (first) loadFluidFromJson(false);
            if (!Config.alwaysRefreshFluid) Config.Set(Config.nFluid, false);
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
    }

    public static void loadRecipeFromJson(boolean first) {
        String json = JsonReads(Recipes);
        if (json == null) {
            creatFile(Recipes, gson.toJson(TransferRecipe.sample()));
            if (first) loadRecipeFromJson(false);
            return;
        }
        TransferRecipe.storage.addAll((Arrays.asList(gson.fromJson(json, TransferRecipe.RecipeContainer[].class))));
        TransferRecipe.Construct();
    }

    public static void makeLocalized(List<Storage.Model> list) {
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
