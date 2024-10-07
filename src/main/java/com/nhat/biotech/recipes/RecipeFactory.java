package com.nhat.biotech.recipes;

import net.minecraft.resources.ResourceLocation;

public interface RecipeFactory<T extends BiotechRecipeHandler> {
    T create(ResourceLocation id, BiotechRecipe recipeContainer);
}
