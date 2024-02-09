package com.nhat.biotech.recipes;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.HashMap;
import java.util.Map;

public abstract class BiotechRecipe implements Recipe<Container> {
    protected final ResourceLocation ID;
    protected final RecipeContainer RECIPE_CONTAINER;

    protected BiotechRecipe(ResourceLocation id, RecipeContainer recipeContainer) {
        ID = id;
        RECIPE_CONTAINER = recipeContainer;
    }

    /**
     * Checks if the recipe matches the current state of the provided container.
     * This implementation always returns false because custom validation logic is used for BiotechRecipe.
     *
     * @param pContainer  the container to check
     * @param pLevel      the current level
     * @return            false because this method is not supported for BreederRecipe
     */
    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        return false;
    }

    /**
     * Assembles the result of the recipe from the state of the provided container.
     * This implementation always returns null because custom assembly logic is used for BiotechRecipe.
     *
     * @param pContainer      the container to assemble the result from
     * @param pRegistryAccess the registry access
     * @return                null because this method is not supported for BreederRecipe
     */
    @Override
    public ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
        return null;
    }

    /**
     * Returns the result of the recipe.
     * This implementation always returns null because custom result logic is used for BiotechRecipe.
     *
     * @param pRegistryAccess the registry access
     * @return                null because this method is not supported for BreederRecipe
     */
    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return null;
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    public RecipeContainer getRecipeContainer() {
        return RECIPE_CONTAINER;
    }

    public boolean recipeMatch(ItemStack[] inputSlots, FluidTank[] inputTanks, ItemStack[] outputSlots, FluidTank[] outputTanks) {
        Map<Item, Integer> slotItems = new HashMap<>();
        Map<Fluid, Integer> tankFluids = new HashMap<>();
        boolean flag1 = false;
        boolean flag2 = false;

        if (!outputSpaceAvailable(outputSlots, outputTanks)) {
            return false;
        }

        if (RECIPE_CONTAINER.getItemIngredients() != null) {
            if (inputSlots == null)
                return false;
            // Save the items in the input slots to a map
            for (ItemStack stack : inputSlots) {
                int newQuantity = slotItems.getOrDefault(stack.getItem(), 0) + stack.getCount();
                slotItems.put(stack.getItem(), newQuantity);
            }
            // Check if the input slots have enough of the required items
            flag1 = itemsMatch(RECIPE_CONTAINER.getItemIngredients(), slotItems);
        }

        if (RECIPE_CONTAINER.getFluidIngredients() != null) {
            if (inputTanks == null)
                return false;
            // Save the fluids in the input tanks to a map
            for (FluidTank tank : inputTanks) {
                FluidStack fluidStack = tank.getFluid();
                int newAmount = tankFluids.getOrDefault(fluidStack.getFluid(), 0) + fluidStack.getAmount();
                tankFluids.put(fluidStack.getFluid(), newAmount);
            }
            // Check if the input tanks have enough of the required fluids
            flag2 = fluidsMatch(RECIPE_CONTAINER.getFluidIngredients(), tankFluids);
        }

        if (flag1 && flag2) {
            return true;
        }

        return false;
    }
    private boolean itemsMatch(ItemStack[] itemIngredients, Map<Item, Integer> slotItems) {
        // Save the required item ingredients to a map
        Map<Item, Integer> requiredItems = new HashMap<>();
        for (ItemStack entry : itemIngredients) {
            requiredItems.put(entry.getItem(), requiredItems.getOrDefault(entry.getItem(), 0) + entry.getCount());
        }

        // Check if the input slots have enough of the required items
        for (Map.Entry<Item, Integer> entry : requiredItems.entrySet()) {
            if (!slotItems.containsKey(entry.getKey()) || slotItems.get(entry.getKey()) < entry.getValue()) {
                return false;
            }
        }

        return true;
    }
    private boolean fluidsMatch(FluidStack[] fluidIngredients, Map<Fluid, Integer> tankFluids) {
        // Save the required fluids ingredients to a map
        Map<Fluid, Integer> requiredFluids = new HashMap<>();
        for (FluidStack fluidInput : fluidIngredients) {
            requiredFluids.put(fluidInput.getFluid(), requiredFluids.getOrDefault(fluidInput.getFluid(), 0) + fluidInput.getAmount());
        }

        // Check if the input tanks have enough of the required fluids
        for (Map.Entry<Fluid, Integer> entry : requiredFluids.entrySet()) {
            if (!tankFluids.containsKey(entry.getKey()) || tankFluids.get(entry.getKey()) < entry.getValue()) {
                return false;
            }
        }

        return true;
    }
    private boolean outputSpaceAvailable(ItemStack[] outputSlots, FluidTank[] outputTanks) {
        // Check item results
        for (ItemStack result : RECIPE_CONTAINER.getItemResults()) {
            boolean canInsertItem = false;
            for (ItemStack slot : outputSlots) {
                if (slot.isEmpty() || (slot.getItem().equals(result.getItem()) && slot.getCount() < slot.getMaxStackSize())) {
                    canInsertItem = true;
                    break;
                }
            }
            if (!canInsertItem) {
                return false;
            }
        }

        // Check fluid results
        for (FluidStack result : RECIPE_CONTAINER.getFluidResults()) {
            boolean canInsertFluid = false;
            for (FluidTank tank : outputTanks) {
                if (tank.isEmpty() || (tank.getFluidInTank(0).getFluid().equals(result.getFluid()) && tank.getFluidInTank(0).getAmount() + result.getAmount() <= tank.getCapacity())) {
                    canInsertFluid = true;
                    break;
                }
            }
            if (!canInsertFluid) {
                return false;
            }
        }

        return true;
    }


    public void craft(ItemStack[] inputSlots, FluidTank[] inputTanks, ItemStack[] outputSlots, FluidTank[] outputTanks) {
        // Consume the required items from the input slots
        if (RECIPE_CONTAINER.getItemIngredients() != null) {
            // If the input slots are null, return
            if (inputSlots == null)
                return;

            // Save the required item ingredients to a map
            Map<Item, Integer> requiredItems = new HashMap<>();
            for (int i = 0; i < RECIPE_CONTAINER.getItemIngredients().length; i++) {
                ItemStack entry = RECIPE_CONTAINER.getItemIngredients()[i];
                requiredItems.put(entry.getItem(), requiredItems.getOrDefault(entry.getItem(), 0) + entry.getCount());
            }

            // Loop through the required items
            for (Map.Entry<Item, Integer> entry : requiredItems.entrySet()) {
                int remaining = entry.getValue();

                // Loop through the input slots
                for (int i = 0; i < inputSlots.length && remaining > 0; i++) {
                    // If the ingredient is not in the input slots, skip it
                    if (inputSlots[i].getItem().equals(entry.getKey())) {
                        int ingredientIndex = RECIPE_CONTAINER.getIngredientIndex(inputSlots[i].getItem());
                        // If the ingredient is not consumable, skip it
                        if (ingredientIndex != -1 && !RECIPE_CONTAINER.getIngredientsConsumable()[ingredientIndex]) {
                            continue;
                        }
                        // If the ingredient is consumable, consume it
                        if (inputSlots[i].getCount() <= remaining) {
                            remaining -= inputSlots[i].getCount();
                            inputSlots[i] = ItemStack.EMPTY;
                        } else {
                            inputSlots[i].shrink(remaining);
                            remaining = 0;
                        }
                    }
                }
            }
        }

        // Consume the required fluids from the input tanks
        if (RECIPE_CONTAINER.getFluidIngredients() != null) {
            // If the input tanks are null, return
            if (inputTanks == null)
                return;

            // Save the required fluids ingredients to a map
            Map<Fluid, Integer> requiredFluids = new HashMap<>();
            for (FluidStack fluidInput : RECIPE_CONTAINER.getFluidIngredients()) {
                requiredFluids.put(fluidInput.getFluid(), requiredFluids.getOrDefault(fluidInput.getFluid(), 0) + fluidInput.getAmount());
            }

            for (Map.Entry<Fluid, Integer> entry : requiredFluids.entrySet()) {
                int remaining = entry.getValue();
                for (int i = 0; i < inputTanks.length && remaining > 0; i++) {
                    if (inputTanks[i].getFluid().getFluid().equals(entry.getKey())) {
                        if (inputTanks[i].getFluidAmount() <= remaining) {
                            remaining -= inputTanks[i].getFluidAmount();
                            inputTanks[i].setFluid(FluidStack.EMPTY);
                        } else {
                            inputTanks[i].drain(remaining, IFluidHandler.FluidAction.EXECUTE);
                            remaining = 0;
                        }
                    }
                }
            }
        }

        // Insert the item results into the output slots
        if (RECIPE_CONTAINER.getItemResults() != null) {
            for (ItemStack result : RECIPE_CONTAINER.getItemResults()) {
                for (int i = 0; i < outputSlots.length; i++) {
                    if (outputSlots[i].isEmpty()) {
                        outputSlots[i] = result.copy();
                        break;
                    } else if (outputSlots[i].getItem().equals(result.getItem()) && outputSlots[i].getCount() < outputSlots[i].getMaxStackSize()) {
                        outputSlots[i].grow(result.getCount());
                        break;
                    }
                }
            }
        }

        // Insert the fluid results into the output tanks
        if (RECIPE_CONTAINER.getFluidResults() != null) {
            for (FluidStack result : RECIPE_CONTAINER.getFluidResults()) {
                for (FluidTank outputTank : outputTanks) {
                    if (outputTank.isEmpty()) {
                        outputTank.setFluid(result.copy());
                        break;
                    } else if (outputTank.getFluid().getFluid().equals(result.getFluid()) && outputTank.getFluidAmount() + result.getAmount() <= outputTank.getCapacity()) {
                        outputTank.fill(result, IFluidHandler.FluidAction.EXECUTE);
                        break;
                    }
                }
            }
        }
    }

    public int getTotalEnergy() {
        return RECIPE_CONTAINER.getTotalEnergy();
    }

    public ItemStack[] getItemIngredients() {
        return RECIPE_CONTAINER.getItemIngredients();
    }

    public FluidStack[] getFluidIngredients() {
        return RECIPE_CONTAINER.getFluidIngredients();
    }

    public ItemStack[] getItemResults() {
        return RECIPE_CONTAINER.getItemResults();
    }
}
