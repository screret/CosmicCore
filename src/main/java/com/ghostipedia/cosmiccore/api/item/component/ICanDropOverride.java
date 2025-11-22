package com.ghostipedia.cosmiccore.api.item.component;

import com.gregtechceu.gtceu.api.item.component.IItemComponent;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface ICanDropOverride extends IItemComponent {

    ICanDropOverride NEVER = (stack, player) -> false;

    /**
     * Pass-through utility function to avoid having to cast a lambda to {@link ICanDropOverride}
     * @param function the drop override component.
     * @return {@code function}
     */
    static ICanDropOverride of(ICanDropOverride function) {
        return function;
    }

    /**
     * Called when an entity drops the item into the world.
     * Returning {@code false} from this will prevent the item from being removed from the player's inventory and
     * spawned into the world.
     *
     * @param entity The entity that dropped the item
     * @param stack  The item stack that's being dropped
     * @return {@code true} if the item is allowed to be dropped
     * @see net.minecraftforge.common.extensions.IForgeItem#onDroppedByPlayer(ItemStack, Player)
     */
    boolean canBeDropped(ItemStack stack, LivingEntity entity);
}
