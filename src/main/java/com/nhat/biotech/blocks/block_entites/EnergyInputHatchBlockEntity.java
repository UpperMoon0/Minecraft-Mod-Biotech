package com.nhat.biotech.blocks.block_entites;

import com.nhat.biotech.view.io_hatches.energy.EnergyInputHatchMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

public class EnergyInputHatchBlockEntity extends EnergyHatchBlockEntity {
    public EnergyInputHatchBlockEntity(BlockPos pos, BlockState state)
    {
        super(IModBlockEntities.ENERGY_INPUT_HATCH.get(), pos, state);
    }

    @Override
    protected EnergyStorage initEnergyStorage() {
        return new EnergyStorage(128000, 128, 0);
    }
    @Override
    protected LazyOptional<IEnergyStorage> initLazyEnergyStorage() {
        return LazyOptional.of(() -> energyStorage);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("menu.title.biotech.energy_input_hatch");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new EnergyInputHatchMenu(i, inventory, this);
    }
}
