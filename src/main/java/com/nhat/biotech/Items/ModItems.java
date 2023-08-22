package com.nhat.biotech.Items;

import com.nhat.biotech.Biotech;
import com.nhat.biotech.Blocks.ModBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashSet;
import java.util.Set;

public interface ModItems {
    DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Biotech.MODID);
    RegistryObject<Item> NET_TRAP_ITEM = ITEMS.register("net_trap", () -> new BlockItem(ModBlocks.NET_TRAP.get(), new Item.Properties()));
    RegistryObject<Item> BREEDER = ITEMS.register("breeder", () -> new BlockItem(ModBlocks.BREEDER.get(), new Item.Properties()));
    RegistryObject<Item> COW_ITEM = ITEMS.register("cow", () -> new MobItem(1));
    RegistryObject<Item> BABY_COW_ITEM = ITEMS.register("baby_cow", () -> new MobItem(2));
    RegistryObject<Item> CHICKEN_ITEM = ITEMS.register("chicken", () -> new MobItem(3));
    RegistryObject<Item> BABY_CHICKEN_ITEM = ITEMS.register("baby_chicken", () -> new MobItem(4));
    RegistryObject<Item> PIG_ITEM = ITEMS.register("pig", () -> new MobItem(5));
    RegistryObject<Item> BABY_PIG_ITEM = ITEMS.register("baby_pig", () -> new MobItem(6));
    RegistryObject<Item> SHEEP_ITEM = ITEMS.register("sheep", () -> new MobItem(7));
    RegistryObject<Item> BABY_SHEEP_ITEM = ITEMS.register("baby_sheep", () -> new MobItem(8));
    RegistryObject<Item> RABBIT_ITEM = ITEMS.register("rabbit", () -> new MobItem(9));
    RegistryObject<Item> BABY_RABBIT_ITEM = ITEMS.register("baby_rabbit", () -> new MobItem(10));
    Set<RegistryObject<Item>> ITEM_SET = new LinkedHashSet<>() {{
        add(NET_TRAP_ITEM);
        add(BREEDER);
        add(COW_ITEM);
        add(BABY_COW_ITEM);
        add(CHICKEN_ITEM);
        add(BABY_CHICKEN_ITEM);
        add(PIG_ITEM);
        add(BABY_PIG_ITEM);
        add(SHEEP_ITEM);
        add(BABY_SHEEP_ITEM);
        add(RABBIT_ITEM);
        add(BABY_RABBIT_ITEM);
    }};
}
