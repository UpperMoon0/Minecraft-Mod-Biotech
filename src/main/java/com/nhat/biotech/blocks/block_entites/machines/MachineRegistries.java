package com.nhat.biotech.blocks.block_entites.machines;

import com.nhat.biotech.Biotech;
import com.nhat.biotech.blocks.MachineBlock;
import com.nhat.biotech.utils.Machines;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MachineRegistries {
    private static final DeferredRegister<Block> BLOCK_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, Biotech.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPE_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Biotech.MODID);
    public static final DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, Biotech.MODID);

    public static MachineRegistry<BreedingChamberBlockEntity> BREEDING_CHAMBER = register(Machines.BREEDING_CHAMBER_ID, BreedingChamberBlockEntity.class);
    public static MachineRegistry<TerrestrialHabitatBlockEntity> TERRESTRIAL_HABITAT = register(Machines.TERRESTRIAL_HABITAT_ID, TerrestrialHabitatBlockEntity.class);

    public static <T extends MachineBlockEntity> MachineRegistry<T> register(String id, Class<T> blockEntityClass) {
        BlockEntityType.BlockEntitySupplier<T> blockEntitySupplier = (pos, state) -> {
            try {
                return blockEntityClass.getConstructor(BlockPos.class, BlockState.class).newInstance(pos, state);
            } catch (Exception e) {
                throw new RuntimeException("Failed to create block entity supplier for " + blockEntityClass.getName(), e);
            }
        };

        RegistryObject<Block> block = BLOCK_REGISTER.register(id, () -> new MachineBlock(blockEntityClass));
        RegistryObject<BlockEntityType<T>> blockEntity = BLOCK_ENTITY_TYPE_REGISTER.register(id, () -> BlockEntityType.Builder.of(blockEntitySupplier, block.get()).build(null));
        RegistryObject<Item> blockItem = ITEM_REGISTER.register(id, () -> new BlockItem(block.get(), new Item.Properties()));

        return new MachineRegistry<>(block, blockEntity, blockItem);
    }

    public static void register(IEventBus modEventBus) {
        BLOCK_REGISTER.register(modEventBus);
        BLOCK_ENTITY_TYPE_REGISTER.register(modEventBus);
        ITEM_REGISTER.register(modEventBus);
    }

    public record MachineRegistry<T extends MachineBlockEntity>(RegistryObject<Block> block,
                                                                RegistryObject<BlockEntityType<T>> blockEntity,
                                                                RegistryObject<Item> blockItem) {
    }
}