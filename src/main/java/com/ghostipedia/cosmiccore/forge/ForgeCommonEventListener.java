package com.ghostipedia.cosmiccore.forge;

import com.ghostipedia.cosmiccore.CosmicCore;
import com.ghostipedia.cosmiccore.CosmicUtils;
import com.ghostipedia.cosmiccore.common.commands.WirelessEnergyCommand;
import com.ghostipedia.cosmiccore.common.data.CosmicBlocks;
import com.ghostipedia.cosmiccore.common.data.CosmicItems;
import com.ghostipedia.cosmiccore.common.data.CosmicMachines;
import com.ghostipedia.cosmiccore.common.item.behavior.EffectApplicationBehavior;
import com.ghostipedia.cosmiccore.common.machine.multiblock.multi.IPBF;
import com.ghostipedia.cosmiccore.common.machine.multiblock.multi.SteamAssembler;
import com.ghostipedia.cosmiccore.common.machine.multiblock.multi.SteamCaster;
import com.ghostipedia.cosmiccore.common.machine.multiblock.multi.SteamMixer;
import com.ghostipedia.cosmiccore.common.machine.multiblock.part.SoulHatchPartMachine;
import com.ghostipedia.cosmiccore.mixin.accessor.LivingEntityAccessor;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.block.MetaMachineBlock;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;

import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.MissingMappingsEvent;

import earth.terrarium.adastra.AdAstra;

import java.util.Locale;

import static com.ghostipedia.cosmiccore.common.item.armor.ChestSanguineWarptechSuite.SANGUINE_SHIELD_NBT_KEY;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = CosmicCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeCommonEventListener {

    @SubscribeEvent
    public static void entityPlacementEventHandler(BlockEvent.EntityPlaceEvent event) {
        if (event.getPlacedBlock().getBlock() instanceof MetaMachineBlock block &&
                block.getMachine(event.getLevel(), event.getPos()) instanceof SoulHatchPartMachine soulHatch &&
                event.getEntity() instanceof Player player) {
            soulHatch.attachSoulNetwork(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        if (CosmicUtils.hasTheOneRing(event.player)) {
            // forcefully get the ring's effects.
            var effects = ((EffectApplicationBehavior) CosmicItems.THE_ONE_RING.get().getComponents().get(0))
                    .getEffects();
            for (var effect : effects) {
                if (event.player.getRandom().nextFloat() < effect.secondFloat()) {
                    event.player.addEffect(new MobEffectInstance(effect.first()));
                }
            }
            ((LivingEntityAccessor) event.player).callRemoveEffectParticles();
        }
    }

    // Sanguine chest piece gives creative flight when equipped and powered.
    // This is handled in the ChestSanguineWarptechSuite.java
    // However, we want to take this flight away when it is taken off.
    @SubscribeEvent
    public static void onEquipChange(LivingEquipmentChangeEvent e) {
        if (!(e.getEntity() instanceof ServerPlayer p)) return;
        if (e.getSlot() != EquipmentSlot.CHEST) return;

        boolean putOn = e.getTo().is(CosmicItems.SANGUINE_WARPTECH_CHESTPLATE.get());
        boolean tookOff = e.getFrom().is(CosmicItems.SANGUINE_WARPTECH_CHESTPLATE.get()) && !putOn;

        if (tookOff && !p.isCreative() && !p.isSpectator()) {
            p.getAbilities().mayfly = false;
            p.getAbilities().flying = false;
            p.fallDistance = 0;
            p.connection.send(
                    new ClientboundPlayerAbilitiesPacket(p.getAbilities()));
            p.getPersistentData().putBoolean(SANGUINE_SHIELD_NBT_KEY, false);
        }
    }

    // Sanguine shield effect from sanguine chest piece negates all damage and defies death.
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onPlayerDamage(LivingDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        CompoundTag tag = player.getPersistentData();
        if (tag.contains(SANGUINE_SHIELD_NBT_KEY) && tag.getBoolean(SANGUINE_SHIELD_NBT_KEY)) {
            event.setCanceled(true);
        }
    }

    // Sanguine shield effect from sanguine chest piece negates all damage and defies death.
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        CompoundTag tag = player.getPersistentData();
        if (tag.contains(SANGUINE_SHIELD_NBT_KEY) && tag.getBoolean(SANGUINE_SHIELD_NBT_KEY)) {
            event.setCanceled(true); // Prevent death
            player.sendSystemMessage(
                    Component.translatable("cosmiccore.armor.sanguinewarptech.message.death_defiance"));
        }
    }

    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        WirelessEnergyCommand.register(event.getDispatcher(), event.getBuildContext());
    }

    @SubscribeEvent
    public static void remapIds(MissingMappingsEvent event) {
        // miscellaneous items
        remapBlockWithItem(event, new ResourceLocation(AdAstra.MOD_ID, "sun_globe"), CosmicBlocks.SUN_GLOBE.get());

        // beeg machines
        remapMachine(event, "steam_caster", SteamCaster.STEAM_CASTER);
        remapMachine(event, "steam_mixer", SteamMixer.STEAM_MIXER);
        remapMachine(event, "industrial_primitive_blast_furnace", IPBF.INDUSTRIAL_PRIMITIVE_BLAST_FURNACE);
        remapMachine(event, "high_pressure_assembler", SteamAssembler.HIGH_PRESSURE_ASSEMBLER);
        remapMachine(event, "large_combustion_engine_cc", CosmicMachines.LARGE_COMBUSTION_ENGINE);
        remapMachine(event, "extreme_combustion_engine_cc", CosmicMachines.EXTREME_COMBUSTION_ENGINE);
        remapMachine(event, "ludicrous_combustion_engine_cc", CosmicMachines.LUDICROUS_COMBUSTION_ENGINE);
        remapMachine(event, "ultimate_combustion_engine_cc", CosmicMachines.ULTIMATE_COMBUSTION_ENGINE);

        // naq mini reactors
        for (MachineDefinition machine : CosmicMachines.NAQUAHINE_MINI_REACTOR) {
            if (machine == null) continue;
            String name = (GTValues.VN[machine.getTier()].toLowerCase(Locale.ROOT) + "_naquahine_mini_reactor");
            remapMachine(event, name, machine);
        }

        // steam singleblocks
        remapMachine(event, "lp_steam_wiremill", CosmicMachines.STEAM_WIREMILL.first());
        remapMachine(event, "hp_steam_wiremill", CosmicMachines.STEAM_WIREMILL.second());
        remapMachine(event, "lp_steam_wiremill", CosmicMachines.STEAM_BENDER.first());
        remapMachine(event, "hp_steam_wiremill", CosmicMachines.STEAM_BENDER.second());

        remapMachine(event, "steam_fluid_output_hatch", CosmicMachines.STEAM_EXPORT_HATCH);
        remapMachine(event, "steam_fluid_input_hatch", CosmicMachines.STEAM_IMPORT_HATCH);
    }

    private static void remapItem(MissingMappingsEvent event, ResourceLocation id, ItemLike replacement) {
        event.getMappings(Registries.ITEM, id.getNamespace()).forEach(mapping -> {
            if (mapping.getKey().equals(id)) {
                mapping.remap(replacement.asItem());
            }
        });
    }

    private static void remapBlock(MissingMappingsEvent event, ResourceLocation id, Block replacement) {
        event.getMappings(Registries.BLOCK, id.getNamespace()).forEach(mapping -> {
            if (mapping.getKey().equals(id)) {
                mapping.remap(replacement);
            }
        });
    }

    private static void remapBlockWithItem(MissingMappingsEvent event, ResourceLocation id, Block replacement) {
        remapBlock(event, id, replacement);
        remapItem(event, id, replacement);
    }

    private static void remapMachine(MissingMappingsEvent event, String name, MachineDefinition machine) {
        ResourceLocation id = GTCEu.id(name);
        remapBlock(event, id, machine.getBlock());
        remapItem(event, id, machine.getItem());
        event.getMappings(Registries.BLOCK_ENTITY_TYPE, GTCEu.MOD_ID).forEach(mapping -> {
            if (mapping.getKey().equals(id)) {
                mapping.remap(machine.getBlockEntityType());
            }
        });
    }
}
