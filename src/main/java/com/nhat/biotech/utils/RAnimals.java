package com.nhat.biotech.utils;

import com.nhat.biotech.items.BiotechItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.LinkedHashSet;
import java.util.Set;

public record RAnimals() {
    public static final Set<Item> ADULT_ANIMAL;
    public static final Set<Item> FOOD;

    static {
        ADULT_ANIMAL = new LinkedHashSet<>() {{
            add(BiotechItems.COW.get());
            add(BiotechItems.CHICKEN.get());
            add(BiotechItems.PIG.get());
            add(BiotechItems.SHEEP.get());
            add(BiotechItems.RABBIT.get());
        }};
        FOOD = new LinkedHashSet<>() {{
            add(Items.WHEAT);
            add(Items.WHEAT_SEEDS);
            add(Items.CARROT);
            add(Items.GOLDEN_CARROT);
            add(Items.POTATO);
            add(Items.MELON_SEEDS);
            add(Items.PUMPKIN_SEEDS);
            add(Items.BEETROOT);
            add(Items.BEETROOT_SEEDS);
            add(Items.TORCHFLOWER_SEEDS);
            add(Items.DANDELION);
        }};
    }
}