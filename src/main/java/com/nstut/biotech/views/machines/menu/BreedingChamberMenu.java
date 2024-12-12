package com.nstut.biotech.views.machines.menu;

import com.nstut.biotech.blocks.block_entites.machines.MachineRegistries;
import com.nstut.biotech.blocks.block_entites.machines.BreedingChamberBlockEntity;
import com.nstut.nstutlib.recipes.ModRecipeData;
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

public class BreedingChamberMenu extends MachineMenu {

    private final Level level;
    private final BreedingChamberBlockEntity blockEntity;
    private final BlockPos pos;

    private FluidStack fluidStored;
    private ModRecipeData recipe;
    private int energyCapacity;
    private int energyStored;
    private int energyConsumeRate;
    private int energyConsumed;
    private int recipeEnergyCost;
    private int fluidCapacity;
    private boolean isStructureValid;

    // Getters
    public BreedingChamberBlockEntity getBlockEntity() {
        return blockEntity;
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
    public ModRecipeData getRecipe() {
        return recipe;
    }
    public int getRecipeEnergyCost() {
        return recipeEnergyCost;
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
    public void setRecipe(ModRecipeData recipeEntity) {
        this.recipe = recipeEntity;
    }

    public BreedingChamberMenu(int pContainerId, Inventory inventory, FriendlyByteBuf friendlyByteBuf) {
        this(pContainerId, inventory, Objects.requireNonNull(inventory.player.level().getBlockEntity(friendlyByteBuf.readBlockPos())));
    }

    public BreedingChamberMenu(int i, Inventory inventory, BlockEntity blockEntity) {
        super(MachineRegistries.BREEDING_CHAMBER.menu().get(), i);
        this.level = inventory.player.level();
        this.blockEntity = (BreedingChamberBlockEntity) blockEntity;
        this.pos = blockEntity.getBlockPos();
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(ContainerLevelAccess.create(level, pos), player, MachineRegistries.BREEDING_CHAMBER.block().get());
    }

    public boolean getIsOperating() {
        return recipe != null;
    }
}