package com.nhat.biotech.data.models;

import com.nhat.biotech.blocks.MachineBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

public class StructurePattern {

    private final Block[][][] pattern;
    private final boolean debug;

    public StructurePattern(Block[][][] pattern) {
        this.pattern = pattern;
        this.debug = false;
    }

    public StructurePattern(Block[][][] pattern, boolean debug) {
        this.pattern = pattern;
        this.debug = debug;
    }

    public Block[][][] getPattern() {
        return pattern;
    }

    public boolean check(Level level, BlockPos blockPos, BlockState blockState, int controllerHeight) {
        BlockPos currentBlockPos;
        Block[][][] patternCopy = copyPattern(pattern);

        // Rotate the pattern copy
        switch (blockState.getValue(MachineBlock.FACING)) {
            case NORTH -> {
                for (Block[][] layer : patternCopy) {
                    for (int k = 0; k < 2; k++) {
                        rotateBlockMatrix(layer);
                    }
                }
                if (debug) {
                    System.out.println("North");
                }
            }
            case EAST -> {
                for (Block[][] layer : patternCopy) {
                    rotateBlockMatrix(layer);
                }
                if (debug) {
                    System.out.println("East");
                }
            }
            case WEST -> {
                for (Block[][] layer : patternCopy) {
                    for (int k = 0; k < 3; k++) {
                        rotateBlockMatrix(layer);
                    }
                }
                if (debug) {
                    System.out.println("West");
                }
            }
            default -> {
                if (debug) {
                    System.out.println("South");
                }
            }
        }

        if (debug) {
            for (int y = 0; y < patternCopy.length; y++) {
                System.out.println("Layer " + (y + 1) + ":");
                for (int z = 0; z < patternCopy[0].length; z++) {
                    for (int x = 0; x < patternCopy[0][0].length; x++) {
                        Block block = patternCopy[patternCopy.length - 1 - y][z][x];
                        System.out.printf("%-40s | ", Objects.requireNonNullElse(block, "null"));
                    }
                    System.out.println();
                }
                System.out.println();
            }
        }

        // Loop the y dimension
        for (int y = 0; y < patternCopy.length; y++) {
            // Loop the z dimension
            for (int z = 0; z < patternCopy[0].length; z++) {
                // Loop the x dimension
                for (int x = 0; x < patternCopy[0][0].length; x++) {
                    // Check facing
                    switch (blockState.getValue(MachineBlock.FACING)) {
                        case NORTH -> {
                            currentBlockPos = new BlockPos(blockPos.getX() - patternCopy[0][0].length / 2 + x, blockPos.getY() - (controllerHeight - 1) + y, blockPos.getZ() + z);

                            if (!level.getBlockState(currentBlockPos).is(patternCopy[patternCopy.length - 1 - y][z][x]) && patternCopy[patternCopy.length - 1 - y][z][x] != null) {
                                if (debug)
                                    System.out.println("Invalid block at " + currentBlockPos + ". Expected " + patternCopy[patternCopy.length - 1 - y][z][x] + ", but found " + level.getBlockState(currentBlockPos));
                                return false;
                            }
                        }
                        case SOUTH -> {
                            currentBlockPos = new BlockPos(blockPos.getX() - patternCopy[0][0].length / 2 + x, blockPos.getY() - (controllerHeight - 1) + y, blockPos.getZ() - (patternCopy[0].length - 1) + z);

                            if (!level.getBlockState(currentBlockPos).is(patternCopy[patternCopy.length - 1 - y][z][x]) && patternCopy[patternCopy.length - 1 - y][z][x] != null) {
                                if (debug)
                                    System.out.println("Invalid block at " + currentBlockPos + ". Expected " + patternCopy[patternCopy.length - 1 - y][z][x] + ", but found " + level.getBlockState(currentBlockPos));
                                return false;
                            }
                        }
                        case EAST -> {
                            currentBlockPos = new BlockPos(blockPos.getX() - (patternCopy[0][0].length - 1) + x, blockPos.getY() - (controllerHeight - 1) + y, blockPos.getZ() - patternCopy[0].length / 2 + z);

                            if (!level.getBlockState(currentBlockPos).is(patternCopy[patternCopy.length - 1 - y][z][x]) && patternCopy[patternCopy.length - 1 - y][z][x] != null) {
                                if (debug)
                                    System.out.println("Invalid block at " + currentBlockPos + ". Expected " + patternCopy[patternCopy.length - 1 - y][z][x] + ", but found " + level.getBlockState(currentBlockPos));
                                return false;
                            }
                        }
                        case WEST -> {
                            currentBlockPos = new BlockPos(blockPos.getX() + x, blockPos.getY() - (controllerHeight - 1) + y, blockPos.getZ() - patternCopy[0].length / 2 + z);

                            if (!level.getBlockState(currentBlockPos).is(patternCopy[patternCopy.length - 1 - y][z][x]) && patternCopy[patternCopy.length - 1 - y][z][x] != null) {
                                if (debug)
                                    System.out.println("Invalid block at " + currentBlockPos + ". Expected " + patternCopy[patternCopy.length - 1 - y][z][x] + ", but found " + level.getBlockState(currentBlockPos));
                                return false;
                            }
                        }
                        default -> {
                        }
                    }
                }
            }
        }

        return true;
    }

    private Block[][][] copyPattern(Block[][][] original) {
        Block[][][] copy = new Block[original.length][][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = new Block[original[i].length][];
            for (int j = 0; j < original[i].length; j++) {
                copy[i][j] = original[i][j].clone();
            }
        }
        return copy;
    }

    private static void rotateBlockMatrix(Block[][] layer)
    {
        int size = layer.length;
        // Consider all squares one by one
        for (int x = 0; x < size / 2; x++) {
            // Consider elements in group of 4 in current square
            for (int y = x; y < size - x - 1; y++) {
                // Store current cell in temp variable
                Block temp = layer[x][y];

                // Move values from right to top
                layer[x][y] = layer[y][size - 1 - x];

                // Move values from bottom to right
                layer[y][size - 1 - x] = layer[size - 1 - x][size - 1 - y];

                // Move values from left to bottom
                layer[size - 1 - x][size - 1 - y] = layer[size - 1 - y][x];

                // Assign temp to left
                layer[size - 1 - y][x] = temp;
            }
        }
    }
}
