package com.nhat.biotech.view;

import com.nhat.biotech.Biotech;
import com.nhat.biotech.view.io_hatches.energy.EnergyInputHatchMenu;
import com.nhat.biotech.view.io_hatches.fluid.FluidInputHatchMenu;
import com.nhat.biotech.view.io_hatches.fluid.FluidOutputHatchMenu;
import com.nhat.biotech.view.io_hatches.item.ItemInputHatchMenu;
import com.nhat.biotech.view.io_hatches.item.ItemOutputHatchMenu;
import com.nhat.biotech.view.machines.BreederMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public interface IModMenus {
    DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Biotech.MODID);
    RegistryObject<MenuType<BreederMenu>> BREEDER = MENUS.register("breeder", () -> IForgeMenuType.create(BreederMenu::new));
    RegistryObject<MenuType<ItemInputHatchMenu>> ITEM_INPUT_HATCH = MENUS.register("item_input_hatch", () -> IForgeMenuType.create(ItemInputHatchMenu::new));
    RegistryObject<MenuType<ItemOutputHatchMenu>> ITEM_OUTPUT_HATCH = MENUS.register("item_output_hatch", () -> IForgeMenuType.create(ItemOutputHatchMenu::new));
    RegistryObject<MenuType<FluidInputHatchMenu>> FLUID_INPUT_HATCH = MENUS.register("fluid_input_hatch", () -> IForgeMenuType.create(FluidInputHatchMenu::new));
    RegistryObject<MenuType<FluidOutputHatchMenu>> FLUID_OUTPUT_HATCH = MENUS.register("fluid_output_hatch", () -> IForgeMenuType.create(FluidOutputHatchMenu::new));
    RegistryObject<MenuType<EnergyInputHatchMenu>> ENERGY_INPUT_HATCH = MENUS.register("energy_input_hatch", () -> IForgeMenuType.create(EnergyInputHatchMenu::new));
}
