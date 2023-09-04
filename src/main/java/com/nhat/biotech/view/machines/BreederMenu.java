package com.nhat.biotech.view.machines;

import com.nhat.biotech.blocks.IModBlocks;
import com.nhat.biotech.utils.IAnimalSets;
import com.nhat.biotech.view.BiotechMenu;
import com.nhat.biotech.view.IModMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BreederMenu extends BiotechMenu {
    private final BlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    public BreederMenu(int pContainerId, Inventory inventory, FriendlyByteBuf friendlyByteBuf) {
        this(pContainerId, inventory, Objects.requireNonNull(inventory.player.level().getBlockEntity(friendlyByteBuf.readBlockPos())), new SimpleContainerData(2));
    }

    public BreederMenu(int i, Inventory inventory, BlockEntity blockEntity, ContainerData data) {
        super(IModMenus.BREEDER.get(), i);
        this.blockEntity = blockEntity;
        this.level = inventory.player.level();
        this.data = data;

        blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(h -> {
            this.addSlot(new SlotItemHandler(h, 0, 36, 35) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    Item item = stack.getItem();
                    for (Item adultAnimal : IAnimalSets.ADULT_ANIMAL) {
                        if (item == adultAnimal) {
                            return true;
                        }
                    }
                    return false;
                }
            });
            this.addSlot(new SlotItemHandler(h, 1, 54, 35) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    Item item = stack.getItem();
                    for (Item food : IAnimalSets.FOOD) {
                        if (item == food) {
                            return true;
                        }
                    }
                    return false;
                }
            });
            this.addSlot(new SlotItemHandler(h, 2, 116, 35) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return false;
                }
            });
        });

        addInventorySlots(inventory);

        this.addDataSlots(data);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        return adaptiveQuickMoveStack(pIndex, 3, 2);
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, IModBlocks.BREEDER.get());
    }
    public int getProgress() {
        return data.get(1) == 0? 0 : data.get(0) * 24 / data.get(1) ;
    }
    public boolean isOperating() {
        return data.get(0) > 0;
    }
}