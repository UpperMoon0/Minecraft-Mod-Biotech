package com.nhat.biotech.blocks.block_entites;

import com.nhat.biotech.recipes.BiotechRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public abstract class CapabilityBlockEntity extends BlockEntity implements MenuProvider {
    public CapabilityBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }
    @Override
    public void load(CompoundTag tag)
    {
        super.load(tag);
        if (tag.contains("item")) {
            this.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent((handler) -> ((ItemStackHandler)handler).deserializeNBT(tag.getCompound("item")));
        }

        if (tag.contains("energy")) {
            this.getCapability(ForgeCapabilities.ENERGY).ifPresent((handler) -> ((EnergyStorage)handler).deserializeNBT(tag.get("energy")));
        }

        if (tag.contains("fluid")) {
            this.getCapability(ForgeCapabilities.FLUID_HANDLER).ifPresent((handler) -> ((FluidTank)handler).readFromNBT(tag.getCompound("fluid")));
        }
    }
    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        this.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent((handler) -> {
            CompoundTag compound = ((ItemStackHandler)handler).serializeNBT();
            tag.put("item", compound);
        });
        this.getCapability(ForgeCapabilities.ENERGY).ifPresent((handler) -> {
            tag.put("energy", ((EnergyStorage)handler).serializeNBT());
        });
        this.getCapability(ForgeCapabilities.FLUID_HANDLER).ifPresent((handler) -> {
            CompoundTag nbt = new CompoundTag();
            ((FluidTank)handler).writeToNBT(nbt);
            tag.put("fluid", nbt);
        });
    }
    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.getCapability(ForgeCapabilities.ITEM_HANDLER).invalidate();
        this.getCapability(ForgeCapabilities.ENERGY).invalidate();
        this.getCapability(ForgeCapabilities.FLUID_HANDLER).invalidate();
    }
}
