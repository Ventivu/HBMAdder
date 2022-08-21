package HA.Transfer;

import api.hbm.fluid.IFluidStandardTransceiver;
import com.hbm.interfaces.IFluidAcceptor;
import com.hbm.interfaces.IFluidSource;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.lib.Library;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

import java.util.ArrayList;
import java.util.List;

public class TileTransfer extends TileEntity implements IFluidHandler, IFluidStandardTransceiver, IFluidAcceptor, IFluidSource {
    static final int[] speed = new int[]{100, 500, 1000, 3000, 5000, 8000, 10000, -1};
    static final int[] capacity = new int[]{1000, 2000, 4000, 6000, 10000, 20000, 50000, Integer.MAX_VALUE};
    FluidTank input;
    com.hbm.inventory.fluid.tank.FluidTank output;
    int age = 0;
    List<IFluidAcceptor> list = new ArrayList<>();

    public TileTransfer(int level) {
        if (level > 7) level = 7;
        input = new FluidTank(capacity[level]);
        output = new com.hbm.inventory.fluid.tank.FluidTank(Fluids.NONE, capacity[level], 0);
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        //if (input.getFluid() == null || input.getFluid().isFluidEqual(resource)) {
        return input.fill(resource, doFill);
        // }
        //return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return (input.getFluid() == null || input.getFluid().getFluid() == fluid);
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[]{input.getInfo()};
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        NBTTagCompound inputTank = nbt.getCompoundTag("Input");
        NBTTagCompound outputTank = nbt.getCompoundTag("Output");
        input.readFromNBT(inputTank);
        output.readFromNBT(outputTank, "output");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        NBTTagCompound inputTank = new NBTTagCompound();
        NBTTagCompound outputTank = new NBTTagCompound();
        input.writeToNBT(inputTank);
        output.writeToNBT(outputTank, "output");
        nbt.setTag("Input", inputTank);
        nbt.setTag("Output", outputTank);
    }

    @Override
    public void updateEntity() {
        int a = getBlockMetadata();
        age = age >= 20 ? 0 : age + 1;
        unsubscribeToAllAround(output.getTankType(), worldObj, xCoord, yCoord, zCoord);
        if (age ==0) {
            if (input.getFluid() != null) {
                FluidType outputFluid = TransferRecipe.recipeMap.get(input.getFluid().getFluid());
                if (outputFluid != null && (output.getTankType() == Fluids.NONE || outputFluid == output.getTankType())) {
                    if (a != 7) {
                        int vir = input.drain(speed[a], false).amount;
                        if (output.getTankType() == Fluids.NONE) output.setTankType(outputFluid);
                        int free = output.getMaxFill() - output.getFill();
                        input.drain(Math.min(vir, free), true);
                        output.setFill(output.getFill() + Math.min(vir, free));
                        this.output.updateTank(xCoord, yCoord, zCoord, worldObj.provider.dimensionId);
                    }else {
                        if (output.getTankType() == Fluids.NONE) output.setTankType(outputFluid);
                        output.setFill(input.getFluidAmount());
                    }
                }
            }
        }
        sendFluidToAll(output.getTankType(), this);
        fillFluidInit(output.getTankType());
        if(a==7) {
            input.drain(input.getFluidAmount()-output.getFill(),true);
        }
        if (output.getFill() == 0 && output.getTankType() != Fluids.NONE) output.setTankType(Fluids.NONE);
    }

    //////////////////////////////////////////Forge End/////////////////////////////////////

    @Override
    public com.hbm.inventory.fluid.tank.FluidTank[] getSendingTanks() {
        return new com.hbm.inventory.fluid.tank.FluidTank[]{output};
    }

    @Override
    public com.hbm.inventory.fluid.tank.FluidTank[] getReceivingTanks() {
        return new com.hbm.inventory.fluid.tank.FluidTank[0];
    }

    @Override
    public int getMaxFluidFill(FluidType type) {
        return type == output.getTankType() ? output.getMaxFill() : 0;
    }

    @Override
    public void fillFluidInit(FluidType type) {
        this.fillFluid(this.xCoord + 1, this.yCoord, this.zCoord, this.getTact(), type);
        this.fillFluid(this.xCoord - 1, this.yCoord, this.zCoord, this.getTact(), type);
        this.fillFluid(this.xCoord, this.yCoord + 1, this.zCoord, this.getTact(), type);
        this.fillFluid(this.xCoord, this.yCoord - 1, this.zCoord, this.getTact(), type);
        this.fillFluid(this.xCoord, this.yCoord, this.zCoord + 1, this.getTact(), type);
        this.fillFluid(this.xCoord, this.yCoord, this.zCoord - 1, this.getTact(), type);
    }

    @Override
    public void fillFluid(int x, int y, int z, boolean newTact, FluidType type) {
        Library.transmitFluid(x, y, z, newTact, this, worldObj, type);
    }

    @Override
    public boolean getTact() {
        return age >= 0 && age < 10;
    }

    @Override
    public List<IFluidAcceptor> getFluidList(FluidType fluidType) {
        return list;
    }

    @Override
    public void clearFluidList(FluidType fluidType) {
        list.clear();
    }

    @Override
    public void setFillForSync(int fill, int index) {
        output.setFill(fill);
    }

    @Override
    public void setFluidFill(int i, FluidType type) {
        if (type == output.getTankType()) output.setFill(i);
    }

    @Override
    public void setTypeForSync(FluidType type, int index) {
        output.setTankType(type);
    }

    @Override
    public int getFluidFill(FluidType type) {
        return type == output.getTankType() ? output.getFill() : 0;
    }
}
