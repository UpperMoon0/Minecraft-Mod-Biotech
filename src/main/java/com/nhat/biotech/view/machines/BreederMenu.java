package com.nhat.biotech.view.machines;

import com.nhat.biotech.blocks.IModBlocks;
import com.nhat.biotech.blocks.block_entites.BreederBlockEntity;
import com.nhat.biotech.utils.IAnimalSets;
import com.nhat.biotech.view.BiotechMenu;
import com.nhat.biotech.view.IModMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BreederMenu extends BiotechMenu {
    private final BreederBlockEntity blockEntity;
    private final Level level;
    private int storeEnergy;
    private int usedEnergy;
    private int maxUsedEnergy;
    private FluidStack storedFluid;
    private boolean isStructureValid;

    public BreederMenu(int pContainerId, Inventory inventory, FriendlyByteBuf friendlyByteBuf) {
        this(pContainerId, inventory, Objects.requireNonNull(inventory.player.level().getBlockEntity(friendlyByteBuf.readBlockPos())));
    }

    public BreederMenu(int i, Inventory inventory, BlockEntity blockEntity) {
        super(IModMenus.BREEDER.get(), i);
        this.blockEntity = (BreederBlockEntity) blockEntity;
        this.level = inventory.player.level();

        blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(h -> {
            this.addSlot(new SlotItemHandler(h, 0, 36, 35) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    Item item = stack.getItem();
                    for (Item adultAnimal : IAnimalSets.ADULT_ANIMAL) {
                        if (item == adultAnimal) {
                            return true;
                        }
                    }
                    return false;
                }
            });
            this.addSlot(new SlotItemHandler(h, 1, 54, 35) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    Item item = stack.getItem();
                    for (Item food : IAnimalSets.FOOD) {
                        if (item == food) {
                            return true;
                        }
                    }
                    return false;
                }
            });
            this.addSlot(new SlotItemHandler(h, 2, 116, 35) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return false;
                }
            });
        });

        addInventorySlots(inventory);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        return adaptiveQuickMoveStack(pIndex, 3, 2);
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, IModBlocks.BREEDER.get());
    }
    public int getEnergyHeight()
    {
        return Math.max(storeEnergy * 52 / 128000, 1);
    }
    public int getProgressWidth() {
        return maxUsedEnergy == 0? 0 : usedEnergy * 24 / maxUsedEnergy;
    }
    public boolean isOperating() {
        return usedEnergy > 0;
    }
    public BreederBlockEntity getBlockEntity() {
        return blockEntity;
    }
    public void setStoredEnergy(int energy) {
        this.storeEnergy = energy;
    }
    public void setUsedEnergy(int usedEnergy) {
        this.usedEnergy = usedEnergy;
    }
    public void setMaxUsedEnergy(int maxUsedEnergy) {
        this.maxUsedEnergy = maxUsedEnergy;
    }
    public void setStoredFluid(FluidStack storedFluid) {
        this.storedFluid = storedFluid;
    }
    public void setStructureValid(boolean structureValid) {
        isStructureValid = structureValid;
    }
    public int getStoredEnergy() {
        return storeEnergy;
    }
    public boolean getStructureValid() {
        return isStructureValid;
    }
    public FluidStack getStoredFluid() {
        return storedFluid;
    }
}