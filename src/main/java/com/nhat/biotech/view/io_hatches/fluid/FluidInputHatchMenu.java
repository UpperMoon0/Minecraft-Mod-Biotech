package com.nhat.biotech.view.io_hatches.fluid;

import com.nhat.biotech.view.IModMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Objects;

public class FluidInputHatchMenu extends FluidHatchMenu {
    public FluidInputHatchMenu(int pContainerId, Inventory inventory, FriendlyByteBuf friendlyByteBuf) {
        this(pContainerId, inventory, Objects.requireNonNull(inventory.player.level().getBlockEntity(friendlyByteBuf.readBlockPos())));
    }
    public FluidInputHatchMenu(int pContainerId, Inventory inventory, BlockEntity blockEntity) {
        super(IModMenus.FLUID_INPUT_HATCH.get(), pContainerId, inventory, blockEntity);
    }
}
