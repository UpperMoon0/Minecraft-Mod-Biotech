package com.nhat.biotech.networking;

import com.nhat.biotech.blocks.block_entites.FluidHatchBlockEntity;
import com.nhat.biotech.view.io_hatches.fluid.FluidHatchMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class FluidPacket {
    private final FluidStack fluidStack;
    private final BlockPos pos;

    public FluidPacket(FluidStack fluidStack, BlockPos pos) {
        this.fluidStack = fluidStack;
        this.pos = pos;
    }

    public FluidPacket(FriendlyByteBuf buf) {
        this.fluidStack = buf.readFluidStack();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeFluidStack(fluidStack);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof FluidHatchBlockEntity blockEntity) {
                blockEntity.setFluid(fluidStack);

                if(Minecraft.getInstance().player.containerMenu instanceof FluidHatchMenu menu &&
                        menu.getBlockEntity().getBlockPos().equals(pos)) {
                    menu.setFluidStack(fluidStack);
                }
            }
        });
        return true;
    }
}
