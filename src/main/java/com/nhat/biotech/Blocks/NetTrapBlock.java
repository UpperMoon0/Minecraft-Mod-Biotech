package com.nhat.biotech.Blocks;

import com.nhat.biotech.Blocks.BlockEntities.NetTrapBlockEntity;
import com.nhat.biotech.Items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class NetTrapBlock extends Block implements EntityBlock {
    public NetTrapBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).noOcclusion());
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Block.box(0,0,0,16,1,16);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new NetTrapBlockEntity(blockPos, blockState);
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        if (!level.isClientSide) {
            if (entity instanceof Cow) {
                // Break the block
                level.destroyBlock(blockPos, false);

                // Delete the cow
                entity.remove(Entity.RemovalReason.KILLED);

                // Drop a cow spawn egg
                ItemEntity itemEntity = new ItemEntity(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), new ItemStack(ModItems.COW_ITEM.get()));
                level.addFreshEntity(itemEntity);
            }
        }
    }
}
