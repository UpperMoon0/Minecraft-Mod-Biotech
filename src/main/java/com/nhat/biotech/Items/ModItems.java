package com.nhat.biotech.Items;

import com.nhat.biotech.Biotech;
import com.nhat.biotech.Blocks.ModBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Biotech.MODID);
    public static final RegistryObject<Item> NET_TRAP_ITEM = ITEMS.register("net_trap", () -> new BlockItem(ModBlocks.NET_TRAP.get(), new Item.Properties()));
    public static final RegistryObject<Item> COW_ITEM = ITEMS.register("cow", () -> new MobItem(1));
    public static final RegistryObject<Item> BABY_COW_ITEM = ITEMS.register("baby_cow", () -> new MobItem(2));
    public static final RegistryObject<Item> CHICKEN_ITEM = ITEMS.register("chicken", () -> new MobItem(3));
    public static final RegistryObject<Item> BABY_CHICKEN_ITEM = ITEMS.register("baby_chicken", () -> new MobItem(4));
    public static final RegistryObject<Item> PIG_ITEM = ITEMS.register("pig", () -> new MobItem(5));
    public static final RegistryObject<Item> BABY_PIG_ITEM = ITEMS.register("baby_pig", () -> new MobItem(6));
    public static final RegistryObject<Item> SHEEP_ITEM = ITEMS.register("sheep", () -> new MobItem(7));
    public static final RegistryObject<Item> BABY_SHEEP_ITEM = ITEMS.register("baby_sheep", () -> new MobItem(8));
    public static final RegistryObject<Item> RABBIT_ITEM = ITEMS.register("rabbit", () -> new MobItem(9));
    public static final RegistryObject<Item> BABY_RABBIT_ITEM = ITEMS.register("baby_rabbit", () -> new MobItem(10));
}
