package com.nhat.biotech.Blocks;

import com.nhat.biotech.Blocks.BlockEntities.NetTrapBlockEntity;
import com.nhat.biotech.Items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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
        super(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS));
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
                level.destroyBlock(blockPos, false);

                entity.remove(Entity.RemovalReason.KILLED);

                if (!((Cow) entity).isBaby()) {
                    ItemEntity itemEntity = new ItemEntity(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), new ItemStack(ModItems.COW_ITEM.get()));
                    level.addFreshEntity(itemEntity);
                } else {
                    ItemEntity itemEntity = new ItemEntity(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), new ItemStack(ModItems.BABY_COW_ITEM.get()));
                    level.addFreshEntity(itemEntity);
                }
            }
            if (entity instanceof Chicken) {
                level.destroyBlock(blockPos, false);

                entity.remove(Entity.RemovalReason.KILLED);

                if (!((Chicken) entity).isBaby()) {
                    ItemEntity itemEntity = new ItemEntity(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), new ItemStack(ModItems.CHICKEN_ITEM.get()));
                    level.addFreshEntity(itemEntity);
                } else {
                    ItemEntity itemEntity = new ItemEntity(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), new ItemStack(ModItems.BABY_CHICKEN_ITEM.get()));
                    level.addFreshEntity(itemEntity);
                }
            }
            if (entity instanceof Pig) {
                level.destroyBlock(blockPos, false);

                entity.remove(Entity.RemovalReason.KILLED);

                if (!((Pig) entity).isBaby()) {
                    ItemEntity itemEntity = new ItemEntity(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), new ItemStack(ModItems.PIG_ITEM.get()));
                    level.addFreshEntity(itemEntity);
                } else {
                    ItemEntity itemEntity = new ItemEntity(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), new ItemStack(ModItems.BABY_PIG_ITEM.get()));
                    level.addFreshEntity(itemEntity);
                }
            }
            if (entity instanceof Sheep) {
                level.destroyBlock(blockPos, false);

                entity.remove(Entity.RemovalReason.KILLED);

                if (!((Sheep) entity).isBaby()) {
                    ItemEntity itemEntity = new ItemEntity(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), new ItemStack(ModItems.SHEEP_ITEM.get()));
                    level.addFreshEntity(itemEntity);
                } else {
                    ItemEntity itemEntity = new ItemEntity(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), new ItemStack(ModItems.BABY_SHEEP_ITEM.get()));
                    level.addFreshEntity(itemEntity);
                }
            }
            if (entity instanceof Rabbit) {
                level.destroyBlock(blockPos, false);

                entity.remove(Entity.RemovalReason.KILLED);

                if (!((Rabbit) entity).isBaby()) {
                    ItemEntity itemEntity = new ItemEntity(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), new ItemStack(ModItems.RABBIT_ITEM.get()));
                    level.addFreshEntity(itemEntity);
                } else {
                    ItemEntity itemEntity = new ItemEntity(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), new ItemStack(ModItems.BABY_RABBIT_ITEM.get()));
                    level.addFreshEntity(itemEntity);
                }
            }
        }
    }
}
