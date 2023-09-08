package com.nhat.biotech.blocks.block_entites;

import com.nhat.biotech.view.io_hatches.fluid.FluidOutputHatchMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

public class FluidOutputHatchBlockEntity extends FluidHatchBlockEntity {
    public FluidOutputHatchBlockEntity(BlockPos pos, BlockState state) {
        super(IModBlockEntities.FLUID_OUTPUT_HATCH.get(), pos, state);
    }
    @Override
    public Component getDisplayName() {
        return Component.translatable("menu.title.biotech.fluid_output_hatch");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new FluidOutputHatchMenu(pContainerId, pPlayerInventory, this);
    }
}
