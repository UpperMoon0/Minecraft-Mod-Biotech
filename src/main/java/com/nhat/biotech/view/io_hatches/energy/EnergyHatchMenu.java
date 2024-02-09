package com.nhat.biotech.view.io_hatches.energy;

import com.nhat.biotech.blocks.ModBlocks;
import com.nhat.biotech.view.BiotechMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class EnergyHatchMenu extends BiotechMenu {
    private final BlockEntity blockEntity;
    private final Level level;
    private int energy;
    public EnergyHatchMenu(MenuType menu, int pContainerId, Inventory inventory, BlockEntity blockEntity) {
        super(menu, pContainerId);
        this.blockEntity = blockEntity;
        level = inventory.player.level();

        addInventorySlots(inventory);
    }
    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }
    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, ModBlocks.ENERGY_INPUT_HATCH.get());
    }
    public int getEnergy() {
        return energy;
    }
    public void setEnergy(int energy) {
        this.energy = energy;
    }
    public BlockEntity getBlockEntity() {
        return blockEntity;
    }
    public int getEnergyHeight()
    {
        return Math.max(energy * 52 / 128000, 1);
    }
}
