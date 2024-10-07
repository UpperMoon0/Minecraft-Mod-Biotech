package com.nhat.biotech.view.machines;

import com.nhat.biotech.blocks.block_entites.machines.MachineRegistries;
import com.nhat.biotech.blocks.block_entites.machines.TerrestrialHabitatBlockEntity;
import com.nhat.biotech.recipes.BiotechRecipe;
import com.nhat.biotech.view.BiotechMenu;
import com.nhat.biotech.view.BiotechMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class TerrestrialHabitatMenu extends BiotechMenu {

    private final Level level;
    private final TerrestrialHabitatBlockEntity blockEntity;
    private final BlockPos pos;

    private FluidStack fluidStored;
    private BiotechRecipe recipe;
    private int energyCapacity;
    private int energyStored;
    private int energyConsumeRate;
    private int energyConsumed;
    private int recipeEnergyCost;
    private int fluidCapacity;
    private boolean isStructureValid;

    // Getters
    public TerrestrialHabitatBlockEntity getBlockEntity() {
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
    public void setRecipe(BiotechRecipe recipeEntity) {
        this.recipe = recipeEntity;
    }

    public TerrestrialHabitatMenu(int pContainerId, Inventory inventory, FriendlyByteBuf friendlyByteBuf) {
        this(pContainerId, inventory, Objects.requireNonNull(inventory.player.level().getBlockEntity(friendlyByteBuf.readBlockPos())));
    }

    public TerrestrialHabitatMenu(int i, Inventory inventory, BlockEntity blockEntity) {
        super(BiotechMenus.TERRESTRIAL_HABITAT.get(), i);
        this.level = inventory.player.level();
        this.blockEntity = (TerrestrialHabitatBlockEntity) blockEntity;
        this.pos = blockEntity.getBlockPos();
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(ContainerLevelAccess.create(level, pos), player, MachineRegistries.TERRESTRIAL_HABITAT.block().get());
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
