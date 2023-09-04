package com.nhat.biotech.blocks.block_entites;

import com.nhat.biotech.view.io_hatches.item.ItemOutputHatchMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ItemOutputHatchBlockEntity extends ItemHatchBlockEntity {
    public ItemOutputHatchBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(IModBlockEntities.ITEM_OUTPUT_HATCH.get(), pPos, pBlockState);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("menu.title.biotech.item_output_hatch");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new ItemOutputHatchMenu(pContainerId, pPlayerInventory, this);
    }
}
