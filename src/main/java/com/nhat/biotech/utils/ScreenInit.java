package com.nhat.biotech.utils;

import com.nhat.biotech.view.*;
import com.nhat.biotech.view.io_hatches.energy.EnergyInputHatchScreen;
import com.nhat.biotech.view.io_hatches.fluid.FluidInputHatchScreen;
import com.nhat.biotech.view.io_hatches.fluid.FluidOutputHatchScreen;
import com.nhat.biotech.view.io_hatches.item.ItemInputHatchScreen;
import com.nhat.biotech.view.io_hatches.item.ItemOutputHatchScreen;
import com.nhat.biotech.view.machines.BreedingChamberScreen;
import net.minecraft.client.gui.screens.MenuScreens;

public class ScreenInit {
    public static void init() {
        MenuScreens.register(ModMenus.BREEDER.get(), BreedingChamberScreen::new);
        MenuScreens.register(ModMenus.ITEM_INPUT_HATCH.get(), ItemInputHatchScreen::new);
        MenuScreens.register(ModMenus.ITEM_OUTPUT_HATCH.get(), ItemOutputHatchScreen::new);
        MenuScreens.register(ModMenus.FLUID_INPUT_HATCH.get(), FluidInputHatchScreen::new);
        MenuScreens.register(ModMenus.FLUID_OUTPUT_HATCH.get(), FluidOutputHatchScreen::new);
        MenuScreens.register(ModMenus.ENERGY_INPUT_HATCH.get(), EnergyInputHatchScreen::new);
    }
}
