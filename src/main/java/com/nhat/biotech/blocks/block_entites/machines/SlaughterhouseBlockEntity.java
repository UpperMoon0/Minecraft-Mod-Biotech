package com.nhat.biotech.blocks.block_entites.machines;

import com.nhat.biotech.blocks.BlockRegistries;
import com.nhat.biotech.blocks.block_entites.hatches.EnergyInputHatchBlockEntity;
import com.nhat.biotech.blocks.block_entites.hatches.FluidInputHatchBlockEntity;
import com.nhat.biotech.blocks.block_entites.hatches.ItemInputHatchBlockEntity;
import com.nhat.biotech.blocks.block_entites.hatches.ItemOutputHatchBlockEntity;
import com.nhat.biotech.data.models.StructurePattern;
import com.nhat.biotech.networking.BiotechPackets;
import com.nhat.biotech.networking.SlaughterhousePacket;
import com.nhat.biotech.recipes.BiotechRecipe;
import com.nhat.biotech.recipes.SlaughterhouseRecipe;
import com.nhat.biotech.view.machines.menu.SlaughterhouseMenu;
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

public class SlaughterhouseBlockEntity extends MachineBlockEntity {

    private ItemInputHatchBlockEntity itemInputHatch1;
    private ItemInputHatchBlockEntity itemInputHatch2;
    private ItemOutputHatchBlockEntity itemOutputHatch;
    private EnergyInputHatchBlockEntity energyInputHatch;
    private FluidInputHatchBlockEntity fluidInputHatch;

    public SlaughterhouseBlockEntity(BlockPos pos, BlockState state) {
        super(MachineRegistries.SLAUGHTERHOUSE.blockEntity().get(), pos, state, 2);
        translateKey = "menu.title.biotech." + MachineRegistries.SLAUGHTERHOUSE.id();
    }
    @Override
    public AbstractContainerMenu createMenu(int pContainerId,
                                            @NotNull Inventory pPlayerInventory,
                                            @NotNull Player pPlayer) {
        return new SlaughterhouseMenu(pContainerId, pPlayerInventory, this);
    }

    @Override
    protected void processRecipe(Level level, BlockPos blockPos) {
        IItemHandler combinedInputItemHandler = new CombinedInvWrapper(
                (IItemHandlerModifiable) itemInputHatch1.getCapability(ForgeCapabilities.ITEM_HANDLER).orElseThrow(NullPointerException::new),
                (IItemHandlerModifiable) itemInputHatch2.getCapability(ForgeCapabilities.ITEM_HANDLER).orElseThrow(NullPointerException::new)
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
            recipeHandler = level.getRecipeManager().getAllRecipesFor(SlaughterhouseRecipe.TYPE).stream().filter(r -> r.recipeMatch(combinedInputItemHandler, inputFluidHandler, outputItemHandler, null)).findFirst();
        } else {
            SlaughterhouseRecipe recipeHandler = (SlaughterhouseRecipe) this.recipeHandler.get();
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
                recipeHandler.assemble(combinedInputItemHandler, inputFluidHandler, outputItemHandler, null);

                this.recipeHandler = level.getRecipeManager().getAllRecipesFor(SlaughterhouseRecipe.TYPE).stream().filter(r -> r.recipeMatch(combinedInputItemHandler, inputFluidHandler, outputItemHandler, null)).findFirst();            }
        }

        BiotechPackets.sendToClients(new SlaughterhousePacket(
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
        Vec3i[] southOffset = new Vec3i[]{
                new Vec3i(-2, -1, -1),
                new Vec3i(-2, -1, -3),
                new Vec3i(2, -1, -2),
                new Vec3i(1, -1, -4),
                new Vec3i(-1, -1, -4)
        };

        // Rotate the south offset and get the hatches
        for (int i = 0; i < southOffset.length; i++) {
            Vec3i rotatedOffset = rotateHatchesOffset(southOffset[i], facing);
            BlockPos hatchPos = blockPos.offset(rotatedOffset);
            switch (i) {
                case 0 -> itemInputHatch1 = (ItemInputHatchBlockEntity) level.getBlockEntity(hatchPos);
                case 1 -> itemInputHatch2 = (ItemInputHatchBlockEntity) level.getBlockEntity(hatchPos);
                case 2 -> itemOutputHatch = (ItemOutputHatchBlockEntity) level.getBlockEntity(hatchPos);
                case 3 -> energyInputHatch = (EnergyInputHatchBlockEntity) level.getBlockEntity(hatchPos);
                case 4 -> fluidInputHatch = (FluidInputHatchBlockEntity) level.getBlockEntity(hatchPos);
            }
        }
    }
    @Override
    protected StructurePattern getStructurePattern()
    {
        Block a = Blocks.AIR,
                b = BlockRegistries.BIOTECH_MACHINE_CASING.get(),
                c = BlockRegistries.ITEM_INPUT_HATCH.get(),
                d = BlockRegistries.ITEM_OUTPUT_HATCH.get(),
                e = BlockRegistries.FLUID_INPUT_HATCH.get(),
                f = BlockRegistries.ENERGY_INPUT_HATCH.get(),
                g = Blocks.RED_CONCRETE,
                h = Blocks.RED_STAINED_GLASS,
                i = Blocks.GLOWSTONE;

        Block[][][] blockArray = new Block[][][]{
                {
                        {g, h, g, a, a},
                        {h, a, h, a, a},
                        {g, h, g, a, a},
                        {a, a, a, a, a},
                        {a, a, a, a, a}
                },
                {
                        {b, b, b, a, a},
                        {b, a, b, a, a},
                        {b, b, b, a, a},
                        {a, a, a, a, a},
                        {a, a, a, a, a}
                },
                {
                        {b, b, b, a, a},
                        {b, a, b, a, a},
                        {b, b, b, a, a},
                        {a, a, a, a, a},
                        {a, a, a, a, a}
                },
                {
                        {b, b, b, g, a},
                        {b, i, i, h, a},
                        {b, i, i, h, a},
                        {b, b, b, h, a},
                        {g, h, h, g, a}
                },
                {
                        {b, b, b, b, b},
                        {b, a, a, a, b},
                        {b, a, a, a, b},
                        {b, a, a, a, b},
                        {b, b, b, b, b}
                },
                {
                        {b, b, b, b, b},
                        {b, a, a, a, b},
                        {b, a, a, a, b},
                        {b, a, a, a, b},
                        {b, b, null, b, b}
                },
                {
                        {b, e, b, f, b},
                        {c, b, b, b, b},
                        {b, b, b, b, d},
                        {c, b, b, b, b},
                        {b, b, b, b, b}
                }
        };

        return new StructurePattern(blockArray);
    }
}