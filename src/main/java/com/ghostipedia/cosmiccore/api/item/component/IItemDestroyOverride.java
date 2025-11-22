package com.ghostipedia.cosmiccore.api.item.component;

import com.gregtechceu.gtceu.api.item.component.IItemComponent;

import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;

@FunctionalInterface
public interface IItemDestroyOverride extends IItemComponent {

    /**
     * Determines whether a damage source can destroy this item while it's dropped.
     * @param damageSource the damage source trying to destroy this item
     * @return {@code true} if the damage source destroys this item
     */
    boolean canBeHurtBy(DamageSource damageSource);

    static IItemDestroyOverride tag(TagKey<DamageType> tag) {
        return damageSource -> damageSource.is(tag);
    }

    static IItemDestroyOverride type(ResourceKey<DamageType> type) {
        return damageSource -> damageSource.is(type);
    }
}
