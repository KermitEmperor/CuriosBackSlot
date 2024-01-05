package com.kermitemperor.curiosbackslot.client;

import com.kermitemperor.curiosbackslot.network.PacketChannel;
import com.kermitemperor.curiosbackslot.network.packet.RenderInfoCapabilityPacket;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;


import static com.kermitemperor.curiosbackslot.CuriosBackSlot.LOGGER;

public class BackWeaponConfigurationScreen extends Screen {
    public BackWeaponConfigurationScreen() {
        super(new TextComponent("yeah"));
    }



    private double x;
    private double y;

    //TODO make a usable stylish screen
    //TODO show what are the current values

    @Override
    public void init() {
        this.addRenderableWidget(new Button(100, 100 ,18 ,20, new TextComponent("1+"), (pButton) -> {
            LOGGER.info("pressed");
            this.x += 1;
        }));
        this.addRenderableWidget(new Button(100, 122 ,18 ,20, new TextComponent("0.1+"), (pButton) -> {
            LOGGER.info("pressed");
            this.x += 0.1;
        }));

        this.addRenderableWidget(new Button(120, 100 ,18 ,20, new TextComponent("1+"), (pButton) -> {
            LOGGER.info("pressed");
            this.y += 1;
        }));
        this.addRenderableWidget(new Button(120, 122 ,18 ,20, new TextComponent("0.1+"), (pButton) -> {
            LOGGER.info("pressed");
            this.y += 0.1;
        }));

        this.addRenderableWidget(new Button(100, 144 ,18 ,20, new TextComponent("Save"), (pButton) -> {
            LOGGER.info("saving");
            PacketChannel.sendToServer(new RenderInfoCapabilityPacket(x, y, 0, 0, 0, 0));
        }));
    }

    @Override
    public void render(@NotNull PoseStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        this.renderBackground(pMatrixStack);
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            renderEntity(this.width / 2, this.height / 2 + 25, 30, pMouseX - (float) this.width / 2, (float) (this.height / 2 - 25) - pMouseY, player);
        }
        super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
    }


    //Hi-pity Hopi ty, your code is my property
    @SuppressWarnings("deprecation")
    public static void renderEntity(int pPosX, int pPosY, int pScale, float pMouseX, float pMouseY, LivingEntity pLivingEntity) {
        float f = (float)Math.atan(pMouseX / 40.0F);
        float f1 = (float)Math.atan(pMouseY / 40.0F);
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.translate(pPosX, pPosY, 1050.0D);
        posestack.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();
        PoseStack posestack1 = new PoseStack();
        posestack1.translate(0.0D, 0.0D, 1000.0D);
        posestack1.scale((float)pScale, (float)pScale, (float)pScale);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
        Quaternion quaternion1 = Vector3f.XP.rotationDegrees(f1 * 20.0F);
        quaternion.mul(quaternion1);
        posestack1.mulPose(quaternion);
        posestack1.mulPose(Vector3f.YP.rotationDegrees(180f));
        float f2 = pLivingEntity.yBodyRot;
        float f3 = pLivingEntity.getYRot();
        float f4 = pLivingEntity.getXRot();
        float f5 = pLivingEntity.yHeadRotO;
        float f6 = pLivingEntity.yHeadRot;
        pLivingEntity.yBodyRot = 180.0F + f * 20.0F;
        pLivingEntity.setYRot(180.0F + f * 40.0F);
        pLivingEntity.setXRot(-f1 * 20.0F);
        pLivingEntity.yHeadRot = pLivingEntity.getYRot();
        pLivingEntity.yHeadRotO = pLivingEntity.getYRot();
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        quaternion1.conj();
        entityrenderdispatcher.overrideCameraOrientation(quaternion1);
        entityrenderdispatcher.setRenderShadow(false);
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() ->
                entityrenderdispatcher.render(pLivingEntity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, posestack1, multibuffersource$buffersource, 15728880)
        );
        multibuffersource$buffersource.endBatch();
        entityrenderdispatcher.setRenderShadow(true);
        pLivingEntity.yBodyRot = f2;
        pLivingEntity.setYRot(f3);
        pLivingEntity.setXRot(f4);
        pLivingEntity.yHeadRotO = f5;
        pLivingEntity.yHeadRot = f6;
        posestack.popPose();
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (pKeyCode == KeyBinding.XYZ_KEY.getKey().getValue()) {
            this.onClose();
            return true;
        }
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onClose() {
        LOGGER.info("saving");
        PacketChannel.sendToServer(new RenderInfoCapabilityPacket(x, y, 0, 0, 0, 0));
        super.onClose();
    }

}
