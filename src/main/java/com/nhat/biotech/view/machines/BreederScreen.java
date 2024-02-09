package com.nhat.biotech.view.machines;

import com.nhat.biotech.Biotech;
import com.nhat.biotech.recipes.RecipeContainer;
import com.nhat.biotech.view.renderer.BiotechFluidRenderer;
import com.nhat.biotech.view.renderer.BiotechFluidTankRenderer;
import com.nhat.biotech.view.renderer.BiotechItemRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.Optional;

public class BreederScreen extends AbstractContainerScreen<BreederMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Biotech.MODID, "textures/gui/breeder.png");
    public BreederScreen(BreederMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        this.imageWidth = 212;
    }
    
    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        int energyConsumeRate = 0;

        if (this.menu.getIsOperating()) {
            energyConsumeRate = menu.getEnergyConsumeRate();

            RecipeContainer recipe = menu.getRecipe();
            String animalRawName = recipe.getItemIngredients()[0].getDisplayName().getString();
            String animalName = animalRawName.substring(1, animalRawName.length() - 1);
            pGuiGraphics.drawCenteredString(font, animalName, 70, 76, 0xFFFFFF);

            String foodRawName = recipe.getItemIngredients()[1].getDisplayName().getString();
            String foodName = foodRawName.substring(1, foodRawName.length() - 1);
            if (isHovering(31, 95, 16, 16, pMouseX, pMouseY)) {
                pGuiGraphics.renderTooltip(font, List.of(Component.literal(foodName)), Optional.empty(), pMouseX - leftPos, pMouseY - topPos);
            }
            int foodCount = recipe.getItemIngredients()[1].getCount();
            pGuiGraphics.drawCenteredString(font, String.valueOf(foodCount), 67, 111, 0xFFFFFF);

            String fluidName = recipe.getFluidIngredients()[0].getDisplayName().getString();
            if (isHovering(94, 125, 16, 16, pMouseX, pMouseY)) {
                pGuiGraphics.renderTooltip(font, List.of(Component.literal(fluidName)), Optional.empty(), pMouseX - leftPos, pMouseY - topPos);
            }
            int fluidAmount = recipe.getFluidIngredients()[0].getAmount();
            pGuiGraphics.drawCenteredString(font, fluidAmount + " mB", 62, 144, 0xFFFFFF);

            int energyCost = recipe.getTotalEnergy();
            pGuiGraphics.drawCenteredString(font, energyCost + " FE", 150, 148, 0xFFFFFF);
        }

        if (isHovering(4, 43, 9, 76, pMouseX, pMouseY)) {
            if (menu.getStructureValid()) {
                pGuiGraphics.renderTooltip(
                        font,
                        List.of(
                                Component.literal("Stored Energy:"),
                                Component.literal(menu.getEnergyStored() + " / " + menu.getEnergyCapacity() + " FE"),
                                Component.literal("Consuming: "),
                                Component.literal(energyConsumeRate+ " FE/t")
                        ),
                        Optional.empty(),
                        pMouseX - leftPos,
                        pMouseY - topPos
                );
            }
            else {
                pGuiGraphics.renderTooltip(font, Component.literal("Invalid Structure"), pMouseX - leftPos, pMouseY - topPos);
            }
        }

        if (isHovering(196, 28, 12, 75, pMouseX, pMouseY)) {
            if (menu.getStructureValid()) {
                FluidStack storedFluid = menu.getFluidStored();
                String fluidName = storedFluid.isEmpty()? "Empty" : storedFluid.getDisplayName().getString();
                pGuiGraphics.renderTooltip(font, List.of(Component.literal("Stored Fluid:"), Component.literal(fluidName), Component.literal( storedFluid.getAmount() + " / " + menu.getFluidCapacity() + " mB")), Optional.empty(), pMouseX - leftPos, pMouseY - topPos);
            }
            else {
                pGuiGraphics.renderTooltip(font, Component.literal("Invalid Structure"), pMouseX - leftPos, pMouseY - topPos);
            }
        }

        int x = 106 - font.width("Breeding Chamber") / 2;
        pGuiGraphics.drawString(font, "Breeding Chamber", x, 3, 0x3F3F3F, false);
        pGuiGraphics.drawCenteredString(font, "Food", 79, 96, 0xFFFFFF);
        pGuiGraphics.drawCenteredString(font, "Fluid", 61, 129, 0xFFFFFF);
        pGuiGraphics.drawCenteredString(font, "Energy", 150, 134, 0xFFFFFF);
    }
    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        renderBackground(graphics);
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        if (menu.getStructureValid())
        {
            if (menu.getEnergyStored() > 0) {
                int energyHeight = menu.getEnergyHeight();
                graphics.blit(TEXTURE, leftPos + 4, topPos + 119 - energyHeight, 212, 95 - energyHeight, 9, energyHeight);
            }
            if (!menu.getFluidStored().isEmpty())
            {
                BiotechFluidTankRenderer fTankRenderer = new BiotechFluidTankRenderer(menu.getFluidCapacity(), 12, 75);
                fTankRenderer.renderFluid(graphics.pose() , leftPos + 196, topPos + 28, menu.getFluidStored());
            }
            if (this.menu.getIsOperating()) {
                graphics.blit(TEXTURE, this.leftPos + 88, this.topPos + 28, 212, 0, this.menu.getProgressWidth() + 1, 19);

                ItemStack currentAnimal = menu.getRecipe().getItemIngredients()[0];

                BiotechItemRenderer animalItemRenderer = new BiotechItemRenderer(48, 48);
                animalItemRenderer.render(graphics.pose(), leftPos + 46, topPos + 35, currentAnimal);

                ItemStack currentFood = menu.getRecipe().getItemIngredients()[1];
                graphics.renderItem(currentFood, leftPos + 30, topPos + 94);

                FluidStack currentFluid = menu.getRecipe().getFluidIngredients()[0];
                BiotechFluidRenderer fluidRenderer = new BiotechFluidRenderer();
                fluidRenderer.renderFluid(graphics.pose(), this.leftPos + 94, this.topPos + 124, 16, 16, currentFluid);

                graphics.blit(TEXTURE, this.leftPos + 147, this.topPos + 126, 212, 95, 7, 2);
            }
        }
    }
}
