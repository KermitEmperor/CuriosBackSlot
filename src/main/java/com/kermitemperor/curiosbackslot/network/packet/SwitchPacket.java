package com.kermitemperor.curiosbackslot.network.packet;

import com.kermitemperor.curiosbackslot.CuriosBackSlot;
import com.kermitemperor.curiosbackslot.util.CuriosBackSlotHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
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
            LivingEntity player = context.getSender();

            if (player != null) {
                CuriosBackSlotHandler.ServerCuriosBackSlotHandler backSlotHandler = new CuriosBackSlotHandler.ServerCuriosBackSlotHandler(player);
                ItemStack handStack = player.getItemBySlot(EquipmentSlot.MAINHAND);
                ItemStack backStack = backSlotHandler.getStackInSlot();

                if (backStack.isEmpty() && handStack.isEmpty()) return;
                if (!CuriosBackSlot.canEquip(handStack.getItem())) return;

                player.setItemSlot(EquipmentSlot.MAINHAND, backStack);
                backSlotHandler.setStackInSlot(handStack);

                player.getLevel().playSound(null, player.blockPosition(), SoundEvents.ARMOR_EQUIP_ELYTRA, SoundSource.AMBIENT, 1, 1);

            }

        });
        return true;
    }
}
