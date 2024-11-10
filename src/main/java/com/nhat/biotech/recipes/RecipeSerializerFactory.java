package com.nhat.biotech.recipes;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class RecipeSerializerFactory<T extends BiotechRecipe<T> & RecipeFactory<T>> {
    private static final Gson GSON = new Gson();

    public RecipeSerializer<T> createSerializer(RecipeFactory<T> factory) {
        return new RecipeSerializer<T>() {
            @Override
            public @NotNull T fromJson(@NotNull ResourceLocation pRecipeId, @NotNull JsonObject pSerializedRecipe) {
                BiotechRecipeData recipeData = readRecipeDataFromJson(pSerializedRecipe);
                return factory.create(pRecipeId, recipeData);
            }

            @Override
            public T fromNetwork(@NotNull ResourceLocation pRecipeId, @NotNull FriendlyByteBuf pBuffer) {
                IngredientItem[] itemIngredients = readItemIngredientArray(pBuffer);
                OutputItem[] itemResults = readOutputItemArray(pBuffer);

                FluidStack[] fluidIngredients = readFluidStackArray(pBuffer);
                FluidStack[] fluidResults = readFluidStackArray(pBuffer);

                int totalEnergy = pBuffer.readInt();

                BiotechRecipeData recipeData = new BiotechRecipeData(itemIngredients, itemResults, fluidIngredients, fluidResults, totalEnergy);
                return factory.create(pRecipeId, recipeData);
            }

            @Override
            public void toNetwork(@NotNull FriendlyByteBuf pBuffer, @NotNull T pRecipe) {
                BiotechRecipeData recipeContainer = pRecipe.getRecipe();

                writeIngredientItemArray(pBuffer, recipeContainer.getIngredientItems());
                writeOutputItemArray(pBuffer, recipeContainer.getOutputItems());

                writeFluidStackArray(pBuffer, recipeContainer.getFluidIngredients());
                writeFluidStackArray(pBuffer, recipeContainer.getFluidOutputs());

                pBuffer.writeInt(recipeContainer.getTotalEnergy());
            }
        };
    }

    private static BiotechRecipeData readRecipeDataFromJson(JsonObject pSerializedRecipe) {
        IngredientItem[] ingredientItems = readIngredientItemArrayFromJson(pSerializedRecipe.getAsJsonArray("itemInputs"));
        OutputItem[] outputItems = readOutputItemArrayFromJson(pSerializedRecipe.getAsJsonArray("itemOutputs"));

        FluidStack[] fluidIngredients = readFluidStackArrayFromJson(pSerializedRecipe.getAsJsonArray("fluidInputs"));
        FluidStack[] fluidResults = readFluidStackArrayFromJson(pSerializedRecipe.getAsJsonArray("fluidOutputs"));

        int totalEnergy = pSerializedRecipe.get("energy").getAsInt();

        return new BiotechRecipeData(ingredientItems, outputItems, fluidIngredients, fluidResults, totalEnergy);
    }

    private static IngredientItem[] readItemIngredientArray(FriendlyByteBuf pBuffer) {
        int length = pBuffer.readInt();
        IngredientItem[] array = new IngredientItem[length];
        for (int i = 0; i < length; i++) {
            array[i] = new IngredientItem(pBuffer.readItem(), pBuffer.readBoolean());
        }
        return array;
    }

    private static OutputItem[] readOutputItemArray(FriendlyByteBuf pBuffer) {
        int length = pBuffer.readInt();
        OutputItem[] array = new OutputItem[length];
        for (int i = 0; i < length; i++) {
            array[i] = new OutputItem(pBuffer.readItem(), pBuffer.readFloat());
        }
        return array;
    }

    private static FluidStack[] readFluidStackArray(FriendlyByteBuf pBuffer) {
        int length = pBuffer.readInt();
        FluidStack[] array = new FluidStack[length];
        for (int i = 0; i < length; i++) {
            array[i] = pBuffer.readFluidStack();
        }
        return array;
    }

    private static void writeIngredientItemArray(FriendlyByteBuf pBuffer, IngredientItem[] array) {
        pBuffer.writeInt(array.length);
        for (IngredientItem item : array) {
            pBuffer.writeItem(item.getItemStack());
            pBuffer.writeBoolean(item.isConsumable());
        }
    }

    private static void writeOutputItemArray(FriendlyByteBuf pBuffer, OutputItem[] array) {
        pBuffer.writeInt(array.length);
        for (OutputItem item : array) {
            pBuffer.writeItem(item.getItemStack());
            pBuffer.writeFloat(item.getChance());
        }
    }

    private static void writeFluidStackArray(FriendlyByteBuf pBuffer, FluidStack[] array) {
        pBuffer.writeInt(array.length);
        for (FluidStack fluid : array) {
            pBuffer.writeFluidStack(fluid);
        }
    }

    private static IngredientItem[] readIngredientItemArrayFromJson(JsonArray ingredientArray) {
        return Optional.ofNullable(ingredientArray)
                .map(array -> {
                    IngredientItem[] ingredientItems = new IngredientItem[array.size()];
                    for (int i = 0; i < array.size(); i++) {
                        JsonObject ingredientObject = array.get(i).getAsJsonObject();
                        JsonObject itemStackObject = ingredientObject.getAsJsonObject("itemStack");
                        ItemStack itemStack = readItemStack(itemStackObject);
                        boolean isConsumable = ingredientObject.has("isConsumable") && ingredientObject.get("isConsumable").getAsBoolean();
                        ingredientItems[i] = new IngredientItem(itemStack, isConsumable);
                    }
                    return ingredientItems;
                })
                .orElse(new IngredientItem[0]);
    }

    private static OutputItem[] readOutputItemArrayFromJson(JsonArray outputArray) {
        return Optional.ofNullable(outputArray)
                .map(array -> {
                    OutputItem[] outputItems = new OutputItem[array.size()];
                    for (int i = 0; i < array.size(); i++) {
                        JsonObject outputObject = array.get(i).getAsJsonObject();
                        JsonObject itemStackObject = outputObject.getAsJsonObject("itemStack");
                        ItemStack itemStack = readItemStack(itemStackObject);
                        float chance = outputObject.has("chance") ? outputObject.get("chance").getAsFloat() : 1.0f;
                        outputItems[i] = new OutputItem(itemStack, chance);
                    }
                    return outputItems;
                })
                .orElse(new OutputItem[0]);
    }

    private static FluidStack[] readFluidStackArrayFromJson(JsonArray jsonArray) {
        return Optional.ofNullable(jsonArray)
                .map(array -> {
                    FluidStack[] fluidStacks = new FluidStack[array.size()];
                    for (int i = 0; i < array.size(); i++) {
                        fluidStacks[i] = readFluidStack(array.get(i).getAsJsonObject());
                    }
                    return fluidStacks;
                })
                .orElse(new FluidStack[0]);
    }

    private static ItemStack readItemStack(JsonObject json) {
        return ItemStack.CODEC.decode(JsonOps.INSTANCE, json).result().orElseThrow().getFirst();
    }

    private static FluidStack readFluidStack(JsonObject json) {
        return FluidStack.CODEC.decode(JsonOps.INSTANCE, json).result().orElseThrow().getFirst();
    }
}