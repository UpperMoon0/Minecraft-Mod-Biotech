package com.nhat.biotech.utils;

import com.nhat.biotech.blocks.MachineBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class MultiblockUtils {
    public static boolean checkMultiblock(Level level, BlockPos blockPos, BlockState blockState, Block[][][] pattern, int controllerHeight)
    {
        boolean debug = false;
        BlockPos currentLoc;

        //Rotate the pattern
        switch (blockState.getValue(MachineBlock.FACING)) {
            case NORTH -> {
                for (Block[][] layer : pattern) {
                    for (int k = 0; k < 2; k++) {
                        rotateBlockMatrix(layer);
                    }
                }
                if (debug)
                    System.out.println("North");
            }
            case EAST -> {
                //Rotate the pattern
                for (Block[][] layer : pattern) {
                    rotateBlockMatrix(layer);
                }
                if (debug)
                    System.out.println("East");
            }
            case WEST -> {
                //Rotate the pattern
                for (Block[][] layer : pattern) {
                    for (int k = 0; k < 3; k++) {
                        rotateBlockMatrix(layer);
                    }
                }
                if (debug)
                    System.out.println("West");
            }
            default -> {
                if (debug)
                    System.out.println("South");
            }
        }

        if (debug) {
            for (int y = 0; y < pattern.length; y++) {
                System.out.println("Layer " + (y + 1) + ":");
                for (int z = 0; z < pattern[0].length; z++) {
                    for (int x = 0; x < pattern[0][0].length; x++) {
                        Block block = pattern[pattern.length - 1 - y][z][x];
                        if (block != null) {
                            System.out.printf("%-40s | ", block);
                        } else {
                            System.out.printf("%-40s | ", "null");
                        }
                    }
                    System.out.println();
                }
                System.out.println();
            }
        }

        //Loop the y dimension
        for (int y = 0; y < pattern.length; y++) {
            //Loop the z dimension
            for (int z = 0; z < pattern[0].length; z++) {
                //Loop the x dimension
                for (int x = 0; x < pattern[0][0].length; x++) {
                    //Check facing
                    switch (blockState.getValue(MachineBlock.FACING)) {
                        case NORTH -> {
                            currentLoc = new BlockPos(blockPos.getX() - pattern[0][0].length / 2 + x, blockPos.getY() - (controllerHeight - 1) + y, blockPos.getZ() + z);

                            if (!level.getBlockState(currentLoc).is(pattern[pattern.length - 1 - y][z][x]) && pattern[pattern.length - 1 - y][z][x] != null) {
                                if (debug)
                                    System.out.println("Invalid block at " + currentLoc + ". Expected " + pattern[pattern.length - 1 - y][z][x] + ", but found " + level.getBlockState(currentLoc));
                                return false;
                            }
                        }
                        case SOUTH -> {
                            currentLoc = new BlockPos(blockPos.getX() - pattern[0][0].length / 2 + x, blockPos.getY() - (controllerHeight - 1) + y, blockPos.getZ() - (pattern[0].length - 1) + z);

                            if (!level.getBlockState(currentLoc).is(pattern[pattern.length - 1 - y][z][x]) && pattern[pattern.length - 1 - y][z][x] != null) {
                                if (debug)
                                    System.out.println("Invalid block at " + currentLoc + ". Expected " + pattern[pattern.length - 1 - y][z][x] + ", but found " + level.getBlockState(currentLoc));
                                return false;
                            }
                        }
                        case EAST -> {
                            currentLoc = new BlockPos(blockPos.getX() - (pattern[0][0].length - 1) + x, blockPos.getY() - (controllerHeight - 1) + y, blockPos.getZ() - pattern[0].length / 2 + z);

                            if (!level.getBlockState(currentLoc).is(pattern[pattern.length - 1 - y][z][x]) && pattern[pattern.length - 1 - y][z][x] != null) {
                                if (debug)
                                    System.out.println("Invalid block at " + currentLoc + ". Expected " + pattern[pattern.length - 1 - y][z][x] + ", but found " + level.getBlockState(currentLoc));
                                return false;
                            }
                        }
                        case WEST -> {
                            currentLoc = new BlockPos(blockPos.getX() + x, blockPos.getY() - (controllerHeight - 1) + y, blockPos.getZ() - pattern[0].length / 2 + z);

                            if (!level.getBlockState(currentLoc).is(pattern[pattern.length - 1 - y][z][x]) && pattern[pattern.length - 1 - y][z][x] != null) {
                                if (debug)
                                    System.out.println("Invalid block at " + currentLoc + ". Expected " + pattern[pattern.length - 1 - y][z][x] + ", but found " + level.getBlockState(currentLoc));
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
    private static void rotateBlockMatrix(Block[][] mat)
    {
        int size = mat.length;
        // Consider all squares one by one
        for (int x = 0; x < size / 2; x++) {
            // Consider elements in group
            // of 4 in current square
            for (int y = x; y < size - x - 1; y++) {
                // Store current cell in
                // temp variable
                Block temp = mat[x][y];

                // Move values from right to top
                mat[x][y] = mat[y][size - 1 - x];

                // Move values from bottom to right
                mat[y][size - 1 - x] = mat[size - 1 - x][size - 1 - y];

                // Move values from left to bottom
                mat[size - 1 - x][size - 1 - y] = mat[size - 1 - y][x];

                // Assign temp to left
                mat[size - 1 - y][x] = temp;
            }
        }
    }
}
