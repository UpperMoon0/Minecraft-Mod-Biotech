package com.nhat.biotech.recipes;

import com.nhat.biotech.Biotech;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Biotech.MODID);

    public static final RegistryObject<RecipeSerializer<BreedingChamberRecipe>> BREEDING_CHAMBER_SERIALIZER =
            SERIALIZERS.register("breeding_chamber", () -> BreedingChamberRecipe.SERIALIZER);
}
