package HA.Converter;

import HA.HBMAddon;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

import java.util.List;

public class ConverterBlock extends Block {
    IIcon[] top = new IIcon[8], side = new IIcon[8], down = new IIcon[8];

    public ConverterBlock() {
        super(Material.iron);
        this.setBlockName("converter");
        GameRegistry.registerBlock(this, ConverterBlockItem.class, "converter");
        GameRegistry.registerTileEntity(TileConverter.class, "Converter");
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        for (int i = 0; i < top.length; i++) {
            top[i] = register.registerIcon(HBMAddon.MODID + ":transfer_top." + i);
            side[i] = register.registerIcon(HBMAddon.MODID + ":transfer_side." + i);
            down[i] = register.registerIcon(HBMAddon.MODID + ":transfer_down." + i);
        }
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        switch (ForgeDirection.getOrientation(side)) {
            case DOWN:
                return down[meta];
            case UP:
                return top[meta];
            default:
                return this.side[meta];
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
        list.add(new ItemStack(item, 1, 2));
        list.add(new ItemStack(item, 1, 3));
        list.add(new ItemStack(item, 1, 4));
        list.add(new ItemStack(item, 1, 5));
        list.add(new ItemStack(item, 1, 6));
        list.add(new ItemStack(item, 1, 7));
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileConverter(metadata);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        TileConverter tile = (TileConverter) world.getTileEntity(x, y, z);
        ItemStack current = player.getCurrentEquippedItem();
        if (current != null) {
            if (!world.isRemote) {
                return handleRightClick(tile, ForgeDirection.getOrientation(side), player, true, false);
            } else return FluidContainerRegistry.isContainer(current);
        }
        return false;
    }

    public static boolean handleRightClick(IFluidHandler tank, ForgeDirection side, EntityPlayer player, boolean fill, boolean drain) {
        if (player == null || tank == null) return false;
        ItemStack current = player.inventory.getCurrentItem();
        if (current != null) {
            FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(current);
            if (fill && liquid != null) {
                int used = tank.fill(side, liquid, true);
                if (used > 0) {
                    if (!player.capabilities.isCreativeMode) {
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, consumeItem(current));
                        player.inventory.markDirty();
                    }
                    return true;
                }
            } else if (drain) {
                FluidStack available = tank.drain(side, 2147483647, false);
                if (available != null) {
                    ItemStack filled = FluidContainerRegistry.fillFluidContainer(available, current);
                    liquid = FluidContainerRegistry.getFluidForFilledItem(filled);
                    if (liquid != null) {
                        if (current.stackSize > 1) {
                            if (!player.inventory.addItemStackToInventory(filled)) return false;
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, consumeItem(current));
                            player.inventory.markDirty();
                        } else {
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, consumeItem(current));
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, filled);
                            player.inventory.markDirty();
                        }
                        tank.drain(side, liquid.amount, true);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static ItemStack consumeItem(ItemStack stack) {
        if (stack.stackSize == 1) {
            if (stack.getItem().hasContainerItem(stack)) return stack.getItem().getContainerItem(stack);
            return null;
        }
        stack.splitStack(1);
        return stack;
    }
}
