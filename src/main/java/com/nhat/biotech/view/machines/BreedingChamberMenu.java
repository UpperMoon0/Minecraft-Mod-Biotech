package com.nhat.biotech.view.machines;

import com.nhat.biotech.blocks.BiotechBlocks;
import com.nhat.biotech.blocks.block_entites.machines.BreedingChamberBlockEntity;
import com.nhat.biotech.recipes.BiotechRecipe;
import com.nhat.biotech.view.BiotechMenu;
import com.nhat.biotech.view.BiotechMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BreedingChamberMenu extends BiotechMenu {
    private final Level LEVEL;
    private final BreedingChamberBlockEntity BLOCK_ENTITY;
    private final BlockPos POS;
    private int energyCapacity;
    private int energyStored;
    private int energyConsumeRate;
    private int energyConsumed;
    private int recipeEnergyCost;
    private int fluidCapacity;
    private FluidStack fluidStored;
    private boolean isStructureValid;
    private BiotechRecipe recipe;

    // Getters
    public BreedingChamberBlockEntity getBlockEntity() {
        return BLOCK_ENTITY;
    }
    public int getEnergyCapacity() {
        return energyCapacity > 0? energyCapacity : 1;
    }
    public int getEnergyStored() {
        return energyStored;
    }
    public int getEnergyConsumeRate() {
        return energyConsumeRate;
    }
    public int getEnergyConsumed() {
        return energyConsumed;
    }
    public int getFluidCapacity() {
        return fluidCapacity > 0? fluidCapacity : 1;
    }
    public FluidStack getFluidStored() {
        return fluidStored;
    }
    public boolean getStructureValid() {
        return isStructureValid;
    }
    public BiotechRecipe getRecipe() {
        return recipe;
    }

    // Setters
    public void setEnergyCapacity(int energy) {
        this.energyCapacity = energy;
    }
    public void setEnergyStored(int energy) {
        this.energyStored = energy;
    }
    public void setEnergyConsumeRate(int energyConsumeRate) {
        this.energyConsumeRate = energyConsumeRate;
    }
    public void setEnergyConsumed(int energyConsumed) {
        this.energyConsumed = energyConsumed;
    }
    public void setRecipeEnergyCost(int recipeEnergyCost) {
        this.recipeEnergyCost = recipeEnergyCost;
    }
    public void setFluidCapacity(int fluidCapacity) {
        this.fluidCapacity = fluidCapacity;
    }
    public void setFluidStored(FluidStack fluidStored) {
        this.fluidStored = fluidStored;
    }
    public void setStructureValid(boolean structureValid) {
        isStructureValid = structureValid;
    }
    public void setRecipeEntity(BiotechRecipe recipeEntity) {
        this.recipe = recipeEntity;
    }

    public BreedingChamberMenu(int pContainerId, Inventory inventory, FriendlyByteBuf friendlyByteBuf) {
        this(pContainerId, inventory, Objects.requireNonNull(inventory.player.level().getBlockEntity(friendlyByteBuf.readBlockPos())));
    }

    public BreedingChamberMenu(int i, Inventory inventory, BlockEntity blockEntity) {
        super(BiotechMenus.BREEDER.get(), i);
        this.LEVEL = inventory.player.level();
        this.BLOCK_ENTITY = (BreedingChamberBlockEntity) blockEntity;
        this.POS = blockEntity.getBlockPos();
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(ContainerLevelAccess.create(LEVEL, POS), player, BiotechBlocks.BREEDING_CHAMBER.get());
    }
    public int getEnergyHeight()
    {
        int energyHeight = energyStored * 76 / energyCapacity;

        if (energyHeight == 0 && energyStored > 0) {
            energyHeight = 1;
        }

        return energyHeight;
    }
    public int getProgressWidth() {
        return recipeEnergyCost == 0? 0 : energyConsumed * 19 / recipeEnergyCost;
    }

    public boolean getIsOperating() {
        return recipe != null;
    }
}