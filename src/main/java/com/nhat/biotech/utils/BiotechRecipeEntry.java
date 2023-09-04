package com.nhat.biotech.utils;

import net.minecraft.world.item.Item;

public class BiotechRecipeEntry {
    private Item item;
    private int count;
    public BiotechRecipeEntry(Item item, int count) {
        this.item = item;
        this.count = count;
    }
    public Item getItem() {
        return item;
    }
    public int getCount() {
        return count;
    }
}
