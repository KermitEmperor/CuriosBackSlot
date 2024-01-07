package com.kermitemperor.curiosbackslot.network.packet;

import com.kermitemperor.curiosbackslot.capability.XYZPosAndRotationProvider;
import com.kermitemperor.curiosbackslot.network.PacketChannel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static com.kermitemperor.curiosbackslot.CuriosBackSlot.LOGGER;

@SuppressWarnings("unused")
public class RenderInfoCapabilityPacket {


    private boolean third_p_render;
    private float x;
    private float y;
    private float z;
    private float xrot;
    private float yrot;
    private float zrot;

    public RenderInfoCapabilityPacket() {}

    public RenderInfoCapabilityPacket(boolean third_p_render,float x, float y, float z, float xrot, float yrot, float zrot) {
        this.third_p_render = third_p_render;
        this.x = x;
        this.y = y;
        this.z = z;
        this.xrot = xrot;
        this.yrot = yrot;
        this.zrot = zrot;
    }

    public RenderInfoCapabilityPacket(FriendlyByteBuf buf) {
        this.third_p_render = buf.readBoolean();
        this.x = buf.readFloat();
        this.y = buf.readFloat();
        this.z = buf.readFloat();
        this.xrot = buf.readFloat();
        this.yrot = buf.readFloat();
        this.zrot = buf.readFloat();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(this.third_p_render);
        buf.writeFloat(this.x);
        buf.writeFloat(this.y);
        buf.writeFloat(this.z);
        buf.writeFloat(this.xrot);
        buf.writeFloat(this.yrot);
        buf.writeFloat(this.zrot);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        boolean ThirdPersonRender = this.third_p_render;
        float X = this.x;
        float Y = this.y;
        float Z = this.z;
        float Xrot = this.xrot;
        float Yrot = this.yrot;
        float Zrot = this.zrot;

        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            LivingEntity player = context.getSender();

            if (player != null) {
                LOGGER.info("recieved on Server");
                player.getCapability(XYZPosAndRotationProvider.PLAYER_BACK_WEAPON_XYZ).ifPresent(xyzPosAndRotation -> {


                    xyzPosAndRotation.setXYZPosAndRotationDATA(ThirdPersonRender,X, Y, Z, Xrot, Yrot, Zrot);
                    //LOGGER.info("Server: "+ " "+ ThirdPersonRender + X+ " " + Y+ " " + Z+ " " + Xrot+ " " + Yrot+ " " + Zrot);
                    PacketChannel.sendToAllClients(new SyncRenderInfoCapabilityPacket(player.getUUID(),ThirdPersonRender,X, Y, Z, Xrot, Yrot, Zrot));
                });

            }

        });
        return true;
    }
}
