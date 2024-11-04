package com.nhat.biotech.items;

import com.nhat.biotech.view.DebugStructureAnalyzerScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class DebugStructureAnalyzer extends Item {

    public DebugStructureAnalyzer(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (level.isClientSide) {
            Minecraft.getInstance().setScreen(new DebugStructureAnalyzerScreen(level));
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }
}