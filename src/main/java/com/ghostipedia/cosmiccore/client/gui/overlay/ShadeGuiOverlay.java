package com.ghostipedia.cosmiccore.client.gui.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import com.ghostipedia.cosmiccore.CosmicCore;
import com.ghostipedia.cosmiccore.CosmicUtils;
import com.mojang.blaze3d.systems.RenderSystem;

public class ShadeGuiOverlay implements IGuiOverlay {

    private static final ResourceLocation OVERLAY_LOCATION = CosmicCore.id("textures/misc/shade_overlay.png");

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        gui.setupOverlayRenderState(true, false);

        if (CosmicUtils.hasTheOneRing(Minecraft.getInstance().cameraEntity)) {
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            guiGraphics.blit(OVERLAY_LOCATION, 0, 0, -90, 0.0F, 0.0F,
                    guiGraphics.guiWidth(), guiGraphics.guiHeight(), guiGraphics.guiWidth(), guiGraphics.guiHeight());
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
        }
    }
}
