package com.nhat.biotech.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.nhat.biotech.recipes.RecipeContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class JsonUtils {
    public static RecipeContainer getRecipeEntityFromJson(JsonObject pSerializedRecipe) {
        Gson gson = new Gson();

        // Read the item ingredients from the JSON
        JsonArray itemIngredientsJson = pSerializedRecipe.getAsJsonArray("item");
        ItemStack[] itemIngredients = null;
        boolean[] ingredientsConsumable = null;
        if (itemIngredientsJson != null) {
            itemIngredients = new ItemStack[itemIngredientsJson.size()];
            for (int i = 0; i < itemIngredientsJson.size(); i++) {
                JsonObject inputJson = itemIngredientsJson.get(i).getAsJsonObject();
                itemIngredients[i] = readItemStack(inputJson);
            }

            JsonArray ingredientsConsumableJson = pSerializedRecipe.getAsJsonArray("itemConsumed");
            ingredientsConsumable = gson.fromJson(ingredientsConsumableJson, boolean[].class);
        }

        // Read the item results from the JSON
        JsonArray itemResultsJson = pSerializedRecipe.getAsJsonArray("itemResult");
        ItemStack[] itemResults = null;
        if (itemIngredientsJson != null) {
            itemResults = new ItemStack[itemResultsJson.size()];
            for (int i = 0; i < itemResultsJson.size(); i++) {
                JsonObject outputJson = itemResultsJson.get(i).getAsJsonObject();
                itemResults[i] = readItemStack(outputJson);
            }
        }

        // Read the fluid ingredients from the JSON
        JsonArray fluidIngredientsJson = pSerializedRecipe.getAsJsonArray("fluid");
        FluidStack[] fluidIngredients = null;
        if (fluidIngredientsJson != null) {
            fluidIngredients = new FluidStack[fluidIngredientsJson.size()];
            for (int i = 0; i < fluidIngredientsJson.size(); i++) {
                JsonObject fluidInputJson = fluidIngredientsJson.get(i).getAsJsonObject();
                fluidIngredients[i] = readFluidStack(fluidInputJson);
            }
        }

        // Read the fluid results from the JSON
        JsonArray fluidResultsJson = pSerializedRecipe.getAsJsonArray("fluidResult");
        FluidStack[] fluidResults = null;
        if (fluidResultsJson != null) {
            fluidResults = new FluidStack[fluidResultsJson.size()];
            for (int i = 0; i < fluidResultsJson.size(); i++) {
                JsonObject fluidOutputJson = fluidResultsJson.get(i).getAsJsonObject();
                fluidResults[i] = readFluidStack(fluidOutputJson);
            }
        }

        // Read the total energy required for the recipe
        int totalEnergy = pSerializedRecipe.get("energy").getAsInt();

        return new RecipeContainer(itemIngredients, ingredientsConsumable, itemResults, fluidIngredients, fluidResults, totalEnergy);
    }

    public static FluidStack readFluidStack(JsonObject json) {
        return FluidStack.CODEC.decode(JsonOps.INSTANCE, json).result().orElseThrow().getFirst();
    }

    public static JsonElement fluidStackToJson(FluidStack stack) {
        return FluidStack.CODEC.encodeStart(JsonOps.INSTANCE, stack).result().orElseThrow();
    }

    public static ItemStack readItemStack(JsonObject json) {
        return ItemStack.CODEC.decode(JsonOps.INSTANCE, json).result().orElseThrow().getFirst();
    }

    public static JsonElement itemStackToJson (ItemStack stack) {
        return ItemStack.CODEC.encodeStart(JsonOps.INSTANCE, stack).result().orElseThrow();
    }
}
