package com.nhat.biotech.networking;

import com.nhat.biotech.blocks.block_entites.machines.BreedingChamberBlockEntity;
import com.nhat.biotech.recipes.RecipeContainer;
import com.nhat.biotech.view.machines.BreedingChamberMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BreedingChamberPacket extends MachinePacket {
    private final int energyCapacity;
    private final int energyStored;
    private final int energyConsumeRate;
    private final int consumedEnergy;
    private final int recipeEnergyCost;
    private final int fluidCapacity;
    private final FluidStack fluidStored;
    private final boolean isStructureValid;
    private final BlockPos pos;
    private final RecipeContainer recipeContainer;
    public BreedingChamberPacket(int energyCapacity, int energyStored, int energyConsumeRate, int consumedEnergy, int recipeEnergyCost, int fluidCapacity, FluidStack fluidStored, boolean isStructureValid, BlockPos pos, RecipeContainer recipeEntity) {
        this.energyCapacity = energyCapacity;
        this.energyStored = energyStored;
        this.energyConsumeRate = energyConsumeRate;
        this.consumedEnergy = consumedEnergy;
        this.recipeEnergyCost = recipeEnergyCost;
        this.fluidCapacity = fluidCapacity;
        this.fluidStored = fluidStored;
        this.isStructureValid = isStructureValid;
        this.pos = pos;
        this.recipeContainer = recipeEntity;
    }
    public BreedingChamberPacket(FriendlyByteBuf buf) {
        this.energyCapacity = buf.readInt();
        this.energyStored = buf.readInt();
        this.energyConsumeRate = buf.readInt();
        this.consumedEnergy = buf.readInt();
        this.recipeEnergyCost = buf.readInt();
        this.fluidCapacity = buf.readInt();
        this.fluidStored = buf.readFluidStack();
        this.isStructureValid = buf.readBoolean();
        this.pos = buf.readBlockPos();
        boolean hasRecipe = buf.readBoolean();
        if(hasRecipe) {
            this.recipeContainer = RecipeContainer.fromBuf(buf);
        } else {
            this.recipeContainer = null;
        }
    }
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(energyCapacity);
        buf.writeInt(energyStored);
        buf.writeInt(energyConsumeRate);
        buf.writeInt(consumedEnergy);
        buf.writeInt(recipeEnergyCost);
        buf.writeInt(fluidCapacity);
        buf.writeFluidStack(fluidStored);
        buf.writeBoolean(isStructureValid);
        buf.writeBlockPos(pos);
        boolean hasRecipe = recipeContainer != null;
        buf.writeBoolean(hasRecipe);
        if(hasRecipe) {
            recipeContainer.writeToBuf(buf);
        }
    }
    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level != null && Minecraft.getInstance().level.getBlockEntity(pos) instanceof BreedingChamberBlockEntity) {
                if(Minecraft.getInstance().player.containerMenu instanceof BreedingChamberMenu menu && menu.getBlockEntity().getBlockPos().equals(pos)) {
                    menu.setEnergyCapacity(energyCapacity);
                    menu.setEnergyStored(energyStored);
                    menu.setEnergyConsumeRate(energyConsumeRate);
                    menu.setEnergyConsumed(consumedEnergy);
                    menu.setRecipeEnergyCost(recipeEnergyCost);
                    menu.setFluidCapacity(fluidCapacity);
                    menu.setFluidStored(fluidStored);
                    menu.setStructureValid(isStructureValid);
                    menu.setRecipeEntity(recipeContainer);
                }
            }
        });
    }
}
