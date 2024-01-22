package com.kermitemperor.curiosbackslot.render;

import com.kermitemperor.curiosbackslot.util.CuriosBackSlotHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BackWeaponRenderer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {


    private final RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> pRenderer;

    public BackWeaponRenderer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> pRenderer) {
        super(pRenderer);
        this.pRenderer = pRenderer;
    }


    @SuppressWarnings("NullableProblems")
    @Override
    public void render(PoseStack matrixStack, MultiBufferSource bufferSource, int packedLight, AbstractClientPlayer livingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        Minecraft mc = Minecraft.getInstance();

        if (livingEntity.isDeadOrDying()) return;
        if (!(CuriosBackSlotHandler.renderItemInSlot(livingEntity))) return;


        ItemStack stack = CuriosBackSlotHandler.getStackInSlot(livingEntity);

        if (stack.isEmpty()) return;

        Item item = stack.getItem();


        matrixStack.pushPose();


        PlayerModel<AbstractClientPlayer> playerModel = pRenderer.getModel();
        playerModel.body.translateAndRotate(matrixStack);
        matrixStack.scale(1,1,1);
        matrixStack.translate(0, 0.35d, 0.16d);
        matrixStack.mulPose(Axis.ZN.rotationDegrees(90f));


        ItemRenderer itemRenderer = mc.getItemRenderer();
        BakedModel model = itemRenderer.getItemModelShaper().getItemModel(stack);


        if (item instanceof TridentItem) {
            if (hasArmor(livingEntity)) matrixStack.translate(0, 0 , 0.03d);
            matrixStack.mulPose(Axis.ZN.rotationDegrees(135f));
            matrixStack.mulPose(Axis.YN.rotationDegrees(60f));
            matrixStack.translate(0,0.2,-0.07);
            itemRenderer.renderStatic(stack, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, packedLight, OverlayTexture.NO_OVERLAY, matrixStack, bufferSource, livingEntity.level(), livingEntity.getId());

        } else if (item instanceof ShieldItem) {
            matrixStack.mulPose(Axis.ZN.rotationDegrees(90f));
            matrixStack.mulPose(Axis.YP.rotationDegrees(-90f));
            matrixStack.translate(-0.12, 0, -0.16);
            matrixStack.scale(0.65f, 0.65f, 0.65f);
            if (hasArmor(livingEntity)) matrixStack.translate(-0.01d, 0, 0);
            itemRenderer.renderStatic(stack, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, packedLight, OverlayTexture.NO_OVERLAY, matrixStack, bufferSource, livingEntity.level(), livingEntity.getId());
        } else if (item instanceof BlockItem){
            matrixStack.mulPose(Axis.ZP.rotationDegrees(90f));
            matrixStack.mulPose(Axis.XN.rotationDegrees(180f));
            if (model.isGui3d()) matrixStack.translate(0,0.15,-0.2);
            if (hasArmor(livingEntity)) matrixStack.translate(0, 0 , -0.07d);
            itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, matrixStack, bufferSource, livingEntity.level(), livingEntity.getId());
        } else {
            if (hasArmor(livingEntity)) matrixStack.translate(0, 0 , 0.07d);
            matrixStack.translate(-0.027,-0.813,-0.44);
            itemRenderer.renderStatic(stack, ItemDisplayContext.HEAD, packedLight, OverlayTexture.NO_OVERLAY, matrixStack, bufferSource, livingEntity.level(), livingEntity.getId());

        }

        matrixStack.popPose();
    }

    private boolean hasArmor(AbstractClientPlayer livingEntity) {
        return livingEntity.hasItemInSlot(EquipmentSlot.CHEST);
    }
}
