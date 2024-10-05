package com.nhat.biotech.recipes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class TerrestrialHabitatRecipeHandler extends BaseBiotechRecipeHandler<TerrestrialHabitatRecipeHandler> {
    public static final RecipeSerializer<TerrestrialHabitatRecipeHandler> SERIALIZER =
            new RecipeSerializerFactory<TerrestrialHabitatRecipeHandler>().createSerializer(TerrestrialHabitatRecipeHandler::new);

    public static final RecipeType<TerrestrialHabitatRecipeHandler> TYPE = new RecipeType<>(){};

    public TerrestrialHabitatRecipeHandler(ResourceLocation id, BiotechRecipe recipe) {
        super(id, recipe, SERIALIZER, TYPE);
    }

    @Override
    protected TerrestrialHabitatRecipeHandler createInstance(ResourceLocation id, BiotechRecipe recipe) {
        return new TerrestrialHabitatRecipeHandler(id, recipe);
    }
}