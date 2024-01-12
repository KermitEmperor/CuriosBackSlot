package com.kermitemperor.curiosbackslot.client;

import com.kermitemperor.curiosbackslot.capability.XYZPosAndRotationProvider;
import com.kermitemperor.curiosbackslot.network.PacketChannel;
import com.kermitemperor.curiosbackslot.network.packet.RenderInfoCapabilityPacket;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;


@SuppressWarnings("FieldCanBeLocal") //please stop with the angry yellow
public class BackWeaponConfigurationScreen extends Screen {
    public BackWeaponConfigurationScreen() {
        super(new TranslatableComponent("backweaponconfigurationscreen.title"));
    }


    private boolean third_p_render;
    private float x;
    private float y;
    private float z;
    private float xrot;
    private float yrot;
    private float zrot;

    private final int RED = 16733525;
    private final int GREEN = 5635925;
    private final int BLUE = 5592575;
    private final int GRAY = 11184810;

    private Minecraft mc;
    private Button yesSave;
    private Button noSave;
    private boolean added_ya_sure = false;


    //TODO make a usable stylish screen
    //TODO show what are the current values

    @Override
    public void init() {
        mc = Minecraft.getInstance();
        LivingEntity player = mc.player;

        if (added_ya_sure) addYesNo();



        if (player != null) {
            player.getCapability(XYZPosAndRotationProvider.PLAYER_BACK_WEAPON_XYZ).ifPresent(xyzPosAndRotation -> {
                this.x = xyzPosAndRotation.getX();
                this.y = xyzPosAndRotation.getY();
                this.z = xyzPosAndRotation.getZ();

                this.xrot = xyzPosAndRotation.getXrot();
                this.yrot = xyzPosAndRotation.getYrot();
                this.zrot = xyzPosAndRotation.getZrot();
                this.third_p_render = xyzPosAndRotation.isThirdPersonRender();
            });
        }


        //position section
        //X
        this.addRenderableWidget(positionSection("+1.0", -26,-36, pButton -> this.x += 1));
        this.addRenderableWidget(positionSection("+0.1", 0,-36, pButton -> this.x += 0.1));
        this.addRenderableWidget(positionSection("-0.1", 26,-36, pButton -> this.x -= 0.1));
        this.addRenderableWidget(positionSection("-1.0", 52,-36, pButton -> this.x -= 1));
        //Y
        this.addRenderableWidget(positionSection("+1.0", -26,-14, pButton -> this.y += 1));
        this.addRenderableWidget(positionSection("+0.1", 0,-14, pButton -> this.y += 0.1));
        this.addRenderableWidget(positionSection("-0.1", 26,-14, pButton -> this.y -= 0.1));
        this.addRenderableWidget(positionSection("-1.0", 52,-14, pButton -> this.y -= 1));
        //z
        this.addRenderableWidget(positionSection("+1.0", -26,8, pButton -> this.z += 1));
        this.addRenderableWidget(positionSection("+0.1", 0,8, pButton -> this.z += 0.1));
        this.addRenderableWidget(positionSection("-0.1", 26,8, pButton -> this.z -= 0.1));
        this.addRenderableWidget(positionSection("-1.0", 52,8, pButton -> this.z -= 1));

        //rotation section
        //X
        this.addRenderableWidget(rotationSection("+1.0", -26,-36, pButton -> this.xrot += 1));
        this.addRenderableWidget(rotationSection("+0.1", 0,-36, pButton -> this.xrot += 0.1));
        this.addRenderableWidget(rotationSection("-0.1", 26,-36, pButton -> this.xrot -= 0.1));
        this.addRenderableWidget(rotationSection("-1.0", 52,-36, pButton -> this.xrot -= 1));
        //Y
        this.addRenderableWidget(rotationSection("+1.0", -26,-14, pButton -> this.yrot += 1));
        this.addRenderableWidget(rotationSection("+0.1", 0,-14, pButton -> this.yrot += 0.1));
        this.addRenderableWidget(rotationSection("-0.1", 26,-14, pButton -> this.yrot -= 0.1));
        this.addRenderableWidget(rotationSection("-1.0", 52,-14, pButton -> this.yrot -= 1));
        //z
        this.addRenderableWidget(rotationSection("+1.0", -26,8, pButton -> this.zrot += 1));
        this.addRenderableWidget(rotationSection("+0.1", 0,8, pButton -> this.zrot += 0.1));
        this.addRenderableWidget(rotationSection("-0.1", 26,8, pButton -> this.zrot -= 0.1));
        this.addRenderableWidget(rotationSection("-1.0", 52,8, pButton -> this.zrot -= 1));

        //Save Section
        this.addRenderableWidget(saveSection(new TranslatableComponent("backweaponconfigurationscreen.save"), 0,8,this.width/4, 20, pButton -> {
            save();
        }));

        //Misc Section

        this.addRenderableWidget(new Button(this.width/4 - Mth.floor(this.width/4f / 2f), this.height/4-26, this.width/4, 20,  new TranslatableComponent("backweaponconfigurationscreen.reset"), pButton -> {
            this.x = 0;
            this.y = 0;
            this.z = 0;

            this.xrot = 0;
            this.yrot = 0;
            this.zrot = 0;
            this.third_p_render = false;
        }));

        this.addRenderableWidget(CycleButton.booleanBuilder(new TranslatableComponent("backweaponconfigurationscreen.cyclebutton.yes"), new TranslatableComponent("backweaponconfigurationscreen.cyclebutton.no"))
            .withInitialValue(third_p_render)
            .displayOnlyValue()
            .create(
                this.width/4 - Mth.floor(this.width/4f / 2f),
                this.height/4,
                this.width/4,
                20,
                new TranslatableComponent("backweaponconfigurationscreen.cyclebutton"), //Where even is this shown?
                (pCycleButton, pValue) -> this.third_p_render = pValue
            )
        );
    }

    private Button positionSection(String str, int x, int y, Button.OnPress onPress) {
        int right_middle = Mth.floor(this.width * 0.75);
        int horizontal_upper_middle = this.height / 4;

        return new Button(
                right_middle+x+12,
                horizontal_upper_middle+y+10,
                24,
                20,
                new TextComponent(str),
                onPress
        );
    }

    private Button rotationSection(String str, int x, int y, Button.OnPress onPress) {
        int right_middle = Mth.floor(this.width * 0.75);
        int horizontal_lower_middle = Mth.floor(this.height * 0.75);

        return new Button(
                right_middle+x+12,
                horizontal_lower_middle+y+10,
                24,
                20,
                new TextComponent(str),
                onPress
        );
    }


    @SuppressWarnings("SameParameterValue") //SSsssshhh!
    private Button saveSection(Component component, int x, int y, int width, int height, Button.OnPress onPress) {
        int left_middle = this.width / 4;
        int horizontal_lower_middle = Mth.floor(this.height * 0.75);

        return new Button(
                left_middle-x-Mth.floor(width/2f),
                horizontal_lower_middle+y+Mth.floor(height/2f),
                width,
                height,
                component,
                onPress
        );
    }

    private void save() {
        PacketChannel.sendToServer(new RenderInfoCapabilityPacket(this.third_p_render, this.x, this.y, this.z, this.xrot, this.yrot, this.zrot));
    }

    @Override
    public void render(@NotNull PoseStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        int right_middle = Mth.floor(this.width * 0.75);
        int horizontal_upper_middle = this.height / 4;
        int horizontal_lower_middle = Mth.floor(this.height * 0.75);

        this.renderBackground(pMatrixStack);
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            renderEntity(this.width / 2, this.height / 2 + 25, 30, pMouseX - (float) this.width / 2, (float) (this.height / 2 - 25) - pMouseY, player);
        }
        drawCenteredString(pMatrixStack, this.font, this.title, this.width / 2, 15, 16777215);

        drawCenteredString(pMatrixStack, this.font, new TranslatableComponent("backweaponconfigurationscreen.position"), right_middle+37, horizontal_upper_middle-40, GRAY);
        drawCenteredString(pMatrixStack, this.font, "X:", right_middle-20, horizontal_upper_middle-20, RED);
        drawCenteredString(pMatrixStack, this.font, "Y:", right_middle-20, horizontal_upper_middle+2, GREEN);
        drawCenteredString(pMatrixStack, this.font, "Z:", right_middle-20, horizontal_upper_middle+24, BLUE);

        drawCenteredString(pMatrixStack, this.font, new TranslatableComponent("backweaponconfigurationscreen.rotation"), right_middle+37, horizontal_lower_middle-40, GRAY);
        drawCenteredString(pMatrixStack, this.font, "X:", right_middle-20, horizontal_lower_middle-20, RED);
        drawCenteredString(pMatrixStack, this.font, "Y:", right_middle-20, horizontal_lower_middle+2, GREEN);
        drawCenteredString(pMatrixStack, this.font, "Z:", right_middle-20, horizontal_lower_middle+24, BLUE);

        if (added_ya_sure) {
            drawCenteredString(pMatrixStack, this.font, new TranslatableComponent("backweaponconfigurationscreen.sure"), this.width / 2, this.height / 2 + 44, 16777215);
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
        if (!added_ya_sure) {
            addYesNo();
        } else {
            super.onClose();
        }
    }

    private void removeYesNo() {
        this.removeWidget(noSave);
        this.removeWidget(yesSave);
        this.added_ya_sure = false;
    }

    private void addYesNo() {
        this.yesSave = new Button(this.width/2+1,this.height/2+55, 50,20, new TranslatableComponent("backweaponconfigurationscreen.yes"), pButton -> super.onClose());
        this.noSave = new Button(this.width/2-51,this.height/2+55, 50,20, new TranslatableComponent("backweaponconfigurationscreen.no"), pButton -> {
            if (added_ya_sure) {
                removeYesNo();
            }
        });

        this.addRenderableWidget(yesSave);
        this.addRenderableWidget(noSave);
        this.added_ya_sure = true;
    }


}
