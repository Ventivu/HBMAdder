package HA.Fluiddder;

import HA.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import javax.imageio.ImageIO;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

@SideOnly(Side.CLIENT)
public class FluidColor extends ColorSpace implements IResourceManagerReloadListener {
    static IResourceManager resourcemanager;
    public static FluidColor instance=new FluidColor();
    private static final ColorSpace CIEXYZ = ColorSpace.getInstance(1001);

    public FluidColor() {
        super(1,3);
    }

    @Override
    public void onResourceManagerReload(IResourceManager manager) {
        resourcemanager=manager;
    }

    public static void setAllColor(){
        for (Storage.Model model:Storage.storage){
            Fluid fluid=FluidRegistry.getFluid(model.name);
            int dcolor=getIconColor(fluid.getIcon(),fluid.getColor());
            model.RGB[0] = dcolor >> 16;
            model.RGB[1] = dcolor >> 8 & 0xff;
            model.RGB[2] = dcolor & 0xff;
        }
        Loader.recreat(Storage.storage);
    }

    public static int getIconColor(IIcon icon, int defaultColor) {
        Integer integer;
        if (icon == null)
            return defaultColor;
        String t = icon.getIconName();
        try {
            integer = readIconCol(t);
        } catch (IOException e) {
            integer = null;
        }
        if (integer == null) return defaultColor;
        float r = (integer >> 16 & 0xFF) / 255.0F * (defaultColor >> 16 & 0xFF) / 255.0F;
        float g = (integer >> 8 & 0xFF) / 255.0F * (defaultColor >> 8 & 0xFF) / 255.0F;
        float b = (integer & 0xFF) / 255.0F * (defaultColor & 0xFF) / 255.0F;
        integer = (int) (r * 255.0F) << 16 | (int) (g * 255.0F) << 8 | (int) (b * 255.0F);
        return integer;
    }

    public static Integer readIconCol(String t) throws IOException {

        String s1 = "minecraft";
        String s2 = t;
        int i = t.indexOf(':');
        if (i >= 0) {
            s2 = t.substring(i + 1);
            if (i > 1)
                s1 = t.substring(0, i);
        }
        s1 = s1.toLowerCase();
        s2 = "textures/blocks/" + s2 + ".png";
        IResource resource = resourcemanager.getResource(new ResourceLocation(s1, s2));
        InputStream inputstream = resource.getInputStream();
        BufferedImage bufferedimage = ImageIO.read(inputstream);
        int height = bufferedimage.getHeight();
        int width = bufferedimage.getWidth();
        int[] aint = new int[height * width];
        bufferedimage.getRGB(0, 0, width, height, aint, 0, width);
        if (aint.length == 0)
            return null;
        float[] lab = new float[3];
        for (int l : aint) {
            float[] f = instance.fromRGB(l);
            for (int k = 0; k < 3; k++)
                lab[k] = lab[k] + f[k];
        }
        for (int j = 0; j < 3; j++)
            lab[j] = lab[j] / aint.length;
        float[] col = instance.toRGB(lab);
        return 0xFF000000 | (int) (col[0] * 255.0F) << 16 | (int) (col[1] * 255.0F) << 8 | (int) (col[2] * 255.0F);
    }

    @Override
    public float[] toRGB(float[] colorvalue) {
        float[] xyz = toCIEXYZ(colorvalue);
        return CIEXYZ.toRGB(xyz);
    }

    public float[] fromRGB(float[] rgbvalue) {
        float[] xyz = CIEXYZ.fromRGB(rgbvalue);
        return fromCIEXYZ(xyz);
    }

    public float[] fromRGB(int r, int g, int b) {
        return fromRGB(new float[] { r * 0.003921569F, g * 0.003921569F, b * 0.003921569F });
    }

    public float[] fromRGB(int col) {
        return fromRGB(new float[] { ((col & 0xFF0000) >> 16) * 0.003921569F, ((col & 0xFF00) >> 8) * 0.003921569F, (col & 0xFF) * 0.003921569F });
    }

    @Override
    public float[] toCIEXYZ(float[] colorvalue) {
        double i = (colorvalue[0] + 16.0D) * 0.008620689655172414D;
        double X = fInv(i + colorvalue[1] * 0.002D) * 0.95047D;
        double Y = fInv(i);
        double Z = fInv(i - colorvalue[2] * 0.005D) * 1.08883D;
        return new float[] { (float)X, (float)Y, (float)Z };
    }

    @Override
    public float[] fromCIEXYZ(float[] colorvalue) {
        double l = f(colorvalue[1] * 1.0D);
        double L = 116.0D * l - 16.0D;
        double a = 500.0D * (f(colorvalue[0] * 1.0521110608435826D) - l);
        double b = 200.0D * (l - f(colorvalue[2] * 0.9184170164304805D));
        return new float[]{(float) L, (float) a, (float) b};
    }

    private static double fInv(double x) {
        if (x > 0.20689655172413793D)
            return x * x * x;
        return 0.12841854934601665D * (x - 0.13793103448275862D);
    }

    private static double f(double x) {
        if (x > 0.008856451679035631D)
            return Math.cbrt(x);
        return 7.787037037037037D * x + 0.13793103448275862D;
    }
}
