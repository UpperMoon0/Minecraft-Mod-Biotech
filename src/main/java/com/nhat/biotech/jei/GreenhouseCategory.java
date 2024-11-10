package com.nhat.biotech.jei;

import com.nhat.biotech.Biotech;
import com.nhat.biotech.blocks.block_entites.machines.MachineRegistries;
import com.nhat.biotech.recipes.GreenhouseRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GreenhouseCategory implements IRecipeCategory<GreenhouseRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(Biotech.MOD_ID, MachineRegistries.GREENHOUSE.id());
    public static final ResourceLocation TEXTURE = new ResourceLocation(Biotech.MOD_ID, "textures/gui/jei/" + MachineRegistries.GREENHOUSE.id() + ".png");

    public static final RecipeType<GreenhouseRecipe> TYPE = new RecipeType<>(UID, GreenhouseRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public GreenhouseCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 121, 52);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(MachineRegistries.GREENHOUSE.blockItem().get()));
    }

    @Override
    public @NotNull RecipeType<GreenhouseRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("block.biotech." + MachineRegistries.GREENHOUSE.id());
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
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull GreenhouseRecipe recipe, @NotNull IFocusGroup focuses) {

        List<Ingredient> ingredients = recipe.getItemIngredients();
        FluidStack fluidIngredient = recipe.getFluidIngredients().get(0);
        List<Ingredient> itemOutputs = recipe.getItemOutputs();

        builder.addSlot(RecipeIngredientRole.INPUT, 23, 1).addIngredients(ingredients.get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 23, 21).addFluidStack(fluidIngredient.getFluid(), fluidIngredient.getAmount()).setFluidRenderer(fluidIngredient.getAmount(), false, 16, 16);

        for (int i = 0; i < itemOutputs.size(); i++) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 81 + i * 17, 12).addIngredients(itemOutputs.get(i));
        }
    }

    @Override
    public void draw(GreenhouseRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Minecraft minecraft = Minecraft.getInstance();
        int energy = recipe.getTotalEnergy();
        guiGraphics.drawString(minecraft.font, "Energy: " + energy + " FE", 0, 42, 4210752, false);
    }
}
