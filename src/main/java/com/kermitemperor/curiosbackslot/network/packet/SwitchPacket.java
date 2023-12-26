package com.kermitemperor.curiosbackslot.network.packet;

import com.kermitemperor.curiosbackslot.util.CuriosBackSlotHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
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
                ItemStack stack = player.getItemBySlot(EquipmentSlot.MAINHAND);
                player.setItemSlot(EquipmentSlot.MAINHAND, CuriosBackSlotHandler.getStackInSlot(player));
                CuriosBackSlotHandler.setStackInSlot(player, stack);

            }

        });
        return true;
    }
}
