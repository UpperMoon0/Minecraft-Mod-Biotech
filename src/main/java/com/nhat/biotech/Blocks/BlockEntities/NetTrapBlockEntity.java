package com.nhat.biotech.Blocks.BlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jline.utils.Log;

public class NetTrapBlockEntity extends BlockEntity {
    public NetTrapBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.NET_TRAP.get(), blockPos, blockState);
    }
}
