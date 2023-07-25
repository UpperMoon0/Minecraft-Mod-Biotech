package com.nhat.biotech.CreativeTabs;

import com.nhat.biotech.Biotech;
import com.nhat.biotech.Items.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Biotech.MODID);
    public static final RegistryObject<CreativeModeTab> BIOTECH_TAB = CREATIVE_MODE_TABS.register("biotech", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ModItems.NET_TRAP_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ModItems.NET_TRAP_ITEM.get());
                output.accept(ModItems.COW_ITEM.get());
            }).build());
}
