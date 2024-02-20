package com.nhat.biotech.blocks.block_entites.machines;

import com.nhat.biotech.blocks.MachineBlock;
import com.nhat.biotech.recipes.BiotechRecipe;
import com.nhat.biotech.utils.MultiblockUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class AbstractMachineBlockEntity extends BlockEntity implements MenuProvider {
    protected int energyConsumed;
    protected int recipeEnergyCost;
    protected boolean isStructureValid;
    protected String translateKey;
    protected Optional<? extends BiotechRecipe> recipe = Optional.empty();
    public AbstractMachineBlockEntity(BlockEntityType<? extends AbstractMachineBlockEntity> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }
    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable(translateKey);
    }
    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        energyConsumed = pTag.getInt("usedEnergy");
    }
    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("usedEnergy", energyConsumed);
    }
    public static <T extends BlockEntity> void serverTick(Level level, BlockPos blockPos, BlockState blockState, T bEntity) {
        AbstractMachineBlockEntity blockEntity = (AbstractMachineBlockEntity) bEntity;

        if (!level.isClientSide) {
            blockEntity.isStructureValid = MultiblockUtils.checkMultiblock(level, blockPos, blockState, blockEntity.getPattern(), 2);

            if (blockEntity.isStructureValid) {
                blockEntity.setHatches(blockPos, level);
                blockEntity.processRecipe(level, blockPos);

                setChanged(level, blockPos, blockState);
            } else {
                blockEntity.energyConsumed = 0;
                blockEntity.recipeEnergyCost = 0;
            }

            BlockState state;
            if (blockEntity.recipe.isPresent()) {
                state = blockState.setValue(MachineBlock.OPERATING, Boolean.TRUE);
            } else {
                state = blockState.setValue(MachineBlock.OPERATING, Boolean.FALSE);
            }
            level.setBlock(blockPos, state, 3);
        }
    }
    protected abstract void processRecipe(Level level, BlockPos blockPos);
    protected Vec3i rotateHatchesOffset(Vec3i southOffset, Direction direction) {
        return switch (direction) {
            case NORTH -> new BlockPos(-southOffset.getX(), southOffset.getY(), -southOffset.getZ());
            case WEST -> new BlockPos(-southOffset.getZ(), southOffset.getY(), southOffset.getX());
            case EAST -> new BlockPos(southOffset.getZ(), southOffset.getY(), southOffset.getX());
            default -> southOffset;
        };
    }
    protected abstract void setHatches(BlockPos blockPos, Level level);
    protected DirectionProperty getFacingProperty() {
        return MachineBlock.FACING;
    }

    protected abstract Block[][][] getPattern();
}
