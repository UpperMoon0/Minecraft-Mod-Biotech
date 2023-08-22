package com.nhat.biotech.View;

import com.nhat.biotech.Biotech;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Biotech.MODID);

    public static final RegistryObject<MenuType<BreederMenu>> BREEDER = MENUS.register("breeder", () -> new MenuType(BreederMenu::new, FeatureFlags.DEFAULT_FLAGS));
}
