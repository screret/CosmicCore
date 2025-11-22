package com.ghostipedia.cosmiccore.common.data;

import forestry.api.core.IItemSubtype;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import thedarkcolour.gendustry.item.IGendustryUpgradeType;

import java.util.Locale;

@Accessors(fluent = true)
public enum CosmicGendustryUpgradeType implements IItemSubtype, IGendustryUpgradeType {

    WAILING(1, 2048),
    DECAYING(1, 2048);

    private final String name;
    @Getter
    private final int maxStackSize;
    @Getter
    private final int energyCost;

    CosmicGendustryUpgradeType(int maxStackSize, int energyCost) {
        this.name = this.name().toLowerCase(Locale.ROOT);
        this.maxStackSize = maxStackSize;
        this.energyCost = energyCost;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }
}
