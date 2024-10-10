package com.nhat.biotech.recipes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class SlaughterhouseRecipeHandler extends BiotechRecipeHandler<SlaughterhouseRecipeHandler> {
    public static final RecipeSerializer<SlaughterhouseRecipeHandler> SERIALIZER =
            new RecipeSerializerFactory<SlaughterhouseRecipeHandler>().createSerializer(SlaughterhouseRecipeHandler::new);

    public static final RecipeType<SlaughterhouseRecipeHandler> TYPE = new RecipeType<>() {
    };

    public SlaughterhouseRecipeHandler(ResourceLocation id, BiotechRecipe recipe) {
        super(id, recipe, SERIALIZER, TYPE);
    }

    @Override
    protected SlaughterhouseRecipeHandler createInstance(ResourceLocation id, BiotechRecipe recipe) {
        return new SlaughterhouseRecipeHandler(id, recipe);
    }
}
