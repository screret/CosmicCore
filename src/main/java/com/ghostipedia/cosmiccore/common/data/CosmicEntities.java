package com.ghostipedia.cosmiccore.common.data;

import net.minecraft.world.entity.MobCategory;

import com.ghostipedia.cosmiccore.common.item.behavior.OneRingBehavior;
import com.tterrag.registrate.util.entry.EntityEntry;

import static com.ghostipedia.cosmiccore.api.registries.CosmicRegistration.REGISTRATE;

public class CosmicEntities {

    public static final EntityEntry<OneRingBehavior.RingItemEntity> RING_ITEM = REGISTRATE
            .entity("the_one_ring", OneRingBehavior.RingItemEntity::new, MobCategory.MISC)
            .properties(p -> p.sized(0.25F, 0.25F).clientTrackingRange(6).updateInterval(20).noSummon())
            .register();

    public static void init() {}
}
