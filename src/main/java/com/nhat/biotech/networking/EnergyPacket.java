package com.nhat.biotech.networking;

import com.nhat.biotech.blocks.block_entites.EnergyHatchBlockEntity;
import com.nhat.biotech.view.io_hatches.energy.EnergyHatchMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class EnergyPacket {
    private final int energy;
    private final BlockPos pos;

    public EnergyPacket(int energy, BlockPos pos) {
        this.energy = energy;
        this.pos = pos;
    }

    public EnergyPacket(FriendlyByteBuf buf) {
        this.energy = buf.readInt();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(energy);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof EnergyHatchBlockEntity blockEntity) {
                blockEntity.setEnergy(energy);

                if(Minecraft.getInstance().player.containerMenu instanceof EnergyHatchMenu menu &&
                        menu.getBlockEntity().getBlockPos().equals(pos)) {
                    menu.setEnergy(energy);
                }
            }
        });
        return true;
    }
}
