package com.nhat.biotech.Blocks;

import com.nhat.biotech.Biotech;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public interface ModBlocks {
    DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Biotech.MODID);
    RegistryObject<Block> NET_TRAP = BLOCKS.register("net_trap", NetTrapBlock::new);
    RegistryObject<Block> BREEDER = BLOCKS.register("breeder", BreederBlock::new);
}
