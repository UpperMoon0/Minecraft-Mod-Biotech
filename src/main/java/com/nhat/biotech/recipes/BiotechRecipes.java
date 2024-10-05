package com.nhat.biotech.recipes;

import com.nhat.biotech.Biotech;
import com.nhat.biotech.utils.Machines;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BiotechRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Biotech.MODID);

    public static final RegistryObject<RecipeSerializer<BreedingChamberRecipeHandler>> BREEDING_CHAMBER_SERIALIZER =
            SERIALIZERS.register(Machines.BREEDING_CHAMBER_ID, () -> BreedingChamberRecipeHandler.SERIALIZER);

    public static final RegistryObject<RecipeSerializer<TerrestrialHabitatRecipeHandler>> TERRESTRIAL_HABITAT_SERIALIZER =
            SERIALIZERS.register(Machines.TERRESTRIAL_HABITAT_ID, () -> TerrestrialHabitatRecipeHandler.SERIALIZER);
}
