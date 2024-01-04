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


    protected double x;
    protected double y;
    protected double z;
    protected float xrot;
    protected float yrot;
    protected float zrot;

    public RenderInfoCapabilityPacket() {}

    public RenderInfoCapabilityPacket(double x, double y, double z, float xrot, float yrot, float zrot) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.xrot = xrot;
        this.yrot = yrot;
        this.zrot = zrot;
    }

    public RenderInfoCapabilityPacket(FriendlyByteBuf buf) {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.xrot = buf.readFloat();
        this.yrot = buf.readFloat();
        this.zrot = buf.readFloat();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeFloat(this.xrot);
        buf.writeFloat(this.yrot);
        buf.writeFloat(this.zrot);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        double X = this.x;
        double Y = this.y;
        double Z = this.z;
        float Xrot = this.xrot;
        float Yrot = this.yrot;
        float Zrot = this.zrot;

        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            LivingEntity player = context.getSender();

            if (player != null) {
                LOGGER.info("recieved on Server");
                player.getCapability(XYZPosAndRotationProvider.PLAYER_BACK_WEAPON_XYZ).ifPresent(xyzPosAndRotation -> {


                    xyzPosAndRotation.setXYZPosAndRotationDATA(X, Y, Z, Xrot, Yrot, Zrot);
                    LOGGER.info("Server:"+ " " + X+ " " + Y+ " " + Z+ " " + Xrot+ " " + Yrot+ " " + Zrot);
                    PacketChannel.sendToAllClients(new SyncRenderInfoCapabilityPacket(player.getUUID(),X, Y, Z, Xrot, Yrot, Zrot));
                });

            }

        });
        return true;
    }
}
