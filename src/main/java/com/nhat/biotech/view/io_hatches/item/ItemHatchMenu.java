package com.nhat.biotech.view.io_hatches.item;

import com.nhat.biotech.blocks.BlockRegistries;
import com.nhat.biotech.view.BiotechMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class ItemHatchMenu extends BiotechMenu {
    private final BlockEntity blockEntity;
    private final Level level;
    public ItemHatchMenu(MenuType menu, int pContainerId, Inventory inventory, BlockEntity blockEntity) {
        super(menu, pContainerId);
        this.blockEntity = blockEntity;
        this.level = inventory.player.level();

        blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(h -> {
            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    addSlot(new SlotItemHandler(h, j + i * 3, 62 + j * 18, 17 + i * 18));
                }
            }
        });

        addInventorySlots(inventory);
    }
    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return adaptiveQuickMoveStack(pIndex, 9, 9);
    }
    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), pPlayer, BlockRegistries.ITEM_INPUT_HATCH.get())
                || stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), pPlayer, BlockRegistries.ITEM_OUTPUT_HATCH.get());
    }
}
