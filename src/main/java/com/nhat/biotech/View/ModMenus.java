package com.nhat.biotech.View;

import com.nhat.biotech.Biotech;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public interface ModMenus {
    DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Biotech.MODID);
    RegistryObject<MenuType<BreederMenu>> BREEDER = MENUS.register("breeder", () -> IForgeMenuType.create(BreederMenu::new));
}
