package com.ghostipedia.cosmiccore.api.item.component;

import com.gregtechceu.gtceu.api.item.component.IItemComponent;

import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface IFoilOverride extends IItemComponent {

    IFoilOverride ALWAYS = stack -> true;
    IFoilOverride NEVER = stack -> false;

    /**
     * Determines whether an item stack has the enchantment glint effect or not.
     * @param stack The item stack
     * @return {@code true} if the stack has the enchantment glint
     */
    boolean isFoil(ItemStack stack);
}
