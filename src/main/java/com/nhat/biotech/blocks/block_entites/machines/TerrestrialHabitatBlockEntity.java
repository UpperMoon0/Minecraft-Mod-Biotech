package com.nhat.biotech.blocks.block_entites.machines;

import com.nhat.biotech.blocks.ModBlocks;
import com.nhat.biotech.blocks.block_entites.ModBlockEntities;
import com.nhat.biotech.blocks.block_entites.hatches.EnergyInputHatchBlockEntity;
import com.nhat.biotech.blocks.block_entites.hatches.FluidInputHatchBlockEntity;
import com.nhat.biotech.blocks.block_entites.hatches.ItemInputHatchBlockEntity;
import com.nhat.biotech.blocks.block_entites.hatches.ItemOutputHatchBlockEntity;
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
import org.jetbrains.annotations.Nullable;

public class TerrestrialHabitatBlockEntity extends AbstractMachineBlockEntity {
    private ItemInputHatchBlockEntity itemInputHatch1;
    private ItemInputHatchBlockEntity itemInputHatch2;
    private ItemInputHatchBlockEntity itemInputHatch3;
    private ItemOutputHatchBlockEntity itemOutputHatch;
    private EnergyInputHatchBlockEntity energyInputHatch;
    private FluidInputHatchBlockEntity fluidInputHatch;
    public TerrestrialHabitatBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.TERRESTRIAL_HABITAT.get(), pPos, pBlockState);
        translateKey = "menu.title.biotech.terrestrial_habitat";
    }

    @Override
    protected void processRecipe(Level level, BlockPos blockPos) {
        if (isStructureValid) {
            System.out.println("TerrestrialHabitatBlockEntity.processRecipe");
        }
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
    protected Block[][][] getPattern() {
        Block a = Blocks.AIR,
                b = ModBlocks.BIOTECH_MACHINE_CASING.get(),
                c = ModBlocks.ITEM_INPUT_HATCH.get(),
                d = ModBlocks.ITEM_OUTPUT_HATCH.get(),
                e = ModBlocks.FLUID_INPUT_HATCH.get(),
                f = ModBlocks.ENERGY_INPUT_HATCH.get(),
                g = Blocks.YELLOW_CONCRETE,
                h = Blocks.YELLOW_STAINED_GLASS,
                i = Blocks.GLOWSTONE,
                j = Blocks.GRASS_BLOCK;

        return new Block[][][]{
                {
                        {g, h, h, g, a, a, a},
                        {h, i, i, h, a, a, a},
                        {h, i, i, h, a, a, a},
                        {g, h, h, g, a, a, a},
                        {a, a, a, a, a, a, a},
                        {a, a, a, a, a, a, a},
                        {a, a, a, a, a, a, a}
                },
                {
                        {b, b, b, b, a, a, a},
                        {b, a, a, b, a, a, a},
                        {b, a, a, b, a, a, a},
                        {b, b, b, b, a, a, a},
                        {a, a, a, a, a, a, a},
                        {a, a, a, a, a, a, a},
                        {a, a, a, a, a, a, a}
                },
                {
                        {b, b, b, b, a, a, a},
                        {b, a, a, b, a, a, a},
                        {b, a, a, b, a, a, a},
                        {b, a, a, b, a, a, a},
                        {a, a, a, a, a, a, a},
                        {a, a, a, a, a, a, a},
                        {a, a, a, a, a, a, a}
                },
                {
                        {b, b, b, b, h, h, g},
                        {b, a, a, b, a, a, h},
                        {b, a, a, b, a, a, h},
                        {b, a, a, b, a, a, h},
                        {h, a, a, a, a, a, h},
                        {h, a, a, a, a, a, h},
                        {g, h, h, h, h, h, g}
                },
                {
                        {b, b, b, b, b, b, b},
                        {b, a, a, b, a, a, b},
                        {b, a, a, b, a, a, b},
                        {b, a, a, b, a, a, b},
                        {b, a, a, a, a, a, b},
                        {b, a, a, a, a, a, b},
                        {b, b, b, null, b, b, b}
                },
                {
                        {b, e, b, f, b, b, b},
                        {c, b, b, b, j, j, b},
                        {b, b, b, b, j, j, b},
                        {c, b, b, b, j, j, d},
                        {b, j, j, j, j, j, b},
                        {c, j, j, j, j, j, b},
                        {b, b, b, b, b, b, b}
                }
        };
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return null;
    }
}