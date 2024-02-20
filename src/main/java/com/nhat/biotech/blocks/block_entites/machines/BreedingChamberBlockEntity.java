package com.nhat.biotech.blocks.block_entites.machines;

import com.nhat.biotech.blocks.ModBlocks;
import com.nhat.biotech.blocks.block_entites.ModBlockEntities;
import com.nhat.biotech.blocks.block_entites.hatches.EnergyInputHatchBlockEntity;
import com.nhat.biotech.blocks.block_entites.hatches.FluidInputHatchBlockEntity;
import com.nhat.biotech.blocks.block_entites.hatches.ItemInputHatchBlockEntity;
import com.nhat.biotech.blocks.block_entites.hatches.ItemOutputHatchBlockEntity;
import com.nhat.biotech.networking.BreedingChamberPacket;
import com.nhat.biotech.networking.ModPackets;
import com.nhat.biotech.recipes.BiotechRecipe;
import com.nhat.biotech.recipes.BreedingChamberRecipe;
import com.nhat.biotech.view.machines.BreedingChamberMenu;
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

public class BreedingChamberBlockEntity extends AbstractMachineBlockEntity {
    private ItemInputHatchBlockEntity itemInputHatch1;
    private ItemInputHatchBlockEntity itemInputHatch2;
    private ItemInputHatchBlockEntity itemInputHatch3;
    private ItemOutputHatchBlockEntity itemOutputHatch;
    private EnergyInputHatchBlockEntity energyInputHatch;
    private FluidInputHatchBlockEntity fluidInputHatch;
    public BreedingChamberBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BREEDING_CHAMBER.get(), pos, state);
        translateKey = "menu.title.biotech.breeder";
    }
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new BreedingChamberMenu(pContainerId, pPlayerInventory, this);
    }

    @Override
    protected void processRecipe(Level level, BlockPos blockPos) {
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
            recipe = level.getRecipeManager().getAllRecipesFor(BreedingChamberRecipe.TYPE).stream().filter(r -> r.recipeMatch(combinedInputItemHandler, inputFluidHandler, outputItemHandler, null)).findFirst();
        } else {
            BreedingChamberRecipe breedingChamber = (BreedingChamberRecipe) recipe.get();
            recipeEnergyCost = breedingChamber.getTotalEnergy();

            if (energyStorage.getEnergyStored() >= energyConsumeRate) {
                int energyToConsume = Math.min(energyConsumeRate, recipeEnergyCost - energyConsumed);
                energyConsumed += energyToConsume;
                energyStorage.extractEnergy(energyToConsume, false);
            }

            if (energyConsumed == recipeEnergyCost) {
                energyConsumed = 0;
                breedingChamber.craft(combinedInputItemHandler, inputFluidHandler, outputItemHandler, null);

                recipe = level.getRecipeManager().getAllRecipesFor(BreedingChamberRecipe.TYPE).stream().filter(r -> r.recipeMatch(combinedInputItemHandler, inputFluidHandler, outputItemHandler, null)).findFirst();            }
        }

        ModPackets.sendToClients(new BreedingChamberPacket(
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

    @Override
    protected void setHatches(BlockPos blockPos, Level level) {
        Direction facing = getBlockState().getValue(getFacingProperty());

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
    @Override
    protected Block[][][] getPattern()
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