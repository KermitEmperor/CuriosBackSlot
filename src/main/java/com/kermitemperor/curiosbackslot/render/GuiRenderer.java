package com.kermitemperor.curiosbackslot.render;

import com.kermitemperor.curiosbackslot.config.ClientConfig;
import com.kermitemperor.curiosbackslot.util.CuriosBackSlotHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

@OnlyIn(Dist.CLIENT)
public class GuiRenderer implements IGuiOverlay {

    final Minecraft mc = Minecraft.getInstance();

    private static final ResourceLocation WIDGETS = new ResourceLocation("textures/gui/widgets.png");

    public void render(ForgeGui gui, PoseStack poseStack, float partialTick, int width, int height) {
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

        int xPos = width / 2 + 69 + ClientConfig.X_OFFSET.get();
        int yPos = height + ClientConfig.Y_OFFSET.get();

        gui.blit(poseStack, xPos + 29, yPos - 23, 24, 22, 29, 24);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        mc.getItemRenderer().renderGuiItem(stack,xPos + 32, yPos - 19);
    }
}
