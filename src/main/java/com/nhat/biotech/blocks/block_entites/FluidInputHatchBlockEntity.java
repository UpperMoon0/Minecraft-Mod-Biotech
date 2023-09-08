package com.nhat.biotech.blocks.block_entites;

import com.nhat.biotech.view.io_hatches.fluid.FluidInputHatchMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

public class FluidInputHatchBlockEntity extends FluidHatchBlockEntity {
    public FluidInputHatchBlockEntity(BlockPos pos, BlockState state) {
        super(IModBlockEntities.FLUID_INPUT_HATCH.get(), pos, state);
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
