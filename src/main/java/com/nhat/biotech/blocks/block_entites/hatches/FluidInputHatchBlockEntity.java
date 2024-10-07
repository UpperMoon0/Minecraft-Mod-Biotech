package com.nhat.biotech.blocks.block_entites.hatches;

import com.nhat.biotech.blocks.block_entites.BlockEntityRegistries;
import com.nhat.biotech.view.io_hatches.fluid.FluidInputHatchMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class FluidInputHatchBlockEntity extends FluidHatchBlockEntity {
    public FluidInputHatchBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistries.FLUID_INPUT_HATCH.get(), pos, state);
    }
    @Override
    public Component getDisplayName() {
        return Component.translatable("menu.title.biotech.fluid_input_hatch");
    }
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new FluidInputHatchMenu(pContainerId, pPlayerInventory, this);
    }
}
