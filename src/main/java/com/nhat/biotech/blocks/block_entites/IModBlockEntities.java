package com.nhat.biotech.blocks.block_entites;

import com.nhat.biotech.Biotech;
import com.nhat.biotech.blocks.IModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public interface IModBlockEntities {
    DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Biotech.MODID);
    RegistryObject<BlockEntityType<BreederBlockEntity>> BREEDER = BLOCK_ENTITIES.register("breeder", () -> BlockEntityType.Builder.of(BreederBlockEntity::new, IModBlocks.BREEDER.get()).build(null));
    RegistryObject<BlockEntityType<ItemInputHatchBlockEntity>> ITEM_INPUT_HATCH = BLOCK_ENTITIES.register("item_input_hatch", () -> BlockEntityType.Builder.of(ItemInputHatchBlockEntity::new, IModBlocks.ITEM_INPUT_HATCH.get()).build(null));
    RegistryObject<BlockEntityType<ItemOutputHatchBlockEntity>> ITEM_OUTPUT_HATCH = BLOCK_ENTITIES.register("item_output_hatch", () -> BlockEntityType.Builder.of(ItemOutputHatchBlockEntity::new, IModBlocks.ITEM_OUTPUT_HATCH.get()).build(null));
    RegistryObject<BlockEntityType<FluidInputHatchBlockEntity>> FLUID_INPUT_HATCH = BLOCK_ENTITIES.register("fluid_input_hatch", () -> BlockEntityType.Builder.of(FluidInputHatchBlockEntity::new, IModBlocks.FLUID_INPUT_HATCH.get()).build(null));
    RegistryObject<BlockEntityType<FluidOutputHatchBlockEntity>> FLUID_OUTPUT_HATCH = BLOCK_ENTITIES.register("fluid_output_hatch", () -> BlockEntityType.Builder.of(FluidOutputHatchBlockEntity::new, IModBlocks.FLUID_OUTPUT_HATCH.get()).build(null));
    RegistryObject<BlockEntityType<EnergyInputHatchBlockEntity>> ENERGY_INPUT_HATCH = BLOCK_ENTITIES.register("energy_input_hatch", () -> BlockEntityType.Builder.of(EnergyInputHatchBlockEntity::new, IModBlocks.ENERGY_INPUT_HATCH.get()).build(null));
}
