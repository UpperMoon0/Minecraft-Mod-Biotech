package com.nhat.biotech.items;

import com.nhat.biotech.Biotech;
import com.nhat.biotech.blocks.IModBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashSet;
import java.util.Set;

public interface IModItems {
    DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Biotech.MODID);
    RegistryObject<Item> NET_TRAP_ITEM = ITEMS.register("net_trap", () -> new BlockItem(IModBlocks.NET_TRAP.get(), new Item.Properties()));
    RegistryObject<Item> BREEDER_ITEM = ITEMS.register("breeder", () -> new BlockItem(IModBlocks.BREEDER.get(), new Item.Properties()));
    RegistryObject<Item> BIOTECH_MACHINE_CASING = ITEMS.register("biotech_machine_casing", () -> new BlockItem(IModBlocks.BIOTECH_MACHINE_CASING.get(), new Item.Properties()));
    RegistryObject<Item> ITEM_INPUT_HATCH = ITEMS.register("item_input_hatch", () -> new BlockItem(IModBlocks.ITEM_INPUT_HATCH.get(), new Item.Properties()));
    RegistryObject<Item> ITEM_OUTPUT_HATCH = ITEMS.register("item_output_hatch", () -> new BlockItem(IModBlocks.ITEM_OUTPUT_HATCH.get(), new Item.Properties()));
    RegistryObject<Item> FLUID_INPUT_HATCH = ITEMS.register("fluid_input_hatch", () -> new BlockItem(IModBlocks.FLUID_INPUT_HATCH.get(), new Item.Properties()));
    RegistryObject<Item> FLUID_OUTPUT_HATCH = ITEMS.register("fluid_output_hatch", () -> new BlockItem(IModBlocks.FLUID_OUTPUT_HATCH.get(), new Item.Properties()));
    RegistryObject<Item> ENERGY_INPUT_HATCH = ITEMS.register("energy_input_hatch", () -> new BlockItem(IModBlocks.ENERGY_INPUT_HATCH.get(), new Item.Properties()));
    RegistryObject<Item> COW = ITEMS.register("cow", () -> new MobItem(1));
    RegistryObject<Item> BABY_COW = ITEMS.register("baby_cow", () -> new MobItem(2));
    RegistryObject<Item> CHICKEN = ITEMS.register("chicken", () -> new MobItem(3));
    RegistryObject<Item> BABY_CHICKEN = ITEMS.register("baby_chicken", () -> new MobItem(4));
    RegistryObject<Item> PIG = ITEMS.register("pig", () -> new MobItem(5));
    RegistryObject<Item> BABY_PIG = ITEMS.register("baby_pig", () -> new MobItem(6));
    RegistryObject<Item> SHEEP = ITEMS.register("sheep", () -> new MobItem(7));
    RegistryObject<Item> BABY_SHEEP = ITEMS.register("baby_sheep", () -> new MobItem(8));
    RegistryObject<Item> RABBIT = ITEMS.register("rabbit", () -> new MobItem(9));
    RegistryObject<Item> BABY_RABBIT = ITEMS.register("baby_rabbit", () -> new MobItem(10));
    Set<RegistryObject<Item>> ITEM_SET = new LinkedHashSet<>() {{
        add(NET_TRAP_ITEM);
        add(BREEDER_ITEM);
        add(BIOTECH_MACHINE_CASING);
        add(ITEM_INPUT_HATCH);
        add(ITEM_OUTPUT_HATCH);
        add(FLUID_INPUT_HATCH);
        add(FLUID_OUTPUT_HATCH);
        add(ENERGY_INPUT_HATCH);
        add(COW);
        add(BABY_COW);
        add(CHICKEN);
        add(BABY_CHICKEN);
        add(PIG);
        add(BABY_PIG);
        add(SHEEP);
        add(BABY_SHEEP);
        add(RABBIT);
        add(BABY_RABBIT);
    }};
}
