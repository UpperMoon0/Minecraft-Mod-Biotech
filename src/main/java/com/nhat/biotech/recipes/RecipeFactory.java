package com.nhat.biotech.recipes;

import net.minecraft.resources.ResourceLocation;

public interface RecipeFactory<T extends BaseBiotechRecipeHandler> {
    T create(ResourceLocation id, BiotechRecipe recipeContainer);
}
