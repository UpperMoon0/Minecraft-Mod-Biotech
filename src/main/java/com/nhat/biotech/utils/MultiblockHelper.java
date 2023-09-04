package com.nhat.biotech.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public abstract class MultiblockHelper {
    public static boolean checkMultiblock(Level level, BlockPos controllerPos, DirectionProperty facing, BlockState blockState, Block[][][] pattern, int controllerHeight)
    {
        BlockPos currentLoc;

        //Rotate the pattern
        switch (blockState.getValue(facing)) {
            case NORTH -> {
                for (Block[][] blocks : pattern) {
                    for (int k = 0; k < 2; k++) {
                        rotateBlockMatrix(3, blocks);
                    }
                }
            }
            case EAST -> {
                //Rotate the pattern
                for (Block[][] blocks : pattern) {
                    rotateBlockMatrix(3, blocks);
                }
            }
            case WEST -> {
                //Rotate the pattern
                for (Block[][] blocks : pattern) {
                    for (int k = 0; k < 3; k++) {
                        rotateBlockMatrix(3, blocks);
                    }
                }
            }
            default -> {
            }
        }

        //Loop the y dimension
        for (int y = 0; y < pattern.length; y++) {
            //Loop the z dimension
            for (int z = 0; z < pattern[0].length; z++) {
                //Loop the x dimension
                for (int x = 0; x < pattern[0][0].length; x++) {
                    //Check facing
                    switch (blockState.getValue(facing)) {
                        case NORTH -> {
                            currentLoc = new BlockPos(controllerPos.getX() - pattern[0][0].length / 2 + x, controllerPos.getY() - (controllerHeight - 1) + y, controllerPos.getZ() + z);

                            if (!level.getBlockState(currentLoc).is(pattern[pattern.length - 1 - y][z][x]) && pattern[pattern.length - 1 - y][z][x] != null)
                                return false;
                        }
                        case SOUTH -> {
                            currentLoc = new BlockPos(controllerPos.getX() - pattern[0][0].length / 2 + x, controllerPos.getY() - (controllerHeight - 1) + y, controllerPos.getZ() - (pattern[0].length - 1) + z);

                            if (!level.getBlockState(currentLoc).is(pattern[pattern.length - 1 - y][z][x]) && pattern[pattern.length - 1 - y][z][x] != null)
                                return false;
                        }
                        case EAST -> {
                            currentLoc = new BlockPos(controllerPos.getX() - (pattern[0][0].length - 1) + x, controllerPos.getY() - (controllerHeight - 1) + y, controllerPos.getZ() - pattern[0].length / 2 + z);

                            if (!level.getBlockState(currentLoc).is(pattern[pattern.length - 1 - y][z][x]) && pattern[pattern.length - 1 - y][z][x] != null)
                                return false;
                        }
                        case WEST -> {
                            currentLoc = new BlockPos(controllerPos.getX() + x, controllerPos.getY() - (controllerHeight - 1) + y, controllerPos.getZ() - pattern[0].length / 2 + z);

                            if (!level.getBlockState(currentLoc).is(pattern[pattern.length - 1 - y][z][x]) && pattern[pattern.length - 1 - y][z][x] != null)
                                return false;
                        }
                        default -> {
                        }
                    }
                }
            }
        }

        return true;
    }
    public static void rotateBlockMatrix(int N, Block[][] mat)
    {
        // Consider all squares one by one
        for (int x = 0; x < N / 2; x++) {
            // Consider elements in group
            // of 4 in current square
            for (int y = x; y < N - x - 1; y++) {
                // Store current cell in
                // temp variable
                Block temp = mat[x][y];

                // Move values from right to top
                mat[x][y] = mat[y][N - 1 - x];

                // Move values from bottom to right
                mat[y][N - 1 - x] = mat[N - 1 - x][N - 1 - y];

                // Move values from left to bottom
                mat[N - 1 - x][N - 1 - y] = mat[N - 1 - y][x];

                // Assign temp to left
                mat[N - 1 - y][x] = temp;
            }
        }
    }
}
