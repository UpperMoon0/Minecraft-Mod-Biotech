package com.nhat.biotech.blocks.block_entites.machines;

import net.minecraft.core.Vec3i;

import java.util.List;

public class HatchesOffset {
    List<Vec3i> itemInputHatchesOffset;
    List<Vec3i> itemOutputHatchesOffset;
    List<Vec3i> fluidInputHatchesOffset;
    List<Vec3i> fluidOutputHatchesOffset;
    List<Vec3i> energyInputHatchesOffset;
    List<Vec3i> energyOutputHatchesOffset;
    public HatchesOffset(List<Vec3i> itemInputHatchesOffset,
                         List<Vec3i> itemOutputHatchesOffset,
                         List<Vec3i> fluidInputHatchesOffset,
                         List<Vec3i> fluidOutputHatchesOffset,
                         List<Vec3i> energyInputHatchesOffset,
                         List<Vec3i> energyOutputHatchesOffset) {
        this.itemInputHatchesOffset = itemInputHatchesOffset;
        this.itemOutputHatchesOffset = itemOutputHatchesOffset;
        this.fluidInputHatchesOffset = fluidInputHatchesOffset;
        this.fluidOutputHatchesOffset = fluidOutputHatchesOffset;
        this.energyInputHatchesOffset = energyInputHatchesOffset;
        this.energyOutputHatchesOffset = energyOutputHatchesOffset;
    }
    public List<Vec3i> getItemInputHatchesOffset() {
        return itemInputHatchesOffset;
    }
    public List<Vec3i> getItemOutputHatchesOffset() {
        return itemOutputHatchesOffset;
    }
    public List<Vec3i> getFluidInputHatchesOffset() {
        return fluidInputHatchesOffset;
    }
    public List<Vec3i> getFluidOutputHatchesOffset() {
        return fluidOutputHatchesOffset;
    }
    public List<Vec3i> getEnergyInputHatchesOffset() {
        return energyInputHatchesOffset;
    }
    public List<Vec3i> getEnergyOutputHatchesOffset() {
        return energyOutputHatchesOffset;
    }
    // get hatches count
    public int getItemInputHatchesCount() {
        return itemInputHatchesOffset.size();
    }
    public int getItemOutputHatchesCount() {
        return itemOutputHatchesOffset.size();
    }
    public int getFluidInputHatchesCount() {
        return fluidInputHatchesOffset.size();
    }
    public int getFluidOutputHatchesCount() {
        return fluidOutputHatchesOffset.size();
    }
    public int getEnergyInputHatchesCount() {
        return energyInputHatchesOffset.size();
    }
    public int getEnergyOutputHatchesCount() {
        return energyOutputHatchesOffset.size();
    }
}
