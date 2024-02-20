package com.nhat.biotech.blocks;

import com.nhat.biotech.blocks.block_entites.machines.AbstractMachineBlockEntity;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressWarnings({"unchecked", "NullableProblems"})
public abstract class MachineBlock extends BaseEntityBlock {
    public static final BooleanProperty OPERATING = BooleanProperty.create("operating");
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    private static final Logger LOGGER = LogManager.getLogger();
    private final Class<? extends AbstractMachineBlockEntity> BLOCK_ENTITY_CLASS;
    protected MachineBlock(Class<? extends AbstractMachineBlockEntity> blockEntityClass) {
        super(BlockBehaviour.Properties.copy(Blocks.GRAY_CONCRETE).strength(2f).sound(SoundType.METAL));
        this.BLOCK_ENTITY_CLASS = blockEntityClass;
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(OPERATING, Boolean.FALSE)
        );
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        try {
            return BLOCK_ENTITY_CLASS.getConstructor(BlockPos.class, BlockState.class).newInstance(pos, state);
        } catch (NoSuchMethodException e) {
            LOGGER.error("No constructor found in " + BLOCK_ENTITY_CLASS.getName() + " with parameters (BlockPos, BlockState)", e);
        } catch (IllegalAccessException e) {
            LOGGER.error("Constructor in " + BLOCK_ENTITY_CLASS.getName() + " is not accessible", e);
        } catch (InstantiationException e) {
            LOGGER.error("Cannot instantiate " + BLOCK_ENTITY_CLASS.getName(), e);
        } catch (InvocationTargetException e) {
            LOGGER.error("Constructor in " + BLOCK_ENTITY_CLASS.getName() + " cannot be invoked", e);
        } catch (Throwable throwable) {
            LOGGER.error("Cannot create new instance of " + BLOCK_ENTITY_CLASS.getName(), throwable);
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
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
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
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (!pLevel.isClientSide) {
            return (level, blockPos, blockState, blockEntity) -> {
                try {
                    Method serverTickMethod = BLOCK_ENTITY_CLASS.getMethod("serverTick", Level.class, BlockPos.class, BlockState.class, BlockEntity.class);
                    serverTickMethod.invoke(blockEntity, level, blockPos, blockState, blockEntity);
                } catch (NoSuchMethodException e) {
                    LOGGER.error("No serverTick method found in " + BLOCK_ENTITY_CLASS.getName() + " with parameters (Level, BlockPos, BlockState, BlockEntity)", e);
                } catch (IllegalAccessException e) {
                    LOGGER.error("serverTick method in " + BLOCK_ENTITY_CLASS.getName() + " is not accessible", e);
                } catch (InvocationTargetException e) {
                    LOGGER.error("serverTick method in " + BLOCK_ENTITY_CLASS.getName() + " cannot be invoked", e);
                } catch (Throwable throwable) {
                    LOGGER.error("Cannot call serverTick method of " + BLOCK_ENTITY_CLASS.getName(), throwable);
                }
            };
        }
        return null;
    }
}
