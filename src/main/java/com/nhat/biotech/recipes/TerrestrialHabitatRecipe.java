package com.nhat.biotech.recipes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class TerrestrialHabitatRecipe extends BiotechRecipe<TerrestrialHabitatRecipe> {
    public static final RecipeSerializer<TerrestrialHabitatRecipe> SERIALIZER =
            new RecipeSerializerFactory<TerrestrialHabitatRecipe>().createSerializer(TerrestrialHabitatRecipe::new);

    public static final RecipeType<TerrestrialHabitatRecipe> TYPE = new RecipeType<>(){};

    public TerrestrialHabitatRecipe(ResourceLocation id, BiotechRecipeData recipe) {
        super(id, recipe, SERIALIZER, TYPE);
    }

    @Override
    protected TerrestrialHabitatRecipe createInstance(ResourceLocation id, BiotechRecipeData recipe) {
        return new TerrestrialHabitatRecipe(id, recipe);
    }
}