package com.ghostipedia.cosmiccore.api.item;

import com.gregtechceu.gtceu.api.item.ComponentItem;
import com.gregtechceu.gtceu.api.item.component.IItemComponent;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;

import com.ghostipedia.cosmiccore.api.item.component.*;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.*;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CosmicComponentItem extends ComponentItem implements ICurioItem {

    public CosmicComponentItem(Properties properties) {
        super(properties);
    }

    // region new methods

    @Override
    public boolean canBeHurtBy(DamageSource damageSource) {
        for (IItemComponent component : components) {
            if (component instanceof IItemDestroyOverride canBeHurtBy) {
                return canBeHurtBy.canBeHurtBy(damageSource);
            }
        }
        return super.canBeHurtBy(damageSource);
    }

    @Override
    public int getEntityLifespan(ItemStack stack, Level level) {
        for (IItemComponent component : components) {
            if (component instanceof ICustomItemEntity customItemEntity) {
                return customItemEntity.getEntityLifetime(stack, level);
            }
        }
        return super.getEntityLifespan(stack, level);
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        for (IItemComponent component : components) {
            if (component instanceof ICustomItemEntity customItemEntity) {
                return customItemEntity.hasCustomEntity(stack);
            }
        }
        return super.hasCustomEntity(stack);
    }

    @Override
    public @Nullable Entity createEntity(Level level, Entity original, ItemStack stack) {
        if (!(original instanceof ItemEntity itemEntity)) {
            // this should never happen, but better safe than sorry
            return null;
        }
        for (IItemComponent component : components) {
            if (component instanceof ICustomItemEntity customItemEntity) {
                return customItemEntity.createEntity(level, itemEntity, stack);
            }
        }
        return super.createEntity(level, original, stack);
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack stack, Player player) {
        for (IItemComponent component : components) {
            if (component instanceof ICanDropOverride dropOverride) {
                return dropOverride.canBeDropped(stack, player);
            }
        }
        return false;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        for (IItemComponent component : components) {
            if (component instanceof IFoilOverride foilOverride) {
                return foilOverride.isFoil(stack);
            }
        }
        return super.isFoil(stack);
    }

    @Override
    public int getDefaultTooltipHideFlags(@NotNull ItemStack stack) {
        int flags = super.getDefaultTooltipHideFlags(stack);
        for (IItemComponent component : components) {
            if (component instanceof IExtendedTooltip tooltip) {
                flags |= tooltip.getDefaultTooltipHideFlags(stack);
            }
        }
        return flags;
    }

    @Override
    public boolean isEnderMask(ItemStack stack, Player player, EnderMan enderMan) {
        for (IItemComponent component : components) {
            if (component instanceof IDangerRemovingItem dangerRemovingItem) {
                return dangerRemovingItem.isEnderMask(stack, player, enderMan);
            }
        }
        return super.isEnderMask(stack, player, enderMan);
    }

    @Override
    public boolean canWalkOnPowderedSnow(ItemStack stack, LivingEntity wearer) {
        for (IItemComponent component : components) {
            if (component instanceof IDangerRemovingItem dangerRemovingItem) {
                return dangerRemovingItem.canWalkOnPowderedSnow(stack, wearer);
            }
        }
        return super.canWalkOnPowderedSnow(stack, wearer);
    }

    @Override
    public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
        for (IItemComponent component : components) {
            if (component instanceof IDangerRemovingItem dangerRemovingItem) {
                return dangerRemovingItem.makesPiglinsNeutral(stack, wearer);
            }
        }
        return super.makesPiglinsNeutral(stack, wearer);
    }

    // endregion
    // -----------------------
    // region curios overrides

    @Override
    public boolean hasCurioCapability(ItemStack stack) {
        for (IItemComponent component : components) {
            if (component instanceof ICurioItem curioItem) {
                if (curioItem.hasCurioCapability(stack)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        for (IItemComponent component : components) {
            if (component instanceof ICurioItem curioItem) {
                curioItem.curioTick(slotContext, stack);
            }
        }
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        for (IItemComponent component : components) {
            if (component instanceof ICurioItem curioItem) {
                curioItem.onEquip(slotContext, prevStack, stack);
            }
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        for (IItemComponent component : components) {
            if (component instanceof ICurioItem curioItem) {
                curioItem.onUnequip(slotContext, newStack, stack);
            }
        }
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        for (IItemComponent component : components) {
            if (component instanceof ICurioItem curioItem) {
                if (curioItem.canEquip(slotContext, stack)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean canUnequip(SlotContext slotContext, ItemStack stack) {
        for (IItemComponent component : components) {
            if (component instanceof ICurioItem curioItem) {
                if (curioItem.canUnequip(slotContext, stack)) {
                    return true;
                }
            }
        }
        return true;
    }

    @Override
    public List<Component> getSlotsTooltip(List<Component> tooltips, ItemStack stack) {
        for (IItemComponent component : components) {
            if (component instanceof ICurioItem curioItem) {
                tooltips = curioItem.getSlotsTooltip(tooltips, stack);
            }
        }
        return tooltips;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();
        for (IItemComponent component : components) {
            if (component instanceof ICurioItem curioItem) {
                modifiers.putAll(curioItem.getAttributeModifiers(slotContext, uuid, stack));
            }
        }
        return modifiers;
    }

    @Override
    public void onEquipFromUse(SlotContext slotContext, ItemStack stack) {
        for (IItemComponent component : components) {
            if (component instanceof ICurioItem curioItem) {
                curioItem.onEquipFromUse(slotContext, stack);
            }
        }
    }

    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        for (IItemComponent component : components) {
            if (component instanceof ICurioItem curioItem) {
                return curioItem.getEquipSound(slotContext, stack);
            }
        }
        return ICurioItem.super.getEquipSound(slotContext, stack);
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        for (IItemComponent component : components) {
            if (component instanceof ICurioItem curioItem) {
                if (curioItem.canEquipFromUse(slotContext, stack)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void curioBreak(SlotContext slotContext, ItemStack stack) {
        for (IItemComponent component : components) {
            if (component instanceof ICurioItem curioItem) {
                curioItem.curioBreak(slotContext, stack);
            }
        }
    }

    @Override
    public boolean canSync(SlotContext slotContext, ItemStack stack) {
        for (IItemComponent component : components) {
            if (component instanceof ICurioItem curioItem) {
                if (curioItem.canSync(slotContext, stack)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public @NotNull CompoundTag writeSyncData(SlotContext slotContext, ItemStack stack) {
        CompoundTag mainTag = new CompoundTag();
        int ordinal = 0;

        for (IItemComponent component : components) {
            if (component instanceof ICurioItem curioItem) {
                mainTag.put(Integer.toString(ordinal), curioItem.writeSyncData(slotContext, stack));
                ordinal++;
            }
        }
        return mainTag;
    }

    @Override
    public void readSyncData(SlotContext slotContext, CompoundTag mainTag, ItemStack stack) {
        int ordinal = 0;

        for (IItemComponent component : components) {
            if (component instanceof ICurioItem curioItem) {
                CompoundTag subTag = mainTag.getCompound(Integer.toString(ordinal));
                curioItem.readSyncData(slotContext, subTag, stack);

                ordinal++;
            }
        }
    }

    @Override
    public @NotNull ICurio.DropRule getDropRule(SlotContext slotContext, DamageSource source, int lootingLevel,
                                                boolean recentlyHit, ItemStack stack) {
        for (IItemComponent component : components) {
            if (component instanceof ICurioItem curioItem) {
                return curioItem.getDropRule(slotContext, source, lootingLevel, recentlyHit, stack);
            }
        }
        return ICurio.DropRule.DEFAULT;
    }

    @Override
    public List<Component> getAttributesTooltip(List<Component> tooltips, ItemStack stack) {
        for (IItemComponent component : components) {
            if (component instanceof ICurioItem curioItem) {
                tooltips = curioItem.getAttributesTooltip(tooltips, stack);
            }
        }
        return tooltips;
    }

    @Override
    public int getFortuneLevel(SlotContext slotContext, LootContext lootContext, ItemStack stack) {
        int maxLevel = 0;
        for (IItemComponent component : components) {
            if (component instanceof ICurioItem curioItem) {
                maxLevel = Math.max(maxLevel, curioItem.getFortuneLevel(slotContext, lootContext, stack));;
            }
        }
        return maxLevel;
    }

    @Override
    public int getLootingLevel(SlotContext slotContext, DamageSource source, LivingEntity target,
                                int baseLooting, ItemStack stack) {
        int maxLevel = 0;
        for (IItemComponent component : components) {
            if (component instanceof ICurioItem curioItem) {
                maxLevel = Math.max(maxLevel, curioItem.getLootingLevel(slotContext, source, target, baseLooting, stack));
            }
        }
        return maxLevel;
    }

    @Override
    public boolean makesPiglinsNeutral(SlotContext slotContext, ItemStack stack) {
        for (IItemComponent component : components) {
            if (component instanceof ICurioItem curioItem) {
                if (curioItem.makesPiglinsNeutral(slotContext, stack)) {
                    return true;
                }
            }
        }
        return this.makesPiglinsNeutral(stack, slotContext.entity());
    }

    @Override
    public boolean canWalkOnPowderedSnow(SlotContext slotContext, ItemStack stack) {
        for (IItemComponent component : components) {
            if (component instanceof ICurioItem curioItem) {
                if (curioItem.canWalkOnPowderedSnow(slotContext, stack)) {
                    return true;
                }
            }
        }
        return this.canWalkOnPowderedSnow(stack, slotContext.entity());
    }

    @Override
    public boolean isEnderMask(SlotContext slotContext, EnderMan enderMan, ItemStack stack) {
        for (IItemComponent component : components) {
            if (component instanceof ICurioItem curioItem) {
                if (curioItem.isEnderMask(slotContext, enderMan, stack)) {
                    return true;
                }
            }
        }

        if (slotContext.entity() instanceof Player player) {
            return this.isEnderMask(stack, player, enderMan);
        } else {
            return false;
        }
    }

    // endregion
    // -----------------------
}
