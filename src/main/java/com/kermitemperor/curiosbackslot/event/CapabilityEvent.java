package com.kermitemperor.curiosbackslot.event;


import com.kermitemperor.curiosbackslot.CuriosBackSlot;
import com.kermitemperor.curiosbackslot.capability.XYZPosAndRotation;
import com.kermitemperor.curiosbackslot.capability.XYZPosAndRotationProvider;
import com.kermitemperor.curiosbackslot.network.PacketChannel;
import com.kermitemperor.curiosbackslot.network.packet.ResyncWithMePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.kermitemperor.curiosbackslot.CuriosBackSlot.LOGGER;

@Mod.EventBusSubscriber(modid = CuriosBackSlot.MOD_ID)
public class CapabilityEvent {



    @SubscribeEvent
    public void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {

            LazyOptional<XYZPosAndRotation> existingCapability = player.getCapability(XYZPosAndRotationProvider.PLAYER_BACK_WEAPON_XYZ);

            final XYZPosAndRotationProvider provider = new XYZPosAndRotationProvider();

            if (!existingCapability.isPresent()) {
                //LOGGER.info("attached, is Client: " + player.level.isClientSide());
                event.addCapability(new ResourceLocation(CuriosBackSlot.MOD_ID, "properties"), provider);

                //This event is called twice, one on Server and on Client, we have to ask the server to
                //send a packet about the player's capability
                //and to set it in our side

                if (player.level.isClientSide) {

                    //We have to use the execute method, otherwise the PacketChannel will be called too soon

                    Minecraft.getInstance().execute(() -> PacketChannel.sendToServer(new ResyncWithMePacket()));
                }
            } else {
                LOGGER.info("XYZPosAndRotation capability already present");
            }
        }
    }

    @SuppressWarnings("CodeBlock2Expr")
    @SubscribeEvent
    public void onPlayerCloned(PlayerEvent.Clone event) {
        if(event.isWasDeath()) {
            event.getOriginal().getCapability(XYZPosAndRotationProvider.PLAYER_BACK_WEAPON_XYZ).ifPresent(oldStore -> {
                event.getOriginal().getCapability(XYZPosAndRotationProvider.PLAYER_BACK_WEAPON_XYZ).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
        }
    }

}
