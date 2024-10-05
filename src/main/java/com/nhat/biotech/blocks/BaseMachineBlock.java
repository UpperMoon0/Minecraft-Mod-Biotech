package com.nhat.biotech.blocks;

import com.mojang.logging.LogUtils;
import com.nhat.biotech.blocks.block_entites.machines.BaseMachineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;

public abstract class BaseMachineBlock extends BaseEntityBlock {
    public static final BooleanProperty OPERATING = BooleanProperty.create("operating");
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    private final Class<? extends BaseMachineBlockEntity> blockEntityClass;

    private static final Logger LOGGER = LogUtils.getLogger();

    protected BaseMachineBlock(Class<? extends BaseMachineBlockEntity> blockEntityClass) {
        super(BlockBehaviour.Properties.copy(Blocks.GRAY_CONCRETE).strength(2f).sound(SoundType.METAL));
        this.blockEntityClass = blockEntityClass;
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(OPERATING, Boolean.FALSE)
        );
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        try {
            return blockEntityClass.getConstructor(BlockPos.class, BlockState.class).newInstance(pos, state);
        } catch (NoSuchMethodException e) {
            LOGGER.error("No constructor found in " + blockEntityClass.getName() + " with parameters (BlockPos, BlockState)", e);
        } catch (IllegalAccessException e) {
            LOGGER.error("Constructor in " + blockEntityClass.getName() + " is not accessible", e);
        } catch (InstantiationException e) {
            LOGGER.error("Cannot instantiate " + blockEntityClass.getName(), e);
        } catch (InvocationTargetException e) {
            LOGGER.error("Constructor in " + blockEntityClass.getName() + " cannot be invoked", e);
        } catch (Throwable throwable) {
            LOGGER.error("Cannot create new instance of " + blockEntityClass.getName(), throwable);
        }
        return null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(FACING, OPERATING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    public @NotNull RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state,
                                          Level level,
                                          @NotNull BlockPos pos,
                                          @NotNull Player player,
                                          @NotNull InteractionHand hand,
                                          @NotNull BlockHitResult hit) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity != null) {
                NetworkHooks.openScreen((ServerPlayer) player, (MenuProvider) blockEntity, pos);
            }
            return InteractionResult.CONSUME;
        } else
            return InteractionResult.SUCCESS;
    }

    @Override
    public <U extends BlockEntity> BlockEntityTicker<U> getTicker(Level pLevel,
                                                                  @NotNull BlockState pState,
                                                                  @NotNull BlockEntityType<U> pBlockEntityType) {
        return pLevel.isClientSide ? null : (level, blockPos, blockState, blockEntity) -> {
            if (blockEntity instanceof BaseMachineBlockEntity) {
                BaseMachineBlockEntity.serverTick(level, blockPos, blockState, blockEntity);
            }
        };
    }
}