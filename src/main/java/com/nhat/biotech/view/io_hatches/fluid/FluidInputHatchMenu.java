package com.nhat.biotech.view.io_hatches.fluid;

import com.nhat.biotech.blocks.block_entites.FluidHatchBlockEntity;
import com.nhat.biotech.view.ModMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Objects;

public class FluidInputHatchMenu extends FluidHatchMenu {
    public FluidInputHatchMenu(int pContainerId, Inventory inventory, FriendlyByteBuf friendlyByteBuf) {
        this(pContainerId, inventory, Objects.requireNonNull(inventory.player.level().getBlockEntity(friendlyByteBuf.readBlockPos())));
    }
    public FluidInputHatchMenu(int pContainerId, Inventory inventory, BlockEntity blockEntity) {
        super( ModMenus.FLUID_INPUT_HATCH.get(), pContainerId, inventory, blockEntity);
    }
}
