package com.nhat.biotech.utils;

import com.nhat.biotech.view.*;
import com.nhat.biotech.view.io_hatches.energy.EnergyInputHatchScreen;
import com.nhat.biotech.view.io_hatches.fluid.FluidInputHatchScreen;
import com.nhat.biotech.view.io_hatches.fluid.FluidOutputHatchScreen;
import com.nhat.biotech.view.io_hatches.item.ItemInputHatchScreen;
import com.nhat.biotech.view.io_hatches.item.ItemOutputHatchScreen;
import com.nhat.biotech.view.machines.BreederScreen;
import net.minecraft.client.gui.screens.MenuScreens;

public abstract class ScreenInit {
    public static void init() {
        MenuScreens.register(IModMenus.BREEDER.get(), BreederScreen::new);
        MenuScreens.register(IModMenus.ITEM_INPUT_HATCH.get(), ItemInputHatchScreen::new);
        MenuScreens.register(IModMenus.ITEM_OUTPUT_HATCH.get(), ItemOutputHatchScreen::new);
        MenuScreens.register(IModMenus.FLUID_INPUT_HATCH.get(), FluidInputHatchScreen::new);
        MenuScreens.register(IModMenus.FLUID_OUTPUT_HATCH.get(), FluidOutputHatchScreen::new);
        MenuScreens.register(IModMenus.ENERGY_INPUT_HATCH.get(), EnergyInputHatchScreen::new);
    }
}
