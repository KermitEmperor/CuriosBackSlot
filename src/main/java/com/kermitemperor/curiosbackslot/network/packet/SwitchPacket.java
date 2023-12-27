package com.kermitemperor.curiosbackslot.network.packet;

import com.kermitemperor.curiosbackslot.util.CuriosBackSlotHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class SwitchPacket {
    public SwitchPacket() {}


    public SwitchPacket(FriendlyByteBuf buf) {

    }

    public void encode(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();

            if (player != null) {
                ItemStack handStack = player.getItemBySlot(EquipmentSlot.MAINHAND);
                /*TODO fix this
                java.lang.RuntimeException: Attempted to load class net/minecraft/client/player/LocalPlayer for invalid dist DEDICATED_SERVER
                */
                ItemStack backStack = CuriosBackSlotHandler.getStackInSlot(player);
                if (backStack.isEmpty() && handStack.isEmpty()) return;
                player.setItemSlot(EquipmentSlot.MAINHAND, backStack);
                CuriosBackSlotHandler.setStackInSlot(player, handStack);
                player.getLevel().playSound(null, player.blockPosition(), SoundEvents.ARMOR_EQUIP_ELYTRA, SoundSource.AMBIENT, 1, 1);

            }

        });
        return true;
    }
}
