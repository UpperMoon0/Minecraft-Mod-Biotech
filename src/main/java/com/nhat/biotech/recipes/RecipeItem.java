package com.nhat.biotech.recipes;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.minecraft.world.item.ItemStack;

@NoArgsConstructor
@Data
public abstract class RecipeItem {
    protected ItemStack itemStack;

    public RecipeItem(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
}
