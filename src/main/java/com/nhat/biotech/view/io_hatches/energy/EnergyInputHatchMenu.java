package com.nhat.biotech.view.io_hatches.energy;

import com.nhat.biotech.blocks.block_entites.hatches.EnergyHatchBlockEntity;
import com.nhat.biotech.view.BiotechMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Objects;

public class EnergyInputHatchMenu extends EnergyHatchMenu {
    public EnergyInputHatchMenu(int pContainerId, Inventory inventory, FriendlyByteBuf friendlyByteBuf) {
        this(pContainerId, inventory, Objects.requireNonNull(inventory.player.level().getBlockEntity(friendlyByteBuf.readBlockPos())));
    }

    public EnergyInputHatchMenu(int pContainerId, Inventory inventory, BlockEntity blockEntity) {
        super(BiotechMenus.ENERGY_INPUT_HATCH.get(), pContainerId, inventory, blockEntity);
    }
}
