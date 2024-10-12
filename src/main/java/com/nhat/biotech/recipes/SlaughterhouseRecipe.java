package com.nhat.biotech.recipes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class SlaughterhouseRecipe extends BiotechRecipe<SlaughterhouseRecipe> {
    public static final RecipeSerializer<SlaughterhouseRecipe> SERIALIZER =
            new RecipeSerializerFactory<SlaughterhouseRecipe>().createSerializer(SlaughterhouseRecipe::new);

    public static final RecipeType<SlaughterhouseRecipe> TYPE = new RecipeType<>() {
    };

    public SlaughterhouseRecipe(ResourceLocation id, BiotechRecipeData recipe) {
        super(id, recipe, SERIALIZER, TYPE);
    }

    @Override
    protected SlaughterhouseRecipe createInstance(ResourceLocation id, BiotechRecipeData recipe) {
        return new SlaughterhouseRecipe(id, recipe);
    }
}
