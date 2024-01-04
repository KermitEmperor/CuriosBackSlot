package com.kermitemperor.curiosbackslot.network;

import com.kermitemperor.curiosbackslot.CuriosBackSlot;
import com.kermitemperor.curiosbackslot.network.packet.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketChannel {
    private static SimpleChannel INSTANCE;

    private static int packetID = 0;
    private static int id() {
        return packetID++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(CuriosBackSlot.MOD_ID, "packetchannel"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(SwitchPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(SwitchPacket::encode)
                .decoder(SwitchPacket::new)
                .consumer(SwitchPacket::handle)
                .add();

        net.messageBuilder(RenderInfoCapabilityPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(RenderInfoCapabilityPacket::encode)
                .decoder(RenderInfoCapabilityPacket::new)
                .consumer(RenderInfoCapabilityPacket::handle)
                .add();

        net.messageBuilder(SyncRenderInfoCapabilityPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(SyncRenderInfoCapabilityPacket::encode)
                .decoder(SyncRenderInfoCapabilityPacket::new)
                .consumer(SyncRenderInfoCapabilityPacket::handle)
                .add();

        net.messageBuilder(ResyncWithMePacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(ResyncWithMePacket::encode)
                .decoder(ResyncWithMePacket::new)
                .consumer(ResyncWithMePacket::handle)
                .add();
    }


    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToAllClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
}