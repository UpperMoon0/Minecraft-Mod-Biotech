package com.nhat.biotech.blocks;

import com.nhat.biotech.Biotech;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public interface IModBlocks {
    DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Biotech.MODID);
    RegistryObject<Block> NET_TRAP = BLOCKS.register("net_trap", NetTrapBlock::new);
    RegistryObject<Block> BREEDER = BLOCKS.register("breeder", BreederBlock::new);
    RegistryObject<Block> BIOTECH_MACHINE_CASING = BLOCKS.register("biotech_machine_casing", () -> new Block(BlockBehaviour.Properties.copy(Blocks.GRAY_CONCRETE).strength(2f).sound(SoundType.METAL)));
    RegistryObject<Block> ITEM_INPUT_HATCH = BLOCKS.register("item_input_hatch", () -> new IOHatchBlock(0));
    RegistryObject<Block> ITEM_OUTPUT_HATCH = BLOCKS.register("item_output_hatch", () -> new IOHatchBlock(1));
    RegistryObject<Block> FLUID_INPUT_HATCH = BLOCKS.register("fluid_input_hatch", () -> new IOHatchBlock(2));
    RegistryObject<Block> FLUID_OUTPUT_HATCH = BLOCKS.register("fluid_output_hatch", () -> new IOHatchBlock(3));
    RegistryObject<Block> ENERGY_INPUT_HATCH = BLOCKS.register("energy_input_hatch", () -> new IOHatchBlock(4));
}
