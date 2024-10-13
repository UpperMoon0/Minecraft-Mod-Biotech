package com.nhat.biotech.jei;

import com.nhat.biotech.Biotech;
import com.nhat.biotech.blocks.block_entites.machines.MachineRegistries;
import com.nhat.biotech.recipes.SlaughterhouseRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SlaughterhouseCategory implements IRecipeCategory<SlaughterhouseRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(Biotech.MOD_ID, "slaughterhouse");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Biotech.MOD_ID, "textures/gui/jei/slaughterhouse.png");

    public static final RecipeType<SlaughterhouseRecipe> TYPE = new RecipeType<>(UID, SlaughterhouseRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public SlaughterhouseCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 132, 38);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(MachineRegistries.SLAUGHTERHOUSE.blockItem().get()));
    }

    @Override
    public @NotNull RecipeType<SlaughterhouseRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("block.biotech.slaughterhouse");
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return this.background;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull SlaughterhouseRecipe recipe, @NotNull IFocusGroup focuses) {

        Ingredient ingredient = recipe.getItemIngredients().get(0);
        FluidStack fluidIngredient = recipe.getFluidIngredients().get(0);
        List<Ingredient> itemOutputs = recipe.getItemOutputs();

        builder.addSlot(RecipeIngredientRole.INPUT, 23, 1).addIngredients(ingredient);
        builder.addSlot(RecipeIngredientRole.INPUT, 23, 21).addFluidStack(fluidIngredient.getFluid(), fluidIngredient.getAmount()).setFluidRenderer(fluidIngredient.getAmount(), false, 16, 16);

        for (int i = 0; i < itemOutputs.size(); i++) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 80 + (i % 3) * 17, 3 + (i / 3) * 17).addIngredients(itemOutputs.get(i));
        }
    }
}
