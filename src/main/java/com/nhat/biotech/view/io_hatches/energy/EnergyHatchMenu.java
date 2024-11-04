package com.nhat.biotech.view.io_hatches.energy;

import com.nhat.biotech.blocks.BlockRegistries;
import com.nhat.biotech.blocks.block_entites.hatches.EnergyHatchBlockEntity;
import com.nhat.biotech.view.machines.menu.MachineMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public class EnergyHatchMenu extends MachineMenu {

    private final EnergyHatchBlockEntity blockEntity;
    private final Level level;
    private int energy;

    public EnergyHatchMenu(MenuType menu, int pContainerId, Inventory inventory, BlockEntity blockEntity) {
        super(menu, pContainerId);
        this.blockEntity = (EnergyHatchBlockEntity) blockEntity;
        level = inventory.player.level();

        addInventorySlots(inventory);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, BlockRegistries.ENERGY_INPUT_HATCH.get());
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public EnergyHatchBlockEntity getBlockEntity() {
        return blockEntity;
    }

    public int getEnergyHeight()
    {
        return Math.max(energy * 52 / blockEntity.ENERGY_CAPACITY, 1);
    }
}
