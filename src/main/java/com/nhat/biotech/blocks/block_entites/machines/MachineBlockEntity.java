package com.nhat.biotech.blocks.block_entites.machines;

import com.nhat.biotech.blocks.MachineBlock;
import com.nhat.biotech.data.models.StructurePattern;
import com.nhat.biotech.recipes.BiotechRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class MachineBlockEntity extends BlockEntity implements MenuProvider {

    // Stores the structure pattern for the multiblock machine
    protected StructurePattern structurePattern;

    private final int controllerHeight;

    // Tracks the amount of energy consumed by the machine
    protected int energyConsumed;

    // Stores the energy cost required to complete the current recipe
    protected int recipeEnergyCost;

    // Indicates if the structure (multiblock) is valid or not
    protected boolean isStructureValid;

    // Translation key for display purposes (used for GUI, etc.)
    protected String translateKey;

    // Holds the current recipe being processed, if any
    protected Optional<? extends BiotechRecipe<?>> recipeHandler = Optional.empty();

    // Constructor for the machine block entity, sets its position and block state
    public MachineBlockEntity(BlockEntityType<? extends MachineBlockEntity> pType, BlockPos pPos, BlockState pBlockState, int controllerHeight) {
        super(pType, pPos, pBlockState);
        this.structurePattern = getStructurePattern();
        this.controllerHeight = controllerHeight;
    }

    // Provides the display name for this machine, used in GUIs or in-game messages
    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable(translateKey);
    }

    // Loads persistent data from the NBT tag when the world loads
    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        energyConsumed = pTag.getInt("energyConsumed");
    }

    // Saves additional persistent data like energy consumed to the NBT tag
    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("energyConsumed", energyConsumed);
    }

    // Server-side tick method that handles updates to the block entity every tick
    public static <T extends BlockEntity> void serverTick(Level level, BlockPos blockPos, BlockState blockState, T bEntity) {
        MachineBlockEntity blockEntity = (MachineBlockEntity) bEntity;

        // Only process on the server side (not client)
        if (!level.isClientSide) {
            // Validate the multiblock structure based on a pattern
            blockEntity.isStructureValid = blockEntity.checkMultiblock(level, blockPos, blockState);

            // If the multiblock structure is valid, continue processing the recipe
            if (blockEntity.isStructureValid) {
                blockEntity.setHatches(blockPos, level);
                blockEntity.processRecipe(level, blockPos);

                // Mark the block state as changed
                setChanged(level, blockPos, blockState);
            } else {
                // Reset energy and recipe cost if the structure is invalid
                blockEntity.energyConsumed = 0;
                blockEntity.recipeEnergyCost = 0;
            }

            // Update block state to indicate if the machine is operating (has a recipe)
            BlockState state;
            if (blockEntity.recipeHandler.isPresent()) {
                state = blockState.setValue(MachineBlock.OPERATING, Boolean.TRUE);
            } else {
                state = blockState.setValue(MachineBlock.OPERATING, Boolean.FALSE);
            }
            level.setBlock(blockPos, state, 3);
        }
    }

    // Abstract method that each machine block entity must implement to process its recipe
    protected abstract void processRecipe(Level level, BlockPos blockPos);

    // Rotates the position offsets of hatches based on the block's facing direction
    protected Vec3i rotateHatchesOffset(Vec3i southOffset, Direction direction) {
        return switch (direction) {
            case NORTH -> new BlockPos(-southOffset.getX(), southOffset.getY(), -southOffset.getZ());
            case WEST -> new BlockPos(-southOffset.getZ(), southOffset.getY(), southOffset.getX());
            case EAST -> new BlockPos(southOffset.getZ(), southOffset.getY(), southOffset.getX());
            default -> southOffset;
        };
    }

    // Abstract method to set the hatches in the machine's structure
    protected abstract void setHatches(BlockPos blockPos, Level level);

    // Returns the facing property of the block (e.g., north, south, east, west)
    protected DirectionProperty getFacingProperty() {
        return MachineBlock.FACING;
    }

    // Abstract method to define the pattern for the multiblock structure
    public abstract StructurePattern getStructurePattern();

    public boolean checkMultiblock(Level level, BlockPos blockPos, BlockState blockState) {
        return structurePattern.check(level, blockPos, blockState, controllerHeight);
    }
}
