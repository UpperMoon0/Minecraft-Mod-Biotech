package com.nhat.biotech.blocks.block_entites;

import com.nhat.biotech.blocks.BreederBlock;
import com.nhat.biotech.blocks.ModBlocks;
import com.nhat.biotech.networking.BreederPacket;
import com.nhat.biotech.networking.ModPackets;
import com.nhat.biotech.recipes.BiotechRecipe;
import com.nhat.biotech.recipes.BreederRecipe;
import com.nhat.biotech.utils.MultiblockUtils;
import com.nhat.biotech.view.machines.BreederMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class BreederBlockEntity extends BlockEntity implements MenuProvider {
    private int energyConsumed;
    private int recipeEnergyCost;
    private boolean isStructureValid;
    private ItemInputHatchBlockEntity itemInputHatch1;
    private ItemInputHatchBlockEntity itemInputHatch2;
    private ItemInputHatchBlockEntity itemInputHatch3;
    private ItemOutputHatchBlockEntity itemOutputHatch;
    private EnergyInputHatchBlockEntity energyInputHatch;
    private FluidInputHatchBlockEntity fluidInputHatch;
    private Optional<BreederRecipe> recipe = Optional.empty();
    public BreederBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BREEDER.get(), pos, state);
    }
    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("menu.title.biotech.breeder");
    }
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new BreederMenu(pContainerId, pPlayerInventory, this);
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

    private void processRecipe(Level level, BlockPos blockPos) {
        IItemHandler combinedInputItemHandler = new CombinedInvWrapper(
                (IItemHandlerModifiable) itemInputHatch1.getCapability(ForgeCapabilities.ITEM_HANDLER).orElseThrow(NullPointerException::new),
                (IItemHandlerModifiable) itemInputHatch2.getCapability(ForgeCapabilities.ITEM_HANDLER).orElseThrow(NullPointerException::new),
                (IItemHandlerModifiable) itemInputHatch3.getCapability(ForgeCapabilities.ITEM_HANDLER).orElseThrow(NullPointerException::new)
        );
        IItemHandler outputItemHandler = itemOutputHatch.getCapability(ForgeCapabilities.ITEM_HANDLER).orElseThrow(NullPointerException::new);
        IFluidHandler inputFluidHandler = fluidInputHatch.getCapability(ForgeCapabilities.FLUID_HANDLER).orElseThrow(NullPointerException::new);
        IEnergyStorage energyStorage = energyInputHatch.getCapability(ForgeCapabilities.ENERGY).orElseThrow(NullPointerException::new);

        int energyCapacity = energyInputHatch.ENERGY_CAPACITY;
        int energyStored = energyStorage.getEnergyStored();
        int energyConsumeRate = energyInputHatch.ENERGY_THROUGHPUT;
        int fluidCapacity = fluidInputHatch.TANK_CAPACITY;
        FluidStack fluidStored = inputFluidHandler.getFluidInTank(0);

        if (recipe.isEmpty()) {
            energyConsumed = 0;
            recipe = level.getRecipeManager().getAllRecipesFor(BreederRecipe.Type.INSTANCE).stream().filter(r -> r.recipeMatch(combinedInputItemHandler, inputFluidHandler, outputItemHandler, null)).findFirst();
        } else {
            BreederRecipe breederRecipe = recipe.get();
            recipeEnergyCost = breederRecipe.getTotalEnergy();

            if (energyStorage.getEnergyStored() >= energyConsumeRate) {
                int energyToConsume = Math.min(energyConsumeRate, recipeEnergyCost - energyConsumed);
                energyConsumed += energyToConsume;
                energyStorage.extractEnergy(energyToConsume, false);
            }

            if (energyConsumed == recipeEnergyCost) {
                energyConsumed = 0;
                breederRecipe.craft(combinedInputItemHandler, inputFluidHandler, outputItemHandler, null);

                recipe = level.getRecipeManager().getAllRecipesFor(BreederRecipe.Type.INSTANCE).stream().filter(r -> r.recipeMatch(combinedInputItemHandler, inputFluidHandler, outputItemHandler, null)).findFirst();            }
        }

        ModPackets.sendToClients(new BreederPacket(
                energyCapacity,
                energyStored,
                energyConsumeRate,
                energyConsumed,
                recipeEnergyCost,
                fluidCapacity,
                fluidStored,
                isStructureValid,
                blockPos,
                recipe.map(BiotechRecipe::getRecipeContainer).orElse(null)
        ));
    }

    public static <T extends BlockEntity> void serverTick(Level level, BlockPos blockPos, BlockState blockState, T t) {
        BreederBlockEntity blockEntity = (BreederBlockEntity) t;

        if (!level.isClientSide) {
            blockEntity.isStructureValid = MultiblockUtils.checkMultiblock(level, blockPos, blockState, getPattern(), 2);

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
                state = blockState.setValue(BreederBlock.OPERATING, Boolean.TRUE);
            } else {
                state = blockState.setValue(BreederBlock.OPERATING, Boolean.FALSE);
            }
            level.setBlock(blockPos, state, 3);
        }
    }

    private void setHatches(BlockPos blockPos, Level level) {
        Direction facing = getBlockState().getValue(BreederBlock.FACING);

        // Define the south offset
        Vec3i[] southOffset = {
                new Vec3i(-3, -1, -1),
                new Vec3i(-3, -1, -3),
                new Vec3i(-3, -1, -5),
                new Vec3i(3, -1, -3),
                new Vec3i(0, -1, -6),
                new Vec3i(-2, -1, -6)
        };

        // Rotate the south offset and get the hatches
        for (int i = 0; i < southOffset.length; i++) {
            Vec3i rotatedOffset = rotateHatchesOffset(southOffset[i], facing);
            BlockPos hatchPos = blockPos.offset(rotatedOffset);
            switch (i) {
                case 0 -> itemInputHatch1 = (ItemInputHatchBlockEntity) level.getBlockEntity(hatchPos);
                case 1 -> itemInputHatch2 = (ItemInputHatchBlockEntity) level.getBlockEntity(hatchPos);
                case 2 -> itemInputHatch3 = (ItemInputHatchBlockEntity) level.getBlockEntity(hatchPos);
                case 3 -> itemOutputHatch = (ItemOutputHatchBlockEntity) level.getBlockEntity(hatchPos);
                case 4 -> energyInputHatch = (EnergyInputHatchBlockEntity) level.getBlockEntity(hatchPos);
                case 5 -> fluidInputHatch = (FluidInputHatchBlockEntity) level.getBlockEntity(hatchPos);
            }
        }
    }

    private Vec3i rotateHatchesOffset(Vec3i southOffset, Direction direction) {
        return switch (direction) {
            case NORTH -> new BlockPos(-southOffset.getX(), southOffset.getY(), -southOffset.getZ());
            case WEST -> new BlockPos(-southOffset.getZ(), southOffset.getY(), southOffset.getX());
            case EAST -> new BlockPos(southOffset.getZ(), southOffset.getY(), southOffset.getX());
            default -> southOffset;
        };
    }

    private static Block[][][] getPattern()
    {
        Block a = Blocks.AIR,
                b = ModBlocks.BIOTECH_MACHINE_CASING.get(),
                c = ModBlocks.ITEM_INPUT_HATCH.get(),
                d = ModBlocks.ITEM_OUTPUT_HATCH.get(),
                e = ModBlocks.FLUID_INPUT_HATCH.get(),
                f = ModBlocks.ENERGY_INPUT_HATCH.get(),
                g = Blocks.PINK_CONCRETE,
                h = Blocks.PINK_STAINED_GLASS,
                i = Blocks.GLOWSTONE,
                j = Blocks.GRASS_BLOCK;

        return new Block[][][]{
                {
                        {a, a, h, b, h, a, a},
                        {a, a, h, i, h, a, a},
                        {a, a, h, i, h, a, a},
                        {a, a, h, i, h, a, a},
                        {a, a, h, i, h, a, a},
                        {a, a, h, i, h, a, a},
                        {a, a, h, b, h, a, a}
                },
                {
                        {a, g, h, b, h, g, a},
                        {a, g, a, a, a, g, a},
                        {a, g, a, a, a, g, a},
                        {a, g, a, a, a, g, a},
                        {a, g, a, a, a, g, a},
                        {a, g, a, a, a, g, a},
                        {a, g, h, b, h, g, a}
                },
                {
                        {a, b, b, b, b, b, a},
                        {a, b, a, a, a, b, a},
                        {a, b, a, a, a, b, a},
                        {a, b, a, a, a, b, a},
                        {a, b, a, a, a, b, a},
                        {a, b, a, a, a, b, a},
                        {a, b, b, b, b, b, a}
                },
                {
                        {b, b, b, b, b, b, b},
                        {b, a, a, a, a, a, b},
                        {b, a, a, a, a, a, b},
                        {b, a, a, a, a, a, b},
                        {b, a, a, a, a, a, b},
                        {b, a, a, a, a, a, b},
                        {b, b, b, null, b, b, b}
                },
                {
                        {b, e, b, f, b, b, b},
                        {c, b, j, j, j, b, b},
                        {b, b, j, j, j, b, b},
                        {c, b, j, j, j, b, d},
                        {b, b, j, j, j, b, b},
                        {c, b, j, j, j, b, b},
                        {b, b, b, b, b, b, b}
                }
        };
    }
}