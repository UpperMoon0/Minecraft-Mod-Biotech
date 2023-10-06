package com.nhat.biotech.recipes;

import com.google.gson.JsonObject;
import com.nhat.biotech.Biotech;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public class BreederRecipe extends BiotechRecipe {
    public BreederRecipe(ResourceLocation id, BiotechRecipeEntity recipeEntity) {
        super(id, recipeEntity);
    }
    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<BreederRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "breeder";
    }

    public static class Serializer implements RecipeSerializer<BreederRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(Biotech.MODID, "breeder");
        @Override
        public BreederRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            return new BreederRecipe(ID, JsonHelper.getRecipeEntityFromJson(pSerializedRecipe));
        }

        @Override
        public @Nullable BreederRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            int itemIngredientsCount = pBuffer.readInt();
            ItemStack[] itemIngredients = null;
            boolean[] ingredientsConsumable = null;
            if (itemIngredientsCount > 0) {
                itemIngredients = new ItemStack[itemIngredientsCount];
                for (int i = 0; i < itemIngredientsCount; i++) {
                    itemIngredients[i] = pBuffer.readItem();
                }

                ingredientsConsumable = new boolean[itemIngredientsCount];
                for (int i = 0; i < itemIngredientsCount; i++) {
                    ingredientsConsumable[i] = pBuffer.readBoolean();
                }
            }

            int itemResultsCount = pBuffer.readInt();
            ItemStack[] itemResults = null;
            if (itemResultsCount > 0) {
                itemResults = new ItemStack[itemResultsCount];
                for (int i = 0; i < itemResultsCount; i++) {
                    itemResults[i] = pBuffer.readItem();
                }
            }

            int fluidIngredientsCount = pBuffer.readInt();
            FluidStack[] fluidIngredients = null;
            if (fluidIngredientsCount > 0) {
                fluidIngredients = new FluidStack[fluidIngredientsCount];
                for (int i = 0; i < fluidIngredientsCount; i++) {
                    fluidIngredients[i] = pBuffer.readFluidStack();
                }
            }

            int fluidResultsCount = pBuffer.readInt();
            FluidStack[] fluidResults = null;
            if (fluidResultsCount > 0) {
                fluidResults = new FluidStack[fluidResultsCount];
                for (int i = 0; i < fluidResultsCount; i++) {
                    fluidResults[i] = pBuffer.readFluidStack();
                }
            }

            int totalEnergy = pBuffer.readInt();

            return new BreederRecipe(pRecipeId, new BiotechRecipeEntity(itemIngredients, ingredientsConsumable, itemResults, fluidIngredients, fluidResults, totalEnergy));
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, BreederRecipe pRecipe) {
            BiotechRecipeEntity rEntity = pRecipe.getRecipeEntity();

            if (rEntity.getItemIngredients() != null) {
                pBuffer.writeInt(rEntity.getItemIngredients().length);
                for (ItemStack itemStack : rEntity.getItemIngredients()) {
                    pBuffer.writeItem(itemStack);
                }

                for (boolean isConsumed : rEntity.getIngredientsConsumable()) {
                    pBuffer.writeBoolean(isConsumed);
                }
            } else {
                pBuffer.writeInt(0);
            }

            if (rEntity.getItemResults() != null) {
                pBuffer.writeInt(rEntity.getItemResults().length);
                for (ItemStack itemStack : rEntity.getItemResults()) {
                    pBuffer.writeItem(itemStack);
                }
            } else {
                pBuffer.writeInt(0);
            }

            if (rEntity.getFluidIngredients() != null) {
                pBuffer.writeInt(rEntity.getFluidIngredients().length);
                for (FluidStack fluidStack : rEntity.getFluidIngredients()) {
                    pBuffer.writeFluidStack(fluidStack);
                }
            } else {
                pBuffer.writeInt(0);
            }

            if (rEntity.getFluidResults() != null) {
                pBuffer.writeInt(rEntity.getFluidResults().length);
                for (FluidStack fluidStack : rEntity.getFluidResults()) {
                    pBuffer.writeFluidStack(fluidStack);
                }
            } else {
                pBuffer.writeInt(0);
            }

            pBuffer.writeInt(rEntity.getTotalEnergy());
        }
    }
}
