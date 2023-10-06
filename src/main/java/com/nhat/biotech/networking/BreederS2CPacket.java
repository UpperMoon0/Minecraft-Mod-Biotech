package com.nhat.biotech.networking;

import com.nhat.biotech.blocks.block_entites.BreederBlockEntity;
import com.nhat.biotech.view.machines.BreederMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BreederS2CPacket {
    private final int storedEnergy;
    private final int usedEnergy;
    private final int maxUsedEnergy;
    private final FluidStack storedFluid;
    private final boolean isStructureValid;
    private final BlockPos pos;
    public BreederS2CPacket(int storedEnergy, int usedEnergy, int maxUsedEnergy, FluidStack storedFluid, boolean isStructureValid, BlockPos pos) {
        this.storedEnergy = storedEnergy;
        this.usedEnergy = usedEnergy;
        this.maxUsedEnergy = maxUsedEnergy;
        this.storedFluid = storedFluid;
        this.isStructureValid = isStructureValid;
        this.pos = pos;
    }
    public BreederS2CPacket(FriendlyByteBuf buf) {
        this.storedEnergy = buf.readInt();
        this.usedEnergy = buf.readInt();
        this.maxUsedEnergy = buf.readInt();
        this.storedFluid = buf.readFluidStack();
        this.isStructureValid = buf.readBoolean();
        this.pos = buf.readBlockPos();
    }
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(storedEnergy);
        buf.writeInt(usedEnergy);
        buf.writeInt(maxUsedEnergy);
        buf.writeFluidStack(storedFluid);
        buf.writeBoolean(isStructureValid);
        buf.writeBlockPos(pos);
    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level != null && Minecraft.getInstance().level.getBlockEntity(pos) instanceof BreederBlockEntity) {
                if(Minecraft.getInstance().player.containerMenu instanceof BreederMenu menu && menu.getBlockEntity().getBlockPos().equals(pos)) {
                    menu.setStoredEnergy(storedEnergy);
                    menu.setUsedEnergy(usedEnergy);
                    menu.setMaxUsedEnergy(maxUsedEnergy);
                    menu.setStoredFluid(storedFluid);
                    menu.setStructureValid(isStructureValid);
                }
            }
        });
        return true;
    }
}
