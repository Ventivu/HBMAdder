package HA.Converter;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class ConverterBlockItem extends ItemBlock {
    public ConverterBlockItem(Block block) {
        super(block);
        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int meta) {
        return meta>=8?7:meta;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return StatCollector.translateToLocal("level."+stack.getItemDamage())+StatCollector.translateToLocal(getUnlocalizedName()+".name");
    }
}
