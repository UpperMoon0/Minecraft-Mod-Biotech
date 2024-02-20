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

import java.util.Optional;

public class RecipeSerializerFactory<T extends BiotechRecipe & RecipeFactory<T>> {
    private static final Gson GSON = new Gson();

    public RecipeSerializer<T> createSerializer(RecipeFactory<T> factory) {
        return new RecipeSerializer<T>() {
            @Override
            public T fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
                RecipeContainer recipeContainer = getRecipeContainerFromJson(pSerializedRecipe);
                return factory.create(pRecipeId, recipeContainer);
            }

            @Override
            public T fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
                ItemStack[] itemIngredients = readItemStackArray(pBuffer);
                boolean[] ingredientsConsumable = readBooleanArray(pBuffer, itemIngredients.length);

                ItemStack[] itemResults = readItemStackArray(pBuffer);

                FluidStack[] fluidIngredients = readFluidStackArray(pBuffer);
                FluidStack[] fluidResults = readFluidStackArray(pBuffer);

                int totalEnergy = pBuffer.readInt();

                RecipeContainer recipeContainer = new RecipeContainer(itemIngredients, ingredientsConsumable, itemResults, fluidIngredients, fluidResults, totalEnergy);
                return factory.create(pRecipeId, recipeContainer);
            }

            @Override
            public void toNetwork(FriendlyByteBuf pBuffer, T pRecipe) {
                RecipeContainer recipeContainer = pRecipe.getRecipeContainer();

                writeItemStackArray(pBuffer, recipeContainer.getItemIngredients());
                writeBooleanArray(pBuffer, recipeContainer.getIngredientsConsumable());

                writeItemStackArray(pBuffer, recipeContainer.getItemResults());

                writeFluidStackArray(pBuffer, recipeContainer.getFluidIngredients());
                writeFluidStackArray(pBuffer, recipeContainer.getFluidResults());

                pBuffer.writeInt(recipeContainer.getTotalEnergy());
            }
        };
    }

    private static RecipeContainer getRecipeContainerFromJson(JsonObject pSerializedRecipe) {
        ItemStack[] itemIngredients = readItemStackArrayFromJson(pSerializedRecipe.getAsJsonArray("item"));
        boolean[] ingredientsConsumable = GSON.fromJson(pSerializedRecipe.getAsJsonArray("itemConsumed"), boolean[].class);

        ItemStack[] itemResults = readItemStackArrayFromJson(pSerializedRecipe.getAsJsonArray("itemResult"));

        FluidStack[] fluidIngredients = readFluidStackArrayFromJson(pSerializedRecipe.getAsJsonArray("fluid"));
        FluidStack[] fluidResults = readFluidStackArrayFromJson(pSerializedRecipe.getAsJsonArray("fluidResult"));

        int totalEnergy = pSerializedRecipe.get("energy").getAsInt();

        return new RecipeContainer(itemIngredients, ingredientsConsumable, itemResults, fluidIngredients, fluidResults, totalEnergy);
    }

    private static ItemStack[] readItemStackArray(FriendlyByteBuf pBuffer) {
        int length = pBuffer.readInt();
        ItemStack[] array = new ItemStack[length];
        for (int i = 0; i < length; i++) {
            array[i] = pBuffer.readItem();
        }
        return array;
    }

    private static boolean[] readBooleanArray(FriendlyByteBuf pBuffer, int length) {
        boolean[] array = new boolean[length];
        for (int i = 0; i < length; i++) {
            array[i] = pBuffer.readBoolean();
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

    private static void writeItemStackArray(FriendlyByteBuf pBuffer, ItemStack[] array) {
        pBuffer.writeInt(array.length);
        for (ItemStack item : array) {
            pBuffer.writeItem(item);
        }
    }

    private static void writeBooleanArray(FriendlyByteBuf pBuffer, boolean[] array) {
        for (boolean b : array) {
            pBuffer.writeBoolean(b);
        }
    }

    private static void writeFluidStackArray(FriendlyByteBuf pBuffer, FluidStack[] array) {
        pBuffer.writeInt(array.length);
        for (FluidStack fluid : array) {
            pBuffer.writeFluidStack(fluid);
        }
    }

    private static ItemStack[] readItemStackArrayFromJson(JsonArray jsonArray) {
        return Optional.ofNullable(jsonArray)
                .map(array -> {
                    ItemStack[] itemStacks = new ItemStack[array.size()];
                    for (int i = 0; i < array.size(); i++) {
                        itemStacks[i] = readItemStack(array.get(i).getAsJsonObject());
                    }
                    return itemStacks;
                })
                .orElse(new ItemStack[0]);
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