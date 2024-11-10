package com.nhat.biotech.recipes;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.world.item.ItemStack;

@EqualsAndHashCode(callSuper = true)
@Data
public class IngredientItem extends RecipeItem {
    private boolean isConsumable;

    public IngredientItem(ItemStack itemStack, boolean isConsumable) {
        super(itemStack);
        this.isConsumable = isConsumable;
    }
}
