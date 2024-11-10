package com.nhat.biotech.blocks.block_entites.machines;

import com.nhat.biotech.blocks.BlockRegistries;
import com.nhat.biotech.blocks.block_entites.hatches.EnergyInputHatchBlockEntity;
import com.nhat.biotech.blocks.block_entites.hatches.FluidInputHatchBlockEntity;
import com.nhat.biotech.blocks.block_entites.hatches.ItemInputHatchBlockEntity;
import com.nhat.biotech.blocks.block_entites.hatches.ItemOutputHatchBlockEntity;
import com.nhat.biotech.data.models.StructurePattern;
import com.nhat.biotech.networking.BiotechPackets;
import com.nhat.biotech.networking.GreenhousePacket;
import com.nhat.biotech.recipes.BiotechRecipe;
import com.nhat.biotech.recipes.GreenhouseRecipe;
import com.nhat.biotech.view.machines.menu.GreenhouseMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;

public class GreenhouseBlockEntity extends MachineBlockEntity {

    private ItemInputHatchBlockEntity itemInputHatch;
    private ItemOutputHatchBlockEntity itemOutputHatch;
    private EnergyInputHatchBlockEntity energyInputHatch;
    private FluidInputHatchBlockEntity fluidInputHatch;

    public GreenhouseBlockEntity(BlockPos pos, BlockState state) {
        super(MachineRegistries.GREENHOUSE.blockEntity().get(), pos, state, 1);
        translateKey = "menu.title.biotech." + MachineRegistries.GREENHOUSE.id();
    }
    @Override
    public AbstractContainerMenu createMenu(int pContainerId,
                                            @NotNull Inventory pPlayerInventory,
                                            @NotNull Player pPlayer) {
        return new GreenhouseMenu(pContainerId, pPlayerInventory, this);
    }

    @Override
    protected void processRecipe(Level level, BlockPos blockPos) {
        IItemHandler combinedInputItemHandler = new CombinedInvWrapper(
                (IItemHandlerModifiable) itemInputHatch.getCapability(ForgeCapabilities.ITEM_HANDLER).orElseThrow(NullPointerException::new)
        );
        IItemHandler outputItemHandler = itemOutputHatch.getCapability(ForgeCapabilities.ITEM_HANDLER).orElseThrow(NullPointerException::new);
        IFluidHandler inputFluidHandler = fluidInputHatch.getCapability(ForgeCapabilities.FLUID_HANDLER).orElseThrow(NullPointerException::new);
        IEnergyStorage energyStorage = energyInputHatch.getCapability(ForgeCapabilities.ENERGY).orElseThrow(NullPointerException::new);

        int energyCapacity = energyInputHatch.ENERGY_CAPACITY;
        int energyStored = energyStorage.getEnergyStored();
        int energyConsumeRate = energyInputHatch.ENERGY_THROUGHPUT;
        int fluidCapacity = FluidInputHatchBlockEntity.TANK_CAPACITY;
        FluidStack fluidStored = inputFluidHandler.getFluidInTank(0);

        if (recipeHandler.isEmpty()) {
            energyConsumed = 0;
            recipeHandler = level.getRecipeManager().getAllRecipesFor(GreenhouseRecipe.TYPE).stream().filter(r -> r.recipeMatch(combinedInputItemHandler, inputFluidHandler, outputItemHandler, null)).findFirst();
        } else {
            GreenhouseRecipe recipeHandler = (GreenhouseRecipe) this.recipeHandler.get();
            recipeEnergyCost = recipeHandler.getTotalEnergy();

            if (energyConsumed == 0) {
                recipeHandler.consumeIngredients(combinedInputItemHandler, inputFluidHandler);
            }

            if (energyStorage.getEnergyStored() >= energyConsumeRate) {
                int energyToConsume = Math.min(energyConsumeRate, recipeEnergyCost - energyConsumed);
                energyConsumed += energyToConsume;
                energyStorage.extractEnergy(energyToConsume, false);
            }

            if (energyConsumed == recipeEnergyCost) {
                energyConsumed = 0;
                recipeHandler.assemble(outputItemHandler, null);

                this.recipeHandler = level.getRecipeManager().getAllRecipesFor(GreenhouseRecipe.TYPE).stream().filter(r -> r.recipeMatch(combinedInputItemHandler, inputFluidHandler, outputItemHandler, null)).findFirst();
            }
        }

        BiotechPackets.sendToClients(new GreenhousePacket(
                energyCapacity,
                energyStored,
                energyConsumeRate,
                energyConsumed,
                recipeEnergyCost,
                fluidCapacity,
                fluidStored,
                isStructureValid,
                blockPos,
                recipeHandler.map(BiotechRecipe::getRecipe).orElse(null)
        ));
    }

    @Override
    protected void setHatches(BlockPos blockPos, Level level) {
        Direction facing = getBlockState().getValue(getFacingProperty());

        // Define the south offset
        Vec3i[] southOffset = {
                new Vec3i(-3, 0, -3),
                new Vec3i(3, 0, -3),
                new Vec3i(0, 0, -6),
                new Vec3i(-2, 0, -6)
        };

        // Rotate the south offset and get the hatches
        for (int i = 0; i < southOffset.length; i++) {
            Vec3i rotatedOffset = rotateHatchesOffset(southOffset[i], facing);
            BlockPos hatchPos = blockPos.offset(rotatedOffset);

            switch (i) {
                case 0 -> itemInputHatch = (ItemInputHatchBlockEntity) level.getBlockEntity(hatchPos);
                case 1 -> itemOutputHatch = (ItemOutputHatchBlockEntity) level.getBlockEntity(hatchPos);
                case 2 -> energyInputHatch = (EnergyInputHatchBlockEntity) level.getBlockEntity(hatchPos);
                case 3 -> fluidInputHatch = (FluidInputHatchBlockEntity) level.getBlockEntity(hatchPos);
            }
        }
    }

    @Override
    public StructurePattern getStructurePattern()
    {
        Block b = Blocks.PRISMARINE_BRICK_SLAB,
                c = Blocks.LIME_CONCRETE,
                d = Blocks.GLOWSTONE,
                e = BlockRegistries.BIOTECH_MACHINE_CASING.get(),
                f = Blocks.LIME_STAINED_GLASS,
                g = Blocks.DIORITE_WALL,
                h = Blocks.PRISMARINE_BRICK_STAIRS,
                i = BlockRegistries.ENERGY_INPUT_HATCH.get(),
                j = BlockRegistries.FLUID_INPUT_HATCH.get(),
                k = Blocks.FARMLAND,
                l = BlockRegistries.ITEM_OUTPUT_HATCH.get(),
                m = Blocks.WATER,
                n = BlockRegistries.ITEM_INPUT_HATCH.get(),
                a = Blocks.AIR;

        Block[][][] blockArray = new Block[][][]{
                {
                        {a, a, a, a, a, a, a},
                        {a, a, a, b, a, a, a},
                        {a, a, c, c, c, a, a},
                        {a, b, c, d, c, b, a},
                        {a, a, c, c, c, a, a},
                        {a, a, a, b, a, a, a},
                        {a, a, a, a, a, a, a},
                },
                {
                        {a, a, b, b, b, a, a},
                        {a, e, e, e, e, e, a},
                        {b, e, a, a, a, e, b},
                        {b, e, a, a, a, e, b},
                        {b, e, a, a, a, e, b},
                        {a, e, e, e, e, e, a},
                        {a, a, b, b, b, a, a},
                },
                {
                        {b, b, a, a, a, b, b},
                        {b, e, f, f, f, e, b},
                        {a, f, a, a, a, f, a},
                        {a, f, a, a, a, f, a},
                        {a, f, a, a, a, f, a},
                        {b, e, f, f, f, e, b},
                        {b, b, a, a, a, b, b},
                },
                {
                        {g, a, a, a, a, a, g},
                        {a, e, f, f, f, e, a},
                        {a, f, a, a, a, f, a},
                        {a, f, a, a, a, f, a},
                        {a, f, a, a, a, f, a},
                        {a, e, f, f, f, e, a},
                        {g, a, a, a, a, a, g},
                },
                {
                        {c, h, a, a, a, h, c},
                        {h, e, f, f, f, e, h},
                        {a, f, a, a, a, f, a},
                        {a, f, a, a, a, f, a},
                        {a, f, a, a, a, f, a},
                        {h, e, f, f, f, e, h},
                        {c, h, a, a, a, h, c},
                },
                {
                        {e, j, e, i, e, e, e},
                        {e, e, e, e, e, e, e},
                        {e, e, k, k, k, e, e},
                        {n, e, k, m, k, e, l},
                        {e, e, k, k, k, e, e},
                        {e, e, e, e, e, e, e},
                        {e, e, e, null, e, e, e},
                },
        };

        return new StructurePattern(blockArray, false);
    }
}