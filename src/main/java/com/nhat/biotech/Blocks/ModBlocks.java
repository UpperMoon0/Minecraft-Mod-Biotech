package com.nhat.biotech.Blocks;

import com.nhat.biotech.Biotech;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Biotech.MODID);
    public static final RegistryObject<Block> NET_TRAP = BLOCKS.register("net_trap", NetTrapBlock::new);
    public static final RegistryObject<Block> BREEDER = BLOCKS.register("breeder", BreederBlock::new);
}
