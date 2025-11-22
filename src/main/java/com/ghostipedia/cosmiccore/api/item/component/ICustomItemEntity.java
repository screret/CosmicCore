package com.ghostipedia.cosmiccore.api.item.component;

import com.gregtechceu.gtceu.api.item.component.IItemComponent;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeItem;

import org.jetbrains.annotations.Nullable;

public interface ICustomItemEntity extends IItemComponent {

    /**
     * Utility function to create a {@link ModifiedLifetime ModifiedLifetime} component with a constant lifetime.
     * @param lifetime The lifetime this item's dropped versions will have.
     * @return a {@link ICustomItemEntity} component specifying the lifetime that this item will have when dropped
     */
    static ICustomItemEntity modifiedLifetime(int lifetime) {
        return modifiedLifetime((stack, level) -> lifetime);
    }

    /**
     * Utility function to create a {@link ModifiedLifetime ModifiedLifetime} component with a function specifying the lifetime of the entity.
     * @param lifetime A function that returns the lifetime this item's dropped versions will have.
     * @return a {@link ICustomItemEntity} component specifying the lifetime that this item will have when dropped
     */
    static ICustomItemEntity modifiedLifetime(ModifiedLifetime lifetime) {
        return lifetime;
    }

    /**
     * Retrieves the normal 'lifetime' of this item when it is dropped on the ground as an {@link ItemEntity}.
     * This is in ticks. The default result is 6000 ticks (5 minutes).
     *
     * @param stack The item stack
     * @param level The level the entity is in
     * @return The normal lifetime in ticks.
     * @see IForgeItem#getEntityLifespan(ItemStack, Level)
     */
    default int getEntityLifetime(ItemStack stack, Level level) {
        // ItemEntity.LIFETIME
        return 6000;
    }

    /**
     * Determines if this Item has a special entity for when they are in the world.
     * It's called when an {@link ItemEntity} is spawned in the world.
     * If {@code true} and {@link #createEntity} returns a non-null value,
     * the {@link ItemEntity} will be destroyed and the new Entity will be added to the world.
     *
     * @param stack The item stack
     * @return True of the item has a custom entity. If true, {@link #createEntity} will be called
     * @see IForgeItem#hasCustomEntity(ItemStack)
     */
    default boolean hasCustomEntity(ItemStack stack) {
        return false;
    }

    /**
     * This function should return a new entity to replace the dropped item.
     * Returning null here will not kill the {@link ItemEntity} and will leave it to function normally.
     * Called when the item it placed in a level.
     *
     * @param level    The level the entity is being created in
     * @param original The original {@link ItemEntity}, useful for getting the position of the entity
     * @param stack    The item stack
     * @return A new entity to spawn, or {@code null}
     * @see IForgeItem#createEntity(Level, Entity, ItemStack)
     */
    default @Nullable Entity createEntity(Level level, ItemEntity original, ItemStack stack) {
        return null;
    }

    @FunctionalInterface
    interface ModifiedLifetime extends ICustomItemEntity {

        /**
         * Retrieves the normal 'lifetime' of this item when it is dropped on the ground as an {@link ItemEntity}.
         * This is in ticks. The default result is 6000 ticks (5 minutes).
         *
         * @param stack The item stack
         * @param level The level the entity is in
         * @return The normal lifetime in ticks.
         * @see IForgeItem#getEntityLifespan(ItemStack, Level)
         */
        @Override
        int getEntityLifetime(ItemStack stack, Level level);
    }

}
