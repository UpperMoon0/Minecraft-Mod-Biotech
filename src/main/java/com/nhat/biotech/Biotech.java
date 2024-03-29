package com.nhat.biotech;

import com.mojang.logging.LogUtils;
import com.nhat.biotech.blocks.ModBlocks;
import com.nhat.biotech.blocks.block_entites.ModBlockEntities;
import com.nhat.biotech.creative_tabs.ModCreativeTabs;
import com.nhat.biotech.items.ModItems;
import com.nhat.biotech.networking.ModPackets;
import com.nhat.biotech.recipes.ModRecipes;
import com.nhat.biotech.utils.ScreenInit;
import com.nhat.biotech.view.ModMenus;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Biotech.MODID)
public class Biotech
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "biotech";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public Biotech()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        ModBlocks.BLOCKS.register(modEventBus);

        // Register the Deferred Register to the mod event bus so block entities get registered
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);

        // Register the Deferred Register to the mod event bus so items get registered
        ModItems.ITEMS.register(modEventBus);

        // Register the Deferred Register to the mod event bus so tabs get registered
        ModCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);

        // Register the Deferred Register to the mod event bus so menus get registered
        ModMenus.MENUS.register(modEventBus);

        // Register the Deferred Register to the mod event bus so recipes get registered
        ModRecipes.SERIALIZERS.register(modEventBus);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        ModPackets.register();
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            ScreenInit.init();
        }
    }
}
