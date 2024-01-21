package com.kermitemperor.curiosbackslot.render;

import com.kermitemperor.curiosbackslot.config.ClientConfig;
import com.kermitemperor.curiosbackslot.util.CuriosBackSlotHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

@OnlyIn(Dist.CLIENT)
public class GuiRenderer {

    final static Minecraft mc = Minecraft.getInstance();

    private static final ResourceLocation WIDGETS = new ResourceLocation("textures/gui/widgets.png");

    public static IGuiOverlay overlay = ((gui, poseStack, partialTick, screenWidth, screenHeight) -> {
        if (mc.player == null) return;
        if (!(ClientConfig.SHOW.get())) return;
        if (mc.player.isDeadOrDying()) return;

        ItemStack stack = CuriosBackSlotHandler.getStackInSlotClient();

        //noinspection DataFlowIssue
        if (mc.options.hideGui || mc.gameMode.getPlayerMode() == GameType.SPECTATOR || stack.isEmpty())
            return;

        gui.setupOverlayRenderState(true, false);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WIDGETS);

        int xPos = screenWidth / 2 + 69 + ClientConfig.X_OFFSET.get();
        int yPos = screenHeight + ClientConfig.Y_OFFSET.get();

        GuiComponent.blit(poseStack, xPos + 29, yPos - 23, 24, 22, 29, 24);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        mc.getItemRenderer().renderGuiItem(poseStack,stack,xPos + 32, yPos - 19);
    });
}
