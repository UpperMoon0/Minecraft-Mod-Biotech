package com.nhat.biotech.recipes;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.world.item.ItemStack;

@EqualsAndHashCode(callSuper = true)
@Data
public class OutputItem extends RecipeItem {
    private float chance;

    public OutputItem(ItemStack itemStack, float chance) {
        super(itemStack);
        this.chance = chance;
    }
}
