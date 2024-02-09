package com.nhat.biotech.recipes;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.io.*;

public class RecipeContainer implements Serializable {
    private final ItemStack[] itemIngredients;
    private final boolean[] ingredientsConsumable;
    private final ItemStack[] itemResults;
    private final FluidStack[] fluidIngredients;
    private final FluidStack[] fluidResults;
    private final int totalEnergy;
    public RecipeContainer(ItemStack[] inputs, boolean[] isInputConsumed, ItemStack[] outputs, FluidStack[] fluidInputs, FluidStack[] fluidOutputs, int totalEnergy) {
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

    public void writeToBuf(FriendlyByteBuf buf) {
        buf.writeInt(itemIngredients.length);
        for (ItemStack itemStack : itemIngredients) {
            buf.writeItem(itemStack);
        }
        buf.writeInt(ingredientsConsumable.length);
        for (boolean b : ingredientsConsumable) {
            buf.writeBoolean(b);
        }
        buf.writeInt(itemResults.length);
        for (ItemStack itemStack : itemResults) {
            buf.writeItem(itemStack);
        }
        buf.writeInt(fluidIngredients.length);
        for (FluidStack fluidStack : fluidIngredients) {
            buf.writeFluidStack(fluidStack);
        }
        buf.writeInt(fluidResults.length);
        for (FluidStack fluidStack : fluidResults) {
            buf.writeFluidStack(fluidStack);
        }
        buf.writeInt(totalEnergy);
    }

    public static RecipeContainer fromBuf(FriendlyByteBuf buf) {
        int itemIngredientsLength = buf.readInt();
        ItemStack[] itemIngredients = new ItemStack[itemIngredientsLength];
        for (int i = 0; i < itemIngredientsLength; i++) {
            itemIngredients[i] = buf.readItem();
        }
        int ingredientsConsumableLength = buf.readInt();
        boolean[] ingredientsConsumable = new boolean[ingredientsConsumableLength];
        for (int i = 0; i < ingredientsConsumableLength; i++) {
            ingredientsConsumable[i] = buf.readBoolean();
        }
        int itemResultsLength = buf.readInt();
        ItemStack[] itemResults = new ItemStack[itemResultsLength];
        for (int i = 0; i < itemResultsLength; i++) {
            itemResults[i] = buf.readItem();
        }
        int fluidIngredientsLength = buf.readInt();
        FluidStack[] fluidIngredients = new FluidStack[fluidIngredientsLength];
        for (int i = 0; i < fluidIngredientsLength; i++) {
            fluidIngredients[i] = buf.readFluidStack();
        }
        int fluidResultsLength = buf.readInt();
        FluidStack[] fluidResults = new FluidStack[fluidResultsLength];
        for (int i = 0; i < fluidResultsLength; i++) {
            fluidResults[i] = buf.readFluidStack();
        }
        int totalEnergy = buf.readInt();
        return new RecipeContainer(itemIngredients, ingredientsConsumable, itemResults, fluidIngredients, fluidResults, totalEnergy);
    }
}
