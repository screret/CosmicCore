package com.ghostipedia.cosmiccore.common.item.behavior;

import com.gregtechceu.gtceu.api.item.component.IItemLifeCycle;
import com.gregtechceu.gtceu.data.lang.LangHandler;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import com.ghostipedia.cosmiccore.api.item.component.*;
import com.ghostipedia.cosmiccore.common.data.CosmicEntities;
import com.ghostipedia.cosmiccore.mixin.accessor.ItemEntityAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.ArrayList;
import java.util.List;

public class OneRingBehavior implements ICurioComponent, IItemLifeCycle, IDangerRemovingItem,
        ICustomItemEntity, IExtendedTooltip {
    
    private final List<MobEffectInstance> effects = new ArrayList<>();
    
    public OneRingBehavior() {
        this.effects.add(new MobEffectInstance(MobEffects.INVISIBILITY, 10));
        this.effects.add(new MobEffectInstance(MobEffects.UNLUCK, 10, 4));
        this.effects.add(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 10, 1));
    }

    public void tick(ItemStack stack, Level level, Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity)) {
            return;
        }
        // forcefully give the ring's effects.
        for (var effect : this.effects) {
            livingEntity.addEffect(new MobEffectInstance(effect));
        }
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        tick(stack, slotContext.entity().level(), slotContext.entity());
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        tick(stack, level, entity);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip,
                                TooltipFlag isAdvanced) {
        tooltip.add(LangHandler.getFromMultiLang("item.cosmiccore.the_one_ring.tooltip", 0)
                .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
        tooltip.add(LangHandler.getFromMultiLang("item.cosmiccore.the_one_ring.tooltip", 1)
                .withStyle(ChatFormatting.DARK_RED));
    }

    @Override
    public List<Component> getAttributesTooltip(List<Component> tooltips, ItemStack stack) {
        return new ArrayList<>();
    }

    @Override
    public int getDefaultTooltipHideFlags(@NotNull ItemStack stack) {
        return ItemStack.TooltipPart.ENCHANTMENTS.getMask();
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    public boolean canUnequip(SlotContext slotContext, ItemStack stack) {
        return false;
    }

    @Override
    public @NotNull ICurio.DropRule getDropRule(SlotContext slotContext, DamageSource source,
                                                int lootingLevel, boolean recentlyHit, ItemStack stack) {
        return ICurio.DropRule.ALWAYS_KEEP;
    }

    @Override
    public boolean isEnderMask(ItemStack stack, Player player, EnderMan enderMan) {
        return true;
    }

    @Override
    public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
        return true;
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Override
    public @Nullable Entity createEntity(Level level, ItemEntity original, ItemStack stack) {
        return RingItemEntity.fromDroppedItem(level, original, stack);
    }

    public static class RingItemEntity extends ItemEntity {

        public RingItemEntity(EntityType<RingItemEntity> entityType, Level level) {
            super(entityType, level);
        }

        @Override
        public boolean fireImmune() {
            return false;
        }

        @Override
        public boolean isOnFire() {
            // disable the fire particle
            return false;
        }

        @Override
        public boolean isInvulnerableTo(@NotNull DamageSource source) {
            return source.is(DamageTypeTags.IS_EXPLOSION) || super.isInvulnerableTo(source);
        }

        public static RingItemEntity fromDroppedItem(Level level, ItemEntity original, ItemStack stack) {
            // copy the original item entity's info over
            RingItemEntity newEntity = CosmicEntities.RING_ITEM.create(level);
            newEntity.setPos(original.position());
            newEntity.setDeltaMovement(original.getDeltaMovement());
            newEntity.setItem(stack);
            newEntity.setThrower(((ItemEntityAccessor) original).getThrower());
            newEntity.setTarget(((ItemEntityAccessor) original).getTarget());
            newEntity.setPickUpDelay(((ItemEntityAccessor) original).getPickupDelay());

            // also make it never despawn
            newEntity.setUnlimitedLifetime();

            return newEntity;
        }
    }
}
