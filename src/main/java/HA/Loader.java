package HA;

import HA.Config.Config;
import HA.Converter.TransferRecipe;
import HA.Fluiddder.FluidAdder;
import HA.Fluiddder.Storage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import ventivu.api.IReload;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static HA.jsonHelper.JsonReads;
import static HA.jsonHelper.creatFile;

@Optional.Interface(iface = "ventivu.api.IReload", modid = "magcore")
public class Loader implements IReload {
    static HashMap<String, String> en_USnames = new HashMap<>(), zh_CNnames = new HashMap<>();
    static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String Fluids = "HAFluids", Recipes = "HARecipes";

    public static void setFolder(File folder) {
        jsonHelper.setFolder(folder);
    }

    public static void loadFluidFromJson(Boolean first) {
        if (Config.alwaysRefreshFluid && !Config.needRefreshFluid) Config.set(Config.aFluid, false);
        String json = JsonReads(Fluids);
        if (json == null || Config.needRefreshFluid) {
            creatFile(Fluids, gson.toJson(Storage.sample()));
            if (Config.needRefreshFluid) Config.needRefreshFluid = false;
            if (first) loadFluidFromJson(false);
            return;
        }
        Storage.Model[] models = gson.fromJson(json, HBMAddon.isClient() ? Storage.TexturedModel[].class : Storage.Model[].class);

        List<Storage.Model> list = new ArrayList<>();
        boolean needRefresh = false;

        if (Config.allcustomMode) list = Arrays.asList(models);
        else for (Storage.Model model : models) {
            if (FluidRegistry.getFluid(model.name) != null) list.add(model);
            else needRefresh = true;
        }
        if (!Config.allcustomMode && needRefresh) creatFile(Fluids, gson.toJson(list.toArray()));
        Storage.storage.addAll((list));
        makeLocalized(list);
    }

    public static void loadRecipeFromJson(boolean first) {
        if (Config.alwaysRefreshRecipe && !Config.needRefreshRecipe) Config.set(Config.nRecipe, false);
        String json = JsonReads(Recipes);
        if (json == null || Config.needRefreshRecipe) {
            creatFile(Recipes, gson.toJson(TransferRecipe.sample()));
            if (Config.needRefreshRecipe) Config.needRefreshRecipe = false;
            if (first) {
                ClientProxy.unColored = true;
                loadRecipeFromJson(false);
            }
            return;
        }
        TransferRecipe.RecipeContainer[] recipes = gson.fromJson(json, TransferRecipe.RecipeContainer[].class);

        List<TransferRecipe.RecipeContainer> list = new ArrayList<>();
        boolean needRefresh = false;

        if (Config.allcustomMode) list = Arrays.asList(recipes);
        else for (TransferRecipe.RecipeContainer recipe : recipes) {
            if (recipe.getInput() != null) list.add(recipe);
            else needRefresh = true;
        }
        if (!Config.allcustomMode && needRefresh) creatFile(Recipes, gson.toJson(list.toArray()));
        TransferRecipe.storage.addAll((list));
    }

    public static void makeLocalized(List<Storage.Model> list) {
        for (Storage.Model model : list) {
            String unlocal = "hbmfluid." + model.name;
            Fluid fluid = FluidRegistry.getFluid(model.name);
            if (fluid == null|| en_USnames.containsKey(unlocal)) continue;
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

    public static void recreat(List<Storage.Model> list) {
        creatFile(Fluids, gson.toJson(list.toArray()));
    }

    @Override
    @Optional.Method(modid = "magcore")
    public void reload() {
        Storage.storage.clear();
        TransferRecipe.rollBack();
        loadFluidFromJson(true);
        loadRecipeFromJson(true);
        FluidAdder.reBuild();
        TransferRecipe.Construct();
    }
}
