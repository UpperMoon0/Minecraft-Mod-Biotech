package com.nhat.biotech.blocks.block_entites;

import com.nhat.biotech.Biotech;
import com.nhat.biotech.blocks.BiotechBlocks;
import com.nhat.biotech.blocks.block_entites.hatches.*;
import com.nhat.biotech.blocks.block_entites.machines.BreedingChamberBlockEntity;
import com.nhat.biotech.blocks.block_entites.machines.TerrestrialHabitatBlockEntity;
import com.nhat.biotech.utils.Machines;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BiotechBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Biotech.MODID);

    public static final RegistryObject<BlockEntityType<BreedingChamberBlockEntity>> BREEDING_CHAMBER = BLOCK_ENTITIES.register(Machines.BREEDING_CHAMBER_ID, () -> BlockEntityType.Builder.of(BreedingChamberBlockEntity::new, BiotechBlocks.BREEDING_CHAMBER.get()).build(null));
    public static final RegistryObject<BlockEntityType<TerrestrialHabitatBlockEntity>> TERRESTRIAL_HABITAT = BLOCK_ENTITIES.register(Machines.TERRESTRIAL_HABITAT_ID, () -> BlockEntityType.Builder.of(TerrestrialHabitatBlockEntity::new, BiotechBlocks.TERRESTRIAL_HABITAT.get()).build(null));

    public static final RegistryObject<BlockEntityType<ItemInputHatchBlockEntity>> ITEM_INPUT_HATCH = BLOCK_ENTITIES.register("item_input_hatch", () -> BlockEntityType.Builder.of(ItemInputHatchBlockEntity::new, BiotechBlocks.ITEM_INPUT_HATCH.get()).build(null));
    public static final RegistryObject<BlockEntityType<ItemOutputHatchBlockEntity>> ITEM_OUTPUT_HATCH = BLOCK_ENTITIES.register("item_output_hatch", () -> BlockEntityType.Builder.of(ItemOutputHatchBlockEntity::new, BiotechBlocks.ITEM_OUTPUT_HATCH.get()).build(null));
    public static final RegistryObject<BlockEntityType<FluidInputHatchBlockEntity>> FLUID_INPUT_HATCH = BLOCK_ENTITIES.register("fluid_input_hatch", () -> BlockEntityType.Builder.of(FluidInputHatchBlockEntity::new, BiotechBlocks.FLUID_INPUT_HATCH.get()).build(null));
    public static final RegistryObject<BlockEntityType<FluidOutputHatchBlockEntity>> FLUID_OUTPUT_HATCH = BLOCK_ENTITIES.register("fluid_output_hatch", () -> BlockEntityType.Builder.of(FluidOutputHatchBlockEntity::new, BiotechBlocks.FLUID_OUTPUT_HATCH.get()).build(null));
    public static final RegistryObject<BlockEntityType<EnergyInputHatchBlockEntity>> ENERGY_INPUT_HATCH = BLOCK_ENTITIES.register("energy_input_hatch", () -> BlockEntityType.Builder.of(EnergyInputHatchBlockEntity::new, BiotechBlocks.ENERGY_INPUT_HATCH.get()).build(null));
}