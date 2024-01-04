package com.kermitemperor.curiosbackslot.network.packet;

import com.kermitemperor.curiosbackslot.capability.XYZPosAndRotationProvider;
import com.kermitemperor.curiosbackslot.network.PacketChannel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ResyncWithMePacket {
    public ResyncWithMePacket() {}


    public ResyncWithMePacket(FriendlyByteBuf buf) {

    }

    public void encode(FriendlyByteBuf buf) {

    }

    @SuppressWarnings("CodeBlock2Expr")
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            LivingEntity player = context.getSender();

            if (player != null) {
                player.getCapability(XYZPosAndRotationProvider.PLAYER_BACK_WEAPON_XYZ).ifPresent(xyzPosAndRotation -> {
                    PacketChannel.sendToAllClients(new SyncRenderInfoCapabilityPacket(
                            player.getUUID(),
                            xyzPosAndRotation.getX(),
                            xyzPosAndRotation.getY(),
                            xyzPosAndRotation.getZ(),
                            xyzPosAndRotation.getXrot(),
                            xyzPosAndRotation.getYrot(),
                            xyzPosAndRotation.getZrot()
                    ));
                });
            }

        });
        return true;
    }
}
