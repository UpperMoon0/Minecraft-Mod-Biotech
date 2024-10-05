package com.nhat.biotech.recipes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class BreedingChamberRecipeHandler extends BaseBiotechRecipeHandler<BreedingChamberRecipeHandler> {
    public static final RecipeSerializer<BreedingChamberRecipeHandler> SERIALIZER =
            new RecipeSerializerFactory<BreedingChamberRecipeHandler>().createSerializer(BreedingChamberRecipeHandler::new);

    public static final RecipeType<BreedingChamberRecipeHandler> TYPE = new RecipeType<>(){};

    public BreedingChamberRecipeHandler(ResourceLocation id, BiotechRecipe recipe) {
        super(id, recipe, SERIALIZER, TYPE);
    }

    @Override
    protected BreedingChamberRecipeHandler createInstance(ResourceLocation id, BiotechRecipe recipe) {
        return new BreedingChamberRecipeHandler(id, recipe);
    }
}