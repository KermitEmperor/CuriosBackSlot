package com.kermitemperor.curiosbackslot.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

public class CuriosBackSlotHandler {

    private static final Minecraft mc = Minecraft.getInstance();
    public static final String SLOT_ID = "back_weapon";

    private static ICurioStacksHandler getCurioStackHandler(Player player) {
        ICuriosItemHandler curiosItemHandler = CuriosApi.getCuriosHelper().getCuriosHandler(player).orElseThrow(NullPointerException::new);
        return curiosItemHandler.getStacksHandler(SLOT_ID).orElseThrow();
    }

    @OnlyIn(Dist.CLIENT)
    public static ItemStack getStackInSlotClient() {
        ICurioStacksHandler curioStacksHandler = getCurioStackHandler(mc.player);
        return curioStacksHandler.getStacks().getStackInSlot(0);
    }

    public static boolean renderItemInSlot(Player player) {
        return getCurioStackHandler(player).getRenders().get(0);
    }

    public static ItemStack getStackInSlot(Player player) {
        return getCurioStackHandler(player).getStacks().getStackInSlot(0);
    }

    public static void setStackInSlot(Player player, ItemStack stack) {
        getCurioStackHandler(player).getStacks().setStackInSlot(0, stack);
    }


}
