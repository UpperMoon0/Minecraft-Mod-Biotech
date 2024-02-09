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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class BreederBlockEntity extends BlockEntity implements MenuProvider {
    private int energyConsumed;
    private int recipeEnergyCost;
    private int energyConsumption;
    private boolean isStructureValid;
    private ItemInputHatchBlockEntity itemInputHatch1;
    private ItemInputHatchBlockEntity itemInputHatch2;
    private ItemInputHatchBlockEntity itemInputHatch3;
    private ItemOutputHatchBlockEntity itemOutputHatch;
    private EnergyInputHatchBlockEntity energyInputHatch;
    private FluidInputHatchBlockEntity fluidInputHatch;
    private ItemStack[] inputSlots;
    private ItemStack[] outputSlots;
    private FluidTank inputTank;
    private EnergyStorage energyStorage;
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
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("usedEnergy", energyConsumed);
    }
    private void copyHatchesContents() {
        // Copy input slots
        inputSlots = new ItemStack[itemInputHatch1.INVENTORY_SIZE + itemInputHatch2.INVENTORY_SIZE + itemInputHatch3.INVENTORY_SIZE];
        itemInputHatch1.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent((handler) -> {
            for (int i = 0; i < itemInputHatch1.INVENTORY_SIZE; i++) {
                inputSlots[i] = handler.getStackInSlot(i).copy();
            }
        });
        itemInputHatch2.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent((handler) -> {
            for (int i = 0; i < itemInputHatch2.INVENTORY_SIZE; i++) {
                inputSlots[i + itemInputHatch1.INVENTORY_SIZE] = handler.getStackInSlot(i).copy();
            }
        });
        itemInputHatch3.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent((handler) -> {
            for (int i = 0; i < itemInputHatch3.INVENTORY_SIZE; i++) {
                inputSlots[i + itemInputHatch1.INVENTORY_SIZE + itemInputHatch2.INVENTORY_SIZE] = handler.getStackInSlot(i).copy();
            }
        });

        // Copy output slots
        outputSlots = new ItemStack[itemOutputHatch.INVENTORY_SIZE];
        itemOutputHatch.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent((handler) -> {
            for (int i = 0; i < itemOutputHatch.INVENTORY_SIZE; i++) {
                outputSlots[i] = handler.getStackInSlot(i).copy();
            }
        });

        // Copy fluid tank
        inputTank = new FluidTank(fluidInputHatch.TANK_CAPACITY);
        fluidInputHatch.getCapability(ForgeCapabilities.FLUID_HANDLER).ifPresent((handler) -> {
            inputTank.setFluid(handler.getFluidInTank(0).copy());
        });

        // Copy energy storage
        energyStorage = new EnergyStorage(energyInputHatch.ENERGY_CAPACITY);
        energyInputHatch.getCapability(ForgeCapabilities.ENERGY).ifPresent((handler) -> {
            energyStorage.extractEnergy(energyStorage.getEnergyStored(), false);
            energyStorage.receiveEnergy(handler.getEnergyStored(), false);
        });
    }

    private void synchronizeItemAndFluidHatches() {
        // Synchronize input slots
        itemInputHatch1.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent((handler) -> {
            for (int i = 0; i < itemInputHatch1.INVENTORY_SIZE; i++) {
                if (!handler.getStackInSlot(i).equals(inputSlots[i])) {
                    handler.extractItem(i, handler.getStackInSlot(i).getCount(), false);
                    handler.insertItem(i, inputSlots[i], false);
                }
            }
        });
        itemInputHatch2.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent((handler) -> {
            for (int i = 0; i < itemInputHatch2.INVENTORY_SIZE; i++) {
                if (!handler.getStackInSlot(i).equals(inputSlots[i + itemInputHatch1.INVENTORY_SIZE])) {
                    handler.extractItem(i, handler.getStackInSlot(i).getCount(), false);
                    handler.insertItem(i, inputSlots[i + itemInputHatch1.INVENTORY_SIZE], false);
                }
            }
        });
        itemInputHatch3.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent((handler) -> {
            for (int i = 0; i < itemInputHatch3.INVENTORY_SIZE; i++) {
                if (!handler.getStackInSlot(i).equals(inputSlots[i + itemInputHatch1.INVENTORY_SIZE + itemInputHatch2.INVENTORY_SIZE])) {
                    handler.extractItem(i, handler.getStackInSlot(i).getCount(), false);
                    handler.insertItem(i, inputSlots[i + itemInputHatch1.INVENTORY_SIZE + itemInputHatch2.INVENTORY_SIZE], false);
                }
            }
        });

        // Synchronize output slots
        itemOutputHatch.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent((handler) -> {
            for (int i = 0; i < itemOutputHatch.INVENTORY_SIZE; i++) {
                if (!handler.getStackInSlot(i).equals(outputSlots[i])) {
                    handler.extractItem(i, handler.getStackInSlot(i).getCount(), false);
                    handler.insertItem(i, outputSlots[i], false);
                }
            }
        });

        // Synchronize fluid tank
        fluidInputHatch.getCapability(ForgeCapabilities.FLUID_HANDLER).ifPresent((handler) -> {
            if (!handler.getFluidInTank(0).equals(inputTank.getFluid()) || !(handler.getFluidInTank(0).getAmount() == inputTank.getFluid().getAmount())) {
                handler.drain(handler.getFluidInTank(0).getAmount(), IFluidHandler.FluidAction.EXECUTE);
                handler.fill(inputTank.getFluid(), IFluidHandler.FluidAction.EXECUTE);
            }
        });
    }

    private void synchronizeEnergyHatches() {
        // Synchronize energy storage
        energyInputHatch.getCapability(ForgeCapabilities.ENERGY).ifPresent((handler) -> {
            if (handler.getEnergyStored() != energyStorage.getEnergyStored()) {
                handler.extractEnergy(handler.getEnergyStored(), false);
                handler.receiveEnergy(energyStorage.getEnergyStored(), false);
            }
        });
    }

    private void processRecipe(Level level, BlockPos blockPos, BreederBlockEntity blockEntity) {
        copyHatchesContents();
        energyConsumption = energyInputHatch.ENERGY_THROUGHPUT;

        if (recipe.isEmpty()) {
            recipe = level.getRecipeManager().getAllRecipesFor(BreederRecipe.Type.INSTANCE).stream().filter(r -> r.recipeMatch(inputSlots, new FluidTank[]{inputTank}, outputSlots, null)).findFirst();
        }

        int energyCapacity = energyInputHatch.ENERGY_CAPACITY;
        int energyStored = energyStorage.getEnergyStored();
        int energyConsumeRate = energyInputHatch.ENERGY_THROUGHPUT;
        int fluidCapacity = fluidInputHatch.TANK_CAPACITY;
        FluidStack fluidStored = inputTank.getFluidInTank(0);
        ModPackets.sendToClients(new BreederPacket(energyCapacity, energyStored, energyConsumeRate, energyConsumed, recipeEnergyCost, fluidCapacity, fluidStored, isStructureValid, blockPos, recipe.map(BiotechRecipe::getRecipeContainer).orElse(null)));

        if (recipe.isPresent()) {
            BreederRecipe breederRecipe = recipe.get();
            recipeEnergyCost = breederRecipe.getTotalEnergy();

            if (energyStorage.getEnergyStored() >= energyConsumption) {
                int energyToConsume = Math.min(energyConsumption, recipeEnergyCost - energyConsumed);
                energyConsumed += energyToConsume;
                energyStorage.extractEnergy(energyToConsume, false);;
                synchronizeEnergyHatches();
            }

            if (energyConsumed == recipeEnergyCost) {
                energyConsumed = 0;
                recipeEnergyCost = 0;
                breederRecipe.craft(inputSlots, new FluidTank[]{inputTank}, outputSlots, null);
                recipe = Optional.empty();
                synchronizeItemAndFluidHatches();
            }
        } else {
            energyConsumed = 0;
            recipeEnergyCost = 0;
        }
    }

    public static <T extends BlockEntity> void serverTick(Level level, BlockPos blockPos, BlockState blockState, T t) {
        BreederBlockEntity blockEntity = (BreederBlockEntity) t;

        if (!level.isClientSide) {
            blockEntity.isStructureValid = MultiblockUtils.checkMultiblock(level, blockPos, blockState, getPattern(), 2);

            if (blockEntity.isStructureValid) {
                
                blockEntity.setHatches(blockPos, level);

                if (blockEntity.itemInputHatch1 != null
                        && blockEntity.itemInputHatch2 != null
                        && blockEntity.itemInputHatch3 != null
                        && blockEntity.itemOutputHatch != null
                        && blockEntity.energyInputHatch != null
                        && blockEntity.fluidInputHatch != null)
                {
                    blockEntity.processRecipe(level, blockPos, blockEntity);
                }

                setChanged(level, blockPos, blockState);
            } else {
                blockEntity.energyConsumed = 0;
                blockEntity.recipeEnergyCost = 0;
            }

            if (blockEntity.energyConsumed > 0) {
                BlockState state = blockState.setValue(BreederBlock.OPERATING, Boolean.TRUE);
                level.setBlock(blockPos, state, 3);
            } else {
                BlockState state = blockState.setValue(BreederBlock.OPERATING, Boolean.FALSE);
                level.setBlock(blockPos, state, 3);
            }
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


