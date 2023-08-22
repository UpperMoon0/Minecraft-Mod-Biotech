package com.nhat.biotech.View;

import com.nhat.biotech.Blocks.BlockEntities.BreederBlockEntity;
import com.nhat.biotech.Blocks.ModBlocks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class BreederMenu extends AbstractContainerMenu
{
    private final BreederBlockEntity blockEntity;
    private final Level level;
    public BreederMenu(int i, Inventory inventory, FriendlyByteBuf friendlyByteBuf) {
        this(i, inventory, inventory.player.level().getBlockEntity(friendlyByteBuf.readBlockPos()));
    }
    public BreederMenu(int i, Inventory inventory, BlockEntity blockEntity) {
        super(ModMenus.BREEDER.get(), i);
        this.blockEntity = (BreederBlockEntity) blockEntity;
        this.level = inventory.player.level();

        blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(h -> {
            this.addSlot(new SlotItemHandler(h, 0, 36, 35));
            this.addSlot(new SlotItemHandler(h, 1, 54, 35));
            this.addSlot(new SlotItemHandler(h, 2, 116, 35));
        });

        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(inventory, col + row * 9 + 9, 8 + col * 18, row * 18 + 84));
            }
        }
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(inventory, col, 8 + col * 18, 142));
        }
    }
    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (pIndex < 3) {
                if (!this.moveItemStackTo(itemstack1, 3, 39, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, 3, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }
    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, ModBlocks.BREEDER.get());
    }
}
