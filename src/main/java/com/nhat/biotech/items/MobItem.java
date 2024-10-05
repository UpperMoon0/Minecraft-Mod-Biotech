package com.nhat.biotech.items;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class MobItem extends Item {
    private final int type;
    public MobItem(int type) {
        super(new Item.Properties());
        this.type = type;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        // Get the world, player, and block position from the context
        Level world = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();

        // Check if the code is running on the server side
        if (!world.isClientSide) {
            // Create a new cow entity at the specified position
            Mob mob = null;
            switch (type) {
                case 1: { //Cow
                    mob = new Cow(EntityType.COW, world);
                    break;
                }
                case 2: { //Baby Cow
                    mob = new Cow(EntityType.COW, world);
                    mob.setBaby(true);
                    break;
                }
                case 3: { //Chicken
                    mob = new Chicken(EntityType.CHICKEN, world);
                    break;
                }
                case 4: { //Baby Chicken
                    mob = new Chicken(EntityType.CHICKEN, world);
                    mob.setBaby(true);
                    break;
                }
                case 5: { //Pig
                    mob = new Pig(EntityType.PIG, world);
                    break;
                }
                case 6: { //Baby Pig
                    mob = new Pig(EntityType.PIG, world);
                    mob.setBaby(true);
                    break;
                }
                case 7: { //Sheep
                    mob = new Sheep(EntityType.SHEEP, world);
                    break;
                }
                case 8: { //Baby Sheep
                    mob = new Sheep(EntityType.SHEEP, world);
                    mob.setBaby(true);
                    break;
                }
                case 9: { //Rabbit
                    mob = new Rabbit(EntityType.RABBIT, world);
                    break;
                }
                case 10: { //Baby Rabbit
                    mob = new Rabbit(EntityType.RABBIT, world);
                    mob.setBaby(true);
                    break;
                }
                default: {
                    break;
                }
            }

            if (mob != null && player != null) {
                mob.setPos(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
                world.addFreshEntity(mob);

                // Consume one item from the player's inventory
                if (!player.isCreative()) {
                    context.getItemInHand().shrink(1);
                }
            }
        }

        return InteractionResult.SUCCESS;
    }
}
