package com.nhat.biotech.utils;

import com.nhat.biotech.items.IModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.LinkedHashSet;
import java.util.Set;

public interface IAnimalSets {
    Set<Item> ADULT_ANIMAL = new LinkedHashSet<>() {{
        add(IModItems.COW.get());
        add(IModItems.CHICKEN.get());
        add(IModItems.PIG.get());
        add(IModItems.SHEEP.get());
        add(IModItems.RABBIT.get());
    }};
    Set<Item> FOOD = new LinkedHashSet<>() {{
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
