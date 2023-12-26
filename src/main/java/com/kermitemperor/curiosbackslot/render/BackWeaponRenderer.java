package com.kermitemperor.curiosbackslot.render;

import com.kermitemperor.curiosbackslot.CuriosBackSlot;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

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

        Player player = (Player) livingEntity;

        ICuriosItemHandler curiosItemHandler = CuriosApi.getCuriosHelper().getCuriosHandler(player).orElseThrow(NullPointerException::new);
        ICurioStacksHandler curioStacksHandler = curiosItemHandler.getStacksHandler(CuriosBackSlot.SLOT_ID).orElseThrow();
        ItemStack stack = curioStacksHandler.getStacks().getStackInSlot(0);




        matrixStack.pushPose();

        PlayerModel<AbstractClientPlayer> playerModel = pRenderer.getModel();
        playerModel.body.translateAndRotate(matrixStack);
        matrixStack.scale(1,1,1);
        matrixStack.translate(0, 0.25d, 0.16d);
        matrixStack.mulPose(Vector3f.ZN.rotationDegrees(90f));


        ItemRenderer itemRenderer = mc.getItemRenderer();
        BakedModel model = itemRenderer.getItemModelShaper().getItemModel(stack);

        //TODO per player customiability (Location, Scale and TransformType modification using GUI, store in capabilities probably)

        itemRenderer.render(stack, ItemTransforms.TransformType.FIXED, true, matrixStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY, model);

        matrixStack.popPose();
    }
}
