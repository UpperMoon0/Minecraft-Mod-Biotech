package com.nhat.biotech.recipes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class BreedingChamberRecipe extends BiotechRecipe<BreedingChamberRecipe> {
    public static final RecipeSerializer<BreedingChamberRecipe> SERIALIZER =
            new RecipeSerializerFactory<BreedingChamberRecipe>().createSerializer(BreedingChamberRecipe::new);

    public static final RecipeType<BreedingChamberRecipe> TYPE = new RecipeType<>(){};

    public BreedingChamberRecipe(ResourceLocation id, BiotechRecipeData recipe) {
        super(id, recipe, SERIALIZER, TYPE);
    }

    @Override
    protected BreedingChamberRecipe createInstance(ResourceLocation id, BiotechRecipeData recipe) {
        return new BreedingChamberRecipe(id, recipe);
    }
}