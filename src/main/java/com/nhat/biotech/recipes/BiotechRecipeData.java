package com.nhat.biotech.recipes;

import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraftforge.fluids.FluidStack;

import java.io.*;

@Getter
public class BiotechRecipeData implements Serializable {

    private final IngredientItem[] ingredientItems;
    private final OutputItem[] outputItems;
    private final FluidStack[] fluidIngredients;
    private final FluidStack[] fluidOutputs;
    private final int totalEnergy;

    public BiotechRecipeData(IngredientItem[] inputs, OutputItem[] outputs, FluidStack[] fluidInputs, FluidStack[] fluidOutputs, int totalEnergy) {
        this.ingredientItems = inputs;
        this.outputItems = outputs;
        this.fluidIngredients = fluidInputs;
        this.fluidOutputs = fluidOutputs;
        this.totalEnergy = totalEnergy;
    }

    public int getIngredientIndex(Item item) {
        for (int i = 0; i < ingredientItems.length; i++) {
            if (ingredientItems[i].getItemStack().getItem().equals(item)) {
                return i;
            }
        }
        return -1;
    }

    public void writeToBuf(FriendlyByteBuf buf) {
        buf.writeInt(ingredientItems.length);
        for (IngredientItem ingredientItem : ingredientItems) {
            buf.writeItem(ingredientItem.getItemStack());
            buf.writeBoolean(ingredientItem.isConsumable());
        }
        buf.writeInt(outputItems.length);
        for (OutputItem outputItem : outputItems) {
            buf.writeItem(outputItem.getItemStack());
            buf.writeFloat(outputItem.getChance());
        }
        buf.writeInt(fluidIngredients.length);
        for (FluidStack fluidStack : fluidIngredients) {
            buf.writeFluidStack(fluidStack);
        }
        buf.writeInt(fluidOutputs.length);
        for (FluidStack fluidStack : fluidOutputs) {
            buf.writeFluidStack(fluidStack);
        }
        buf.writeInt(totalEnergy);
    }

    public static BiotechRecipeData fromBuf(FriendlyByteBuf buf) {
        int ingredientItemCount = buf.readInt();
        IngredientItem[] ingredientItems = new IngredientItem[ingredientItemCount];
        for (int i = 0; i < ingredientItemCount; i++) {
            ingredientItems[i] = new IngredientItem(buf.readItem(), buf.readBoolean());
        }
        int outputItemCount = buf.readInt();
        OutputItem[] outputItems = new OutputItem[outputItemCount];
        for (int i = 0; i < outputItemCount; i++) {
            outputItems[i] = new OutputItem(buf.readItem(), buf.readFloat());
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
        return new BiotechRecipeData(ingredientItems, outputItems, fluidIngredients, fluidResults, totalEnergy);
    }
}
