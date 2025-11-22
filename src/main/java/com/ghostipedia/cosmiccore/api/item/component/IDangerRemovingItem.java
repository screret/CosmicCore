package com.ghostipedia.cosmiccore.api.item.component;

import com.gregtechceu.gtceu.api.item.component.IItemComponent;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.extensions.IForgeItem;

public interface IDangerRemovingItem extends IItemComponent {

    /**
     * Whether this item can be used to make endermen not target a player.
     *
     * @param stack    The item stack
     * @param player   The player looking at the enderman
     * @param enderMan The enderman that the player is looking at
     * @return {@code true} if this item can be used to make endermen not target a player
     * @see IForgeItem#isEnderMask(ItemStack, Player, EnderMan)
     */
    default boolean isEnderMask(ItemStack stack, Player player, EnderMan enderMan) {
        return stack.getItem() == Blocks.CARVED_PUMPKIN.asItem();
    }

    /**
     * Called by the powdered snow block to check if a living entity wearing this can walk on the snow,
     * granting the same behavior as leather boots.
     * <br>
     * Only affects items worn in the boots slot.
     *
     * @param stack  The item stack
     * @param wearer The entity wearing this item
     *
     * @return {@code true} if the entity can walk on powdered snow
     * @see IForgeItem#canWalkOnPowderedSnow(ItemStack, LivingEntity)
     */
    default boolean canWalkOnPowderedSnow(ItemStack stack, LivingEntity wearer) {
        return stack.is(Items.LEATHER_BOOTS);
    }

    /**
     * Called by piglins to check if a given item prevents hostility on sight.<br>
     * If this returns {@code true}, the piglins will be neutral to the entity wearing this item
     * and will not attack on sight.
     * <p>
     * Note: This does not prevent piglins from becoming hostile due to other actions,
     * nor does it make piglins that are already hostile stop being so.
     * </p>
     *
     * @param stack  The item stack
     * @param wearer The entity wearing this item
     * @return {@code true} if piglins are neutral to players wearing this item in an armor slot
     * @see IForgeItem#makesPiglinsNeutral(ItemStack, LivingEntity)
     */
    default boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
        return stack.getItem() instanceof ArmorItem armorItem && armorItem.getMaterial() == ArmorMaterials.GOLD;
    }

}
