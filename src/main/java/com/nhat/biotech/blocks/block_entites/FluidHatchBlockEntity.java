package com.nhat.biotech.blocks.block_entites;

import com.nhat.biotech.blocks.IOHatchBlock;
import com.nhat.biotech.networking.FluidPacket;
import com.nhat.biotech.networking.ModPackets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class FluidHatchBlockEntity extends CapabilityBlockEntity {
    protected final ItemStackHandler slots = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    protected FluidTank tank = new FluidTank(FluidType.BUCKET_VOLUME * 16) {
        @Override
        protected void onContentsChanged() {
            setChanged();
        }
    };
    protected final LazyOptional<IItemHandler> lazySlots = LazyOptional.of(() -> slots);

    protected final LazyOptional<IFluidHandler> lazyTank = LazyOptional.of(() -> tank);

    public FluidHatchBlockEntity(@NotNull BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state)
    {
        super(blockEntityType, pos, state);
    }

    @Override
    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing)
    {
        if (capability == ForgeCapabilities.FLUID_HANDLER)
            if (facing == getBlockState().getValue(IOHatchBlock.FACING) || facing == null)
                return lazyTank.cast();
        if(capability == ForgeCapabilities.ITEM_HANDLER) {
            if (facing == null)
                return lazySlots.cast();
        }
        return super.getCapability(capability, facing);
    }

    public void dropItem() {
        SimpleContainer inventory = new SimpleContainer(slots.getSlots());
        for (int i = 0; i < slots.getSlots(); i++) {
            inventory.setItem(i, slots.getStackInSlot(i));
        }

        Containers.dropContents(level, this.worldPosition, inventory);
    }

    public static <T extends BlockEntity> void serverTick(Level level, BlockPos blockPos, BlockState blockState, T t) {
        FluidHatchBlockEntity blockEntity = (FluidHatchBlockEntity) t;
        if (!level.isClientSide()) {
            ModPackets.sendToClients(new FluidPacket(blockEntity.tank.getFluid(), blockPos));
            setChanged(level, blockPos, blockState);
        }
    }
    public void setFluid(FluidStack fluidStack) {
        tank.setFluid(fluidStack);
    }
}
