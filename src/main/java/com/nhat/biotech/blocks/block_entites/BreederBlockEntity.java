package com.nhat.biotech.blocks.block_entites;

import com.nhat.biotech.blocks.BreederBlock;
import com.nhat.biotech.utils.BiotechRecipe;
import com.nhat.biotech.utils.BreederRecipes;
import com.nhat.biotech.utils.RecipeHelper;
import com.nhat.biotech.view.machines.BreederMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BreederBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler slots = new ItemStackHandler(3);
    private final LazyOptional<IItemHandler> lazySlots = LazyOptional.of(() -> slots);
    private final ContainerData data;
    private int usedEnergy;
    private int maxUsedEnergy;
    public BreederBlockEntity(BlockPos pos, BlockState state) {
        super(IModBlockEntities.BREEDER.get(), pos, state);
        data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> usedEnergy;
                    case 1 -> maxUsedEnergy;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> usedEnergy = pValue;
                    case 1 -> maxUsedEnergy = pValue;
                    default -> {}
                };
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }
    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("menu.title.biotech.breeder");
    }
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new BreederMenu(pContainerId, pPlayerInventory, this, data);
    }
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction facing) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazySlots.cast();
        }
        return super.getCapability(cap, facing);
    }
    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        slots.deserializeNBT(pTag.getCompound("inventory"));
        usedEnergy = pTag.getInt("usedEnergy");
    }
    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("inventory", slots.serializeNBT());
        pTag.putInt("usedEnergy", usedEnergy);
    }
    private void operate() {
        int outputStartIndex = 2;
        BiotechRecipe recipe = RecipeHelper.hasRecipe(BreederRecipes.getRecipeList(), outputStartIndex, this);
        if (recipe != null) {
            maxUsedEnergy = recipe.getTotalEnergy();
            if (usedEnergy < maxUsedEnergy) {
                usedEnergy += 200;
            } else {
                RecipeHelper.craft(recipe, outputStartIndex, this);
                usedEnergy = 0;
            }
        } else {
            usedEnergy = 0;
            maxUsedEnergy = 0;
        }
    }
    public void dropItem() {
        SimpleContainer inventory = new SimpleContainer(slots.getSlots());
        for (int i = 0; i < slots.getSlots(); i++) {
            inventory.setItem(i, slots.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static <T extends BlockEntity> void serverTick(Level level, BlockPos blockPos, BlockState blockState, T t) {
        BreederBlockEntity blockEntity = (BreederBlockEntity) t;
        if (!level.isClientSide) {
            blockEntity.operate();
            if (blockEntity.usedEnergy > 0) {
                blockState = blockState.setValue(BreederBlock.OPERATING, Boolean.valueOf(true));
                level.setBlock(blockPos, blockState, 3);
            } else {
                blockState = blockState.setValue(BreederBlock.OPERATING, Boolean.valueOf(false));
                level.setBlock(blockPos, blockState, 3);
            }
            setChanged(level, blockPos, blockState);
        }
    }
}
