package com.nhat.biotech.utils;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public abstract class RecipeHelper {
    private static boolean isMatch(BiotechRecipeEntry[] recipeIngredients, List<BiotechRecipeEntry> ingredient) {
        for (int i = 0; i < recipeIngredients.length; i++) {
            if (recipeIngredients[i].getItem() != ingredient.get(i).getItem() || recipeIngredients[i].getCount() > ingredient.get(i).getCount()) {
                return false;
            }
        }
        return true;
    }
    public static BiotechRecipe hasRecipe(List<BiotechRecipe> recipeList, int outputStartIndex, BlockEntity blockEntity) {
        AtomicReference<BiotechRecipe> result = new AtomicReference<>(null);
        blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            List<BiotechRecipeEntry> ingredient = new ArrayList<>();
            for (int i = 0; i < outputStartIndex; i++) {
                ItemStack stack = handler.getStackInSlot(i);
                ingredient.add(new BiotechRecipeEntry(stack.getItem(), stack.getCount()));
            }
            for (BiotechRecipe recipe : recipeList) {
                if (recipe.getInputs().length == ingredient.size() && isMatch(recipe.getInputs(), ingredient)) {
                    result.set(recipe);
                    break;
                }
            }
        });
        return result.get();
    }
    private static boolean isOutputEmpty(int[] outputRange, IItemHandler handler) {
        for (int i = outputRange[0]; i <= outputRange[1]; i++) {
            if (!handler.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public static void craft(BiotechRecipe recipe, int outputStartIndex, BlockEntity blockEntity) {
        blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            boolean canOutput = true;
            BiotechRecipeEntry[] inputs = recipe.getInputs();
            boolean[] isInputConsumed = recipe.getIsInputConsumed();
            BiotechRecipeEntry[] outputs = recipe.getOutputs();

            if (outputs != null) {
                int[] outputIndexRange = new int[]{outputStartIndex, outputStartIndex + outputs.length - 1};
                if (!isOutputEmpty(outputIndexRange, handler)) {
                    int i = outputStartIndex;
                    for (BiotechRecipeEntry output : outputs) {
                        if (handler.getStackInSlot(i).getItem() != output.getItem() || handler.getStackInSlot(i).getCount() + output.getCount() > handler.getStackInSlot(i).getMaxStackSize()) {
                            canOutput = false;
                            break;
                        }
                        i++;
                    }
                }
                if (canOutput) {
                    int i = 0;
                    for (BiotechRecipeEntry input : inputs) {
                        if (isInputConsumed[i])
                            handler.extractItem(i, input.getCount(), false);
                        i++;
                    }
                    i = outputStartIndex;
                    for (BiotechRecipeEntry output : outputs) {
                        handler.insertItem(i, new ItemStack(output.getItem(), output.getCount()), false);
                        i++;
                    }
                }
            }
        });
    }
}
