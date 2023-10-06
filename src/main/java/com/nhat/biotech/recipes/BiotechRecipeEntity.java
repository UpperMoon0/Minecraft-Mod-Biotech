package com.nhat.biotech.recipes;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class BiotechRecipeEntity {
    private final ItemStack[] itemIngredients;
    private final boolean[] ingredientsConsumable;
    private final ItemStack[] itemResults;
    private final FluidStack[] fluidIngredients;
    private final FluidStack[] fluidResults;
    private final int totalEnergy;
    public BiotechRecipeEntity(ItemStack[] inputs, boolean[] isInputConsumed, ItemStack[] outputs, FluidStack[] fluidInputs, FluidStack[] fluidOutputs, int totalEnergy) {
        this.itemIngredients = inputs;
        this.ingredientsConsumable = isInputConsumed;
        this.itemResults = outputs;
        this.fluidIngredients = fluidInputs;
        this.fluidResults = fluidOutputs;
        this.totalEnergy = totalEnergy;
    }
    public ItemStack[] getItemIngredients() {
        return itemIngredients;
    }
    public boolean[] getIngredientsConsumable() {
        return ingredientsConsumable;
    }
    public ItemStack[] getItemResults() {
        return itemResults;
    }
    public FluidStack[] getFluidIngredients() {
        return fluidIngredients;
    }
    public FluidStack[] getFluidResults() {
        return fluidResults;
    }
    public int getTotalEnergy() {
        return totalEnergy;
    }

    public int getIngredientIndex(Item item) {
        for (int i = 0; i < itemIngredients.length; i++) {
            if (itemIngredients[i].getItem().equals(item)) {
                return i;
            }
        }
        return -1;
    }
}
