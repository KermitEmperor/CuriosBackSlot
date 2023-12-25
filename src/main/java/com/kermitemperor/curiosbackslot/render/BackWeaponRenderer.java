package com.kermitemperor.curiosbackslot.render;

import com.kermitemperor.curiosbackslot.CuriosBackSlot;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import static com.kermitemperor.curiosbackslot.CuriosBackSlot.LOGGER;

@OnlyIn(Dist.CLIENT)
public class BackWeaponRenderer {



    public BackWeaponRenderer() {
        super();
    }

    public void render(PlayerModel<AbstractClientPlayer> playerModel, PoseStack matrixStack, LivingEntity livingEntity, MultiBufferSource source, int packedLight) {
        Minecraft mc = Minecraft.getInstance();

        Player player = (Player) livingEntity;

        ICuriosItemHandler curiosItemHandler = CuriosApi.getCuriosHelper().getCuriosHandler(player).orElseThrow(NullPointerException::new);
        ICurioStacksHandler curioStacksHandler = curiosItemHandler.getStacksHandler(CuriosBackSlot.SLOT_ID).orElseThrow();
        ItemStack stack = curioStacksHandler.getStacks().getStackInSlot(0);

        if (stack.isEmpty()) return;

        matrixStack.pushPose();

        ModelPart modelPart = playerModel.body;

        matrixStack.scale(1,1,1);

        LOGGER.info(String.valueOf(player.yBodyRot));

        modelPart.translateAndRotate(matrixStack);
        matrixStack.mulPose(Vector3f.YN.rotationDegrees(player.yBodyRot));




        mc.getItemRenderer().renderStatic(livingEntity, stack, ItemTransforms.TransformType.FIXED, false, matrixStack, source, livingEntity.level, packedLight, 0, 0);

        matrixStack.popPose();

    }
}
