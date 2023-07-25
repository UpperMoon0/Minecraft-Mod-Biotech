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
    public static final RegistryObject<Item> COW_ITEM = ITEMS.register("cow", CowItem::new);
}
