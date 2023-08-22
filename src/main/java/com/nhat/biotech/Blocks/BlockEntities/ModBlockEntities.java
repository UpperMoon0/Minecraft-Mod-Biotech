package com.nhat.biotech.Blocks.BlockEntities;

import com.nhat.biotech.Biotech;
import com.nhat.biotech.Blocks.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public interface ModBlockEntities {
    DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Biotech.MODID);
    RegistryObject<BlockEntityType<BreederBlockEntity>> BREEDER = BLOCK_ENTITIES.register("breeder", () -> BlockEntityType.Builder.of(BreederBlockEntity::new, ModBlocks.BREEDER.get()).build(null));
}
