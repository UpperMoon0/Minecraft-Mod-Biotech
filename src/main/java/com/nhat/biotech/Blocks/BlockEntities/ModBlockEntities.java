package com.nhat.biotech.Blocks.BlockEntities;

import com.nhat.biotech.Biotech;
import com.nhat.biotech.Blocks.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Biotech.MODID);
    public static final RegistryObject<BlockEntityType<NetTrapBlockEntity>> NET_TRAP =
            BLOCK_ENTITIES.register("net_trap", () ->
                    BlockEntityType.Builder.of(NetTrapBlockEntity::new,
                            ModBlocks.NET_TRAP.get()).build(null));
}
