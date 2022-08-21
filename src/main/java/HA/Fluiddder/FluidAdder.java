package HA.Fluiddder;

import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.render.util.EnumSymbol;

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
            metaOrder.add(new FluidType(model.name, rgb, model.poison,model.flammability,model.reactivity,EnumSymbol.NONE));
            namespace.add(model.name);
        }
    }
}
