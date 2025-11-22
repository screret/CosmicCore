package com.ghostipedia.cosmiccore.mixin;

import com.ghostipedia.cosmiccore.common.data.CosmicGendustryUpgradeType;

import com.llamalad7.mixinextras.sugar.Local;
import forestry.api.apiculture.IBeeModifier;
import forestry.core.inventory.IInventoryAdapter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thedarkcolour.gendustry.item.IGendustryUpgradeType;

@Mixin(targets = "thedarkcolour.gendustry.blockentity.IndustrialApiaryBeeModifier", remap = false)
public abstract class IndustrialApiaryBeeModifierMixin implements IBeeModifier {

    @Shadow
    float mutation;
    @Shadow
    float lifespan;
    @Shadow
    int throttle;

    @Inject(method = "recalculate",
            at = @At(value = "INVOKE_ASSIGN",
                    target = "Lthedarkcolour/gendustry/item/GendustryUpgradeItem;getType()Lthedarkcolour/gendustry/item/IGendustryUpgradeType;",
                    shift = At.Shift.AFTER))
    private void cosmicCore$injectCCUpgradeTypes(IInventoryAdapter inventory, CallbackInfoReturnable<Integer> cir,
                                                 @Local IGendustryUpgradeType upgradeType) {
        if (upgradeType instanceof CosmicGendustryUpgradeType type) {
            switch (type) {
                case WAILING:
                    this.mutation = 1000f;
                    break;
                case DECAYING:
                    this.lifespan = 1000f;
                    this.throttle = 10000;
            }
        }
    }
}
