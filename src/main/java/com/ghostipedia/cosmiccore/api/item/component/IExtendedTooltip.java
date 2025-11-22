package com.ghostipedia.cosmiccore.api.item.component;

import com.gregtechceu.gtceu.api.item.component.IAddInformation;

import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

public interface IExtendedTooltip extends IAddInformation {

    /**
     * Get the tooltip parts that should be hidden by default on the given stack if the {@code HideFlags} tag isn't set.
     * @param stack the stack
     * @return the default hide flags
     * @see ItemStack.TooltipPart
     * @see net.minecraftforge.common.extensions.IForgeItem#getDefaultTooltipHideFlags(ItemStack) IForgeItem#getDefaultTooltipHideFlags
     */
    default int getDefaultTooltipHideFlags(@NotNull ItemStack stack) {
        return 0;
    }
}
