package com.nhat.biotech.View;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;

public class BreederMenu extends AbstractContainerMenu {
    public BreederMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public BreederMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(ModMenus.BREEDER_MENU.get(), containerId);
    }
    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return false;
    }
}
