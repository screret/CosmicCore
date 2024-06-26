package com.ghostipedia.cosmiccore;

import com.ghostipedia.cosmiccore.api.capability.recipe.CosmicRecipeCapabilities;
import com.ghostipedia.cosmiccore.api.registries.CosmicRegistries;
import com.ghostipedia.cosmiccore.api.capability.CosmicCapabilities;
import com.ghostipedia.cosmiccore.common.data.CosmicBlocks;
import com.ghostipedia.cosmiccore.common.data.CosmicCreativeModeTabs;
import com.ghostipedia.cosmiccore.common.data.CosmicItems;
import com.ghostipedia.cosmiccore.common.data.CosmicMachines;
import com.ghostipedia.cosmiccore.common.data.materials.CosmicElements;
import com.ghostipedia.cosmiccore.common.data.materials.CosmicMaterials;
import com.ghostipedia.cosmiccore.gtbridge.CosmicCoreRecipeTypes;
import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.data.chemical.Element;
import com.gregtechceu.gtceu.api.data.chemical.material.event.MaterialEvent;
import com.gregtechceu.gtceu.api.data.chemical.material.event.MaterialRegistryEvent;
import com.gregtechceu.gtceu.api.data.chemical.material.registry.MaterialRegistry;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.block.CoilBlock;
import com.gregtechceu.gtceu.config.ConfigHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




@Mod(CosmicCore.MOD_ID)
public class CosmicCore {
    public static final String MOD_ID = "cosmiccore", NAME = "CosmicCore";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

    public static MaterialRegistry MATERIAL_REGISTRY;

    //Init Everything
    public CosmicCore() {
        CosmicCore.init();
        var bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.register(this);
        bus.addGenericListener(GTRecipeType.class, this::registerRecipeTypes);
       // bus.addGenericListener(Class.class, this::registerRecipeConditions);
       // bus.addGenericListener(MachineDefinition.class, this::registerMachines);
        bus.addGenericListener(MachineDefinition.class, this::registerMachines);
    }

    public static void init() {
        ConfigHolder.init();
        CosmicCreativeModeTabs.init();
        CosmicBlocks.init();
        CosmicItems.init();
        CosmicRegistries.REGISTRATE.registerRegistrate();
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    @SubscribeEvent
    public void registerMaterialRegistry(MaterialRegistryEvent event) {
        MATERIAL_REGISTRY = GTCEuAPI.materialManager.createRegistry(CosmicCore.MOD_ID);
    }
    @SubscribeEvent
    public void  registerMaterials(MaterialEvent event) {
        CosmicMaterials.register();
    }

    @SubscribeEvent
    public void onLoadComplete(FMLLoadCompleteEvent event) {
        GTCEuAPI.HEATING_COILS.remove(CoilBlock.CoilType.RTMALLOY);
    }

    public void registerRecipeTypes(GTCEuAPI.RegisterEvent<ResourceLocation, GTRecipeType> event) {
        CosmicCoreRecipeTypes.init();
    }

    public void registerMachines(GTCEuAPI.RegisterEvent<ResourceLocation, MachineDefinition> event) {
        CosmicMachines.init();
    }

    @SubscribeEvent
    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        CosmicCapabilities.register(event);
    }
}