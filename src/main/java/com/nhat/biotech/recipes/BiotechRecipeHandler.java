package com.nhat.biotech.recipes;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.HashMap;
import java.util.Map;

public abstract class BiotechRecipeHandler<T extends BiotechRecipeHandler<T>> implements Recipe<Container>, RecipeFactory<T> {
    protected final ResourceLocation id;
    protected final BiotechRecipe recipe;
    private final RecipeSerializer<T> serializer;
    private final RecipeType<T> type;

    protected BiotechRecipeHandler(ResourceLocation id, BiotechRecipe recipe, RecipeSerializer<T> serializer, RecipeType<T> type) {
        this.id = id;
        this.recipe = recipe;
        this.serializer = serializer;
        this.type = type;
    }

    @Override
    public T create(ResourceLocation id, BiotechRecipe recipeContainer) {
        if (recipeContainer == null) {
            throw new IllegalArgumentException("RecipeContainer cannot be null");
        }
        return createInstance(id, recipeContainer);
    }

    protected abstract T createInstance(ResourceLocation id, BiotechRecipe recipeContainer);

    @Override
    public RecipeSerializer<T> getSerializer() {
        return serializer;
    }

    @Override
    public RecipeType<T> getType() {
        return type;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    public BiotechRecipe getRecipe() {
        return recipe;
    }

    /**
     * Method to check if the inputs (items and fluids) match the recipe requirements.
     * This method ensures that the input slots and tanks contain the required ingredients.
     *
     * @param inputSlots     The item input slots
     * @param inputTanks     The fluid input tanks
     * @param outputSlots    The item output slots
     * @param outputTanks    The fluid output tanks
     * @return               True if the inputs match the recipe, false otherwise
     */
    public boolean recipeMatch(IItemHandler inputSlots, IFluidHandler inputTanks, IItemHandler outputSlots, IFluidHandler outputTanks) {
        Map<Item, Integer> inputItems = new HashMap<>();
        Map<Fluid, Integer> inputFluids = new HashMap<>();
        boolean itemsMatch = false;
        boolean fluidsMatch = false;

        // Check if the output slots and tanks have enough space for the result
        if (!outputSpaceAvailable(outputSlots, outputTanks)) {
            return false;
        }

        // Validate item ingredients
        if (recipe.getItemIngredients() != null) {
            // Store input items in a map to count quantities
            for (int i = 0; i < inputSlots.getSlots(); i++) {
                ItemStack stack = inputSlots.getStackInSlot(i);
                int newQuantity = inputItems.getOrDefault(stack.getItem(), 0) + stack.getCount();
                inputItems.put(stack.getItem(), newQuantity);
            }
            // Check if the input slots have enough of the required items
            itemsMatch = itemsMatch(recipe.getItemIngredients(), inputItems);
        }

        // Validate fluid ingredients
        if (recipe.getFluidIngredients() != null) {
            // Store input fluids in a map to count amounts
            for (int i = 0; i < inputTanks.getTanks(); i++) {
                FluidStack fluidStack = inputTanks.getFluidInTank(i);
                int newAmount = inputFluids.getOrDefault(fluidStack.getFluid(), 0) + fluidStack.getAmount();
                inputFluids.put(fluidStack.getFluid(), newAmount);
            }
            // Check if the input tanks have enough of the required fluids
            fluidsMatch = fluidsMatch(recipe.getFluidIngredients(), inputFluids);
        }

        return itemsMatch && fluidsMatch;
    }

    /**
     * Helper method to check if input items match the recipe's required items.
     *
     * @param itemIngredients The required item ingredients
     * @param slotItems       The items present in the input slots
     * @return                True if the input items match the required items, false otherwise
     */
    private boolean itemsMatch(ItemStack[] itemIngredients, Map<Item, Integer> slotItems) {
        Map<Item, Integer> requiredItems = new HashMap<>();
        for (ItemStack entry : itemIngredients) {
            requiredItems.put(entry.getItem(), requiredItems.getOrDefault(entry.getItem(), 0) + entry.getCount());
        }

        for (Map.Entry<Item, Integer> entry : requiredItems.entrySet()) {
            if (!slotItems.containsKey(entry.getKey()) || slotItems.get(entry.getKey()) < entry.getValue()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Helper method to check if input fluids match the recipe's required fluids.
     *
     * @param fluidIngredients The required fluid ingredients
     * @param tankFluids       The fluids present in the input tanks
     * @return                 True if the input fluids match the required fluids, false otherwise
     */
    private boolean fluidsMatch(FluidStack[] fluidIngredients, Map<Fluid, Integer> tankFluids) {
        Map<Fluid, Integer> requiredFluids = new HashMap<>();
        for (FluidStack fluidInput : fluidIngredients) {
            requiredFluids.put(fluidInput.getFluid(), requiredFluids.getOrDefault(fluidInput.getFluid(), 0) + fluidInput.getAmount());
        }

        for (Map.Entry<Fluid, Integer> entry : requiredFluids.entrySet()) {
            if (!tankFluids.containsKey(entry.getKey()) || tankFluids.get(entry.getKey()) < entry.getValue()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if the output slots and tanks have enough space for the recipe result.
     *
     * @param outputSlots The item output slots
     * @param outputTanks The fluid output tanks
     * @return            True if there is enough space for the result, false otherwise
     */
    private boolean outputSpaceAvailable(IItemHandler outputSlots, IFluidHandler outputTanks) {
        // Check if the item output slots have enough space for the result
        for (ItemStack result : recipe.getItemResults()) {
            boolean canInsertItem = false;
            for (int i = 0; i < outputSlots.getSlots(); i++) {
                ItemStack slot = outputSlots.getStackInSlot(i);
                if (slot.isEmpty() || (slot.getItem().equals(result.getItem()) && slot.getCount() < slot.getMaxStackSize())) {
                    canInsertItem = true;
                    break;
                }
            }
            if (!canInsertItem) {
                return false;
            }
        }

        // Check if the fluid output tanks have enough space for the result
        for (FluidStack result : recipe.getFluidResults()) {
            boolean canInsertFluid = false;
            for (int i = 0; i < outputTanks.getTanks(); i++) {
                FluidStack tankFluid = outputTanks.getFluidInTank(i);
                if (tankFluid.isEmpty() || (tankFluid.getFluid().equals(result.getFluid()) && tankFluid.getAmount() + result.getAmount() <= outputTanks.getTankCapacity(i))) {
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

    public void assemble(IItemHandler inputSlots, IFluidHandler inputTanks, IItemHandler outputSlots, IFluidHandler outputTanks) {
        // Insert the item results into the output slots
        if (recipe.getItemResults() != null) {
            for (ItemStack result : recipe.getItemResults()) {
                for (int i = 0; i < outputSlots.getSlots(); i++) {
                    ItemStack slotStack = outputSlots.getStackInSlot(i);
                    if (slotStack.isEmpty()) {
                        outputSlots.insertItem(i, result.copy(), false);
                        break;
                    } else if (slotStack.getItem().equals(result.getItem()) && slotStack.getCount() < slotStack.getMaxStackSize()) {
                        slotStack.grow(result.getCount());
                        break;
                    }
                }
            }
        }

        // Insert the fluid results into the output tanks
        if (recipe.getFluidResults() != null) {
            for (FluidStack result : recipe.getFluidResults()) {
                for (int i = 0; i < outputTanks.getTanks(); i++) {
                    FluidStack tankFluid = outputTanks.getFluidInTank(i);
                    if (tankFluid.isEmpty()) {
                        outputTanks.fill(result.copy(), IFluidHandler.FluidAction.EXECUTE);
                        break;
                    } else if (tankFluid.getFluid().equals(result.getFluid()) && tankFluid.getAmount() + result.getAmount() <= outputTanks.getTankCapacity(i)) {
                        outputTanks.fill(result, IFluidHandler.FluidAction.EXECUTE);
                        break;
                    }
                }
            }
        }
    }

    public void consumeIngredients(IItemHandler inputSlots, IFluidHandler inputTanks) {
        // Consume the required items
        if (recipe.getItemIngredients() != null) {
            Map<Item, Integer> requiredItems = new HashMap<>();
            for (ItemStack entry : recipe.getItemIngredients()) {
                requiredItems.put(entry.getItem(), requiredItems.getOrDefault(entry.getItem(), 0) + entry.getCount());
            }

            for (Map.Entry<Item, Integer> entry : requiredItems.entrySet()) {
                int remaining = entry.getValue();
                for (int i = 0; i < inputSlots.getSlots() && remaining > 0; i++) {
                    ItemStack slotStack = inputSlots.getStackInSlot(i);
                    if (slotStack.getItem().equals(entry.getKey())) {
                        int ingredientIndex = recipe.getIngredientIndex(slotStack.getItem());
                        if (ingredientIndex != -1 && !recipe.getIngredientsConsumable()[ingredientIndex]) {
                            continue;
                        }
                        if (slotStack.getCount() <= remaining) {
                            remaining -= slotStack.getCount();
                            inputSlots.extractItem(i, slotStack.getCount(), false);
                        } else {
                            inputSlots.extractItem(i, remaining, false);
                            remaining = 0;
                        }
                    }
                }
            }
        }

        // Consume the required fluids
        if (recipe.getFluidIngredients() != null) {
            Map<Fluid, Integer> requiredFluids = new HashMap<>();
            for (FluidStack fluidInput : recipe.getFluidIngredients()) {
                requiredFluids.put(fluidInput.getFluid(), requiredFluids.getOrDefault(fluidInput.getFluid(), 0) + fluidInput.getAmount());
            }

            for (Map.Entry<Fluid, Integer> entry : requiredFluids.entrySet()) {
                int remaining = entry.getValue();
                for (int i = 0; i < inputTanks.getTanks() && remaining > 0; i++) {
                    FluidStack tankFluid = inputTanks.getFluidInTank(i);
                    if (tankFluid.getFluid().equals(entry.getKey())) {
                        if (tankFluid.getAmount() <= remaining) {
                            remaining -= tankFluid.getAmount();
                            inputTanks.drain(tankFluid.getAmount(), IFluidHandler.FluidAction.EXECUTE);
                        } else {
                            inputTanks.drain(remaining, IFluidHandler.FluidAction.EXECUTE);
                            remaining = 0;
                        }
                    }
                }
            }
        }
    }

    /**
     * Retrieves the total energy required for this recipe.
     *
     * @return The total energy cost of the recipe
     */
    public int getTotalEnergy() {
        return recipe.getTotalEnergy();
    }

    // Methods not used by BiotechRecipe, overridden to return default values
    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        return false;
    }

    @Override
    public ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
        return null;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }
}
