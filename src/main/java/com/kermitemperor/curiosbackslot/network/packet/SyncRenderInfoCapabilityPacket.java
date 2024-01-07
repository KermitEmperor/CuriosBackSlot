package com.kermitemperor.curiosbackslot.network.packet;

import com.kermitemperor.curiosbackslot.capability.XYZPosAndRotationProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

import static com.kermitemperor.curiosbackslot.CuriosBackSlot.LOGGER;

@SuppressWarnings("unused")
public class SyncRenderInfoCapabilityPacket {


    private UUID playerUUID;
    private boolean third_p_render;
    private float x;
    private float y;
    private float z;
    private float xrot;
    private float yrot;
    private float zrot;

    public SyncRenderInfoCapabilityPacket() {}

    public SyncRenderInfoCapabilityPacket(UUID playerUUID, boolean third_p_render, float x, float y, float z, float xrot, float yrot, float zrot) {
        this.playerUUID = playerUUID;
        this.third_p_render = third_p_render;
        this.x = x;
        this.y = y;
        this.z = z;
        this.xrot = xrot;
        this.yrot = yrot;
        this.zrot = zrot;
    }

    public SyncRenderInfoCapabilityPacket(FriendlyByteBuf buf) {
        this.playerUUID = buf.readUUID();
        this.third_p_render = buf.readBoolean();
        this.x = buf.readFloat();
        this.y = buf.readFloat();
        this.z = buf.readFloat();
        this.xrot = buf.readFloat();
        this.yrot = buf.readFloat();
        this.zrot = buf.readFloat();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(this.playerUUID);
        buf.writeBoolean(this.third_p_render);
        buf.writeFloat(this.x);
        buf.writeFloat(this.y);
        buf.writeFloat(this.z);
        buf.writeFloat(this.xrot);
        buf.writeFloat(this.yrot);
        buf.writeFloat(this.zrot);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        UUID PlayerUUID = this.playerUUID;
        boolean ThirdPersonRender = this.third_p_render;
        float X = this.x;
        float Y = this.y;
        float Z = this.z;
        float Xrot = this.xrot;
        float Yrot = this.yrot;
        float Zrot = this.zrot;
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            LivingEntity player = Minecraft.getInstance().level.getPlayerByUUID(PlayerUUID);

            if (player != null) {
                LOGGER.info("recieved on Client");
                player.getCapability(XYZPosAndRotationProvider.PLAYER_BACK_WEAPON_XYZ).ifPresent(xyzPosAndRotation -> {
                    //LOGGER.info("Client: " + player.getName().getContents() +" " + X+ " " + Y+ " " + Z+ " " + Xrot+ " " + Yrot+ " " + Zrot);
                    xyzPosAndRotation.setXYZPosAndRotationDATA(ThirdPersonRender,X,Y,Z,Xrot,Yrot,Zrot);
                });

            }

        });
        return true;
    }
}
