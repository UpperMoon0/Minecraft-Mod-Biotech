package com.nhat.biotech.recipes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class BreedingChamberRecipe extends BiotechRecipe implements RecipeFactory<BreedingChamberRecipe> {
    public static final RecipeSerializer<BreedingChamberRecipe> SERIALIZER =
            new RecipeSerializerFactory<BreedingChamberRecipe>().createSerializer(BreedingChamberRecipe::new);
    public static final RecipeType<BreedingChamberRecipe> TYPE = new RecipeType<>(){};

    public BreedingChamberRecipe(ResourceLocation id, RecipeContainer recipeEntity) {
        super(id, recipeEntity);
    }

    @Override
    public BreedingChamberRecipe create(ResourceLocation id, RecipeContainer recipeContainer) {
        if (recipeContainer == null) {
            throw new IllegalArgumentException("RecipeContainer cannot be null");
        }
        return new BreedingChamberRecipe(id, recipeContainer);
    }

    @Override
    public RecipeSerializer<BreedingChamberRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public RecipeType<BreedingChamberRecipe> getType() {
        return TYPE;
    }
}
