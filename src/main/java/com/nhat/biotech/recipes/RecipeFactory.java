package com.nhat.biotech.recipes;

import net.minecraft.resources.ResourceLocation;

public interface RecipeFactory<T extends BiotechRecipe> {
    T create(ResourceLocation id, RecipeContainer recipeContainer);
}
